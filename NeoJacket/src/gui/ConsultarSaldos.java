package gui;

import funcionalidades.SesionUsuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.CRUD.CRUD;
import main.Conexion.conexion;

public class ConsultarSaldos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private Image fondo;
    private Image logo;
    private JLabel lblBancoValor;
    private JLabel lblSaldoValor;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public ConsultarSaldos() {
        initComponents();

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Consultar Saldos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

    class BotonNeo extends JButton {

        private Color colorNormal;
        private Color colorHover;

        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.colorNormal = normal;
            this.colorHover = hover;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getModel().isRollover() ? colorHover : colorNormal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            super.paintComponent(g);
            g2.dispose();
        }
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
            Color amarilloHover = new Color(255, 245, 180);
            Color fondoTransparente = new Color(0, 0, 0, 0);
            Color amarilloBorde = new Color(251, 232, 138, 150);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] opciones = {"Saldos", "Bancos Conectados", "Transferencias", "Historial"};
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
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));

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

                sidebar.add(btn);
                y += 68;
            }

            BotonNeo btnCerrarSesion = new BotonNeo("Cerrar sesión", amarillo, amarilloHover);
            btnCerrarSesion.setBounds(20, 880, 250, 55);
            btnCerrarSesion.setBackground(new Color(191, 76, 58));
            btnCerrarSesion.setForeground(Color.WHITE);
            btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
                    g2.setColor(new Color(25, 38, 35, 150));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    g2.dispose();
                }
            };
            contenedor.setLayout(null);
            contenedor.setOpaque(false);
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            JPanel barraSuperior = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 240));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                }
            };
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setOpaque(false);
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);
            Color tabInactivo = new Color(40, 58, 54);

            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 433, 55);
            btnTab1.setBackground(tabInactivo);
            btnTab1.setForeground(Color.WHITE);
            btnTab1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab1.setFocusPainted(false);
            btnTab1.addActionListener(e -> {
                new AgregarFondos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab1);

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 434, 55);
            btnTab2.setBackground(tabInactivo);
            btnTab2.setForeground(Color.WHITE);
            btnTab2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab2.setFocusPainted(false);
            btnTab2.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab2);

            JButton btnTab3 = new JButton("Consultar Saldos");
            btnTab3.setBounds(867, 0, 433, 55);
            btnTab3.setBackground(amarillo);
            btnTab3.setForeground(Color.BLACK);
            btnTab3.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab3.setFocusPainted(false);
            barraSuperior.add(btnTab3);

            // MARCO CONTENEDOR PARA FILTRO
            JPanel marcoFiltro = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(251, 232, 138, 40));
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    g2.dispose();
                }
            };
            marcoFiltro.setBounds(40, 95, 1220, 95); // Se ajustó ligeramente la altura de 90 a 95
            marcoFiltro.setOpaque(false);
            marcoFiltro.setLayout(null);
            contenedor.add(marcoFiltro);

            // 1. SELECCIONA TU BANCO (Izquierda)
            JLabel lblSelecciona = new JLabel("Selecciona tu banco:");
            lblSelecciona.setFont(tituloCampos);
            lblSelecciona.setForeground(Color.WHITE);
            lblSelecciona.setBounds(20, 10, 200, 25);
            marcoFiltro.add(lblSelecciona);

            String[] opcionesBancos = {"Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(20, 40, 260, 38);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(13, 18, 16));
            cbBancos.setForeground(Color.WHITE);
            cbBancos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1));

            cbBancos.setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton("▼");
                    btn.setBorderPainted(false);
                    btn.setContentAreaFilled(false);
                    btn.setFocusPainted(false);
                    btn.setForeground(amarillo);
                    btn.setBackground(new Color(20, 30, 28));
                    return btn;
                }

                @Override
                protected ComboPopup createPopup() {
                    BasicComboPopup popup = (BasicComboPopup) super.createPopup();
                    popup.getList().setBackground(new Color(20, 30, 28));
                    popup.getList().setForeground(Color.WHITE);
                    popup.getList().setSelectionBackground(amarillo);
                    popup.getList().setSelectionForeground(Color.BLACK);
                    return popup;
                }
            });

            cbBancos.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    if (isSelected) {
                        label.setBackground(amarillo);
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(new Color(20, 30, 28));
                        label.setForeground(Color.WHITE);
                    }
                    return label;
                }
            });
            marcoFiltro.add(cbBancos);

            // 2. NÚMERO DE TARJETA (A la par del banco)
            JLabel lblTarjeta = new JLabel("Número de tarjeta:");
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setBounds(300, 10, 200, 25);
            marcoFiltro.add(lblTarjeta);

            JTextField txtTarjeta = new JTextField();
            txtTarjeta.setBounds(300, 40, 260, 38);
            txtTarjeta.setFont(textoInputs);
            txtTarjeta.setBackground(new Color(13, 18, 16));
            txtTarjeta.setForeground(Color.WHITE);
            txtTarjeta.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1));
            marcoFiltro.add(txtTarjeta);

            // 3. BOTÓN CONSULTAR (A la par de la tarjeta)
            BotonNeo btnConsultar = new BotonNeo("ⓘ Consultar", amarillo, amarilloHover);
            btnConsultar.setForeground(Color.BLACK);
            btnConsultar.setBounds(580, 40, 150, 38);
            marcoFiltro.add(btnConsultar);

            // 4. INFORMACIÓN DEL BANCO Y SALDO (Desplazados a la derecha de forma prolija)
            JLabel lblBanco = new JLabel("Banco:");
            lblBanco.setFont(tituloCampos);
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setBounds(760, 20, 100, 25);
            marcoFiltro.add(lblBanco);

            lblBancoValor = new JLabel("--");
            lblBancoValor.setFont(textoInputs);
            lblBancoValor.setForeground(Color.WHITE);
            lblBancoValor.setBounds(910, 20, 250, 25);
            marcoFiltro.add(lblBancoValor);

            JLabel lblSaldo = new JLabel("Saldo Disponible:");
            lblSaldo.setFont(tituloCampos);
            lblSaldo.setForeground(Color.WHITE);
            lblSaldo.setBounds(760, 50, 150, 25);
            marcoFiltro.add(lblSaldo);

            lblSaldoValor = new JLabel("Q. 0.00");
            lblSaldoValor.setFont(textoInputs);
            lblSaldoValor.setForeground(Color.WHITE);
            lblSaldoValor.setBounds(910, 50, 250, 25);
            marcoFiltro.add(lblSaldoValor);

            // PANEL DE LA TABLA (Mantiene su posición original abajo del marco de filtro)
            JPanel panelTabla = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(251, 232, 138, 40));
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    g2.dispose();
                }
            };
            panelTabla.setLayout(null);
            panelTabla.setOpaque(false);
            panelTabla.setBounds(40, 215, 1220, 490);
            contenedor.add(panelTabla);

            String[] columnas = {"Fecha", "Actividad", "Monto", "Saldo Restante", "Estado"};

            modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tabla = new JTable(modelo);
            tabla.setRowHeight(38);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(94, 116, 73, 100));
            tabla.setSelectionBackground(amarillo);
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setPreferredSize(new Dimension(header.getWidth(), 40));

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(15, 15, 1190, 460);
            panelTabla.add(scroll);

            // Lógica funcional intacta
            btnConsultar.addActionListener(e -> {
                try {
                    CRUD crud = new CRUD();
                    int idUsuario = SesionUsuario.getIdUsuario();
                    String bancoSeleccionado = (String) cbBancos.getSelectedItem();
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
                            "SELECT t.id_tarjeta, c.id_cuenta, c.saldo "
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
                    double saldo = crud.consultarSaldo(idUsuario, idBanco, idCuenta);
                    lblSaldoValor.setText("Q. " + saldo);
                    lblBancoValor.setText(bancoSeleccionado); // Asigna el texto del banco consultado para dar feedback

                    rsTarjeta.close();
                    psTarjeta.close();
                    rsBanco.close();
                    psBanco.close();

                    ResultSet rsTrans = crud.consultarTransacciones(idUsuario, idBanco, idCuenta);
                    modelo.setRowCount(0);

                    while (rsTrans.next()) {
                        Object[] fila = {
                            rsTrans.getTimestamp("creado_en"),
                            rsTrans.getString("tipo_transaccion"),
                            rsTrans.getDouble("monto"),
                            rsTrans.getDouble("saldoRestante"),
                            rsTrans.getString("estado")
                        };
                        modelo.addRow(fila);
                    }
                    rsTrans.close();
                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new ConsultarSaldos().setVisible(true));
    }
}