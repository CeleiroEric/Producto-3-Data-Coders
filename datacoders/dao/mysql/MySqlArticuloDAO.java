package datacoders.dao.mysql;

import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.modelo.Articulo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlArticuloDAO implements ArticuloDAOInterface {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_pedidos";
    private static final String USER = "root";
    private static final String PASS = "rootsql1.26";

    @Override
    public void insertar(Articulo a) throws SQLException {
        // CORRECCIÓN: He cambiado los nombres de las columnas para que coincidan con MySQL
        String sql = "INSERT INTO articulos (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getCodigo());
            ps.setString(2, a.getDescripcion());
            ps.setDouble(3, a.getPrecioVenta());
            ps.setDouble(4, a.getGastosEnvio());
            ps.setInt(5, a.getTiempoPreparacionMin());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Articulo> listarTodos() {
        List<Articulo> lista = new ArrayList<>();
        // CORRECCIÓN: Usamos los nombres exactos de tu BD
        String sql = "SELECT codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion FROM articulos";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion FROM articulos WHERE codigo = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Articulo(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio_venta"),
                            rs.getDouble("gastos_envio"),
                            rs.getInt("tiempo_preparacion")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}