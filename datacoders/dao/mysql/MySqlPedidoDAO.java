package datacoders.dao.mysql;

import datacoders.dao.interfaces.PedidoDAOInterface;
import datacoders.modelo.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySqlPedidoDAO implements PedidoDAOInterface {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_pedidos";
    private static final String USER = "root";
    private static final String PASS = "rootsql1.26";

    @Override
    public Pedido crearPedido(String email, String datos, String codigo, int cant, LocalDateTime ahora) {
        String sql = "INSERT INTO pedidos (cantidad, fecha_hora, articulo_codigo, cliente_email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cant);
            ps.setTimestamp(2, Timestamp.valueOf(ahora));
            ps.setString(3, codigo);
            ps.setString(4, email);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    // Para que la UI se actualice, necesitamos recuperar el objeto completo
                    return buscarPorId(id);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Pedido> findPendientes(String email) {
        // Si no tienes columna 'estado', simplemente listamos todos los del cliente
        return listarPedidos("SELECT * FROM pedidos WHERE cliente_email = ?", email);
    }

    @Override
    public List<Pedido> findEnviados(String email) {
        // Ajustar esta consulta si añades una columna 'enviado' (1 o 0) en el futuro
        return new ArrayList<>();
    }

    private List<Pedido> listarPedidos(String sql, String filtroEmail) {
        List<Pedido> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, filtroEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // 1. Necesitamos el Cliente y el Artículo para crear el objeto Pedido
                    Cliente c = new MySqlClienteDAO().buscarPorEmail(rs.getString("cliente_email"));
                    Articulo a = new MySqlArticuloDAO().buscarPorCodigo(rs.getString("articulo_codigo"));

                    if (c != null && a != null) {
                        Pedido p = new Pedido(
                                rs.getInt("num_pedido"),
                                c,
                                a,
                                rs.getInt("cantidad"),
                                rs.getTimestamp("fecha_hora").toLocalDateTime()
                        );
                        lista.add(p);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Método auxiliar para recuperar un pedido recién creado
    private Pedido buscarPorId(int id) {
        String sql = "SELECT * FROM pedidos WHERE num_pedido = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new MySqlClienteDAO().buscarPorEmail(rs.getString("cliente_email"));
                    Articulo a = new MySqlArticuloDAO().buscarPorCodigo(rs.getString("articulo_codigo"));
                    return new Pedido(id, c, a, rs.getInt("cantidad"), rs.getTimestamp("fecha_hora").toLocalDateTime());
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean eliminarPedido(int num, LocalDateTime ahora) {
        String sql = "DELETE FROM pedidos WHERE num_pedido = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, num);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}