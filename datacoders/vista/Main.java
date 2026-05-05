package datacoders.vista;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import datacoders.controlador.Controlador;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Usamos el mismo controlador
        Controlador controlador = new Controlador();

        // Instanciamos la VentanaPrincipal (asegúrate de que use SeccionClientes con TableView)
        VentanaPrincipal raiz = new VentanaPrincipal(controlador);

        Scene escena = new Scene(raiz, 1100, 750);

        primaryStage.setTitle("DataCoders - Sistema de Gestión Profesional");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("¿Cómo deseas iniciar la aplicación?");
        System.out.println("1. Interfaz Gráfica (Ventanas)");
        System.out.println("2. Consola (Modo Texto)");

        String opcion = sc.nextLine();

        if (opcion.equals("1")) {
            launch(args); // Lanza JavaFX
        } else {
            // Lanza el menú de consola de toda la vida
            GestionStore gestion = new GestionStore();
            gestion.inicio();
        }
    }
}