package datacoders.dao;

import datacoders.modelo.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAOInterface {
    void insertar(Cliente cliente) throws SQLException;
    List<Cliente> listar() throws SQLException;
    boolean existeEmail(String email) throws SQLException;
}