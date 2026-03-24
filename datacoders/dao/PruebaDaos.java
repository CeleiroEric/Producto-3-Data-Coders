package datacoders.dao;

import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.Pedido;
import java.util.List;

public class PruebaDaos {
    public static void main(String[] args) {
        ArticuloDAOInterface articuloDAO = DAOFactory.getArticuloDAO();

        try {
            System.out.println("--- 1. COMPROBANDO CONEXIÓN Y LECTURA ---");

            // Intentamos leer los artículos que insertamos en MySQL Workbench
            List<Articulo> lista = articuloDAO.listar();

            if (lista.isEmpty()) {
                System.out.println("⚠️ La tabla está vacía. Inserta algo en MySQL Workbench primero.");
            } else {
                System.out.println("✅ Se han encontrado " + lista.size() + " artículos en la BD:");
                for (Articulo a : lista) {
                    System.out.println(" > " + a.getDescripcion() + " - " + a.getPrecioVenta() + "€");
                }
            }

            System.out.println("\n--- 2. COMPROBANDO ESCRITURA ---");
            // Usamos un código aleatorio para evitar el error de "Duplicate Entry" si lo corres varias veces
            String codigoAzar = "A" + (int)(Math.random() * 10000);
            Articulo nuevo = new Articulo(codigoAzar, "Ratón Gaming", 45.0, 5.0, 10);

            articuloDAO.insertar(nuevo);
            System.out.println("✅ ¡Artículo '" + codigoAzar + "' insertado con éxito desde Java!");

            System.out.println("\n--- 3. COMPROBANDO LÓGICA DE NEGOCIO (CALCULAR TOTAL) ---");
            // Creamos un cliente premium y uno estándar para comparar
            Cliente premium = new Cliente("Maria", "Calle Falsa 123", "12345678X", "maria@test.com", true);
            Cliente estandard = new Cliente("Pepe", "Calle Mayor 1", "87654321Z", "pepe@test.com", false);

            // Creamos pedidos de prueba (no se guardan en la BD, solo probamos el método calcularTotal)
            Pedido pedidoPre = new Pedido(101, premium, nuevo, 2, null);
            Pedido pedidoEst = new Pedido(102, estandard, nuevo, 2, null);

            System.out.println("Calculando pedido de 2 " + nuevo.getDescripcion() + " (45€/u + 5€ envío)");
            System.out.println("💰 Total Cliente Premium: " + String.format("%.2f", pedidoPre.calcularTotal()) + "€");
            System.out.println("💰 Total Cliente Estándar: " + String.format("%.2f", pedidoEst.calcularTotal()) + "€");

            if (pedidoPre.calcularTotal() < pedidoEst.calcularTotal()) {
                System.out.println("\n✅ ¡ÉXITO! El sistema aplica correctamente los descuentos de la Persona 2.");
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR: Algo ha fallado:");
            e.printStackTrace();
        }
    }
}