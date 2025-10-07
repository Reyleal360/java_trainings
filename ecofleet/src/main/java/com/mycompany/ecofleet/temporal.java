/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet;

import com.mycompany.ecofleet.config.ConexionDB;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author Coder
 */
public class temporal {
    public static void main(String[] args) {
        try (Connection conn = ConexionDB.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a PostgreSQL");
            } else {
                System.out.println("❌ No se pudo conectar");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}
