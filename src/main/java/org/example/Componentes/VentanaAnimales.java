package org.example.Componentes;

import org.example.Controlador;
import org.example.Vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.example.Aplicacion.cargarMenuAlta;
import static org.example.Aplicacion.cargarPanelAnimal;

public class VentanaAnimales extends JFrame {
    public VentanaAnimales(ArrayList<Controlador> lista) {
        this.setLayout(new BorderLayout());
        this.setTitle("Ventana principal");
        int size = 350 * (lista.size() / 3);
        this.setPreferredSize(new Dimension(Math.min(size, 1500), 850));
        JButton botonAlta = new JButton("Alta");
        botonAlta.addActionListener(e -> cargarMenuAlta(new VentanaAlta(lista.size())));

        GridLayout gl = new GridLayout(3, lista.size() / 3);
        JPanel gridPane = new JPanel(gl);
        gridPane.setPreferredSize(new Dimension(245 * (lista.size() / 2), 735));
        gl.setHgap(25);
        gl.setVgap(25);


        for (Controlador c : lista) {
            JPanel contenedorAnimal = new JPanel(new BorderLayout());
            JButton botonDetalle = new JButton("Detalles");
            botonDetalle.addActionListener(actionEvent -> cargarPanelAnimal(new VentanaAnimal(c)));

            Vista vistas = c.getVista();

            contenedorAnimal.add(vistas.getVistaNormal(), BorderLayout.CENTER);
            contenedorAnimal.add(botonDetalle, BorderLayout.SOUTH);

            gridPane.add(contenedorAnimal);
        }
        
        this.add(gridPane, BorderLayout.CENTER);
        this.add(botonAlta, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(gridPane);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.getContentPane().add(scroll);

        //centrar la ventana
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
