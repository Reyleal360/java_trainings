package com.mycompany.domain.service;

import com.mycompany.domain.exceptions.*;
import com.mycompany.domain.modelo.Producto;
import java.util.List;

/**
 * Interface de la capa de Servicio que define la lógica de negocio.
 * 
 * Esta capa se encuentra entre la UI y el DAO, y es responsable de:
 * - Validar los datos antes de enviarlos al DAO
 * - Aplicar reglas de negocio
 * - Lanzar excepciones personalizadas
 * - Coordinar operaciones entre múltiples DAOs si fuera necesario
 */
public interface InventarioServiceLocal {
    
    /**
     * Agrega un nuevo producto después de validar los datos
     * @param nombre Nombre del producto (no puede estar vacío)
     * @param precio Precio del producto (debe ser mayor a 0)
     * @param stock Cantidad en stock (no puede ser negativo)
     * @throws DatoInvalidoException Si algún dato no cumple las validaciones
     * @throws DuplicadoException Si ya existe un producto con ese nombre
     * @throws PersistenciaException Si hay error en la base de datos
     */
    void agregarProducto(String nombre, double precio, int stock) 
        throws DatoInvalidoException, DuplicadoException, PersistenciaException;
    
    /**
     * Obtiene el listado completo del inventario
     * @return Lista de todos los productos
     * @throws PersistenciaException Si hay error en la base de datos
     */
    List<Producto> listarInventario() throws PersistenciaException;
    
    /**
     * Actualiza el precio de un producto existente
     * @param id Identificador del producto
     * @param nuevoPrecio Nuevo precio (debe ser mayor a 0)
     * @throws DatoInvalidoException Si el precio no es válido
     * @throws PersistenciaException Si hay error en la base de datos
     */
    void actualizarPrecio(int id, double nuevoPrecio) 
        throws DatoInvalidoException, PersistenciaException;
    
    /**
     * Actualiza el stock de un producto existente
     * @param id Identificador del producto
     * @param nuevoStock Nueva cantidad (no puede ser negativo)
     * @throws DatoInvalidoException Si el stock no es válido
     * @throws PersistenciaException Si hay error en la base de datos
     */
    void actualizarStock(int id, int nuevoStock) 
        throws DatoInvalidoException, PersistenciaException;
    
    /**
     * Elimina un producto del inventario
     * @param id Identificador del producto a eliminar
     * @throws PersistenciaException Si hay error en la base de datos
     */
    void eliminarProducto(int id) throws PersistenciaException;
    
    /**
     * Busca un producto por su nombre
     * @param nombre Nombre o parte del nombre a buscar
     * @return Producto encontrado o null si no existe
     * @throws PersistenciaException Si hay error en la base de datos
     */
    Producto buscarProducto(String nombre) throws PersistenciaException;
}