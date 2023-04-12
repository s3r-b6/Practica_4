package org.example.Componentes;

import org.example.Controlador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.example.Aplicacion.recargarVistas;
import static org.example.Componentes.Adaptadores.adaptadorInputFecha;

public class VentanaTratamiento extends JFrame {
    public VentanaTratamiento(Controlador c) {
        this.setTitle("Panel de tratamientos");
        this.setSize(450, 325);
        this.setResizable(false);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        JPanel contenedorTratamiento = new JPanel(new BorderLayout());
        contenedorTratamiento.setBorder(padding);
        JLabel errorFecha = new JLabel("");
        errorFecha.setForeground(Color.RED);
        JPanel contenedorFecha = new JPanel(new GridLayout(2, 1));
        JTextArea textArea = new JTextArea(5, 10);
        textArea.setLineWrap(true);
        contenedorFecha.add(new JLabel("Fecha de fin: (p.ej., 12/03/2023)"));
        JTextField fecha = new JTextField(7);
        fecha.addKeyListener(adaptadorInputFecha(fecha, errorFecha));
        contenedorFecha.add(fecha);
        contenedorFecha.add(errorFecha);
        contenedorTratamiento.add(contenedorFecha, BorderLayout.NORTH);
        contenedorTratamiento.add(textArea, BorderLayout.CENTER);

        JButton botonAdd = new JButton("Añadir");
        botonAdd.addActionListener(e -> accionAddTratamiento(c, fecha, textArea));

        contenedorTratamiento.add(botonAdd, BorderLayout.SOUTH);
        this.add(contenedorTratamiento);
        this.setVisible(true);
    }

    public static void accionAddTratamiento(Controlador c, JTextField fecha, JTextArea textArea) {
        String fechaTexto = fecha.getText().trim();
        String descripcionTexto = textArea.getText().trim();
        LocalDate fechaParseada;
        try {
            fechaParseada = LocalDate.parse(fechaTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Por favor introduce una fecha válida, p.ej: 12/02/2023", "Error", JOptionPane.ERROR_MESSAGE);
            fecha.setText("");
            return;
        }
        if (!descripcionTexto.matches(".{12,144}")) {
            JOptionPane.showMessageDialog(null, "Mal input de la descripción", "Error", JOptionPane.ERROR_MESSAGE);
            textArea.setText("");
            return;
        }
        c.nuevoTratamientoControlador(descripcionTexto, fechaParseada);
        recargarVistas(c);
    }
}
