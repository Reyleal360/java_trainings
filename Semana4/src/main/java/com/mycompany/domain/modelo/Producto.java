    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.domain.modelo;

/**
 * Clase de dominio que representa un producto en el inventario.
 * Esta clase pertenece a la capa de Dominio (Model).
 */
public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int stock;
    
    // Constructor vacío
    public Producto() {
    }
    
    // Constructor completo
    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    
    // Constructor sin ID (para inserciones)
    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Producto: %s | Precio: $%.2f | Stock: %d", 
                           id, nombre, precio, stock);
    }
}