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
     * Esencialmente el constructor y actualizarVista tienen la misma  función, ya que actualizarVista no es
     * selectivo. Es decir, siempre se crea un nuevo JPanel que se liga a la vista y se revalida y se pinta otra vez.
     */
    Vista(String tipo, int id, String especie, LocalDate fechaEntrada, String estado, int peso, HashMap<Date, String> historialTratamiento) {
        actualizarVista(tipo, id, especie, fechaEntrada, estado, peso, historialTratamiento);
    }

    /**
     * Este método toma los atributos del tipo de dato Mamifero, Reptil o Ave y crea un JPanel con su representación
     * para el usuario; cada vez que se llama a actualizarVista se reemplaza el atributo panel de la vista.
     * Probablemente sería mejor -y más o menos sencillo- actualizar selectivamente la parte del panel que
     * ha sido modificada, y, luego revalidar y repintar el componente.
     * P.ej., si se modifica el estado, o bien borrar ese JLabel y volverlo a añadir en esa posición con el valor
     * adecuado, o bien seleccionar el JLabel  y meter el nuevo estado.
     */
    public void actualizarVista(String tipo, int id, String especie, LocalDate fechaEntrada, String estado, int peso, HashMap<Date, String> historialTratamiento) {
        printAnimal(tipo, id, especie, fechaEntrada, estado, peso, historialTratamiento);
        this.panel = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout());
        header.add(new JLabel(tipo));
        header.add(new JLabel("ID: " + id));

        JPanel body = new JPanel(new BorderLayout());
        body.add(new JLabel(fechaEntrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), BorderLayout.NORTH);
        body.add(new JLabel(especie), BorderLayout.CENTER);
        body.add(new JLabel("Peso: " + peso), BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        panel.add(new JLabel(estado), BorderLayout.SOUTH);
        this.panel.revalidate();
        this.panel.repaint();
    }


    /**
     * DEBUG: Escribe los datos del animal
     */
    private static void printAnimal(String tipo, int id, String especie, LocalDate fechaEntrada, String estado, int peso, HashMap<Date, String> historialTratamiento) {
        System.out.println("->Animal " + id + " " + tipo);
        System.out.println("  Especie: " + especie + "  Entrada: " + fechaEntrada.toString());
        System.out.println("  Estado: " + estado + "  Peso:" + peso + "kg");
        historialTratamiento.forEach((k, v) -> {
            System.out.println("  ->Fecha:" + k);
            System.out.println("  ->Tratamiento: " + v);
        });
    }
}
