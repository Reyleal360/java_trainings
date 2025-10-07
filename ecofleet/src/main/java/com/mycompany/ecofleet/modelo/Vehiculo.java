/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet.modelo;

/**
 *
 * @author Coder
 */
public interface Vehiculo {
    String getTipo();
    void acelerar(int kmh);
    void frenar();
    double calcularCostoUso(double kmRecorridos);
}