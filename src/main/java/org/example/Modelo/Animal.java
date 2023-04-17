package org.example.Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Esta clase representa el modelo de datos de los animales. Las familias concretas de animales heredan de ella.
 */
public abstract class Animal {
    int id;
    int peso;
    LocalDate fechaEntrada;
    LocalDate fechaSalida;
    String especie;
    Estado estado;
    ArrayList<LocalDate[]> fechasTratamientos;
    ArrayList<String> descripcionTratamientos;

    /**
     * Constructor de animales nuevos. No toma datos como fecha entrada, porque se supone que se dan de alta en
     * el momento en que entran. Igualmente con el estado. Se utiliza a través de super.
     *
     * @param id      La id
     * @param peso    El peso
     * @param especie La especie
     */
    public Animal(int id, int peso, String especie) {
        this.id = id;
        this.peso = peso;
        this.fechaEntrada = LocalDate.now();
        this.especie = especie;
        this.estado = Estado.Tratamiento;
        this.fechasTratamientos = new ArrayList<>();
        this.descripcionTratamientos = new ArrayList<>();
    }

    /**
     * Este constructor utiliza todos los atributos, ya que es el que se utiliza para recrear los animales
     * a partir de los datos almacenados.
     *
     * @param id                      La id
     * @param peso                    El peso
     * @param fechaEntrada            La fecha de entrada del animal
     * @param fechaSalida             La fecha de salida del animal
     * @param especie                 La especie del animal
     * @param estado                  El estado del animal
     * @param fechasTratamientos      Array de arrays. Las fechas de inicio[x][0] y fin[x][1] de los tratamientos
     * @param descripcionTratamientos Las descripciones de los diferentes tratamientos
     */
    public Animal(int id, int peso, LocalDate fechaEntrada, LocalDate fechaSalida, String especie, String estado, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        Estado st = null;
        switch (estado) {
            case "Liberado" -> st = Estado.Liberado;
            case "Fallecido" -> st = Estado.Fallecido;
            case "Tratamiento" -> st = Estado.Tratamiento;
        }
        this.id = id;
        this.peso = peso;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.especie = especie;
        this.estado = st;
        this.fechasTratamientos = new ArrayList<>(Arrays.asList(fechasTratamientos));
        this.descripcionTratamientos = new ArrayList<>(Arrays.asList(descripcionTratamientos));
    }

    /**
     * A partir de todos los datos se llama a un constructor u otro. Factoría.
     *
     * @param id                      La id
     * @param tipo                    El tipo o "familia" del animal
     * @param peso                    El peso
     * @param fechaEntrada            La fecha de entrada del animal
     * @param fechaSalida             La fecha de salida del animal
     * @param especie                 La especie del animal
     * @param estado                  El estado del animal
     * @param fechasTratamientos      Array de arrays. Las fechas de inicio[x][0] y fin[x][1] de los tratamientos
     * @param descripcionTratamientos Las descripciones de los diferentes tratamientos
     * @param tipoLesion              El tipo de lesión del animal
     * @return Devuelve un Animal
     */
    public static Animal rebuildFromData(int id, String tipo, int peso, LocalDate fechaEntrada, LocalDate fechaSalida,
                                         String especie, String estado, String tipoLesion, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        switch (tipo) {
            case "Ave" -> {
                return new Ave(id, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, fechasTratamientos, descripcionTratamientos);
            }
            case "Mamifero" -> {
                return new Mamifero(id, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, fechasTratamientos, descripcionTratamientos);
            }
            case "Reptil" -> {
                return new Reptil(id, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, fechasTratamientos, descripcionTratamientos);
            }
        }

        return null;
    }

    /**
     * Este tipo representa los 3 posibles estados del animal
     */
    enum Estado {
        Liberado, Tratamiento, Fallecido
    }

    /**
     * @return La id del animal
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return El peso
     */
    public int getPeso() {
        return this.peso;
    }

    /**
     * @return La fecha de entrada como String (formateada)
     */
    public String getFechaEntrada() {
        return this.fechaEntrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * @return La fecha de salida como String (formateada)
     */
    public String getFechaSalida() {
        if (this.fechaSalida == null) return "";
        else return this.fechaSalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * @return La representación de la Especie como String
     */
    public String getEspecie() {
        return this.especie;
    }

    /**
     * @return La representación del Estado como un String
     */
    public String getEstado() {
        return this.estado.toString();
    }

    /**
     * Este método devuelve el historial de tratamientos como un array de arrays de objetos, ya que es lo que
     * la JTable toma.
     *
     * @return La representación del historial de tratamientos como un Object[][]
     */
    public Object[][] getHistorialTratamiento() {
        Object[][] tratamientos = new Object[fechasTratamientos.size()][3];

        for (int i = 0; i < tratamientos.length; i++) {
            LocalDate[] fechasArray = fechasTratamientos.get(i);
            String descripcionTratamiento = descripcionTratamientos.get(i);

            tratamientos[i] = new Object[]{fechasArray[0].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fechasArray[1].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), descripcionTratamiento, (fechasArray[1].isBefore(LocalDate.now()) || fechasArray[1].isEqual(LocalDate.now()))};
        }

        return tratamientos;
    }

    /**
     * Este método añade un nuevo tratamiento al historial de tratamientos del animal. Toma sólo la fecha de fin ya que
     * la fecha de inicio se presupone que es en el mismo momento en que se añade el nuevo tratamiento
     *
     * @param tratamiento La descripción del tratamiento
     * @param fechaFin    La fecha de final del tratamiento
     */
    public void addTratamiento(String tratamiento, LocalDate fechaFin) {
        fechasTratamientos.add(new LocalDate[]{LocalDate.now(), fechaFin});
        descripcionTratamientos.add(tratamiento);
    }

    /**
     * Cambia el estado del animal a fallecido, siempre y cuando no haya fallecido o sido liberado ya
     *
     * @return Devuelve si la operación tuvo éxito o no
     */
    public boolean bajaAnimal() {
        if (this.estado == Estado.Fallecido || this.estado == Estado.Liberado) return false;
        this.estado = Estado.Fallecido;
        this.fechaSalida = LocalDate.now();
        return true;
    }


    /**
     * Cambia el estado del animal a liberado, siempre y cuando no haya fallecido o sido liberado ya
     *
     * @return Devuelve si la operación tuvo éxito o no
     */
    public boolean liberacionAnimal() {
        if (this.estado == Estado.Fallecido || this.estado == Estado.Liberado) return false;
        this.estado = Estado.Liberado;
        this.fechaSalida = LocalDate.now();
        return true;
    }

    /**
     * Este método devuelve la representación del animal como JSON. Podría haberse utilizado la librería que
     * está importada y que se está utilizando para deserializar, pero quería escribir la función para serializar
     * a mano.
     *
     * @return Un string de JSON que representa los diferentes datos del animal
     */
    public String toJSON() {
        String plantilla = """
                {
                    "id":%d,
                    "tipo":"%s",
                    "peso":%d,
                    "fechaEntrada":"%s",
                    "fechaSalida":"%s",
                    "especie":"%s",
                    "estado":"%s",
                    "tipoLesion": "%s",
                    "fechasTratamientos":[%s],
                    "descripcionTratamientos":[%s]
                },""";

        ArrayList<LocalDate[]> tratamientos = this.fechasTratamientos;
        StringBuilder fechasT = new StringBuilder();
        for (LocalDate[] arr : tratamientos)
            fechasT.append("[\"").append(arr[0]).append("\",\"").append(arr[1]).append("\"],");
        fechasT.deleteCharAt(fechasT.length() - 1);

        StringBuilder tratam = new StringBuilder();
        for (String t : descripcionTratamientos) {
            tratam.append("\"").append(t).append("\",");
        }
        tratam.deleteCharAt(tratam.length() - 1);

        return String.format(plantilla, this.id, this.getClass().getSimpleName(), this.peso, this.getFechaEntrada(),
                this.getFechaSalida(), this.getEspecie(), this.getEstado(), this.getTipoLesion(), fechasT, tratam);
    }

    /**
     * @return La lesión concreta para la familia del animal
     */
    public abstract String getTipoLesion();
}
