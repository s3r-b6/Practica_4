package org.example.Modelo;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public abstract class Animal {
    int id;
    int peso;
    LocalDate fechaEntrada;
    String especie;
    Estado estado;
    HashMap<Date, String> historialTratamiento;

    public Animal(int id, int peso, String especie) {
        this.id = id;
        this.peso = peso;
        this.fechaEntrada = LocalDate.now();
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

    public LocalDate getFechaEntrada() {
        return this.fechaEntrada;
    }

    public String getEspecie() {
        return this.especie;
    }

    public String getEstado() {
        return this.estado.toString();
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

    public abstract String getTipoLesion();
}
