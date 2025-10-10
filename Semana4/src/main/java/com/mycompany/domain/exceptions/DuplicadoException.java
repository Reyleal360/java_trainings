/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.domain.exceptions;

/**
 * Excepción personalizada lanzada cuando se intenta agregar un producto
 * con un nombre que ya existe en la base de datos.
 * 
 * Representa una violación de la restricción UNIQUE en el campo 'nombre'.
 * 
 * Esta excepción se lanza desde la capa de Servicio después de verificar
 * la existencia del nombre en la base de datos.
 */
public class DuplicadoException extends Exception {
    
    /**
     * Constructor con mensaje personalizado
     * @param mensaje Descripción del error de duplicado
     */
    public DuplicadoException(String mensaje) {
        super(mensaje);
    }
}