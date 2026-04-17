package datacoders.dao.jpa;

import datacoders.dao.PedidoDao;
import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDAOJPA implements PedidoDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Aquí deberías instanciar tu pedido. Ejemplo:
            // Pedido pedido = new Pedido(emailCliente, datosCliente, codigoArticulo, cantidad, ahora);
            // em.persist(pedido);

            em.getTransaction().commit();
            return null; // Cambia 'null' por el objeto 'pedido' cuando lo tengas creado
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pedido p = em.find(Pedido.class, numPedido);

            if (p == null) throw new PedidoNoEncontradoException("Pedido no existe");

            // Lógica para borrar
            em.remove(p);

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
    public List<Pedido> findPendientes(String emailCliente) {
        EntityManager em = emf.createEntityManager();
        try {
            // Ajusta los nombres de los campos (email, enviado) según tu clase Pedido
            return em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.email = :email AND p.enviado = false", Pedido.class)
                    .setParameter("email", emailCliente)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> findEnviados(String emailCliente) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.email = :email AND p.enviado = true", Pedido.class)
                    .setParameter("email", emailCliente)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}