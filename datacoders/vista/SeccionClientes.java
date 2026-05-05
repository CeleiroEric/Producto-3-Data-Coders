package datacoders.vista;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;
import datacoders.controlador.Controlador;
import datacoders.modelo.Cliente;
import datacoders.modelo.ClientePremium;
import datacoders.modelo.excepciones.DuplicadoException;

public class SeccionClientes extends VBox {
    private TextField txtNombre, txtDomicilio, txtNif, txtEmail;
    private ComboBox<String> cbTipo;
    private TableView<Cliente> tablaClientes; // <--- ESTO es lo que hace que salgan columnas
    private Controlador controlador;

    public SeccionClientes(Controlador controlador) {
        this.controlador = controlador;
        this.setPadding(new Insets(15));
        this.setSpacing(10);

        Label lblTitulo = new Label("GESTIÓN DE CLIENTES");
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // --- FORMULARIO ---
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtDomicilio = new TextField(); txtDomicilio.setPromptText("Domicilio");
        txtNif = new TextField(); txtNif.setPromptText("NIF");
        txtEmail = new TextField(); txtEmail.setPromptText("Email");
        cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Estandar", "Premium");
        cbTipo.setValue("Estandar");

        Button btnAñadir = new Button("Añadir Cliente");
        btnAñadir.setMaxWidth(Double.MAX_VALUE);

        // --- AQUÍ SE CREAN LAS COLUMNAS (Lo que te faltaba) ---
        tablaClientes = new TableView<>();

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<Cliente, String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));

        TableColumn<Cliente, String> colDom = new TableColumn<>("Domicilio");
        colDom.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDomicilio()));

        TableColumn<Cliente, String> colNif = new TableColumn<>("NIF");
        colNif.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNif()));

        TableColumn<Cliente, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(
                (c.getValue() instanceof ClientePremium) ? "Premium" : "Estandar"
        ));

        TableColumn<Cliente, String> colCuota = new TableColumn<>("Cuota");
        colCuota.setCellValueFactory(c -> {
            if (c.getValue() instanceof ClientePremium) {
                return new SimpleStringProperty(((ClientePremium) c.getValue()).getCuotaAnual() + "€");
            }
            return new SimpleStringProperty("-");
        });

        TableColumn<Cliente, String> colDesc = new TableColumn<>("Descuento");
        colDesc.setCellValueFactory(c -> {
            if (c.getValue() instanceof ClientePremium) {
                return new SimpleStringProperty(((ClientePremium) c.getValue()).getDescuentoEnvio() + "%");
            }
            return new SimpleStringProperty("0%");
        });

        // Metemos las 7 columnas en la tabla
        tablaClientes.getColumns().setAll(colEmail, colNom, colDom, colNif, colTipo, colCuota, colDesc);
        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- BOTONES ---
        HBox hbBotones = new HBox(10);
        Button btnTodos = new Button("Mostrar todos");
        hbBotones.getChildren().add(btnTodos);

        btnAñadir.setOnAction(e -> {
            try {
                if (cbTipo.getValue().equals("Premium")) {
                    controlador.addClientePremium(txtNombre.getText(), txtDomicilio.getText(), txtNif.getText(), txtEmail.getText());
                } else {
                    controlador.addClienteEstandar(txtNombre.getText(), txtDomicilio.getText(), txtNif.getText(), txtEmail.getText());
                }
                limpiar();
                btnTodos.fire(); // Refresca la tabla automáticamente
            } catch (DuplicadoException ex) {
                new Alert(Alert.AlertType.WARNING, "Email ya existe").show();
            }
        });

        btnTodos.setOnAction(e -> {
            tablaClientes.getItems().clear();
            tablaClientes.getItems().addAll(controlador.getClientes());
        });

        // IMPORTANTE: Aquí NO debe haber ningún "areaTexto"
        this.getChildren().addAll(lblTitulo, txtNombre, txtDomicilio, txtNif, txtEmail, cbTipo, btnAñadir, new Separator(), hbBotones, tablaClientes);
    }

    private void limpiar() {
        txtNombre.clear(); txtDomicilio.clear(); txtNif.clear(); txtEmail.clear();
    }
}