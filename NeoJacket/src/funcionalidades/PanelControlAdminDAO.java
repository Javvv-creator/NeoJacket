package funcionalidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import main.Conexion.conexion;


public class PanelControlAdminDAO {

    // ==========================================
    // TOTALES GENERALES (para las tarjetas de arriba)
    // ==========================================

    /** Total de usuarios clientes (id_rol = 2), incluye adultos y menores. */
    public int obtenerTotalUsuarios() {
        return contar("SELECT COUNT(*) AS total FROM usuarios WHERE id_rol = 2");
    }

    /** Total de cuentas bancarias registradas (todas, sin importar estado). */
    public int obtenerTotalCuentas() {
        return contar("SELECT COUNT(*) AS total FROM cuentas_bancarias");
    }

    /** Total de tarjetas registradas (todas, sin importar estado). */
    public int obtenerTotalTarjetas() {
        return contar("SELECT COUNT(*) AS total FROM tarjetas_bancarias");
    }

    /** Total de usuarios cuyo perfil es "Menor supervisado". */
    public int obtenerTotalMenores() {
        return contar("SELECT COUNT(*) AS total FROM usuarios WHERE perfil = 'Menor supervisado'");
    }

    /** Total de transacciones registradas (depósitos, retiros, transferencias, actualizaciones). */
    public int obtenerTotalTransacciones() {
        return contar("SELECT COUNT(*) AS total FROM transacciones");
    }

    private int contar(String sql) {
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ==========================================
    // GRÁFICA: USUARIOS REGISTRADOS POR MES
    // ==========================================

    public static class DatoMes {
        public String etiqueta; // Ej. "Mar"
        public int total;

        public DatoMes(String etiqueta, int total) {
            this.etiqueta = etiqueta;
            this.total = total;
        }
    }

    /**
     * Devuelve el conteo de usuarios registrados (creado_en) para cada uno
     * de los últimos `meses` meses, incluyendo el mes actual, en orden
     * cronológico (el más antiguo primero). Si un mes no tiene registros,
     * igual aparece en la lista con total = 0 (no se omite).
     */
    public List<DatoMes> obtenerUsuariosPorMes(int meses) {
        List<DatoMes> lista = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        String sql = "SELECT COUNT(*) AS total FROM usuarios WHERE YEAR(creado_en) = ? AND MONTH(creado_en) = ?";

        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            for (int i = meses - 1; i >= 0; i--) {
                LocalDate mes = hoy.minusMonths(i);
                int total = 0;
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, mes.getYear());
                    ps.setInt(2, mes.getMonthValue());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            total = rs.getInt("total");
                        }
                    }
                }
                String etiqueta = mes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
                etiqueta = etiqueta.substring(0, 1).toUpperCase() + etiqueta.substring(1);
                lista.add(new DatoMes(etiqueta, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // ACTIVIDAD RECIENTE (inicio de sesión + transacciones + registros)
    // ==========================================

    public static class ActividadItem {
        public String texto;
        public Timestamp fecha;

        public ActividadItem(String texto, Timestamp fecha) {
            this.texto = texto;
            this.fecha = fecha;
        }
    }

    /**
     * Combina en una sola línea de tiempo: inicios de sesión, transacciones
     * (depósitos, retiros, transferencias, actualizaciones) y altas de
     * usuario nuevo, cada una con el nombre del usuario involucrado,
     * ordenadas de más reciente a más antigua.
     */
    public List<ActividadItem> obtenerActividadReciente(int limite) {
        List<ActividadItem> lista = new ArrayList<>();

        String sql =
                "SELECT texto, fecha FROM ("
                + "  SELECT CONCAT('Inicio de sesión de ', u.nombre, ' ', u.apellido) AS texto, s.ocurrido_en AS fecha "
                + "  FROM sesiones s JOIN usuarios u ON u.id_usuario = s.id_usuario "
                + "  WHERE s.tipo_evento = 'inicio_sesion' "
                + "  UNION ALL "
                + "  SELECT CONCAT("
                + "           CASE t.tipo_transaccion "
                + "             WHEN 'deposito' THEN 'Fondo agregado de ' "
                + "             WHEN 'transferencia' THEN 'Transferencia de ' "
                + "             WHEN 'retiro' THEN 'Retiro de ' "
                + "             ELSE 'Transacción de ' "
                + "           END, u.nombre, ' ', u.apellido) AS texto, "
                + "         t.creado_en AS fecha "
                + "  FROM transacciones t JOIN usuarios u ON u.id_usuario = t.id_usuario_realizador "
                + "  UNION ALL "
                + "  SELECT CONCAT('Usuario registrado: ', u.nombre, ' ', u.apellido) AS texto, u.creado_en AS fecha "
                + "  FROM usuarios u "
                + ") AS actividad "
                + "ORDER BY fecha DESC "
                + "LIMIT ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ActividadItem(rs.getString("texto"), rs.getTimestamp("fecha")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}