package datacoders.modelo;

import jakarta.persistence.*; // Importante para las anotaciones

@Entity
@Table(name = "clientes")
@Inheritance(strategy = InheritanceType.JOINED) // Crea tablas separadas unidas por el ID
public class Cliente {

    @Id // El email será el identificador único en la DB
    @Column(name = "email", length = 100)
    private String email;

    private String nombre;
    private String domicilio;
    private String nif;

    @Column(name = "es_premium")
    protected boolean esPremium;

    // 1. Constructor vacío (OBLIGATORIO para Hibernate)
    public Cliente() {
    }

    // 2. Tu constructor actual (lo mantenemos igual)
    public Cliente(String nombre, String domicilio, String nif, String email, boolean esPremium) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
        this.esPremium = esPremium;
    }

    // --- GETTERS Y SETTERS (Mantén los que ya tienes) ---
    public String getNombre(){ return nombre; }
    public String getDomicilio(){ return domicilio; }
    public String getNif(){ return nif; }
    public String getEmail(){ return email; }
    public boolean isPremium() { return esPremium; }

    public void setNombre(String nombre){ this.nombre = nombre; }
    public void setDomicilio(String domicilio){ this.domicilio = domicilio; }
    public void setNif(String nif){ this.nif = nif; }
    public void setEmail(String email){ this.email = email; }
    public void setEsPremium(boolean esPremium) { this.esPremium = esPremium; }

    public double getFactorEnvio(Articulo articulo) {
        return articulo.getGastosEnvio();
    }

    @Override
    public String toString(){
        return "Cliente{ Nombre: "+ nombre + " | NIF: " + nif + " | Email: " + email + " }";
    }
}