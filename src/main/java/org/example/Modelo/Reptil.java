package org.example.Modelo;

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

    @Override
    public String getTipoLesion() {
        return tipoLesion.toString();
    }
}
