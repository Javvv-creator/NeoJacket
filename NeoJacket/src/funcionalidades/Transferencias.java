package funcionalidades;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Map;
import javax.swing.JOptionPane;
import main.Conexion.conexion; 

public class Transferencias {

    private final gui.Transferencias vista;
    
    // Control de IDs internos de la base de datos
    private int idCuentaOrigenValida = -1;
    private int idCuentaDestinoValida = -1;
    private int idUsuarioRealizador = -1; 
    
    // Variables de estado financiero
    private double saldoOrigenActual = 0.00;
    private double tasaCambioActual = 1.000000;
    private double montoDestinoCalculado = 0.00;

    public Transferencias(gui.Transferencias vista) {
        this.vista = vista;
        inicializarListeners();
        cargarDatosIniciales();
    }

    private void inicializarListeners() {
        // BLOQUE 1: Datos de Origen (Búsqueda por Cuenta o Tarjeta)
        vista.txtNumCuentaO.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarCuentaOrigen();
            }
        });
        vista.cmbSelBancoO.addActionListener(e -> buscarCuentaOrigen());
        vista.cmbTipoCuentaO.addActionListener(e -> buscarCuentaOrigen());

        // BLOQUE 2: Datos de Destino 
        vista.txtNumCuentaD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarCuentaDestino();
            }
        });
        vista.cmbSelBancoD.addActionListener(e -> buscarCuentaDestino());
        vista.cmbTipoCuentaD.addActionListener(e -> buscarCuentaDestino());

        // BLOQUE 3: Montos, Divisas y Resumen
        vista.txtMonto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarConversionesYResumen();
            }
        });
        vista.cmbMonedaO.addActionListener(e -> calcularTasaCambio());
        vista.cmbMonedaD.addActionListener(e -> calcularTasaCambio());

        // Botones de Acción
        vista.btnTransferir.addActionListener(e -> ejecutarTransferencia());
        vista.btnCancelar.addActionListener(e -> limpiarCampos());
    }

    private void cargarDatosIniciales() {
        calcularTasaCambio();
    }

    /**
     * BLOQUE 1: Busca la cuenta origen filtrando por el Banco seleccionado.
     * Permite ingresar tanto el Número de Cuenta como el Número de Tarjeta para facilitar la exposición.
     */
    private void buscarCuentaOrigen() {
        String entradaInput = vista.txtNumCuentaO.getText().trim();
        String banco = (String) vista.cmbSelBancoO.getSelectedItem();
        String tipo = (String) vista.cmbTipoCuentaO.getSelectedItem();

        if (entradaInput.isEmpty()) {
            vista.lblSaldoO.setText("Saldo: Q0.00");
            vista.lblO_Cuenta.setText("Cuenta: --");
            vista.lblO_Banco.setText("Banco: --");
            vista.lblO_Tipo.setText("Tipo: --");
            vista.lblTitularD.setText("Titular: --"); 
            idCuentaOrigenValida = -1;
            idUsuarioRealizador = -1;
            return;
        }

        // QUERY UNIFICADA: Busca el número directamente tanto en la cuenta como en la tarjeta
        // garantizando que funcionen como el mismo identificador para la exposición.
        String query = "SELECT c.id_cuenta, c.saldo, c.moneda, c.numero_cuenta, u.id_usuario, u.nombre, u.apellido " +
                       "FROM cuentas_bancarias c " +
                       "JOIN bancos b ON c.id_banco = b.id_banco " +
                       "JOIN tipos_cuentas tc ON c.id_tipo_cuenta = tc.id_tipo " +
                       "JOIN usuarios u ON c.id_usuario = u.id_usuario " +
                       "LEFT JOIN tarjetas_bancarias t ON c.id_cuenta = t.id_cuenta " +
                       "WHERE (c.numero_cuenta = ? OR t.numero_tarjeta = ?) " +
                       "AND b.nombre = ? AND tc.nombre = ? AND c.estado = 'activa'";

        try (Connection con = conexion.getConexion(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, entradaInput);
            ps.setString(2, entradaInput);
            ps.setString(3, banco);
            ps.setString(4, tipo);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idCuentaOrigenValida = rs.getInt("id_cuenta");
                idUsuarioRealizador  = rs.getInt("id_usuario"); 
                saldoOrigenActual = rs.getDouble("saldo");
                String monedaReal = rs.getString("moneda");
                String nombreTitular = rs.getString("nombre") + " " + rs.getString("apellido");

                // Sincroniza la moneda del ComboBox automáticamente
                if(vista.cmbMonedaO.getSelectedItem() != null && !vista.cmbMonedaO.getSelectedItem().equals(monedaReal)) {
                    vista.cmbMonedaO.setSelectedItem(monedaReal);
                }

                // Muestra el número ingresado (el de la tarjeta/cuenta) directamente en la interfaz
                vista.lblSaldoO.setText("Saldo: " + monedaReal + " " + String.format("%.2f", saldoOrigenActual));
                vista.lblO_Cuenta.setText("Cuenta: " + entradaInput); // Aquí forzamos que use el mismo número visible
                vista.lblO_Nombre.setText("Nombre: " + nombreTitular);
                vista.lblO_Tipo.setText("Tipo: " + tipo);
                vista.lblO_Banco.setText("Banco: " + banco);
                
                // Muestra el nombre de quien hace la transferencia en el Bloque 2
                vista.lblTitularD.setText("Titular: " + nombreTitular);
                
            } else {
                idCuentaOrigenValida = -1;
                idUsuarioRealizador = -1;
                vista.lblSaldoO.setText("No encontrado en " + banco);
                vista.lblO_Cuenta.setText("Cuenta: NO EXISTE");
                vista.lblTitularD.setText("Titular: --");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        actualizarConversionesYResumen();
    }

    /**
     * BLOQUE 2: Busca los datos de destino.
     * Requerimiento: No importa si está registrada o no en la BD, la transferencia continuará.
     */
    private void buscarCuentaDestino() {
        String entradaInput = vista.txtNumCuentaD.getText().trim();
        String banco = (String) vista.cmbSelBancoD.getSelectedItem();
        String tipo = (String) vista.cmbTipoCuentaD.getSelectedItem();

        if (entradaInput.isEmpty()) {
            vista.lblD_Cuenta.setText("Cuenta: --");
            vista.lblD_Banco.setText("Banco: --");
            vista.lblD_Tipo.setText("Tipo: --");
            idCuentaDestinoValida = -1;
            return;
        }

        String cuentaConPrefijo = entradaInput.startsWith("NC-") ? entradaInput : "NC-" + entradaInput;
        String bancoBusqueda = "%" + banco.trim().toLowerCase() + "%";

        String query = "SELECT c.id_cuenta, u.nombre, u.apellido " +
                    "FROM cuentas_bancarias c " +
                    "JOIN bancos b ON c.id_banco = b.id_banco " +
                    "JOIN tipos_cuentas tc ON c.id_tipo_cuenta = tc.id_tipo " +
                    "JOIN usuarios u ON c.id_usuario = u.id_usuario " +
                    "WHERE (c.numero_cuenta = ? OR c.numero_cuenta = ? OR c.id_cuenta IN (SELECT id_cuenta FROM tarjetas_bancarias WHERE numero_tarjeta = ?)) " +
                    "AND LOWER(b.nombre) LIKE ? AND tc.nombre = ?";

        try (Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, entradaInput);
            ps.setString(2, cuentaConPrefijo);
            ps.setString(3, entradaInput);
            ps.setString(4, bancoBusqueda);
            ps.setString(5, tipo);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idCuentaDestinoValida = rs.getInt("id_cuenta");
                String destinatario = rs.getString("nombre") + " " + rs.getString("apellido");

                vista.lblD_Cuenta.setText("Cuenta: " + entradaInput);
                vista.lblD_Nombre.setText("Destinatario: " + destinatario);
                vista.lblD_Tipo.setText("Tipo: " + tipo);
                vista.lblD_Banco.setText("Banco: " + banco);
            } else {
                idCuentaDestinoValida = -1; 
                vista.lblD_Cuenta.setText("Cuenta: " + entradaInput + " (Externa)");
                vista.lblD_Nombre.setText("Destinatario: Cliente Externo");
                vista.lblD_Tipo.setText("Tipo: " + tipo);
                vista.lblD_Banco.setText("Banco: " + banco);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        actualizarConversionesYResumen();
    }

    private void calcularTasaCambio() {
        String mO = (String) vista.cmbMonedaO.getSelectedItem();
        String mD = (String) vista.cmbMonedaD.getSelectedItem();

        if (mO == null || mD == null) return;

        if (mO.equals(mD)) {
            tasaCambioActual = 1.000000;
            if(vista.lblTipoCambio != null) {
                vista.lblTipoCambio.setText("Tipo de cambio: 1 " + mO + " = 1.0000 " + mD);
            }
            actualizarConversionesYResumen();
            return;
        }

        boolean exitoApi = false;
        try {
            String[] monedasDestino = { mD };
            Map<String, String> tasas = API.obtenerTasas(mO, monedasDestino);
            
            if (tasas.containsKey(mD) && !tasas.get(mD).equals("N/A")) {
                tasaCambioActual = Double.parseDouble(tasas.get(mD));
                exitoApi = true;
            }
        } catch (Exception e) {
            System.out.println("API desconectada o fuera de línea. Recurriendo a Tasas locales.");
        }

        if (!exitoApi) {
            String query = "SELECT tasa FROM tipos_cambio WHERE moneda_origen = ? AND moneda_destino = ?";
            try (Connection con = conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, mO);
                ps.setString(2, mD);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    tasaCambioActual = rs.getDouble("tasa");
                } else {
                    tasaCambioActual = 1.00; 
                }
            } catch (SQLException ex) {
                tasaCambioActual = 1.00;
            }
        }
        
        if (vista.lblTipoCambio != null) {
            vista.lblTipoCambio.setText("Tipo de cambio: 1 " + mO + " = " + String.format("%.4f", tasaCambioActual) + " " + mD);
        }
        actualizarConversionesYResumen();
    }

    /**
     * REQUERIMIENTO: Actualiza de manera automática el resumen gráfico inferior
     * conforme cambian los datos de la interfaz.
     */
    private void actualizarConversionesYResumen() {
        String txtMontoStr = vista.txtMonto.getText().trim();
        String mO = (String) vista.cmbMonedaO.getSelectedItem();
        String mD = (String) vista.cmbMonedaD.getSelectedItem();

        if (txtMontoStr.isEmpty()) {
            vista.lblDestinatarioRecibe.setText("El destinatario recibe: 0.00 " + mD);
            vista.lblO_Monto.setText("Monto enviado: --");
            vista.lblD_Monto.setText("Monto recibido: --");
            return;
        }

        try {
            double montoOrigen = Double.parseDouble(txtMontoStr);
            montoDestinoCalculado = montoOrigen * tasaCambioActual;

            vista.lblDestinatarioRecibe.setText("El destinatario recibe: " + String.format("%.2f", montoDestinoCalculado) + " " + mD);
            vista.lblO_Monto.setText("Monto enviado: " + mO + " " + String.format("%.2f", montoOrigen));
            vista.lblD_Monto.setText("Monto recibido: " + mD + " " + String.format("%.2f", montoDestinoCalculado));
        } catch (NumberFormatException e) {
            vista.lblDestinatarioRecibe.setText("Monto inválido");
        }
    }

    private boolean verificarContrasena(int idUsuario, String passwordClaro) {
        // Enlazado con tu columna exacta 'password_hash' definida en tu script
        String sql = "SELECT password_hash FROM usuarios WHERE id_usuario = ? LIMIT 1";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passBd = rs.getString("password_hash");
                    return PasswordUtil.verify(passwordClaro, passBd);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void ejecutarTransferencia() {
        if (idCuentaOrigenValida == -1) {
            JOptionPane.showMessageDialog(vista, "La cuenta origen no es válida o no corresponde al banco seleccionado.", "Error de Origen", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(vista.txtMonto.getText().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Ingresa un monto numérico válido.", "Monto inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = new String(vista.txtPassword.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese su contraseña de autorización.", "Seguridad", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!verificarContrasena(idUsuarioRealizador, password)) {
            JOptionPane.showMessageDialog(vista, "Contraseña incorrecta. Acceso denegada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection con = null;
        try {
            con = conexion.getConexion();
            con.setAutoCommit(false); 

            // Bloqueo y verificación limpia del saldo actual en BD
            String sqlCheckSaldo = "SELECT saldo FROM cuentas_bancarias WHERE id_cuenta = ? FOR UPDATE";
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheckSaldo)) {
                psCheck.setInt(1, idCuentaOrigenValida);
                try (ResultSet rsCheck = psCheck.executeQuery()) {
                    if (rsCheck.next()) {
                        double saldoReal = rsCheck.getDouble("saldo");
                        if (monto > saldoReal) {
                            JOptionPane.showMessageDialog(vista, "Fondos Insuficientes. Saldo real: " + saldoReal, "Error", JOptionPane.WARNING_MESSAGE);
                            con.rollback();
                            return;
                        }
                    }
                }
            }

            // 1. Debitar de la cuenta origen
            String sqlDebito = "UPDATE cuentas_bancarias SET saldo = saldo - ? WHERE id_cuenta = ?";
            try (PreparedStatement psDebito = con.prepareStatement(sqlDebito)) {
                psDebito.setDouble(1, monto);
                psDebito.setInt(2, idCuentaOrigenValida);
                psDebito.executeUpdate();
            }

            // 2. Acreditar a la cuenta destino (solo si es una cuenta interna registrada en el sistema)
            if (idCuentaDestinoValida != -1) {
                String sqlCredito = "UPDATE cuentas_bancarias SET saldo = saldo + ? WHERE id_cuenta = ?";
                try (PreparedStatement psCredito = con.prepareStatement(sqlCredito)) {
                    psCredito.setDouble(1, montoDestinoCalculado);
                    psCredito.setInt(2, idCuentaDestinoValida);
                    psCredito.executeUpdate();
                }
            }

            // 3. Registrar auditoría en la tabla transacciones (incluye la columna descripcion del ALTER TABLE)
            String sqlTransaccion = "INSERT INTO transacciones (id_cuenta_origen, id_cuenta_destino, id_usuario_realizador, " +
                                    "tipo_transaccion, monto, moneda_origen, moneda_destino, tasa_cambio_historica, estado, descripcion) " +
                                    "VALUES (?, ?, ?, 'transferencia', ?, ?, ?, ?, 'completada', ?)";
            
            try (PreparedStatement psTrans = con.prepareStatement(sqlTransaccion)) {
                psTrans.setInt(1, idCuentaOrigenValida);
                if (idCuentaDestinoValida != -1) {
                    psTrans.setInt(2, idCuentaDestinoValida);
                } else {
                    psTrans.setNull(2, Types.INTEGER);
                }
                psTrans.setInt(3, idUsuarioRealizador);
                psTrans.setDouble(4, monto);
                psTrans.setString(5, (String) vista.cmbMonedaO.getSelectedItem());
                psTrans.setString(6, (String) vista.cmbMonedaD.getSelectedItem());
                psTrans.setDouble(7, tasaCambioActual);
                psTrans.setString(8, "Transferencia Digital - Modulo Neo Jacket");
                psTrans.executeUpdate();
            }

            con.commit(); 
            
            if (vista.btnImprimir != null) {
                vista.btnImprimir.setVisible(true);
            }
            
            JOptionPane.showMessageDialog(vista, "¡Transferencia completada de manera exitosa!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // CAMBIO CLAVE: ya NO se llama a limpiarCamposSeguridad() aquí.
            // Los campos (cuentas, monto) se CONSERVAN para que el usuario
            // pueda revisar el resumen y presionar "Imprimir comprobante".
            // Solo se limpia la contraseña, por seguridad, y se refresca el
            // saldo mostrado en pantalla (buscarCuentaOrigen/Destino).
            limpiarSoloPassword();
            buscarCuentaOrigen();
            buscarCuentaDestino();
            
        } catch (SQLException ex) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            }
            JOptionPane.showMessageDialog(vista, "Error en procesamiento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Botón "Cancelar": este sí limpia todo de inmediato, incluyendo
     * cuenta origen/destino, monto y comprobante (comportamiento distinto
     * al de una transferencia exitosa).
     */
    private void limpiarCampos() {
        vista.txtNumCuentaO.setText("");
        vista.txtNumCuentaD.setText("");
        vista.txtMonto.setText("");
        vista.txtPassword.setText("");
        if (vista.btnImprimir != null) {
            vista.btnImprimir.setVisible(false); 
        }
        buscarCuentaOrigen();
        buscarCuentaDestino();
    }

    /**
     * Solo limpia la contraseña (por seguridad) tras una transferencia
     * exitosa. El monto y las cuentas se conservan para el comprobante.
     */
    private void limpiarSoloPassword() {
        vista.txtPassword.setText("");
    }
}