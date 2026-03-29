package datacoders.dao;

import datacoders.dao.interfaces.*;
import datacoders.dao.*;

public class DAOFactory {

    // Devolvemos la interfaz, pero instanciamos la clase concreta
    public static ArticuloDAOInterface getArticuloDAO() {
        return new ArticuloDao();
    }

    public static ClienteDAOInterface getClienteDAO() {
        return new ClienteDao();
    }

    public static datacoders.dao.PedidoDAOInterface getPedidoDAO() {
        return new PedidoDao();
    }
}