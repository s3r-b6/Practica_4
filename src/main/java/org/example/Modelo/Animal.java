package org.example.Modelo;

import java.util.Date;
import java.util.HashMap;

public abstract class Animal {
    int id;
    int peso;
    Date fechaEntrada;
    String especie;
    Estado estado;
    HashMap<Date, String> historialTratamiento;

    public Animal(int id, int peso, String especie) {
        this.id = id;
        this.peso = peso;
        this.fechaEntrada = new Date();
        this.especie = especie;
        this.estado = Estado.Tratamiento;
        this.historialTratamiento = new HashMap<>();
    }

    enum Estado {
        Liberado,
        Tratamiento,
        Fallecido
    }

    public int getId() {
        return this.id;
    }

    public int getPeso() {
        return this.peso;
    }

    public Date getFechaEntrada() {
        return this.fechaEntrada;
    }

    public String getEspecie() {
        return this.especie;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public HashMap<Date, String> getHistorialTratamiento() {
        return this.historialTratamiento;
    }

    public void tratamientoAnimal(String tratamiento) {
        this.historialTratamiento.put(new Date(), tratamiento);
    }


    public void bajaAnimal() {
        this.estado = Estado.Fallecido;
    }


    public void liberacionAnimal() {
        this.estado = Estado.Liberado;
    }
}
