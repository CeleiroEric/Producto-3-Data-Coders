package datacoders.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Datos de tu MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_pedidos";
    private static final String USER = "root";
    private static final String PASS = "rootsql1.26"; //

    public static Connection obtenerConexion() throws SQLException {
        try {
            // Esto carga el driver que acabamos de descargar
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL", e);
        }
    }
}