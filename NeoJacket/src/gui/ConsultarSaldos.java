package gui;

import funcionalidades.CrearUsuario;
import funcionalidades.SesionUsuario;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import main.CRUD.CRUD;
import main.Conexion.conexion;

/**
 * CORREGIDO (mismo criterio que AgregarFondos / ActualizarSaldos):
 *  - El mapeo de banco usaba códigos que no existen en la tabla `bancos`
 *    ("Bi", "bac", "banrural", "gyt"). Ahora usa
 *    CrearUsuario.obtenerIdBancoPorNombre(...).
 *  - La cuenta se resuelve directo por id_usuario + id_banco en
 *    cuentas_bancarias (antes dependía de tarjetas_bancarias.id_cuenta, que
 *    puede quedar en NULL si la tarjeta no se vinculó explícitamente).
 *  - El saldo ahora se muestra con formato de 2 decimales (antes salía
 *    "Q. 500.0" en vez de "Q. 500.00").
 *  - Recursos de BD cerrados con try-with-resources para evitar fugas de
 *    conexión si algo lanza una excepción a la mitad.
 */
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
            marcoFiltro.setBounds(40, 95, 1220, 95);
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

            // NOTA: antes había un cbBancos.setUI(new BasicComboBoxUI(){...}) aquí.
            // Ese override rompía el fondo oscuro del recuadro CERRADO del combobox
            // (aunque el desplegable sí se veía bien), dejando un fondo blanco poco
            // legible. Con solo setBackground/setForeground/border (como en
            // AgregarFondos y ActualizarSaldos) el tema oscuro se respeta en ambos
            // estados: cerrado y desplegado.

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
            BotonNeo btnConsultar = new BotonNeo("Consultar", amarillo, amarilloHover);
            btnConsultar.setForeground(Color.BLACK);
            btnConsultar.setBounds(580, 40, 150, 38);
            marcoFiltro.add(btnConsultar);

            // 4. INFORMACIÓN DEL BANCO Y SALDO
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

            // PANEL DE LA TABLA
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

            String[] columnas = {"Fecha", "Actividad", "Monto", "Estado"};

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

            // ⚙️ LÓGICA DE CONSULTA (corregida: banco real + cuenta real)
            btnConsultar.addActionListener(e -> {
                int idUsuario = SesionUsuario.getIdUsuario();
                if (idUsuario <= 0) {
                    JOptionPane.showMessageDialog(this, "No hay una sesión válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

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

                // CORREGIDO: antes se mapeaba a códigos ("Bi", "bac"...) que no
                // existen en la tabla `bancos`. Ahora se resuelve el id_banco real
                // por nombre o nombre_corto, igual que en el resto de la app.
                Integer idBanco = new CrearUsuario().obtenerIdBancoPorNombre(bancoSeleccionado);
                if (idBanco == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el banco \"" + bancoSeleccionado + "\" en la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection con = conexion.getConexion()) {
                    if (con == null) {
                        JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validar que la tarjeta exista, sea del usuario, del banco y esté activa
                    try (PreparedStatement psTarjeta = con.prepareStatement(
                            "SELECT id_tarjeta FROM tarjetas_bancarias "
                            + "WHERE numero_tarjeta = ? AND id_usuario = ? AND id_banco = ? AND estado = 'activa'")) {
                        psTarjeta.setString(1, numeroTarjeta);
                        psTarjeta.setInt(2, idUsuario);
                        psTarjeta.setInt(3, idBanco);

                        try (ResultSet rsTarjeta = psTarjeta.executeQuery()) {
                            if (!rsTarjeta.next()) {
                                JOptionPane.showMessageDialog(this,
                                        "No se encontró una tarjeta activa con ese número para este usuario y banco.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }

                    // CORREGIDO: la cuenta se busca directo en cuentas_bancarias por
                    // usuario + banco, en vez de depender de tarjetas_bancarias.id_cuenta
                    // (que puede quedar en NULL).
                    int idCuenta;
                    try (PreparedStatement psCuenta = con.prepareStatement(
                            "SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ?")) {
                        psCuenta.setInt(1, idUsuario);
                        psCuenta.setInt(2, idBanco);
                        try (ResultSet rsCuenta = psCuenta.executeQuery()) {
                            if (!rsCuenta.next()) {
                                JOptionPane.showMessageDialog(this,
                                        "No se encontró una cuenta bancaria para este usuario en ese banco.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            idCuenta = rsCuenta.getInt("id_cuenta");
                        }
                    }

                    CRUD crud = new CRUD();
                    double saldo = crud.consultarSaldo(idUsuario, idBanco, idCuenta);
                    lblSaldoValor.setText(String.format("Q. %,.2f", saldo));

                    // Se pidió que el nombre mostrado sea el que realmente está en la
                    // BD (columna `nombre` de `bancos`), no solo el texto del combobox.
                    String nombreBancoReal = bancoSeleccionado;
                    try (PreparedStatement psNombreBanco = con.prepareStatement(
                            "SELECT nombre FROM bancos WHERE id_banco = ?")) {
                        psNombreBanco.setInt(1, idBanco);
                        try (ResultSet rsNombreBanco = psNombreBanco.executeQuery()) {
                            if (rsNombreBanco.next()) {
                                nombreBancoReal = rsNombreBanco.getString("nombre");
                            }
                        }
                    }
                    lblBancoValor.setText(nombreBancoReal);

                    // Historial de transacciones de esa cuenta
                    modelo.setRowCount(0);
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    try (PreparedStatement psTrans = con.prepareStatement(
                            "SELECT creado_en, tipo_transaccion, monto, estado FROM transacciones "
                            + "WHERE id_usuario_realizador = ? AND id_cuenta_origen = ? "
                            + "ORDER BY creado_en DESC")) {
                        psTrans.setInt(1, idUsuario);
                        psTrans.setInt(2, idCuenta);
                        try (ResultSet rsTrans = psTrans.executeQuery()) {
                            while (rsTrans.next()) {
                                java.sql.Timestamp fecha = rsTrans.getTimestamp("creado_en");
                                Object[] fila = {
                                    fecha != null ? sdf.format(fecha) : "--",
                                    rsTrans.getString("tipo_transaccion"),
                                    String.format("%,.2f", rsTrans.getDouble("monto")),
                                    rsTrans.getString("estado")
                                };
                                modelo.addRow(fila);
                            }
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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