package datacoders.dao.hibernate;

import datacoders.dao.ArticuloDao;
import datacoders.modelo.Articulo;
import jakarta.persistence.*;
import java.util.List;

public class ArticuloDAOHibernate implements ArticuloDao {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public boolean insert(Articulo articulo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(articulo);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Articulo findByCodigo(String codigo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Articulo.class, codigo);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Articulo> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM Articulo", Articulo.class).getResultList();
        } finally {
            em.close();
        }
    }
}