package org.example.Modelo;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class Mamifero extends Animal {
    LesionMamifero tipoLesion;

    enum LesionMamifero {
        Atropello,
        Otro
    }

    public Mamifero(String especie, int id, int peso, boolean atropello) {
        super(id, peso, especie);
        this.tipoLesion = atropello ? LesionMamifero.Atropello : LesionMamifero.Otro;
    }

    public Mamifero(int id, int peso, LocalDate fechaEntrada, LocalDate fechaSalida, String especie, String estado, String tipoLesion, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        super(id, peso, fechaEntrada, fechaSalida, especie, estado, fechasTratamientos, descripcionTratamientos);
        //TODO !!!!!
        this.tipoLesion = tipoLesion.equals("infeccion") ? Reptil.LesionReptil.Infeccion : Reptil.LesionReptil.Otro;
    }

    @Override
    public String getTipoLesion() {
        return tipoLesion.toString();
    }
}
