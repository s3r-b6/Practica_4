package org.example.Componentes;

import org.example.Controlador;
import org.example.Vista;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.Aplicacion.cargarMenuAlta;
import static org.example.Aplicacion.cargarPanelAnimal;
import static org.example.Persistencia.Ficheros.guardarEstado;

public class VentanaAnimales extends JFrame {
    public VentanaAnimales(ArrayList<Controlador> lista) {
        this.setLayout(new BorderLayout());
        this.setTitle("Ventana principal");
        this.setIconImage(new ImageIcon((System.getProperty("user.dir") + "/src/main/java/org/example/IMG/icono.png")).getImage());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int size = 350 * (lista.size() / 3);
        this.setPreferredSize(new Dimension(Math.min(size, 1500), 850));
        JButton botonAlta = new JButton("Alta");
        JButton botonGuardar = new JButton("Guardar");
        JPanel contenedorBotones = new JPanel(new FlowLayout());

        botonAlta.addActionListener(e -> cargarMenuAlta(new VentanaAlta(lista.size())));
        botonGuardar.addActionListener(e -> accionGuardar(lista));
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

        contenedorBotones.add(botonAlta);
        contenedorBotones.add(botonGuardar);

        this.add(gridPane, BorderLayout.CENTER);
        this.add(contenedorBotones, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(gridPane);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.getContentPane().add(scroll);

        //centrar la ventana
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private static void accionGuardar(ArrayList<Controlador> lista) {
        Object[] opt = {"Sí", "No"};

        if (JOptionPane.showOptionDialog(null,
                "¿Seguro que deseas guardar? Esta acción es irreversible",
                "Confirmación de guardado",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opt, opt[0]) != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            guardarEstado(lista);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
