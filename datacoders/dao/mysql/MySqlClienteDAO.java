package datacoders.dao.mysql;

import datacoders.dao.ClienteDao;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import datacoders.modelo.excepciones.ClienteNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.util.BDConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlClienteDAO implements ClienteDao {

    @Override
    public boolean insertEstandar(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        return insertCliente(nombre, domicilio, nif, email, "Estandard", 0.0, 0.0);
    }

    @Override
    public boolean insertPremium(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        return insertCliente(nombre, domicilio, nif, email, "Premium", 30.0, 0.20);
    }

    private boolean insertCliente(String nombre, String domicilio, String nif, String email,
                                  String tipo, double cuotaAnual, double descuentoEnvio)
            throws DuplicadoException {

        String sql = """
                INSERT INTO clientes (nombre, domicilio, nif, email, tipo, cuota_anual, descuento_envio)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, domicilio);
            ps.setString(3, nif);
            ps.setString(4, email);
            ps.setString(5, tipo);
            ps.setDouble(6, cuotaAnual);
            ps.setDouble(7, descuentoEnvio);

            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicadoException("Ya existe un cliente con email: " + email);
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente findByEmail(String email) throws ClienteNoEncontradoException {
        String sql = """
                SELECT nombre, domicilio, nif, email, tipo
                FROM clientes
                WHERE email = ?
                """;

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new ClienteNoEncontradoException("No existe cliente con email: " + email);
                }
                return mapCliente(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> findAll() {
        String sql = """
                SELECT nombre, domicilio, nif, email, tipo
                FROM clientes
                ORDER BY email
                """;

        return ejecutarListado(sql, null);
    }

    @Override
    public List<Cliente> findAllEstandar() {
        String sql = """
                SELECT nombre, domicilio, nif, email, tipo
                FROM clientes
                WHERE tipo = ?
                ORDER BY email
                """;

        return ejecutarListado(sql, "Estandar");
    }

    @Override
    public List<Cliente> findAllPremium() {
        String sql = """
                SELECT nombre, domicilio, nif, email, tipo
                FROM clientes
                WHERE tipo = ?
                ORDER BY email
                """;

        return ejecutarListado(sql, "Premium");
    }

    private List<Cliente> ejecutarListado(String sql, String tipo) {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (tipo != null) {
                ps.setString(1, tipo);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapCliente(rs));
                }
            }

            return clientes;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes: " + e.getMessage(), e);
        }
    }

    private Cliente mapCliente(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");

        if ("PREMIUM".equalsIgnoreCase(tipo)) {
            return new ClientePremium(
                    rs.getString("nombre"),
                    rs.getString("domicilio"),
                    rs.getString("nif"),
                    rs.getString("email")
            );
        }

        return new ClienteEstandar(
                rs.getString("nombre"),
                rs.getString("domicilio"),
                rs.getString("nif"),
                rs.getString("email")
        );
    }
}