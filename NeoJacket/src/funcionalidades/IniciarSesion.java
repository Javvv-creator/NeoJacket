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

        String sql = "SELECT id_rol, estado, password_hash FROM usuarios WHERE nombre = ?";

        try (Connection con = conexion.getConexion()) {
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    if (!PasswordUtil.verify(password, rs.getString("password_hash"))) {
                        return null;
                    }

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
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al verificar el inicio de sesión: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return null;
    }

    public int obtenerIdUsuario(String nombre, String password) {
        String sql = "SELECT id_usuario, password_hash FROM usuarios WHERE nombre = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.verify(password, rs.getString("password_hash"))) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return -1;
    }
}
