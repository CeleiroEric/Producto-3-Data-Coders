import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TiendaOnline {

    private List<Cliente> clientes;
    private List<Articulo> articulos;
    private List<Pedido> pedidos;

    public TiendaOnline() {
        this.clientes = new ArrayList<>();
        this.articulos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public boolean addCliente(Cliente c) {
        if (c == null) return false;
        if (c.getEmail() == null || c.getEmail().isBlank()) {
            throw new IllegalArgumentException("El cliente debe tener email.");
        }
        if (buscarClientePorEmail(c.getEmail()) != null) {
            return false; // ya existe
        }
        return clientes.add(c);
    }

    public boolean addArticulo(Articulo a) {
        if (a == null) return false;
        if (buscarArticuloPorCodigo(a.getCodigo()) != null) {
            return false; // ya existe
        }
        return articulos.add(a);
    }

    public Cliente buscarClientePorEmail(String email) {
        if (email == null || email.isBlank()) return null;
        for (Cliente c : clientes) {
            if (c.getEmail() != null && c.getEmail().equalsIgnoreCase(email.trim())) {
                return c;
            }
        }
        return null;
    }

    public Articulo buscarArticuloPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) return null;
        for (Articulo a : articulos) {
            if (a.getCodigo() != null && a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return a;
            }
        }
        return null;
    }

    /**
     * Crea un pedido. Según el enunciado, si el cliente no existe se debe registrar.
     * Si no existe, lo creamos a partir de datosCliente.
     *
     * Formato datosCliente:
     *   "nombre|domicilio|nif|tipo"
     * donde tipo puede ser "estandar" o "premium". Si no se indica, se asume "estandar".
     */
    public Pedido addPedido(String emailCliente, String datosCliente, String codigoArticulo, int cantidad, LocalDateTime ahora) {
        if (ahora == null) ahora = LocalDateTime.now();
        if (emailCliente == null || emailCliente.isBlank()) {
            throw new IllegalArgumentException("emailCliente es obligatorio.");
        }
        if (codigoArticulo == null || codigoArticulo.isBlank()) {
            throw new IllegalArgumentException("codigoArticulo es obligatorio.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser > 0.");
        }

        Articulo articulo = buscarArticuloPorCodigo(codigoArticulo);
        if (articulo == null) {
            throw new IllegalArgumentException("No existe el artículo con código: " + codigoArticulo);
        }

        Cliente cliente = buscarClientePorEmail(emailCliente);
        if (cliente == null) {
            // Registrar cliente (si no existe)
            cliente = crearClienteDesdeDatos(emailCliente, datosCliente);
            addCliente(cliente);
        }

        int numPedido = generarSiguienteNumPedido();
        Pedido pedido = new Pedido(numPedido, cliente, articulo, cantidad, ahora);

        pedidos.add(pedido);
        return pedido;
    }

    public boolean eliminarPedido(int numPedido, LocalDateTime ahora) {
        if (ahora == null) ahora = LocalDateTime.now();

        Pedido p = buscarPedidoPorNumero(numPedido);
        if (p == null) return false;

        // Regla: solo se elimina si es cancelable (no enviado según tiempo preparación)
        if (!p.esCancelable(ahora)) {
            return false;
        }
        return pedidos.remove(p);
    }

    public List<Pedido> getPedidosPendientes(String emailCliente) {
        LocalDateTime ahora = LocalDateTime.now();
        return filtrarPedidosPorEstado(emailCliente, ahora, true);
    }

    public List<Pedido> getPedidosEnviados(String emailCliente) {
        LocalDateTime ahora = LocalDateTime.now();
        return filtrarPedidosPorEstado(emailCliente, ahora, false);
    }

    // -------------------------
    // Helpers privados
    // -------------------------

    private Pedido buscarPedidoPorNumero(int numPedido) {
        for (Pedido p : pedidos) {
            if (p.getNumPedido() == numPedido) {
                return p;
            }
        }
        return null;
    }

    private int generarSiguienteNumPedido() {
        int max = 0;
        for (Pedido p : pedidos) {
            if (p.getNumPedido() > max) max = p.getNumPedido();
        }
        return max + 1;
    }

    /**
     * Pendiente = aún dentro del tiempo de preparación.
     * Enviado = ha superado el tiempo de preparación.
     */
    private List<Pedido> filtrarPedidosPorEstado(String emailCliente, LocalDateTime ahora, boolean pendiente) {
        List<Pedido> resultado = new ArrayList<>();
        String email = (emailCliente == null) ? null : emailCliente.trim();

        for (Pedido p : pedidos) {
            if (email != null && !email.isBlank()) {
                if (p.getCliente() == null || p.getCliente().getEmail() == null) continue;
                if (!p.getCliente().getEmail().equalsIgnoreCase(email)) continue;
            }

            boolean esPendiente = esPendiente(p, ahora);
            if (pendiente == esPendiente) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    private boolean esPendiente(Pedido p, LocalDateTime ahora) {
        if (p.getFechaHora() == null || p.getArticulo() == null) return false;

        long minutos = Duration.between(p.getFechaHora(), ahora).toMinutes();
        int prep = p.getArticulo().getTiempoPreparacionMin();
        return minutos < prep;
    }

    private Cliente crearClienteDesdeDatos(String email, String datosCliente) {
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

        // Pere: Antes se devolvía Cliente_premium, sin barra baja.
        // Ahora devolvemos ClientePremium o ClienteEstandar.
        if ("premium".equals(tipo)) {
            return new ClientePremium(nombre, domicilio, nif, email);
        }
        return new ClienteEstandar(nombre, domicilio, nif, email);
    }
}