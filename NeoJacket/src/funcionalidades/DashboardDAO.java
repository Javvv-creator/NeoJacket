package funcionalidades;

import main.Conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Centraliza todas las consultas necesarias para poblar el Dashboard del
 * usuario con datos reales: nombre, saldo, cuentas, bancos conectados,
 * transferencias y actividad reciente.
 */
public class DashboardDAO {

    /**
     * Representa una fila de la tabla `transacciones`, usada tanto para
     * "Movimientos Recientes" (mini gráfica) como para "Actividad Reciente".
     */
    public static class Movimiento {
        public String tipo;      // deposito / transferencia / actualizacion / retiro
        public double monto;
        public String moneda;
        public String estado;
        public Timestamp fecha;
    }

    // ==========================================
    // NOMBRE DEL USUARIO
    // ==========================================
    public String obtenerNombreCompleto(int idUsuario) {
        String sql = "SELECT nombre, apellido FROM usuarios WHERE id_usuario = ?";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Usuario";
    }

    // ==========================================
    // SALDO TOTAL (suma de todas las cuentas activas del usuario)
    // ==========================================
    public double obtenerSaldoTotal(int idUsuario) {
        String sql = "SELECT COALESCE(SUM(saldo), 0) AS total FROM cuentas_bancarias "
                + "WHERE id_usuario = ? AND estado = 'activa'";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ==========================================
    // NÚMERO DE CUENTAS ACTIVAS
    // ==========================================
    public int obtenerNumeroCuentas(int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM cuentas_bancarias "
                + "WHERE id_usuario = ? AND estado = 'activa'";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ==========================================
    // NÚMERO DE TRANSFERENCIAS REALIZADAS
    // ==========================================
    public int obtenerNumeroTransferencias(int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM transacciones "
                + "WHERE id_usuario_realizador = ? AND tipo_transaccion = 'transferencia'";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ==========================================
    // NÚMERO DE CUENTA PRINCIPAL (la más antigua registrada)
    // ==========================================
    public String obtenerNumeroCuentaPrincipal(int idUsuario) {
        String sql = "SELECT numero_cuenta FROM cuentas_bancarias "
                + "WHERE id_usuario = ? AND estado = 'activa' ORDER BY id_cuenta ASC LIMIT 1";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numero_cuenta");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================
    // NÚMERO DE TARJETA PRINCIPAL (la más antigua activa)
    // Se deja por compatibilidad, pero el Dashboard ahora usa
    // obtenerNumerosTarjetasActivas() para mostrar TODAS las tarjetas.
    // ==========================================
    public String obtenerNumeroTarjetaPrincipal(int idUsuario) {
        String sql = "SELECT numero_tarjeta FROM tarjetas_bancarias "
                + "WHERE id_usuario = ? AND estado = 'activa' ORDER BY id_tarjeta ASC LIMIT 1";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("numero_tarjeta");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================
    // ID DE LA TARJETA PRINCIPAL (la más antigua activa). Se usa para poder
    // abrir DetalleTarjetaDashboard(idTarjeta, ...) desde cualquier menú.
    // Devuelve -1 si el usuario todavía no tiene ninguna tarjeta.
    // ==========================================
    public int obtenerIdTarjetaPrincipal(int idUsuario) {
        String sql = "SELECT id_tarjeta FROM tarjetas_bancarias "
                + "WHERE id_usuario = ? AND estado = 'activa' ORDER BY id_tarjeta ASC LIMIT 1";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_tarjeta");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ==========================================
    // TODAS LAS TARJETAS ACTIVAS DEL USUARIO (en orden de creación)
    // Se usa para pintar una fila con botón "Copiar" por cada tarjeta
    // que el usuario haya agregado, hasta un máximo de 4.
    // ==========================================
    public List<String> obtenerNumerosTarjetasActivas(int idUsuario) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT numero_tarjeta FROM tarjetas_bancarias "
                + "WHERE id_usuario = ? AND estado = 'activa' ORDER BY id_tarjeta ASC LIMIT 4";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString("numero_tarjeta"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // MOVIMIENTOS RECIENTES (solo transferencias) — para la mini gráfica
    // ==========================================
    public List<Movimiento> obtenerMovimientosRecientes(int idUsuario, int limite) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT tipo_transaccion, monto, moneda_origen, estado, creado_en FROM transacciones "
                + "WHERE id_usuario_realizador = ? AND tipo_transaccion = 'transferencia' "
                + "ORDER BY creado_en DESC LIMIT ?";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // ACTIVIDAD RECIENTE (todo tipo de transacción) — para el feed
    // ==========================================
    public List<Movimiento> obtenerActividadReciente(int idUsuario, int limite) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT tipo_transaccion, monto, moneda_origen, estado, creado_en FROM transacciones "
                + "WHERE id_usuario_realizador = ? "
                + "ORDER BY creado_en DESC LIMIT ?";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        Movimiento m = new Movimiento();
        m.tipo = rs.getString("tipo_transaccion");
        m.monto = rs.getDouble("monto");
        m.moneda = rs.getString("moneda_origen");
        m.estado = rs.getString("estado");
        m.fecha = rs.getTimestamp("creado_en");
        return m;
    }
}