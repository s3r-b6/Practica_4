package org.example;

import org.example.Componentes.VentanaAlta;
import org.example.Componentes.VentanaAnimal;
import org.example.Componentes.VentanaAnimales;
import org.example.Componentes.VentanaTratamiento;
import org.example.Modelo.Animal;
import org.example.Persistencia.Ficheros;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author Sergio Bermejo de las Heras
 * Esta clase es el punto de entrada a la aplicación. Tiene como atributos las diferentes ventanas de la aplicación.
 */
public class Aplicacion {

    //TODO: comentar y limpiar código

    static JFrame ventanaPrincipal;
    static JFrame ventanaDetalle;
    static JFrame ventanaAlta;
    static JFrame ventanaTratamiento;
    /**
     * La lista de animales contiene los controladores para cada uno de los animales.
     * La interfaz gráfica se realiza creando un JFrame y añadiéndole a ese JFrame cada
     * una de las vistas del controlador. Desde la interfaz, se pueden enviar señales al
     * controlador para que modifique los  datos del modelo y justo después actualice la
     * representación de los datos.
     */
    static ArrayList<Controlador> lista = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        rebuildLista();
        cargarVentanaPrincipal(new VentanaAnimales(lista));
    }

    /**
     * Llama al método reconstruirLista del paquete de Persistencia para crear una lista de animales
     * a partir del archivo .json, y, a partir de él reconstruye una lista de contrladores.
     *
     * @throws IOException
     */
    public static void rebuildLista() throws IOException {
        ArrayList<Animal> listaAnimales = Ficheros.reconstruirLista();
        ArrayList<Controlador> listaControladores = new ArrayList<>();
        listaAnimales.forEach(animal -> listaControladores.add(new Controlador(animal)));
        lista = listaControladores;
    }

    /**
     * El método cargarPanelAnimales intenta cargar el componente VentanaAnimales como ventana principal,
     * si el atributo ya tiene asignada una ventana, se destruye con el método dispose
     *
     * @param v Un objeto del tipo VentanaAnimales
     */
    public static void cargarVentanaPrincipal(VentanaAnimales v) {
        if (ventanaPrincipal != null) ventanaPrincipal.dispose();
        ventanaPrincipal = v;
        JButton botonAlta = new JButton("Alta");
        JButton botonGuardar = new JButton("Guardar");
        JPanel contenedorBotones = new JPanel(new FlowLayout());

        botonAlta.addActionListener(e -> cargarVentanaAlta(new VentanaAlta(lista.size())));
        botonGuardar.addActionListener(e -> accionGuardar(lista));
        contenedorBotones.add(botonAlta);
        contenedorBotones.add(botonGuardar);

        v.getContentPane().add(contenedorBotones, BorderLayout.SOUTH);
    }

    /**
     * El método cargarPanelAnimal intenta cargar el componente VentanaAnimal como ventana XXXXX,
     * si el atributo ya tiene asignada una ventana, se destruye con el método dispose
     *
     * @param v Un objeto del tipo VentanaAnimal
     */
    public static void cargarVentanaDetalle(VentanaAnimal v) {
        if (ventanaDetalle != null) ventanaDetalle.dispose();
        ventanaDetalle = v;
    }

    /**
     * El método cargarMenuAlta intenta cargar el componente VentanaAlta como ventana XXXXX,
     * si el atributo ya tiene asignada una ventana, se destruye con el método dispose
     *
     * @param v Un objeto del tipo VentanaAlta
     */
    public static void cargarVentanaAlta(VentanaAlta v) {
        if (ventanaAlta != null) ventanaAlta.dispose();
        ventanaAlta = v;
    }

    /**
     * El método cargarMenuAlta intenta cargar el componente VentanaTratamiento como ventana XXXXX,
     * si el atributo ya tiene asignada una ventana, se destruye con el método dispose
     *
     * @param v Un objeto del tipo VentanaTratamiento
     */
    public static void cargarVentanaTratamiento(VentanaTratamiento v) {
        if (ventanaTratamiento != null) ventanaTratamiento.dispose();
        ventanaTratamiento = v;
    }

    /**
     * Este método destruye las ventanas principal y de detalle (en las que los datos pueden desactualizarse
     * respesto a su representación) y carga nuevos JFrames en ellas.
     *
     * @param c
     */
    public static void recrearVentanas(Controlador c) {
        if (ventanaPrincipal != null) ventanaPrincipal.dispose();
        if (ventanaDetalle != null) ventanaDetalle.dispose();
        cargarVentanaPrincipal(new VentanaAnimales(lista));
        cargarVentanaDetalle(new VentanaAnimal(c));
    }

    /**
     * Este método añade a la lista un nuevo controlador y llama al método que carga un nuevo panel de animales
     *
     * @param a El Animal a añadir
     */
    public static void addAnimal(Animal a) {
        lista.add(new Controlador(a));
        cargarVentanaPrincipal(new VentanaAnimales(lista));
    }

    /**
     * El método desencadenado por el botón de guardar. Envía la lista de controladores al método que se encarga
     * de persistir los datos, si el usuario confirma que desea hacerlo.
     *
     * @param lista La lista de controladores
     */
    private static void accionGuardar(ArrayList<Controlador> lista) {
        Object[] opt = {"Sí", "No"};
        if (JOptionPane.showOptionDialog(null,
                "¿Seguro que deseas guardar?\n Esta acción es irreversible",
                "Confirmación de guardado",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opt, opt[0]) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            Ficheros.guardarEstado(lista);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
