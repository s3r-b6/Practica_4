package org.example;

import org.example.Componentes.VentanaAlta;
import org.example.Componentes.VentanaAnimal;
import org.example.Componentes.VentanaAnimales;
import org.example.Componentes.VentanaTratamiento;
import org.example.Modelo.Animal;
import org.example.Persistencia.GestionDatos;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.example.Persistencia.Conexion.poblarHashMaps;
import static org.example.Persistencia.GestionDatos.buildListaFromResultSet;

/**
 * @author Sergio Bermejo de las Heras
 * Esta clase es el punto de entrada a la aplicación. Tiene como
 * atributos las diferentes ventanas de la aplicación.
 */
public class Aplicacion {
    static JFrame ventanaPrincipal;
    static JFrame ventanaDetalle;
    static JFrame ventanaAlta;
    static JFrame ventanaTratamiento;

    public static HashMap<String, Integer> estados = new HashMap<>();
    public static HashMap<String, Integer> familias = new HashMap<>();
    public static HashMap<String, Integer> gravedades = new HashMap<>();

    public static HashMap<Integer, String> estados_id = new HashMap<>();
    public static HashMap<Integer, String> familias_id = new HashMap<>();
    public static HashMap<Integer, String> gravedades_id = new HashMap<>();
    /**
     * La lista de animales contiene los controladores para cada uno de los
     * animales. La interfaz gráfica se realiza creando un JFrame y añadiéndole a ese JFrame
     * cada una de las vistas del controlador. Desde la interfaz, se pueden enviar
     * señales al controlador para que modifique los datos del modelo y justo después actualice
     * la representación de los datos.
     */
    static ArrayList<Controlador> lista = new ArrayList<>();

    //TODO: arreglar ventana principal con pocos elementos
    public static void main(String[] args) {
        rebuildLista();
        cargarVentanaPrincipal(new VentanaAnimales(lista));
    }

    /**
     * Llama al método reconstruirLista del paquete de Persistencia para crear una
     * lista de animales
     * a partir del archivo .json, y, a partir de él reconstruye una lista de
     * contrladores.
     */

    public static void rebuildLista() {
        ArrayList<Animal> listaAnimales = buildListaFromResultSet();
        ArrayList<Controlador> listaControladores = new ArrayList<>();
        listaAnimales.forEach(animal -> listaControladores.add(new Controlador(animal)));
        lista = listaControladores;
    }

    /**
     * El método cargarPanelAnimales intenta cargar el componente VentanaAnimales
     * como ventana principal,
     * si el atributo ya tiene asignada una ventana, se destruye con el método
     * dispose
     *
     * @param v Un objeto del tipo VentanaAnimales
     */
    public static void cargarVentanaPrincipal(VentanaAnimales v) {
        if (ventanaPrincipal != null) ventanaPrincipal.dispose();
        ventanaPrincipal = v;

        JButton botonAlta = new JButton("Alta");
        JPanel contenedorBotones = new JPanel(new FlowLayout());

        botonAlta.addActionListener(e -> cargarVentanaAlta(new VentanaAlta(lista.size())));

        attachFiltros(contenedorBotones);
        contenedorBotones.add(botonAlta);
        v.getContentPane().add(contenedorBotones, BorderLayout.SOUTH);

        contenedorBotones.revalidate();
        contenedorBotones.repaint();
    }

    public static void attachFiltros(JPanel contenedorBotones) {
        ButtonGroup especieRButtons = new ButtonGroup();
        ButtonGroup estadoRButtons = new ButtonGroup();
        JButton botonBuscar = new JButton("Buscar");

        JPanel contReptil = new JPanel(new FlowLayout());
        JPanel contAve = new JPanel(new FlowLayout());
        JPanel contMamifero = new JPanel(new FlowLayout());
        JRadioButton ave = new JRadioButton();
        JRadioButton mamifero = new JRadioButton();
        JRadioButton reptil = new JRadioButton();
        ave.setActionCommand("Ave");
        mamifero.setActionCommand("Mamifero");
        reptil.setActionCommand("Reptil");
        contReptil.add(new JLabel("Reptil"));
        contReptil.add(reptil);
        contAve.add(new JLabel("Ave"));
        contAve.add(ave);
        contMamifero.add(new JLabel("Mamifero"));
        contMamifero.add(mamifero);

        especieRButtons.add(ave);
        especieRButtons.add(mamifero);
        especieRButtons.add(reptil);

        JPanel contTratamiento = new JPanel(new FlowLayout());
        JPanel contLiberados = new JPanel(new FlowLayout());
        JPanel contFallecidos = new JPanel(new FlowLayout());
        JRadioButton tratamiento = new JRadioButton();
        JRadioButton liberados = new JRadioButton();
        JRadioButton fallecidos = new JRadioButton();
        tratamiento.setActionCommand("Tratamiento");
        liberados.setActionCommand("Liberado");
        fallecidos.setActionCommand("Fallecido");
        contTratamiento.add(new JLabel("Tratamiento"));
        contTratamiento.add(tratamiento);
        contLiberados.add(new JLabel("Liberado"));
        contLiberados.add(liberados);
        contFallecidos.add(new JLabel("Fallecido"));
        contFallecidos.add(fallecidos);
        estadoRButtons.add(tratamiento);
        estadoRButtons.add(liberados);
        estadoRButtons.add(fallecidos);

        JPanel contenedorChecks = new JPanel(new GridLayout(1, 6));
        contenedorChecks.add(contMamifero);
        contenedorChecks.add(contReptil);
        contenedorChecks.add(contAve);
        contenedorChecks.add(contTratamiento);
        contenedorChecks.add(contLiberados);
        contenedorChecks.add(contFallecidos);

        botonBuscar.addActionListener(e -> accionBuscar(especieRButtons, estadoRButtons));
        contenedorBotones.add(contenedorChecks);
        contenedorBotones.add(botonBuscar);
    }

    private static void accionBuscar(ButtonGroup especieRButtons, ButtonGroup estadoRButtons) {
        ButtonModel especieSelecc = especieRButtons.getSelection();
        ButtonModel estadoSelecc = estadoRButtons.getSelection();

        if (especieSelecc == null && estadoSelecc == null) {
            cargarVentanaPrincipal(new VentanaAnimales(lista));
            return;
        }

        // Si alguno de los botones no está seleccionado, enviar un string vacío para
        // gestionarlo dentro del constructor, else, enviar la opción
        String[] filtro = {
                especieSelecc == null ? "" : especieSelecc.getActionCommand(),
                estadoSelecc == null ? "" : estadoSelecc.getActionCommand()
        };

        ArrayList<Controlador> lista = new ArrayList<>();
        GestionDatos.buildListaFromFiltros(filtro).forEach(a -> lista.add(new Controlador(a)));
        cargarVentanaPrincipal(new VentanaAnimales(lista));
    }

    /**
     * El método cargarPanelAnimal intenta cargar el componente VentanaAnimal como
     * ventana XXXXX, si el atributo ya tiene asignada una ventana, se destruye con el método
     * dispose
     *
     * @param v Un objeto del tipo VentanaAnimal
     */
    public static void cargarVentanaDetalle(VentanaAnimal v) {
        if (ventanaDetalle != null) ventanaDetalle.dispose();
        ventanaDetalle = v;
    }

    /**
     * El método cargarMenuAlta intenta cargar el componente VentanaAlta como
     * ventana XXXXX,
     * si el atributo ya tiene asignada una ventana, se destruye con el método
     * dispose
     *
     * @param v Un objeto del tipo VentanaAlta
     */
    public static void cargarVentanaAlta(VentanaAlta v) {
        if (ventanaAlta != null) ventanaAlta.dispose();
        ventanaAlta = v;
    }

    /**
     * El método cargarMenuAlta intenta cargar el componente VentanaTratamiento como
     * ventana XXXXX, si el atributo ya tiene asignada una ventana, se destruye con el método
     * dispose
     *
     * @param v Un objeto del tipo VentanaTratamiento
     */
    public static void cargarVentanaTratamiento(VentanaTratamiento v) {
        if (ventanaTratamiento != null) ventanaTratamiento.dispose();
        ventanaTratamiento = v;
    }

    /**
     * Este método destruye las ventanas principal y de detalle (en las que los
     * datos pueden desactualizarse
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
     * Este método añade a la lista un nuevo controlador y llama al método que carga
     * un nuevo panel de animales
     *
     * @param a El Animal a añadir
     */
    public static void addAnimal(Animal a) {
        lista.add(new Controlador(a));
        cargarVentanaPrincipal(new VentanaAnimales(lista));
    }
}
