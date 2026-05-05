package datacoders.vista;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import datacoders.controlador.Controlador;
import datacoders.modelo.Articulo;
import javafx.geometry.Insets;

public class SeccionArticulos extends HBox {
    private TableView<Articulo> tabla;
    private Controlador controlador;

    public SeccionArticulos(Controlador controlador) {
        this.controlador = controlador;
        this.setPadding(new Insets(10));
        this.setSpacing(10);

        FormularioArticulo form = new FormularioArticulo(controlador, this);

        tabla = new TableView<>();
        configurarColumnas();
        refrescarTabla();

        this.getChildren().addAll(form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
    }

    private void configurarColumnas() {
        TableColumn<Articulo, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Articulo, String> colDes = new TableColumn<>("Descripción");
        colDes.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Articulo, Double> colPre = new TableColumn<>("Precio Venta");
        colPre.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));

        TableColumn<Articulo, Double> colGastos = new TableColumn<>("Gastos Envío");
        colGastos.setCellValueFactory(new PropertyValueFactory<>("gastosEnvio"));

        // Aquí usamos tiempoPreparacion para que Java busque getTiempoPreparacion()
        TableColumn<Articulo, Integer> colTiempo = new TableColumn<>("Tiempo Prep.");
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoPreparacionMin"));

        tabla.getColumns().addAll(colCod, colDes, colPre, colGastos, colTiempo);
    }

    public void refrescarTabla() {
        if (controlador != null) {
            tabla.getItems().clear();
            tabla.getItems().addAll(controlador.getArticulos());
        }
    }
}