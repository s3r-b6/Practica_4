package org.example;


import org.example.Modelo.Animal;

import java.time.LocalDate;

/**
 * El controlador  liga el modelo de datos con la vista. Los modelos son esencialmente animales únicos, con sus
 * propios datos, y, la vista, es un Componente Swing que representa esos datos. Las updates a los datos se realizan
 * a través del Controlador, que llama a los métodos del Modelo, para modificar los datos, y, al método de la
 * vista, para actualizar la representación de los datos.
 * La representación "binaria" de los datos del animal está desligada de la representación gráfica de estos datos.
 */
public class Controlador {
    Animal animal;
    Vista vista;


    Controlador(Animal modelo) {
        this.animal = modelo;
        this.vista = new Vista();
        recrearVista();
    }

    public void tratamientoControlador(String tratamiento, LocalDate fechaFin) {
        animal.tratamientoAnimal(tratamiento, fechaFin);
        recrearVista();
    }

    public void liberacionControlador() {
        if (animal.liberacionAnimal()) {
            vista.setLabelEstado(animal.getEstado());
        }
    }

    public void bajaControlador() {
        if (animal.bajaAnimal()) recargarVistas();
    }

    public void recargarVistas() {
        vista.vistaDetalle.repaint();
        vista.vistaNormal.repaint();
    }

    private void recrearVista() {
        vista.actualizarVistas(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                animal.getFechaEntrada(), animal.getEstado(), animal.getPeso(), animal.getHistorialTratamiento(), animal.getTipoLesion());
    }
}
