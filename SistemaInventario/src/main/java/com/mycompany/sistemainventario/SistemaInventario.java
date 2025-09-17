/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemainventario;

/**
 *
 * @author Coder
 */
import javax.swing.JOptionPane;
import java.util.*;

// Clase principal del sistema
public class SistemaInventario {
    // TASK 2: Estructuras de datos
    private static ArrayList<Producto> productos = new ArrayList<>();
    private static HashMap<String, Integer> stock = new HashMap<>();
    private static ArrayList<TicketItem> ticket = new ArrayList<>();
    private static double totalCompras = 0;
    
    public static void main(String[] args) {
        // TASK 3: Menú principal con JOptionPane
        mostrarMenuPrincipal();
    }
    
    private static void mostrarMenuPrincipal() {
        String[] opciones = {
            "Agregar producto",
            "Listar inventario", 
            "Comprar producto",
            "Estadísticas",
            "Buscar producto",
            "Salir"
        };
        
        int opcion;
        do {
            String seleccion = (String) JOptionPane.showInputDialog(
                null,
                "=== SISTEMA DE INVENTARIO ===\nSeleccione una opción:",
                "Menú Principal",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            
            if (seleccion == null) {
                opcion = 5; // Salir si cancela
            } else {
                opcion = Arrays.asList(opciones).indexOf(seleccion);
            }
            
            switch (opcion) {
                case 0:
                    agregarProducto();
                    break;
                case 1:
                    listarInventario();
                    break;
                case 2:
                    comprarProducto();
                    break;
                case 3:
                    mostrarEstadisticas();
                    break;
                case 4:
                    buscarProducto();
                    break;
                case 5:
                    mostrarTicketFinal();
                    break;
            }
        } while (opcion != 5);
    }
    
    // TASK 1: Opción 1 - Agregar producto
    private static void agregarProducto() {
        try {
            // Seleccionar tipo de producto
            String[] tipos = {"Alimento", "Electrodoméstico"};
            String tipoSeleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el tipo de producto:",
                "Tipo de Producto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos,
                tipos[0]
            );
            
            if (tipoSeleccionado == null) return;
            
            // Solicitar nombre
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar que no exista duplicado
            if (buscarProductoPorNombre(nombre) != null) {
                JOptionPane.showMessageDialog(null, "Ya existe un producto con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Solicitar precio
            String precioStr = JOptionPane.showInputDialog("Ingrese el precio del producto:");
            if (precioStr == null || precioStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El precio no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                JOptionPane.showMessageDialog(null, "El precio no puede ser negativo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Solicitar stock inicial
            String stockStr = JOptionPane.showInputDialog("Ingrese la cantidad en stock:");
            if (stockStr == null || stockStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El stock no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int cantidadStock = Integer.parseInt(stockStr);
            if (cantidadStock < 0) {
                JOptionPane.showMessageDialog(null, "El stock no puede ser negativo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Crear producto según el tipo
            Producto nuevoProducto;
            if (tipoSeleccionado.equals("Alimento")) {
                nuevoProducto = new Alimento(nombre, precio);
            } else {
                nuevoProducto = new Electrodomestico(nombre, precio);
            }
            
            // Agregar a las estructuras de datos
            productos.add(nuevoProducto);
            stock.put(nombre, cantidadStock);
            
            JOptionPane.showMessageDialog(null, 
                String.format("Producto agregado exitosamente:\n%s\nPrecio: $%.2f\nStock: %d", 
                             nombre, precio, cantidadStock), 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Opción 2 - Listar inventario
    private static void listarInventario() {
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos en el inventario", "Inventario Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder inventario = new StringBuilder("=== INVENTARIO ===\n\n");
        for (Producto producto : productos) {
            String nombre = producto.getNombre();
            int cantidadStock = stock.get(nombre);
            inventario.append(String.format("Producto: %s\nPrecio: $%.2f\nStock: %d\nDescripción: %s\n\n", 
                                           nombre, producto.getPrecio(), cantidadStock, producto.getDescripcion()));
        }
        
        JOptionPane.showMessageDialog(null, inventario.toString(), "Inventario", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Opción 3 - Comprar producto
    private static void comprarProducto() {
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos disponibles para comprar", "Inventario Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            // Solicitar nombre del producto
            String nombreProducto = JOptionPane.showInputDialog("Ingrese el nombre del producto a comprar:");
            if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre de producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buscar producto
            Producto producto = buscarProductoPorNombre(nombreProducto);
            if (producto == null) {
                JOptionPane.showMessageDialog(null, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar stock
            int stockDisponible = stock.get(producto.getNombre());
            if (stockDisponible <= 0) {
                JOptionPane.showMessageDialog(null, "Producto sin stock disponible", "Sin Stock", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Solicitar cantidad
            String cantidadStr = JOptionPane.showInputDialog(
                String.format("Producto: %s\nPrecio: $%.2f\nStock disponible: %d\n\nIngrese la cantidad a comprar:", 
                             producto.getNombre(), producto.getPrecio(), stockDisponible));
            
            if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cantidad > stockDisponible) {
                JOptionPane.showMessageDialog(null, "Cantidad solicitada mayor al stock disponible", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Actualizar stock
            stock.put(producto.getNombre(), stockDisponible - cantidad);
            
            // Agregar al ticket
            TicketItem item = new TicketItem(producto.getNombre(), producto.getPrecio(), cantidad);
            ticket.add(item);
            totalCompras += item.getSubtotal();
            
            // Mostrar ticket parcial
            JOptionPane.showMessageDialog(null, 
                String.format("=== TICKET PARCIAL ===\n%s\n\nTotal acumulado: $%.2f", 
                             item.toString(), totalCompras), 
                "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad numérica válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Opción 4 - Estadísticas
    private static void mostrarEstadisticas() {
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos para mostrar estadísticas", "Sin Datos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Producto masCaro = productos.get(0);
        Producto masBarato = productos.get(0);
        
        for (Producto producto : productos) {
            if (producto.getPrecio() > masCaro.getPrecio()) {
                masCaro = producto;
            }
            if (producto.getPrecio() < masBarato.getPrecio()) {
                masBarato = producto;
            }
        }
        
        StringBuilder estadisticas = new StringBuilder("=== ESTADÍSTICAS ===\n\n");
        estadisticas.append(String.format("Producto más caro:\n%s - $%.2f\n\n", masCaro.getNombre(), masCaro.getPrecio()));
        estadisticas.append(String.format("Producto más barato:\n%s - $%.2f\n\n", masBarato.getNombre(), masBarato.getPrecio()));
        estadisticas.append(String.format("Total de productos en inventario: %d\n", productos.size()));
        estadisticas.append(String.format("Total de compras realizadas: $%.2f", totalCompras));
        
        JOptionPane.showMessageDialog(null, estadisticas.toString(), "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Opción 5 - Buscar producto
    private static void buscarProducto() {
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos en el inventario", "Inventario Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String termino = JOptionPane.showInputDialog("Ingrese el nombre o parte del nombre del producto:");
        if (termino == null || termino.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un término de búsqueda", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder resultados = new StringBuilder("=== RESULTADOS DE BÚSQUEDA ===\n\n");
        boolean encontrado = false;
        
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                String nombre = producto.getNombre();
                int cantidadStock = stock.get(nombre);
                resultados.append(String.format("Producto: %s\nPrecio: $%.2f\nStock: %d\nDescripción: %s\n\n", 
                                               nombre, producto.getPrecio(), cantidadStock, producto.getDescripcion()));
                encontrado = true;
            }
        }
        
        if (!encontrado) {
            resultados.append("No se encontraron productos que coincidan con la búsqueda");
        }
        
        JOptionPane.showMessageDialog(null, resultados.toString(), "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Opción 6 - Salir y mostrar ticket final
    private static void mostrarTicketFinal() {
        StringBuilder ticketFinal = new StringBuilder("=== TICKET FINAL ===\n\n");
        
        if (ticket.isEmpty()) {
            ticketFinal.append("No se realizaron compras");
        } else {
            ticketFinal.append("Productos comprados:\n\n");
            for (TicketItem item : ticket) {
                ticketFinal.append(item.toString()).append("\n");
            }
            ticketFinal.append(String.format("\n=== TOTAL GENERAL: $%.2f ===", totalCompras));
        }
        
        ticketFinal.append("\n\n¡Gracias por usar nuestro sistema!");
        
        JOptionPane.showMessageDialog(null, ticketFinal.toString(), "Ticket Final", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
    
    // TASK 4: Métodos auxiliares
    private static Producto buscarProductoPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }
}