package org.example;

import java.util.Date;
import java.util.HashMap;

public class VistaAnimales {

    JPanel panel;

    VistaAnimales() {
        this.panel = new JPanel();
    }

    public void drawData(String tipo, int id, String especie, Date fechaEntrada, String estado, int peso, HashMap<Date, String> historialTratamiento) {
        System.out.println("->Animal " + id + " " + tipo);
        System.out.println("  Especie: " + especie + "  Entrada: " + fechaEntrada.toString().replaceAll("\\d{2}:\\d{2}:\\d{2} .*", ""));
        System.out.println("  Estado: " + estado + "  Peso:" + peso + "kg");
        historialTratamiento.forEach((k, v) -> {
            System.out.println("  ->Fecha:" + k);
            System.out.println("  ->Tratamiento: " + v);
        });
    }

    //así, se puede llamar a updateView desde el controlador si el animal cambia
    // y no hay que resetear el panel completo devolver un JPanel que contenga el animal?
    public void actualizarVista(
}
