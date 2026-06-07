package main.Conexion;

import java.sql.*;

public class conexion {
    // Al ser static, estas variables le pertenecen a la clase y no a un objeto
    private static Connection con;
    private static final String URL = "jdbc:mysql://localhost:3306/neojacket_db";          
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Modificamos el método a static para poder llamarlo directamente
    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();    
        }
        return con;
    }
}