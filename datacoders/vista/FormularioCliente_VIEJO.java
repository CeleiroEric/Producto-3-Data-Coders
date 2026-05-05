package datacoders.vista;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import datacoders.controlador.Controlador;

public class FormularioCliente_VIEJO extends VBox {
    private TextField txtNombre, txtDom, txtNif, txtEmail;
    private ComboBox<String> cbTipo;
    private TextArea area;
    private Controlador controlador;

    public FormularioCliente_VIEJO(Controlador controlador) {
        this.controlador = controlador;
        this.setPadding(new Insets(15));
        this.setSpacing(10);

        // Campos de texto
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtDom = new TextField(); txtDom.setPromptText("Domicilio");
        txtNif = new TextField(); txtNif.setPromptText("NIF");
        txtEmail = new TextField(); txtEmail.setPromptText("Email");

        cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Estandar", "Premium");
        cbTipo.setValue("Estandar");

        // Fila de botones de listado
        HBox filaBotones = new HBox(10);
        Button btnAdd = new Button("Añadir Cliente");
        Button btnTodos = new Button("Mostrar todos");
        Button btnEstandar = new Button("Mostrar estándar");
        Button btnPremium = new Button("Mostrar premium");
        filaBotones.getChildren().addAll(btnAdd, btnTodos, btnEstandar, btnPremium);

        area = new TextArea();
        area.setEditable(false);

        // Lógica de botones
        btnAdd.setOnAction(e -> {
            try {
                if(cbTipo.getValue().equals("Estandar"))
                    controlador.addClienteEstandar(txtNombre.getText(), txtDom.getText(), txtNif.getText(), txtEmail.getText());
                else
                    controlador.addClientePremium(txtNombre.getText(), txtDom.getText(), txtNif.getText(), txtEmail.getText());
                area.setText("Cliente registrado.");
            } catch (Exception ex) { area.setText("Error: " + ex.getMessage()); }
        });

        // Eventos para listar (convertimos la lista a String para el TextArea)
        btnTodos.setOnAction(e -> area.setText(controlador.getClientes().toString()));
        btnEstandar.setOnAction(e -> area.setText(controlador.getClientesEstandar().toString()));
        btnPremium.setOnAction(e -> area.setText(controlador.getClientesPremium().toString()));

        this.getChildren().addAll(new Label("CLIENTES"), txtNombre, txtDom, txtNif, txtEmail, cbTipo, filaBotones, area);
    }
}