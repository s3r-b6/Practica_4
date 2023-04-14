package org.example.Modelo;

import java.time.LocalDate;

public class Reptil extends Animal {
    LesionReptil tipoLesion;

    enum LesionReptil {
        Infeccion,
        Otro
    }

    public Reptil(String especie, int id, int peso, boolean infeccion) {
        super(id, peso, especie);
        this.tipoLesion = infeccion ? LesionReptil.Infeccion : LesionReptil.Otro;
    }

    public Reptil(int id, int peso, LocalDate fechaEntrada, LocalDate fechaSalida, String especie, String estado, String tipoLesion, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        super(id, peso, fechaEntrada, fechaSalida, especie, estado, fechasTratamientos, descripcionTratamientos);
        this.tipoLesion = tipoLesion.equals("infeccion") ? LesionReptil.Infeccion : LesionReptil.Otro;
    }

    @Override
    public String getTipoLesion() {
        return tipoLesion.toString();
    }
}
