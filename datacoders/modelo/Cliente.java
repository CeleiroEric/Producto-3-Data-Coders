package datacoders.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cliente {
    @Id
    @Column(name = "email", length = 100)
    private String email;
    private String nombre;
    private String domicilio;
    private String nif;

    @Column(name = "es_premium")
    protected boolean esPremium;

    public Cliente() {}

    public Cliente(String nombre, String domicilio, String nif, String email, boolean esPremium) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
        this.esPremium = esPremium;
    }

    // Getters necesarios para la tabla
    public String getNombre(){ return nombre; }
    public String getNif(){ return nif; }
    public String getEmail(){ return email; }
    public String getDomicilio(){ return domicilio; }
    public boolean isPremium() { return esPremium; }

    public double getFactorEnvio(Articulo articulo) {
        return articulo.getGastosEnvio();
    }
}