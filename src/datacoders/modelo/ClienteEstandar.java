package datacoders.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESTANDAR")
public class ClienteEstandar extends Cliente {

    public ClienteEstandar() {
        super();
    }

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override
    public String toString() {
        return "ClienteEstandar{" + super.toString() + "}";
    }
}