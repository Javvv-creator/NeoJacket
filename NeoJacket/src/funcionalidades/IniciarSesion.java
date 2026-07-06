package funcionalidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import main.Conexion.conexion;

public class IniciarSesion {

    public String iniciarSesion(String nombre, String password) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        String sql = "SELECT id_rol, estado FROM usuarios WHERE nombre = ? AND password_hash = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = conexion.getConexion();
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, nombre.trim());
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                String estado = rs.getString("estado");
                if (!"activo".equalsIgnoreCase(estado)) {
                    JOptionPane.showMessageDialog(null,
                            "La cuenta no está activa. Consulte con soporte.",
                            "Cuenta inactiva",
                            JOptionPane.WARNING_MESSAGE);
                    return null;
                }

                int idRol = rs.getInt("id_rol");
                return idRol == 1 ? "ADMIN" : "CLIENTE";
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al verificar el inicio de sesión: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    
    public int obtenerIdUsuario(String nombre, String password) {
        int idUsuario = -1;
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                "SELECT id_usuario FROM usuarios WHERE nombre = ? AND password_hash = ?"
            );
            ps.setString(1, nombre.trim());
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idUsuario;
    }
}