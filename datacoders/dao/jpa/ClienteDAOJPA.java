package datacoders.dao.jpa;

import datacoders.dao.ClienteDao;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import datacoders.modelo.excepciones.ClienteNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import jakarta.persistence.*;
import java.util.List;

public class ClienteDAOJPA implements ClienteDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public boolean insertEstandar(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Verificamos si ya existe.
            // IMPORTANTE: He añadido un mensaje al constructor para solucionar tu error de "cannot be applied to given types"
            if (em.find(Cliente.class, email) != null) {
                throw new DuplicadoException("El cliente con email " + email + " ya existe.");
            }

            em.persist(new ClienteEstandar(nombre, domicilio, nif, email));
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean insertPremium(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Añadido mensaje al constructor
            if (em.find(Cliente.class, email) != null) {
                throw new DuplicadoException("El cliente con email " + email + " ya existe.");
            }

            em.persist(new ClientePremium(nombre, domicilio, nif, email));
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente findByEmail(String email) throws ClienteNoEncontradoException {
        EntityManager em = emf.createEntityManager();
        try {
            Cliente c = em.find(Cliente.class, email);
            // Añadido mensaje al constructor
            if (c == null) {
                throw new ClienteNoEncontradoException("No se ha encontrado ningún cliente con el email: " + email);
            }
            return c;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> findAllEstandar() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM ClienteEstandar c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> findAllPremium() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM ClientePremium c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }
}