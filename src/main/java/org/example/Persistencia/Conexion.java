package org.example.Persistencia;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Conexion {
    static HashMap<Integer, String> estados = new HashMap<>();
    static HashMap<Integer, String> familias = new HashMap<>();
    static HashMap<Integer, String> gravedades = new HashMap<>();
    static final String URL = "jdbc:mysql://localhost:3306/db_animales";
    static final String INSERT_ANIMAL = """
            INSERT INTO animales(id, !!!tipo!!!, peso, fechaEntrada, fechaSalida, especie, !!!estado!!!, tipoLesion, gravedad)
            VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');
            """;
    static final String INSERT_TRATAM = """
            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin, descripcion) VALUES (%s, %s, %s, %s)
            """;

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
                PRIMARY KEY (id)
            );
            """, """
            INSERT INTO familias(nombre)
            VALUES(mamifero), (reptil), (ave)
            """, """
            CREATE TABLE estados (
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(10) UNIQUE NOT NULL,
                PRIMARY KEY (id)
            );
            """, """
            INSERT INTO estados(nombre)
            VALUES(liberado), (fallecido), (tratamiento)
            """, """
            CREATE TABLE gravedad(
                    id INT UNIQUE NOT NULL AUTO_INCREMENT,
                    nombre VARCHAR(10)UNIQUE NOT NULL,
                    PRIMARY KEY(id)
            );
                                        
            """, """
            INSERT INTO gravedad(nombre)
            VALUES(alta), (media), (baja)
            """, """
            CREATE TABLE

            tratamientos(
                    id_animal INT NOT NULL,
                    id_tratamiento INT AUTO_INCREMENT,
                    fechaInicioT DATE NOT NULL,
                    fechaFinT DATE NOT NULL,
                    descripcion VARCHAR(200),

            FOREIGN KEY(id) REFERENCES id_animal
                    );
                    """, """
            CREATE TABLE

            animales(
                    id INT UNIQUE NOT NULL AUTO_INCREMENT,
                    tipo_familia INT NOT NULL,
                    tipo_estado INT NOT NULL,
                    tipo_gravedad INT NOT NULL,
                    especie VARCHAR(10) NOT NULL,

            tipoLesion BOOLEAN
            NOT NULL,
            fechaIngreso DATE
            NOT NULL,
            fechaSalida DATE,
            FOREIGN KEY

            familias(id) REFERENCES tipo_familia,

            FOREIGN KEY

            estados(id) REFERENCES tipo_estado,

            FOREIGN KEY

            gravedad(id) REFERENCES tipo_gravedad,

            PRIMARY KEY(id)
                    );
                    """);


    public static void checkDatabase() {
        try {
            DriverManager.getConnection(URL, "root", "");
        } catch (SQLException e) {
            System.out.println("Base de datos no detectada\nClonando la base de datos");
            try (Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/")) {
                Statement st = c.createStatement();
                st.executeUpdate("CREATE DATABASE db_animales");
                c.close();
                try (Connection c2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_animales")) {
                    Statement st2 = c2.createStatement();
                    for (String q : queriesCrear) st2.executeUpdate(q);
                }
            } catch (Exception e2) {
                System.out.println("La conexión a la base de datos falló." + "\nAsegúrate de que el servidor está funcionando");
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
            while (rs.next()) {
                estados.put(rs.getInt(1), rs.getString(2));
            }
            rs = st.executeQuery("SELECT * FROM familias");
            while (rs.next()) {
                familias.put(rs.getInt(1), rs.getString(2));
            }
            rs = st.executeQuery("SELECT * FROM gravedad");
            while (rs.next()) {
                gravedades.put(rs.getInt(1), rs.getString(2));
            }

            String insertAnimalQuery = String.format("""
                            INSERT INTO animales(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, gravedad)
                            VALUES('%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');""", 1, "Ave", 2.3, "2023-04-23", null,
                    "Canario", estados.get("tratamiento"), true, gravedades.get("alta"));
            String insertTratamQuery = String.format("""
                            INSERT INTO tratamientos (id_animal, fechaInicio, fechaFin, descripcion) VALUES (%d, '%s', '%s', '%s')""",
                    1, "2023-04-20", "2023-05-01", "Administración de medicamento");
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

