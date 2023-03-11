package org.example;

import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import java.awt.*;
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
        JPanel gridPane = new JPanel();

        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventanaPrincipal.setSize(new Dimension(900, 1020));
        ventanaPrincipal.setTitle("Aplicación del santuario");

        GridLayout gl = new GridLayout(3, lista.size() / 3);
        gl.setHgap(25);
        gl.setVgap(25);
        gridPane.setLayout(gl);

        for (Controlador e : lista) {
            JPanel contenedorAnimal = new JPanel(new BorderLayout());
            JButton botonDetalle = new JButton("Detalle");
            botonDetalle.addActionListener(actionEvent -> spawnVistaDetalle(e));

            contenedorAnimal.add(e.vista.vistaNormal, BorderLayout.NORTH);
            contenedorAnimal.add(botonDetalle, BorderLayout.SOUTH);

            gridPane.add(contenedorAnimal);
        }

        ventanaPrincipal.add(gridPane);
        ventanaPrincipal.setVisible(true);
    }

    private static void spawnVistaDetalle(Controlador e) {
        //TODO: añadir botones para modificar el estado del animal.
        // IDEA: click -> ventanaDetalle(de ese animal) -> Botones para realizar las acciones posibles
        ventanaDetalle.getContentPane().removeAll();
        ventanaDetalle.setSize(600, 600);
        ventanaDetalle.add(e.vista.vistaDetalle);
        ventanaDetalle.revalidate();
        ventanaDetalle.repaint();
        ventanaDetalle.setVisible(true);
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
    }

    public static void main(String[] args) {
        readToList();
        initVentanaPrincipal();
    }
}
