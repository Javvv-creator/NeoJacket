package funcionalidades;

import gui.Historial;
import java.sql.*;
import java.text.SimpleDateFormat;
import main.Conexion.conexion;

public class HistorialController {

    private final Historial vista;

    public HistorialController(Historial vista) {
        this.vista = vista;
    }

    /**
     * Coordina la carga completa de datos del usuario en sesión.
     */
    public void cargarDatosPantalla() {
        int idUsuario = SesionUsuario.getIdUsuario();
        if (idUsuario <= 0) {
            // No hay sesión válida: no se intenta consultar nada.
            return;
        }

        // Una sola conexión para toda la carga de la pantalla, cerrada al final.
        try (Connection con = conexion.getConexion()) {
            if (con == null) {
                return;
            }
            cargarBloqueMisCuentas(con, idUsuario);
            cargarBloqueResumenFinanciero(con, idUsuario);
            cargarBloqueBancosConectados(con, idUsuario);
            cargarBloqueTransferenciasRecientes(con, idUsuario);
            cargarBloqueDivisas(con, idUsuario);
            cargarBloqueHistorialActividad(con, idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarBloqueMisCuentas(Connection con, int idUsuario) {
        String sql = "SELECT tc.nombre, cb.numero_cuenta, cb.saldo, cb.moneda "
                + "FROM cuentas_bancarias cb "
                + "JOIN tipos_cuentas tc ON cb.id_tipo_cuenta = tc.id_tipo "
                + "WHERE cb.id_usuario = ? AND cb.estado = 'activa' "
                + "ORDER BY cb.id_cuenta ASC LIMIT 2";
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

                    if (ctaContador == 1 && vista.lblCtaPrincipal != null) {
                        vista.lblCtaPrincipal.setText(formato);
                    } else if (ctaContador == 2 && vista.lblCtaAhorros != null) {
                        vista.lblCtaAhorros.setText(formato);
                    }
                    ctaContador++;
                }
                if (ctaContador == 1 && vista.lblCtaPrincipal != null) {
                    vista.lblCtaPrincipal.setText("Aún no tienes cuentas registradas");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarBloqueResumenFinanciero(Connection con, int idUsuario) {
        String sqlSaldo = "SELECT COALESCE(SUM(saldo), 0) AS total FROM cuentas_bancarias "
                + "WHERE id_usuario = ? AND estado = 'activa'";
        // id_usuario_realizador siempre es quien EJECUTA la transacción, así que:
        //  - 'deposito'      -> entra dinero a su cuenta  -> ingreso
        //  - 'retiro'        -> sale dinero de su cuenta   -> gasto
        //  - 'transferencia' -> sale dinero de su cuenta origen -> gasto
        // (antes se usaba "id_cuenta_origen IN (sus cuentas)" como condición de gasto,
        // pero un depósito TAMBIÉN usa id_cuenta_origen para la cuenta que recibe el
        // dinero, así que los depósitos se contaban por error como gasto además de ingreso)
        String sqlFlujos = "SELECT "
                + "SUM(CASE WHEN tipo_transaccion = 'deposito' THEN monto ELSE 0 END) AS ingresos, "
                + "SUM(CASE WHEN tipo_transaccion IN ('retiro', 'transferencia') THEN monto ELSE 0 END) AS gastos "
                + "FROM transacciones "
                + "WHERE id_usuario_realizador = ? AND estado = 'completada' "
                + "AND MONTH(creado_en) = MONTH(CURRENT_DATE()) AND YEAR(creado_en) = YEAR(CURRENT_DATE())";

        try (PreparedStatement ps = con.prepareStatement(sqlSaldo)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && vista.lblSaldoDisponible != null) {
                    vista.lblSaldoDisponible.setText(String.format("Saldo disponible total: GTQ %,.2f", rs.getDouble("total")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement ps = con.prepareStatement(sqlFlujos)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (vista.lblIngresosMes != null) {
                        vista.lblIngresosMes.setText(String.format("Ingresos del mes: GTQ %,.2f", rs.getDouble("ingresos")));
                    }
                    if (vista.lblGastosMes != null) {
                        vista.lblGastosMes.setText(String.format("Gastos del mes: GTQ %,.2f", rs.getDouble("gastos")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (vista.lblUltimaActResumen != null) {
            vista.lblUltimaActResumen.setText("Última actualización: "
                    + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        }
    }

    private void cargarBloqueBancosConectados(Connection con, int idUsuario) {
        String sql = "SELECT DISTINCT b.nombre FROM cuentas_bancarias cb "
                + "JOIN bancos b ON cb.id_banco = b.id_banco "
                + "WHERE cb.id_usuario = ? AND cb.estado = 'activa' LIMIT 3";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                boolean alMenosUno = false;
                if (rs.next() && vista.lblBanco1 != null) {
                    vista.lblBanco1.setText("✔ Enlazado: " + rs.getString("nombre"));
                    alMenosUno = true;
                }
                if (rs.next() && vista.lblBanco2 != null) {
                    vista.lblBanco2.setText("✔ Enlazado: " + rs.getString("nombre"));
                }
                if (rs.next() && vista.lblBanco3 != null) {
                    vista.lblBanco3.setText("✔ Enlazado: " + rs.getString("nombre"));
                }
                if (!alMenosUno && vista.lblBanco1 != null) {
                    vista.lblBanco1.setText("Aún no tienes bancos conectados");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarBloqueTransferenciasRecientes(Connection con, int idUsuario) {
        // LEFT JOIN: cuando el destino es una cuenta externa (no registrada en el
        // sistema), funcionalidades.Transferencias guarda id_cuenta_destino = NULL
        // a propósito. Con INNER JOIN esas filas se perdían por completo.
        String sql = "SELECT t.monto, t.moneda_origen, t.estado, cb.numero_cuenta FROM transacciones t "
                + "LEFT JOIN cuentas_bancarias cb ON t.id_cuenta_destino = cb.id_cuenta "
                + "WHERE t.id_usuario_realizador = ? AND t.tipo_transaccion = 'transferencia' "
                + "ORDER BY t.creado_en DESC LIMIT 4";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                JLabelArraySetter setter = new JLabelArraySetter(
                        vista.lblTransReciente1, vista.lblTransReciente2,
                        vista.lblTransReciente3, vista.lblTransReciente4);
                int i = 0;
                while (rs.next() && i < 4) {
                    String numero = rs.getString("numero_cuenta");
                    String destinoTexto = numero != null && numero.length() >= 4
                            ? "*" + numero.substring(numero.length() - 4)
                            : "Cuenta externa";
                    String item = String.format("A %s | %s %,.2f [%s]",
                            destinoTexto, rs.getString("moneda_origen"), rs.getDouble("monto"),
                            rs.getString("estado").toUpperCase());
                    setter.set(i, item);
                    i++;
                }
                if (i == 0) {
                    setter.set(0, "Aún no tienes transferencias registradas");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarBloqueDivisas(Connection con, int idUsuario) {
        // "Cambio de Divisas" ahora muestra el ÚLTIMO cambio de divisa que el
        // usuario realmente usó en una transferencia (moneda_origen / moneda_destino /
        // tasa_cambio_historica quedan guardados ahí por funcionalidades.Transferencias),
        // en vez de depender de la tabla tipos_cambio que no se está usando.
        String sql = "SELECT moneda_origen, moneda_destino, tasa_cambio_historica, monto, creado_en "
                + "FROM transacciones "
                + "WHERE id_usuario_realizador = ? AND tipo_transaccion = 'transferencia' "
                + "AND moneda_origen IS NOT NULL AND moneda_destino IS NOT NULL "
                + "ORDER BY creado_en DESC LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String mO = rs.getString("moneda_origen");
                    String mD = rs.getString("moneda_destino");
                    double tasa = rs.getDouble("tasa_cambio_historica");
                    double monto = rs.getDouble("monto");
                    Timestamp fecha = rs.getTimestamp("creado_en");

                    if (vista.lblDivUSD != null) {
                        vista.lblDivUSD.setText("Moneda origen:   " + mO);
                    }
                    if (vista.lblDivEUR != null) {
                        vista.lblDivEUR.setText("Moneda destino:  " + mD);
                    }
                    if (vista.lblDivMXN != null) {
                        vista.lblDivMXN.setText(String.format("Tasa aplicada:   1 %s = %.4f %s", mO, tasa, mD));
                    }
                    if (vista.lblUltimaActDivisas != null) {
                        vista.lblUltimaActDivisas.setText("Realizado: "
                                + (fecha != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha) : "--")
                                + String.format("  |  Monto: %,.2f %s", monto, mO));
                    }
                } else {
                    if (vista.lblDivUSD != null) vista.lblDivUSD.setText("Aún no has hecho ningún cambio de divisa");
                    if (vista.lblDivEUR != null) vista.lblDivEUR.setText("");
                    if (vista.lblDivMXN != null) vista.lblDivMXN.setText("");
                    if (vista.lblUltimaActDivisas != null) vista.lblUltimaActDivisas.setText("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarBloqueHistorialActividad(Connection con, int idUsuario) {
        String sql = "SELECT tipo_transaccion, monto, moneda_origen, estado, creado_en FROM transacciones "
                + "WHERE id_usuario_realizador = ? ORDER BY creado_en DESC LIMIT 4";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy - HH:mm");
                javax.swing.JLabel[] titulos = {vista.lblHist1_Titulo, vista.lblHist2_Titulo, vista.lblHist3_Titulo, vista.lblHist4_Titulo};
                javax.swing.JLabel[] metas = {vista.lblHist1_Meta, vista.lblHist2_Meta, vista.lblHist3_Meta, vista.lblHist4_Meta};

                int i = 0;
                while (rs.next() && i < 4) {
                    if (titulos[i] != null) {
                        titulos[i].setText(String.format("%s de %s %,.2f",
                                rs.getString("tipo_transaccion").toUpperCase(),
                                rs.getString("moneda_origen"), rs.getDouble("monto")));
                    }
                    if (metas[i] != null) {
                        metas[i].setText(String.format("Fecha: %s | Estado: %s",
                                fmt.format(rs.getTimestamp("creado_en")), rs.getString("estado")));
                    }
                    i++;
                }
                if (i == 0 && titulos[0] != null) {
                    titulos[0].setText("Sin actividad registrada todavía");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pequeño helper para asignar texto a un arreglo de labels por índice
     * sin repetir if/else en cada bloque.
     */
    private static class JLabelArraySetter {
        private final javax.swing.JLabel[] labels;

        JLabelArraySetter(javax.swing.JLabel... labels) {
            this.labels = labels;
        }

        void set(int index, String texto) {
            if (index >= 0 && index < labels.length && labels[index] != null) {
                labels[index].setText(texto);
            }
        }
    }
}