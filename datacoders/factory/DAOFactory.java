package datacoders.factory;

import datacoders.dao.ArticuloDao;
import datacoders.dao.ClienteDao;
import datacoders.dao.PedidoDao;

public abstract class DAOFactory {

    public static final int MYSQL = 1;     // Tu código antiguo de JDBC
    public static final int HIBERNATE = 2; // El nuevo código de Hibernate

    public static DAOFactory getFactory(int tipo) {
        switch (tipo) {
            case MYSQL:
                return new MySqlDAOFactory();
            case HIBERNATE:
                // Esta es la clase que deberás crear ahora (HibernateDAOFactory)
                return new HibernateDAOFactory();
            default:
                throw new IllegalArgumentException("Tipo de factoría no soportado: " + tipo);
        }
    }

    public abstract ClienteDao getClienteDAO();
    public abstract ArticuloDao getArticuloDAO();
    public abstract PedidoDao getPedidoDAO();
}