/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet.modelo;

/**
 *
 * @author Coder
 */
public class Bicicleta implements Vehiculo {
    private Integer id;
    private String marca;
    private String modelo;
    private boolean disponible;

    public Bicicleta() {}
    public Bicicleta(Integer id, String marca, String modelo, boolean disponible) {
        this.id = id; this.marca = marca; this.modelo = modelo; this.disponible = disponible;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override public String getTipo() { return "BICICLETA"; }
    @Override public void acelerar(int kmh) { System.out.println("Bici acelerando a " + kmh); }
    @Override public void frenar() { System.out.println("Bici frenando"); }
    @Override public double calcularCostoUso(double km) { return 1.0 + 0.2 * km; }
}