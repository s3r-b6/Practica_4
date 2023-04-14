package org.example.Componentes;

import org.example.Controlador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.example.Aplicacion.recargarVistas;

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
        botonAdd.addActionListener(e -> accionAddTratamiento(this, c, fecha, textArea));

        contenedorTratamiento.add(botonAdd, BorderLayout.SOUTH);

        this.add(contenedorTratamiento);
        //centrar la ventana
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void accionAddTratamiento(VentanaTratamiento ventanaTratamiento, Controlador c, JTextField fecha, JTextArea textArea) {
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
        ventanaTratamiento.dispose();
        recargarVistas(c);
    }


    static KeyAdapter adaptadorInputFecha(JTextField especieTF, JLabel errorEspecie) {
        return new KeyAdapter() {
            public void keyPressed(KeyEvent key) {
                String value = especieTF.getText();
                if (value.length() >= 10 || (key.getKeyChar() < '0' || key.getKeyChar() > '9')) {
                    especieTF.setEditable(false);
                    especieTF.setText("");
                    errorEspecie.setText("Por favor, introduce sólo números");
                } else {
                    especieTF.setEditable(true);
                    errorEspecie.setText("");
                    if (value.length() == 2 || value.length() == 5)
                        especieTF.setText(value + '/');
                }
            }
        };
    }
}