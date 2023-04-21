package org.example.Componentes;

import org.example.Aplicacion;
import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Esta clase representa el JFrame de la "ventana de alta de animales" de la aplicación
 */
public class VentanaAlta extends JFrame {
    /**
     * El constructor del componente de alta de animales
     *
     * @param listaSize El número de animales que hay en la lista. Se utiliza para asignarle id al animal que se crea nuevo
     */
    public VentanaAlta(int listaSize) {
        this.setSize(375, 250);
        this.setResizable(false);
        this.setTitle("Panel alta de animales");
        this.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon((System.getProperty("user.dir") + "/src/main/java/org/example/IMG/icono.png")).getImage());

        JPanel contenedorCampos = new JPanel();
        contenedorCampos.setLayout(new BoxLayout(contenedorCampos, BoxLayout.PAGE_AXIS));
        JPanel contenedorPeso = new JPanel(new GridLayout(2, 1));
        JPanel contenedorFechaEntrada = new JPanel(new GridLayout(2, 2));
        JPanel contenedorLesiones = new JPanel(new GridLayout(1, 2));
        JPanel contenedorTipo = new JPanel(new GridLayout(1, 1));
        JPanel contenedorGravedad = new JPanel(new GridLayout(1, 1));

        JLabel labelTipoLesion = new JLabel("Caza / Atropello / Infeccion: ");
        JLabel errorPeso = new JLabel();

        JTextField especie = new JTextField(10);
        JTextField pesoTF = new JTextField(4);

        ButtonGroup familiasBtn = new ButtonGroup();
        ButtonGroup gravedadBtn = new ButtonGroup();

        JRadioButton aveBoton = new JRadioButton("Ave");
        aveBoton.setActionCommand("Ave");
        JRadioButton mamiferoBoton = new JRadioButton("Mamífero");
        mamiferoBoton.setActionCommand("Mamífero");
        JRadioButton reptilBoton = new JRadioButton("Reptil");
        reptilBoton.setActionCommand("Reptil");

        aveBoton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) labelTipoLesion.setText("Caza");
        });
        mamiferoBoton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) labelTipoLesion.setText("Atropello");
        });
        reptilBoton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) labelTipoLesion.setText("Infección");
        });

        JRadioButton gravedadAlta = new JRadioButton("Alta");
        gravedadAlta.setActionCommand("Alta");
        JRadioButton gravedadMedia = new JRadioButton("Media");
        gravedadMedia.setActionCommand("Media");
        JRadioButton gravedadBaja = new JRadioButton("Baja");
        gravedadBaja.setActionCommand("Baja");

        JCheckBox tipoLesion = new JCheckBox();
        JButton botonAddAnimal = new JButton("Añadir");

        errorPeso.setForeground(Color.RED);
        pesoTF.addKeyListener(adaptadorInputPeso(pesoTF, errorPeso));

        botonAddAnimal.addActionListener(e -> accionAddAnimal(this, pesoTF, especie, familiasBtn, tipoLesion, gravedadBtn, listaSize));

        familiasBtn.add(mamiferoBoton);
        familiasBtn.add(aveBoton);
        familiasBtn.add(reptilBoton);

        contenedorFechaEntrada.add(new JLabel("Especie: "));
        contenedorFechaEntrada.add(especie);
        contenedorTipo.add(new JLabel("Familia: "));
        contenedorTipo.add(aveBoton);
        contenedorTipo.add(mamiferoBoton);
        contenedorTipo.add(reptilBoton);

        gravedadBtn.add(gravedadAlta);
        gravedadBtn.add(gravedadMedia);
        gravedadBtn.add(gravedadBaja);

        contenedorGravedad.add(new JLabel("Gravedad: "));
        contenedorGravedad.add(gravedadAlta);
        contenedorGravedad.add(gravedadMedia);
        contenedorGravedad.add(gravedadBaja);

        JPanel contenedorLabelPeso = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contenedorLabelPeso.add(new JLabel("Peso: "));
        contenedorLabelPeso.add(errorPeso);
        contenedorPeso.add(contenedorLabelPeso);
        contenedorPeso.add(pesoTF);
        contenedorLesiones.add(labelTipoLesion);
        contenedorLesiones.add(tipoLesion);

        contenedorCampos.add(contenedorTipo);
        contenedorCampos.add(contenedorFechaEntrada);
        contenedorCampos.add(contenedorPeso);
        contenedorCampos.add(contenedorLesiones);
        contenedorCampos.add(contenedorGravedad);

        this.add(contenedorCampos, BorderLayout.CENTER);
        this.add(botonAddAnimal, BorderLayout.SOUTH);
        //centrar la ventana
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    /**
     * Este es el método que toma los valores de la ventana de alta para registrar un nuevo animal.
     *
     * @param ventanaAlta Un puntero a la ventana de alta, para destruirla en caso de que se cree el nuevo animal
     * @param pesoTF      El JTextField que contiene el peso
     * @param especieTF   El JTextField que contiene la especie
     * @param familiasBtn El grupo de botones que marca el tipo de animal
     * @param tipoLesion  El JCheckbox que marca si el tipo de lesión es la característica de la especie, o no
     * @param listaSize   El tamaño actual de la lista (listaSize+1 será la ID del animal nuevo)
     */
    private static void accionAddAnimal(VentanaAlta ventanaAlta, JTextField pesoTF, JTextField especieTF, ButtonGroup familiasBtn, JCheckBox tipoLesion, ButtonGroup gravedadBtn, int listaSize) {
        String peso = pesoTF.getText();
        String especie = especieTF.getText();

        if (pesoTF.getText().equals("") || especie.equals("") || familiasBtn.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, introduce valores en los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String familia = familiasBtn.getSelection().getActionCommand();
        String gravedad = gravedadBtn.getSelection().getActionCommand();
        if (gravedad == null) gravedad = "N/A";
        switch (familia) {
            case "Ave" -> {
                Aplicacion.addAnimal(new Ave(especie, listaSize + 1, Integer.parseInt(peso), tipoLesion.isSelected(), gravedad));
                ventanaAlta.dispose();
            }
            case "Mamífero" -> {
                Aplicacion.addAnimal(new Mamifero(especie, listaSize + 1, Integer.parseInt(peso), tipoLesion.isSelected(), gravedad));
                ventanaAlta.dispose();
            }
            case "Reptil" -> {
                Aplicacion.addAnimal(new Reptil(especie, listaSize + 1, Integer.parseInt(peso), tipoLesion.isSelected(), gravedad));
                ventanaAlta.dispose();
            }
            default -> throw new IllegalStateException("Valor inesperado: " + familia);
        }
    }


    /**
     * Este metodo devuelve el KeyAdapter que controla los keyPresses del input de peso
     *
     * @param pesoTF  El JTextField en el que se introduce el peso
     * @param errores Un JLabel sobre el que se escriben errores (tiene color rojo)
     * @return
     */
    static KeyAdapter adaptadorInputPeso(JTextField pesoTF, JLabel errores) {
        return new KeyAdapter() {
            public void keyPressed(KeyEvent key) {
                String value = pesoTF.getText();
                if (key.getKeyChar() >= '0' && key.getKeyChar() <= '9') {
                    pesoTF.setEditable(true);
                    errores.setText("");
                } else if (value.length() > 4) {
                    pesoTF.setEditable(false);
                    pesoTF.setText("");
                    errores.setText("Por favor, introduce un valor válido (muy largo)");
                } else {
                    pesoTF.setEditable(false);
                    pesoTF.setText("");
                    errores.setText("Por favor, introduce sólo números");
                }
            }
        };
    }

}
