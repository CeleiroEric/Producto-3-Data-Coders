package datacoders.modelo;

import datacoders.dao.ClienteDAO;
import datacoders.dao.ArticuloDAO;
import datacoders.dao.PedidoDAO;
import datacoders.factory.DAOFactory;
import datacoders.modelo.excepciones.*;

import java.time.LocalDateTime;
import java.util.List;

public class Datos {

    private final ClienteDAO clienteDAO;
    private final ArticuloDAO articuloDAO;
    private final PedidoDAO pedidoDAO;

    public Datos() {
        // La fábrica te da los DAOs concretos de MySQL.
        DAOFactory factory = DAOFactory.getFactory(DAOFactory.MYSQL);
        this.clienteDAO = factory.getClienteDAO();
        this.articuloDAO = factory.getArticuloDAO();
        this.pedidoDAO = factory.getPedidoDAO();
    }

    // === CLIENTES ===
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        return clienteDAO.insertEstandar(nombre, domicilio, nif, email);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        return clienteDAO.insertPremium(nombre, domicilio, nif, email);
    }

    public Cliente buscarClientePorEmail(String email) throws ClienteNoEncontradoException {
        return clienteDAO.findByEmail(email);
    }

    public List<Cliente> getClientes() {
        return clienteDAO.findAll();
    }

    public List<Cliente> getClientesEstandar() {
        return clienteDAO.findAllEstandar();
    }

    public List<Cliente> getClientesPremium() {
        return clienteDAO.findAllPremium();
    }

    // === ARTÍCULOS ===
    public boolean addArticulo(Articulo a) throws DuplicadoException {
        return articuloDAO.insert(a);
    }

    public Articulo buscarArticuloPorCodigo(String codigo) throws ArticuloNoEncontradoException {
        return articuloDAO.findByCodigo(codigo);
    }

    public List<Articulo> getArticulos() {
        return articuloDAO.findAll();
    }

    // === PEDIDOS ===
    public Pedido addPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        return pedidoDAO.crearPedido(emailCliente, datosCliente, codigoArticulo, cantidad, ahora);
    }

    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {
        return pedidoDAO.eliminarPedido(numPedido, ahora);
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) {
        return pedidoDAO.findPendientes(emailCliente);
    }

    public List<Pedido> getPedidosEnviados(String emailCliente) {
        return pedidoDAO.findEnviados(emailCliente);
    }
}