package datacoders.dao.hibernate;

import datacoders.dao.PedidoDao;
import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.Pedido;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDAOHibernate implements PedidoDao {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, emailCliente);
            Articulo articulo = em.find(Articulo.class, codigoArticulo);

            // Creamos el pedido (el 0 es porque el ID es autoincremental)
            Pedido nuevo = new Pedido(0, cliente, articulo, cantidad, ahora);
            em.persist(nuevo);

            em.getTransaction().commit();
            return nuevo;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return null;
        } finally { em.close(); }
    }

    @Override
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pedido p = em.find(Pedido.class, numPedido);
            if (p != null && p.esCancelable(ahora)) {
                em.remove(p);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } finally { em.close(); }
    }

    @Override
    public List<Pedido> findPendientes(String email) {
        EntityManager em = emf.createEntityManager();
        String jpql = "FROM Pedido p WHERE p.cliente.email = :email";
        List<Pedido> lista = em.createQuery(jpql, Pedido.class)
                .setParameter("email", email)
                .getResultList();
        em.close();
        return lista;
    }

    @Override
    public List<Pedido> findEnviados(String email) {
        // Aquí iría tu lógica de filtrado por fecha si la tienes
        return List.of();
    }
}