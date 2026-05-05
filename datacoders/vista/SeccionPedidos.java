package datacoders.vista;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;
import datacoders.controlador.Controlador;
import datacoders.modelo.Pedido;
import java.time.LocalDateTime;

public class SeccionPedidos extends VBox {
    private TableView<Pedido> tablaPedidos;
    private Controlador controlador;

    // Campos para Crear Pedido
    private TextField txtEmail, txtArticulo, txtCantidad, txtNomCli, txtDomCli, txtNifCli;
    private ComboBox<String> cbTipoCli;
    private TextField txtEmailFiltro;

    public SeccionPedidos(Controlador controlador) {
        this.controlador = controlador;
        this.setPadding(new Insets(15));
        this.setSpacing(10);

        // --- 1. FORMULARIO CREAR PEDIDO ---
        TitledPane paneCrear = new TitledPane();
        paneCrear.setText("Crear Nuevo Pedido");
        paneCrear.setExpanded(false); // Se puede expandir para que no ocupe espacio

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(10));

        txtEmail = new TextField(); txtEmail.setPromptText("juanperez@gmail.com");
        txtArticulo = new TextField(); txtArticulo.setPromptText("Monitor1");
        txtCantidad = new TextField(); txtCantidad.setPromptText("3");
        txtNomCli = new TextField(); txtNomCli.setPromptText("Nombre del cliente");
        txtDomCli = new TextField(); txtDomCli.setPromptText("Domicilio del cliente");
        txtNifCli = new TextField(); txtNifCli.setPromptText("NIF del cliente");

        cbTipoCli = new ComboBox<>();
        cbTipoCli.getItems().addAll("Estandar", "Premium");
        cbTipoCli.setValue("Estandar");

        grid.add(new Label("Email cliente:"), 0, 0); grid.add(txtEmail, 1, 0);
        grid.add(new Label("Código artículo:"), 0, 1); grid.add(txtArticulo, 1, 1);
        grid.add(new Label("Cantidad:"), 0, 2); grid.add(txtCantidad, 1, 2);
        grid.add(new Label("Nombre cliente:"), 0, 3); grid.add(txtNomCli, 1, 3);
        grid.add(new Label("Domicilio cliente:"), 0, 4); grid.add(txtDomCli, 1, 4);
        grid.add(new Label("NIF cliente:"), 0, 5); grid.add(txtNifCli, 1, 5);
        grid.add(new Label("Tipo cliente:"), 0, 6); grid.add(cbTipoCli, 1, 6);

        Button btnCrear = new Button("Crear pedido");
        btnCrear.setMaxWidth(Double.MAX_VALUE);
        grid.add(btnCrear, 0, 7, 2, 1);

        paneCrear.setContent(grid);

        // --- 2. TABLA DE PEDIDOS (Ya la tenías) ---
        tablaPedidos = new TableView<>();
        tablaPedidos.setPlaceholder(new Label("Usa los botones para cargar datos"));

        TableColumn<Pedido, String> colNum = new TableColumn<>("Nº Pedido");
        colNum.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getNumPedido())));

        TableColumn<Pedido, String> colCli = new TableColumn<>("Cliente");
        colCli.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));

        TableColumn<Pedido, String> colArt = new TableColumn<>("Artículo");
        colArt.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArticulo().getDescripcion()));

        TableColumn<Pedido, String> colCant = new TableColumn<>("Cant.");
        colCant.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getCantidad())));

        TableColumn<Pedido, String> colTotal = new TableColumn<>("Precio Total");
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f€", c.getValue().calcularTotal())));

        tablaPedidos.getColumns().addAll(colNum, colCli, colArt, colCant, colTotal);
        tablaPedidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- 3. SECCIÓN DE CONSULTAS (Filtros) ---
        txtEmailFiltro = new TextField();
        txtEmailFiltro.setPromptText("Email filtro...");
        Button btnPendientes = new Button("Mostrar pendientes");
        Button btnEnviados = new Button("Mostrar enviados");
        Button btnTodos = new Button("Mostrar todos");

        HBox hbFiltros = new HBox(10, new Label("Filtro:"), txtEmailFiltro, btnPendientes, btnEnviados, btnTodos);

        // --- LÓGICA DE BOTONES ---
        btnCrear.setOnAction(e -> {
            try {
                // Formateamos los datos del cliente por si no existe
                String datosCliente = txtNomCli.getText() + "|" + txtDomCli.getText() + "|" + txtNifCli.getText() + "|" + cbTipoCli.getValue();

                controlador.addPedido(
                        txtEmail.getText(),
                        datosCliente,
                        txtArticulo.getText(),
                        Integer.parseInt(txtCantidad.getText()),
                        LocalDateTime.now()
                );

                limpiarCampos();
                btnTodos.fire(); // Refresca la tabla automáticamente
                new Alert(Alert.AlertType.INFORMATION, "Pedido creado correctamente").show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
            }
        });

        btnPendientes.setOnAction(e -> {
            tablaPedidos.getItems().setAll(controlador.getPedidosPendientes(txtEmailFiltro.getText()));
        });

        btnEnviados.setOnAction(e -> {
            tablaPedidos.getItems().setAll(controlador.getPedidosEnviados(txtEmailFiltro.getText()));
        });

        btnTodos.setOnAction(e -> {
            tablaPedidos.getItems().clear();
            tablaPedidos.getItems().addAll(controlador.getPedidosPendientes(""));
            tablaPedidos.getItems().addAll(controlador.getPedidosEnviados(""));
        });

        // AÑADIR TODO AL VBOX
        this.getChildren().addAll(new Label("GESTIÓN DE PEDIDOS"), paneCrear, new Separator(), hbFiltros, tablaPedidos);
        VBox.setVgrow(tablaPedidos, Priority.ALWAYS);
    }

    private void limpiarCampos() {
        txtEmail.clear(); txtArticulo.clear(); txtCantidad.clear();
        txtNomCli.clear(); txtDomCli.clear(); txtNifCli.clear();
    }
}