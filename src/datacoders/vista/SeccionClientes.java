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
    private TableView<Cliente> tablaClientes;
    private Controlador controlador;

    public SeccionClientes(Controlador controlador) {
        this.controlador = controlador;
        this.setPadding(new Insets(15));
        this.setSpacing(10);

        Label lblTitulo = new Label("GESTIÓN DE CLIENTES");
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        txtDomicilio = new TextField();
        txtDomicilio.setPromptText("Domicilio");

        txtNif = new TextField();
        txtNif.setPromptText("NIF");

        txtEmail = new TextField();
        txtEmail.setPromptText("Email");

        cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Estandar", "Premium");
        cbTipo.setValue("Estandar");

        Button btnAñadir = new Button("Añadir Cliente");
        btnAñadir.setMaxWidth(Double.MAX_VALUE);

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

        tablaClientes.getColumns().setAll(colEmail, colNom, colDom, colNif, colTipo, colCuota, colDesc);
        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox hbBotones = new HBox(10);
        Button btnTodos = new Button("Mostrar todos");
        Button btnEstandar = new Button("Mostrar estándar");
        Button btnPremium = new Button("Mostrar premium");
        hbBotones.getChildren().addAll(btnTodos, btnEstandar, btnPremium);

        btnAñadir.setOnAction(e -> {
            try {
                if (cbTipo.getValue().equals("Premium")) {
                    controlador.addClientePremium(
                            txtNombre.getText(),
                            txtDomicilio.getText(),
                            txtNif.getText(),
                            txtEmail.getText()
                    );
                } else {
                    controlador.addClienteEstandar(
                            txtNombre.getText(),
                            txtDomicilio.getText(),
                            txtNif.getText(),
                            txtEmail.getText()
                    );
                }

                limpiar();
                btnTodos.fire();
                new Alert(Alert.AlertType.INFORMATION, "Cliente añadido correctamente").show();
            }

                catch (DuplicadoException ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                }
        });

        btnTodos.setOnAction(e -> tablaClientes.getItems().setAll(controlador.getClientes()));
        btnEstandar.setOnAction(e -> tablaClientes.getItems().setAll(controlador.getClientesEstandar()));
        btnPremium.setOnAction(e -> tablaClientes.getItems().setAll(controlador.getClientesPremium()));

        this.getChildren().addAll(
                lblTitulo,
                txtNombre,
                txtDomicilio,
                txtNif,
                txtEmail,
                cbTipo,
                btnAñadir,
                new Separator(),
                hbBotones,
                tablaClientes
        );

        VBox.setVgrow(tablaClientes, Priority.ALWAYS);
    }

    private void limpiar() {
        txtNombre.clear();
        txtDomicilio.clear();
        txtNif.clear();
        txtEmail.clear();
        cbTipo.setValue("Estandar");
    }
}