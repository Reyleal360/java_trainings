/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet.controlador;

/**
 *
 * @author Coder
 */

import com.mycompany.ecofleet.dao.AlquilerDAO;
import com.mycompany.ecofleet.modelo.Alquiler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HistorialView {
    private AlquilerDAO alquilerDAO = new AlquilerDAO();
    private ObservableList<Alquiler> historial = FXCollections.observableArrayList();

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Historial de Alquileres");

        TableView<Alquiler> tabla = new TableView<>();
        TableColumn<Alquiler, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<Alquiler, Integer> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getUsuarioId()));

        TableColumn<Alquiler, Integer> colVehiculo = new TableColumn<>("Vehículo");
        colVehiculo.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getVehiculoId()));

        TableColumn<Alquiler, String> colInicio = new TableColumn<>("Inicio");
        colInicio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFechaInicio() != null ? data.getValue().getFechaInicio().toString() : ""
        ));

        TableColumn<Alquiler, String> colFin = new TableColumn<>("Fin");
        colFin.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFechaFin() != null ? data.getValue().getFechaFin().toString() : ""
        ));

        TableColumn<Alquiler, Double> colKm = new TableColumn<>("Km");
        colKm.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getKmRecorridos()));

        TableColumn<Alquiler, Double> colCosto = new TableColumn<>("Costo");
        colCosto.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCostoTotal()));

        tabla.getColumns().addAll(colId, colUsuario, colVehiculo, colInicio, colFin, colKm, colCosto);

        BorderPane root = new BorderPane(tabla);

        cargarHistorial(tabla);

        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }

    private void cargarHistorial(TableView<Alquiler> tabla) {
        try {
            historial.setAll(alquilerDAO.listarTodos());
            tabla.setItems(historial);
        } catch (Exception e) {
            mostrarError("Error cargando historial: " + e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
