package org.example;

import org.example.Componentes.VentanaAlta;
import org.example.Componentes.VentanaAnimal;
import org.example.Componentes.VentanaAnimales;
import org.example.Componentes.VentanaTratamiento;
import org.example.Modelo.Animal;
import org.example.Modelo.Ave;
import org.example.Modelo.Mamifero;
import org.example.Modelo.Reptil;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Esta clase contiene el main() y también contiene el punto de entrada a la interfaz, i.e., la ventana sobre
 * la que se insertan los animales.
 * En el patrón de diseño MVC (Modelo-Vista-Controlador), la lógica de negocio generalmente se maneja en el
 * controlador y/o en el modelo, mientras que la vista solo muestra los datos al usuario. La lógica de negocio
 * se refiere a las reglas y procesos que rigen cómo funciona una aplicación o sistema. Por ejemplo, en una
 * aplicación bancaria, la lógica de negocio podría incluir reglas sobre cómo se calculan los intereses o cómo
 * se realizan las transferencias entre cuentas. En el patrón de diseño MVC (Modelo-Vista-Controlador), la lógica
 * de negocio generalmente se maneja en el controlador y/o en el modelo, mientras que la vista solo muestra
 * los datos al usuario.
 */
public class Aplicacion {

    /*
    TODO: El adaptador es muy molesto. Mejorar estética y experiencia un poco. Después centrarse en buscar bugs.
     Mejorar cómo se ven los tratamientos. Cambiar fotos.
     */

    static JFrame ventanaPrincipal;
    static JFrame ventanaDetalle;
    static JFrame ventanaAlta;
    static JFrame ventanaTratamiento;
    /**
     * La lista de animales contiene los controladores para cada uno de los animales.
     * La interfaz gráfica se realiza creando un JFrame y añadiéndole a ese JFrame cada una de las vistas del controlador.
     * Desde la interfaz, se pueden enviar señales al controlador para que modifique los  datos del modelo y
     * justo después actualice la representación de los datos.
     */
    static ArrayList<Controlador> lista = new ArrayList<>();

    public static void main(String[] args) {
        cargarMockData();
        cargarPanelAnimales(new VentanaAnimales(lista));
    }

    public static void cargarPanelAnimales(VentanaAnimales v) {
        if (ventanaPrincipal != null) ventanaPrincipal.dispose();
        ventanaPrincipal = v;
    }

    public static void cargarPanelAnimal(VentanaAnimal v) {
        if (ventanaDetalle != null) ventanaDetalle.dispose();
        ventanaDetalle = v;
    }

    public static void cargarMenuAlta(VentanaAlta v) {
        if (ventanaAlta != null) ventanaAlta.dispose();
        ventanaAlta = v;
    }

    public static void cargarPanelTratamiento(VentanaTratamiento v) {
        if (ventanaTratamiento != null) ventanaTratamiento.dispose();
        ventanaTratamiento = v;
    }

    public static void recargarVistas(Controlador c) {
        if (ventanaPrincipal != null) ventanaPrincipal.dispose();
        if (ventanaDetalle != null) ventanaDetalle.dispose();
        cargarPanelAnimales(new VentanaAnimales(lista));
        cargarPanelAnimal(new VentanaAnimal(c));
    }

    public static void addAnimal(Animal a) {
        lista.add(new Controlador(a));
        ventanaPrincipal.setVisible(false);
        cargarPanelAnimales(new VentanaAnimales(lista));
    }

    private static void cargarMockData() {
        lista.add(new Controlador(new Reptil("Lagarto", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Mamifero("Perro", lista.size() + 1, 15, true)));
        lista.add(new Controlador(new Ave("Jilguero", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Reptil("Cocodrilo", lista.size() + 1, 18, true)));
        lista.add(new Controlador(new Mamifero("Ciervo", lista.size() + 1, 25, true)));
        lista.add(new Controlador(new Ave("Cigüeña", lista.size() + 1, 7, true)));
        lista.add(new Controlador(new Reptil("Salamandra", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Mamifero("Gato", lista.size() + 1, 4, true)));
        lista.add(new Controlador(new Ave("Paloma", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Reptil("Lagarto", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Mamifero("Perro", lista.size() + 1, 15, true)));
        lista.add(new Controlador(new Ave("Jilguero", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Reptil("Cocodrilo", lista.size() + 1, 18, true)));
        lista.add(new Controlador(new Mamifero("Ciervo", lista.size() + 1, 25, true)));
        lista.add(new Controlador(new Ave("Cigüeña", lista.size() + 1, 7, true)));
        lista.add(new Controlador(new Reptil("Salamandra", lista.size() + 1, 1, true)));
        lista.add(new Controlador(new Mamifero("Gato", lista.size() + 1, 4, true)));
        lista.add(new Controlador(new Ave("Paloma", lista.size() + 1, 1, true)));
        //Cargar algunos tratamientos de prueba
        lista.forEach(e -> {
            int rand1 = (int) ((Math.random() * 3) + 0);
            if (rand1 >= 0) e.nuevoTratamientoControlador("Fisioterapia", getFechaAleatoria());
            if (rand1 >= 1) e.nuevoTratamientoControlador("Medicina B", getFechaAleatoria());
            if (rand1 >= 2) e.nuevoTratamientoControlador("Pastillas C", getFechaAleatoria());
        });
    }

    private static LocalDate getFechaAleatoria() {
        int rand2 = (int) ((Math.random() * (12 - 1)) + 1);
        StringBuilder fecha = new StringBuilder("2023-");
        if (rand2 < 10) fecha.append("0").append(rand2).append("-01");
        else fecha.append(rand2).append("-01");
        return LocalDate.parse(fecha);
    }
}
