package datacoders.vista;

import datacoders.modelo.*;
import datacoders.controlador.*;
import java.time.LocalDateTime;
import java.util.List;

public class GestionStore {

    private Controlador controlador;

    public GestionStore() {
        this.controlador = new Controlador();
    }

    public void inicio() {
        boolean salir = false;
        do {
            System.out.println("\n===== GESTIÓN TIENDA ONLINE (CONSOLA) =====");
            System.out.println("1. Gestión de Artículos");
            System.out.println("2. Gestión de Clientes");
            System.out.println("3. Gestión de Pedidos");
            System.out.println("0. Salir");

            int opcion = LeerDatos.leerEntero("Selecciona una opción: ");

            switch (opcion) {
                case 1 -> menuArticulos();
                case 2 -> menuClientes();
                case 3 -> menuPedidos();
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        } while (!salir);
    }

    // --- SUBMENÚ ARTÍCULOS ---
    private void menuArticulos() {
        System.out.println("\n-- ARTÍCULOS --");
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        int opt = LeerDatos.leerEntero("Selecciona: ");

        if (opt == 1) {
            String cod = LeerDatos.leerTexto("Código: ");
            String desc = LeerDatos.leerTexto("Descripción: ");
            double precio = LeerDatos.leerDouble("Precio: ");
            double envio = LeerDatos.leerDouble("Gastos Envío: ");
            int tiempo = LeerDatos.leerEntero("Tiempo Prep (min): ");

            try {
                controlador.addArticulo(cod, desc, precio, envio, tiempo);
                System.out.println("Artículo añadido correctamente.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("\nCÓDIGO | DESCRIPCIÓN | PRECIO | GASTOS");
            System.out.println("---------------------------------------");
            for (Articulo a : controlador.getArticulos()) {
                // CORRECCIÓN: Usamos getPrecioVenta() que es el nombre real en tu modelo
                System.out.println(a.getCodigo() + " | " + a.getDescripcion() + " | " + a.getPrecioVenta() + "€ | " + a.getGastosEnvio() + "€");
            }
        }
    }

    // --- SUBMENÚ CLIENTES ---
    private void menuClientes() {
        System.out.println("\n-- CLIENTES --");
        System.out.println("1. Añadir Cliente");
        System.out.println("2. Mostrar Todos");
        System.out.println("3. Mostrar Estándar");
        System.out.println("4. Mostrar Premium");

        int opt = LeerDatos.leerEntero("Selecciona: ");

        if (opt == 1) {
            String email = LeerDatos.leerTexto("Email: ");
            String nombre = LeerDatos.leerTexto("Nombre: ");
            String domicilio = LeerDatos.leerTexto("Domicilio: ");
            String nif = LeerDatos.leerTexto("NIF: ");
            String tipo = LeerDatos.leerTexto("Tipo (Estandar/Premium): ");

            try {
                if (tipo.equalsIgnoreCase("Estandar")) {
                    controlador.addClienteEstandar(nombre, domicilio, nif, email);
                    System.out.println("Cliente Estándar añadido!");
                } else {
                    controlador.addClientePremium(nombre, domicilio, nif, email);
                    System.out.println("Cliente Premium añadido!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } else {
            List<Cliente> lista;
            if (opt == 3) lista = controlador.getClientesEstandar();
            else if (opt == 4) lista = controlador.getClientesPremium();
            else lista = controlador.getClientes();

            System.out.println("\nEMAIL | NOMBRE | NIF | TIPO | EXTRA");
            System.out.println("---------------------------------------");
            for (Cliente c : lista) {
                String tipoCli = (c instanceof ClientePremium) ? "PREMIUM" : "ESTANDAR";
                String extra = "";
                if (c instanceof ClientePremium) {
                    extra = " | Cuota: " + ((ClientePremium) c).getCuotaAnual() + "€";
                }
                System.out.println(c.getEmail() + " | " + c.getNombre() + " | " + c.getNif() + " | " + tipoCli + extra);
            }
        }
    }

    // --- SUBMENÚ PEDIDOS ---
    private void menuPedidos() {
        System.out.println("\n-- PEDIDOS --");
        System.out.println("1. Añadir Pedido");
        System.out.println("2. Eliminar Pedido");
        System.out.println("3. Mostrar Pedidos Pendientes");
        System.out.println("4. Mostrar Pedidos Enviados");
        int opt = LeerDatos.leerEntero("Selecciona: ");

        try {
            switch (opt) {
                case 1 -> {
                    String email = LeerDatos.leerTexto("Email Cliente: ");
                    String cod = LeerDatos.leerTexto("Código Artículo: ");
                    int cant = LeerDatos.leerEntero("Cantidad: ");

                    // Nota: El controlador gestiona si el cliente existe o no
                    controlador.addPedido(email, "Nuevo Cliente", cod, cant, LocalDateTime.now());
                    System.out.println("Pedido registrado con éxito.");
                }
                case 2 -> {
                    int num = LeerDatos.leerEntero("Número de pedido a cancelar: ");
                    controlador.eliminarPedido(num, LocalDateTime.now());
                    System.out.println("Acción procesada.");
                }
                case 3, 4 -> {
                    String email = LeerDatos.leerTexto("Filtrar por email (vacío para todos): ");
                    List<Pedido> pedidos = (opt == 3) ? controlador.getPedidosPendientes(email) : controlador.getPedidosEnviados(email);

                    if (pedidos.isEmpty()) {
                        System.out.println("No hay pedidos para mostrar.");
                    } else {
                        for (Pedido p : pedidos) {
                            System.out.println(p.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}