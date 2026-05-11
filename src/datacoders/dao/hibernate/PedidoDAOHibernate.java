package datacoders.dao.hibernate;

import datacoders.dao.interfaces.PedidoDAOInterface;
import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClienteEstandar;
import datacoders.modelo.ClientePremium;
import datacoders.modelo.Pedido;
import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;
import datacoders.modelo.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDAOHibernate implements PedidoDAOInterface {

    @Override
    public Pedido crearPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Cliente cliente = em.find(Cliente.class, emailCliente);

            if (cliente == null) {
                cliente = crearClienteDesdeDatos(emailCliente, datosCliente);
                em.persist(cliente);
            }

            Articulo articulo = em.find(Articulo.class, codigoArticulo);
            if (articulo == null) {
                throw new ArticuloNoEncontradoException(codigoArticulo);
            }

            Pedido nuevo = new Pedido(0, cliente, articulo, cantidad, ahora);
            em.persist(nuevo);

            em.getTransaction().commit();
            return nuevo;

        } catch (ArticuloNoEncontradoException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (DuplicadoException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al crear pedido en Hibernate: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Pedido pedido = em.find(Pedido.class, numPedido);

            if (pedido == null) {
                throw new PedidoNoEncontradoException("ID: " + numPedido);
            }

            if (!pedido.esCancelable(ahora)) {
                throw new PedidoNoCancelableException("El pedido ya ha sido enviado o ya no puede cancelarse.");
            }

            em.remove(pedido);
            em.getTransaction().commit();
            return true;

        } catch (PedidoNoEncontradoException | PedidoNoCancelableException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar pedido en Hibernate: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> findPendientes(String email) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            List<Pedido> pedidos;

            if (email == null || email.isBlank()) {
                pedidos = em.createQuery(
                        "SELECT p FROM Pedido p ORDER BY p.fechaHora DESC",
                        Pedido.class
                ).getResultList();
            } else {
                pedidos = em.createQuery(
                        "SELECT p FROM Pedido p WHERE p.cliente.email = :email ORDER BY p.fechaHora DESC",
                        Pedido.class
                ).setParameter("email", email)
                 .getResultList();
            }

            LocalDateTime ahora = LocalDateTime.now();

            return pedidos.stream()
                    .filter(p -> p.esCancelable(ahora))
                    .collect(Collectors.toList());

        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> findEnviados(String email) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            List<Pedido> pedidos;

            if (email == null || email.isBlank()) {
                pedidos = em.createQuery(
                        "SELECT p FROM Pedido p ORDER BY p.fechaHora DESC",
                        Pedido.class
                ).getResultList();
            } else {
                pedidos = em.createQuery(
                        "SELECT p FROM Pedido p WHERE p.cliente.email = :email ORDER BY p.fechaHora DESC",
                        Pedido.class
                ).setParameter("email", email)
                 .getResultList();
            }

            LocalDateTime ahora = LocalDateTime.now();

            return pedidos.stream()
                    .filter(p -> !p.esCancelable(ahora))
                    .collect(Collectors.toList());

        } finally {
            em.close();
        }
    }

    private Cliente crearClienteDesdeDatos(String emailCliente, String datosCliente) throws DuplicadoException {
        if (datosCliente == null || datosCliente.isBlank()) {
            throw new DuplicadoException("No existen datos suficientes para crear el cliente.");
        }

        String[] partes = datosCliente.split("\\|");
        if (partes.length < 4) {
            throw new DuplicadoException("Formato de datosCliente incorrecto. Se esperaba: nombre|domicilio|nif|tipo");
        }

        String nombre = partes[0].trim();
        String domicilio = partes[1].trim();
        String nif = partes[2].trim();
        String tipo = partes[3].trim();

        if ("Premium".equalsIgnoreCase(tipo)) {
            return new ClientePremium(nombre, domicilio, nif, emailCliente);
        }

        return new ClienteEstandar(nombre, domicilio, nif, emailCliente);
    }
}