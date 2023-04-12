package org.example.Componentes;

import org.example.Controlador;
import org.example.Vista;

import javax.swing.*;
import java.awt.*;

import static org.example.Aplicacion.cargarPanelTratamiento;
import static org.example.Aplicacion.recargarVistas;

public class VentanaAnimal extends JFrame {

    public VentanaAnimal(Controlador c) {
        this.setLayout(new BorderLayout());
        this.setSize(600, 600);
        this.setTitle("Vista de animal");

        JPanel contenedorBotones = new JPanel(new GridLayout(1, 3));
        JButton botonBaja = new JButton("Baja");
        JButton botonLiberar = new JButton("Liberar");
        JButton botonTratamiento = new JButton("Tratamiento");
        botonBaja.addActionListener(e -> accionBaja(c));
        botonLiberar.addActionListener(e -> accionLiberar(c));
        botonTratamiento.addActionListener(e -> cargarPanelTratamiento(new VentanaTratamiento(c)));

        contenedorBotones.add(botonBaja);
        contenedorBotones.add(botonLiberar);
        contenedorBotones.add(botonTratamiento);

        Vista v = c.getVista();
        this.add(v.getVistaDetalle(), BorderLayout.CENTER);
        if (!v.isFueraDelSantuario()) this.add(contenedorBotones, BorderLayout.SOUTH);
        this.setVisible(true);
    }


    private static void accionBaja(Controlador c) {
        c.bajaControlador();
        recargarVistas(c);
    }

    private static void accionLiberar(Controlador c) {
        c.liberacionControlador();
        recargarVistas(c);
    }


}
