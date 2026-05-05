package datacoders.factory;

// 1. IMPORTANTE: Importamos las interfaces que tus DAOs de Hibernate implementan
import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.dao.interfaces.ClienteDAOInterface;
import datacoders.dao.interfaces.PedidoDAOInterface;

// Importamos tus implementaciones reales de la carpeta hibernate
import datacoders.dao.hibernate.ArticuloDAOHibernate;
import datacoders.dao.hibernate.ClienteDAOHibernate;
import datacoders.dao.hibernate.PedidoDAOHibernate;

public class HibernateDAOFactory extends DAOFactory {

    @Override
    public ClienteDAOInterface getClienteDAO() {
        // Devolvemos la implementación de Hibernate
        // Asegúrate de que ClienteDAOHibernate implemente ClienteDAOInterface
        return new ClienteDAOHibernate();
    }

    @Override
    public ArticuloDAOInterface getArticuloDAO() {
        return new ArticuloDAOHibernate();
    }

    @Override
    public PedidoDAOInterface getPedidoDAO() {
        return new PedidoDAOHibernate();
    }
}