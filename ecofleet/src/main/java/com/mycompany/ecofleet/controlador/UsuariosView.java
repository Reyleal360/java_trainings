package com.mycompany.ecofleet.controlador;

import com.mycompany.ecofleet.dao.UsuarioDao;
import com.mycompany.ecofleet.modelo.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class UsuariosView {
    private UsuarioDao usuarioDAO = new UsuarioDao();
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Gestión de Usuarios");

        TextField nombreField = new TextField(); nombreField.setPromptText("Nombre");
        TextField emailField = new TextField(); emailField.setPromptText("Email");
        TextField telField = new TextField(); telField.setPromptText("Teléfono");
        Button agregarBtn = new Button("Agregar");

        TableView<Usuario> tabla = new TableView<>();
        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));

        TableColumn<Usuario, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Usuario, String> colTel = new TableColumn<>("Teléfono");
        colTel.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));

        tabla.getColumns().addAll(colId, colNombre, colEmail, colTel);

        HBox form = new HBox(10, nombreField, emailField, telField, agregarBtn);
        BorderPane root = new BorderPane();
        root.setTop(form);
        root.setCenter(tabla);

        // Cargar datos iniciales
        cargarUsuarios(tabla);

        // Acción botón
        agregarBtn.setOnAction(e -> {
            try {
                Usuario u = new Usuario(null, nombreField.getText(), emailField.getText(), telField.getText());
                usuarioDAO.create(u);
                usuarios.add(u);
                tabla.setItems(usuarios);
                nombreField.clear(); emailField.clear(); telField.clear();
            } catch (Exception ex) {
                mostrarError("Error creando usuario: " + ex.getMessage());
            }
        });

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void cargarUsuarios(TableView<Usuario> tabla) {
        try {
            usuarios.setAll(usuarioDAO.findAll());
            tabla.setItems(usuarios);
        } catch (Exception e) {
            mostrarError("Error cargando usuarios: " + e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
