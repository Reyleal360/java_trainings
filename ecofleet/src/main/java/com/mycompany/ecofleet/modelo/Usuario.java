/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecofleet.modelo;

/**
 *
 * @author Coder
 */
public class Usuario {
    private Integer id;
    private String nombre;
    private String email;
    private String telefono;

    public Usuario() {}
    public Usuario(Integer id, String nombre, String email, String telefono) {
        this.id = id; this.nombre = nombre; this.email = email; this.telefono = telefono;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return nombre + " (" + email + ")";
    }
}