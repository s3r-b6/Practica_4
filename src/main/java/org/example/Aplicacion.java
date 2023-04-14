package org.example;

import org.example.Componentes.VentanaAlta;
import org.example.Componentes.VentanaAnimal;
import org.example.Componentes.VentanaAnimales;
import org.example.Componentes.VentanaTratamiento;
import org.example.Modelo.Animal;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.Persistencia.Ficheros.reconstruirLista;


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

    //TODO: Javadoc, iconos, imágenes

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

    public static void main(String[] args) throws IOException {
        rebuildLista();
        cargarPanelAnimales(new VentanaAnimales(lista));
    }

    public static void rebuildLista() throws IOException {
        ArrayList<Animal> listaAnimales = reconstruirLista();
        ArrayList<Controlador> listaControlador = new ArrayList<>();
        listaAnimales.forEach(animal -> listaControlador.add(new Controlador(animal)));
        lista = listaControlador;
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


}
