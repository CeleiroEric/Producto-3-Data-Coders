package datacoders.modelo;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_pedido")
    private int numPedido;

    // Vinculamos con cliente_email que es lo que tienes en MySQL
    @ManyToOne
    @JoinColumn(name = "cliente_email", referencedColumnName = "email", nullable = false)
    private Cliente cliente;

    // Vinculamos con articulo_codigo que es lo que tienes en MySQL
    @ManyToOne
    @JoinColumn(name = "articulo_codigo", referencedColumnName = "codigo", nullable = false)
    private Articulo articulo;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    public Pedido() {
    }

    public Pedido(int numPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numPedido = numPedido;
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        this.articulo = Objects.requireNonNull(articulo, "El artículo no puede ser nulo");
        this.cantidad = cantidad;
        this.fechaHora = (fechaHora == null) ? LocalDateTime.now() : fechaHora;
    }

    public boolean esCancelable(LocalDateTime ahora) {
        if (ahora == null) ahora = LocalDateTime.now();
        // Usamos tiempoPreparacionMin que es el nombre de tu atributo en Articulo
        long minutosPasados = Duration.between(this.fechaHora, ahora).toMinutes();
        return minutosPasados < articulo.getTiempoPreparacionMin();
    }

    public double calcularTotal() {
        double subtotal = articulo.getPrecioVenta() * cantidad;
        double gastosEnvio = articulo.getGastosEnvio();

        // Corrección del error 'Cannot resolve method getDescuentoEnvio'
        if (cliente instanceof ClientePremium) {
            // Suponiendo que el descuento se guarda como porcentaje (ej: 20 para 20%)
            double descuento = ((ClientePremium) cliente).getDescuentoEnvio();
            gastosEnvio = gastosEnvio * (1 - (descuento / 100));
        }
        return subtotal + gastosEnvio;
    }

    // Getters
    public int getNumPedido() { return numPedido; }
    public Cliente getCliente() { return cliente; }
    public Articulo getArticulo() { return articulo; }
    public int getCantidad() { return cantidad; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    @Override
    public String toString() {
        return "Pedido #" + numPedido +
                " | Cliente: " + cliente.getNombre() +
                " | Articulo: " + articulo.getDescripcion() +
                " | Cant: " + cantidad +
                " | Total: " + String.format("%.2f", calcularTotal()) + "€";
    }
}