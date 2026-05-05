package datacoders.dao.hibernate;

import datacoders.dao.interfaces.ClienteDAOInterface;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import jakarta.persistence.*;
import java.sql.SQLException;
import java.util.List;

public class ClienteDAOHibernate implements ClienteDAOInterface {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new SQLException("Error al insertar cliente en Hibernate: " + e.getMessage());
        } finally { em.close(); }
    }

    @Override
    public List<Cliente> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Cliente> lista = em.createQuery("FROM Cliente", Cliente.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public List<Cliente> listarEstandar() {
        EntityManager em = emf.createEntityManager();
        List<Cliente> lista = em.createQuery("FROM ClienteEstandar", Cliente.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public List<Cliente> listarPremium() {
        EntityManager em = emf.createEntityManager();
        List<Cliente> lista = em.createQuery("FROM ClientePremium", Cliente.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        EntityManager em = emf.createEntityManager();
        Cliente c = em.find(Cliente.class, email);
        em.close();
        return c;
    }
}