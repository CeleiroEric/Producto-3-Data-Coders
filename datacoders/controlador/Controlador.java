package datacoders.controlador;

import datacoders.modelo.*;
import datacoders.modelo.excepciones.*;
import java.time.LocalDateTime;
import java.util.List;

public class Controlador {
    private final Datos datos;

    public Controlador() {
        this.datos = new Datos();
    }

    // ARTÍCULOS
    public boolean addArticulo(String codigo, String desc, double precio, double gastos, int tiempo) throws DuplicadoException {
        Articulo a = new Articulo(codigo, desc, precio, gastos, tiempo);
        return datos.addArticulo(a);
    }

    public List<Articulo> getArticulos() { return datos.getArticulos(); }
    public Articulo buscarArticuloPorCodigo(String cod) throws ArticuloNoEncontradoException {
        return datos.buscarArticuloPorCodigo(cod);
    }

    // CLIENTES
    public boolean addClienteEstandar(String n, String d, String ni, String e) throws DuplicadoException {
        return datos.addClienteEstandar(n, d, ni, e);
    }

    public boolean addClientePremium(String n, String d, String ni, String e) throws DuplicadoException {
        return datos.addClientePremium(n, d, ni, e);
    }

    public List<Cliente> getClientes() { return datos.getClientes(); }
    public List<Cliente> getClientesEstandar() { return datos.getClientesEstandar(); }
    public List<Cliente> getClientesPremium() { return datos.getClientesPremium(); }
    public Cliente buscarClientePorEmail(String e) throws ClienteNoEncontradoException {
        return datos.buscarClientePorEmail(e);
    }

    // PEDIDOS
    public Pedido addPedido(String email, String datosC, String codArt, int cant, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {
        return datos.addPedido(email, datosC, codArt, cant, ahora);
    }

    public boolean eliminarPedido(int num, LocalDateTime ahora) throws PedidoNoEncontradoException, PedidoNoCancelableException {
        return datos.eliminarPedido(num, ahora);
    }

    public List<Pedido> getPedidosPendientes(String e) { return datos.getPedidosPendientes(e); }
    public List<Pedido> getPedidosEnviados(String e) { return datos.getPedidosEnviados(e); }
}