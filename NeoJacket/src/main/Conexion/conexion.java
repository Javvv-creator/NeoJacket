package main.Conexion;

import java.sql.*;
import javax.swing.JOptionPane;

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
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el driver MySQL JDBC.\n" +
                    "Agrega mysql-connector-java.jar al classpath del proyecto.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Error al conectar a la base de datos: com.mysql.cj.jdbc.Driver no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar a la base de datos:\n" + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado al conectar a la base de datos:\n" + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return con;
    }
}