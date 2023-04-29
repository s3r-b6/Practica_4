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
    static final String URL = "jdbc:mysql://localhost:3306/db_animales";

    static final List<String> queriesCrear = Arrays.asList("""
            CREATE TABLE Aplicacion.familias (
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(10) UNIQUE NOT NULL,
                PRIMARY KEY (id);
            );
            """, """
            INSERT INTO Aplicacion.familias(nombre)
            VALUES('mamifero'), ('reptil'), ('ave');
            """, """
            CREATE TABLE Aplicacion.estados (
                id INT UNIQUE NOT NULL AUTO_INCREMENT,
                nombre VARCHAR(15) UNIQUE NOT NULL,
                PRIMARY KEY (id)
            );
            """, """
            INSERT INTO Aplicacion.estados(nombre)
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
                FOREIGN KEY (tipo_familia) REFERENCES Aplicacion.familias(id),
                FOREIGN KEY (tipo_estado) REFERENCES Aplicacion.estados(id),
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

    public static Connection getConnection() {
        Connection conn;
        try {
            // Problema: la base de datos podría existir y no tener la estructura adecuada
            conn = DriverManager.getConnection(URL, "root", "");
            return conn;
        } catch (SQLException e) {
            System.out.println("Base de datos no detectada\nClonando la base de datos");
            try (Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/")) {
                Statement st = c.createStatement();
                st.executeUpdate("CREATE DATABASE db_animales");
                c.close();
                try (Connection c2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_animales")) {
                    Statement st2 = c2.createStatement();
                    for (String q : queriesCrear)
                        st2.executeUpdate(q);

                    conn = c2;
                    return conn;
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
        try (Connection c = DriverManager.getConnection(URL, "root", "")) {
            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Aplicacion.estados");
            while (rs.next())
                Aplicacion.estados.put(rs.getString(2), rs.getInt(1));
            rs = st.executeQuery("SELECT * FROM Aplicacion.familias");
            while (rs.next())
                Aplicacion.familias.put(rs.getString(2), rs.getInt(1));
            rs = st.executeQuery("SELECT * FROM gravedad");
            while (rs.next())
                Aplicacion.gravedades.put(rs.getString(2), rs.getInt(1));

            String insertAnimalQuery[] = {
                    String.format(
                            """
                                    INSERT INTO animales(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, gravedad)
                                    VALUES(%d, %d, %d, '%s', '%s', '%s', %d, '%s', %d);""",
                            1, Aplicacion.familias.get("ave"), 2.3, "2023-04-23", true, "Canario",
                            Aplicacion.estados.get("tratamiento"), true, Aplicacion.gravedades.get("alta")),
                    String.format(
                            """
                                    INSERT INTO animales(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, gravedad)
                                    VALUES(%d, %d, %d, '%s', '%s', '%s', %d, '%s', %d);""",
                            2, Aplicacion.familias.get("mamifero"), 180.0, "2023-04-21", false, "Tigre",
                            Aplicacion.estados.get("tratamiento"), "Herida de garra",
                            Aplicacion.gravedades.get("media")),
                    String.format(
                            """
                                    INSERT INTO animales(id, tipo, peso, fechaEntrada, fechaSalida, especie, estado, tipoLesion, gravedad)
                                    VALUES(%d, %d, %d, '%s', '%s', '%s', %d, '%s', %d);""",
                            3, Aplicacion.familias.get("reptil"), 3.5, "2023-04-22", "2023-04-24", "Cocodrilo",
                            Aplicacion.estados.get("libertad"), true, Aplicacion.gravedades.get("n-a")) };

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
                            "Administración de antibióticos") };

            for (String str : insertAnimalQuery)
                st.executeUpdate(str);

            for (String str : insertTratamQuery)
                st.executeUpdate(str);

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
