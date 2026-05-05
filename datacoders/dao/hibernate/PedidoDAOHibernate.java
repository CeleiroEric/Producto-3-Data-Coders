package datacoders.dao.hibernate;

// 1. IMPORTANTE: Importamos la interfaz correcta de la carpeta interfaces
import datacoders.dao.interfaces.PedidoDAOInterface;
import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.*; // Añadido para las excepciones de la interfaz
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// 2. Cambiamos 'implements PedidoDao' por 'implements PedidoDAOInterface'
public class PedidoDAOHibernate implements PedidoDAOInterface {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistenciaDataCoders");

    @Override
    public Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException { // 3. Añadimos los throws obligatorios

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, emailCliente);
            Articulo articulo = em.find(Articulo.class, codigoArticulo);

            if (articulo == null) throw new ArticuloNoEncontradoException(codigoArticulo);

            Pedido nuevo = new Pedido(0, cliente, articulo, cantidad, ahora);
            em.persist(nuevo);

            em.getTransaction().commit();
            return nuevo;
        } catch (ArticuloNoEncontradoException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return null;
        } finally { em.close(); }
    }

    @Override
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException { // 4. Throws obligatorios

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pedido p = em.find(Pedido.class, numPedido);

            if (p == null) throw new PedidoNoEncontradoException("ID: " + numPedido);

            if (p.esCancelable(ahora)) {
                em.remove(p);
                em.getTransaction().commit();
                return true;
            } else {
                throw new PedidoNoCancelableException("El pedido ya ha sido enviado.");
            }
        } finally { em.close(); }
    }

    @Override
    public List<Pedido> findPendientes(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "FROM Pedido p WHERE p.cliente.email = :email";
            return em.createQuery(jpql, Pedido.class)
                    .setParameter("email", email)
                    .getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<Pedido> findEnviados(String email) {
        return List.of();
    }
}