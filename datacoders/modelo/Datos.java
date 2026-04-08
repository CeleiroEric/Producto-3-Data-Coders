package datacoders.modelo;

import datacoders.dao.ClienteDao;
import datacoders.dao.ArticuloDao;
import datacoders.dao.PedidoDao;
import datacoders.factory.DAOFactory;
import datacoders.modelo.excepciones.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Datos {

    private final ClienteDao ClienteDao;
    private final ArticuloDao ArticuloDao;
    private final PedidoDao PedidoDao;

    /**
     * Constructor principal usado por la aplicación.
     * MOD (Persona 3): Datos obtiene los DAO desde la fábrica,
     * manteniendo la Vista y el Controlador sin cambios.
     */
    public Datos() {
        DAOFactory factory = DAOFactory.getFactory(DAOFactory.MYSQL);

        // MOD (Persona 3):
        // Se usa Objects.requireNonNull para detectar antes y con mensaje claro
        // si la fábrica aún no devuelve implementaciones reales.

        this.ClienteDao = Objects.requireNonNull(factory.getClienteDAO(),
                "ClienteDao no inicializado en MySqlDAOFactory");
        this.ArticuloDao = Objects.requireNonNull(factory.getArticuloDAO(),
                "ArticuloDao no inicializado en MySqlDAOFactory");
        this.PedidoDao = Objects.requireNonNull(factory.getPedidoDAO(),
                "PedidoDao no inicializado en MySqlDAOFactory");
    }

    /**
     * MOD (Persona 3):
     * Constructor alternativo para pruebas o integración manual.
     * Permite inyectar DAO concretos sin depender de la fábrica.
     * No afecta al funcionamiento normal del programa.
     */
    public Datos(ClienteDao ClienteDao, ArticuloDao ArticuloDao, PedidoDao PedidoDao) {
        this.ClienteDao = Objects.requireNonNull(ClienteDao, "ClienteDAO no puede ser null");
        this.ArticuloDao = Objects.requireNonNull(ArticuloDao, "ArticuloDAO no puede ser null");
        this.PedidoDao = Objects.requireNonNull(PedidoDao, "PedidoDao no puede ser null");
    }

    // =========================
    // CLIENTES
    // =========================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {
        return ClienteDao.insertEstandar(nombre, domicilio, nif, email);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {
        return ClienteDao.insertPremium(nombre, domicilio, nif, email);
    }

    public Cliente buscarClientePorEmail(String email) throws ClienteNoEncontradoException {
        return ClienteDao.findByEmail(email);
    }

    public List<Cliente> getClientes() {
        return ClienteDao.findAll();
    }

    public List<Cliente> getClientesEstandar() {
        return ClienteDao.findAllEstandar();
    }

    public List<Cliente> getClientesPremium() {
        return ClienteDao.findAllPremium();
    }

    // =========================
    // ARTÍCULOS
    // =========================
    public boolean addArticulo(Articulo a) throws DuplicadoException {
        return ArticuloDao.insert(a);
    }

    public Articulo buscarArticuloPorCodigo(String codigo) throws ArticuloNoEncontradoException {
        return ArticuloDao.findByCodigo(codigo);
    }

    public List<Articulo> getArticulos() {
        return ArticuloDao.findAll();
    }

    // =========================
    // PEDIDOS
    // =========================

    /**
     * MOD (Persona 3):
     * Este método ya está adaptado a persistencia.
     * La implementación real de negocio queda delegada en PedidoDao.
     * NOTA PARA EL GRUPO:
     * En Producto 3, PedidoDao debería implementar esta operación usando
     * JDBC y, preferiblemente, un procedimiento almacenado para crear pedido.
     */
    public Pedido addPedido(String emailCliente, String datosCliente, String codigoArticulo,
                            int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        return PedidoDao.crearPedido(emailCliente, datosCliente, codigoArticulo, cantidad, ahora);
    }

    /**
     * MOD (Persona 3):
     * La lógica de cancelación debe resolverse en PedidoDao
     * usando persistencia real y, si corresponde, transacción/procedimiento almacenado.
     */
    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {
        return PedidoDao.eliminarPedido(numPedido, ahora);
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) {
        return PedidoDao.findPendientes(emailCliente);
    }

    public List<Pedido> getPedidosEnviados(String emailCliente) {
        return PedidoDao.findEnviados(emailCliente);
    }
}