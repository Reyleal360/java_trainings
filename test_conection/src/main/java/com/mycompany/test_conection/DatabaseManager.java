package com.mycompany.test_conection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/mi_aplicacion";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    
    // Método para obtener conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // Método para insertar un usuario
    public static boolean insertarUsuario(String nombre, String email, int edad) {
        String sql = "INSERT INTO usuarios (nombre, email, edad) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setInt(3, edad);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // Método para obtener todos los usuarios
    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, email, edad, fecha_registro FROM usuarios ORDER BY id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setEdad(rs.getInt("edad"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuarios.add(usuario);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    // Método para eliminar un usuario
    public static boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // Método para actualizar un usuario
    public static boolean actualizarUsuario(int id, String nombre, String email, int edad) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, edad = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setInt(3, edad);
            pstmt.setInt(4, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}