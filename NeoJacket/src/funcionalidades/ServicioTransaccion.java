package funcionalidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import main.Conexion.conexion;

public class ServicioTransaccion {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Object[][] listarTransacciones() {
        String sql = "SELECT t.id_transaccion, t.creado_en, c.numero_cuenta AS cuenta_origen, "
                + "t.tipo_transaccion, t.monto, t.moneda_origen, t.estado "
                + "FROM transacciones t "
                + "JOIN cuentas_bancarias c ON t.id_cuenta_origen = c.id_cuenta "
                + "ORDER BY t.creado_en DESC";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Object[]> filas = new ArrayList<>();
            while (rs.next()) {
                String fecha = rs.getTimestamp("creado_en") == null ? "" : rs.getTimestamp("creado_en").toLocalDateTime().format(FORMATO_FECHA);
                filas.add(new Object[]{
                    rs.getLong("id_transaccion"),
                    fecha,
                    rs.getString("cuenta_origen"),
                    rs.getString("tipo_transaccion"),
                    rs.getBigDecimal("monto") + " " + rs.getString("moneda_origen")
                });
            }
            return filas.toArray(new Object[0][]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al listar las transacciones: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new Object[0][];
        }
    }

    public Map<String, Object> obtenerDetalleTransaccion(long idTransaccion) {
        String sql = "SELECT t.id_transaccion, t.creado_en, t.tipo_transaccion, t.monto, t.moneda_origen, "
                + "t.moneda_destino, t.tasa_cambio_historica, t.estado, t.id_cuenta_origen, t.id_cuenta_destino, "
                + "c_origen.numero_cuenta AS cuenta_origen, c_destino.numero_cuenta AS cuenta_destino, "
                + "u.nombre, u.apellido "
                + "FROM transacciones t "
                + "JOIN cuentas_bancarias c_origen ON t.id_cuenta_origen = c_origen.id_cuenta "
                + "LEFT JOIN cuentas_bancarias c_destino ON t.id_cuenta_destino = c_destino.id_cuenta "
                + "LEFT JOIN usuarios u ON t.id_usuario_realizador = u.id_usuario "
                + "WHERE t.id_transaccion = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idTransaccion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> detalle = new LinkedHashMap<>();
                    String fecha = rs.getTimestamp("creado_en") == null ? "" : rs.getTimestamp("creado_en").toLocalDateTime().format(FORMATO_FECHA);
                    detalle.put("id_transaccion", rs.getLong("id_transaccion"));
                    detalle.put("fecha", fecha);
                    detalle.put("tipo_transaccion", rs.getString("tipo_transaccion"));
                    detalle.put("monto", rs.getBigDecimal("monto") + " " + rs.getString("moneda_origen"));
                    detalle.put("moneda_destino", rs.getString("moneda_destino"));
                    detalle.put("tasa_cambio", rs.getBigDecimal("tasa_cambio_historica"));
                    detalle.put("estado", rs.getString("estado"));
                    detalle.put("cuenta_origen", rs.getString("cuenta_origen"));
                    detalle.put("cuenta_destino", rs.getString("cuenta_destino"));
                    detalle.put("descripcion", rs.getString("descripcion"));
                    detalle.put("usuario", rs.getString("nombre") + " " + rs.getString("apellido"));
                    return detalle;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener el detalle de la transacción: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }
}
