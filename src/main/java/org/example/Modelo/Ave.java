package org.example.Modelo;

import java.time.LocalDate;

public class Ave extends Animal {
    LesionAve tipoLesion;

    enum LesionAve {
        Caza,
        Otro
    }

    public Ave(String especie, int id, int peso, boolean caza) {
        super(id, peso, especie);
        this.tipoLesion = caza ? LesionAve.Caza : LesionAve.Otro;
    }

    public Ave(int id, int peso, LocalDate fechaEntrada, LocalDate fechaSalida, String especie, String estado, String tipoLesion, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        super(id, peso, fechaEntrada, fechaSalida, especie, estado, fechasTratamientos, descripcionTratamientos);
        //TODO !!!!!
        this.tipoLesion = tipoLesion.equals("infeccion") ? Reptil.LesionReptil.Infeccion : Reptil.LesionReptil.Otro;
    }

    @Override
    public String getTipoLesion() {
        return tipoLesion.toString();
    }
}