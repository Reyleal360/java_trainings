/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet.servicio;

/**
 *
 * @author Coder
 */

import com.mycompany.ecofleet.config.ConexionDB;
import com.mycompany.ecofleet.dao.VehiculoDAO;
import com.mycompany.ecofleet.dao.AlquilerDAO;
import com.mycompany.ecofleet.dao.UsuarioDao;

import java.sql.Connection;
import java.sql.SQLException;

public class AlquilerServicio {
    private VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private AlquilerDAO alquilerDAO = new AlquilerDAO();
    private UsuarioDao usuarioDao = new UsuarioDao();

    public boolean alquilar(int usuarioId, int vehiculoId) {
        try (Connection c = ConexionDB.getConnection()) {
            c.setAutoCommit(false);
            // verificar existencia usuario (puedes mejorar con findById)
            // verificar disponibilidad del vehículo
            boolean disponible = vehiculoDAO.isDisponible(vehiculoId);
            if (!disponible) {
                System.out.println("Vehículo no disponible.");
                return false;
            }
            // crear alquiler y actualizar disponibilidad dentro de la misma transacción
            Integer alquilerId = alquilerDAO.crearAlquiler(c, usuarioId, vehiculoId);
            if (alquilerId == null) throw new SQLException("No se creó el alquiler");
            vehiculoDAO.setDisponibilidad(c, vehiculoId, false);
            c.commit();
            System.out.println("Alquiler creado id=" + alquilerId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al alquilar: " + e.getMessage());
            return false;
        }
    }

    public boolean devolver(int alquilerId, int vehiculoId, double kmRecorridos, String tipoVehiculo) {
        if (kmRecorridos <= 0) {
            System.out.println("KM recorridos debe ser > 0");
            return false;
        }
        try (Connection c = ConexionDB.getConnection()) {
            c.setAutoCommit(false);
            // calcular costo según tipo
            double costo;
            if ("AUTO".equalsIgnoreCase(tipoVehiculo)) {
                // ejemplo: tarifaAuto
                double tarifaBase = 5.0;
                double porKm = 0.8;
                costo = tarifaBase + porKm * kmRecorridos;
            } else {
                double tarifaBase = 1.0;
                double porKm = 0.2;
                costo = tarifaBase + porKm * kmRecorridos;
            }
            // actualizar alquiler
            alquilerDAO.cerrarAlquiler(c, alquilerId, kmRecorridos, costo);
            // marcar vehículo disponible
            vehiculoDAO.setDisponibilidad(c, vehiculoId, true);
            c.commit();
            System.out.println("Alquiler cerrado. Costo: " + costo);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al devolver: " + e.getMessage());
            return false;
        }
    }
}