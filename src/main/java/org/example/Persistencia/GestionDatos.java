package org.example.Persistencia;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.example.Aplicacion;
import org.example.Modelo.Animal;

import static org.example.Aplicacion.gravedades;
import static org.example.Aplicacion.estados;
import static org.example.Aplicacion.familias;
import static org.example.Persistencia.Conexion.*;

public class GestionDatos {

    public static final String INSERT_ANIMAL_SIN_FECHA = """
            INSERT INTO
            animales(id, tipo_familia, peso, fecha_entrada, fecha_salida, especie, tipo_estado, tipo_lesion, tipo_gravedad)
            VALUES(%d,   %d,           %d,   '%s',          %s,           '%s',    '%s',         %s,         %d);""";
    public static final String INSERT_ANIMAL_CON_FECHA = """
            INSERT INTO
            animales(id, tipo_familia, peso, fecha_entrada, fecha_salida, especie, tipo_estado, tipo_lesion, tipo_gravedad)
            VALUES(%d,   %d,           %d,   '%s',          '%s',           '%s',    '%s',         %s,         %d);""";

    static final String INSERT_TRATAM = """
            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin, descripcion) VALUES (%s, %s, %s, %s)
            """;

    static final String QUERY_FAMILIA = "SELECT * FROM animales WHERE tipo_familia = %d;";
    static final String QUERY_ESTADO = "SELECT * FROM animales WHERE tipo_estado = %d; ";
    static final String QUERY_TRATAM = "SELECT * FROM tratamientos WHERE id_animal = '%s';";
    private static final String QUERY_FAMILIA_ESTADO = """
            SELECT * FROM animales WHERE tipo_familia = %d AND tipo_estado = %d;
            """;
    private static final String UPDATE_ESTADO = "UPDATE animales SET estado = %d WHERE id = %d";
    private static final String UPDATE_GRAVEDAD = "UPDATE animales SET gravedad = %d WHERE id = %d";

    public static void updateEstado(int idAnimal, String estadoAnimal) {
        try (Connection c = getConnection()) {
            Statement st = c.createStatement();
            st.executeUpdate(String.format(UPDATE_ESTADO, estados.get(estadoAnimal), idAnimal));
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query");
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static void updateGravedad(int idAnimal, String nuevaGravedad) {
        try (Connection c = getConnection()) {
            Statement st = c.createStatement();
            st.executeUpdate(String.format(UPDATE_GRAVEDAD, gravedades.get(nuevaGravedad), idAnimal));
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query");
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static void updateAnimal(int idAnimal, String fechaInicio, String fechaFin, String descripcion) {
        try (Connection c = getConnection()) {
            Statement st = c.createStatement();
            st.executeQuery(String.format(INSERT_TRATAM, idAnimal, fechaInicio, fechaFin, descripcion));
        } catch (SQLException e) {
            System.out.println("Hubo un problema al ejecutar la query");
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static ArrayList<Animal> buildListaFromFiltros(String[] filtro) {
        try (Connection c = getConnection()) {
            Statement st = c.createStatement();

            ResultSet rs;
            // que el filtro[0] no esté vacío
            if (filtro[0].equals("")) {
                if (filtro[1].equals("")) {
                    rs = st.executeQuery("SELECT * FROM animales");
                } else
                    rs = st.executeQuery(String.format(QUERY_ESTADO, estados.get(filtro[1])));
            } else if (filtro[1].equals(""))
                rs = st.executeQuery(String.format(QUERY_FAMILIA, familias.get(filtro[0])));
            else {
                rs = st.executeQuery(
                        String.format(QUERY_FAMILIA_ESTADO, familias.get(filtro[0]), estados.get(filtro[1])));
            }

            return buildListaFromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Animal> buildListaFromResultSet() {
        ArrayList<Animal> animalList = new ArrayList<>();
        try (Connection c = getConnection()) {
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

                String queryTratam = String.format(QUERY_TRATAM, id);
                ResultSet rs2 = c.createStatement().executeQuery(queryTratam);
                LocalDate fechaEnt = LocalDate.parse(fechaEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate fechaSal = null;

                if (fechaSalida != null) {
                    fechaSal = LocalDate.parse(fechaEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }

                while (rs2.next()) {
                    fechas.add(new LocalDate[] {
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

    public static void insertarAnimal(Animal a) {
        try (Connection c = Conexion.getConnection()) {
            Statement st = c.createStatement();
            st.executeUpdate(a.getInsert());
        } catch (Exception e) {
            System.out.println("Hubo un problema al ejecutar la query " + e.getCause());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static void insertTratamiento(int idAnimal, String fechaInicio, String fechaFin, String descripcion) {
        try (Connection c = Conexion.getConnection()) {
            Statement st = c.createStatement();
            st.executeUpdate(String.format(INSERT_TRATAM, idAnimal, fechaInicio, fechaFin, descripcion));
        } catch (Exception e) {
            System.out.println("Hubo un problema al ejecutar la query " + e.getCause());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static ArrayList<Animal> buildListaFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<Animal> animalList = new ArrayList<>();
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

            LocalDate fechaEnt = LocalDate.parse(fechaEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate fechaSal = null;

            if (fechaSalida != null) {
                fechaSal = LocalDate.parse(fechaEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            try (Connection c = getConnection()) {
                Statement st = c.createStatement();
                String queryTratam = String.format("SELECT * FROM tratamientos WHERE id_animal='%d'", id);
                ResultSet rs2 = st.executeQuery(queryTratam);

                while (rs2.next()) {
                    fechas.add(new LocalDate[] {
                            LocalDate.parse(rs2.getString(3), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            LocalDate.parse(rs2.getString(4), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    });
                    descripciones.add(rs2.getString(5));
                }
            }

            Animal a = Animal.rebuildFromData(id, familia, peso, fechaEnt, fechaSal, especie, estado,
                    tipoLesion, fechas, descripciones, gravedad);

            animalList.add(a);
        }
        return animalList;
    }
}
