package datacoders.modelo;

public class ClientePremium extends Cliente {
    private double cuota_anual;
    private double descuento;

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email,true);
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
        // En lugar de 0.80 fijo, usamos (1 - 0.20) para que sea dinámico
        return articulo.getGastosEnvio() * (1 - this.descuento);
    }

    @Override
    public String toString(){
        // Añadimos @Override al toString por buena práctica
        return super.toString() + " | Tipo: Premium" +
                " | Cuota anual: " + cuota_anual + "€" +
                " | Descuento aplicado: " + (descuento * 100) + "%";
    }
}