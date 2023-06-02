package org.example.Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.example.Aplicacion;

import static org.example.Aplicacion.*;

public class Conexion {
    //NOTA: El puerto es el 3307 porque es como está configurado el XAMPP en clase
    static final String URL_DB = "jdbc:mysql://localhost:3307/db_animales";
    static final String URL = "jdbc:mysql://localhost:3307";
    static final String USER = "root";

    static final String[] queriesCrear = new String[]{"""
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
                        especie VARCHAR(20) NOT NULL,
                        fecha_entrada VARCHAR(15) NOT NULL,
                        fecha_salida VARCHAR(15),
                        FOREIGN KEY (tipo_familia) REFERENCES familias(id),
                        FOREIGN KEY (tipo_estado) REFERENCES estados(id),
                        FOREIGN KEY (tipo_gravedad) REFERENCES gravedad(id),
                        PRIMARY KEY(id)
                    );
                    """, """
                    CREATE TABLE tratamientos(
                        id_animal INT NOT NULL,
                        id_tratamiento INT AUTO_INCREMENT,
                        fecha_inicio VARCHAR(15) NOT NULL,
                        fecha_fin VARCHAR(15) NOT NULL,
                        descripcion VARCHAR(200),
                        FOREIGN KEY(id_animal) REFERENCES animales(id),
                        PRIMARY KEY(id_tratamiento)
                    );"""
    };

    public static Connection getConnection() {
        try {
            Connection c = DriverManager.getConnection(URL_DB, USER, "");
            // Si no ha habido excepción, la BD existe y funciona
            // Problema: la base de datos podría existir y no tener la estructura adecuada,
            // pero no se comprueba eso
            if (estados.isEmpty() || familias.isEmpty() || gravedades.isEmpty()) {
                poblarHashMaps(c);
            }
            return c;
        } catch (SQLException e) {
            System.out.println("Base de datos no detectada\nCreando la base de datos de cero");
            try (Connection c2 = DriverManager.getConnection(URL, USER, "")) {
                Statement st = c2.createStatement();
                st.executeUpdate("DROP DATABASE IF EXISTS db_animales;");
                st.executeUpdate("CREATE DATABASE db_animales");
                st.close();
                c2.close();
                try (Connection c3 = DriverManager.getConnection(URL_DB, USER, "")) {
                    Statement st2 = c3.createStatement();

                    for (String q : queriesCrear) st2.executeUpdate(q);

                    st2.close();
                    poblarHashMaps(c3);
                    poblarMockData(c3);
                }
                return DriverManager.getConnection(URL_DB, USER, "");

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

    public static void poblarMockData(Connection c) {
        try {
            Statement st = c.createStatement();

            String[] insertAnimalQuery = {
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA,
                            1, Aplicacion.familias.get("Ave"), 2, "2023-04-23", "NULL", "Canario",
                            estados.get("Tratamiento"), "TRUE", Aplicacion.gravedades.get("Alta")),
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA,
                            2, Aplicacion.familias.get("Mamifero"), 180, "2023-04-21", "NULL", "Tigre",
                            estados.get("Tratamiento"), "TRUE",
                            Aplicacion.gravedades.get("Media")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            3, Aplicacion.familias.get("Reptil"), 3, "2023-04-22", "2023-04-24", "Cocodrilo",
                            estados.get("Liberado"), "TRUE", Aplicacion.gravedades.get("N/A")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            4, Aplicacion.familias.get("Reptil"), 7, "2023-02-20", "2023-06-26", "Caimán",
                            estados.get("Fallecido"), "FALSE", Aplicacion.gravedades.get("Media")),
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA,
                            5, Aplicacion.familias.get("Ave"), 2, "2023-06-23", "NULL", "Canario",
                            estados.get("Tratamiento"), "FALSE", Aplicacion.gravedades.get("Alta")),
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA,
                            6, Aplicacion.familias.get("Mamifero"), 180, "2023-07-21", "NULL", "Tigre",
                            estados.get("Tratamiento"), "FALSE",
                            Aplicacion.gravedades.get("Media")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            7, Aplicacion.familias.get("Reptil"), 3, "2023-03-22", "2023-09-24", "Salamanquesa",
                            estados.get("Liberado"), "TRUE", Aplicacion.gravedades.get("N/A")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            8, Aplicacion.familias.get("Reptil"), 7, "2023-01-20", "2023-12-26", "Serpiente",
                            estados.get("Liberado"), "TRUE", Aplicacion.gravedades.get("Media")),
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA,
                            9, Aplicacion.familias.get("Ave"), 30, "2023-06-15", "NULL", "Búho real",
                            estados.get("Tratamiento"), "FALSE", Aplicacion.gravedades.get("Baja")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            10, Aplicacion.familias.get("Mamifero"), 50, "2023-02-10", "2023-07-20", "Oso pardo",
                            estados.get("Liberado"), "TRUE", Aplicacion.gravedades.get("Alta")),
                    String.format(GestionDatos.INSERT_ANIMAL_SIN_FECHA, 11, Aplicacion.familias.get("Mamifero"), 2,
                            "2023-04-05", "NULL", "Rana verde",
                            estados.get("Tratamiento"), "FALSE", Aplicacion.gravedades.get("N/A")),
                    String.format(GestionDatos.INSERT_ANIMAL_CON_FECHA,
                            12, Aplicacion.familias.get("Mamifero"), 50, "2023-02-10", "2023-09-24", "Oso polar",
                            estados.get("Liberado"), "TRUE", Aplicacion.gravedades.get("Alta")),

            };

            String[] insertTratamQuery = {
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            1, "2023-04-20", "2023-05-01", "Administración de medicamento"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            1, "2023-04-22", "2023-05-05", "Cirugía de urgencia"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            2, "2023-04-20", "2023-05-01", "Administración de medicamento"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            2, "2023-04-22", "2023-05-05", "Cirugía de urgencia"),
                    String.format("""
                                     INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            2, "2023-04-23", "2023-04-24", "Administración de antibióticos"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            3, "2023-04-24", "2023-04-30", "Rehabilitación física"),

                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            3, "2023-04-20", "2023-05-01", "Administración de medicamento"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            3, "2023-04-22", "2023-05-05", "Cirugía de urgencia"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            4, "2023-04-20", "2023-05-01", "Administración de medicamento"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            4, "2023-04-22", "2023-05-05", "Cirugía de urgencia"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            4, "2023-04-26", "2023-04-28", "Administración de analgésicos"),
                    String.format("""
                                     INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            5, "2023-04-27", "2023-04-29", "Observación médica"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            6, "2023-04-28", "2023-05-03", "Radiografía"),
                    String.format("""
                                    INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            7, "2023-04-29", "2023-05-01", "Tratamiento antiparasitario"),
                    String.format("""
                                     INSERT INTO tratamientos (id_animal, fecha_inicio, fecha_fin, descripcion)
                                    VALUES                   (%d,        '%s',         '%s',      '%s')""",
                            9, "2023-04-30", "2023-05-05", "Cirugía de rutina")

            };

            for (String str : insertAnimalQuery)
                st.executeUpdate(str);

            for (String str : insertTratamQuery)
                st.executeUpdate(str);

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    // al crear la DB por primera vez, o, en cada query (mejorable)
    public static void poblarHashMaps(Connection c) {
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM estados");
            while (rs.next()) {
                estados.put(rs.getString(2), rs.getInt(1));
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
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
