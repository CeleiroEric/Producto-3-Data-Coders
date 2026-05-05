package datacoders.dao.hibernate;

import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.modelo.Articulo;
import jakarta.persistence.*;
import java.sql.SQLException;
import java.util.List;

public class ArticuloDAOHibernate implements ArticuloDAOInterface {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public void insertar(Articulo articulo) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(articulo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new SQLException("Error en Hibernate: " + e.getMessage());
        } finally { em.close(); }
    }

    @Override
    public List<Articulo> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Articulo> lista = em.createQuery("FROM Articulo", Articulo.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) {
        EntityManager em = emf.createEntityManager();
        Articulo a = em.find(Articulo.class, codigo);
        em.close();
        return a;
    }
}