package com.mycompany.domain.repository.IDBC;

import com.mycompany.domain.modelo.Producto;
import com.mycompany.domain.exceptions.PersistenciaException;
import java.util.List;

/**
 * Interface DAO que define las operaciones CRUD para la entidad Producto.
 * 
 * Esta interface pertenece a la capa de Datos (DAO) y establece el contrato
 * para todas las operaciones de persistencia relacionadas con productos.
 * 
 * Todas las operaciones pueden lanzar PersistenciaException en caso de
 * errores en la base de datos.
 */
public interface ProductoDAO {
    
    /**
     * Agrega un nuevo producto a la base de datos
     * @param producto Objeto Producto con los datos a insertar
     * @throws PersistenciaException Si ocurre un error en la base de datos
     */
    void agregar(Producto producto) throws PersistenciaException;
    
    /**
     * Obtiene todos los productos del inventario
     * @return Lista de productos ordenados por ID
     * @throws PersistenciaException Si ocurre un error en la base de datos
     */
    List<Producto> listar() throws PersistenciaException;
    
    /**
     * Actualiza el precio de un producto existente
     * @param id Identificador del producto
     * @param nuevoPrecio Nuevo precio a asignar
     * @throws PersistenciaException Si ocurre un error o el producto no existe
     */
    void actualizarPrecio(int id, double nuevoPrecio) throws PersistenciaException;
    
    /**
     * Actualiza el stock de un producto existente
     * @param id Identificador del producto
     * @param nuevoStock Nueva cantidad en stock
     * @throws PersistenciaException Si ocurre un error o el producto no existe
     */
    void actualizarStock(int id, int nuevoStock) throws PersistenciaException;
    
    /**
     * Elimina un producto de la base de datos
     * @param id Identificador del producto a eliminar
     * @throws PersistenciaException Si ocurre un error o el producto no existe
     */
    void eliminar(int id) throws PersistenciaException;
    
    /**
     * Busca un producto por su nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Producto encontrado o null si no existe
     * @throws PersistenciaException Si ocurre un error en la base de datos
     */
    Producto buscarPorNombre(String nombre) throws PersistenciaException;
    
    /**
     * Verifica si existe un producto con el nombre exacto especificado
     * @param nombre Nombre exacto a verificar
     * @return true si existe, false en caso contrario
     * @throws PersistenciaException Si ocurre un error en la base de datos
     */
    boolean existeNombre(String nombre) throws PersistenciaException;
}