package datacoders.dao.hibernate;

import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.modelo.Articulo;
import datacoders.modelo.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public class ArticuloDAOHibernate implements ArticuloDAOInterface {

    @Override
    public void insertar(Articulo articulo) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(articulo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error en Hibernate: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Articulo> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Articulo", Articulo.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Articulo.class, codigo);
        } finally {
            em.close();
        }
    }
}