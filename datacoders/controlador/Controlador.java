package datacoders.controlador;

import datacoders.dao.*;
import datacoders.modelo.*;
import datacoders.modelo.excepciones.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Controlador {

    // Instancias de los DAO (Capa de Persistencia)
    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final PedidoDao pedidoDao = new PedidoDao();

    public Controlador() {
        // Constructor vacío
    }

    // =========================================================================
    // GESTIÓN DE ARTÍCULOS
    // =========================================================================

    public boolean addArticulo(String cod, String desc, double precio, double env, int tiempo) throws DuplicadoException {
        try {
            articuloDao.insertar(new Articulo(cod, desc, precio, env, tiempo));
            return true;
        } catch (SQLException e) {
            // El código 1062 en MySQL indica una entrada duplicada (Primary Key)
            if (e.getErrorCode() == 1062) throw new DuplicadoException("El código '" + cod + "' ya existe.");
            return false;
        }
    }

    public List<Articulo> getArticulos() {
        try {
            return articuloDao.listar();
        } catch (SQLException e) {
            return List.of();
        }
    }

    // =========================================================================
    // GESTIÓN DE CLIENTES
    // =========================================================================

    public boolean addClienteEstandar(String n, String d, String ni, String em) throws DuplicadoException {
        try {
            clienteDao.insertar(new ClienteEstandar(n, d, ni, em));
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) throw new DuplicadoException("El cliente con NIF/Email ya existe.");
            return false;
        }
    }

    public boolean addClientePremium(String n, String d, String ni, String em) throws DuplicadoException {
        try {
            clienteDao.insertar(new ClientePremium(n, d, ni, em));
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) throw new DuplicadoException("El cliente con NIF/Email ya existe.");
            return false;
        }
    }

    public List<Cliente> getClientes() {
        try {
            return clienteDao.listar();
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Cliente> getClientesEstandar() {
        return getClientes().stream()
                .filter(c -> !(c instanceof ClientePremium))
                .collect(Collectors.toList());
    }

    public List<Cliente> getClientesPremium() {
        return getClientes().stream()
                .filter(c -> c instanceof ClientePremium)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // GESTIÓN DE PEDIDOS
    // =========================================================================

    public void addPedido(String email, String dummy, String cod, int cant, LocalDateTime fecha) throws Exception {
        try {
            // IMPORTANTE: El orden debe ser email (String), cod (String), cant (int), fecha (LocalDateTime)
            pedidoDao.insertar(email, cod, cant, fecha);
        } catch (SQLException e) {
            throw new Exception("Error en BD: " + e.getMessage());
        }
    }

    public void eliminarPedido(int num, LocalDateTime ahora) throws PedidoNoCancelableException {
        try {
            // Se delega la lógica de borrado al DAO
            pedidoDao.eliminar(num);
        } catch (SQLException e) {
            throw new PedidoNoCancelableException("No se puede eliminar el pedido #" + num + ". Puede que ya esté enviado.");
        }
    }

    public List<Pedido> getPedidosPendientes(String email) {
        try {
            // Corregido: pasamos 'email' directamente sin el tipo de dato
            return pedidoDao.listarPendientes(email);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Pedido> getPedidosEnviados(String email) {
        try {
            return pedidoDao.listarEnviados(email);
        } catch (SQLException e) {
            return List.of();
        }
    }
}