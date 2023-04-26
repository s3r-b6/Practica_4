package org.example.Persistencia;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Conexion {
    static HashMap<String, Integer> estados = new HashMap<>();
    static HashMap<String, Integer> familias = new HashMap<>();
    static HashMap<String, Integer> gravedades = new HashMap<>();

    static final String URL = "jdbc:mysql://localhost:3306/db_animales";
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
            SELECT * FROM tratamientos WHERE id_animal = %s;
            """;
    static final List<String> queriesCrear = Arrays.asList("""
            CREATE TABLE familias (
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(10) UNIQUE NOT NULL,
                PRIMARY KEY (id);
            );
            """, """
            INSERT INTO familias(nombre)
            VALUES('mamifero'), ('reptil'), ('ave');
            """, """
            CREATE TABLE estados (
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(15) UNIQUE NOT NULL,
                PRIMARY KEY (id)
            );
            """, """
            INSERT INTO estados(nombre)
            VALUES('liberado'), ('fallecido'), ('tratamiento');
            """, """
            CREATE TABLE gravedad(
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(10)UNIQUE NOT NULL,
                PRIMARY KEY(id)
            );

            """, """
            INSERT INTO gravedad(nombre)
            VALUES('alta'), ('media'), ('baja'), ('n-a');
            """, """
            CREATE TABLE animales(
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                tipo_familia INT NOT NULL,
                tipo_estado INT NOT NULL,
                tipo_gravedad INT NOT NULL,
                especie VARCHAR(10) NOT NULL,
                tipoLesion BOOLEAN NOT NULL, fechaIngreso DATE NOT NULL,
                fechaSalida DATE,
                FOREIGN KEY (tipo_familia) REFERENCES familias(id),
                FOREIGN KEY (tipo_estado) REFERENCES estados(id),
                FOREIGN KEY (tipo_gravedad) REFERENCES gravedad(id),
                PRIMARY KEY(id)
            );
            """, """
            CREATE TABLE tratamientos(
                id_animal INT NOT NULL,
                id_tratamiento INT AUTO_INCREMENT,
                fechaInicioT DATE NOT NULL,
                fechaFinT DATE NOT NULL,
                descripcion VARCHAR(200),
                FOREIGN KEY(id_animal) REFERENCES animales(id),
                PRIMARY KEY(id_tratamiento)
            ); """);


    public static void checkDatabase() {
        try {
            DriverManager.getConnection(URL, "root", "");
        } catch (SQLException e) {
            System.out.println("Base de datos no detectada\nClonando la base de datos");
            try (Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/")) {
                Statement st = c.createStatement();
                st.executeUpdate("CREATE DATABASE db_animales");
                c.close();
                try (Connection c2 =
                        DriverManager.getConnection("jdbc:mysql://localhost:3306/db_animales")) {
                    Statement st2 = c2.createStatement();
                    for (String q : queriesCrear)
                        st2.executeUpdate(q);
                }
            } catch (Exception e2) {
                System.out.println("La conexión a la base de datos falló."
                        + "\nAsegúrate de que el servidor está funcionando");
                System.out.println(e2.getLocalizedMessage());
                throw new RuntimeException(e2);
            }
            throw new RuntimeException(e);
        }
    }

    public static ResultSet busquedaPorAnimal(String query, String filtro) {
        try (Connection c = DriverManager.getConnection(URL, "root", "")) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(String.format(query, filtro));
            return rs;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static ResultSet busquedaPorTipo(String query, String filtro) {
        try (Connection c = DriverManager.getConnection(URL, "root", "")) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(String.format(query, filtro));
            return rs;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }


    public static void poblarMockData() {
        try (Connection c = DriverManager.getConnection(URL, "root", "")) {
            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM estados");
            while (rs.next())
                estados.put(rs.getString(2), rs.getInt(1));
            rs = st.executeQuery("SELECT * FROM familias");
            while (rs.next())
                familias.put(rs.getString(2), rs.getInt(1));
            rs = st.executeQuery("SELECT * FROM gravedad");
            while (rs.next())
                gravedades.put(rs.getString(2), rs.getInt(1));

            String insertAnimalQuery[] = {String.format(
                    """
                            INSERT INTO animales(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, gravedad)
                            VALUES('%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');""",
                    1, familias.get("ave"), 2.3, "2023-04-23", true, "Canario",
                    estados.get("tratamiento"), true, gravedades.get("alta")),
                    String.format(
                            """
                                    INSERT INTO animales(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, gravedad)
                                    VALUES('%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');""",
                            2, familias.get("mamifero"), 180.0, "2023-04-21", false, "Tigre",
                            estados.get("tratamiento"), "Herida de garra", gravedades.get("media")),
                    String.format(
                            """
                                    INSERT INTO animales(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, gravedad)
                                    VALUES('%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');""",
                            3, familias.get("reptil"), 3.5, "2023-04-22", "2023-04-24", "Cocodrilo",
                            estados.get("libertad"), true, gravedades.get("n-a"))};


            String insertTratamQuery[] = {
                    String.format("""
                            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin, descripcion)
                            VALUES (%d, '%s', '%s', '%s')""", 1, "2023-04-20", "2023-05-01",
                            "Administración de medicamento"),
                    String.format("""
                            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin, descripcion)
                            VALUES (%d, '%s', '%s', '%s')""", 2, "2023-04-22", "2023-05-05",
                            "Cirugía de urgencia"),
                    String.format("""
                            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin, descripcion)
                            VALUES (%d, '%s', '%s', '%s')""", 3, "2023-04-23", "2023-04-24",
                            "Administración de antibióticos")};
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static ResultSet busquedaGeneral() {
        try (Connection c = DriverManager.getConnection(URL, "root", "")) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM animales");
            return rs;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}

