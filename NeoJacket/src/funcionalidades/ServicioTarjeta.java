package funcionalidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import main.Conexion.conexion;

public class ServicioTarjeta {

    public Object[][] listarTarjetas() {
        String sql = "SELECT t.id_tarjeta, t.numero_tarjeta, CONCAT(u.nombre, ' ', u.apellido) AS propietario, "
                + "t.tipo_tarjeta, t.estado "
                + "FROM tarjetas_bancarias t "
                + "JOIN usuarios u ON t.id_usuario = u.id_usuario";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Object[]> filas = new ArrayList<>();
            while (rs.next()) {
                filas.add(new Object[]{
                    rs.getInt("id_tarjeta"),
                    rs.getString("numero_tarjeta"),
                    rs.getString("propietario"),
                    rs.getString("tipo_tarjeta"),
                    rs.getString("estado")
                });
            }
            return filas.toArray(new Object[0][]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al listar las tarjetas: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new Object[0][];
        }
    }

    public Map<String, Object> obtenerDetalleTarjeta(int idTarjeta) {
        String sql = "SELECT t.id_tarjeta, t.numero_tarjeta, CONCAT(u.nombre, ' ', u.apellido) AS propietario, "
                + "t.tipo_tarjeta, t.estado, b.nombre AS banco, t.pais, t.creado_en "
                + "FROM tarjetas_bancarias t "
                + "JOIN usuarios u ON t.id_usuario = u.id_usuario "
                + "JOIN bancos b ON t.id_banco = b.id_banco "
                + "WHERE t.id_tarjeta = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTarjeta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> detalle = new LinkedHashMap<>();
                    detalle.put("id_tarjeta", rs.getInt("id_tarjeta"));
                    detalle.put("numero_tarjeta", rs.getString("numero_tarjeta"));
                    detalle.put("propietario", rs.getString("propietario"));
                    detalle.put("tipo_tarjeta", rs.getString("tipo_tarjeta"));
                    detalle.put("estado", rs.getString("estado"));
                    detalle.put("banco", rs.getString("banco"));
                    detalle.put("pais", rs.getString("pais"));
                    detalle.put("creado_en", rs.getTimestamp("creado_en"));
                    return detalle;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener el detalle de la tarjeta: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }

    public boolean cambiarEstadoTarjeta(int idTarjeta, String estado) {
        String sql = "UPDATE tarjetas_bancarias SET estado = ? WHERE id_tarjeta = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idTarjeta);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cambiar el estado de la tarjeta: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}
