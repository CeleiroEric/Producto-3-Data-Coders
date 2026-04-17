package datacoders.modelo;

import jakarta.persistence.*; // Importante para las anotaciones

@Entity
@Table(name = "clientes_estandar") // Su propia tabla para datos específicos
public class ClienteEstandar extends Cliente {

    // 1. Constructor vacío OBLIGATORIO para Hibernate
    public ClienteEstandar() {
        super();
    }

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        // Añadimos 'false' al final porque un cliente estándar NO es premium
        super(nombre, domicilio, nif, email, false);
    }

    @Override
    public double getFactorEnvio(Articulo articulo) {
        // El cliente estándar paga el 100% de los gastos de envío
        return articulo.getGastosEnvio();
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: Estándar";
    }
}