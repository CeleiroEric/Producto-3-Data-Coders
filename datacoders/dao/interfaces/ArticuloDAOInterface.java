package datacoders.dao.interfaces;
import datacoders.modelo.Articulo;
import java.sql.SQLException;
import java.util.List;

public interface ArticuloDAOInterface {
    void insertar(Articulo articulo) throws SQLException;
    List<Articulo> listarTodos();
    Articulo buscarPorCodigo(String codigo);
}