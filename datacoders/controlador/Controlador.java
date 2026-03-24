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

    public void addPedido(String email, String cod, int cant, LocalDateTime fecha) throws Exception {
        try {
            // NOTA: Tu PedidoDao.crearPedido espera (int, String, int)
            // Si el procedimiento en MySQL acepta Email, cambia el tipo en PedidoDao.
            // Por ahora, lo llamamos con el nombre correcto del método:

            // Suponiendo que conviertes el email a ID o que el DAO acepta String:
            // pedidoDao.crearPedido(email, cod, cant);

            // Para que no te dé error de compilación ahora mismo, asegúrate de que
            // los tipos coincidan con lo que escribiste en PedidoDao.java
            int idSimulado = 1; // Esto es temporal hasta que obtengas el ID real del cliente
            pedidoDao.crearPedido(idSimulado, cod, cant);

        } catch (SQLException e) {
            throw new Exception("Error en BD: " + e.getMessage());
        }
    }

    public void eliminarPedido(int num, LocalDateTime ahora) throws PedidoNoCancelableException {
        try {
            // Corregido: El método en tu PedidoDao se llama eliminarPedido
            pedidoDao.eliminarPedido(num);
        } catch (SQLException e) {
            throw new PedidoNoCancelableException("No se puede eliminar el pedido #" + num + ". Puede que ya esté enviado.");
        }
    }

    public List<Pedido> getPedidosPendientes(String email) {
        try {
            // Asegúrate de que estos métodos existan en PedidoDao con estos nombres
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