package org.example.Persistencia;

import java.sql.*;
import org.example.Aplicacion;

public class GestionDatos {

    static final String INSERT_ANIMAL = """
            INSERT INTO animales(id, !!!tipo!!!, peso, fechaEntrada,
            fechaSalida, especie, !!!estado!!!, tipoLesion, gravedad)
            VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');
                """;
    static final String INSERT_TRATAM = """
            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin,
            descripcion) VALUES (%s, %s, %s, %s) """;

    static final String QUERY_FAMILIA = """
            SELECT * FROM animales WHERE tipo = (
                SELECT id FROM familias WHERE nombre = '%s'
            );
            """;

    static final String QUERY_ESTADO = """
            SELECT * FROM animales WHERE estado = (
                SELECT id FROM estados WHERE nombre = '%s'
            );
            """;
    static final String QUERY_TRATAM = """
            SELECT * FROM tratamientos WHERE id_animal = '%s';
            """;

    public static void updateAnimal(int id_animal, String estado) {
        // Sólo pueden cambiarse los estados
        Connection c = Conexion.getConnection();
        Statement st;
        try {
            int id_estado = Aplicacion.estados.get(estado);
            st = c.createStatement();
            st.executeUpdate(String.format("UPDATE animales SET estado = %d WHERE id = %d", id_animal, id_estado));
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query");
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    public static ResultSet ejecutarQuery(String query, String filtro) {
        Connection c = Conexion.getConnection();
        Statement st;
        try {
            st = c.createStatement();
            ResultSet rs = null;

            switch (query) {
                case "i_animal" -> {
                    rs = st.executeQuery(String.format(INSERT_ANIMAL, filtro));
                }
                case "i_tratam" -> {
                    rs = st.executeQuery(String.format(INSERT_TRATAM, filtro));
                }
                case "q_familia" -> {
                    rs = st.executeQuery(String.format(QUERY_FAMILIA, filtro));
                }
                case "q_estado" -> {
                    rs = st.executeQuery(String.format(QUERY_ESTADO, filtro));
                }
                case "q_tratam" -> {
                    rs = st.executeQuery(String.format(QUERY_TRATAM, filtro));
                }
                case "q_all" -> {
                    rs = st.executeQuery("SELECT * FROM animales;");
                }
                default -> throw new RuntimeException("Mala query");
            }

            return rs;
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query");
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
