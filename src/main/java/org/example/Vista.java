package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Es la representación de cara al usuario del modelo de datos. Cuando el
 * Controlador actualiza el estado de
 * algún dato, envía una señal a la vista del dato para que dibuje los nuevos
 * datos. La vista no tiene acceso
 * a los datos del modelo, sólo a los que el controlador le pasa a la hora de
 * redibujar el componente
 */
public class Vista {

    /**
     * JPanel que representa el estado general del animal
     */
    JPanel vistaNormal;
    /**
     * JPanel que representa el estado general del animal, y, además, datos
     * específicos como sus tratamientos
     */
    JPanel vistaDetalle;

    /**
     * Este atributo expone la especie del Animal
     */
    String familia;

    /**
     * El constructor toma los datos del modelo y crea una vista normal y una vista
     * detalle.
     */
    Vista(String tipo, int id, String especie, String fechaEntrada, String fechaSalida, String estado, int peso,
            Object[][] historialTratamiento, String tipoLesion, String gravedad) {
        this.vistaNormal = buildCuerpo(tipo, id, especie, fechaEntrada, fechaSalida, estado, peso, tipoLesion,
                gravedad);
        this.vistaDetalle = buildDetalle(
                buildCuerpo(tipo, id, especie, fechaEntrada, fechaSalida, estado, peso, tipoLesion, gravedad),
                historialTratamiento);
        this.familia = tipo;
    }

    /**
     * @return La familia del animal
     */
    public String getFamilia() {
        return this.familia;
    }

    /**
     * Toma un cuerpo y la representación de tabla del historialTratamiento
     *
     * @param contenedorCuerpo     El cuerpo "normal" del componente. Una llamada a
     *                             buildCuerpo
     * @param historialTratamiento La representación de los tratamientos como un
     *                             array de arrays de objetos (strings)
     * @return Devuelve el JPanel que representa "en detalle" al animal
     */
    private JPanel buildDetalle(JPanel contenedorCuerpo, Object[][] historialTratamiento) {
        DefaultTableModel model = new DefaultTableModel(historialTratamiento,
                new String[] { "Fecha-Inicio", "Fecha-Fin", "Tratamiento", "Completado" });
        JTable tabla = new JTable(model) {
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        tabla.setEnabled(false);
        tabla.setPreferredScrollableViewportSize(tabla.getPreferredSize());
        JScrollPane panelTratamientos = new JScrollPane(tabla);
        JPanel cont = new JPanel(new BorderLayout());
        cont.add(contenedorCuerpo, BorderLayout.NORTH);
        cont.add(panelTratamientos, BorderLayout.CENTER);
        return cont;
    }

    /**
     * @return La vista normal
     */
    public JPanel getVistaNormal() {
        return vistaNormal;
    }

    /**
     * @return La vista detalle
     */
    public JPanel getVistaDetalle() {
        return vistaDetalle;
    }

    /**
     * A partir de los datos se construye un cuerpo (un JPanel) que muestra todos
     * los datos del animal
     *
     * @param tipo         El tipo de animal
     * @param id           El id del animal
     * @param especie      La especie del animal
     * @param fechaEntrada La fecha en que entró al centros
     * @param fechaSalida  La fecha en que salió del centro
     *                     (liberación/fallecimiento)
     * @param estado       El estado en que se encuentra
     * @param peso         El peso del animal
     * @param tipoLesion   El tipo de lesión por que fue ingresado
     * @return Devuelve el JPanel que representa los datos del animal
     */
    private JPanel buildCuerpo(String tipo, int id, String especie, String fechaEntrada, String fechaSalida,
            String estado, int peso, String tipoLesion, String gravedad) {
        JPanel contenedorCuerpo = new JPanel(new BorderLayout());
        JPanel h1 = new JPanel(new GridLayout(1, 2));
        h1.add(new JLabel("Familia: " + tipo));
        h1.add(new JLabel("ID: " + id));

        JPanel h2 = new JPanel(new GridLayout(1, 2));
        h2.add(new JLabel("Especie: " + especie));
        h2.add(new JLabel("Peso: " + peso + "kg"));

        JLabel img = getImgLabel(tipo, estado);
        img.setSize(50, 50);

        JPanel headers = new JPanel(new GridLayout(2, 1));
        headers.add(h1);
        headers.add(h2);

        JPanel datosEntrada = new JPanel(new GridLayout(2, 2));
        datosEntrada.add(new JLabel("Fecha alta: " + fechaEntrada));
        JLabel labelFechaSalida = new JLabel();
        if (!fechaSalida.equals(""))
            labelFechaSalida.setText("Fecha baja: " + fechaSalida);
        datosEntrada.add(labelFechaSalida);
        JLabel labelLesion = new JLabel("Tipo lesión: " + tipoLesion);
        if (!estado.equals("Fallecido") && !estado.equals("Liberado")) {
            switch (gravedad) {
                case "Alta" -> labelLesion.setForeground(new Color(211, 33, 44));
                case "Media" -> labelLesion.setForeground(new Color(255, 152, 14));
                case "Baja" -> labelLesion.setForeground(new Color(6, 156, 86));
                default -> labelLesion.setForeground(Color.BLACK);
            }
        }
        datosEntrada.add(labelLesion);
        datosEntrada.add(new JLabel("Estado " + estado));
        contenedorCuerpo.add(headers, BorderLayout.NORTH);
        contenedorCuerpo.add(img, BorderLayout.CENTER);
        contenedorCuerpo.add(datosEntrada, BorderLayout.SOUTH);
        return contenedorCuerpo;
    }

    /**
     * Devuelve un JLabel con una imagen u otra según el estado y tipo del animal
     *
     * @param tipo   El tipo del animal
     * @param estado El estado en que se encuentra
     * @return La imagen que representa ambos datos
     */
    private static JLabel getImgLabel(String tipo, String estado) {
        StringBuilder imgPath = new StringBuilder("src/main/java/org/example/IMG/");
        switch (tipo) {
            case "Ave" -> imgPath.append("ave_");
            case "Mamifero" -> imgPath.append("mam_");
            case "Reptil" -> imgPath.append("rep_");
            default -> throw new IllegalStateException("Valor inesperado: " + tipo);
        }
        switch (estado) {
            case "Tratamiento" -> imgPath.append("1.png");
            case "Fallecido" -> imgPath.append("2.png");
            case "Liberado" -> imgPath.append("3.png");
            default -> throw new IllegalStateException("Valor inesperado: " + estado);
        }
        return new JLabel(new ImageIcon(String.valueOf(imgPath)));
    }
}
