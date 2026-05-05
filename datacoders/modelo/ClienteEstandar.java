package datacoders.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes_estandar")
@PrimaryKeyJoinColumn(name = "email")
public class ClienteEstandar extends Cliente {
    public ClienteEstandar() { super(); }

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email, false);
    }

    @Override
    public double getFactorEnvio(Articulo articulo) {
        return articulo.getGastosEnvio();
    }
}