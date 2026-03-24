package datacoders.dao;

// ESTO ES LO QUE TE FALTA:
import datacoders.modelo.Pedido;
import datacoders.modelo.Cliente;
import datacoders.modelo.Articulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao implements PedidoDAOInterface {

    // Llamamos al procedimiento sp_crear_pedido que creamos en MySQL
    public void crearPedido(int idCliente, String codigoArticulo, int cantidad) throws SQLException {
        String sql = "{CALL sp_crear_pedido(?, ?, ?)}";
        try (Connection con = Conexion.obtenerConexion();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idCliente);
            cs.setString(2, codigoArticulo);
            cs.setInt(3, cantidad);
            cs.execute();
        }
    }

    // Llamamos al procedimiento de eliminar
    public void eliminarPedido(int numeroPedido) throws SQLException {
        String sql = "{CALL sp_eliminar_pedido(?)}";
        try (Connection con = Conexion.obtenerConexion();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, numeroPedido);
            cs.execute();
        }
    }

    // =========================================================================
    // MÉTODOS PARA LISTAR (Añadidos para que el Controlador no de error)
    // =========================================================================

    public List<Pedido> listarPendientes(String email) throws SQLException {
        return listarPorEstado(email, false); // false = pendiente
    }

    public List<Pedido> listarEnviados(String email) throws SQLException {
        return listarPorEstado(email, true); // true = enviado
    }

    // Método privado auxiliar para no repetir código (DRY)
    private List<Pedido> listarPorEstado(String email, boolean enviado) throws SQLException {
        List<Pedido> lista = new ArrayList<>();

        // Query que une pedido con cliente y articulo para poder crear los objetos
        String sql = "SELECT p.*, c.nombre, c.email as cli_email, c.es_premium, " +
                "a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion " +
                "FROM pedidos p " +
                "JOIN clientes c ON p.id_cliente = c.id_cliente " +
                "JOIN articulos a ON p.codigo_articulo = a.codigo_articulo " +
                "WHERE c.email = ? AND p.esta_enviado = ?";

        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setBoolean(2, enviado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Creamos los objetos necesarios para el constructor de Pedido
                    Cliente cli = new Cliente(rs.getString("nombre"), "", "", rs.getString("cli_email"), rs.getBoolean("es_premium"));
                    Articulo art = new Articulo(rs.getString("codigo_articulo"), rs.getString("descripcion"),
                            rs.getDouble("precio_venta"), rs.getDouble("gastos_envio"),
                            rs.getInt("tiempo_preparacion"));

                    Pedido p = new Pedido(
                            rs.getInt("numero_pedido"),
                            cli,
                            art,
                            rs.getInt("cantidad"),
                            rs.getTimestamp("fecha_hora").toLocalDateTime()
                    );
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}