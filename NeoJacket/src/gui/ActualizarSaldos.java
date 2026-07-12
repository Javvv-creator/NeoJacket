package gui;

import funcionalidades.SesionUsuario;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.CRUD.CRUD;
import main.Conexion.conexion;

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
            sidebar.setBounds(20, 20, 300, 950);
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
                "Historial"
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
                        new Saldos().setVisible(true);
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

                JButton btnCerrarSesion = new JButton("Cerrar sesión");
                btnCerrarSesion.setBounds(20, 880, 250, 55);
                btnCerrarSesion.setFocusPainted(false);
                btnCerrarSesion.setBorderPainted(false);
                btnCerrarSesion.setBackground(new Color(191, 76, 58));
                btnCerrarSesion.setForeground(Color.WHITE);
                btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCerrarSesion.addActionListener(e -> {
                    new InicioNeo().setVisible(true);
                    dispose();
                });
                sidebar.add(btnCerrarSesion);

                sidebar.add(btn);
                y += 68;
            }

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

            // 🌟 CAMBIO AQUÍ: Se agrandó el alto de panelForm de 510 a 575 para alojar todos los campos ordenadamente
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

            // 2. NÚMERO DE TARJETA (Ahora justo abajo del ComboBox)
            JLabel lblTarjeta = new JLabel("Número de tarjeta");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(30, 110, 390, 25);
            panelForm.add(lblTarjeta);

            JTextFieldRedondeado txtTarjeta = new JTextFieldRedondeado();
            txtTarjeta.setBounds(30, 140, 390, 45);
            txtTarjeta.setFont(textoInputs);
            panelForm.add(txtTarjeta);

            // 3. MONTO GASTADO
            JLabel lblMontoGastado = new JLabel("Monto gastado");
            lblMontoGastado.setForeground(Color.WHITE);
            lblMontoGastado.setFont(tituloCampos);
            lblMontoGastado.setBounds(30, 200, 390, 25);
            panelForm.add(lblMontoGastado);

            JTextFieldRedondeado txtMontoGastado = new JTextFieldRedondeado();
            txtMontoGastado.setBounds(30, 230, 390, 45);
            txtMontoGastado.setFont(textoInputs);
            panelForm.add(txtMontoGastado);

            // 4. NUEVO SALDO DESPUÉS DEL GASTO
            JLabel lblNuevoSaldo = new JLabel("Nuevo saldo después del gasto");
            lblNuevoSaldo.setForeground(Color.WHITE);
            lblNuevoSaldo.setFont(tituloCampos);
            lblNuevoSaldo.setBounds(30, 290, 390, 25);
            panelForm.add(lblNuevoSaldo);

            JTextFieldRedondeado txtNuevoSaldo = new JTextFieldRedondeado();
            txtNuevoSaldo.setBounds(30, 320, 390, 45);
            txtNuevoSaldo.setFont(textoInputs);
            panelForm.add(txtNuevoSaldo);

            // 5. DESCRIPCIÓN
            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(30, 380, 390, 25);
            panelForm.add(lblDescripcion);

            JTextFieldRedondeado txtDescripcion = new JTextFieldRedondeado();
            txtDescripcion.setBounds(30, 410, 390, 45);
            txtDescripcion.setFont(textoInputs);
            panelForm.add(txtDescripcion);

            // 6. BOTÓN GUARDAR
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
            btnGuardar.setBounds(30, 495, 390, 50);
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

            btnGuardar.addActionListener(e -> {
                try {
                    CRUD crud = new CRUD();

                    int idUsuario = SesionUsuario.getIdUsuario();
                    String bancoSeleccionado = (String) cbBancos.getSelectedItem();
                    double montoActual = Double.parseDouble(txtMontoGastado.getText());
                    double nuevoMonto = Double.parseDouble(txtNuevoSaldo.getText());
                    String motivo = txtDescripcion.getText();

                    String numeroTarjeta = txtTarjeta.getText().trim();
                    if (numeroTarjeta.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Debes ingresar un número de tarjeta.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!numeroTarjeta.matches("\\d{16}")) {
                        JOptionPane.showMessageDialog(this, "El número de tarjeta debe contener 16 dígitos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String nombreBD = "";
                    switch (bancoSeleccionado) {
                        case "Banco Industrial":
                            nombreBD = "Bi";
                            break;
                        case "BAC Credomatic":
                            nombreBD = "bac";
                            break;
                        case "Banrural":
                            nombreBD = "banrural";
                            break;
                        case "G&T Continental":
                            nombreBD = "gyt";
                            break;
                    }

                    Connection con = conexion.getConexion();
                    PreparedStatement psBanco = con.prepareStatement("SELECT id_banco FROM bancos WHERE nombre = ?");
                    psBanco.setString(1, nombreBD);
                    ResultSet rsBanco = psBanco.executeQuery();

                    if (!rsBanco.next()) {
                        JOptionPane.showMessageDialog(this, "Banco no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        rsBanco.close();
                        psBanco.close();
                        con.close();
                        return;
                    }
                    int idBanco = rsBanco.getInt("id_banco");

                    PreparedStatement psTarjeta = con.prepareStatement(
                            "SELECT t.id_tarjeta, c.id_cuenta "
                            + "FROM tarjetas_bancarias t "
                            + "JOIN cuentas_bancarias c ON t.id_cuenta = c.id_cuenta "
                            + "WHERE t.numero_tarjeta = ? AND t.id_usuario = ? AND t.id_banco = ? AND t.estado = 'activa'"
                    );
                    psTarjeta.setString(1, numeroTarjeta);
                    psTarjeta.setInt(2, idUsuario);
                    psTarjeta.setInt(3, idBanco);

                    ResultSet rsTarjeta = psTarjeta.executeQuery();

                    if (!rsTarjeta.next()) {
                        JOptionPane.showMessageDialog(this, "Número de tarjeta inválido o banco no registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                        rsTarjeta.close();
                        psTarjeta.close();
                        rsBanco.close();
                        psBanco.close();
                        con.close();
                        return;
                    }

                    int idCuenta = rsTarjeta.getInt("id_cuenta");

                    boolean ok = crud.actualizarSaldo(idUsuario, idBanco, idCuenta, montoActual, nuevoMonto, motivo);
                    if (ok) {
                        PreparedStatement psTrans = con.prepareStatement(
                                "INSERT INTO transacciones (id_cuenta_origen, id_usuario_realizador, tipo_transaccion, monto, moneda_origen, estado) "
                                + "VALUES (?, ?, 'actualizacion', ?, 'GTQ', 'completada')"
                        );
                        psTrans.setInt(1, idCuenta);
                        psTrans.setInt(2, idUsuario);
                        psTrans.setDouble(3, nuevoMonto);
                        psTrans.executeUpdate();
                        psTrans.close();

                        JOptionPane.showMessageDialog(this, "Saldo actualizado con éxito");
                    }

                    rsTarjeta.close();
                    psTarjeta.close();
                    rsBanco.close();
                    psBanco.close();
                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

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
