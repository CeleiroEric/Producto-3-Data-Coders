package datacoders.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[] args) {
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                System.out.println("¡CONEXIÓN EXITOSA! Java ya habla con MySQL.");
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("ERROR DE CONEXIÓN: " + e.getMessage());
            e.printStackTrace();
        }
    }
}