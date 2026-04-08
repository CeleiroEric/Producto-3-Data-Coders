package datacoders.dao.mysql;

import datacoders.dao.ArticuloDao;
import datacoders.modelo.Articulo;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.util.BDConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlArticuloDAO implements ArticuloDao {

    @Override
    public boolean insert(Articulo a) throws DuplicadoException {
        String sql = """
                INSERT INTO articulos (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getCodigo());
            ps.setString(2, a.getDescripcion());
            ps.setDouble(3, a.getPrecioVenta());
            ps.setDouble(4, a.getGastosEnvio());
            ps.setInt(5, a.getTiempoPreparacionMin());

            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicadoException("Ya existe un artículo con código: " + a.getCodigo());
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar artículo: " + e.getMessage(), e);
        }
    }

    @Override
    public Articulo findByCodigo(String codigo) throws ArticuloNoEncontradoException {
        String sql = """
                SELECT codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min
                FROM articulos
                WHERE codigo = ?
                """;

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new ArticuloNoEncontradoException("No existe artículo con código: " + codigo);
                }

                return new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion_min")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar artículo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Articulo> findAll() {
        String sql = """
                SELECT codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion_min
                FROM articulos
                ORDER BY codigo
                """;

        List<Articulo> articulos = new ArrayList<>();

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                articulos.add(new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion_min")
                ));
            }

            return articulos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar artículos: " + e.getMessage(), e);
        }
    }
}