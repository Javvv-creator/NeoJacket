package funcionalidades;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Map;
import javax.swing.JOptionPane;
import main.Conexion.conexion; 

public class Transferencias {

    private final gui.Transferencias vista;
    
    private int idCuentaOrigenValida = -1;
    private int idCuentaDestinoValida = -1;
    private int idUsuarioRealizador = 1; 
    
    private double saldoOrigenActual = 0.00;
    private double tasaCambioActual = 1.000000;
    private double montoDestinoCalculado = 0.00;

    public Transferencias(gui.Transferencias vista) {
        this.vista = vista;
        inicializarListeners();
        cargarDatosIniciales();
    }

    private void inicializarListeners() {
        vista.txtNumCuentaO.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarCuentaOrigen();
            }
        });

        vista.txtNumCuentaD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarCuentaDestino();
            }
        });

        vista.txtMonto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarConversionesYResumen();
            }
        });

        vista.cmbMonedaO.addActionListener(e -> calcularTasaCambio());
        vista.cmbMonedaD.addActionListener(e -> calcularTasaCambio());

        vista.cmbSelBancoO.addActionListener(e -> buscarCuentaOrigen());
        vista.cmbTipoCuentaO.addActionListener(e -> buscarCuentaOrigen());
        vista.cmbSelBancoD.addActionListener(e -> buscarCuentaDestino());
        vista.cmbTipoCuentaD.addActionListener(e -> buscarCuentaDestino());

        vista.btnTransferir.addActionListener(e -> ejecutarTransferencia());
        vista.btnCancelar.addActionListener(e -> limpiarCampos());
    }

    private void cargarDatosIniciales() {
        calcularTasaCambio();
    }

    private void buscarCuentaOrigen() {
        String numCuenta = vista.txtNumCuentaO.getText().trim();
        String banco = (String) vista.cmbSelBancoO.getSelectedItem();
        String tipo = (String) vista.cmbTipoCuentaO.getSelectedItem();

        if (numCuenta.isEmpty()) {
            vista.lblSaldoO.setText("Saldo: Q0.00");
            vista.lblO_Cuenta.setText("Cuenta: --");
            vista.lblO_Banco.setText("Banco: --");
            vista.lblO_Tipo.setText("Tipo: --");
            idCuentaOrigenValida = -1;
            return;
        }

        String query = "SELECT c.id_cuenta, c.saldo, c.moneda, u.nombre, u.apellido " +
                       "FROM cuentas_bancarias c " +
                       "JOIN bancos b ON c.id_banco = b.id_banco " +
                       "JOIN tipos_cuentas tc ON c.id_tipo_cuenta = tc.id_tipo " +
                       "JOIN usuarios u ON c.id_usuario = u.id_usuario " +
                       "WHERE c.numero_cuenta = ? AND b.nombre = ? AND tc.nombre = ?";

        try (Connection con = conexion.getConexion(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, numCuenta);
            ps.setString(2, banco);
            ps.setString(3, tipo);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idCuentaOrigenValida = rs.getInt("id_cuenta");
                saldoOrigenActual = rs.getDouble("saldo");
                String moneda = rs.getString("moneda");
                String titular = rs.getString("nombre") + " " + rs.getString("apellido");

                vista.lblSaldoO.setText("Saldo: " + moneda + " " + String.format("%.2f", saldoOrigenActual));
                vista.lblO_Cuenta.setText("Cuenta: " + numCuenta);
                vista.lblO_Nombre.setText("Nombre: " + titular);
                vista.lblO_Tipo.setText("Tipo: " + tipo);
                vista.lblO_Banco.setText("Banco: " + banco);
            } else {
                idCuentaOrigenValida = -1;
                vista.lblSaldoO.setText("Cuenta no encontrada o inválida");
                vista.lblO_Cuenta.setText("Cuenta: NO EXISTE");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        actualizarConversionesYResumen();
    }

    private void buscarCuentaDestino() {
        String numCuenta = vista.txtNumCuentaD.getText().trim();
        String banco = (String) vista.cmbSelBancoD.getSelectedItem();
        String tipo = (String) vista.cmbTipoCuentaD.getSelectedItem();

        if (numCuenta.isEmpty()) {
            vista.lblTitularD.setText("Titular: --");
            vista.lblD_Cuenta.setText("Cuenta: --");
            vista.lblD_Banco.setText("Banco: --");
            vista.lblD_Tipo.setText("Tipo: --");
            idCuentaDestinoValida = -1;
            return;
        }

        String query = "SELECT c.id_cuenta, u.nombre, u.apellido " +
                       "FROM cuentas_bancarias c " +
                       "JOIN bancos b ON c.id_banco = b.id_banco " +
                       "JOIN tipos_cuentas tc ON c.id_tipo_cuenta = tc.id_tipo " +
                       "JOIN usuarios u ON c.id_usuario = u.id_usuario " +
                       "WHERE c.numero_cuenta = ? AND b.nombre = ? AND tc.nombre = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, numCuenta);
            ps.setString(2, banco);
            ps.setString(3, tipo);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idCuentaDestinoValida = rs.getInt("id_cuenta");
                String titular = rs.getString("nombre") + " " + rs.getString("apellido");

                vista.lblTitularD.setText("Titular: " + titular);
                vista.lblD_Cuenta.setText("Cuenta: " + numCuenta);
                vista.lblD_Nombre.setText("Nombre: " + titular);
                vista.lblD_Tipo.setText("Tipo: " + tipo);
                vista.lblD_Banco.setText("Banco: " + banco);
            } else {
                idCuentaDestinoValida = -1;
                vista.lblTitularD.setText("Cuenta destino no encontrada");
                vista.lblD_Cuenta.setText("Cuenta: NO EXISTE");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        actualizarConversionesYResumen();
    }

    private void calcularTasaCambio() {
        String mO = (String) vista.cmbMonedaO.getSelectedItem();
        String mD = (String) vista.cmbMonedaD.getSelectedItem();

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
            System.out.println("No se pudo conectar a la API. Usando tasa de la Base de Datos.");
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

    private void ejecutarTransferencia() {
        if (idCuentaOrigenValida == -1 || idCuentaDestinoValida == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, valida que ambas cuentas bancarias existan.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (idCuentaOrigenValida == idCuentaDestinoValida) {
            JOptionPane.showMessageDialog(vista, "La cuenta origen no puede ser igual a la cuenta destino.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(vista.txtMonto.getText().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Ingresa un monto numérico válido y mayor a cero.", "Monto inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (monto > saldoOrigenActual) {
            JOptionPane.showMessageDialog(vista, "Fondos Insuficientes en la cuenta seleccionada.", "Fondos Insuficientes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String password = new String(vista.txtPassword.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por seguridad, debes ingresar tu contraseña para autorizar.", "Falta autenticación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection con = null;
        try {
            con = conexion.getConexion();
            con.setAutoCommit(false); 

            String sqlDebito = "UPDATE cuentas_bancarias SET saldo = saldo - ? WHERE id_cuenta = ?";
            try (PreparedStatement psDebito = con.prepareStatement(sqlDebito)) {
                psDebito.setDouble(1, monto);
                psDebito.setInt(2, idCuentaOrigenValida);
                psDebito.executeUpdate();
            }

            String sqlCredito = "UPDATE cuentas_bancarias SET saldo = saldo + ? WHERE id_cuenta = ?";
            try (PreparedStatement psCredito = con.prepareStatement(sqlCredito)) {
                psCredito.setDouble(1, montoDestinoCalculado);
                psCredito.setInt(2, idCuentaDestinoValida);
                psCredito.executeUpdate();
            }

            String sqlTransaccion = "INSERT INTO transacciones (id_cuenta_origen, id_cuenta_destino, id_usuario_realizador, " +
                                    "tipo_transaccion, monto, moneda_origen, moneda_destino, tasa_cambio_historica, estado) " +
                                    "VALUES (?, ?, ?, 'transferencia', ?, ?, ?, ?, 'completada')";
            
            try (PreparedStatement psTrans = con.prepareStatement(sqlTransaccion)) {
                psTrans.setInt(1, idCuentaOrigenValida);
                psTrans.setInt(2, idCuentaDestinoValida);
                psTrans.setInt(3, idUsuarioRealizador);
                psTrans.setDouble(4, monto);
                psTrans.setString(5, (String) vista.cmbMonedaO.getSelectedItem());
                psTrans.setString(6, (String) vista.cmbMonedaD.getSelectedItem());
                psTrans.setDouble(7, tasaCambioActual);
                psTrans.executeUpdate();
            }

            con.commit(); 
            
            // OPERACIÓN EXITOSA: Hacemos visible el botón de impresión limpio
            if (vista.btnImprimir != null) {
                vista.btnImprimir.setVisible(true);
            }
            
            JOptionPane.showMessageDialog(vista, "¡Transferencia procesada con éxito!", "Neo Jacket - Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            buscarCuentaOrigen();
            buscarCuentaDestino();
            limpiarCamposSeguridad();
            
        } catch (SQLException ex) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            }
            JOptionPane.showMessageDialog(vista, "Error en el sistema financiero: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private void limpiarCampos() {
        vista.txtNumCuentaO.setText("");
        vista.txtNumCuentaD.setText("");
        vista.txtMonto.setText("");
        vista.txtPassword.setText("");
        if (vista.btnImprimir != null) {
            vista.btnImprimir.setVisible(false); // Ocultar si cancelan o limpian campos
        }
        buscarCuentaOrigen();
        buscarCuentaDestino();
    }

    private void limpiarCamposSeguridad() {
        vista.txtMonto.setText("");
        vista.txtPassword.setText("");
        actualizarConversionesYResumen();
    }
}