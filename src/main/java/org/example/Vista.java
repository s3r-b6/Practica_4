package org.example;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

/**
 * Esencialmente, un JPanel y las operaciones que se realizan sobre él.
 * Es la representación gráfica del modelo de datos. Cuando el Controlador actualiza el estado de algún dato, envía
 * una señal a la vista del dato para que dibuje los nuevos datos. La vista no tiene acceso a los datos del modelo,
 * sólo a los que el controlador le pasa a la hora de redibujar el componente
 */
public class Vista {
    JPanel vistaNormal;
    JPanel vistaDetalle;

    /**
     * Este método toma los atributos del tipo de dato Mamifero, Reptil o Ave y crea un JPanel con su representación
     * para el usuario; cada vez que se llama a actualizarVista se reemplaza el atributo panel de la vista.
     * Probablemente sería mejor -y más o menos sencillo- actualizar selectivamente la parte del panel que
     * ha sido modificada, y, luego revalidar y repintar el componente.
     * P.ej., si se modifica el estado, o bien borrar ese JLabel y volverlo a añadir en esa posición con el valor
     * adecuado, o bien seleccionar el JLabel  y meter el nuevo estado.
     */
    public void actualizarVistas(String tipo, int id, String especie, String fechaEntrada, String estado, int peso, String[][] historialTratamiento, String tipoLesion) {
        JPanel contenedorCuerpo = buildCuerpo(tipo, id, especie, fechaEntrada, estado, peso, tipoLesion);
        actualizarVistaNormal(contenedorCuerpo);
        contenedorCuerpo = buildCuerpo(tipo, id, especie, fechaEntrada, estado, peso, tipoLesion);
        actualizarVistaDetalle(contenedorCuerpo, historialTratamiento);
    }

    private void actualizarVistaNormal(JPanel contenedorCuerpo) {
        this.vistaNormal = new JPanel(new BorderLayout());
        this.vistaNormal.add(contenedorCuerpo);
        this.vistaNormal.revalidate();
        this.vistaNormal.repaint();
    }

    private void actualizarVistaDetalle(JPanel contenedorCuerpo, String[][] historialTratamiento) {
        JTable tabla = new JTable(historialTratamiento, new String[]{"Fecha-Inicio", "Tratamiento", "Fecha-Fin"});
        tabla.setEnabled(false);
        JScrollPane panelTratamientos = new JScrollPane(tabla);
        this.vistaDetalle = new JPanel(new BorderLayout());
        this.vistaDetalle.add(contenedorCuerpo, BorderLayout.NORTH);
        this.vistaDetalle.add(panelTratamientos, BorderLayout.CENTER);
        this.vistaDetalle.revalidate();
        this.vistaDetalle.repaint();
    }

    private static JPanel buildCuerpo(String tipo, int id, String especie, String fechaEntrada, String estado, int peso, String tipoLesion) {
        JPanel contenedorCuerpo = new JPanel(new BorderLayout());

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
        datosEntrada.add(new JLabel("Fecha alta: " + fechaEntrada));
        datosEntrada.add(new JLabel("Tipo lesión: " + tipoLesion));
        datosEntrada.add(new JLabel("Estado: " + estado));

        contenedorCuerpo.add(headers, BorderLayout.NORTH);
        contenedorCuerpo.add(img, BorderLayout.CENTER);
        contenedorCuerpo.add(datosEntrada, BorderLayout.SOUTH);
        return contenedorCuerpo;
    }
}
