package datacoders.dao.hibernate;

import datacoders.dao.ClienteDao;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import jakarta.persistence.*;
import java.util.List;

public class ClienteDAOHibernate implements ClienteDao {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public boolean insertEstandar(String nombre, String domicilio, String nif, String email) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new ClienteEstandar(nombre, domicilio, nif, email));
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally { em.close(); }
    }

    @Override
    public boolean insertPremium(String nombre, String domicilio, String nif, String email) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new ClientePremium(nombre, domicilio, nif, email));
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally { em.close(); }
    }

    @Override
    public Cliente findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        Cliente c = em.find(Cliente.class, email);
        em.close();
        return c;
    }

    @Override
    public List<Cliente> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Cliente> lista = em.createQuery("FROM Cliente", Cliente.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public List<Cliente> findAllEstandar() {
        EntityManager em = emf.createEntityManager();
        List<Cliente> lista = em.createQuery("FROM ClienteEstandar", Cliente.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public List<Cliente> findAllPremium() {
        EntityManager em = emf.createEntityManager();
        List<Cliente> lista = em.createQuery("FROM ClientePremium", Cliente.class).getResultList();
        em.close();
        return lista;
    }
}