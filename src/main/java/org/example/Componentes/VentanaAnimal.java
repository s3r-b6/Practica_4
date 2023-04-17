package org.example.Componentes;

import org.example.Aplicacion;
import org.example.Controlador;
import org.example.Vista;

import javax.swing.*;
import java.awt.*;

import static org.example.Aplicacion.cargarVentanaTratamiento;

/**
 * Esta clase representa el JFrame de la "ventana de vista de detalle" de la aplicación
 */
public class VentanaAnimal extends JFrame {

    /**
     * El constructor de vista en detalle de cada animal
     *
     * @param c El controlador del animal que está representado en el JPanel de la ventana
     */
    public VentanaAnimal(Controlador c) {
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setPreferredSize(new Dimension(720, 600));
        this.setTitle("Vista de animal");
        this.setIconImage(new ImageIcon((System.getProperty("user.dir") + "/src/main/java/org/example/IMG/icono.png")).getImage());

        JPanel contenedorBotones = new JPanel(new GridLayout(1, 3));
        JButton botonBaja = new JButton("Baja");
        JButton botonLiberar = new JButton("Liberar");
        JButton botonTratamiento = new JButton("Tratamiento");
        botonBaja.addActionListener(e -> accionBaja(c));
        botonLiberar.addActionListener(e -> accionLiberar(c));
        botonTratamiento.addActionListener(e -> cargarVentanaTratamiento(new VentanaTratamiento(c)));

        contenedorBotones.add(botonBaja);
        contenedorBotones.add(botonLiberar);
        contenedorBotones.add(botonTratamiento);

        Vista v = c.getVista();
        this.add(v.getVistaDetalle(), BorderLayout.CENTER);
        String estado = c.getEstado();
        if (estado.equals("Fallecido") || estado.equals("Liberado")) this.add(contenedorBotones, BorderLayout.SOUTH);
        //centrar la ventana
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    /**
     * Envía la señal de baja al controlador del animal, y, envía a la aplicación la señal de recargar las
     * vistas
     *
     * @param c El controlador de la ventana
     */
    private static void accionBaja(Controlador c) {
        c.bajaControlador();
        Aplicacion.recrearVentanas(c);
    }

    /**
     * Envía la señal de liberacion al controlador del animal, y, envía a la aplicación la señal de recargar las
     * vistas
     *
     * @param c El controlador de la ventana
     */
    private static void accionLiberar(Controlador c) {
        c.liberacionControlador();
        Aplicacion.recrearVentanas(c);
    }


}
