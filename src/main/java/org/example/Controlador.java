package org.example;

import org.example.Modelo.Animal;
import org.example.Persistencia.GestionDatos;

import java.time.LocalDate;

/**
 * El controlador liga el modelo de datos con la vista. Los modelos son
 * esencialmente animales únicos, con sus
 * propios datos; la vista, es la representación en un JPanel de esos datos.
 * Los cambios en los datos se realizan a través del Controlador, que llama a
 * los métodos del Modelo, para
 * modificar los datos, y, que se reflejan en la vista.
 */
public class Controlador {
    Animal animal;
    Vista vista;

    /**
     * El constructor toma un Animal y crea una vista para él a partir de sus datos.
     *
     * @param modelo Un Animal
     */
    Controlador(Animal modelo) {
        this.animal = modelo;
        this.vista = construirVista();
    }

    private Vista construirVista() {
        return new Vista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                animal.getFechaEntradaFormateada(), animal.getFechaSalidaFormateada(), animal.getEstado(), animal.getPeso(),
                animal.getHistorialTratamiento(), animal.getTipoLesion(), animal.getGravedad());
    }

    /**
     * @return La representación del animal
     */
    public Vista getVista() {
        return this.vista;
    }

    /**
     * Este método envía al modelo de datos los datos necesarios para crear un nuevo
     * tratamiento y crea
     * una nueva vista para el animal
     *
     * @param tratamiento La descripción del nuevo tratamiento
     * @param fechaFin    La fecha de final del tratamiento
     */
    public void nuevoTratamientoControlador(String tratamiento, LocalDate fechaFin) {
        animal.addTratamiento(tratamiento, fechaFin);
        LocalDate fechaInicio = LocalDate.now();
        GestionDatos.insertTratamiento(animal.getId(), fechaInicio.toString(), fechaFin.toString(), tratamiento);
        this.vista = construirVista();
    }

    /**
     * Envía la señal al modelo de datos para que se actualice el estado. Se crea
     * una nueva vista.
     */
    public void liberacionControlador() {
        if (animal.liberacionAnimal()) {
            GestionDatos.updateEstado(animal.getId(), animal.getEstado());
            this.vista = new Vista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                    animal.getFechaEntrada(), animal.getFechaSalida(), animal.getEstado(), animal.getPeso(),
                    animal.getHistorialTratamiento(), animal.getTipoLesion(), animal.getGravedad());
        }
    }

    /**
     * Envía la señal al modelo de datos para que se actualice el estado. Se crea
     * una nueva vista.
     */
    public void bajaControlador() {
        if (animal.bajaAnimal()) {
            GestionDatos.updateEstado(animal.getId(), animal.getEstado());
            this.vista = new Vista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                    animal.getFechaEntrada(), animal.getFechaSalida(), animal.getEstado(), animal.getPeso(),
                    animal.getHistorialTratamiento(), animal.getTipoLesion(), animal.getGravedad());
        }
    }

    /**
     * Envía la señal al modelo de datos para que se actualice la gravedad. Se crea
     * una nueva vista.
     */
    public void aumentarGravedadCont() {
        if (animal.aumentarGravedad()) {
            GestionDatos.updateGravedad(animal.getId(), animal.getGravedad());
            this.vista = new Vista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                    animal.getFechaEntrada(), animal.getFechaSalida(), animal.getEstado(), animal.getPeso(),
                    animal.getHistorialTratamiento(), animal.getTipoLesion(), animal.getGravedad());
        }
    }

    /**
     * Envía la señal al modelo de datos para que se actualice la gravedad. Se crea
     * una nueva vista.
     */
    public void dismGravedadCont() {
        if (animal.disminuirGravedad()) {
            GestionDatos.updateGravedad(animal.getId(), animal.getGravedad());
            this.vista = new Vista(animal.getClass().getSimpleName(), animal.getId(), animal.getEspecie(),
                    animal.getFechaEntrada(), animal.getFechaSalida(), animal.getEstado(), animal.getPeso(),
                    animal.getHistorialTratamiento(), animal.getTipoLesion(), animal.getGravedad());
        }
    }

    /**
     * @return El estado del animal
     */
    public String getEstado() {
        return this.animal.getEstado();
    }

    /**
     * @return La vista del animal
     */
    public String getFamilia() {
        return this.vista.getFamilia();
    }
}
