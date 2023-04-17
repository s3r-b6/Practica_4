package org.example.Modelo;

import java.time.LocalDate;

public class Reptil extends Animal {
    LesionReptil tipoLesion;

    /**
     * Este tipo contiene los tipos de lesiones que puede tener un animal
     */
    enum LesionReptil {
        Infeccion,
        Otro
    }

    /**
     * Constructor usado para crear un animal nuevo
     *
     * @param id         La id
     * @param peso       El peso
     * @param especie    La especie del animal
     * @param tipoLesion El tipo de lesión del animal
     */
    public Reptil(String especie, int id, int peso, boolean tipoLesion) {
        super(id, peso, especie);
        this.tipoLesion = tipoLesion ? LesionReptil.Infeccion : LesionReptil.Otro;
    }

    /**
     * Constructor usado para recrear el animal a partir de los datos
     *
     * @param id                      La id
     * @param peso                    El peso
     * @param fechaEntrada            La fecha de entrada del animal
     * @param fechaSalida             La fecha de salida del animal
     * @param especie                 La especie del animal
     * @param estado                  El estado del animal
     * @param fechasTratamientos      Array de arrays. Las fechas de inicio[x][0] y fin[x][1] de los tratamientos
     * @param descripcionTratamientos Las descripciones de los diferentes tratamientos
     * @param tipoLesion              El tipo de lesión del animal
     */
    public Reptil(int id, int peso, LocalDate fechaEntrada, LocalDate fechaSalida, String especie, String estado, String tipoLesion, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        super(id, peso, fechaEntrada, fechaSalida, especie, estado, fechasTratamientos, descripcionTratamientos);
        this.tipoLesion = tipoLesion.equals("infeccion") ? LesionReptil.Infeccion : LesionReptil.Otro;
    }

    /**
     * @return El tipo de lesión del Reptil
     */
    @Override
    public String getTipoLesion() {
        return tipoLesion.toString();
    }
}
