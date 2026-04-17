package datacoders.modelo;

import jakarta.persistence.*; // Importante para las anotaciones

@Entity
@Table(name = "clientes_premium") // Su propia tabla para cuota y descuento
public class ClientePremium extends Cliente {

    @Column(name = "cuota_anual")
    private double cuota_anual;

    private double descuento;

    // 1. Constructor vacío OBLIGATORIO para Hibernate
    public ClientePremium() {
        super();
    }

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email, true);
        this.cuota_anual = 30.0;
        this.descuento = 0.20;
    }

    // --- GETTERS Y SETTERS ---
    public double getCuotaAnual(){ return cuota_anual; }
    public void setCuotaAnual(double cuota_anual){ this.cuota_anual = cuota_anual; }

    public double getDescuento(){ return descuento; }
    public void setDescuento(double descuento){ this.descuento = descuento; }

    public String getTipo() {
        return "Premium";
    }

    @Override
    public double getFactorEnvio(Articulo articulo){
        return articulo.getGastosEnvio() * (1 - this.descuento);
    }

    @Override
    public String toString(){
        return super.toString() + " | Tipo: Premium" +
                " | Cuota anual: " + cuota_anual + "€" +
                " | Descuento aplicado: " + (descuento * 100) + "%";
    }
}