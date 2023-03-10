package org.example.Modelo;

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
}
