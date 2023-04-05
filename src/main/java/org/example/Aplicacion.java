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
import java.time.format.DateTimeFormatter;
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

    //Cargar JPanels || abrir  ventanas
    private static void cargarPanelAnimales() {
        ventanaPrincipal.getContentPane().removeAll();
        ventanaPrincipal.setLayout(new BorderLayout());
        ventanaPrincipal.add(crearPanelAnimales(), BorderLayout.CENTER);
        JButton botonAlta = new JButton("Alta");
        botonAlta.addActionListener(e -> abrirMenuAlta());
        ventanaPrincipal.add(botonAlta, BorderLayout.SOUTH);
        ventanaPrincipal.setVisible(true);
    }

    private static void cargarPanelAnimal(Controlador cont) {
        ventanaDetalle.getContentPane().removeAll();
        ventanaDetalle.setSize(600, 600);
        ventanaDetalle.setTitle("Vista de animal");
        ventanaDetalle.add(crearPanelAnimal(cont));
        ventanaDetalle.revalidate();
        ventanaDetalle.repaint();
        ventanaDetalle.setVisible(true);
    }

    private static void abrirMenuAlta() {
        ventanaAlta.getContentPane().removeAll();
        ventanaAlta.setSize(375, 250);
        ventanaAlta.setResizable(false);
        ventanaAlta.setTitle("Panel alta de animales");
        ventanaAlta.add(crearMenuAlta());
        ventanaAlta.setVisible(true);
    }

    private static void abrirMenuTratamiento(Controlador cont) {
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
        fecha.addKeyListener(adaptadorInputFecha(fecha, errorFecha));
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


    //Crear JPanels
    private static JPanel crearPanelAnimales() {
        GridLayout gl = new GridLayout(3, lista.size() / 3);
        JPanel gridPane = new JPanel(gl);
        gl.setHgap(25);
        gl.setVgap(25);

        for (Controlador e : lista) {
            JPanel contenedorAnimal = new JPanel(new BorderLayout());
            JButton botonDetalle = new JButton("Detalles");
            botonDetalle.addActionListener(actionEvent -> cargarPanelAnimal(e));

            contenedorAnimal.add(e.vista.vistaNormal, BorderLayout.CENTER);
            contenedorAnimal.add(botonDetalle, BorderLayout.SOUTH);

            gridPane.add(contenedorAnimal);
        }
        return gridPane;
    }

    private static JPanel crearPanelAnimal(Controlador cont) {
        JPanel contenedorDetalle = new JPanel(new BorderLayout());
        JPanel contenedorBotones = new JPanel(new GridLayout(1, 3));

        JButton botonBaja = new JButton("Baja");
        JButton botonLiberar = new JButton("Liberar");
        JButton botonTratamiento = new JButton("Tratamiento");
        botonBaja.addActionListener(e -> accionBaja(cont));
        botonLiberar.addActionListener(e -> accionLiberar(cont));
        botonTratamiento.addActionListener(e -> abrirMenuTratamiento(cont));

        contenedorBotones.add(botonBaja);
        contenedorBotones.add(botonLiberar);
        contenedorBotones.add(botonTratamiento);

        contenedorDetalle.add(cont.vista.vistaDetalle, BorderLayout.CENTER);
        if (!cont.vista.fueraDelSantuario) contenedorDetalle.add(contenedorBotones, BorderLayout.SOUTH);
        return contenedorDetalle;
    }

    public static JPanel crearMenuAlta() {
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
        aveBoton.setActionCommand("Ave");
        JRadioButton mamiferoBoton = new JRadioButton("Mamífero");
        mamiferoBoton.setActionCommand("Mamífero");
        JRadioButton reptilBoton = new JRadioButton("Reptil");
        reptilBoton.setActionCommand("Reptil");

        JCheckBox tipoLesion = new JCheckBox();
        JButton botonAddAnimal = new JButton("Añadir");

        errorPeso.setForeground(Color.RED);
        pesoTF.addKeyListener(adaptadorInputPeso(pesoTF, errorPeso));

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

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(contenedorCampos, BorderLayout.CENTER);
        contenedor.add(botonAddAnimal, BorderLayout.SOUTH);
        return contenedor;
    }

    //Acciones

    private static void accionAddAnimal(JTextField pesoTF, JTextField especieTF, ButtonGroup familiasBtn, JCheckBox tipoLesion) {
        String peso = pesoTF.getText();
        String especie = especieTF.getText();

        if (pesoTF.getText().equals("") || especie.equals("") || familiasBtn.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, introduce valores en los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String familia = familiasBtn.getSelection().getActionCommand();
        switch (familia) {
            case "Ave" -> {
                System.out.println("Ave");
                lista.add(new Controlador(new Ave(especie, lista.size() + 1, Integer.parseInt(peso), tipoLesion.isSelected())));
                cargarPanelAnimales();
            }
            case "Mamífero" -> {
                System.out.println("Mamifero");
                lista.add(new Controlador(new Mamifero(especie, lista.size() + 1, Integer.parseInt(peso), tipoLesion.isSelected())));
                cargarPanelAnimales();
            }
            case "Reptil" -> {
                System.out.println("Reptil");
                lista.add(new Controlador(new Reptil(especie, lista.size() + 1, Integer.parseInt(peso), tipoLesion.isSelected())));
                cargarPanelAnimales();
            }
        }
    }

    private static void accionBaja(Controlador cont) {
        cont.bajaControlador();
        cargarPanelAnimales();
        cargarPanelAnimal(cont);
    }

    private static void accionLiberar(Controlador cont) {
        cont.liberacionControlador();
        cargarPanelAnimales();
        cargarPanelAnimal(cont);
    }

    private static void accionAddTratamiento(Controlador cont, JTextField fecha, JTextArea textArea) {
        String fechaTexto = fecha.getText().trim();
        String descripcionTexto = textArea.getText().trim();
        LocalDate fechaParseada;
        try {
            fechaParseada = LocalDate.parse(fechaTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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
        cargarPanelAnimales();
        cargarPanelAnimal(cont);
    }

    //KeyAdapters (controlar inputs por teclado)

    private static KeyAdapter adaptadorInputPeso(JTextField pesoTF, JLabel errorFecha) {
        return new KeyAdapter() {
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
    }

    private static KeyAdapter adaptadorInputFecha(JTextField especieTF, JLabel errorEspecie) {
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

    private static void addToLista() {
        lista.add(new Controlador(new Reptil("Lagarto", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Mamifero("Perro", lista.size() + 1, 15, true)));
        lista.add(new Controlador(new Ave("Jilguero", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Reptil("Cocodrilo", lista.size() + 1, 18, true)));
        lista.add(new Controlador(new Mamifero("Ciervo", lista.size() + 1, 25, true)));
        lista.add(new Controlador(new Ave("Cigüeña", lista.size() + 1, 7, true)));
        lista.add(new Controlador(new Reptil("Salamandra", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Mamifero("Gato", lista.size() + 1, 4, true)));
        lista.add(new Controlador(new Ave("Paloma", lista.size() + 1, 1, true)));
        //Cargar algunos tratamientos de prueba
        lista.forEach(e -> {
            int rand1 = (int) ((Math.random() * 3) + 0);
            if (rand1 >= 0) e.nuevoTratamientoControlador("Fisioterapia", getFechaAleatoria());
            if (rand1 >= 1) e.nuevoTratamientoControlador("Medicina B", getFechaAleatoria());
            if (rand1 >= 2) e.nuevoTratamientoControlador("Pastillas C", getFechaAleatoria());
        });
    }

    private static LocalDate getFechaAleatoria() {
        int rand2 = (int) ((Math.random() * (12 - 1)) + 1);
        StringBuilder fecha = new StringBuilder("2023-");
        if (rand2 < 10) fecha.append("0").append(rand2).append("-01");
        else fecha.append(rand2).append("-01");
        return LocalDate.parse(fecha);
    }

    public static void main(String[] args) {
        addToLista();
        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventanaPrincipal.setSize(new Dimension(900, 1020));
        ventanaPrincipal.setTitle("Aplicación del santuario");
        cargarPanelAnimales();
    }
}
