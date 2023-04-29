package org.example.Persistencia;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.example.Aplicacion;
import org.example.Modelo.Animal;

import static org.example.Persistencia.Conexion.poblarHashMaps;

public class GestionDatos {

    static final String INSERT_ANIMAL_SIN_FECHA = """
            INSERT INTO
            animales(id, tipo_familia, peso, fecha_entrada, fecha_salida, especie, tipo_estado, tipo_lesion, tipo_gravedad)
            VALUES(%d,   %d,           %d,   '%s',          %s,           '%s',    '%s',         %s,         %d);""";
    static final String INSERT_ANIMAL_CON_FECHA = """
            INSERT INTO
            animales(id, tipo_familia, peso, fecha_entrada, fecha_salida, especie, tipo_estado, tipo_lesion, tipo_gravedad)
            VALUES(%d,   %d,           %d,   '%s',          '%s',           '%s',    '%s',         %s,         %d);""";

    static final String INSERT_TRATAM = """
            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin,
            descripcion) VALUES (%s, %s, %s, %s)""";

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
        try (Connection c = Conexion.getConnection()) {
            Statement st = c.createStatement();
            int id_estado = Aplicacion.estados.get(estado);
            st.executeUpdate(String.format("UPDATE animales SET estado = %d WHERE id = %d", id_animal, id_estado));
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query");
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static void insertarUpdate(String query, int id, String familia, int peso, LocalDate fechaEntrada,
                                      LocalDate fechaSalida, String especie, String estado, boolean tipoLesion, String gravedad) {
        try (Connection c = Conexion.getConnection()) {
            Statement st = c.createStatement();
            switch (query) {
                case "i_animal" -> {
                    String q;
                    if (fechaSalida == null) {
                        q = String.format(INSERT_ANIMAL_SIN_FECHA, id, Aplicacion.familias.get(familia),
                                peso, fechaEntrada, "NULL", especie, Aplicacion.estados.get(estado),
                                tipoLesion ? "TRUE" : "FALSE", Aplicacion.gravedades.get(gravedad));
                    } else {
                        q = String.format(INSERT_ANIMAL_CON_FECHA, id, Aplicacion.familias.get(familia),
                                peso, fechaEntrada.toString(), fechaSalida.toString(), especie,
                                Aplicacion.estados.get(estado), tipoLesion ? "TRUE" : "FALSE",
                                Aplicacion.gravedades.get(gravedad));
                    }
                    st.executeQuery(q);
                }
                case "i_tratam" -> {
                    st.executeQuery(
                            String.format(INSERT_TRATAM, id, Aplicacion.familias.get(familia), peso, fechaEntrada,
                                    fechaSalida, especie, Aplicacion.estados.get(estado), tipoLesion,
                                    Aplicacion.gravedades.get(gravedad))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet ejecutarQuery(String query, String filtro) {
        try (Connection c = Conexion.getConnection()) {
            Statement st = c.createStatement();
            ResultSet rs = null;

            switch (query) {
                case "q_familia" -> {
                    rs = st.executeQuery(String.format(QUERY_FAMILIA, filtro));
                }
                case "q_estado" -> {
                    rs = st.executeQuery(String.format(QUERY_ESTADO, filtro));
                }
                case "q_tratam" -> {
                    rs = st.executeQuery(String.format(QUERY_TRATAM, filtro));
                }
                default -> throw new RuntimeException("Mala query");
            }

            return rs;
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query");
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static ArrayList<Animal> buildListaFromResultSet() {
        ArrayList<Animal> animalList = new ArrayList<>();
        try (Connection c = Conexion.getConnection()) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM animales;");
            while (rs.next()) {
                int id = rs.getInt(1);
                String familia = Aplicacion.familias_id.get(rs.getInt(2));
                String estado = Aplicacion.estados_id.get(rs.getInt(3));
                String gravedad = Aplicacion.gravedades_id.get(rs.getInt(4));
                boolean tipoLesion = rs.getBoolean(5);
                int peso = rs.getInt(6);
                String especie = rs.getString(7);
                String fechaEntrada = rs.getString(8);
                String fechaSalida = rs.getString(9);

                ArrayList<LocalDate[]> fechas = new ArrayList<>();
                ArrayList<String> descripciones = new ArrayList<>();

                String queryTratam = String.format("SELECT * FROM tratamientos WHERE id_animal='%d'", id);
                ResultSet rs2 = c.createStatement().executeQuery(queryTratam);
                LocalDate fechaEnt = LocalDate.parse(fechaEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate fechaSal = null;
                if (fechaSalida != null) {
                    fechaSal = LocalDate.parse(fechaEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }

                while (rs2.next()) {
                    fechas.add(new LocalDate[]{
                            LocalDate.parse(rs2.getString(3), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            LocalDate.parse(rs2.getString(4), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    });
                    descripciones.add(rs2.getString(5));
                }

                Animal a = Animal.rebuildFromData(id, familia, peso, fechaEnt, fechaSal, especie, estado,
                        tipoLesion, fechas, descripciones, gravedad);

                animalList.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query " + e.getCause());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return animalList;
    }

//    public static ArrayList<Animal> buildListaFromResultSet(ResultSet rs, Statement st) throws SQLException {
//
//    }
}
