/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.test_conection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UsuarioSwingApp extends JFrame {
    
    // Componentes del formulario
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtEdad;
    private JButton btnInsertar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnLimpiar;
    
    // Tabla para mostrar datos
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    
    // Variable para saber si estamos editando
    private int idUsuarioSeleccionado = -1;
    
    public UsuarioSwingApp() {
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setTitle("Gestión de Usuarios - PostgreSQL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior con formulario
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);
        
        // Panel central con tabla
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);
        
        // Configuración de la ventana
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);
        
        // Edad
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Edad:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        txtEdad = new JTextField(20);
        panel.add(txtEdad, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnInsertar = new JButton("Insertar");
        btnInsertar.setBackground(new Color(46, 125, 50));
        btnInsertar.setForeground(Color.WHITE);
        btnInsertar.addActionListener(e -> insertarUsuario());
        
        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(255, 152, 0));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setEnabled(false);
        btnActualizar.addActionListener(e -> actualizarUsuario());
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(211, 47, 47));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarUsuario());
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(96, 125, 139));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(33, 150, 243));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRefrescar);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));
        
        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "Email", "Edad", "Fecha Registro"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getTableHeader().setBackground(new Color(63, 81, 181));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        
        // Configurar anchos de columnas
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(200); // Email
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(80);  // Edad
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(150); // Fecha
        
        // Listener para selección de filas
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tablaUsuarios.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    cargarDatosEnFormulario(filaSeleccionada);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void insertarUsuario() {
        try {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            
            if (validarDatos(nombre, email, edad)) {
                boolean insertado = DatabaseManager.insertarUsuario(nombre, email, edad);
                
                if (insertado) {
                    JOptionPane.showMessageDialog(this, "Usuario insertado correctamente", 
                                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al insertar usuario", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número válido", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarUsuario() {
        try {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            
            if (validarDatos(nombre, email, edad) && idUsuarioSeleccionado > 0) {
                boolean actualizado = DatabaseManager.actualizarUsuario(idUsuarioSeleccionado, nombre, email, edad);
                
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente", 
                                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar usuario", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número válido", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarUsuario() {
        if (idUsuarioSeleccionado > 0) {
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea eliminar este usuario?", 
                    "Confirmar eliminación", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminado = DatabaseManager.eliminarUsuario(idUsuarioSeleccionado);
                
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente", 
                                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar usuario", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private boolean validarDatos(String nombre, String email, int edad) {
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (email.isEmpty() || !email.contains("@")) {
            JOptionPane.showMessageDialog(this, "El email es obligatorio y debe ser válido", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (edad <= 0 || edad > 120) {
            JOptionPane.showMessageDialog(this, "La edad debe estar entre 1 y 120 años", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void cargarDatos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Cargar datos de la base de datos
        List<Usuario> usuarios = DatabaseManager.obtenerUsuarios();
        
        for (Usuario usuario : usuarios) {
            Object[] fila = {
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getEdad(),
                usuario.getFechaFormateada()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void cargarDatosEnFormulario(int fila) {
        idUsuarioSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        txtEmail.setText((String) modeloTabla.getValueAt(fila, 2));
        txtEdad.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        
        // Habilitar botones de actualizar y eliminar
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInsertar.setEnabled(false);
    }
    
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtEdad.setText("");
        idUsuarioSeleccionado = -1;
        
        // Resetear botones
        btnInsertar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        
        // Limpiar selección de tabla
        tablaUsuarios.clearSelection();
    }
    
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            // Usar el Look and Feel del sistema de forma compatible
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si falla, usar el Look and Feel por defecto
            System.out.println("Usando Look and Feel por defecto");
        }
        new UsuarioSwingApp();
    });
}
}