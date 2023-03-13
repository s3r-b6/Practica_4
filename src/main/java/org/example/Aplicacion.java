package org.example;

import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Esta clase contiene el main() y también contiene el punto de entrada a la interfaz, i.e., la ventana sobre
 * la que se insertan los animales.
 */
public class Aplicacion {
    static JFrame ventanaPrincipal = new JFrame();
    static JFrame ventanaDetalle = new JFrame();
    static ArrayList<Controlador> lista = new ArrayList<>();

    /**
     * La lista de animales contiene los controladores para cada uno de los animales.
     * La interfaz gráfica se realiza creando un JFrame y añadiéndole a ese JFrame cada una de las vistas del controlador.
     * Desde la interfaz, se pueden enviar señales al controlador para que modifique los  datos del modelo y
     * justo después actualice la representación de los datos.
     */

    public static void initVentanaPrincipal() {
        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventanaPrincipal.setSize(new Dimension(900, 1020));
        ventanaPrincipal.setTitle("Aplicación del santuario");
        cargarGrid();
    }

    private static void cargarGrid() {
        ventanaPrincipal.getContentPane().removeAll();
        GridLayout gl = new GridLayout(3, lista.size() / 3);
        gl.setHgap(25);
        gl.setVgap(25);

        JPanel gridPane = new JPanel(gl);

        for (Controlador e : lista) {
            JPanel contenedorAnimal = new JPanel(new BorderLayout());
            JButton botonDetalle = new JButton("Detalle");
            botonDetalle.addActionListener(actionEvent -> cargarVistaDetalle(e));

            contenedorAnimal.add(e.vista.vistaNormal, BorderLayout.CENTER);
            contenedorAnimal.add(botonDetalle, BorderLayout.SOUTH);

            gridPane.add(contenedorAnimal);
        }
        ventanaPrincipal.add(gridPane);
        ventanaPrincipal.setVisible(true);
    }


    //TODO -> BUG. Al modificar el estado, se vuelven a cargar los tratamientos?


    private static void cargarVistaDetalle(Controlador cont) {
        ventanaDetalle.getContentPane().removeAll();
        ventanaDetalle.setSize(600, 600);
        JPanel contenedorDetalle = new JPanel(new BorderLayout());
        JPanel contenedorBotones = new JPanel(new GridLayout(1, 3));

        JButton botonBaja = new JButton("Baja");
        JButton botonLiberar = new JButton("Liberar");
        JButton botonTratamiento = new JButton("Tratamiento");
        botonBaja.addActionListener(e -> accionBaja(cont));
        botonLiberar.addActionListener(e -> accionLiberar(cont));
        botonTratamiento.addActionListener(e -> accionTratamiento(cont));

        contenedorBotones.add(botonBaja);
        contenedorBotones.add(botonLiberar);
        contenedorBotones.add(botonTratamiento);

        contenedorDetalle.add(cont.vista.vistaDetalle, BorderLayout.CENTER);
        contenedorDetalle.add(contenedorBotones, BorderLayout.SOUTH);

        ventanaDetalle.add(contenedorDetalle);
        ventanaDetalle.revalidate();
        ventanaDetalle.repaint();
        ventanaDetalle.setVisible(true);
    }

    private static void accionTratamiento(Controlador cont) {
        JFrame ventanaTratamiento = new JFrame();
        JPanel contenedorTratamiento = new JPanel(new FlowLayout());

        contenedorTratamiento.add(new JTextField());
        contenedorTratamiento.add(new JTextField());
        contenedorTratamiento.add(new JTextField());
        ventanaTratamiento.add(contenedorTratamiento);
        ventanaTratamiento.setSize(200, 200);
        ventanaTratamiento.setVisible(true);

        cargarVistaDetalle(cont); //sólo debería ocurrir si hay una update en los tratamientos <- temp
    }

    private static void accionBaja(Controlador cont) {
        cont.bajaControlador();
        cargarGrid();
        cargarVistaDetalle(cont);
    }

    private static void accionLiberar(Controlador cont) {
        cont.liberacionControlador();
        cargarGrid();
        cargarVistaDetalle(cont);
    }

    private static void readToList() {
        //TODO: añadir una pequeña función para la serialización / deserialización de animales, simular W/R de una DB
        lista.add(new Controlador(new Reptil("Lagarto", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Mamifero("Perro", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Ave("Jilguero", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Reptil("Lagarto", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Mamifero("Perro", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Ave("Jilguero", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Reptil("Lagarto", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Mamifero("Perro", lista.size() + 1, 3, true)));
        lista.add(new Controlador(new Ave("Jilguero", lista.size() + 1, 3, true)));

        // DEBUG
        lista.forEach(e -> {
            e.tratamientoControlador("Pastillas", LocalDate.now());
            e.tratamientoControlador("Pastillas 2", LocalDate.now());
            e.tratamientoControlador("Pastillas 3", LocalDate.now());
        });
        // DEBUG

    }

    public static void main(String[] args) {
        readToList();
        initVentanaPrincipal();
    }
}
