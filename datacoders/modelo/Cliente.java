public class Cliente {
    private String nombre;
    private String domicilio;
    private String nif;
    private String email;

    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }
    public String getNombre(){
        return nombre;
    }
    public String getDomicilio(){
        return domicilio;
    }
    public String getNif(){
        return nif;
    }
    public String getEmail(){
        return email;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setDomicilio(String domicilio){
        this.domicilio = domicilio;
    }
    public void setNif(String nif){
        this.nif = nif;
    }
    public void setEmail(String email){
        this.email = email;
    }

    //Actualizar al correcto nombre //Actualizado
    public double getFactorEnvio(Articulo articulo) {
        return articulo.getGastosEnvio();
    }
    public String toString(){
        return "Cliente{ Nombre: "+ nombre + "/ Domicilio: " + domicilio +
                "/ NIF: " + nif + "/ Email: " + email + "}";
    }
}
