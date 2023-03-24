package org.example;

import org.example.Modelo.Animal;
import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Esta clase contiene el main() y también contiene el punto de entrada a la interfaz, i.e., la ventana sobre
 * la que se insertan los animales.
 * <p>
 * En el patrón de diseño MVC (Modelo-Vista-Controlador), la lógica de negocio generalmente se maneja en el
 * controlador y/o en el modelo, mientras que la vista solo muestra los datos al usuario. La lógica de negocio
 * se refiere a las reglas y procesos que rigen cómo funciona una aplicación o sistema. Por ejemplo, en una
 * aplicación bancaria, la lógica de negocio podría incluir reglas sobre cómo se calculan los intereses o cómo
 * se realizan las transferencias entre cuentas. En el patrón de diseño MVC (Modelo-Vista-Controlador), la lógica
 * de negocio generalmente se maneja en el controlador y/o en el modelo, mientras que la vista solo muestra
 * los datos al usuario.
 */
public class Aplicacion {
    static JFrame ventanaPrincipal = new JFrame();
    static JFrame ventanaDetalle = new JFrame();
    /**
     * La lista de animales contiene los controladores para cada uno de los animales.
     * La interfaz gráfica se realiza creando un JFrame y añadiéndole a ese JFrame cada una de las vistas del controlador.
     * Desde la interfaz, se pueden enviar señales al controlador para que modifique los  datos del modelo y
     * justo después actualice la representación de los datos.
     */
    static ArrayList<Controlador> lista = new ArrayList<>();

    private static void cargarGrid() {
        ventanaPrincipal.getContentPane().removeAll();
        ventanaPrincipal.setLayout(new BorderLayout());
        ventanaPrincipal.add(crearGridAnimales(), BorderLayout.CENTER);
        JButton botonAlta = new JButton("Alta");
        botonAlta.addActionListener(e -> {
            JFrame ventanaAlta = new JFrame();
            ventanaAlta.setSize(450, 325);
            ventanaAlta.setResizable(false);

            JPanel contenedorCampos = new JPanel(new GridLayout(4, 1));
            JPanel contenedorPeso = new JPanel(new FlowLayout());
            JPanel contenedorFechaEntrada = new JPanel(new FlowLayout());
            JPanel contenedorEspecie = new JPanel(new FlowLayout());

            JTextField pesoTF = new JTextField(4);
            JTextField fechaEntradaTF = new JTextField(10);

            ButtonGroup especies = new ButtonGroup();
            JRadioButton aveBoton = new JRadioButton("Ave");
            JRadioButton mamiferoBoton = new JRadioButton("Mamífero");
            JRadioButton reptilBoton = new JRadioButton("Reptil");

            JCheckBox tipoLesion = new JCheckBox();

            JPanel contenedorLesiones = new JPanel(new FlowLayout());

            especies.add(mamiferoBoton);
            especies.add(aveBoton);
            especies.add(reptilBoton);

            contenedorFechaEntrada.add(new JLabel("Fecha de entrada"));
            contenedorFechaEntrada.add(fechaEntradaTF);
            contenedorEspecie.add(new JLabel("Especie"));
            contenedorEspecie.add(aveBoton);
            contenedorEspecie.add(mamiferoBoton);
            contenedorEspecie.add(reptilBoton);
            contenedorPeso.add(new JLabel("Peso"));
            contenedorPeso.add(pesoTF);
            contenedorLesiones.add(new JLabel("Lesion: "));
            contenedorLesiones.add(tipoLesion);

            contenedorCampos.add(contenedorEspecie);
            contenedorCampos.add(contenedorFechaEntrada);
            contenedorCampos.add(contenedorPeso);
            contenedorCampos.add(contenedorLesiones);
            //peso
            //especie
            //fechaEntrada
            //tipoLesion -> bool

            //id -> calculado
            //estado -> por defecto en tratamiento
            //fechasTratamientos -> no
            //descripcionTratamientos -> no

            ventanaAlta.add(contenedorCampos);

            ventanaAlta.setVisible(true);
        });
        ventanaPrincipal.add(botonAlta, BorderLayout.SOUTH);
        ventanaPrincipal.setVisible(true);
    }

    private static JPanel crearGridAnimales() {
        GridLayout gl = new GridLayout(3, lista.size() / 3);
        JPanel gridPane = new JPanel(gl);
        gl.setHgap(25);
        gl.setVgap(25);

        for (Controlador e : lista) {
            JPanel contenedorAnimal = new JPanel(new BorderLayout());
            JButton botonDetalle = new JButton("Detalles");
            botonDetalle.addActionListener(actionEvent -> cargarVistaDetalle(e));

            contenedorAnimal.add(e.vista.vistaNormal, BorderLayout.CENTER);
            contenedorAnimal.add(botonDetalle, BorderLayout.SOUTH);

            gridPane.add(contenedorAnimal);
        }
        return gridPane;
    }


    private static void cargarVistaDetalle(Controlador cont) {
        ventanaDetalle.getContentPane().removeAll();
        ventanaDetalle.setSize(600, 600);
        ventanaDetalle.add(crearVistaDetalle(cont));
        ventanaDetalle.revalidate();
        ventanaDetalle.repaint();
        ventanaDetalle.setVisible(true);
    }

    private static JPanel crearVistaDetalle(Controlador cont) {
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
        if (!cont.vista.fueraDelSantuario) contenedorDetalle.add(contenedorBotones, BorderLayout.SOUTH);
        return contenedorDetalle;
    }

    private static void accionTratamiento(Controlador cont) {
        JFrame ventanaTratamiento = new JFrame();
        ventanaTratamiento.setSize(450, 325);
        ventanaTratamiento.setResizable(false);

        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        JPanel contenedorTratamiento = new JPanel(new BorderLayout());
        contenedorTratamiento.setBorder(padding);

        JPanel contenedorFecha = new JPanel(new FlowLayout());
        JTextArea textArea = new JTextArea(5, 10);
        textArea.setLineWrap(true);
        contenedorFecha.add(new JLabel("Fecha de fin: (p.ej., 12/03/2023)"));
        JTextField fecha = new JTextField(7);
        contenedorFecha.add(fecha);
        contenedorTratamiento.add(contenedorFecha, BorderLayout.NORTH);
        contenedorTratamiento.add(textArea, BorderLayout.CENTER);

        JButton botonAdd = new JButton("Añadir");
        botonAdd.addActionListener(e -> accionAddTratamiento(cont, fecha, textArea));

        contenedorTratamiento.add(botonAdd, BorderLayout.SOUTH);
        ventanaTratamiento.add(contenedorTratamiento);

        ventanaTratamiento.setVisible(true);
    }

    private static void accionAddTratamiento(Controlador cont, JTextField fecha, JTextArea textArea) {
        String fechaTexto = fecha.getText().trim();
        String descripcionTexto = textArea.getText().trim();
        String mensajeError = "";

        if (!descripcionTexto.matches(".{12,144}")) {
            fecha.setText("");
            mensajeError += "Mal input de la descripción\n";
        }
        if (!fechaTexto.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d")) {
            textArea.setText("");
            mensajeError += "Mal input en la fecha";
        }
        if (mensajeError.length() != 0) {
            JOptionPane.showMessageDialog(null, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cont.nuevoTratamientoControlador(descripcionTexto, parseFecha(fechaTexto));

        cargarGrid();
        cargarVistaDetalle(cont);
    }

    private static LocalDate parseFecha(String fechaTexto) {
        String[] partesFecha = fechaTexto.split("/");
        String temp = partesFecha[0];
        partesFecha[0] = partesFecha[2]; //dd -> yyyy
        partesFecha[2] = temp; // yyyy -> dd
        return LocalDate.parse(partesFecha[0] + "-" + partesFecha[1] + "-" + partesFecha[2]);
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
            e.nuevoTratamientoControlador("Pastillas", LocalDate.now());
            e.nuevoTratamientoControlador("Pastillas 2", LocalDate.now());
            e.nuevoTratamientoControlador("Pastillas 3", LocalDate.now());
        });
        // DEBUG

    }

    public static void main(String[] args) {
        readToList();
        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventanaPrincipal.setSize(new Dimension(900, 1020));
        ventanaPrincipal.setTitle("Aplicación del santuario");
        cargarGrid();
    }
}
