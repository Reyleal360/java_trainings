package com.mycompany.domain.repository.JDBC;

import com.mycompany.domain.config.DBconexion;
import com.mycompany.domain.exceptions.PersistenciaException;
import com.mycompany.domain.modelo.Producto;
import com.mycompany.domain.repository.IDBC.ProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la capa DAO usando JDBC con PreparedStatement.
 * 
 * Esta clase maneja todas las operaciones CRUD contra la base de datos MySQL.
 * Utiliza try-with-resources para gestión automática de recursos.
 * Traduce SQLException a PersistenciaException para no exponer detalles técnicos.
 */
public class ProductoDAOImpl implements ProductoDAO {
    
    /**
     * Obtiene la conexión a la base de datos desde el Singleton
     */
    private Connection getConnection() {
        return DBconexion.getInstance().getConnection();
    }
    
    @Override
    public void agregar(Producto producto) throws PersistenciaException {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.executeUpdate();
            System.out.println("✅ Producto agregado: " + producto.getNombre());
        } catch (SQLException e) {
            throw new PersistenciaException("Error al agregar producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Producto> listar() throws PersistenciaException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                productos.add(new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                ));
            }
            System.out.println("📋 Se listaron " + productos.size() + " productos");
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar productos: " + e.getMessage(), e);
        }
        
        return productos;
    }
    
    @Override
    public void actualizarPrecio(int id, double nuevoPrecio) throws PersistenciaException {
        String sql = "UPDATE productos SET precio = ? WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, nuevoPrecio);
            stmt.setInt(2, id);
            
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se encontró el producto con ID: " + id);
            }
            System.out.println("💰 Precio actualizado para producto ID: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar precio: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void actualizarStock(int id, int nuevoStock) throws PersistenciaException {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, id);
            
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se encontró el producto con ID: " + id);
            }
            System.out.println("📊 Stock actualizado para producto ID: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar stock: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void eliminar(int id) throws PersistenciaException {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se encontró el producto con ID: " + id);
            }
            System.out.println("🗑️ Producto eliminado con ID: " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Producto buscarPorNombre(String nombre) throws PersistenciaException {
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    );
                    System.out.println("🔍 Producto encontrado: " + producto.getNombre());
                    return producto;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar producto: " + e.getMessage(), e);
        }
        
        System.out.println("❌ No se encontró producto con nombre: " + nombre);
        return null;
    }
    
    @Override
    public boolean existeNombre(String nombre) throws PersistenciaException {
        String sql = "SELECT COUNT(*) FROM productos WHERE nombre = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nombre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al verificar nombre: " + e.getMessage(), e);
        }
        
        return false;
    }
}