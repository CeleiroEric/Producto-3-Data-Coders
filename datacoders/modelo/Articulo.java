package datacoders.modelo;

import jakarta.persistence.*; // Importamos las anotaciones de JPA
import java.util.Objects;

@Entity // Indica que esta clase es una entidad de base de datos
@Table(name = "articulos") // Nombre de la tabla en MySQL
public class Articulo {

    @Id // Marca el código como la Clave Primaria
    @Column(name = "codigo", length = 50) // Mapeo de la columna
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_venta")
    private double precioVenta;

    @Column(name = "gastos_envio")
    private double gastosEnvio;

    @Column(name = "tiempo_preparacion")
    private int tiempoPreparacionMin;

    // Constructor vacío REQUERIDO por Hibernate
    public Articulo() {
    }

    // Tu constructor con validaciones
    public Articulo(String codigo, String descripcion, double precioVenta, double gastosEnvio, int tiempoPreparacionMin) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del artículo no puede ser nulo/vacío.");
        }
        if (precioVenta < 0) {
            throw new IllegalArgumentException("El precio de venta no puede ser negativo.");
        }
        if (gastosEnvio < 0) {
            throw new IllegalArgumentException("Los gastos de envío no pueden ser negativos.");
        }
        if (tiempoPreparacionMin < 0) {
            throw new IllegalArgumentException("El tiempo de preparación no puede ser negativo.");
        }

        this.codigo = codigo.trim();
        this.descripcion = (descripcion == null) ? "" : descripcion.trim();
        this.precioVenta = precioVenta;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }

    // Getters y Setters (los mantengo igual)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del artículo no puede ser nulo/vacío.");
        }
        this.codigo = codigo.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = (descripcion == null) ? "" : descripcion.trim();
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        if (precioVenta < 0) {
            throw new IllegalArgumentException("El precio de venta no puede ser negativo.");
        }
        this.precioVenta = precioVenta;
    }

    public double getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(double gastosEnvio) {
        if (gastosEnvio < 0) {
            throw new IllegalArgumentException("Los gastos de envío no pueden ser negativos.");
        }
        this.gastosEnvio = gastosEnvio;
    }

    public int getTiempoPreparacionMin() {
        return tiempoPreparacionMin;
    }

    public void setTiempoPreparacionMin(int tiempoPreparacionMin) {
        if (tiempoPreparacionMin < 0) {
            throw new IllegalArgumentException("El tiempo de preparación no puede ser negativo.");
        }
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }

    @Override
    public String toString() {
        return "Articulo{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precioVenta=" + precioVenta +
                ", gastosEnvio=" + gastosEnvio +
                ", tiempoPreparacionMin=" + tiempoPreparacionMin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Articulo)) return false;
        Articulo articulo = (Articulo) o;
        return codigo.equalsIgnoreCase(articulo.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo.toLowerCase());
    }
}