    package com.mycompany.domain.ui;

import com.mycompany.domain.exceptions.DatoInvalidoException;
import com.mycompany.domain.exceptions.PersistenciaException;
import com.mycompany.domain.exceptions.DuplicadoException;

import com.mycompany.domain.modelo.Producto;
import com.mycompany.domain.service.impl.InventarioServiceImpl;
import com.mycompany.domain.service.InventarioServiceLocal;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Capa de Presentación con JavaFX.
 * 
 * Responsabilidades:
 * - Mostrar la interfaz gráfica al usuario
 * - Capturar eventos de usuario (clicks, inputs)
 * - Comunicarse ÚNICAMENTE con la capa de Servicio
 * - NO accede directamente a la base de datos ni al DAO
 * - Maneja las excepciones y muestra mensajes amigables
 */
public class MainApp extends Application {
    
    // Inyección de dependencia de la capa de servicio
    private final InventarioServiceLocal inventarioService = (InventarioServiceLocal) new InventarioServiceImpl();
    
    // Contadores de operaciones para estadísticas
    private int operacionesExitosas = 0;
    private int operacionesAltas = 0;
    private int operacionesBajas = 0;
    private int operacionesActualizaciones = 0;
    
    // Componentes de la UI
    private TableView<Producto> tablaProductos;
    private ObservableList<Producto> productosObservable;
    private Label lblEstadisticas;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🏪 Mini-Tienda - Sistema de Gestión de Inventario");
        
        // Layout principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Construir las diferentes secciones de la UI
        VBox topPanel = crearPanelSuperior();
        root.setTop(topPanel);
        
        VBox centerPanel = crearTablaProductos();
        root.setCenter(centerPanel);
        
        VBox rightPanel = crearPanelBotones();
        root.setRight(rightPanel);
        
        HBox bottomPanel = crearPanelEstadisticas();
        root.setBottom(bottomPanel);
        
        // Crear y mostrar la escena
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Cargar datos iniciales
        cargarProductos();
    }
    
    /**
     * Crea el panel superior con título y subtítulo
     */
    private VBox crearPanelSuperior() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(10));
        
        Label titulo = new Label("🏪 SISTEMA DE GESTIÓN DE INVENTARIO");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitulo = new Label("Arquitectura por Capas con Manejo de Excepciones Personalizadas");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        panel.getChildren().addAll(titulo, subtitulo, new Separator());
        return panel;
    }
    
    /**
     * Crea la tabla de productos con todas sus columnas
     */
    private VBox crearTablaProductos() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        
        Label lblTabla = new Label("📦 Inventario de Productos");
        lblTabla.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Inicializar tabla
        tablaProductos = new TableView<>();
        productosObservable = FXCollections.observableArrayList();
        tablaProductos.setItems(productosObservable);
        
        // Columna ID
        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colId.setPrefWidth(50);
        
        // Columna Nombre
        TableColumn<Producto, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colNombre.setPrefWidth(300);
        
        // Columna Precio (con formato)
        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecio()).asObject());
        colPrecio.setPrefWidth(100);
        colPrecio.setCellFactory(col -> new TableCell<Producto, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                setText(empty ? null : String.format("$%.2f", precio));
            }
        });
        
        // Columna Stock
        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStock()).asObject());
        colStock.setPrefWidth(80);
        
        tablaProductos.getColumns().addAll(colId, colNombre, colPrecio, colStock);
        
        panel.getChildren().addAll(lblTabla, tablaProductos);
        VBox.setVgrow(tablaProductos, Priority.ALWAYS);
        return panel;
    }
    
    /**
     * Crea el panel lateral con todos los botones de acción
     */
    private VBox crearPanelBotones() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(200);
        
        Button btnAgregar = crearBoton("➕ Agregar Producto", "#27ae60");
        btnAgregar.setOnAction(e -> agregarProducto());
        
        Button btnActualizarPrecio = crearBoton("💰 Actualizar Precio", "#3498db");
        btnActualizarPrecio.setOnAction(e -> actualizarPrecio());
        
        Button btnActualizarStock = crearBoton("📊 Actualizar Stock", "#9b59b6");
        btnActualizarStock.setOnAction(e -> actualizarStock());
        
        Button btnEliminar = crearBoton("🗑️ Eliminar Producto", "#e74c3c");
        btnEliminar.setOnAction(e -> eliminarProducto());
        
        Button btnBuscar = crearBoton("🔍 Buscar Producto", "#f39c12");
        btnBuscar.setOnAction(e -> buscarProducto());
        
        Button btnRefrescar = crearBoton("🔄 Refrescar", "#95a5a6");
        btnRefrescar.setOnAction(e -> cargarProductos());
        
        Separator sep = new Separator();
        
        Button btnSalir = crearBoton("🚪 Salir", "#34495e");
        btnSalir.setOnAction(e -> {
            mostrarResumen();
            System.exit(0);
        });
        
        panel.getChildren().addAll(
            btnAgregar, btnActualizarPrecio, btnActualizarStock,
            btnEliminar, btnBuscar, btnRefrescar, sep, btnSalir
        );
        
        return panel;
    }
    
    /**
     * Crea un botón estilizado
     */
    private Button crearBoton(String texto, String color) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: white; " +
            "-fx-font-size: 12px; -fx-padding: 10px; -fx-cursor: hand;", color
        ));
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-opacity: 0.8;"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-opacity: 0.8;", "")));
        return btn;
    }
    
    /**
     * Crea el panel inferior con estadísticas
     */
    private HBox crearPanelEstadisticas() {
        HBox panel = new HBox(20);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1px;");
        
        lblEstadisticas = new Label();
        actualizarEstadisticas();
        
        panel.getChildren().add(lblEstadisticas);
        return panel;
    }
    
    /**
     * Actualiza el texto de las estadísticas
     */
    private void actualizarEstadisticas() {
        lblEstadisticas.setText(String.format(
            "📊 Operaciones Exitosas: %d | ➕ Altas: %d | ➖ Bajas: %d | ✏️ Actualizaciones: %d",
            operacionesExitosas, operacionesAltas, operacionesBajas, operacionesActualizaciones
        ));
    }
    /**
     * Carga todos los productos desde el servicio
     */
    private void cargarProductos() {
        try {
            List<Producto> productos = inventarioService.listarInventario();
            productosObservable.clear();
            productosObservable.addAll(productos);
        } catch (PersistenciaException e) {
            mostrarError("Error al cargar productos", e.getMessage());
        }
    }
    
    /**
     * Muestra diálogo para agregar un nuevo producto
     */
    private void agregarProducto() {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Agregar Producto");
        dialog.setHeaderText("Ingrese los datos del nuevo producto");
        
        ButtonType btnAgregar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAgregar, ButtonType.CANCEL);
        
        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Laptop HP");
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Ej: 799.99");
        TextField txtStock = new TextField();
        txtStock.setPromptText("Ej: 10");
        
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Precio:"), 0, 1);
        grid.add(txtPrecio, 1, 1);
        grid.add(new Label("Stock:"), 0, 2);
        grid.add(txtStock, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        // Procesar resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnAgregar) {
                try {
                    String nombre = txtNombre.getText();
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int stock = Integer.parseInt(txtStock.getText());
                    return new Producto(nombre, precio, stock);
                } catch (NumberFormatException e) {
                    mostrarError("Error de formato", "Precio y stock deben ser números válidos");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Producto> result = dialog.showAndWait();
        
        result.ifPresent(new Consumer<Producto>() {
            @Override
            public void accept(Producto producto) {
                try {
                    // Llamar a la capa de servicio
                    inventarioService.agregarProducto(
                            producto.getNombre(),
                            producto.getPrecio(),
                            producto.getStock()
                    );
                    operacionesExitosas++;
                    operacionesAltas++;
                    actualizarEstadisticas();
                    cargarProductos();
                    mostrarExito("✅ Producto agregado exitosamente");
                } catch (DatoInvalidoException e) {
                    mostrarError("Dato Inválido", e.getMessage());
                } catch (DuplicadoException e) {
                    mostrarError("Producto Duplicado", e.getMessage());
                } catch (PersistenciaException e) {
                    mostrarError("Error de Base de Datos", e.getMessage());
                }
            }
        });
    }
    
    /**
     * Actualiza el precio de un producto seleccionado
     */
    private void actualizarPrecio() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un producto de la tabla");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog(String.valueOf(seleccionado.getPrecio()));
        dialog.setTitle("Actualizar Precio");
        dialog.setHeaderText("Producto: " + seleccionado.getNombre());
        dialog.setContentText("Nuevo precio:");
        
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(precioStr -> {
            try {
                double nuevoPrecio = Double.parseDouble(precioStr);
                inventarioService.actualizarPrecio(seleccionado.getId(), nuevoPrecio);
                operacionesExitosas++;
                operacionesActualizaciones++;
                actualizarEstadisticas();
                cargarProductos();
                mostrarExito("✅ Precio actualizado exitosamente");
            } catch (NumberFormatException e) {
                mostrarError("Error de formato", "El precio debe ser un número válido");
            } catch (DatoInvalidoException e) {
                mostrarError("Dato Inválido", e.getMessage());
            } catch (PersistenciaException e) {
                mostrarError("Error de Base de Datos", e.getMessage());
            }
        });
    }
    
    /**
     * Actualiza el stock de un producto seleccionado
     */
    private void actualizarStock() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un producto de la tabla");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog(String.valueOf(seleccionado.getStock()));
        dialog.setTitle("Actualizar Stock");
        dialog.setHeaderText("Producto: " + seleccionado.getNombre());
        dialog.setContentText("Nuevo stock:");
        
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(stockStr -> {
            try {
                int nuevoStock = Integer.parseInt(stockStr);
                inventarioService.actualizarStock(seleccionado.getId(), nuevoStock);
                operacionesExitosas++;
                operacionesActualizaciones++;
                actualizarEstadisticas();
                cargarProductos();
                mostrarExito("✅ Stock actualizado exitosamente");
            } catch (NumberFormatException e) {
                mostrarError("Error de formato", "El stock debe ser un número entero válido");
            } catch (DatoInvalidoException e) {
                mostrarError("Dato Inválido", e.getMessage());
            } catch (PersistenciaException e) {
                mostrarError("Error de Base de Datos", e.getMessage());
            }
        });
    }
    
    /**
     * Elimina un producto seleccionado
     */
    private void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un producto de la tabla");
            return;
        }
        
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar este producto?");
        confirmacion.setContentText(seleccionado.getNombre());
        
        Optional<ButtonType> result = confirmacion.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                inventarioService.eliminarProducto(seleccionado.getId());
                operacionesExitosas++;
                operacionesBajas++;
                actualizarEstadisticas();
                cargarProductos();
                mostrarExito("✅ Producto eliminado exitosamente");
            } catch (PersistenciaException e) {
                mostrarError("Error de Base de Datos", e.getMessage());
            }
        }
    }
    
    /**
     * Busca un producto por nombre
     */
    private void buscarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Producto");
        dialog.setHeaderText("Ingrese el nombre del producto a buscar");
        dialog.setContentText("Nombre:");
        
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(nombre -> {
            try {
                Producto producto = inventarioService.buscarProducto(nombre);
                
                if (producto != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Producto Encontrado");
                    alert.setHeaderText("✅ Resultado de la búsqueda");
                    alert.setContentText(producto.toString());
                    alert.showAndWait();
                    
                    // Seleccionar en la tabla
                    tablaProductos.getSelectionModel().select(producto);
                    tablaProductos.scrollTo(producto);
                } else {
                    mostrarAdvertencia("No se encontró ningún producto con ese nombre");
                }
            } catch (PersistenciaException e) {
                mostrarError("Error de Base de Datos", e.getMessage());
            }
        });
    }
    
    /**
     * Muestra resumen final al salir
     */
    private void mostrarResumen() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resumen de Operaciones");
        alert.setHeaderText("🏁 Sesión Finalizada");
        
        String resumen = String.format(
            "📊 Estadísticas de la sesión:\n\n" +
            "✅ Operaciones exitosas: %d\n" +
            "➕ Productos agregados: %d\n" +
            "➖ Productos eliminados: %d\n" +
            "✏️ Actualizaciones realizadas: %d\n\n" +
            "¡Gracias por usar el sistema!",
            operacionesExitosas, operacionesAltas, operacionesBajas, operacionesActualizaciones
        );
        
        alert.setContentText(resumen);
        alert.showAndWait();
    }
    
    // Métodos auxiliares para mostrar mensajes
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}