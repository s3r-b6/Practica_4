package org.example.Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Animal {
    int id;
    int peso;
    LocalDate fechaEntrada;
    LocalDate fechaSalida;
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

    public Animal(int id, int peso, LocalDate fechaEntrada, LocalDate fechaSalida, String especie, String estado, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        Estado st = null;
        switch (estado) {
            case "Liberado" -> st = Estado.Liberado;
            case "Fallecido" -> st = Estado.Fallecido;
            case "Tratamiento" -> st = Estado.Tratamiento;
        }
        this.id = id;
        this.peso = peso;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.especie = especie;
        this.estado = st;
        this.fechasTratamientos = new ArrayList<>(Arrays.asList(fechasTratamientos));
        this.descripcionTratamientos = new ArrayList<>(Arrays.asList(descripcionTratamientos));
    }

    public static Animal rebuildFromData(int id, String tipo, int peso, LocalDate fechaEntrada, LocalDate fechaSalida,
                                         String especie, String estado, String tipoLesion, LocalDate[][] fechasTratamientos, String[] descripcionTratamientos) {
        switch (tipo) {
            case "Ave" -> {
                return new Ave(id, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, fechasTratamientos, descripcionTratamientos);
            }
            case "Mamifero" -> {
                return new Mamifero(id, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, fechasTratamientos, descripcionTratamientos);
            }
            case "Reptil" -> {
                return new Reptil(id, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, fechasTratamientos, descripcionTratamientos);
            }
        }

        return null;
    }

    enum Estado {
        Liberado, Tratamiento, Fallecido
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

    public String getFechaSalida() {
        if (this.fechaSalida == null) return "";
        else return this.fechaSalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getEspecie() {
        return this.especie;
    }

    public String getEstado() {
        return this.estado.toString();
    }

    public Object[][] getHistorialTratamiento() {
        Object[][] tratamientos = new Object[fechasTratamientos.size()][3];

        for (int i = 0; i < tratamientos.length; i++) {
            LocalDate[] fechasArray = fechasTratamientos.get(i);
            String descripcionTratamiento = descripcionTratamientos.get(i);

            tratamientos[i] = new Object[]{fechasArray[0].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fechasArray[1].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), descripcionTratamiento, (fechasArray[1].isBefore(LocalDate.now()) || fechasArray[1].isEqual(LocalDate.now()))};
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
        this.fechaSalida = LocalDate.now();
        return true;
    }


    public boolean liberacionAnimal() {
        if (this.estado == Estado.Fallecido) return false;
        this.estado = Estado.Liberado;
        this.fechaSalida = LocalDate.now();
        return true;
    }

//    public Animal buildAnimal() {
//
//    }

    public String toJSON() {
        String plantilla = """
                {
                    "id":%d,
                    "tipo":"%s",
                    "peso":%d,
                    "fechaEntrada":"%s",
                    "fechaSalida":"%s",
                    "especie":"%s",
                    "estado":"%s",
                    "tipoLesion": "%s",
                    "fechasTratamientos":[%s],
                    "descripcionTratamientos":[%s]
                },""";

        ArrayList<LocalDate[]> tratamientos = this.fechasTratamientos;
        StringBuilder fechasT = new StringBuilder();
        for (LocalDate[] arr : tratamientos)
            fechasT.append("[\"").append(arr[0]).append("\",\"").append(arr[1]).append("\"],");
        fechasT.deleteCharAt(fechasT.length() - 1);

        StringBuilder tratam = new StringBuilder();
        for (String t : descripcionTratamientos) {
            tratam.append("\"").append(t).append("\",");
        }
        tratam.deleteCharAt(tratam.length() - 1);

        return String.format(plantilla, this.id, this.getClass().getSimpleName(), this.peso, this.getFechaEntrada(),
                this.getFechaSalida(), this.getEspecie(), this.getEstado(), this.getTipoLesion(), fechasT, tratam);
    }

    public abstract String getTipoLesion();
}
