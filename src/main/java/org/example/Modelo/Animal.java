package org.example.Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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


    public String toJSON() {
        String template = """
                {
                    'id': %d,
                    'peso': %d,
                    'fechaEntrada':'%s',
                    'fechaSalida':'%s',
                    'especie':'%s',
                    'estado':'%s',
                    'fechasTratamientos':[%s],
                    'descripcionTratamientos':[%s]
                },""";

        ArrayList<LocalDate[]> tratamientos = this.fechasTratamientos;
        StringBuilder fechasT = new StringBuilder();
        for (int i = 0; i < tratamientos.size(); i++) {
            LocalDate[] arr = tratamientos.get(i);
            fechasT.append("['").append(arr[0]).append("','").append(arr[1]).append("'],");
        }
        fechasT.deleteCharAt(fechasT.length() - 1);

        StringBuilder tratam = new StringBuilder();
        for (String t : descripcionTratamientos) {
            tratam.append("'").append(t).append("',");
        }
        tratam.deleteCharAt(tratam.length() - 1);

        return String.format(template, this.id, this.peso, this.getFechaEntrada(), this.getFechaSalida(), this.getEspecie(), this.getEstado(), fechasT, tratam);
    }

    public abstract String getTipoLesion();
}
