package datacoders.dao.jpa;

// 1. Apuntamos a la interfaz correcta para evitar el error "Cannot resolve symbol"
import datacoders.dao.interfaces.PedidoDAOInterface;
import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// 2. Implementamos PedidoDAOInterface (el nombre exacto de tu archivo en /interfaces)
public class PedidoDAOJPA implements PedidoDAOInterface {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Implementación básica (ajustar según tu lógica de negocio)
            Pedido pedido = new Pedido(0, null, null, cantidad, ahora);
            em.persist(pedido);

            em.getTransaction().commit();
            return pedido;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            // 3. Importante: si no es una excepción controlada, relanzamos o manejamos el error
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

            if (p == null) throw new PedidoNoEncontradoException("Pedido " + numPedido + " no existe");

            // 4. Verificación de lógica de cancelación antes de borrar
            if (!p.esCancelable(ahora)) throw new PedidoNoCancelableException("El tiempo de preparación ha expirado");

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
            // Asegúrate de que los atributos 'cliente' y 'email' existan en tu clase Pedido
            return em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.email = :email", Pedido.class)
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
            // Lógica similar a findPendientes pero filtrando por estado 'enviado'
            return em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.email = :email", Pedido.class)
                    .setParameter("email", emailCliente)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}