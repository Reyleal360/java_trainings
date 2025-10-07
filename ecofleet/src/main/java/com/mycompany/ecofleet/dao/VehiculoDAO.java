/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet.dao;

/**
 *
 * @author Coder
 */

import com.mycompany.ecofleet.config.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.ecofleet.modelo.Auto;
import com.mycompany.ecofleet.modelo.Bicicleta;
import com.mycompany.ecofleet.modelo.Vehiculo;

public class VehiculoDAO {

    public Integer createVehiculo(String tipo, String marca, String modelo, String placa, boolean disponible) throws SQLException {
        String sql = "INSERT INTO vehiculos (tipo, marca, modelo, placa, disponible) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo);
            ps.setString(2, marca);
            ps.setString(3, modelo);
            ps.setString(4, placa);
            ps.setBoolean(5, disponible);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    public boolean isDisponible(int vehiculoId) throws SQLException {
        String sql = "SELECT disponible FROM vehiculos WHERE id = ?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vehiculoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean("disponible");
            }
        }
        throw new SQLException("Vehículo no encontrado: " + vehiculoId);
    }

    public void setDisponibilidad(Connection c, int vehiculoId, boolean disponible) throws SQLException {
        String sql = "UPDATE vehiculos SET disponible = ? WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, disponible);
            ps.setInt(2, vehiculoId);
            int updated = ps.executeUpdate();
            if (updated != 1) throw new SQLException("No se pudo actualizar disponibilidad");
        }
    }

    public List<String> listarTodos() throws SQLException {
        List<String> res = new ArrayList<>();
        String sql = "SELECT id, tipo, marca, modelo, placa, disponible FROM vehiculos";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res.add(String.format("%d - %s %s %s (placa=%s) Disponible=%s",
                                     rs.getInt("id"), rs.getString("tipo"), rs.getString("marca"),
                                     rs.getString("modelo"), rs.getString("placa"), rs.getBoolean("disponible")));
            }
        }
        return res;
    }
}