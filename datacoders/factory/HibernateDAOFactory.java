package datacoders.factory;

import datacoders.dao.ArticuloDao;
import datacoders.dao.ClienteDao;
import datacoders.dao.PedidoDao;
// Importamos las clases que acabas de crear en la carpeta hibernate
import datacoders.dao.hibernate.ArticuloDAOHibernate;
import datacoders.dao.hibernate.ClienteDAOHibernate;
import datacoders.dao.hibernate.PedidoDAOHibernate;

public class HibernateDAOFactory extends DAOFactory {

    @Override
    public ClienteDao getClienteDAO() {
        // Devolvemos la implementación real de Hibernate
        return new ClienteDAOHibernate();
    }

    @Override
    public ArticuloDao getArticuloDAO() {
        // Devolvemos la implementación real de Hibernate
        return new ArticuloDAOHibernate();
    }

    @Override
    public PedidoDao getPedidoDAO() {
        // Devolvemos la implementación real de Hibernate
        return new PedidoDAOHibernate();
    }
}