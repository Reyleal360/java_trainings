package com.mycompany.domain.service.impl;
import com.mycompany.domain.exceptions.DatoInvalidoException;
import com.mycompany.domain.exceptions.PersistenciaException;
import com.mycompany.domain.exceptions.DuplicadoException;
import com.mycompany.domain.modelo.Producto;
import com.mycompany.domain.repository.IDBC.ProductoDAO;
import com.mycompany.domain.repository.JDBC.ProductoDAOImpl;
import com.mycompany.domain.service.InventarioServiceLocal;

import java.util.List;

/**
 * Implementación de la capa de Servicio (lógica de negocio).
 * 
 * Responsabilidades:
 * - Validar todos los datos antes de procesarlos
 * - Aplicar reglas de negocio (no duplicados, precios positivos, etc.)
 * - Lanzar excepciones personalizadas según el tipo de error
 * - Delegar las operaciones CRUD al DAO
 * - No exponer detalles de implementación del DAO a la capa UI
 */
public class InventarioServiceImpl implements InventarioServiceLocal {
    
    private final ProductoDAO productoDAO;
    
    /**
     * Constructor que inicializa la dependencia del DAO
     */
    public InventarioServiceImpl() {
        this.productoDAO = new ProductoDAOImpl();
    }
    
    @Override
    public void agregarProducto(String nombre, double precio, int stock) 
            throws DatoInvalidoException, DuplicadoException, PersistenciaException {
        
        // VALIDACIÓN 1: Nombre no puede estar vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("❌ El nombre del producto no puede estar vacío");
        }
        
        // VALIDACIÓN 2: Precio debe ser mayor a cero
        if (precio <= 0) {
            throw new DatoInvalidoException("❌ El precio debe ser mayor a cero. Valor ingresado: " + precio);
        }
        
        // VALIDACIÓN 3: Stock no puede ser negativo
        if (stock < 0) {
            throw new DatoInvalidoException("❌ El stock no puede ser negativo. Valor ingresado: " + stock);
        }
        
        // VALIDACIÓN 4: Verificar que no exista un producto con el mismo nombre
        if (productoDAO.existeNombre(nombre.trim())) {
            throw new DuplicadoException("❌ Ya existe un producto con el nombre: '" + nombre + "'");
        }
        
        // Si todas las validaciones pasan, delegar al DAO
        Producto producto = new Producto(nombre.trim(), precio, stock);
        productoDAO.agregar(producto);
        
        System.out.println("✅ [SERVICE] Producto validado y agregado correctamente");
    }
    
    @Override
    public List<Producto> listarInventario() throws PersistenciaException {
        System.out.println("📋 [SERVICE] Solicitando listado de productos al DAO");
        return productoDAO.listar();
    }
    
    @Override
    public void actualizarPrecio(int id, double nuevoPrecio) 
            throws DatoInvalidoException, PersistenciaException {
        
        // VALIDACIÓN: Precio debe ser mayor a cero
        if (nuevoPrecio <= 0) {
            throw new DatoInvalidoException("❌ El precio debe ser mayor a cero. Valor ingresado: " + nuevoPrecio);
        }
        
        // Delegar al DAO
        productoDAO.actualizarPrecio(id, nuevoPrecio);
        System.out.println("✅ [SERVICE] Precio actualizado correctamente");
    }
    
    @Override
    public void actualizarStock(int id, int nuevoStock) 
            throws DatoInvalidoException, PersistenciaException {
        
        // VALIDACIÓN: Stock no puede ser negativo
        if (nuevoStock < 0) {
            throw new DatoInvalidoException("❌ El stock no puede ser negativo. Valor ingresado: " + nuevoStock);
        }
        
        // Delegar al DAO
        productoDAO.actualizarStock(id, nuevoStock);
        System.out.println("✅ [SERVICE] Stock actualizado correctamente");
    }
    
    @Override
    public void eliminarProducto(int id) throws PersistenciaException {
        System.out.println("🗑️ [SERVICE] Solicitando eliminación del producto ID: " + id);
        productoDAO.eliminar(id);
    }
    
    @Override
    public Producto buscarProducto(String nombre) throws PersistenciaException {
        // Validación básica
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("⚠️ [SERVICE] Nombre de búsqueda vacío");
            return null;
        }
        
        System.out.println("🔍 [SERVICE] Buscando producto: " + nombre);
        return productoDAO.buscarPorNombre(nombre.trim());
    }
}