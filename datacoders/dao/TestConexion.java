package datacoders.dao;

import datacoders.modelo.util.BDConnection; // Importamos la clase que nos quedamos
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión...");

        // Intentamos obtener la conexión usando el método correcto: getConnection()
        try (Connection con = BDConnection.getConnection()) {

            if (con != null && !con.isClosed()) {
                System.out.println("==========================================");
                System.out.println("¡CONEXIÓN EXITOSA!");
                System.out.println("Java ya habla con la base de datos: " + con.getCatalog());
                System.out.println("==========================================");
            }

        } catch (SQLException e) {
            System.err.println("==========================================");
            System.err.println("ERROR DE CONEXIÓN:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Código de error: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("==========================================");
            e.printStackTrace();
        }
    }
}