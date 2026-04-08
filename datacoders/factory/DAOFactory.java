package datacoders.factory;

import datacoders.dao.ArticuloDao;
import datacoders.dao.ClienteDao;
import datacoders.dao.PedidoDao;

public abstract class DAOFactory {

    public static final int MYSQL = 1;

    public static DAOFactory getFactory(int tipo) {
        if (tipo == MYSQL) {
            return new MySqlDAOFactory();
        }
        throw new IllegalArgumentException("Tipo de factoría no soportado: " + tipo);
    }

    public abstract ClienteDao getClienteDAO();
    public abstract ArticuloDao getArticuloDAO();
    public abstract PedidoDao getPedidoDAO();
}