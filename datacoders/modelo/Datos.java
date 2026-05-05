package datacoders.modelo;

import datacoders.dao.interfaces.*;
import datacoders.factory.DAOFactory;
import datacoders.modelo.excepciones.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Datos {
    private final ClienteDAOInterface clienteDao;
    private final ArticuloDAOInterface articuloDao;
    private final PedidoDAOInterface pedidoDao;

    public Datos() {
        DAOFactory factory = DAOFactory.getFactory(DAOFactory.MYSQL);
        this.clienteDao = factory.getClienteDAO();
        this.articuloDao = factory.getArticuloDAO();
        this.pedidoDao = factory.getPedidoDAO();
    }

    // --- ARTÍCULOS ---
    public boolean addArticulo(Articulo articulo) throws DuplicadoException {
        try {
            articuloDao.insertar(articulo);
            return true;
        } catch (SQLException e) {
            // Imprime el error en la consola roja para que sepas qué falla (columna, tipo de dato, etc.)
            e.printStackTrace();
            throw new DuplicadoException("Error BD: " + e.getMessage());
        }
    }

    public List<Articulo> getArticulos() {
        return articuloDao.listarTodos();
    }

    public Articulo buscarArticuloPorCodigo(String codigo) throws ArticuloNoEncontradoException {
        Articulo a = articuloDao.buscarPorCodigo(codigo);
        if (a == null) throw new ArticuloNoEncontradoException(codigo);
        return a;
    }

    // --- CLIENTES ---
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        try {
            Cliente c = new ClienteEstandar(nombre, domicilio, nif, email);
            return clienteDao.insertar(c);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DuplicadoException("Error al añadir cliente: " + e.getMessage());
        }
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) throws DuplicadoException {
        try {
            Cliente c = new ClientePremium(nombre, domicilio, nif, email);
            return clienteDao.insertar(c);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DuplicadoException("Error al añadir cliente: " + e.getMessage());
        }
    }

    public List<Cliente> getClientes() { return clienteDao.listarTodos(); }
    public List<Cliente> getClientesEstandar() { return clienteDao.listarEstandar(); }
    public List<Cliente> getClientesPremium() { return clienteDao.listarPremium(); }

    public Cliente buscarClientePorEmail(String email) throws ClienteNoEncontradoException {
        Cliente c = clienteDao.buscarPorEmail(email);
        if (c == null) throw new ClienteNoEncontradoException(email);
        return c;
    }

    // --- PEDIDOS ---
    public Pedido addPedido(String email, String datos, String codigo, int cant, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        return pedidoDao.crearPedido(email, datos, codigo, cant, ahora);
    }

    public boolean eliminarPedido(int num, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {
        return pedidoDao.eliminarPedido(num, ahora);
    }

    public List<Pedido> getPedidosPendientes(String email) { return pedidoDao.findPendientes(email); }
    public List<Pedido> getPedidosEnviados(String email) { return pedidoDao.findEnviados(email); }
}