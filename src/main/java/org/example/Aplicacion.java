package org.example;

import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Esta clase contiene el main() y también contiene el punto de entrada a la interfaz, i.e., la ventana sobre
 * la que se insertan los animales.
 * <p>
 * En el patrón de diseño MVC (Modelo-Vista-Controlador), la lógica de negocio generalmente se maneja en el
 * controlador y/o en el modelo, mientras que la vista solo muestra los datos al usuario. La lógica de negocio
 * se refiere a las reglas y procesos que rigen cómo funciona una aplicación o sistema. Por ejemplo, en una
 * aplicación bancaria, la lógica de negocio podría incluir reglas sobre cómo se calculan los intereses o cómo se realizan las transferencias entre cuentas. En el patrón de diseño MVC (Modelo-Vista-Controlador), la lógica de negocio generalmente se maneja en el controlador y/o en el modelo, mientras que la vista solo muestra los datos al usuario.
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
        ventanaPrincipal.add(crearGridAnimales());
        ventanaPrincipal.setVisible(true);
    }

    private static JPanel crearGridAnimales() {
        GridLayout gl = new GridLayout(3, lista.size() / 3);
        JPanel gridPane = new JPanel(gl);
        gl.setHgap(25);
        gl.setVgap(25);

        for (Controlador e : lista) {
            JPanel contenedorAnimal = new JPanel(new BorderLayout());
            JButton botonDetalle = new JButton("Detalle");
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
        contenedorDetalle.add(contenedorBotones, BorderLayout.SOUTH);
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
        contenedorFecha.add(new JLabel("Fecha de fin: (12/03/2023)"));
        JTextField fecha = new JTextField(7);
        contenedorFecha.add(fecha);
        contenedorTratamiento.add(contenedorFecha, BorderLayout.NORTH);
        contenedorTratamiento.add(textArea, BorderLayout.CENTER);

        JButton botonAdd = new JButton("Añadir");
        botonAdd.addActionListener(e -> accionAddTratamiento(cont, fecha, textArea));

        contenedorTratamiento.add(botonAdd, BorderLayout.SOUTH);
        ventanaTratamiento.add(contenedorTratamiento);

        ventanaTratamiento.setVisible(true);
        //cargarVistaDetalle(cont); //sólo debería ocurrir si hay una update en los tratamientos <- temp
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

        LocalDate fechaParseada = parseFecha(fechaTexto);
        cont.tratamientoControlador(descripcionTexto, fechaParseada);
        cargarGrid();
        cargarVistaDetalle(cont);

        System.out.println("Añadir");
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
        //cargarGrid();
        //cargarVistaDetalle(cont);
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
        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventanaPrincipal.setSize(new Dimension(900, 1020));
        ventanaPrincipal.setTitle("Aplicación del santuario");
        cargarGrid();
    }
}
