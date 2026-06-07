package main.CRUD;

import java.sql.*;
// Importamos la clase conexion desde su paquete
import main.Conexion.conexion; 

public class CRUD {

    // Metodo Crear - Javier
    public void nuevoUsuario(String nombre, String apellido, String correo, String telefono,
            String fechaNacimiento, String genero, String password, String dpiNumero) {

        String sql = "INSERT INTO usuarios (id_rol, nombre, apellido, correo, telefono, fecha_nacimiento, genero, password_hash, dpi_numero, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, 2); // Cliente fijo
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, correo);
            ps.setString(5, telefono);
            ps.setDate(6, java.sql.Date.valueOf(fechaNacimiento));
            ps.setString(7, genero);     
            ps.setString(8, password);   
            ps.setString(9, dpiNumero); 
            ps.setString(10, "activo");

            ps.executeUpdate();
            System.out.println("El usuario fue creado con éxito en Neo Jacket.");

            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error al crear el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo Editar - Javier
    public void editarUsuario(int idUsuario, String nombre, String apellido, String correo, 
                              String telefono, String genero, String estado) {
        
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, telefono = ?, genero = ?, estado = ? WHERE id_usuario = ?";
        
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, correo);
            ps.setString(4, telefono);
            ps.setString(5, genero); 
            ps.setString(6, estado); 
            ps.setInt(7, idUsuario); 
            
            ps.executeUpdate();
            System.out.println("El usuario con ID " + idUsuario + " fue actualizado con éxito en Neo Jacket.");
            
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error de SQL al editar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo Eliminar - Gallardo
    public void eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            System.out.println("El usuario con ID " + idUsuario + " fue eliminado con éxito de Neo Jacket.");
            
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error de SQL al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
      
    // Metodo Desactivar - Gallardo
    public void desactivarUsuario(int idUsuario) {
        String sql = "UPDATE usuarios SET estado = 'inactivo' WHERE id_usuario = ?";
        
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            System.out.println("La cuenta del usuario con ID " + idUsuario + " ahora está inactiva en Neo Jacket.");
            
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error de SQL al desactivar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public Integer buscarUsuario(int idUsuario) {
        Integer idEncontrado = null;
        String sql = "SELECT id_usuario FROM usuarios WHERE id_usuario = ?";
        
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, idUsuario); 
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Asignamos el valor real de la base de datos a la variable que vamos a retornar
                idEncontrado = rs.getInt("id_usuario");
                System.out.println("El usuario con ID " + idEncontrado + " fue encontrado con éxito en Neo Jacket.");
            } else {
                System.out.println("No se encontró ningún usuario con ID " + idUsuario);
            }
            
            rs.close();
            ps.close();
            con.close();
           
        } catch (SQLException e) {
            System.out.println("Error de SQL al buscar el usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return idEncontrado; // Ahora sí devuelve el ID si existe, o null si no
    }
   
    public String buscarUsuario2(String dpiNumero) {
        String dpiEncontrado = null; 
        String sql = "SELECT dpi_numero FROM usuarios WHERE dpi_numero = ?";
      
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, dpiNumero); 
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                dpiEncontrado = rs.getString("dpi_numero");
                System.out.println("El usuario con el DPI: " + dpiEncontrado + " fue encontrado con éxito en Neo Jacket.");
            } else {
                System.out.println("No se encontró ningún usuario con DPI: " + dpiNumero);
            }
            
            rs.close();
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error de SQL al buscar el usuario por DPI: " + e.getMessage());
            e.printStackTrace();
        }
        return dpiEncontrado; // Devuelve el DPI encontrado o null
    }
}