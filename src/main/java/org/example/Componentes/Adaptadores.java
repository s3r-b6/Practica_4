package org.example.Componentes;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Adaptadores {
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

    static KeyAdapter adaptadorInputFecha(JTextField especieTF, JLabel errorEspecie) {
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


}
