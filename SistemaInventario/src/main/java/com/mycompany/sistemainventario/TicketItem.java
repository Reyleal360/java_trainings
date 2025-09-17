/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemainventario;

/**
 *
 * @author Coder
 */
// Clase para manejar tickets de compra
public class TicketItem {
    private String nombre;
    private double precio;
    private int cantidad;
    
    public TicketItem(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    
    public double getSubtotal() {
        return precio * cantidad;
    }
    
    @Override
    public String toString() {
        return String.format("%s - Cantidad: %d - Precio unitario: $%.2f - Subtotal: $%.2f", 
                           nombre, cantidad, precio, getSubtotal());
    }
}