package org.example.Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.example.Aplicacion;

public class Conexion {
    static final String URL_DB = "jdbc:mysql://localhost:3306/db_animales";
    static final String URL = "jdbc:mysql://localhost:3306/";
    static final String USER = "root";

    static final List<String> queriesCrear = Arrays.asList("""
            CREATE TABLE familias (
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(10) UNIQUE NOT NULL,
                PRIMARY KEY (id)
            );
            """, """
            INSERT INTO familias(nombre)
            VALUES('Mamifero'), ('Reptil'), ('Ave');
            """, """
            CREATE TABLE estados (
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(15) UNIQUE NOT NULL,
                PRIMARY KEY (id)
            );
            """, """
            INSERT INTO estados(nombre)
            VALUES('Liberado'), ('Fallecido'), ('Tratamiento');
            """, """
            CREATE TABLE gravedad(
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(10)UNIQUE NOT NULL,
                PRIMARY KEY(id)
            );

            """, """
            INSERT INTO gravedad(nombre)
            VALUES('Alta'), ('Media'), ('Baja'), ('N/A');
            """, """
            CREATE TABLE animales(
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                tipo_familia INT NOT NULL,
                tipo_estado INT NOT NULL,
                tipo_gravedad INT NOT NULL,
                tipo_lesion BOOLEAN NOT NULL,
                peso INT NOT NULL,
                especie VARCHAR(10) NOT NULL,
                fecha_entrada DATE NOT NULL,
                fecha_salida DATE,
                FOREIGN KEY (tipo_familia) REFERENCES familias(id),
                FOREIGN KEY (tipo_estado) REFERENCES estados(id),
                FOREIGN KEY (tipo_gravedad) REFERENCES gravedad(id),
                PRIMARY KEY(id)
            );
            """, """
            CREATE TABLE tratamientos(
                id_animal INT NOT NULL,
                id_tratamiento INT AUTO_INCREMENT,
                fecha_inicio DATE NOT NULL,
                fecha_fin DATE NOT NULL,
                descripcion VARCHAR(200),
                FOREIGN KEY(id_animal) REFERENCES animales(id),
                PRIMARY KEY(id_tratamiento)
            ); """);

    public static Connection getConnection() {
        // Problema: la base de datos podría existir y no tener la estructura adecuada
        try {
            Connection c = DriverManager.getConnection(URL_DB, USER, "");
            return c;
        } catch (Exception e) {
            System.out.println("Base de datos no detectada\nCreando la base de datos de cero");
            try (Connection c2 = DriverManager.getConnection(URL, USER, "")) {
                Statement st = c2.createStatement();
                st.executeUpdate("DROP DATABASE IF EXISTS db_animales;");
                st.executeUpdate("CREATE DATABASE db_animales");
                try {
                    Connection c3 = DriverManager.getConnection(URL_DB, USER, "");
                    Statement st2 = c3.createStatement();
                    for (String q : queriesCrear) {
                        System.out.println(q);
                        st2.executeUpdate(q);
                    }

                    poblarMockData();

                    return c3;

                } catch (Exception e2) {
                    System.out.println("La conexión a la base de datos falló."
                            + "\nAsegúrate de que el servidor está funcionando");
                    System.out.println(e2.getLocalizedMessage());
                    throw new RuntimeException(e2);
                }
            } catch (Exception e2) {
                System.out.println("La conexión a la base de datos falló."
                        + "\nAsegúrate de que el servidor está funcionando");
                System.out.println(e2.getLocalizedMessage());
                throw new RuntimeException(e2);
            }
        }
    }

    public static void poblarMockData() {
        try (Connection c = DriverManager.getConnection(URL_DB, USER, "")) {
            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM estados");
            while (rs.next()) {
                Aplicacion.estados.put(rs.getString(2), rs.getInt(1));
                Aplicacion.estados_id.put(rs.getInt(1), rs.getString(2));
            }
            rs = st.executeQuery("SELECT * FROM familias");
            while (rs.next()) {
                Aplicacion.familias.put(rs.getString(2), rs.getInt(1));
                Aplicacion.familias_id.put(rs.getInt(1), rs.getString(2));
            }
            rs = st.executeQuery("SELECT * FROM gravedad");
            while (rs.next()) {
                Aplicacion.gravedades.put(rs.getString(2), rs.getInt(1));
                Aplicacion.gravedades_id.put(rs.getInt(1), rs.getString(2));
            }

            String insertAnimalQuery[] = {
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA,
                            1, Aplicacion.familias.get("Ave"), 2, "2023-04-23", "NULL", "Canario",
                            Aplicacion.estados.get("Tratamiento"), "TRUE", Aplicacion.gravedades.get("Alta")),
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA,
                            2, Aplicacion.familias.get("Mamifero"), 180, "2023-04-21", "NULL", "Tigre",
                            Aplicacion.estados.get("Tratamiento"), "TRUE",
                            Aplicacion.gravedades.get("Media")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            3, Aplicacion.familias.get("Reptil"), 3, "2023-04-22", "2023-04-24", "Cocodrilo",
                            Aplicacion.estados.get("Liberado"), "TRUE", Aplicacion.gravedades.get("N/A")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            4, Aplicacion.familias.get("Reptil"), 7, "2023-02-20", "2023-06-26", "Caimán",
                            Aplicacion.estados.get("Fallecido"), "FALSE", Aplicacion.gravedades.get("Media"))
            };

            String insertTratamQuery[] = {
                    String.format("""
                            INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                            VALUES (%d, '%s', '%s', '%s')""",
                            1, "2023-04-20", "2023-05-01", "Administración de medicamento"),
                    String.format("""
                            INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                            VALUES (%d, '%s', '%s', '%s')""",
                            2, "2023-04-22", "2023-05-05", "Cirugía de urgencia"),
                    String.format("""
                            INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                            VALUES (%d, '%s', '%s', '%s')""",
                            3, "2023-04-23", "2023-04-24", "Administración de antibióticos") };

            for (String str : insertAnimalQuery) {
                st.executeUpdate(str);
            }

            for (String str : insertTratamQuery) {
                st.executeUpdate(str);
            }

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
