package gui;

import funcionalidades.CrearUsuario;
import funcionalidades.SesionUsuario;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.CRUD.CRUD;
import main.Conexion.conexion;

/**
 * Ventana para registrar un gasto y descontarlo del saldo de una cuenta.
 * NUEVO: agregado botón "Agregar Tarjeta" en el sidebar y el campo
 * "Monto gastado" ahora solo acepta dígitos y un punto decimal.
 */
public class ActualizarSaldos extends JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 14);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public ActualizarSaldos() {
        initComponents();

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Actualizar Saldos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

    // ==========================================
    // BOTÓN DE ACCIÓN DEL SIDEBAR (Supervisión / Cerrar sesión / Regresar)
    // ==========================================
    class BotonAccionNeo extends JButton {
        private Color normal;
        private Color hover;

        public BotonAccionNeo(String texto, Color normal, Color hover, Color colorTexto) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(colorTexto);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    /**
     * Restringe un JTextField para que solo acepte dígitos y, como mucho,
     * un punto decimal (para campos de montos/cantidades).
     */
    private static void permitirSoloNumeros(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (resultanteValido(fb, offset, 0, string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (resultanteValido(fb, offset, length, text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean resultanteValido(FilterBypass fb, int offset, int length, String textoNuevo) throws BadLocationException {
                String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String resultante = actual.substring(0, offset) + textoNuevo + actual.substring(offset + length);
                return resultante.matches("\\d*(\\.\\d*)?");
            }
        });
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar(this);
            crearContenido();
        }

        private void crearSidebar(JPanel panel) {
            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                    g2.dispose();
                }
            };

            sidebar.setOpaque(false);
            sidebar.setBounds(20, 20, 300, 870);
            sidebar.setLayout(null);

            Color amarillo = new Color(251, 232, 138);
            Color fondoTransparente = new Color(0, 0, 0, 0);
            Color amarilloBorde = new Color(251, 232, 138);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] opciones = {
                "Saldos",
                "Bancos Conectados",
                "Transferencias",
                "Historial",
                "Agregar Tarjeta"
            };

            int y = 140;

            for (String texto : opciones) {
                JButton btn = new JButton(texto) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                        if (getBackground() != amarillo) {
                            g2.setColor(amarilloBorde);
                            g2.setStroke(new BasicStroke(1f));
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                        }

                        g2.dispose();
                        super.paintComponent(g);
                    }
                };

                btn.setBounds(20, y, 250, 46);
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setOpaque(false);
                btn.setForeground(Color.WHITE);
                btn.setBackground(fondoTransparente);

                Font fuenteActual = btn.getFont();
                btn.setFont(new Font(fuenteActual.getName(), fuenteActual.getStyle(), fuenteActual.getSize() + 2));

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        btn.setBackground(amarillo);
                        btn.setForeground(Color.BLACK);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        btn.setBackground(fondoTransparente);
                        btn.setForeground(Color.WHITE);
                    }
                });

                if (texto.equals("Saldos")) {
                    btn.addActionListener(e -> {
                        new AgregarFondos().setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Bancos Conectados")) {
                    btn.addActionListener(e -> {
                        new BancosConectados().setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Transferencias")) {
                    btn.addActionListener(e -> {
                        new Transferencias().setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Historial")) {
                    btn.addActionListener(e -> {
                        new Historial().setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Agregar Tarjeta")) {
                    btn.addActionListener(e -> {
                        int idUsuario = SesionUsuario.getIdUsuario();
                        new DetalleTarjetaDasboard(idUsuario);
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 68;
            }

            // Botón Supervisión — aparece justo debajo del último botón del
            // menú, solo si el usuario tiene menores a cargo.
            funcionalidades.SupervisionDAO daoSup = new funcionalidades.SupervisionDAO();
            int idSesion = SesionUsuario.getIdUsuario();
            if (idSesion > 0 && daoSup.tieneMenoresACargo(idSesion)) {
                BotonAccionNeo btnSupervision = new BotonAccionNeo(
                        "Supervisión",
                        new Color(251, 232, 138),
                        new Color(255, 245, 180),
                        new Color(25, 38, 35));
                btnSupervision.setBounds(20, y, 250, 55);
                btnSupervision.addActionListener(e -> {
                    new PanelSupervision(idSesion).setVisible(true);
                    dispose();
                });
                sidebar.add(btnSupervision);
            }

            BotonAccionNeo btnCerrarSesion = new BotonAccionNeo(
                    "Cerrar sesión",
                    new Color(191, 76, 58),
                    new Color(214, 100, 80),
                    Color.WHITE);
            btnCerrarSesion.setBounds(20, 800, 250, 55);
            btnCerrarSesion.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            panel.add(sidebar);
        }

        private void crearContenido() {
            JPanel contenedor = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            contenedor.setLayout(null);
            contenedor.setOpaque(false);
            contenedor.setBounds(350, 60, 1300, 760);
            contenedor.setBorder(new LineBorder(new Color(251, 232, 138, 50), 1));
            add(contenedor);

            // Botón Regresar al Dashboard — arriba a la derecha, estilo verde
            BotonAccionNeo btnRegresarDashboard = new BotonAccionNeo(
                    "← Regresar al Dashboard",
                    new Color(94, 116, 73, 220),
                    new Color(120, 150, 90),
                    Color.WHITE);
            btnRegresarDashboard.setBounds(1430, 15, 220, 40);
            btnRegresarDashboard.addActionListener(e -> {
                new Dashboard(SesionUsuario.getIdUsuario()).setVisible(true);
                dispose();
            });
            add(btnRegresarDashboard);

            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(23, 32, 29));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            JButton btnTab1 = crearBotonPestaña("Agregar Fondos", 0);
            btnTab1.addActionListener(e -> {
                new AgregarFondos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab1);

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 434, 55);
            btnTab2.setBackground(new Color(251, 232, 138));
            btnTab2.setForeground(Color.BLACK);
            btnTab2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab2.setFocusPainted(false);
            btnTab2.setBorder(BorderFactory.createEmptyBorder());
            barraSuperior.add(btnTab2);

            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 867);
            btnTab3.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab3);

            PanelFormularioRedondeado panelForm = new PanelFormularioRedondeado();
            panelForm.setBounds(425, 90, 450, 575);
            panelForm.setLayout(null);
            contenedor.add(panelForm);

            // 1. SELECCIONA TU BANCO
            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(30, 20, 390, 25);
            panelForm.add(lblBanco);

            JComboBox<String> cbBancos = new JComboBox<>(new String[]{
                "Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"
            });
            cbBancos.setBounds(30, 50, 390, 45);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(13, 18, 16));
            cbBancos.setForeground(Color.WHITE);
            cbBancos.setBorder(new LineBorder(new Color(251, 232, 138, 120), 1));

            cbBancos.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                    if (isSelected) {
                        label.setBackground(new Color(251, 232, 138));
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(new Color(13, 18, 16));
                        label.setForeground(Color.WHITE);
                    }
                    return label;
                }
            });
            panelForm.add(cbBancos);

            // 2. NÚMERO DE TARJETA
            JLabel lblTarjeta = new JLabel("Número de tarjeta");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(30, 110, 390, 25);
            panelForm.add(lblTarjeta);

            JTextFieldRedondeado txtTarjeta = new JTextFieldRedondeado();
            txtTarjeta.setBounds(30, 140, 390, 45);
            txtTarjeta.setFont(textoInputs);
            panelForm.add(txtTarjeta);

            // --- Saldo actual (solo lectura, se llena automáticamente) ---
            JLabel lblSaldoActualTitulo = new JLabel("Saldo actual");
            lblSaldoActualTitulo.setForeground(Color.WHITE);
            lblSaldoActualTitulo.setFont(tituloCampos);
            lblSaldoActualTitulo.setBounds(30, 195, 390, 22);
            panelForm.add(lblSaldoActualTitulo);

            CampoSoloLectura cajaSaldoActual = new CampoSoloLectura();
            cajaSaldoActual.setBounds(30, 220, 390, 45);
            panelForm.add(cajaSaldoActual);

            JLabel lblSaldoActualValor = new JLabel("Selecciona el banco e ingresa la tarjeta", SwingConstants.CENTER);
            lblSaldoActualValor.setForeground(new Color(251, 232, 138));
            lblSaldoActualValor.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblSaldoActualValor.setBounds(0, 0, cajaSaldoActual.getWidth(), cajaSaldoActual.getHeight());
            cajaSaldoActual.add(lblSaldoActualValor);

            // 3. MONTO GASTADO (el programa calcula el saldo restante, ya no se pide)
            JLabel lblMontoGastado = new JLabel("Monto gastado");
            lblMontoGastado.setForeground(Color.WHITE);
            lblMontoGastado.setFont(tituloCampos);
            lblMontoGastado.setBounds(30, 280, 390, 25);
            panelForm.add(lblMontoGastado);

            JTextFieldRedondeado txtMontoGastado = new JTextFieldRedondeado();
            txtMontoGastado.setBounds(30, 310, 390, 45);
            txtMontoGastado.setFont(textoInputs);
            permitirSoloNumeros(txtMontoGastado);
            panelForm.add(txtMontoGastado);

            // 4. DESCRIPCIÓN
            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(30, 370, 390, 25);
            panelForm.add(lblDescripcion);

            JTextFieldRedondeado txtDescripcion = new JTextFieldRedondeado();
            txtDescripcion.setBounds(30, 400, 390, 45);
            txtDescripcion.setFont(textoInputs);
            panelForm.add(txtDescripcion);

            // --- Consulta automática del saldo actual cuando hay banco + tarjeta válidos ---
            Runnable actualizarSaldoMostrado = () -> {
                String bancoSel = (String) cbBancos.getSelectedItem();
                String tarjeta = txtTarjeta.getText().trim();
                if (bancoSel == null || !tarjeta.matches("\\d{16}")) {
                    lblSaldoActualValor.setText("Selecciona el banco e ingresa la tarjeta");
                    return;
                }
                Double saldo = consultarSaldoActual(bancoSel, tarjeta);
                if (saldo != null) {
                    lblSaldoActualValor.setText(String.format("GTQ %,.2f", saldo));
                } else {
                    lblSaldoActualValor.setText("No se encontró esa tarjeta en ese banco");
                }
            };

            txtTarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {
                    actualizarSaldoMostrado.run();
                }
            });
            cbBancos.addActionListener(e -> actualizarSaldoMostrado.run());

            // 5. BOTÓN GUARDAR
            JButton btnGuardar = new JButton("Guardar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnGuardar.setBounds(30, 460, 390, 45);
            btnGuardar.setBackground(new Color(251, 232, 138));
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setBorderPainted(false);

            btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btnGuardar.setBackground(new Color(255, 245, 180));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btnGuardar.setBackground(new Color(251, 232, 138));
                }
            });
            panelForm.add(btnGuardar);

            // ⚙️ LÓGICA DE GUARDADO, con diagnóstico paso a paso (mismo criterio que AgregarFondos)
            btnGuardar.addActionListener(e -> {

                // ---------- PASO 1: Validar monto gastado ----------
                double montoGastado;
                String montoTexto = txtMontoGastado.getText().trim();
                if (montoTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debes ingresar el monto gastado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    montoGastado = Double.parseDouble(montoTexto);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 1] El monto gastado debe ser un número válido.\nValor ingresado: \"" + montoTexto + "\"",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (montoGastado <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto gastado debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ---------- PASO 2: Sesión ----------
                int idUsuario = SesionUsuario.getIdUsuario();
                if (idUsuario <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 2 - Sesión] No hay un usuario válido en la sesión.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ---------- PASO 3: Validar número de tarjeta ----------
                String numeroTarjeta = txtTarjeta.getText().trim();
                if (numeroTarjeta.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debes ingresar un número de tarjeta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!numeroTarjeta.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 3] El número de tarjeta debe contener 16 dígitos numéricos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ---------- PASO 4: Resolver banco seleccionado ----------
                String bancoSeleccionado = (String) cbBancos.getSelectedItem();
                Integer idBanco = new CrearUsuario().obtenerIdBancoPorNombre(bancoSeleccionado);
                if (idBanco == null) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 4] No se encontró el banco \"" + bancoSeleccionado + "\" en la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String motivo = txtDescripcion.getText();

                Connection con = null;
                PreparedStatement psTarjeta = null;
                ResultSet rsTarjeta = null;
                PreparedStatement psCuenta = null;
                ResultSet rsCuenta = null;

                try {
                    // ---------- PASO 5: Conexión ----------
                    con = conexion.getConexion();
                    if (con == null) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 5] No se pudo conectar a la base de datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // ---------- PASO 6: Validar que la tarjeta exista, sea del usuario y esté activa ----------
                    psTarjeta = con.prepareStatement(
                            "SELECT id_tarjeta FROM tarjetas_bancarias "
                            + "WHERE numero_tarjeta = ? AND id_usuario = ? AND id_banco = ? AND estado = 'activa'"
                    );
                    psTarjeta.setString(1, numeroTarjeta);
                    psTarjeta.setInt(2, idUsuario);
                    psTarjeta.setInt(3, idBanco);
                    rsTarjeta = psTarjeta.executeQuery();

                    if (!rsTarjeta.next()) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 6] No se encontró una tarjeta activa con ese número para este usuario y banco.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // ---------- PASO 7: Obtener id de cuenta y saldo actual REAL desde cuentas_bancarias ----------
                    // (ya no depende de tarjetas_bancarias.id_cuenta, que puede ser NULL)
                    psCuenta = con.prepareStatement(
                            "SELECT id_cuenta, saldo FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ?"
                    );
                    psCuenta.setInt(1, idUsuario);
                    psCuenta.setInt(2, idBanco);
                    rsCuenta = psCuenta.executeQuery();

                    if (!rsCuenta.next()) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 7] No se encontró una cuenta bancaria para este usuario en ese banco.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int idCuenta = rsCuenta.getInt("id_cuenta");
                    double saldoActual = rsCuenta.getDouble("saldo");

                    // ---------- PASO 8: El PROGRAMA calcula el saldo restante (ya no lo escribe el usuario) ----------
                    if (montoGastado > saldoActual) {
                        JOptionPane.showMessageDialog(this,
                                String.format("Fondos insuficientes. Saldo actual: GTQ %,.2f", saldoActual),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double saldoRestante = saldoActual - montoGastado;

                    // ---------- PASO 9: Descontar el gasto y registrar UNA sola transacción ----------
                    PreparedStatement psUpdate = con.prepareStatement(
                            "UPDATE cuentas_bancarias SET saldo = ? WHERE id_cuenta = ? AND id_usuario = ? AND id_banco = ?"
                    );
                    psUpdate.setDouble(1, saldoRestante);
                    psUpdate.setInt(2, idCuenta);
                    psUpdate.setInt(3, idUsuario);
                    psUpdate.setInt(4, idBanco);
                    int filas = psUpdate.executeUpdate();
                    psUpdate.close();

                    if (filas > 0) {
                        PreparedStatement psTrans = con.prepareStatement(
                                "INSERT INTO transacciones (id_cuenta_origen, id_usuario_realizador, tipo_transaccion, monto, descripcion, moneda_origen, estado) "
                                + "VALUES (?, ?, 'retiro', ?, ?, 'GTQ', 'completada')"
                        );
                        psTrans.setInt(1, idCuenta);
                        psTrans.setInt(2, idUsuario);
                        psTrans.setDouble(3, montoGastado);
                        psTrans.setString(4, motivo != null ? motivo : "");
                        psTrans.executeUpdate();
                        psTrans.close();

                        JOptionPane.showMessageDialog(this,
                                String.format("Gasto registrado.\nSaldo anterior: GTQ %,.2f\nGasto: GTQ %,.2f\nSaldo restante: GTQ %,.2f",
                                        saldoActual, montoGastado, saldoRestante),
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        txtMontoGastado.setText("");
                        txtDescripcion.setText("");
                        actualizarSaldoMostrado.run();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se pudo actualizar el saldo. Intenta de nuevo.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    try { if (rsCuenta != null) rsCuenta.close(); } catch (Exception ignored) {}
                    try { if (psCuenta != null) psCuenta.close(); } catch (Exception ignored) {}
                    try { if (rsTarjeta != null) rsTarjeta.close(); } catch (Exception ignored) {}
                    try { if (psTarjeta != null) psTarjeta.close(); } catch (Exception ignored) {}
                    try { if (con != null) con.close(); } catch (Exception ignored) {}
                }
            });

        }

        /**
         * Consulta rápida de solo lectura para mostrar el saldo actual en pantalla
         * mientras el usuario llena el formulario (no valida tarjeta contra usuario
         * en detalle, solo banco + número, para dar feedback inmediato).
         */
        private Double consultarSaldoActual(String bancoSeleccionado, String numeroTarjeta) {
            int idUsuario = SesionUsuario.getIdUsuario();
            if (idUsuario <= 0) {
                return null;
            }
            Integer idBanco = new CrearUsuario().obtenerIdBancoPorNombre(bancoSeleccionado);
            if (idBanco == null) {
                return null;
            }
            String sql = "SELECT c.saldo FROM cuentas_bancarias c "
                    + "JOIN tarjetas_bancarias t ON t.id_usuario = c.id_usuario AND t.id_banco = c.id_banco "
                    + "WHERE c.id_usuario = ? AND c.id_banco = ? AND t.numero_tarjeta = ? AND t.estado = 'activa'";
            try (Connection con = conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idBanco);
                ps.setString(3, numeroTarjeta);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("saldo");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        private JButton crearBotonPestaña(String texto, int xPos) {
            JButton btn = new JButton(texto);
            btn.setBounds(xPos, 0, 434, 55);
            btn.setBackground(new Color(16, 22, 20));
            btn.setForeground(new Color(150, 150, 150));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setForeground(new Color(150, 150, 150));
                }
            });
            return btn;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class PanelFormularioRedondeado extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(20, 28, 25, 120));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(251, 232, 138, 70));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            g2.dispose();
        }
    }

    // Caja de solo lectura con el mismo lenguaje visual de los campos de texto,
    // usada para mostrar el saldo actual centrado (sin dejar un espacio "flotante").
    class CampoSoloLectura extends JPanel {

        public CampoSoloLectura() {
            setOpaque(false);
            setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(13, 18, 16));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            g2.setColor(new Color(251, 232, 138, 130));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    class JTextFieldRedondeado extends JTextField {

        public JTextFieldRedondeado() {
            setOpaque(false);
            setCaretColor(Color.WHITE);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(13, 18, 16));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            g2.setColor(new Color(251, 232, 138, 130));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ActualizarSaldos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new ActualizarSaldos().setVisible(true);
        });
    }
}