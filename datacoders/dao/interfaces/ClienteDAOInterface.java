package datacoders.dao.interfaces;
import datacoders.modelo.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAOInterface {
    boolean insertar(Cliente cliente) throws SQLException;
    List<Cliente> listarTodos();
    List<Cliente> listarEstandar();
    List<Cliente> listarPremium();
    Cliente buscarPorEmail(String email);
}