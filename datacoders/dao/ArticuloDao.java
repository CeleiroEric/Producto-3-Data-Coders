
package datacoders.dao;

import datacoders.modelo.Articulo;
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class ArticuloDao implements ArticuloDAOInterface {

    public void insertar(Articulo articulo) throws SQLException {
        String sql = "INSERT INTO articulos (codigo_articulo, descripcion, precio_venta, gastos_envio, tiempo_preparacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3, articulo.getPrecioVenta());
            ps.setDouble(4, articulo.getGastosEnvio());
            ps.setInt(5, articulo.getTiempoPreparacionMin());
            ps.executeUpdate();
        }
    }

    public List<Articulo> listar() throws SQLException {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM articulos";
        try (Connection con = Conexion.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Articulo(
                        rs.getString("codigo_articulo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                ));
            }
        }
        return lista;
    }
}