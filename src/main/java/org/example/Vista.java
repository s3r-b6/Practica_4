package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
     * Este atributo marca si el animal ya no está bajo el cuidado del santuario, es decir, si ha muerto
     * o ha sido liberado. Si el animal ya no está bajo el control del mismo, no tiene sentido mostrar los
     * botones para interactuar con él (liberarlo, declararlo como fallecido, o, cambiar su tratamiento).
     */
    boolean fueraDelSantuario;

    /**
     * Este método toma los atributos del tipo de dato Mamifero, Reptil o Ave y crea un JPanel con su representación
     * para el usuario; cada vez que se llama a actualizarVista se reemplaza el atributo panel de la vista.
     * Probablemente sería mejor -y más o menos sencillo- actualizar selectivamente la parte del panel que
     * ha sido modificada, y, luego revalidar y repintar el componente.
     * P.ej., si se modifica el estado, o bien borrar ese JLabel y volverlo a añadir en esa posición con el valor
     * adecuado, o bien seleccionar el JLabel  y meter el nuevo estado.
     */
    public void actualizarVistas(String tipo, int id, String especie, String fechaEntrada, String estado, int peso, Object[][] historialTratamiento, String tipoLesion) {
        fueraDelSantuario = estado.equals("Fallecido") || estado.equals("Liberado");
        actualizarVistaNormal(buildCuerpo(tipo, id, especie, fechaEntrada, estado, peso, tipoLesion));
        actualizarVistaDetalle(buildCuerpo(tipo, id, especie, fechaEntrada, estado, peso, tipoLesion), historialTratamiento);
    }

    private void actualizarVistaNormal(JPanel contenedorCuerpo) {
        this.vistaNormal = contenedorCuerpo;
        this.vistaNormal.revalidate();
        this.vistaNormal.repaint();
    }

    private void actualizarVistaDetalle(JPanel contenedorCuerpo, Object[][] historialTratamiento) {
        DefaultTableModel model = new DefaultTableModel(historialTratamiento, new String[]{"Fecha-Inicio", "Tratamiento", "Fecha-Fin", "Completado"});
        JTable tabla = new JTable(model) {
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        tabla.setEnabled(false);
        tabla.setPreferredScrollableViewportSize(tabla.getPreferredSize());
        JScrollPane panelTratamientos = new JScrollPane(tabla);
        this.vistaDetalle = new JPanel(new BorderLayout());
        this.vistaDetalle.add(contenedorCuerpo, BorderLayout.NORTH);
        this.vistaDetalle.add(panelTratamientos, BorderLayout.CENTER);
        this.vistaDetalle.revalidate();
        this.vistaDetalle.repaint();
    }

    private JPanel buildCuerpo(String tipo, int id, String especie, String fechaEntrada, String estado, int peso, String tipoLesion) {
        JPanel contenedorCuerpo = new JPanel(new BorderLayout());
        JLabel labelEstado = new JLabel();
        JPanel h1 = new JPanel(new GridLayout(1, 2));
        h1.add(new JLabel("Familia: " + tipo));
        h1.add(new JLabel("ID: " + id));

        JPanel h2 = new JPanel(new GridLayout(1, 2));
        h2.add(new JLabel("Especie: " + especie));
        h2.add(new JLabel("Peso: " + peso + "kg"));

        JLabel img = getImg(tipo);
        img.setSize(40, 40);

        JPanel headers = new JPanel(new GridLayout(2, 1));
        headers.add(h1);
        headers.add(h2);

        JPanel datosEntrada = new JPanel(new GridLayout(2, 2));
        datosEntrada.add(new JLabel("Fecha alta: " + fechaEntrada));
        datosEntrada.add(new JLabel("Tipo lesión: " + tipoLesion));
        labelEstado.setText("Estado " + estado);
        datosEntrada.add(labelEstado);
        contenedorCuerpo.add(headers, BorderLayout.NORTH);
        contenedorCuerpo.add(img, BorderLayout.CENTER);
        contenedorCuerpo.add(datosEntrada, BorderLayout.SOUTH);
        return contenedorCuerpo;
    }

    private static JLabel getImg(String tipo) {
        StringBuilder imgPath = new StringBuilder("src/main/java/org/example/IMG/");
        switch (tipo) {
            case "Ave" -> imgPath.append("ave_");
            case "Mamifero" -> imgPath.append("mam_");
            case "Reptil" -> imgPath.append("rep_");
        }

        imgPath.append((int) ((Math.random() * (3 - 1)) + 1)).append(".png"); //rand_number entre 1 y 3


        return new JLabel(new ImageIcon(String.valueOf(imgPath)));
    }
}
