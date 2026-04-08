package datacoders.dao.mysql;

import datacoders.dao.PedidoDao;
import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;
import datacoders.modelo.util.BDConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySqlPedidoDAO implements PedidoDao {

    @Override
    public Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo,
                              int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {

        // NOTA:
        // Aunque la firma recibe "ahora", la implementación actual usa NOW() en MySQL
        // porque el procedimiento almacenado sp_crear_pedido inserta la fecha directamente
        // en base de datos. Si más adelante queremos respetar "ahora" desde Java,
        // habrá que modificar el procedimiento almacenado y su llamada.

        // El procedimiento almacenado actual no crea cliente si no existe.
        // Por eso aquí se comprueba y, si falta, se inserta antes de llamar al procedimiento.
        asegurarClienteExiste(emailCliente, datosCliente);
        asegurarArticuloExiste(codigoArticulo);

        String call = "{CALL sp_crear_pedido(?, ?, ?)}";

        try (Connection con = BDConnection.getConnection();
             CallableStatement cs = con.prepareCall(call)) {

            cs.setString(1, emailCliente);
            cs.setString(2, codigoArticulo);
            cs.setInt(3, cantidad);

            cs.execute();

            int ultimoId = getLastInsertId(con);
            return buscarPedidoPorNumero(con, ultimoId);

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {

        // NOTA:
        // Igual que en crearPedido, el parámetro "ahora" no se usa actualmente porque
        // la lógica temporal está delegada a MySQL mediante NOW() en el procedimiento
        // almacenado sp_eliminar_pedido.

        comprobarPedidoExiste(numPedido);

        String call = "{CALL sp_eliminar_pedido(?)}";

        try (Connection con = BDConnection.getConnection();
             CallableStatement cs = con.prepareCall(call)) {

            cs.setInt(1, numPedido);
            cs.execute();
            return true;

        } catch (SQLException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();

            if (msg.contains("no se puede cancelar")) {
                throw new PedidoNoCancelableException(
                        "El pedido no se puede cancelar: ya está en preparación o enviado."
                );
            }

            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pedido> findPendientes(String emailCliente) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.num_pedido, p.cantidad, p.fecha_hora,
                    c.nombre, c.domicilio, c.nif, c.email, c.tipo,
                    a.codigo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion_min
                FROM pedidos p
                JOIN clientes c ON p.id_cliente = c.id_cliente
                JOIN articulos a ON p.id_articulo = a.id_articulo
                WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) < a.tiempo_preparacion_min
                """);

        boolean filtrar = emailCliente != null && !emailCliente.isBlank();
        if (filtrar) {
            sql.append(" AND c.email = ?");
        }
        sql.append(" ORDER BY p.num_pedido");

        return ejecutarListadoPedidos(sql.toString(), filtrar ? emailCliente : null);
    }

    @Override
    public List<Pedido> findEnviados(String emailCliente) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.num_pedido, p.cantidad, p.fecha_hora,
                    c.nombre, c.domicilio, c.nif, c.email, c.tipo,
                    a.codigo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion_min
                FROM pedidos p
                JOIN clientes c ON p.id_cliente = c.id_cliente
                JOIN articulos a ON p.id_articulo = a.id_articulo
                WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) >= a.tiempo_preparacion_min
                """);

        boolean filtrar = emailCliente != null && !emailCliente.isBlank();
        if (filtrar) {
            sql.append(" AND c.email = ?");
        }
        sql.append(" ORDER BY p.num_pedido");

        return ejecutarListadoPedidos(sql.toString(), filtrar ? emailCliente : null);
    }

    private List<Pedido> ejecutarListadoPedidos(String sql, String email) {
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (email != null) {
                ps.setString(1, email);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapPedido(rs));
                }
            }

            return pedidos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage(), e);
        }
    }

    private Pedido mapPedido(ResultSet rs) throws SQLException {
        Cliente cliente;
        String tipo = rs.getString("tipo");

        if ("Premium".equalsIgnoreCase(tipo)) {
            cliente = new ClientePremium(
                    rs.getString("nombre"),
                    rs.getString("domicilio"),
                    rs.getString("nif"),
                    rs.getString("email")
            );
        } else {
            cliente = new ClienteEstandar(
                    rs.getString("nombre"),
                    rs.getString("domicilio"),
                    rs.getString("nif"),
                    rs.getString("email")
            );
        }

        Articulo articulo = new Articulo(
                rs.getString("codigo"),
                rs.getString("descripcion"),
                rs.getDouble("precio_venta"),
                rs.getDouble("gastos_envio"),
                rs.getInt("tiempo_preparacion_min")
        );

        Timestamp ts = rs.getTimestamp("fecha_hora");
        LocalDateTime fechaHora = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();

        return new Pedido(
                rs.getInt("num_pedido"),
                cliente,
                articulo,
                rs.getInt("cantidad"),
                fechaHora
        );
    }

    private void asegurarArticuloExiste(String codigoArticulo) throws ArticuloNoEncontradoException {
        String sql = "SELECT id_articulo FROM articulos WHERE codigo = ?";

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoArticulo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new ArticuloNoEncontradoException(
                            "No existe artículo con código: " + codigoArticulo
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando artículo: " + e.getMessage(), e);
        }
    }

    private void asegurarClienteExiste(String emailCliente, String datosCliente) throws DuplicadoException {
        String checkSql = "SELECT id_cliente FROM clientes WHERE email = ?";

        try (Connection con = BDConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setString(1, emailCliente);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    return; // ya existe
                }
            }

            // Si no existe, se crea a partir de datosCliente: nombre|domicilio|nif|tipo
            String[] partes = parseDatosCliente(datosCliente);
            String nombre = partes[0];
            String domicilio = partes[1];
            String nif = partes[2];
            String tipo = partes[3];

            MySqlClienteDAO clienteDAO = new MySqlClienteDAO();
            if ("premium".equalsIgnoreCase(tipo)) {
                clienteDAO.insertPremium(nombre, domicilio, nif, emailCliente);
            } else {
                clienteDAO.insertEstandar(nombre, domicilio, nif, emailCliente);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando/creando cliente: " + e.getMessage(), e);
        }
    }

    private String[] parseDatosCliente(String datosCliente) {
        // Formato esperado: nombre|domicilio|nif|tipo
        String nombre = "N/D";
        String domicilio = "N/D";
        String nif = "N/D";
        String tipo = "Estandar";

        if (datosCliente != null && !datosCliente.isBlank()) {
            String[] parts = datosCliente.split("\\|");
            if (parts.length > 0 && !parts[0].isBlank()) nombre = parts[0].trim();
            if (parts.length > 1 && !parts[1].isBlank()) domicilio = parts[1].trim();
            if (parts.length > 2 && !parts[2].isBlank()) nif = parts[2].trim();
            if (parts.length > 3 && !parts[3].isBlank()) tipo = parts[3].trim();
        }

        return new String[]{nombre, domicilio, nif, tipo};
    }

    private int getLastInsertId(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT LAST_INSERT_ID()");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo recuperar LAST_INSERT_ID()");
        }
    }

    private Pedido buscarPedidoPorNumero(Connection con, int numPedido) throws SQLException {
        String sql = """
                SELECT
                    p.num_pedido, p.cantidad, p.fecha_hora,
                    c.nombre, c.domicilio, c.nif, c.email, c.tipo,
                    a.codigo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion_min
                FROM pedidos p
                JOIN clientes c ON p.id_cliente = c.id_cliente
                JOIN articulos a ON p.id_articulo = a.id_articulo
                WHERE p.num_pedido = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No se pudo recuperar el pedido insertado");
                }
                return mapPedido(rs);
            }
        }
    }

    private void comprobarPedidoExiste(int numPedido) throws PedidoNoEncontradoException {
        String sql = "SELECT num_pedido FROM pedidos WHERE num_pedido = ?";

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new PedidoNoEncontradoException(
                            "No existe pedido con número: " + numPedido
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando pedido: " + e.getMessage(), e);
        }
    }
}