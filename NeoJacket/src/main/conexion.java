package main;

import java.sql.*;

public class conexion {

    Connection con;
    String url = "jdbc:mysql://localhost:3306/neo_jacket";
    String user = "root";
    String password = "123456789";

    public Connection getConexion() {
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return con;
    }
    
}
