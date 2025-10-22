package com.mycompany.domain.exceptions;

/**
 * Excepción personalizada lanzada desde la capa DAO cuando ocurre
 * un error en la base de datos.
 * 
 * Encapsula SQLException para no exponer detalles técnicos de la base de datos
 * en las capas superiores (Servicio y Presentación).
 * 
 * Se utiliza para errores como:
 * - Problemas de conexión
 * - Errores en sentencias SQL
 * - Violaciones de integridad referencial
 * - Timeouts de base de datos
 */
public class PersistenciaException extends Exception {
    
    /**
     * Constructor con mensaje personalizado
     * @param mensaje Descripción del error de persistencia
     */
    public PersistenciaException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor con mensaje y causa original (SQLException)
     * @param mensaje Descripción del error
     * @param causa Excepción original que causó el error
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}