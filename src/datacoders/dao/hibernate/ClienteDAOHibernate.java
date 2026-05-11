package datacoders.dao.hibernate;

import datacoders.dao.interfaces.ClienteDAOInterface;
import datacoders.modelo.Cliente;
import datacoders.modelo.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class ClienteDAOHibernate implements ClienteDAOInterface {

    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error al insertar cliente en Hibernate: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Cliente", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarEstandar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c WHERE TYPE(c) = datacoders.modelo.ClienteEstandar",
                    Cliente.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarPremium() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c WHERE TYPE(c) = datacoders.modelo.ClientePremium",
                    Cliente.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cliente.class, email);
        } finally {
            em.close();
        }
    }
}