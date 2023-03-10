package org.example;

import org.example.Modelo.*;

import java.util.ArrayList;

public class Aplicacion {

    static ArrayList<ControladorAnimales> lista = new ArrayList<>();

    /*
      modelo <-> controlador <-> vista

      ArrayList<Componente> c ; Componente: la vista de cada animal se actualiza con .repaint() ??¿?¿¿?¿?¿?¿?;

      vista: redibujar
     */

    public static void init() {
        lista.add(new ControladorAnimales(new Reptil("Lagarto", lista.size() + 1, 3, true)));
        lista.add(new ControladorAnimales(new Mamifero("Perro", lista.size() + 1, 3, true)));
        lista.add(new ControladorAnimales(new Ave("Jilguero", lista.size() + 1, 3, true)));
    }

    public static void main(String[] args) {
        init();
        for (ControladorAnimales e : lista) {
            if (e.animal instanceof Ave) e.bajaControlador();
            else if (e.animal instanceof Mamifero) e.tratamientoControlador("Medicina");
            else if (e.animal instanceof Reptil) e.liberacionControlador();
        }
    }

}
