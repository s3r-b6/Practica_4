package org.example;

import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    //TODO: Añadir títulos a las ventanas

    static JFrame ventanaPrincipal = new JFrame();
    static JFrame ventanaDetalle = new JFrame();
    static JFrame ventanaAlta = new JFrame();
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
        botonAlta.addActionListener(e -> accionMenuAlta());
        ventanaPrincipal.add(botonAlta, BorderLayout.SOUTH);
        ventanaPrincipal.setVisible(true);
    }

    private static void accionMenuAlta() {
        ventanaAlta.getContentPane().removeAll();
        ventanaAlta.setLayout(new BorderLayout());
        ventanaAlta.setSize(375, 250);
        ventanaAlta.setResizable(false);

        JPanel contenedorCampos = new JPanel();
        contenedorCampos.setLayout(new BoxLayout(contenedorCampos, BoxLayout.PAGE_AXIS));
        JPanel contenedorPeso = new JPanel(new GridLayout(2, 1));
        JPanel contenedorFechaEntrada = new JPanel(new GridLayout(2, 2));
        JPanel contenedorLesiones = new JPanel(new GridLayout(1, 2));
        JPanel contenedorTipo = new JPanel(new GridLayout(1, 1));

        JLabel errorPeso = new JLabel();

        JTextField especie = new JTextField(10);
        JTextField pesoTF = new JTextField(4);

        ButtonGroup familiasBtn = new ButtonGroup();
        JRadioButton aveBoton = new JRadioButton("Ave");
        JRadioButton mamiferoBoton = new JRadioButton("Mamífero");
        JRadioButton reptilBoton = new JRadioButton("Reptil");

        JCheckBox tipoLesion = new JCheckBox();
        JButton botonAddAnimal = new JButton("Añadir");

        errorPeso.setForeground(Color.RED);
        pesoTF.addKeyListener(parseInputPeso(pesoTF, errorPeso));

        botonAddAnimal.addActionListener(e -> accionAddAnimal(pesoTF, especie, familiasBtn, tipoLesion));

        familiasBtn.add(mamiferoBoton);
        familiasBtn.add(aveBoton);
        familiasBtn.add(reptilBoton);

        contenedorFechaEntrada.add(new JLabel("Especie: "));
        contenedorFechaEntrada.add(especie);
        contenedorTipo.add(new JLabel("Familia: "));
        contenedorTipo.add(aveBoton);
        contenedorTipo.add(mamiferoBoton);
        contenedorTipo.add(reptilBoton);
        JPanel contenedorLabelPeso = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contenedorLabelPeso.add(new JLabel("Peso: "));
        contenedorLabelPeso.add(errorPeso);
        contenedorPeso.add(contenedorLabelPeso);
        contenedorPeso.add(pesoTF);
        contenedorLesiones.add(new JLabel("Caza / Atropello / Infeccion: "));
        contenedorLesiones.add(tipoLesion);

        contenedorCampos.add(contenedorTipo);
        contenedorCampos.add(contenedorFechaEntrada);
        contenedorCampos.add(contenedorPeso);
        contenedorCampos.add(contenedorLesiones);

        ventanaAlta.add(contenedorCampos, BorderLayout.CENTER);
        ventanaAlta.add(botonAddAnimal, BorderLayout.SOUTH);

        ventanaAlta.setVisible(true);
    }

    //TODO ---->
    private static void accionAddAnimal(JTextField pesoTF, JTextField especieTF, ButtonGroup familiasBtn, JCheckBox tipoLesion) {
        String familia = familiasBtn.getSelection().toString();
        String peso = pesoTF.getText();
        String especie = especieTF.getText();

        switch (familia) {
            case "Ave" ->
                    lista.add(new Controlador(new Ave(especie, lista.size() + 1, Integer.parseInt(peso), tipoLesion.isSelected())));
            case "Mamífero" ->
                    lista.add(new Controlador(new Mamifero(especie, lista.size() + 1, Integer.parseInt(peso), tipoLesion.isSelected())));
            case "Reptil" ->
                    lista.add(new Controlador(new Reptil(especie, lista.size() + 1, Integer.parseInt(peso), tipoLesion.isSelected())));
        }
    }

    private static KeyAdapter parseInputPeso(JTextField pesoTF, JLabel errorFecha) {
        KeyAdapter keyAdapter = new KeyAdapter() {
            public void keyPressed(KeyEvent key) {
                String value = pesoTF.getText();
                if (key.getKeyChar() >= '0' && key.getKeyChar() <= '9') {
                    pesoTF.setEditable(true);
                    errorFecha.setText("");
                } else if (value.length() > 4) {
                    pesoTF.setEditable(false);
                    pesoTF.setText("");
                    errorFecha.setText("Por favor, introduce un valor válido (muy largo)");
                } else {
                    pesoTF.setEditable(false);
                    pesoTF.setText("");
                    errorFecha.setText("Por favor, introduce sólo números");
                }
            }
        };
        return keyAdapter;
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
        JLabel errorFecha = new JLabel("");
        errorFecha.setForeground(Color.RED);
        JPanel contenedorFecha = new JPanel(new GridLayout(2, 1));
        JTextArea textArea = new JTextArea(5, 10);
        textArea.setLineWrap(true);
        contenedorFecha.add(new JLabel("Fecha de fin: (p.ej., 12/03/2023)"));
        JTextField fecha = new JTextField(7);
        fecha.addKeyListener(fechaKeyAdapter(fecha, errorFecha));
        contenedorFecha.add(fecha);
        contenedorFecha.add(errorFecha);
        contenedorTratamiento.add(contenedorFecha, BorderLayout.NORTH);
        contenedorTratamiento.add(textArea, BorderLayout.CENTER);

        JButton botonAdd = new JButton("Añadir");
        botonAdd.addActionListener(e -> accionAddTratamiento(cont, fecha, textArea));

        contenedorTratamiento.add(botonAdd, BorderLayout.SOUTH);
        ventanaTratamiento.add(contenedorTratamiento);

        ventanaTratamiento.setVisible(true);
    }

    private static KeyAdapter fechaKeyAdapter(JTextField especieTF, JLabel errorEspecie) {
        return new KeyAdapter() {
            public void keyPressed(KeyEvent key) {
                String value = especieTF.getText();
                if (value.length() >= 10 || (key.getKeyChar() < '0' || key.getKeyChar() > '9')) {
                    especieTF.setEditable(false);
                    especieTF.setText("");
                    errorEspecie.setText("Por favor, introduce sólo números");
                } else {
                    especieTF.setEditable(true);
                    errorEspecie.setText("");
                    if (value.length() == 2 || value.length() == 5)
                        especieTF.setText(value + '/');
                }
            }
        };
    }

    private static void accionAddTratamiento(Controlador cont, JTextField fecha, JTextArea textArea) {
        String fechaTexto = fecha.getText().trim();
        String descripcionTexto = textArea.getText().trim();

        LocalDate fechaParseada;
        try {
            fechaParseada = parseFecha(fechaTexto);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Por favor introduce una fecha válida, p.ej: 12/02/2023", "Error", JOptionPane.ERROR_MESSAGE);
            fecha.setText("");
            return;
        }

        if (!descripcionTexto.matches(".{12,144}")) {
            JOptionPane.showMessageDialog(null, "Mal input de la descripción", "Error", JOptionPane.ERROR_MESSAGE);
            textArea.setText("");
            return;
        }

        cont.nuevoTratamientoControlador(descripcionTexto, fechaParseada);

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
