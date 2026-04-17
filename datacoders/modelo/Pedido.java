package datacoders.modelo;

import jakarta.persistence.*; // Importante
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El ID se genera solo (1, 2, 3...)
    @Column(name = "num_pedido")
    private int numPedido;

    @ManyToOne // Relación: Muchos pedidos -> Un Cliente
    @JoinColumn(name = "cliente_email", nullable = false) // Columna FK en la tabla
    private Cliente cliente;

    @ManyToOne // Relación: Muchos pedidos -> Un Artículo
    @JoinColumn(name = "articulo_codigo", nullable = false) // Columna FK en la tabla
    private Articulo articulo;

    private int cantidad;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    // 1. Constructor vacío OBLIGATORIO
    public Pedido() {
    }

    // 2. Tu constructor (lo mantenemos igual)
    public Pedido(int numPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numPedido = numPedido;
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        this.articulo = Objects.requireNonNull(articulo, "El artículo no puede ser nulo");
        this.cantidad = cantidad;
        this.fechaHora = (fechaHora == null) ? LocalDateTime.now() : fechaHora;
    }

    // --- Lógica de Negocio (Se queda igual, Hibernate no la toca) ---

    public boolean esCancelable(LocalDateTime ahora) {
        if (ahora == null) ahora = LocalDateTime.now();
        long minutosPasados = Duration.between(this.fechaHora, ahora).toMinutes();
        return minutosPasados < articulo.getTiempoPreparacionMin();
    }

    public double calcularTotal() {
        double subtotal = articulo.getPrecioVenta() * cantidad;
        double gastosEnvio = articulo.getGastosEnvio();
        if (cliente.isPremium()) {
            gastosEnvio = gastosEnvio * 0.8;
        }
        return subtotal + gastosEnvio;
    }

    // --- GETTERS (Iguales) ---
    public int getNumPedido() { return numPedido; }
    public Cliente getCliente() { return cliente; }
    public Articulo getArticulo() { return articulo; }
    public int getCantidad() { return cantidad; }
    public LocalDateTime getFechaHora() { return fechaHora; }

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