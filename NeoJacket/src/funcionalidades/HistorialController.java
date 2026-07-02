
package funcionalidades;

import main.Conexion.conexion;
import gui.Historial;
import java.sql.*;
import java.text.SimpleDateFormat;

public class HistorialController {

    private Historial vista;

    // Constructor limpio
    public HistorialController(Historial vista) {
        this.vista = vista;
    }

    /**
     * Coordina la carga completa de datos usando el ID automático
     */
    public void cargarDatosPantalla() {
        Connection con = conexion.getConexion();
        if (con == null) return;

        // Recuperamos el ID automáticamente sin pasarlo por constructor
        int idUsuario = funcionalidades.UsuarioSesion.idUsuarioLogueado;

        cargarBloqueMisCuentas(con, idUsuario);
        cargarBloqueResumenFinanciero(con, idUsuario);
        cargarBloqueBancosConectados(con, idUsuario);
        cargarBloqueTransferenciasRecientes(con, idUsuario);
        cargarBloqueDivisas(con);
        cargarBloqueHistorialActividad(con, idUsuario);
    }

    private void cargarBloqueMisCuentas(Connection con, int idUsuario) {
        String sql = "SELECT tc.nombre, cb.numero_cuenta, cb.saldo, cb.moneda " +
                     "FROM cuentas_bancarias cb " +
                     "JOIN tipos_cuentas tc ON cb.id_tipo_cuenta = tc.id_tipo " +
                     "WHERE cb.id_usuario = ? LIMIT 2";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                int ctaContador = 1;
                while (rs.next()) {
                    String tipo = rs.getString("nombre");
                    String num = rs.getString("numero_cuenta");
                    double saldo = rs.getDouble("saldo");
                    String moneda = rs.getString("moneda");
                    String formato = String.format("%s - %s: %s %,.2f", tipo, num, moneda, saldo);
                    
                    if (ctaContador == 1) vista.lblCtaPrincipal.setText(formato);
                    else if (ctaContador == 2) vista.lblCtaAhorros.setText(formato);
                    ctaContador++;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void cargarBloqueResumenFinanciero(Connection con, int idUsuario) {
        String sqlSaldo = "SELECT SUM(saldo) AS total FROM cuentas_bancarias WHERE id_usuario = ?";
        String sqlFlujos = "SELECT " +
                           "SUM(CASE WHEN id_cuenta_destino IN (SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ?) OR tipo_transaccion = 'deposito' THEN monto ELSE 0 END) AS ingresos, " +
                           "SUM(CASE WHEN id_cuenta_origen IN (SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ?) OR tipo_transaccion = 'retiro' THEN monto ELSE 0 END) AS gastos " +
                           "FROM transacciones " +
                           "WHERE estado = 'completada' AND MONTH(creado_en) = MONTH(CURRENT_DATE()) AND YEAR(creado_en) = YEAR(CURRENT_DATE())";
        try {
            try (PreparedStatement ps = con.prepareStatement(sqlSaldo)) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) vista.lblSaldoDisponible.setText(String.format("Saldo disponible total: GTQ %,.2f", rs.getDouble("total")));
                }
            }
            vista.lblSaldoRetenido.setText("Saldo retenido: GTQ 0.00");

            try (PreparedStatement ps = con.prepareStatement(sqlFlujos)) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        vista.lblIngresosMes.setText(String.format("Ingresos del mes: GTQ %,.2f", rs.getDouble("incresos")));
                        vista.lblGastosMes.setText(String.format("Gastos del mes: GTQ %,.2f", rs.getDouble("gastos")));
                    }
                }
            }
            vista.lblUltimaActResumen.setText("Última actualización: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void cargarBloqueBancosConectados(Connection con, int idUsuario) {
        String sql = "SELECT DISTINCT b.nombre_corto FROM cuentas_bancarias cb " +
                     "JOIN bancos b ON cb.id_banco = b.id_banco WHERE cb.id_usuario = ? LIMIT 3";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) vista.lblBanco1.setText("✔ Enlazado: " + rs.getString("nombre_corto"));
                if (rs.next()) vista.lblBanco2.setText("✔ Enlazado: " + rs.getString("nombre_corto"));
                if (rs.next()) vista.lblBanco3.setText("✔ Enlazado: " + rs.getString("nombre_corto"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void cargarBloqueTransferenciasRecientes(Connection con, int idUsuario) {
        String sql = "SELECT t.monto, t.moneda_origen, t.estado, cb.numero_cuenta FROM transacciones t " +
                     "JOIN cuentas_bancarias cb ON t.id_cuenta_destino = cb.id_cuenta " +
                     "WHERE t.id_usuario_realizador = ? AND t.tipo_transaccion = 'transferencia' " +
                     "ORDER BY t.creado_en DESC LIMIT 4";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                int i = 1;
                while (rs.next()) {
                    String item = String.format("A Cta: *%s | %s %,.2f [%s]", 
                            rs.getString("numero_cuenta").substring(Math.max(0, rs.getString("numero_cuenta").length() - 4)),
                            rs.getString("moneda_origen"), rs.getDouble("monto"), rs.getString("estado").toUpperCase());
                    if (i == 1) vista.lblTransReciente1.setText(item);
                    else if (i == 2) vista.lblTransReciente2.setText(item);
                    else if (i == 3) vista.lblTransReciente3.setText(item);
                    else if (i == 4) vista.lblTransReciente4.setText(item);
                    i++;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void cargarBloqueDivisas(Connection con) {
        String sql = "SELECT moneda_destino, tasa, actualizado_en FROM tipos_cambio WHERE moneda_origen = 'GTQ'";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String dest = rs.getString("moneda_destino");
                double tasa = rs.getDouble("tasa");
                if ("USD".equals(dest)) vista.lblDivUSD.setText(String.format("USD / GTQ   ➡   %,.2f", tasa));
                else if ("EUR".equals(dest)) vista.lblDivEUR.setText(String.format("EUR / GTQ   ➡   %,.2f", tasa));
                vista.lblUltimaActDivisas.setText("Actualizado: " + new SimpleDateFormat("dd/MM/yyyy").format(rs.getTimestamp("actualizado_en")));
            }
            vista.lblDivMXN.setText("MXN / GTQ   ➡   0.38");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void cargarBloqueHistorialActividad(Connection con, int idUsuario) {
        String sql = "SELECT tipo_transaccion, monto, moneda_origen, estado, creado_en FROM transacciones " +
                     "WHERE id_usuario_realizador = ? ORDER BY creado_en DESC LIMIT 4";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy - HH:mm");
                if (rs.next()) {
                    vista.lblHist1_Titulo.setText(String.format("%s de %s %,.2f", rs.getString("tipo_transaccion").toUpperCase(), rs.getString("moneda_origen"), rs.getDouble("monto")));
                    vista.lblHist1_Meta.setText(String.format("Fecha: %s | Estado: %s", fmt.format(rs.getTimestamp("creado_en")), rs.getString("estado")));
                }
                if (rs.next()) {
                    vista.lblHist2_Titulo.setText(String.format("%s de %s %,.2f", rs.getString("tipo_transaccion").toUpperCase(), rs.getString("moneda_origen"), rs.getDouble("monto")));
                    vista.lblHist2_Meta.setText(String.format("Fecha: %s | Estado: %s", fmt.format(rs.getTimestamp("creado_en")), rs.getString("estado")));
                }
                if (rs.next()) {
                    vista.lblHist3_Titulo.setText(String.format("%s de %s %,.2f", rs.getString("tipo_transaccion").toUpperCase(), rs.getString("moneda_origen"), rs.getDouble("monto")));
                    vista.lblHist3_Meta.setText(String.format("Fecha: %s | Estado: %s", fmt.format(rs.getTimestamp("creado_en")), rs.getString("estado")));
                }
                if (rs.next()) {
                    vista.lblHist4_Titulo.setText(String.format("%s de %s %,.2f", rs.getString("tipo_transaccion").toUpperCase(), rs.getString("moneda_origen"), rs.getDouble("monto")));
                    vista.lblHist4_Meta.setText(String.format("Fecha: %s | Estado: %s", fmt.format(rs.getTimestamp("creado_en")), rs.getString("estado")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}