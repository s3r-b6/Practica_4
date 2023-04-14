package org.example.Persistencia;

import org.example.Controlador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


    public static void guardarEstado(ArrayList<Controlador> lista) throws IOException {
        StringBuilder str = new StringBuilder();
        for (Controlador c : lista) str.append(c.toJSON()).append("\n");
        str.deleteCharAt(str.length() - 1).deleteCharAt(str.length() - 1);
        String JSONarr = String.format("[\n%s\n]%n", str);

        File fichero = new File(System.getProperty("user.dir") + "/src/main/java/org/example/fichero.json");
        if (fichero.exists() && fichero.delete()) fichero.createNewFile();

        try (FileWriter writer = new FileWriter(fichero)) {
            writer.write(JSONarr);
        }
    }

    public static void leerEstado() {

    }

}
