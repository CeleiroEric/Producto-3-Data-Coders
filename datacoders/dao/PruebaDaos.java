package datacoders.dao;

import datacoders.modelo.Articulo;
import java.util.List;

public class PruebaDaos {
    public static void main(String[] args) {
        ArticuloDao articuloDAO = new ArticuloDao();

        try {
            System.out.println("--- COMPROBANDO CONEXIÓN Y LECTURA ---");

            // 1. Intentamos leer los artículos que insertamos en MySQL Workbench
            List<Articulo> lista = articuloDAO.listar();

            if (lista.isEmpty()) {
                System.out.println("⚠️ La tabla está vacía. Inserta algo en MySQL Workbench primero.");
            } else {
                System.out.println("✅ Se han encontrado " + lista.size() + " artículos:");
                for (Articulo a : lista) {
                    System.out.println(" > " + a.getDescripcion() + " - " + a.getPrecioVenta() + "€");
                }
            }

            // 2. Intentamos INSERTAR un artículo nuevo desde Java
            System.out.println("\n--- COMPROBANDO ESCRITURA ---");
            Articulo nuevo = new Articulo("A99", "Ratón Gaming", 45.0, 5.0, 10);
            articuloDAO.insertar(nuevo);
            System.out.println("✅ ¡Artículo 'A99' insertado con éxito desde Java!");

        } catch (Exception e) {
            System.err.println("❌ ERROR: Algo ha fallado:");
            e.printStackTrace();
        }
    }
}