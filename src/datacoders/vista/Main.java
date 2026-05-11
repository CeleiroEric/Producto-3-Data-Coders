package datacoders.vista;

import datacoders.controlador.Controlador;
import datacoders.modelo.util.JPAUtil;
import javafx.application.Application;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        Controlador controlador = new Controlador();
        VentanaPrincipal raiz = new VentanaPrincipal(controlador);

        Scene escena = new Scene(raiz, 1100, 750);

        primaryStage.setTitle("DataCoders - Sistema de Gestión Profesional");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    @Override
    public void stop() {
        JPAUtil.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}