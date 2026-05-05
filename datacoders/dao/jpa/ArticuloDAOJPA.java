package datacoders.dao.jpa;

import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.modelo.Articulo;
import jakarta.persistence.*;
import java.sql.SQLException; // Añadido para cumplir con la firma de la interfaz
import java.util.List;

public class ArticuloDAOJPA implements ArticuloDAOInterface {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public void insertar(Articulo articulo) throws SQLException { // Cambiado de 'insert' a 'insertar'
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(articulo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            // Lanzamos SQLException para que Datos.java pueda capturarla
            throw new SQLException("Error al insertar artículo en JPA: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) { // Cambiado de 'findByCodigo' a 'buscarPorCodigo'
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Articulo.class, codigo);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Articulo> listarTodos() { // Cambiado de 'findAll' a 'listarTodos'
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM Articulo", Articulo.class).getResultList();
        } finally {
            em.close();
        }
    }
}