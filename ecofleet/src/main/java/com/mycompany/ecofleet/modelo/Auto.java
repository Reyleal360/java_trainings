/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet.modelo;

/**
 *
 * @author Coder
 */
public class Auto implements Vehiculo {
    private Integer id;
    private String marca;
    private String modelo;
    private String placa;
    private boolean disponible;

    public Auto() {}
    public Auto(Integer id, String marca, String modelo, String placa, boolean disponible) {
        this.id = id; this.marca = marca; this.modelo = modelo; this.placa = placa; this.disponible = disponible;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override public String getTipo() { return "AUTO"; }
    @Override public void acelerar(int kmh) { System.out.println("Auto acelerando a " + kmh); }
    @Override public void frenar() { System.out.println("Auto frenando"); }
    @Override public double calcularCostoUso(double km) { return 5.0 + 0.8 * km; }
}
