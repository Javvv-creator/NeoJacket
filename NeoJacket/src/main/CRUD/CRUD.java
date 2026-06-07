package main.CRUD;

import java.sql.*;
// Importamos la clase conexion desde su paquete
import main.Conexion.conexion; 

public class CRUD {

    // Metodo Crear - Javier
    // CORREGIDO: Se quitó el password duplicado, se agregó 'genero' y se usó 'dpiNumero'
    public void nuevoUsuario(String nombre, String apellido, String correo, String telefono,
            String fechaNacimiento, String genero, String password, String dpiNumero) {

        // 1. Instrucción SQL.
        String sql = "INSERT INTO usuarios (id_rol, nombre, apellido, correo, telefono, fecha_nacimiento, genero, password_hash, dpi_numero, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // 2. Bloque try-catch para abrir la conexión y preparar la sentencia
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            // 3. Los setString y setInt para enviar los datos a MySQL. El id_rol lo dejamos fijo en 2 (cliente).
            ps.setInt(1, 2);

            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, correo);
            ps.setString(5, telefono);

            // fecha como String "AAAA-MM-DD", java.sql.Date.valueOf para enviarla
            ps.setDate(6, java.sql.Date.valueOf(fechaNacimiento));

            ps.setString(7, genero);     // Tu ENUM de MySQL recibirá 'M', 'F' u 'Otro'
            ps.setString(8, password);   // Tu contraseña encriptada (Hash)
            ps.setString(9, dpiNumero); 

            // Mandamos "activo" como String.
            ps.setString(10, "activo");

            // executeUpdate para mandar los datos a MySQL
            ps.executeUpdate();

            // Mensaje de éxito
            System.out.println("El usuario fue creado con éxito en Neo Jacket.");

            // Cerrar las conexiones intermedias
            ps.close();
            con.close();

        } catch (SQLException e) {
            //  error de SQL
            System.out.println("Error al crear el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // editar

    // eliminar

    // buscar
}