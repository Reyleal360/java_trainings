/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registroacademico.domain;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.TableModel;
/**
 *
 * @author Coder
 */
public class EstudianteManager {
private final List<Estudiante> estudiantes = new ArrayList<>();
    private int contadorId = 1;

    // Agregar un estudiante a la lista
    public void agregarEstudiante(String nombre, int edad, double nota1, double nota2, double nota3) {
        Estudiante nuevo = new Estudiante(contadorId,nombre, edad, nota1, nota2, nota3);
        estudiantes.add(nuevo);
        contadorId++;
    }

    // Retorna la lista de estudiantes
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
}
      public Estudiante getMejorPromedio() {
        Estudiante mejor = null;
        double maxProm = 0;
        for (Estudiante e : estudiantes) {
            double prom = e.calcularPromedio();
            if (prom > maxProm) {
                maxProm = prom;
                mejor = e;
            }
        }
        return mejor;
    }
       public Estudiante getPeorPromedio() {
        Estudiante peor = null;
        double minProm = 5; // asumimos notas sobre 0–5
        for (Estudiante e : estudiantes) {
            double prom = e.calcularPromedio();
            if (prom < minProm) {
                minProm = prom;
                peor = e;
            }
        }
        return peor;
    }
 public Estudiante buscarPorNombre(String nombre) {
        for (Estudiante e : estudiantes) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                return e;
            }
        }
        return null;
    }
 
   public static void guardarTablaEnCSV(JTable tabla, String nombreArchivo) {
        try (FileWriter fw = new FileWriter(nombreArchivo)) {
            TableModel modelo = tabla.getModel();

            // Escribir encabezados
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                fw.write(modelo.getColumnName(i) + (i < modelo.getColumnCount() - 1 ? "," : ""));
            }
            fw.write("\n");

            // Escribir filas
            for (int i = 0; i < modelo.getRowCount(); i++) {
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    fw.write(modelo.getValueAt(i, j).toString() + (j < modelo.getColumnCount() - 1 ? "," : ""));
                }
                fw.write("\n");
            }

            fw.flush();
            System.out.println("✅ Datos guardados en " + nombreArchivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
