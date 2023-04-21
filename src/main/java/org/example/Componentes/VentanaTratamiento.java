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

import static org.example.Aplicacion.recrearVentanas;

/**
 * Esta clase representa el JFrame de la "ventana de tratamientos" de la aplicación
 */
public class VentanaTratamiento extends JFrame {
    /**
     * El constructor del la ventana para añadir nuevos tratamientos a un animal.
     *
     * @param c El controlador del animal para el que se va a crear un tratamiento
     */
    public VentanaTratamiento(Controlador c) {
        this.setTitle("Panel de tratamientos");
        this.setSize(450, 325);
        this.setResizable(false);
        this.setIconImage(new ImageIcon((System.getProperty("user.dir") + "/src/main/java/org/example/IMG/icono.png")).getImage());

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

    /**
     * La acción que trata de añadir un nuevo tratamiento desde la ventana. Sólo envía la señal de guardar
     * el nuevo tratamiento si los valores son validados correctamentes
     *
     * @param ventanaTratamiento Un puntero a la ventana, para cerrarla si tiene éxito
     * @param c                  El controlador del animal
     * @param fecha              El contenedor en que se introduce la fecha de fin del tratamiento
     * @param textArea           El contenedor en que se introduce la descripción del tratamiento
     */
    public static void accionAddTratamiento(VentanaTratamiento ventanaTratamiento, Controlador c, JTextField fecha, JTextArea textArea) throws DateTimeParseException {
        String fechaTexto = fecha.getText().trim();
        String descripcionTexto = textArea.getText().trim();
        LocalDate fechaParseada;
        try {
            fechaParseada = LocalDate.parse(fechaTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (fechaParseada.getYear() < 2023) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor introduce una fecha válida, p.ej: 12/02/2023\n" +
                    "El año mínimo es 2023", "Error", JOptionPane.ERROR_MESSAGE);
            fecha.setText("");
            return;
        }
        if (!descripcionTexto.matches(".{5,144}")) {
            JOptionPane.showMessageDialog(null, "Mal input de la descripción", "Error", JOptionPane.ERROR_MESSAGE);
            textArea.setText("");
            return;
        }
        c.nuevoTratamientoControlador(descripcionTexto, fechaParseada);
        ventanaTratamiento.dispose();
        recrearVentanas(c);
    }
}
