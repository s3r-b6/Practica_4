package org.example.Persistencia;

import org.example.Controlador;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Ficheros {
    static Path ruta = Path.of(System.getProperty("user.dir") + "/src/main/java/org/example/fichero.json");

    public static void guardarEstado(ArrayList<Controlador> lista) throws IOException {
        StringBuilder str = new StringBuilder();
        for (Controlador c : lista) str.append(c.toJSON()).append("\n");
        str.deleteCharAt(str.length() - 1).deleteCharAt(str.length() - 1);
        String JSONarr = String.format("[\n%s\n]%n", str);

        File fichero = new File(ruta.toUri());
        if (fichero.exists() && fichero.delete()) fichero.createNewFile();

        try (FileWriter writer = new FileWriter(fichero)) {
            writer.write(JSONarr);
        }

        leerEstado();

    }

    public static void leerEstado() throws IOException {
        String str = new String(Files.readAllBytes(ruta));
        System.out.println(str);
    }

}
