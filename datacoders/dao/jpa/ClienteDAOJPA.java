package datacoders.dao.jpa;

import datacoders.dao.interfaces.ClienteDAOInterface;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import jakarta.persistence.*;
import java.sql.SQLException;
import java.util.List;

public class ClienteDAOJPA implements ClienteDAOInterface {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanzamos SQLException para que la capa de Datos.java la capture como DuplicadoException
            throw new SQLException("Error al insertar cliente en JPA: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Cliente> listarEstandar() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM ClienteEstandar c", Cliente.class).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Cliente> listarPremium() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM ClientePremium c", Cliente.class).getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            // Buscamos directamente por la clave primaria (email)
            return em.find(Cliente.class, email);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}