/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Coder
 */
package com.mycompany.ecofleet.controlador;

import com.mycompany.ecofleet.dao.VehiculoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class VehiculosView {
    private VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private ObservableList<String> vehiculos = FXCollections.observableArrayList();

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Gestión de Vehículos");

        ChoiceBox<String> tipoChoice = new ChoiceBox<>();
        tipoChoice.setItems(FXCollections.observableArrayList("AUTO", "BICICLETA"));
        TextField marcaField = new TextField(); marcaField.setPromptText("Marca");
        TextField modeloField = new TextField(); modeloField.setPromptText("Modelo");
        TextField placaField = new TextField(); placaField.setPromptText("Placa (solo auto)");
        Button agregarBtn = new Button("Agregar");

        TableView<String> tabla = new TableView<>();
        TableColumn<String, String> colDatos = new TableColumn<>("Vehículos");
        colDatos.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        tabla.getColumns().add(colDatos);

        HBox form = new HBox(10, tipoChoice, marcaField, modeloField, placaField, agregarBtn);
        BorderPane root = new BorderPane();
        root.setTop(form);
        root.setCenter(tabla);

        cargarVehiculos(tabla);

        agregarBtn.setOnAction(e -> {
            try {
                String tipo = tipoChoice.getValue();
                String marca = marcaField.getText();
                String modelo = modeloField.getText();
                String placa = tipo != null && tipo.equals("AUTO") ? placaField.getText() : null;

                vehiculoDAO.createVehiculo(tipo, marca, modelo, placa, true);
                cargarVehiculos(tabla);
                marcaField.clear(); modeloField.clear(); placaField.clear();
            } catch (Exception ex) {
                mostrarError("Error creando vehículo: " + ex.getMessage());
            }
        });

        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    private void cargarVehiculos(TableView<String> tabla) {
        try {
            vehiculos.setAll(vehiculoDAO.listarTodos());
            tabla.setItems(vehiculos);
        } catch (Exception e) {
            mostrarError("Error cargando vehículos: " + e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
