package org.example.Modelo;

import java.util.Date;
import java.util.HashMap;

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
}
