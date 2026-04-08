package datacoders.dao.interfaces;

import datacoders.modelo.Articulo;
import java.sql.SQLException;
import java.util.List;

public interface ArticuloDAOInterface {
    void insertar(Articulo articulo) throws SQLException;
    List<Articulo> listar() throws SQLException;
    // Aquí irían borrar, buscar, etc.
}