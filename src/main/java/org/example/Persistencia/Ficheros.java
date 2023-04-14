package org.example.Persistencia;

import org.example.Controlador;
import org.example.Modelo.Animal;
import org.json.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Animal> reconstruirLista() throws JSONException, IOException {
        String jsonString = new String(Files.readAllBytes(ruta));
        List<Animal> datosAnimales = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            int id = jsonObject.getInt("id");
            String tipo = jsonObject.getString("tipo");
            int peso = jsonObject.getInt("peso");
            LocalDate fechaEntrada = LocalDate.parse(jsonObject.getString("fechaEntrada"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate fechaSalida = LocalDate.parse(jsonObject.optString("fechaSalida"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String especie = jsonObject.getString("especie");
            String tipoLesion = jsonObject.getString("tipoLesion");
            String estado = jsonObject.getString("estado");

            JSONArray fechasTratamientosArray = jsonObject.getJSONArray("fechasTratamientos");
            LocalDate[][] fechasTratamientos = new LocalDate[fechasTratamientosArray.length()][2];
            for (int j = 0; j < fechasTratamientosArray.length(); j++) {
                JSONArray fechasTratamiento = fechasTratamientosArray.getJSONArray(j);
                LocalDate[] fechas = new LocalDate[2];
                fechas[0] = LocalDate.parse(fechasTratamiento.getString(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                fechas[1] = LocalDate.parse(fechasTratamiento.getString(1), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                fechasTratamientos[j] = fechas;
            }

            JSONArray descripcionTratamientosArray = jsonObject.getJSONArray("descripcionTratamientos");
            String[] descripcionTratamientos = new String[descripcionTratamientosArray.length()];
            for (int j = 0; j < descripcionTratamientosArray.length(); j++) {
                String descripcionTratamiento = descripcionTratamientosArray.getString(j);
                descripcionTratamientos[j] = descripcionTratamiento;
            }

            datosAnimales.add(Animal.rebuildFromData(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, fechasTratamientos, descripcionTratamientos));
        }

        return datosAnimales;
    }


    public static void leerEstado() throws IOException {
        String str = new String(Files.readAllBytes(ruta));
        System.out.println(str);
    }

}
