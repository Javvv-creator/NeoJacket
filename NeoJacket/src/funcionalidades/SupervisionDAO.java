package funcionalidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import main.Conexion.conexion;

public class SupervisionDAO {

    // ============================
    // MODELOS SIMPLES (DTOs)
    // ============================

    public static class MenorSupervisado {
        public int idUsuario;
        public String nombre;
        public String apellido;
        public String correo;
        public String getNombreCompleto() { return nombre + " " + apellido; }
    }

    public static class MovimientoCuenta {
        public String tipoTransaccion;
        public double monto;
        public String moneda;
        public String estado;
        public Timestamp fecha;
    }

    public static class EventoSesion {
        public String tipoEvento;
        public String dispositivo;
        public Timestamp ocurridoEn;
    }

    public static class LimiteCuenta {
        public int idLimite;
        public String tipoLimite;
        public double montoMaximo;
    }

    public static class SolicitudPermiso {
        public int idSolicitud;
        public int idMenor;
        public int idAdulto;       // CORREGIDO: era idTutor
        public String tipoAccion;
        public String descripcion;
        public double monto;
        public String estado;
        public Timestamp creadoEn;
        public String nombreMenor; // para mostrar en DashboardTutor
    }

    // ============================
    // BUSCAR USUARIO POR CORREO
    // ============================
    public Integer buscarIdUsuarioPorCorreo(String correo) {
        String sql = "SELECT id_usuario FROM usuarios WHERE correo = ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, correo.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el correo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    // ============================
    // CREAR VÍNCULO TUTOR - MENOR
    // ============================
    public boolean crearSupervision(int idAdulto, int idMenor) {
        String sql = "INSERT INTO supervisiones (id_adulto, id_menor, activa) VALUES (?, ?, TRUE)";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAdulto);
                ps.setInt(2, idMenor);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al vincular tutor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    // ============================
    // ¿TIENE MENORES A CARGO?
    // ============================
    public boolean tieneMenoresACargo(int idAdulto) {
        String sql = "SELECT COUNT(*) AS total FROM supervisiones WHERE id_adulto = ? AND activa = TRUE";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAdulto);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ============================
    // LISTAR MENORES A CARGO
    // ============================
    public List<MenorSupervisado> listarMenoresDeTutor(int idAdulto) {
        List<MenorSupervisado> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.correo " +
                     "FROM supervisiones s JOIN usuarios u ON u.id_usuario = s.id_menor " +
                     "WHERE s.id_adulto = ? AND s.activa = TRUE ORDER BY u.nombre";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAdulto);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        MenorSupervisado m = new MenorSupervisado();
                        m.idUsuario = rs.getInt("id_usuario");
                        m.nombre    = rs.getString("nombre");
                        m.apellido  = rs.getString("apellido");
                        m.correo    = rs.getString("correo");
                        lista.add(m);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // ============================
    // SALDO TOTAL DEL MENOR
    // ============================
    public double obtenerSaldoTotal(int idMenor) {
        String sql = "SELECT COALESCE(SUM(saldo), 0) AS total FROM cuentas_bancarias WHERE id_usuario = ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return 0.0;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getDouble("total");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    // ============================
    // TRANSACCIONES RECIENTES DEL MENOR
    // ============================
    public List<MovimientoCuenta> obtenerTransaccionesRecientes(int idMenor, int limite) {
        List<MovimientoCuenta> lista = new ArrayList<>();
        String sql = "SELECT tipo_transaccion, monto, moneda_origen, estado, creado_en " +
                     "FROM transacciones WHERE id_usuario_realizador = ? ORDER BY creado_en DESC LIMIT ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                ps.setInt(2, limite);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        MovimientoCuenta mov = new MovimientoCuenta();
                        mov.tipoTransaccion = rs.getString("tipo_transaccion");
                        mov.monto           = rs.getDouble("monto");
                        mov.moneda          = rs.getString("moneda_origen");
                        mov.estado          = rs.getString("estado");
                        mov.fecha           = rs.getTimestamp("creado_en");
                        lista.add(mov);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // ============================
    // SESIONES RECIENTES DEL MENOR
    // ============================
    public List<EventoSesion> obtenerSesionesRecientes(int idMenor, int limite) {
        List<EventoSesion> lista = new ArrayList<>();
        String sql = "SELECT tipo_evento, dispositivo, ocurrido_en FROM sesiones " +
                     "WHERE id_usuario = ? ORDER BY ocurrido_en DESC LIMIT ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                ps.setInt(2, limite);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        EventoSesion ev  = new EventoSesion();
                        ev.tipoEvento    = rs.getString("tipo_evento");
                        ev.dispositivo   = rs.getString("dispositivo");
                        ev.ocurridoEn    = rs.getTimestamp("ocurrido_en");
                        lista.add(ev);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // ============================
    // OBTENER ID DE SUPERVISIÓN
    // ============================
    public Integer obtenerIdSupervision(int idAdulto, int idMenor) {
        String sql = "SELECT id_supervision FROM supervisiones WHERE id_adulto = ? AND id_menor = ? AND activa = TRUE";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAdulto);
                ps.setInt(2, idMenor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("id_supervision");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ============================
    // OBTENER ID DE CUENTA DEL MENOR
    // ============================
    public Integer obtenerIdCuentaMenor(int idMenor) {
        String sql = "SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? LIMIT 1";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("id_cuenta");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ============================
    // GUARDAR O ACTUALIZAR LÍMITE
    // ============================
    public boolean guardarLimite(int idSupervision, int idCuenta, String tipoLimite, double montoMaximo) {
        String sqlCheck  = "SELECT id_limite FROM limites WHERE id_supervision = ? AND id_cuenta = ? AND tipo_limite = ?";
        String sqlUpdate = "UPDATE limites SET monto_maximo = ? WHERE id_supervision = ? AND id_cuenta = ? AND tipo_limite = ?";
        String sqlInsert = "INSERT INTO limites (id_supervision, id_cuenta, tipo_limite, monto_maximo) VALUES (?, ?, ?, ?)";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return false;
            boolean existe = false;
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idSupervision);
                psCheck.setInt(2, idCuenta);
                psCheck.setString(3, tipoLimite);
                try (ResultSet rs = psCheck.executeQuery()) { existe = rs.next(); }
            }
            if (existe) {
                try (PreparedStatement psUp = con.prepareStatement(sqlUpdate)) {
                    psUp.setDouble(1, montoMaximo);
                    psUp.setInt(2, idSupervision);
                    psUp.setInt(3, idCuenta);
                    psUp.setString(4, tipoLimite);
                    psUp.executeUpdate();
                }
            } else {
                try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                    psIns.setInt(1, idSupervision);
                    psIns.setInt(2, idCuenta);
                    psIns.setString(3, tipoLimite);
                    psIns.setDouble(4, montoMaximo);
                    psIns.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el límite: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    // ============================
    // LEER LÍMITES DE UN MENOR
    // ============================
    public List<LimiteCuenta> obtenerLimitesDeMenor(int idMenor) {
        List<LimiteCuenta> lista = new ArrayList<>();
        String sql = "SELECT l.id_limite, l.tipo_limite, l.monto_maximo FROM limites l " +
                     "JOIN supervisiones s ON s.id_supervision = l.id_supervision " +
                     "WHERE s.id_menor = ? AND s.activa = TRUE";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        LimiteCuenta lim  = new LimiteCuenta();
                        lim.idLimite      = rs.getInt("id_limite");
                        lim.tipoLimite    = rs.getString("tipo_limite");
                        lim.montoMaximo   = rs.getDouble("monto_maximo");
                        lista.add(lim);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // ============================
    // VALIDAR LÍMITE
    // ============================
    public String validarLimite(int idMenor, double montoIntentado) {
        List<LimiteCuenta> limites = obtenerLimitesDeMenor(idMenor);
        for (LimiteCuenta lim : limites) {
            if ("por_transaccion".equals(lim.tipoLimite) && montoIntentado > lim.montoMaximo)
                return String.format("El monto Q%.2f supera el límite por transacción de Q%.2f configurado por tu tutor.", montoIntentado, lim.montoMaximo);
            if ("diario".equals(lim.tipoLimite)) {
                double gastado = obtenerGastadoEnPeriodo(idMenor, "DAY");
                if (gastado + montoIntentado > lim.montoMaximo)
                    return String.format("Supera el límite diario de Q%.2f. Ya gastaste Q%.2f hoy.", lim.montoMaximo, gastado);
            }
            if ("semanal".equals(lim.tipoLimite)) {
                double gastado = obtenerGastadoEnPeriodo(idMenor, "WEEK");
                if (gastado + montoIntentado > lim.montoMaximo)
                    return String.format("Supera el límite semanal de Q%.2f. Ya gastaste Q%.2f esta semana.", lim.montoMaximo, gastado);
            }
            if ("mensual".equals(lim.tipoLimite)) {
                double gastado = obtenerGastadoEnPeriodo(idMenor, "MONTH");
                if (gastado + montoIntentado > lim.montoMaximo)
                    return String.format("Supera el límite mensual de Q%.2f. Ya gastaste Q%.2f este mes.", lim.montoMaximo, gastado);
            }
        }
        return null; // null = sin problemas, puede proceder
    }

    private double obtenerGastadoEnPeriodo(int idMenor, String periodo) {
        String sql = "SELECT COALESCE(SUM(monto), 0) AS total FROM transacciones " +
                     "WHERE id_usuario_realizador = ? AND estado = 'completada' " +
                     "AND creado_en >= DATE_SUB(NOW(), INTERVAL 1 " + periodo + ")";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return 0.0;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getDouble("total");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    // ============================
    // OBTENER ADULTO/TUTOR DE UN MENOR
    // ============================
    // Alias antiguo — mantenido para compatibilidad con código existente
    public Integer obtenerIdTutorDeMenor(int idMenor) {
        return obtenerIdAdultoDeMenor(idMenor);
    }

    // Nombre correcto alineado con la columna id_adulto de la tabla supervisiones
    public Integer obtenerIdAdultoDeMenor(int idMenor) {
        String sql = "SELECT id_adulto FROM supervisiones WHERE id_menor = ? AND activa = TRUE LIMIT 1";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("id_adulto");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ============================
    // CREAR SOLICITUD DE PERMISO
    // ============================
    // CORREGIDO: columna id_adulto (no id_tutor) según NeoJacketDB
    public boolean crearSolicitudPermiso(int idMenor, int idAdulto, String tipoAccion, String descripcion, double monto) {
        String sql = "INSERT INTO solicitudes_permiso (id_menor, id_adulto, tipo_accion, descripcion, monto, estado) " +
                     "VALUES (?, ?, ?, ?, ?, 'pendiente')";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                ps.setInt(2, idAdulto);
                ps.setString(3, tipoAccion);
                ps.setString(4, descripcion);
                ps.setDouble(5, monto);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al enviar la solicitud: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    // ============================
    // LISTAR SOLICITUDES PENDIENTES DEL TUTOR
    // ============================
    // CORREGIDO: columna id_adulto y fecha_respuesta según NeoJacketDB
    public List<SolicitudPermiso> listarSolicitudesPendientes(int idAdulto) {
        List<SolicitudPermiso> lista = new ArrayList<>();
        String sql = "SELECT sp.id_solicitud, sp.id_menor, sp.id_adulto, sp.tipo_accion, " +
                     "sp.descripcion, sp.monto, sp.estado, sp.fecha_solicitud, " +
                     "CONCAT(u.nombre, ' ', u.apellido) AS nombre_menor " +
                     "FROM solicitudes_permiso sp " +
                     "JOIN usuarios u ON u.id_usuario = sp.id_menor " +
                     "WHERE sp.id_adulto = ? AND sp.estado = 'pendiente' " +
                     "ORDER BY sp.fecha_solicitud DESC";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAdulto);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        SolicitudPermiso s = new SolicitudPermiso();
                        s.idSolicitud  = rs.getInt("id_solicitud");
                        s.idMenor      = rs.getInt("id_menor");
                        s.idAdulto     = rs.getInt("id_adulto");
                        s.tipoAccion   = rs.getString("tipo_accion");
                        s.descripcion  = rs.getString("descripcion");
                        s.monto        = rs.getDouble("monto");
                        s.estado       = rs.getString("estado");
                        s.creadoEn     = rs.getTimestamp("fecha_solicitud");
                        s.nombreMenor  = rs.getString("nombre_menor");
                        lista.add(s);
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // ============================
    // RESPONDER SOLICITUD (APROBAR O RECHAZAR)
    // ============================
    // CORREGIDO: columna fecha_respuesta (no respondido_en) según NeoJacketDB
    public boolean responderSolicitud(int idSolicitud, String decision) {
        String sql = "UPDATE solicitudes_permiso SET estado = ?, fecha_respuesta = NOW() WHERE id_solicitud = ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, decision); // "aprobada" o "rechazada"
                ps.setInt(2, idSolicitud);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al responder la solicitud: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    // ============================
    // VERIFICAR SI UNA ACCIÓN TIENE PERMISO APROBADO
    // ============================
    // CORREGIDO: columna fecha_respuesta (no respondido_en) según NeoJacketDB
    public boolean tienePermisoAprobado(int idMenor, String tipoAccion) {
        String sql = "SELECT COUNT(*) AS total FROM solicitudes_permiso " +
                     "WHERE id_menor = ? AND tipo_accion = ? AND estado = 'aprobada' " +
                     "AND fecha_respuesta >= DATE_SUB(NOW(), INTERVAL 24 HOUR)";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                ps.setString(2, tipoAccion);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ============================
    // PUNTO 3 — REGISTRAR ACTIVIDAD DEL MENOR EN TABLA SESIONES
    // Se llama al iniciar sesión y al intentar cualquier acción
    // Usa el ENUM ya existente en tu tabla sesiones de NeoJacketDB
    // ============================
    public void registrarSesionMenor(int idMenor, String tipoEvento) {
        // tipoEvento válidos según tu ENUM en tabla sesiones:
        // 'inicio_sesion', 'cierre_sesion', 'fallo_password',
        // 'fallo_usuario_inexistente', 'fallo_cuenta_bloqueada', 'sesion_expirada'
        String sql = "INSERT INTO sesiones (id_usuario, tipo_evento, dispositivo) VALUES (?, ?, ?)";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                ps.setString(2, tipoEvento);
                ps.setString(3, "Neo Jacket Desktop");
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
    // PUNTO 3 — REGISTRAR INTENTO DE ACCIÓN BLOQUEADA
    // Llama esto desde mostrarFuncionBloqueada() en DashboardMenor
    // Guarda en auditoria_logs para que el tutor lo vea
    // ============================
    public void registrarAccionBloqueada(int idMenor, String accion) {
        String sql = "INSERT INTO auditoria_logs (id_admin, accion, detalle) VALUES (?, ?, ?)";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMenor);
                ps.setString(2, "accion_bloqueada_menor");
                ps.setString(3, "El menor intentó acceder a: " + accion);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
    // PUNTO 3 — OBTENER ACTIVIDAD RECIENTE DEL MENOR (para DashboardTutor)
    // Combina sesiones + auditoria_logs en una lista unificada
    // ============================
    public List<String[]> obtenerActividadMenor(int idMenor, int limite) {
        List<String[]> lista = new ArrayList<>();

        // Sesiones del menor
        String sqlSesiones = "SELECT tipo_evento, dispositivo, ocurrido_en " +
                             "FROM sesiones WHERE id_usuario = ? " +
                             "ORDER BY ocurrido_en DESC LIMIT ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sqlSesiones)) {
                ps.setInt(1, idMenor);
                ps.setInt(2, limite);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new String[]{
                            rs.getString("tipo_evento"),
                            rs.getString("dispositivo") != null ? rs.getString("dispositivo") : "—",
                            rs.getString("ocurrido_en")
                        });
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        // Acciones bloqueadas del menor
        String sqlAuditoria = "SELECT accion, detalle, creado_en " +
                              "FROM auditoria_logs WHERE id_admin = ? " +
                              "AND accion = 'accion_bloqueada_menor' " +
                              "ORDER BY creado_en DESC LIMIT ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sqlAuditoria)) {
                ps.setInt(1, idMenor);
                ps.setInt(2, limite);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new String[]{
                            rs.getString("accion"),
                            rs.getString("detalle"),
                            rs.getString("creado_en")
                        });
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return lista;
    }

    // Verifica si un usuario es menor supervisado por el campo perfil en la tabla usuarios
    public boolean esMenorSupervisado(int idUsuario) {
        String sql = "SELECT perfil FROM usuarios WHERE id_usuario = ?";
        try (Connection con = conexion.getConexion()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return "Menor supervisado".equals(rs.getString("perfil"));
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}