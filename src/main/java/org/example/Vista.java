package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class Vista {
    /**
     * El panel se añade a las ventanas de la aplicación. Es la representación gráfica de los datos.
     */
    JPanel panel;

    /**
     * Este método toma los atributos del tipo de dato Mamifero, Reptil o Ave y crea un JPanel con su representación
     * para el usuario; cada vez que se llama a actualizarVista se reemplaza el atributo panel de la vista.
     * Probablemente sería mejor -y más o menos sencillo- actualizar selectivamente la parte del panel que
     * ha sido modificada, y, luego revalidar y repintar el componente.
     * P.ej., si se modifica el estado, o bien borrar ese JLabel y volverlo a añadir en esa posición con el valor
     * adecuado, o bien seleccionar el JLabel  y meter el nuevo estado.
     */
    public void actualizarVista(String tipo, int id, String especie, LocalDate fechaEntrada, String estado, int peso, HashMap<Date, String> historialTratamiento, String tipoLesion) {
        this.panel = new JPanel(new BorderLayout());

        Component[] components = buildComponents(tipo, id, especie, fechaEntrada, estado, peso, historialTratamiento, tipoLesion);

        panel.add(components[0], BorderLayout.NORTH);
        panel.add(components[1], BorderLayout.CENTER);
        panel.add(components[2], BorderLayout.SOUTH);

        this.panel.revalidate();
        this.panel.repaint();
    }

    public Component[] buildComponents(String tipo, int id, String especie, LocalDate fechaEntrada, String estado, int peso, HashMap<Date, String> historialTratamiento, String tipoLesion) {
        JPanel h1 = new JPanel(new GridLayout(1, 2));
        h1.add(new JLabel("Familia: " + tipo));
        h1.add(new JLabel("ID: " + id));

        JPanel h2 = new JPanel(new GridLayout(1, 2));
        h2.add(new JLabel("Especie: " + especie));
        h2.add(new JLabel("Peso: " + peso + "kg"));

        JLabel img = new JLabel(new ImageIcon("src/main/java/org/example/sil.png"));
        img.setSize(40, 40);

        JPanel headers = new JPanel(new GridLayout(2, 1));
        headers.add(h1);
        headers.add(h2);

        JPanel datosEntrada = new JPanel(new GridLayout(2, 2));
        datosEntrada.add(new JLabel("Fecha alta: " + fechaEntrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        datosEntrada.add(new JLabel("Tipo lesión: " + tipoLesion));
        datosEntrada.add(new JLabel("Estado: " + estado));

        return new Component[]{headers, img, datosEntrada};
    }
}
