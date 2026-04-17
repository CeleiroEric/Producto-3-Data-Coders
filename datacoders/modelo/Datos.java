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
     * Ahora configurado para usar la factoría de HIBERNATE.
     */
    public Datos() {
        // CAMBIO CLAVE: Cambiamos de MYSQL (JDBC) a HIBERNATE (JPA)
        DAOFactory factory = DAOFactory.getFactory(DAOFactory.HIBERNATE);

        // Verificamos que la fábrica nos devuelva los DAOs de Hibernate correctamente
        this.ClienteDao = Objects.requireNonNull(factory.getClienteDAO(),
                "Error: ClienteDao no inicializado en HibernateDAOFactory");
        this.ArticuloDao = Objects.requireNonNull(factory.getArticuloDAO(),
                "Error: ArticuloDao no inicializado en HibernateDAOFactory");
        this.PedidoDao = Objects.requireNonNull(factory.getPedidoDAO(),
                "Error: PedidoDao no inicializado en HibernateDAOFactory");
    }

    /**
     * Constructor alternativo para pruebas o integración manual.
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

    public Pedido addPedido(String emailCliente, String datosCliente, String codigoArticulo,
                            int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        // La lógica se delega al DAO, que ahora usará EntityManager
        return PedidoDao.crearPedido(emailCliente, datosCliente, codigoArticulo, cantidad, ahora);
    }

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