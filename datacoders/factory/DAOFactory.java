package datacoders.factory;

// IMPORTANTE: Importamos las interfaces oficiales
import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.dao.interfaces.ClienteDAOInterface;
import datacoders.dao.interfaces.PedidoDAOInterface;

public abstract class DAOFactory {

    public static final int MYSQL = 1;
    public static final int HIBERNATE = 2;

    public static DAOFactory getFactory(int tipo) {
        switch (tipo) {
            case MYSQL:
                return new MySqlDAOFactory();
            case HIBERNATE:
                return new HibernateDAOFactory();
            default:
                throw new IllegalArgumentException("Tipo de factoría no soportado: " + tipo);
        }
    }

    // Cambiamos los retornos para que usen las Interfaces
    // Esto permite que el factory devuelva tanto MySql como Hibernate sin errores
    public abstract ClienteDAOInterface getClienteDAO();
    public abstract ArticuloDAOInterface getArticuloDAO();
    public abstract PedidoDAOInterface getPedidoDAO();
}