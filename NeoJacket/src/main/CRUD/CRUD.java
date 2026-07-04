package main.CRUD;

import java.sql.*;
import javax.swing.JOptionPane;
// Importamos la clase conexion desde su paquete
import main.Conexion.conexion; 

public class CRUD {

    // Metodo Crear - Javier
    public boolean nuevoUsuario(String nombre, String apellido, String correo, String telefono,
            String fechaNacimiento, String genero, String password, String dpiNumero) {

        String sql = "INSERT INTO usuarios (id_rol, nombre, apellido, correo, telefono, fecha_nacimiento, genero, password_hash, dpi_numero, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = conexion.getConexion();
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
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
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al crear el usuario: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
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
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
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
            JOptionPane.showMessageDialog(null,
                    "Error de SQL al buscar el usuario por DPI: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return dpiEncontrado; // Devuelve el DPI encontrado o null
    }
    
     
    // =============================
// MÉTODO: Agregar Fondos
// =============================
   public boolean agregarFondos(int idUsuario, int idBanco, double monto, String descripcion) {
    try {
        Connection con = conexion.getConexion();

        // 1. Actualizar saldo en cuentas_bancarias
        PreparedStatement psUpdate = con.prepareStatement(
            "UPDATE cuentas_bancarias SET saldo = saldo + ? WHERE id_usuario = ? AND id_banco = ?"
        );
        psUpdate.setDouble(1, monto);
        psUpdate.setInt(2, idUsuario);
        psUpdate.setInt(3, idBanco);
        psUpdate.executeUpdate();

        // 2. Insertar transacción tipo "deposito"
        PreparedStatement psInsert = con.prepareStatement(
            "INSERT INTO transacciones (id_cuenta_origen, id_usuario_realizador, tipo_transaccion, monto, descripcion, moneda_origen) " +
            "VALUES ((SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ? LIMIT 1), ?, 'deposito', ?, ?, 'GTQ')"
        );
        psInsert.setInt(1, idUsuario);
        psInsert.setInt(2, idBanco);
        psInsert.setInt(3, idUsuario);
        psInsert.setDouble(4, monto);
        psInsert.setString(5, descripcion.isEmpty() ? null : descripcion);
        psInsert.executeUpdate();

        psUpdate.close();
        psInsert.close();
        con.close();
        return true;

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al agregar fondos: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

   // =============================
// MÉTODO: Actualizar Saldos
// =============================
public boolean actualizarSaldo(int idUsuario, int idBanco, double montoActual, double nuevoMonto, String motivo) {
    Connection con = null;
    PreparedStatement psUpdate = null;
    PreparedStatement psTrans = null;
    ResultSet rsCuenta = null;

    try {
        con = conexion.getConexion();

        // 1. Obtener la cuenta bancaria del usuario en ese banco
        PreparedStatement psCuenta = con.prepareStatement(
            "SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ?"
        );
        psCuenta.setInt(1, idUsuario);
        psCuenta.setInt(2, idBanco);
        rsCuenta = psCuenta.executeQuery();

        if (rsCuenta.next()) {
            int idCuenta = rsCuenta.getInt("id_cuenta");

            // 2. Actualizar el saldo en cuentas_bancarias
            psUpdate = con.prepareStatement(
                "UPDATE cuentas_bancarias SET saldo = ? WHERE id_cuenta = ?"
            );
            psUpdate.setDouble(1, nuevoMonto);
            psUpdate.setInt(2, idCuenta);
            psUpdate.executeUpdate();

            // 3. Registrar la transacción en transacciones
            psTrans = con.prepareStatement(
                "INSERT INTO transacciones (id_cuenta_origen, id_usuario_realizador, tipo_transaccion, monto, moneda_origen, estado) " +
                "VALUES (?, ?, ?, ?, 'GTQ', 'completada')"
            );
            psTrans.setInt(1, idCuenta);
            psTrans.setInt(2, idUsuario);

            // 🔹 Aquí decides el tipo de transacción según el caso
            if (nuevoMonto > montoActual) {
                psTrans.setString(3, "deposito");   // saldo aumentó
                psTrans.setDouble(4, nuevoMonto - montoActual);
            } else {
                psTrans.setString(3, "retiro");     // saldo disminuyó
                psTrans.setDouble(4, montoActual - nuevoMonto);
            }

            psTrans.executeUpdate();
        }

        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (rsCuenta != null) rsCuenta.close();
            if (psUpdate != null) psUpdate.close();
            if (psTrans != null) psTrans.close();
            if (con != null) con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


   // =============================
// MÉTODO: Consultar Saldo
// =============================
public double consultarSaldo(int idUsuario, int idBanco) {
    double saldo = 0;
    try {
        Connection con = conexion.getConexion();
        PreparedStatement ps = con.prepareStatement(
            "SELECT saldo FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ?"
        );
        ps.setInt(1, idUsuario);
        ps.setInt(2, idBanco);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            saldo = rs.getDouble("saldo");
        }

        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al consultar saldo: " + e.getMessage());
        e.printStackTrace();
    }
    return saldo;
}

// =============================
// MÉTODO: Consultar Transacciones
// =============================
public ResultSet consultarTransacciones(int idUsuario, int idBanco) {
    try {
        Connection con = conexion.getConexion();
        PreparedStatement ps = con.prepareStatement(
            "SELECT creado_en, tipo_transaccion, monto, " +
            "(SELECT saldo FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ?) AS saldoRestante, estado " +
            "FROM transacciones WHERE id_usuario_realizador = ? " +
            "AND id_cuenta_origen = (SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ? LIMIT 1) " +
            "ORDER BY creado_en DESC LIMIT 10"
        );
        ps.setInt(1, idUsuario);
        ps.setInt(2, idBanco);
        ps.setInt(3, idUsuario);
        ps.setInt(4, idUsuario);
        ps.setInt(5, idBanco);

        return ps.executeQuery(); // devolvemos el ResultSet para llenar la tabla

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al consultar transacciones: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
}