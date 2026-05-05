package datacoders.dao.mysql;

import datacoders.dao.interfaces.ClienteDAOInterface;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlClienteDAO implements ClienteDAOInterface {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_pedidos";
    private static final String USER = "root";
    private static final String PASS = "rootsql1.26";

    @Override
    public boolean insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, domicilio, nif, email, tipo, cuota_anual, descuento_envio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDomicilio());
            ps.setString(3, c.getNif());
            ps.setString(4, c.getEmail());
            if (c instanceof ClientePremium) {
                ps.setString(5, "Premium");
                ps.setDouble(6, ((ClientePremium) c).getCuotaAnual());
                ps.setDouble(7, ((ClientePremium) c).getDescuentoEnvio());
            } else {
                ps.setString(5, "Estandar");
                ps.setDouble(6, 0.0);
                ps.setDouble(7, 0.0);
            }
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(crearClienteDesdeRS(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        String sql = "SELECT * FROM clientes WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return crearClienteDesdeRS(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Métodos adicionales que suele pedir la interfaz
    @Override
    public List<Cliente> listarEstandar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE tipo = 'Estandar'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(crearClienteDesdeRS(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public List<Cliente> listarPremium() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE tipo = 'Premium'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(crearClienteDesdeRS(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Método auxiliar para no repetir código
    private Cliente crearClienteDesdeRS(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        if ("Premium".equalsIgnoreCase(tipo)) {
            return new ClientePremium(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), rs.getString("email"));
        } else {
            return new ClienteEstandar(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), rs.getString("email"));
        }
    }
}