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
import com.mycompany.ecofleet.modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    public Usuario create(Usuario u) throws SQLException {
        if (findByEmail(u.getEmail()) != null) {
            throw new SQLException("Email ya existe: " + u.getEmail());
        }

        String sql = "INSERT INTO usuarios (nombre, email, telefono) VALUES (?, ?, ?)";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefono());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }
        return u;
    }

    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT id, nombre, email, telefono FROM usuarios WHERE email = ?";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), rs.getString("telefono"));
                }
            }
        }
        return null;
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> list = new ArrayList<>();
        String sql = "SELECT id, nombre, email, telefono FROM usuarios";
        try (Connection c = ConexionDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), rs.getString("telefono")));
            }
        }
        return list;
    }
}