package datacoders.dao;

import datacoders.dao.interfaces.ArticuloDAOInterface;
import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.Pedido;
import java.time.LocalDateTime; // Importante para la fecha
import java.util.List;

public class PruebaDaos {
    public static void main(String[] args) {
        // Usamos la Factory para obtener la interfaz
        ArticuloDAOInterface articuloDAO = DAOFactory.getArticuloDAO();

        try {
            System.out.println("--- 1. COMPROBANDO CONEXIÓN Y LECTURA ---");

            // Intentamos leer los artículos que existan en la base de datos
            List<Articulo> lista = articuloDAO.listar();

            if (lista.isEmpty()) {
                System.out.println("⚠️ La tabla 'articulos' está vacía en MySQL.");
            } else {
                System.out.println("✅ Se han encontrado " + lista.size() + " artículos en la BD:");
                for (Articulo a : lista) {
                    System.out.println(" > " + a.getDescripcion() + " - " + a.getPrecioVenta() + "€");
                }
            }

            System.out.println("\n--- 2. COMPROBANDO ESCRITURA ---");
            // Generamos un código único para que no falle por clave duplicada
            String codigoAzar = "A" + (int)(Math.random() * 10000);
            Articulo nuevo = new Articulo(codigoAzar, "Ratón Gaming", 45.0, 5.0, 10);

            articuloDAO.insertar(nuevo);
            System.out.println("✅ ¡Artículo '" + codigoAzar + "' insertado con éxito!");

            System.out.println("\n--- 3. COMPROBANDO LÓGICA DE NEGOCIO (CALCULAR TOTAL) ---");
            // Creamos los objetos para la comparativa de la Persona 2
            Cliente premium = new Cliente("Maria", "Calle Falsa 123", "12345678X", "maria@test.com", true);
            Cliente estandard = new Cliente("Pepe", "Calle Mayor 1", "87654321Z", "pepe@test.com", false);

            // Importante: Usamos LocalDateTime.now() en lugar de null para evitar errores en el modelo
            Pedido pedidoPre = new Pedido(101, premium, nuevo, 2, LocalDateTime.now());
            Pedido pedidoEst = new Pedido(102, estandard, nuevo, 2, LocalDateTime.now());

            System.out.println("Calculando pedido de 2 unidades de: " + nuevo.getDescripcion());
            System.out.println("💰 Total Cliente Premium: " + String.format("%.2f", pedidoPre.calcularTotal()) + "€");
            System.out.println("💰 Total Cliente Estándar: " + String.format("%.2f", pedidoEst.calcularTotal()) + "€");

            // Verificamos que el Premium pague menos (descuento en envío)
            if (pedidoPre.calcularTotal() < pedidoEst.calcularTotal()) {
                System.out.println("\n✅ ¡BRUTAL! El sistema aplica correctamente los descuentos de socio.");
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO: Revisa si MySQL está encendido o si las tablas existen.");
            e.printStackTrace();
        }
    }
}