package org.example.Componentes;

import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static org.example.Aplicacion.addAnimal;

public class VentanaAlta extends JFrame {
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

        botonAddAnimal.addActionListener(e -> accionAddAnimal(this, pesoTF, especie, familiasBtn, tipoLesion, listaSize));

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

        this.add(contenedorCampos, BorderLayout.CENTER);
        this.add(botonAddAnimal, BorderLayout.SOUTH);
        //centrar la ventana
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private static void accionAddAnimal(VentanaAlta ventanaAlta, JTextField pesoTF, JTextField especieTF, ButtonGroup familiasBtn, JCheckBox tipoLesion, int listaSize) {
        String peso = pesoTF.getText();
        String especie = especieTF.getText();

        if (pesoTF.getText().equals("") || especie.equals("") || familiasBtn.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, introduce valores en los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String familia = familiasBtn.getSelection().getActionCommand();

        System.out.println("Añadiendo " + familia);
        switch (familia) {
            case "Ave" -> {
                addAnimal(new Ave(especie, listaSize + 1, Integer.parseInt(peso), tipoLesion.isSelected()));
                ventanaAlta.dispose();
            }
            case "Mamífero" -> {
                addAnimal(new Mamifero(especie, listaSize + 1, Integer.parseInt(peso), tipoLesion.isSelected()));
                ventanaAlta.dispose();
            }
            case "Reptil" -> {
                addAnimal(new Reptil(especie, listaSize + 1, Integer.parseInt(peso), tipoLesion.isSelected()));
                ventanaAlta.dispose();
            }
            default -> throw new IllegalStateException("Valor inesperado: " + familia);
        }
    }


    static KeyAdapter adaptadorInputPeso(JTextField pesoTF, JLabel errorFecha) {
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

}
