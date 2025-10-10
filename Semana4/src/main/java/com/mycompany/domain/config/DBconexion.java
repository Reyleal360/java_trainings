/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.domain.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de configuración para gestionar la conexión a la base de datos MySQL.
 * Implementa el patrón Singleton para mantener una única instancia de conexión.
 */
public class DBconexion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/minitienda?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234"; // Cambia esto por tu contraseña
    
    private static DBconexion instance;
    private Connection connection;
    
    /**
     * Constructor privado para prevenir instanciación directa
     */
    private DBconexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a base de datos establecida exitosamente");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la instancia única de la conexión (Singleton)
     */
    public static DBconexion getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DBconexion();
        }
        return instance;
    }
    
    /**
     * Verifica si la conexión es válida
     */
    private static boolean isConnectionValid() {
        try {
            return instance != null && 
                   instance.connection != null && 
                   !instance.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Obtiene la conexión activa
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Cierra la conexión
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}