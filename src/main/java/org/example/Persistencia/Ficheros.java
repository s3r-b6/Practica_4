package org.example.Persistencia;

import org.example.Controlador;

import java.util.ArrayList;

public class Ficheros {
/*
    int id;
    int peso;
    LocalDate fechaEntrada;
    LocalDate fechaSalida;
    String especie;
    Estado estado;
    ArrayList<LocalDate[]> fechasTratamientos;
    ArrayList<String> descripcionTratamientos;
 */

//fecha1 fecha2 descripcion -> String[][]


    public static void guardarEstado(ArrayList<Controlador> lista) {
        StringBuilder str = new StringBuilder();
        for (Controlador c : lista) str.append(c.toJSON());

        str.deleteCharAt(str.length() - 1);

        System.out.println(str);

    }

    public static void leerEstado() {

    }

}
