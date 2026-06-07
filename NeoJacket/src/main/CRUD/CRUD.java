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
    // editar
    public void editarUsuario(int idUsuario, String nombre, String apellido, String correo, 
                              String telefono, String genero, String estado) {
        
        // 1. Instrucción SQL adaptada a las columnas de tu tabla usuarios
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, telefono = ?, genero = ?, estado = ? WHERE id_usuario = ?";
        
        // 2. Bloque try-catch usando tu conexión estática
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            // 3. Mapeo de los '?' en el orden exacto de la consulta SQL
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, correo);
            ps.setString(4, telefono);
            ps.setString(5, genero); 
            ps.setString(6, estado); 
            ps.setInt(7, idUsuario); 
            
            // 4. Ejecutar la actualización en MySQL
            ps.executeUpdate();
            
            System.out.println("El usuario con ID " + idUsuario + " fue actualizado con éxito en Neo Jacket.");
            
            // Cerramos recursos
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error de SQL al editar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // eliminar
      public void eliminarUsuario(int idUsuario) {
        // 1. Instrucción SQL para eliminar 
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        // 2. Bloque try-catch utilizando tu misma estructura de conexión
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            // 3. Asignamos el ID del usuario al parámetro '?'
            ps.setInt(1, idUsuario);
            
            // 4. Ejecutamos la eliminación en la base de datos
            ps.executeUpdate();
            
            System.out.println("El usuario con ID " + idUsuario + " fue eliminado con éxito de Neo Jacket.");
            
            // Cerramos recursos de forma ordenada
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error de SQL al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        
  }
      
    public void desactivarUsuario(int idUsuario) {
        //  Instrucción SQL para actualizar únicamente el estado a 'inactivo'
        String sql = "UPDATE usuarios SET estado = 'inactivo' WHERE id_usuario = ?";
        
        
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            
            //  Pasamos el ID del usuario al único parámetro '?' de la consulta
            ps.setInt(1, idUsuario);
            
            //  Ejecutamos la actualización en MySQL
            ps.executeUpdate();
            
            System.out.println("La cuenta del usuario con ID " + idUsuario + " ahora está inactiva en Neo Jacket.");
            
            // Cerramos recursos
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error de SQL al desactivar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // buscar con ID
   public Integer buscarUsuario(int idUsuario){
       Integer idEncontrado = null;
       
       // 1. Instruccion de SQL.
        String sql = "SELECT idUsuario FROM usuarios WHERE id_usuario = ?";
        
        // 2. Bloque try catch para una conexion estática
       try{
           Connection con = conexion.getConexion();
           PreparedStatement ps = con.prepareStatement(sql);
           
           // 3. Orden exacto de la consulta de SQL 
            ps.setInt(1, idUsuario); 
            
            // 4 Ejercutar la buscaqueda en MySQL
            ResultSet rs = ps.executeQuery();
            
            //5. Guardamos el id encontrado
            if (rs.next()){
                idUsuario = rs.getInt("id_usuario");
                System.out.println("El usuario con ID " + idUsuario + " fue encontrado con éxito en Neo Jacket.");
            }else {
            System.out.println("No se encontró ningún usuario con ID " + idUsuario);
        }
            // Cerramos recursos
            rs.close();
            ps.close();
            con.close();
           
       } catch (SQLException e){
           System.out.println("Error de SQL al buscar el usuario" + e.getMessage());
           e.printStackTrace();
       }
       return idEncontrado;
   }
   
   public Integer buscarUsuario2(int dpiNumero){
      Integer dpi = null; 
      
      // 1. Intruccion de SQL.
      String sql = "SELECT dpi_numero FROM usuarios WHRE dpi_umero = ?";
      
      // 2. Bloque try catch para una conexion estatica
      try{
          Connection con = conexion.getConexion();
           PreparedStatement ps = con.prepareStatement(sql);
           
           // 3. Orden exacto de la consulta de SQL 
            ps.setInt(1, dpiNumero); 
            
            // 4 Ejercutar la buscaqueda en MySQL
            ResultSet rs = ps.executeQuery();
            
            //5. Guardamos el id encontrado
            if (rs.next()){
                dpiNumero = rs.getInt("dpi_numero");
                System.out.println("El usuario con el DPI: " + dpiNumero + " fue encontrado con éxito en Neo Jacket.");
            }else {
            System.out.println("No se encontró ningún usuario con DPI: " + dpiNumero);
        }
            // Cerramos recursos
            rs.close();
            ps.close();
            con.close();
      } catch (SQLException e){
          System.out.println("Error de SQL al buscar el usuario" + e.getMessage());
           e.printStackTrace();
      }
      return dpi;
   }
}  