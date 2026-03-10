package datacoders.modelo;

import datacoders.modelo.excepciones.ArticuloNoEncontradoException;
import datacoders.modelo.excepciones.ClienteNoEncontradoException;
import datacoders.modelo.excepciones.DuplicadoException;
import datacoders.modelo.excepciones.PedidoNoCancelableException;
import datacoders.modelo.excepciones.PedidoNoEncontradoException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Datos (MVC - Modelo)
 * - Requisito del anexo: "Datos contendrá todos los datos de la aplicación y llevará a cabo
 *   todas las acciones que afectan a las mismas."
 *
 * MOD (diseño): usamos colecciones óptimas:
 * - Clientes por email: Map<String, Cliente>  (búsqueda O(1))
 * - Artículos por código: Map<String, Articulo> (búsqueda O(1))
 * - Pedidos por número: Map<Integer, Pedido>
 *
 * Nota: NO hay I/O aquí (no Scanner, no System.out).
 */
public class Datos {

    private final Map<String, Cliente> clientesPorEmail;
    private final Map<String, Articulo> articulosPorCodigo;
    private final Map<Integer, Pedido> pedidosPorNumero;

    public Datos() {
        // LinkedHashMap conserva orden de inserción -> listados más “amigables” en consola
        this.clientesPorEmail = new LinkedHashMap<>();
        this.articulosPorCodigo = new LinkedHashMap<>();
        this.pedidosPorNumero = new LinkedHashMap<>();
    }

    // =========================
    // ARTÍCULOS
    // =========================
    public boolean addArticulo(Articulo a) throws DuplicadoException {
        if (a == null) throw new IllegalArgumentException("Articulo no puede ser null");
        String key = normalizar(a.getCodigo());

        if (articulosPorCodigo.containsKey(key)) {
            throw new DuplicadoException("Ya existe un artículo con código: " + a.getCodigo());
        }
        articulosPorCodigo.put(key, a);
        return true;
    }

    public Articulo buscarArticuloPorCodigo(String codigo) throws ArticuloNoEncontradoException {
        String key = normalizar(codigo);
        Articulo a = articulosPorCodigo.get(key);
        if (a == null) {
            throw new ArticuloNoEncontradoException("No existe artículo con código: " + codigo);
        }
        return a;
    }

    public List<Articulo> getArticulos() {
        return new ArrayList<>(articulosPorCodigo.values());
    }

    // =========================
    // CLIENTES
    // =========================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {

        Cliente c = new ClienteEstandar(nombre, domicilio, nif, email);
        return addCliente(c);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email)
            throws DuplicadoException {

        Cliente c = new ClientePremium(nombre, domicilio, nif, email);
        return addCliente(c);
    }

    public boolean addCliente(Cliente c) throws DuplicadoException {
        if (c == null) throw new IllegalArgumentException("Cliente no puede ser null");
        String key = normalizar(c.getEmail());

        if (clientesPorEmail.containsKey(key)) {
            throw new DuplicadoException("Ya existe un cliente con email: " + c.getEmail());
        }
        clientesPorEmail.put(key, c);
        return true;
    }

    public Cliente buscarClientePorEmail(String email) throws ClienteNoEncontradoException {
        String key = normalizar(email);
        Cliente c = clientesPorEmail.get(key);
        if (c == null) {
            throw new ClienteNoEncontradoException("No existe cliente con email: " + email);
        }
        return c;
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientesPorEmail.values());
    }

    public List<Cliente> getClientesEstandar() {
        List<Cliente> res = new ArrayList<>();
        for (Cliente c : clientesPorEmail.values()) {
            if (c instanceof ClienteEstandar) res.add(c);
        }
        return res;
    }

    public List<Cliente> getClientesPremium() {
        List<Cliente> res = new ArrayList<>();
        for (Cliente c : clientesPorEmail.values()) {
            if (c instanceof ClientePremium) res.add(c);
        }
        return res;
    }

    // =========================
    // PEDIDOS
    // =========================

    /**
     * Regla del enunciado: al añadir pedido,
     * - el artículo debe existir
     * - si el cliente no existe, se registra (con datosCliente)
     */
    public Pedido addPedido(String emailCliente, String datosCliente, String codigoArticulo,
                            int cantidad, LocalDateTime ahora)
            throws ArticuloNoEncontradoException, DuplicadoException {

        if (cantidad <= 0) throw new IllegalArgumentException("cantidad debe ser > 0");
        if (ahora == null) ahora = LocalDateTime.now();

        Articulo articulo = buscarArticuloPorCodigo(codigoArticulo);

        Cliente cliente;
        String keyEmail = normalizar(emailCliente);
        if (clientesPorEmail.containsKey(keyEmail)) {
            cliente = clientesPorEmail.get(keyEmail);
        } else {
            // MOD: cumplimos el requisito "si cliente no existe, pedir datos y registrarlo"
            cliente = crearClienteDesdeDatos(emailCliente, datosCliente);
            addCliente(cliente); // puede lanzar DuplicadoException si hay carrera (raro, pero correcto)
        }

        int numPedido = generarSiguienteNumPedido();
        Pedido p = new Pedido(numPedido, cliente, articulo, cantidad, ahora);
        pedidosPorNumero.put(numPedido, p);
        return p;
    }

    public boolean eliminarPedido(int numPedido, LocalDateTime ahora)
            throws PedidoNoEncontradoException, PedidoNoCancelableException {

        if (ahora == null) ahora = LocalDateTime.now();

        Pedido p = pedidosPorNumero.get(numPedido);
        if (p == null) {
            throw new PedidoNoEncontradoException("No existe pedido con número: " + numPedido);
        }

        // Regla del enunciado: solo eliminar si no ha sido enviado (tiempo preparación)
        if (!p.esCancelable(ahora)) {
            throw new PedidoNoCancelableException("El pedido " + numPedido + " ya no es cancelable (considerado enviado).");
        }

        pedidosPorNumero.remove(numPedido);
        return true;
    }

    /**
     * "Pendiente" = aún dentro del tiempo de preparación (equivalente a cancelable).
     * Se permite filtrar por email si no es null/vacío.
     */
    public List<Pedido> getPedidosPendientes(String emailCliente) {
        return filtrarPedidosPorEstado(emailCliente, true);
    }

    /**
     * "Enviado" = ha superado el tiempo de preparación (no cancelable).
     */
    public List<Pedido> getPedidosEnviados(String emailCliente) {
        return filtrarPedidosPorEstado(emailCliente, false);
    }

    // =========================
    // HELPERS
    // =========================

    private List<Pedido> filtrarPedidosPorEstado(String emailCliente, boolean pendientes) {
        LocalDateTime ahora = LocalDateTime.now();
        String emailKey = (emailCliente == null || emailCliente.isBlank()) ? null : normalizar(emailCliente);

        List<Pedido> res = new ArrayList<>();
        for (Pedido p : pedidosPorNumero.values()) {

            if (emailKey != null) {
                String pEmail = normalizar(p.getCliente().getEmail());
                if (!pEmail.equals(emailKey)) continue;
            }

            boolean esPendiente = p.esCancelable(ahora);
            if (pendientes == esPendiente) res.add(p);
        }
        return res;
    }

    private int generarSiguienteNumPedido() {
        int max = 0;
        for (Integer n : pedidosPorNumero.keySet()) {
            if (n > max) max = n;
        }
        return max + 1;
    }

    private Cliente crearClienteDesdeDatos(String email, String datosCliente) {
        // Formato esperado: "nombre|domicilio|nif|tipo"
        // tipo: "estandar" o "premium"
        String nombre = "N/D";
        String domicilio = "N/D";
        String nif = "N/D";
        String tipo = "estandar";

        if (datosCliente != null && !datosCliente.isBlank()) {
            String[] parts = datosCliente.split("\\|");
            if (parts.length > 0 && !parts[0].isBlank()) nombre = parts[0].trim();
            if (parts.length > 1 && !parts[1].isBlank()) domicilio = parts[1].trim();
            if (parts.length > 2 && !parts[2].isBlank()) nif = parts[2].trim();
            if (parts.length > 3 && !parts[3].isBlank()) tipo = parts[3].trim().toLowerCase();
        }

        // MOD: devolvemos el subtipo correcto (herencia del diagrama)
        if ("premium".equals(tipo)) {
            return new ClientePremium(nombre, domicilio, nif, email);
        }
        return new ClienteEstandar(nombre, domicilio, nif, email);
    }

    private String normalizar(String s) {
        if (s == null) throw new IllegalArgumentException("Clave no puede ser null");
        String t = s.trim().toLowerCase();
        if (t.isEmpty()) throw new IllegalArgumentException("Clave no puede ser vacía");
        return t;
    }
}