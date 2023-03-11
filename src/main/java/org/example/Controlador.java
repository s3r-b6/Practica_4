package org.example;


import org.example.Modelo.Animal;

/**
 * El controlador  liga el modelo de datos con la vista. Los modelos son esencialmente animales únicos, con sus
 * propios datos, y, la vista, es un Componente Swing que representa esos datos. Las updates a los datos se realizan
 * a través del Controlador, que llama a los métodos del Modelo, para modificar los datos, y, al método de la
 * vista, para actualizar la representación de los datos.
 */
public class Controlador {
    Animal animal;
    Vista vista;


    //la cosa sería cambiar el modelo desde aquí, usando la interfaz del modelo y, llamar a las
    //updates de la vista desde aquí.
    Controlador(Animal modelo) {
        this.animal = modelo;
        this.vista = new Vista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                animal.getFechaEntrada(), animal.getEstado() + "", animal.getPeso(), animal.getHistorialTratamiento());
    }

    public void tratamientoControlador(String tratamiento) {
        animal.tratamientoAnimal(tratamiento);
        vista.actualizarVista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                animal.getFechaEntrada(), animal.getEstado() + "", animal.getPeso(), animal.getHistorialTratamiento());
    }

    public void liberacionControlador() {
        animal.liberacionAnimal();
        vista.actualizarVista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                animal.getFechaEntrada(), animal.getEstado() + "", animal.getPeso(), animal.getHistorialTratamiento());
    }

    public void bajaControlador() {
        animal.bajaAnimal();
        vista.actualizarVista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                animal.getFechaEntrada(), animal.getEstado() + "", animal.getPeso(), animal.getHistorialTratamiento());
    }
}
