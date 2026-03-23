package datacoders.controlador;

import datacoders.modelo.Articulo;
import datacoders.modelo.Cliente;
import datacoders.modelo.excepciones.DuplicadoException;
import java.util.List;

public class PruebaControlador {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();

        System.out.println("=== TEST DE CONTROLADOR CON JDBC ===");

        try {
            // 1. Probar añadir un artículo (Lógica de escritura)
            System.out.println("\n1. Intentando añadir artículo 'TECLADO'...");
            controlador.addArticulo("T01", "Teclado Mecánico RGB", 85.50, 6.0, 24);
            System.out.println("✅ Artículo añadido con éxito.");

            // 2. Probar control de DUPLICADOS (Excepciones)
            System.out.println("\n2. Intentando añadir el MISMO artículo (debe fallar)...");
            try {
                controlador.addArticulo("T01", "Otro Teclado", 10.0, 1.0, 1);
            } catch (DuplicadoException e) {
                System.out.println("✅ Capturada excepción de duplicado correctamente: " + e.getMessage());
            }

            // 3. Probar recuperación de datos (Lógica de lectura)
            System.out.println("\n3. Listando artículos desde el Controlador:");
            List<Articulo> lista = controlador.getArticulos();
            for (Articulo a : lista) {
                System.out.println(" > " + a.getCodigo() + ": " + a.getDescripcion());
            }

            // 4. Probar Clientes (Herencia y Polimorfismo)
            System.out.println("\n4. Añadiendo un Cliente Premium...");
            controlador.addClientePremium("Juan Perez", "Calle Falsa 123", "12345678X", "juan@email.com");

            System.out.println("Listando solo clientes Premium:");
            List<Cliente> premiums = controlador.getClientesPremium();
            for (Cliente c : premiums) {
                System.out.println(" > " + c.getNombre() + " (Email: " + c.getEmail() + ")");
            }

        } catch (Exception e) {
            System.err.println("❌ Error inesperado en el test:");
            e.printStackTrace();
        }
    }
}