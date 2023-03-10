package org.example.Modelo;

import java.util.Date;
import java.util.HashMap;

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
}