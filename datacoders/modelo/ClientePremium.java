package datacoders.modelo;

public class ClientePremium extends Cliente {
    private double cuota_anual;
    private double descuento;

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        this.cuota_anual = 30.0;
        this.descuento = 0.20;
    }

    public double getCuotaAnual(){
        return cuota_anual;
    }

    public void setCuotaAnual(double cuota_anual){
        this.cuota_anual=cuota_anual;
    }

    public double getDescuento(){
        return descuento;
    }

    public void setDescuento(double descuento){
        this.descuento=descuento;
    }

    public String getTipo() {
        return "Premium";
    }
    //Cambiar al correcto nombre //Hecho
    @Override
    public double getFactorEnvio(Articulo articulo){
        return articulo.getGastosEnvio() * 0.80;
    }
    public String toString(){
        return super.toString() + "Tipo: Premium " + "/ Cuota anual: " + cuota_anual
                + "/ Descuento: " + descuento;
    }   
}