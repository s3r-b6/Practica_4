package org.example.Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class Animal {
    int id;
    int peso;
    LocalDate fechaEntrada;
    String especie;
    Estado estado;
    ArrayList<LocalDate[]> fechasTratamientos;
    ArrayList<String> descripcionTratamientos;

    public Animal(int id, int peso, String especie) {
        this.id = id;
        this.peso = peso;
        this.fechaEntrada = LocalDate.now();
        this.especie = especie;
        this.estado = Estado.Tratamiento;
        this.fechasTratamientos = new ArrayList<>();
        this.descripcionTratamientos = new ArrayList<>();
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

    public String getFechaEntrada() {
        return this.fechaEntrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getEspecie() {
        return this.especie;
    }

    public String getEstado() {
        return this.estado.toString();
    }

    public String[][] getHistorialTratamiento() {
        String[][] tratamientos = new String[fechasTratamientos.size()][3];

        for (int i = 0; i < tratamientos.length; i++) {
            LocalDate[] fechasArray = fechasTratamientos.get(i);
            String descripcionTratamiento = descripcionTratamientos.get(i);

            tratamientos[i] = new String[]{
                    fechasArray[0].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    fechasArray[1].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    descripcionTratamiento
            };
        }

        return tratamientos;
    }

    public void tratamientoAnimal(String tratamiento, LocalDate fechaFin) {
        fechasTratamientos.add(new LocalDate[]{LocalDate.now(), fechaFin});
        descripcionTratamientos.add(tratamiento);
    }


    public boolean bajaAnimal() {
        if (this.estado == Estado.Fallecido) return false;
        this.estado = Estado.Fallecido;
        return true;
    }


    public boolean liberacionAnimal() {
        if (this.estado == Estado.Fallecido) return false;
        this.estado = Estado.Liberado;
        return true;
    }

    public abstract String getTipoLesion();
}
