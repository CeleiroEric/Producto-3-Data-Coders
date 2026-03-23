package datacoders.modelo;

public class Cliente {
    private String nombre;
    private String domicilio;
    private String nif;
    private String email;
    protected boolean esPremium; // <--- 1. Añadimos el atributo (protected para que los hijos lo vean)

    // 2. Actualizamos el constructor para aceptar 5 parámetros
    public Cliente(String nombre, String domicilio, String nif, String email, boolean esPremium) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
        this.esPremium = esPremium;
    }

    public String getNombre(){ return nombre; }
    public String getDomicilio(){ return domicilio; }
    public String getNif(){ return nif; }
    public String getEmail(){ return email; }

    // 3. Añadimos el getter para que el DAO pueda saber si es premium
    public boolean isPremium() {
        return esPremium;
    }

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