package datacoders.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes") // Usamos la tabla principal de clientes
public class ClientePremium extends Cliente {

    @Column(name = "cuota_anual")
    private double cuota_anual;

    @Column(name = "descuento_envio") // Nombre exacto para MySQL
    private double descuento;

    public ClientePremium() {
        super();
    }

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email, true);
        this.cuota_anual = 30.0;
        this.descuento = 20.0; // Guardamos como 20 para representar el 20%
    }

    // --- GETTERS Y SETTERS ---
    public double getCuotaAnual() { return cuota_anual; }
    public void setCuotaAnual(double cuota_anual) { this.cuota_anual = cuota_anual; }

    // Este es el método que le faltaba a Pedido.java
    public double getDescuentoEnvio() { return descuento; }
    public void setDescuentoEnvio(double descuento) { this.descuento = descuento; }

    @Override
    public double getFactorEnvio(Articulo articulo) {
        return articulo.getGastosEnvio() * (1 - (this.descuento / 100));
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: Premium" +
                " | Cuota: " + cuota_anual + "€" +
                " | Descuento: " + descuento + "%";
    }
}