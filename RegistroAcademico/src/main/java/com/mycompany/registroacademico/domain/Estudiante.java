/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registroacademico.domain;

/**
 * @author Coder
 */

public class Estudiante {
    private int id;          // ID autoincremental
    private String nombre;
    private int edad;
    private double nota1;
    private double nota2;
    private double nota3;

    public Estudiante(int id, String nombre, int edad, double nota1, double nota2, double nota3) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
    }

    // Getters básicos
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public double getNota1() { return nota1; }
    public double getNota2() { return nota2; }
    public double getNota3() { return nota3; }

    // 🔹 Método para calcular promedio
    public double calcularPromedio() {
        return (nota1 + nota2 + nota3) / 3.0;
    }

    // 🔹 Método para obtener la nota mayor
    public double getNotaMayor() {
        return Math.max(nota1, Math.max(nota2, nota3));
    }

    // 🔹 Método para obtener la nota menor
    public double getNotaMenor() {
        return Math.min(nota1, Math.min(nota2, nota3));
    }

    // 🔹 Método para obtener el estado
    public String getEstado() {
        return calcularPromedio() >= 3.0 ? "Aprobado" : "Reprobado";
    }

    // 🔹 Para guardar fácil en CSV
    @Override
    public String toString() {
        return id + "," + nombre + "," + getNotaMayor() + "," + getNotaMenor() + "," +
               calcularPromedio() + "," + getEstado();
    }
}