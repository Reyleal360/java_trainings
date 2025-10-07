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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.ecofleet.modelo.Alquiler;

public class AlquilerDAO {

    public Integer crearAlquiler(Connection c, int usuarioId, int vehiculoId) throws SQLException {
        String sql = "INSERT INTO alquileres (usuario_id, vehiculo_id, fecha_inicio) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, vehiculoId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    public void cerrarAlquiler(Connection c, int alquilerId, double kmRecorridos, double costoTotal) throws SQLException {
        String sql = "UPDATE alquileres SET fecha_fin = CURRENT_TIMESTAMP, km_recorridos = ?, costo_total = ? WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, kmRecorridos);
            ps.setDouble(2, costoTotal);
            ps.setInt(3, alquilerId);
            int updated = ps.executeUpdate();
            if (updated != 1) throw new SQLException("No se pudo cerrar alquiler " + alquilerId);
        }
    }

    public List<Alquiler> listarTodos() throws SQLException {
        List<Alquiler> list = new ArrayList<>();
        String sql = "SELECT id, usuario_id, vehiculo_id, fecha_inicio, fecha_fin, km_recorridos, costo_total FROM alquileres ORDER BY fecha_inicio DESC";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Alquiler a = new Alquiler();
                a.setId(rs.getInt("id"));
                a.setUsuarioId(rs.getInt("usuario_id"));
                a.setVehiculoId(rs.getInt("vehiculo_id"));
                Timestamp fi = rs.getTimestamp("fecha_inicio");
                if (fi != null) a.setFechaInicio(fi.toLocalDateTime());
                Timestamp ff = rs.getTimestamp("fecha_fin");
                if (ff != null) a.setFechaFin(ff.toLocalDateTime());
                a.setKmRecorridos(rs.getDouble("km_recorridos"));
                a.setCostoTotal(rs.getDouble("costo_total"));
                list.add(a);
            }
        }
        return list;
    }
}