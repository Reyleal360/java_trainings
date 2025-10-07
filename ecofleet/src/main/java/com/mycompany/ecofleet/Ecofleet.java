package com.mycompany.ecofleet;

import com.mycompany.ecofleet.controlador.UsuariosView;
import com.mycompany.ecofleet.controlador.VehiculosView;
import com.mycompany.ecofleet.controlador.AlquilerView;
import com.mycompany.ecofleet.controlador.HistorialView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Ecofleet extends Application {
    @Override
    public void start(Stage stage) {
        // Menú principal
        Label titulo = new Label("EcoFleet - Menú Principal");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button usuariosBtn = new Button("Usuarios");
        Button vehiculosBtn = new Button("Vehículos");
        Button alquileresBtn = new Button("Alquileres");
        Button historialBtn = new Button("Historial");

        usuariosBtn.setOnAction(e -> new UsuariosView().mostrar());
        vehiculosBtn.setOnAction(e -> new VehiculosView().mostrar());
        alquileresBtn.setOnAction(e -> new AlquilerView().mostrar());
        historialBtn.setOnAction(e -> new HistorialView().mostrar());

        VBox root = new VBox(15, titulo, usuariosBtn, vehiculosBtn, alquileresBtn, historialBtn);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");

        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("EcoFleet");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
