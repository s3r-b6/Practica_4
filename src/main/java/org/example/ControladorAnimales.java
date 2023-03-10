package org.example;


import org.example.Modelo.Animal;

public class ControladorAnimales {
    Animal animal;
    VistaAnimales vista;


    //la cosa sería cambiar el modelo desde aquí, usando la interfaz del modelo y, llamar a las
    //updates de la vista desde aquí.
    ControladorAnimales(Animal modelo) {
        this.animal = modelo;
        this.vista = new VistaAnimales();
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
