package com.mycompany.ecofleet.controlador;

import com.mycompany.ecofleet.servicio.AlquilerServicio;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AlquilerView {
    private AlquilerServicio servicio = new AlquilerServicio();

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Gestión de Alquileres");

        // Alquilar
        TextField usuarioIdField = new TextField(); usuarioIdField.setPromptText("Usuario ID");
        TextField vehiculoIdField = new TextField(); vehiculoIdField.setPromptText("Vehículo ID");
        Button alquilarBtn = new Button("Alquilar");
        HBox alquilarBox = new HBox(10, usuarioIdField, vehiculoIdField, alquilarBtn);

        // Devolver
        TextField alquilerIdField = new TextField(); alquilerIdField.setPromptText("Alquiler ID");
        TextField vehiculoDevIdField = new TextField(); vehiculoDevIdField.setPromptText("Vehículo ID");
        TextField tipoVehiculoField = new TextField(); tipoVehiculoField.setPromptText("AUTO/BICICLETA");
        TextField kmField = new TextField(); kmField.setPromptText("Km recorridos");
        Button devolverBtn = new Button("Devolver");
        HBox devolverBox = new HBox(10, alquilerIdField, vehiculoDevIdField, tipoVehiculoField, kmField, devolverBtn);

        VBox root = new VBox(15,
                new Label("Alquilar Vehículo"), alquilarBox,
                new Label("Devolver Vehículo"), devolverBox
        );
        root.setStyle("-fx-padding: 15;");

        alquilarBtn.setOnAction(e -> {
            try {
                int uid = Integer.parseInt(usuarioIdField.getText());
                int vid = Integer.parseInt(vehiculoIdField.getText());
                servicio.alquilar(uid, vid);
            } catch (Exception ex) {
                mostrarError("Error al alquilar: " + ex.getMessage());
            }
        });

        devolverBtn.setOnAction(e -> {
            try {
                int aid = Integer.parseInt(alquilerIdField.getText());
                int vid = Integer.parseInt(vehiculoDevIdField.getText());
                String tipo = tipoVehiculoField.getText();
                double km = Double.parseDouble(kmField.getText());
                servicio.devolver(aid, vid, km, tipo);
            } catch (Exception ex) {
                mostrarError("Error al devolver: " + ex.getMessage());
            }
        });

        stage.setScene(new Scene(root, 700, 250));
        stage.show();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
