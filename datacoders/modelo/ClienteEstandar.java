public class ClienteEstandar extends Cliente{

    public ClienteEstandar(String nombre, String domicilio, String nif, String email){
        super(nombre,domicilio,nif,email);
    }
    public String getTipo() {
        return "Estandar";
    }

    public String toString(){
    return super.toString() + "Tipo: Estandar";
    }
}
