package datacoders.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

    @Column(name = "cuota_anual")
    private double cuotaAnual;

    @Column(name = "descuento_envio")
    private double descuentoEnvio;

    public ClientePremium() {
        super();
        this.cuotaAnual = 30.0;
        this.descuentoEnvio = 20.0;
    }

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        this.cuotaAnual = 30.0;
        this.descuentoEnvio = 20.0;
    }

    public double getCuotaAnual() {
        return cuotaAnual;
    }

    public double getDescuentoEnvio() {
        return descuentoEnvio;
    }

    public void setCuotaAnual(double cuotaAnual) {
        this.cuotaAnual = cuotaAnual;
    }

    public void setDescuentoEnvio(double descuentoEnvio) {
        this.descuentoEnvio = descuentoEnvio;
    }

    @Override
    public String toString() {
        return "ClientePremium{" +
                super.toString() +
                ", cuotaAnual=" + cuotaAnual +
                ", descuentoEnvio=" + descuentoEnvio +
                '}';
    }
}