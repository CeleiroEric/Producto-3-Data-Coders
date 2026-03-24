package datacoders.modelo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Pedido {

    private int numPedido;
    private Cliente cliente;
    private Articulo articulo;
    private int cantidad;
    private LocalDateTime fechaHora;

    // Constructor que usa TiendaOnline
    public Pedido(int numPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numPedido = numPedido;
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        this.articulo = Objects.requireNonNull(articulo, "El artículo no puede ser nulo");
        this.cantidad = cantidad;
        this.fechaHora = (fechaHora == null) ? LocalDateTime.now() : fechaHora;
    }

    /**
     * Calcula si el pedido se puede cancelar comparando la hora actual
     * con el tiempo de preparación del artículo.
     */
    public boolean esCancelable(LocalDateTime ahora) {
        if (ahora == null) ahora = LocalDateTime.now();

        // Calculamos minutos transcurridos desde la compra
        long minutosPasados = Duration.between(this.fechaHora, ahora).toMinutes();

        // Es cancelable si aún no ha superado el tiempo de preparación
        return minutosPasados < articulo.getTiempoPreparacionMin();
    }

    /**
     * Calcula el coste total del pedido aplicando lógica de la Persona 2
     */
    public double calcularTotal() {
        double subtotal = articulo.getPrecioVenta() * cantidad;
        double gastosEnvio = articulo.getGastosEnvio();

        // Lógica de Negocio: Si el cliente es Premium, aplicamos un 20% de descuento en el envío
        if (cliente.isPremium()) {
            gastosEnvio = gastosEnvio * 0.8;
        }

        return subtotal + gastosEnvio;
    }

    // --- GETTERS ---

    public int getNumPedido() {
        return numPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    // --- MÉTODOS ---

    @Override
    public String toString() {
        return "Pedido #" + numPedido +
                " [Cliente: " + cliente.getNombre() +
                ", Articulo: " + articulo.getDescripcion() +
                ", Total: " + String.format("%.2f", calcularTotal()) + "€]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido pedido = (Pedido) o;
        return numPedido == pedido.numPedido;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numPedido);
    }
}