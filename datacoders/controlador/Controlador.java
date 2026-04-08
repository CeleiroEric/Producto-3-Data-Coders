package datacoders.controlador;

import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.Datos;
import datacoders.modelo.Pedido;

import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;
import datacoders.modelo.excepciones.ClienteNoEncontradoException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador (MVC)
 * - La Vista solo habla con el Controlador.
 * - El Controlador delega en Datos (Modelo).
 * - El Controlador NO hace I/O (no System.out, no Scanner).
 */
public class Controlador {

    private final Datos datos;

    public Controlador() {
        this.datos = new Datos();
    }

    // Útil para tests
    public Controlador(Datos datos) {
        this.datos = datos;
    }

    // =========================
    // ARTÍCULOS
    // =========================
    public boolean addArticulo(String codigo, String descripcion, double precioVenta,
                               double gastosEnvio, int tiempoPreparacionMin)
            throws DuplicadoException {

        Articulo a = new Articulo(codigo, descripcion, precioVenta, gastosEnvio, tiempoPreparacionMin);
        return datos.addArticulo(a);
    }

    public List<Articulo> getArticulos() {
        return datos.getArticulos();
    }

    public Articulo buscarArticuloPorCodigo(String codigo) throws ArticuloNoEncontradoException {
        return datos.buscarArticuloPorCodigo(codigo);
    }

    // =========================
    // CLIENTES
    // =========================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {
        return datos.addClienteEstandar(nombre, domicilio, nif, email);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {
        return datos.addClientePremium(nombre, domicilio, nif, email);
    }

    public List<Cliente> getClientes() {
        return datos.getClientes();
    }

    public List<Cliente> getClientesEstandar() {
        return datos.getClientesEstandar();
    }

    public List<Cliente> getClientesPremium() {
        return datos.getClientesPremium();
    }

    public Cliente buscarClientePorEmail(String email) throws ClienteNoEncontradoException {
        return datos.buscarClientePorEmail(email);
    }

    // =========================
    // PEDIDOS
    // =========================
    public Pedido addPedido(String emailCliente, String datosCliente, String codigoArticulo,
                            int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {

        return datos.addPedido(emailCliente, datosCliente, codigoArticulo, cantidad, ahora);
    }

    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {
        return datos.eliminarPedido(numPedido, ahora);
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) {
        return datos.getPedidosPendientes(emailCliente);
    }

    public List<Pedido> getPedidosEnviados(String emailCliente) {
        return datos.getPedidosEnviados(emailCliente);
    }
}