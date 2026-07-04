package gui;

import funcionalidades.SesionUsuario;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import main.Conexion.conexion;

public class BancosConectados extends JFrame {

    private Image fondo;
    private Image logo;

    Font tituloPanel = new Font("Segoe UI", Font.BOLD, 16);
    Font textoNormal = new Font("Segoe UI", Font.PLAIN, 14);

    public BancosConectados() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Bancos Conectados");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
        
    }

    // ==========================================================
    // CLASE CLONADA DEL RADIO BUTTON PREMIUM DORADO
    // ==========================================================
    class NeoRadioButton extends JRadioButton {

        public NeoRadioButton(String texto) {
            super(texto);
            setOpaque(false);
            setForeground(new Color(235, 235, 230)); // Blanco suave
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setIconTextGap(10);
            getModel().addChangeListener(e -> repaint());
            setIcon(new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillOval(x, y, 18, 18);
                    g2.setColor(new Color(251, 232, 138)); // Dorado amarillo
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(x, y, 18, 18);
                    if (isSelected()) {
                        g2.setColor(new Color(251, 232, 138));
                        g2.fillOval(x + 4, y + 4, 10, 10);
                    }
                    g2.dispose();
                }

                @Override
                public int getIconWidth() {
                    return 18;
                }

                @Override
                public int getIconHeight() {
                    return 18;
                }
            });
        }
    }

    // ==========================================================
    // CLASE PARA EL BOTÓN FLUJO TOTALMENTE REDONDEADO
    // ==========================================================
    class BotonFlujoNeo extends JButton {

        public BotonFlujoNeo(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean over = getModel().isRollover();
            g2.setColor(over ? new Color(255, 245, 180) : new Color(251, 232, 138));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25); // Bordes bien redondeados

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Variables globales para los radio buttons y labels
    private NeoRadioButton rbIndustrial, rbBanrural, rbBac, rbGYT;
    private JLabel lblBancoValor, lblSaldoValor;
    private DefaultTableModel modelo;

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar(this);
            crearContenido(this);
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

            panel.add(sidebar);
        }

        private void crearContenido(JPanel panel) {

            // ==========================================================
            // NUEVO PANEL TRASPARENTE DE FONDO PARA EL CONTENIDO (Cuerpo principal)
            // ==========================================================
            JPanel panelContenedorGris = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(15, 22, 20, 130)); // Cristal oscuro traslúcido
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                }
            };
            panelContenedorGris.setLayout(null);
            panelContenedorGris.setOpaque(false);
            panelContenedorGris.setBounds(350, 20, 1250, 950); // Cubre toda la zona de trabajo derecha
            panel.add(panelContenedorGris);

            // Títulos montados ahora dentro del contenedor transparente
            JLabel lblTitulo = new JLabel("BANCOS CONECTADOS");
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitulo.setBounds(40, 20, 400, 40);
            panelContenedorGris.add(lblTitulo);

            JLabel lblSubtitulo = new JLabel("AQUÍ PUEDES GESTIONAR EL CONTROL DE TUS BANCOS");
            lblSubtitulo.setForeground(new Color(190, 195, 190));
            lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblSubtitulo.setBounds(40, 60, 500, 30);
            panelContenedorGris.add(lblSubtitulo);

            ButtonGroup grupoBancos = new ButtonGroup();

            // ==========================================================
            // DISTRIBUCIÓN MATEMÁTICA CORRECTA PARA CENTRAR LAS 4 IMÁGENES
            // ==========================================================
            int cardW = 220;
            int cardH = 175;
            int inicioX = 40; // Margen izquierdo inicial interno
            int separacion = 80; // Distancia exacta entre inicios de tarjeta
            int posY = 120;

            // --- Panel Banco Industrial ---
            JPanel panelIndustrial = crearCard(inicioX, posY, cardW, cardH);
            panelIndustrial.setLayout(null);
            Image imgIndustrial = new ImageIcon(getClass().getResource("/gui/image/banco_industrial.png")).getImage();
            Image imgEscaladaIndustrial = imgIndustrial.getScaledInstance(230, 150, Image.SCALE_SMOOTH);

            // Reemplazo a JLabel para el icono del banco (así el RadioButton personalizado no se solapa feo)
            JLabel lblImgIndustrial = new JLabel(new ImageIcon(imgEscaladaIndustrial));
            lblImgIndustrial.setBounds((cardW - 180) / 2, 25, 180, 100);
            panelIndustrial.add(lblImgIndustrial);

            rbIndustrial = new NeoRadioButton("Banco Industrial");
            rbIndustrial.setBounds(30, 130, 160, 26);
            grupoBancos.add(rbIndustrial);
            panelIndustrial.add(rbIndustrial);
            panelContenedorGris.add(panelIndustrial);

            // --- Panel Banrural ---
            JPanel panelBanrural = crearCard(inicioX + (cardW + separacion), posY, cardW, cardH);
            panelBanrural.setLayout(null);
            Image imgBanrural = new ImageIcon(getClass().getResource("/gui/image/banrural.png")).getImage();
            Image imgEscaladaBanrural = imgBanrural.getScaledInstance(280, 200, Image.SCALE_SMOOTH);

            JLabel lblImgBanrural = new JLabel(new ImageIcon(imgEscaladaBanrural));
            lblImgBanrural.setBounds((cardW - 180) / 2, 25, 180, 100);
            panelBanrural.add(lblImgBanrural);

            rbBanrural = new NeoRadioButton("Banrural");
            rbBanrural.setBounds(30, 130, 160, 26);
            grupoBancos.add(rbBanrural);
            panelBanrural.add(rbBanrural);
            panelContenedorGris.add(panelBanrural);

            // --- Panel BAC Credomatic ---
            JPanel panelBAC = crearCard(inicioX + 2 * (cardW + separacion), posY, cardW, cardH);
            panelBAC.setLayout(null);
            Image imgBAC = new ImageIcon(getClass().getResource("/gui/image/bac.png")).getImage();
            Image imgEscaladaBAC = imgBAC.getScaledInstance(360, 280, Image.SCALE_SMOOTH);

            JLabel lblImgBAC = new JLabel(new ImageIcon(imgEscaladaBAC));
            lblImgBAC.setBounds((cardW - 180) / 2, 30, 180, 100);
            panelBAC.add(lblImgBAC);

            rbBac = new NeoRadioButton("BAC Credomatic");
            rbBac.setBounds(30, 130, 160, 26);
            grupoBancos.add(rbBac);
            panelBAC.add(rbBac);
            panelContenedorGris.add(panelBAC);

            // --- Panel G&T Continental ---
            JPanel panelGYT = crearCard(inicioX + 3 * (cardW + separacion), posY, cardW, cardH);
            panelGYT.setLayout(null);
            Image imgGYT = new ImageIcon(getClass().getResource("/gui/image/gyt.png")).getImage();
            Image imgEscaladaGYT = imgGYT.getScaledInstance(340, 260, Image.SCALE_SMOOTH);

            JLabel lblImgGYT = new JLabel(new ImageIcon(imgEscaladaGYT));
            lblImgGYT.setBounds((cardW - 180) / 2, 25, 180, 100);
            panelGYT.add(lblImgGYT);

            rbGYT = new NeoRadioButton("G&T Continental");
            rbGYT.setBounds(30, 130, 160, 26);
            grupoBancos.add(rbGYT);
            panelGYT.add(rbGYT);
            panelContenedorGris.add(panelGYT);

            // ==========================================================
            // CORRECCIÓN: PANELES AL FRENTE (Agregados a panelContenedorGris)
            // ==========================================================
            
            // Panel de "Saldo Disponible" - Reposicionado simétricamente adentro
            JPanel panelSaldo = crearCardSinBorde(40, 320, 410, 110);
            panelSaldo.setLayout(null);
            JLabel lblSaldoTitulo = new JLabel("Saldo Disponible:");
            lblSaldoTitulo.setForeground(Color.WHITE);
            lblSaldoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblSaldoTitulo.setBounds(20, 15, 200, 25);
            panelSaldo.add(lblSaldoTitulo);

            lblSaldoValor = new JLabel("-");
            lblSaldoValor.setForeground(new Color(251, 232, 138));
            lblSaldoValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblSaldoValor.setBounds(20, 45, 300, 35);
            panelSaldo.add(lblSaldoValor);
            panelContenedorGris.add(panelSaldo); // <-- Antes decía panel.add

            // Panel de "Nombre del Banco" - Reposicionado simétricamente adentro
            JPanel panelBanco = crearCardSinBorde(480, 320, 410, 110);
            panelBanco.setLayout(null);
            JLabel lblBancoTitulo = new JLabel("Nombre del Banco:");
            lblBancoTitulo.setForeground(Color.WHITE);
            lblBancoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblBancoTitulo.setBounds(20, 15, 250, 25);
            panelBanco.add(lblBancoTitulo);

            lblBancoValor = new JLabel("-");
            lblBancoValor.setForeground(new Color(251, 232, 138));
            lblBancoValor.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblBancoValor.setBounds(20, 45, 300, 35);
            panelBanco.add(lblBancoValor);
            panelContenedorGris.add(panelBanco); // <-- Antes decía panel.add

            // ==========================================================
            // BOTÓN FLUJO DE BANCOS CON ESTILO REDONDEADO NEO
            // ==========================================================
            BotonFlujoNeo btnFlujo = new BotonFlujoNeo("→ Flujo de Bancos");
            btnFlujo.setBounds(930, 350, 280, 50); // Ajustado levemente en Y para equilibrar visualmente con los paneles
            panelContenedorGris.add(btnFlujo);
            btnFlujo.addActionListener(e -> {
                if (grupoBancos.getSelection() == null) {
                    JOptionPane.showMessageDialog(this,
                            "Debes seleccionar un banco antes de continuar.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (rbIndustrial.isSelected()) {
                    mostrarCuentasPorBanco(1);
                } else if (rbBanrural.isSelected()) {
                    mostrarCuentasPorBanco(3);
                } else if (rbBac.isSelected()) {
                    mostrarCuentasPorBanco(2);
                } else if (rbGYT.isSelected()) {
                    mostrarCuentasPorBanco(4);
                }
            });

            // ==========================================================
            // PANEL DE TABLA CON BORDE BLANCO ESTILIZADO DELGADO Y CURVO
            // ==========================================================
            JPanel panelTabla = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 180));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                    g2.setColor(new Color(255, 255, 255, 140));
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                    g2.dispose();
                }
            };
            panelTabla.setLayout(null);
            panelTabla.setOpaque(false);
            panelTabla.setBounds(40, 470, 1170, 440);
            panelContenedorGris.add(panelTabla);

            String[] columnas = {"Fecha", "Cuenta", "Monto", "Banco Conectado", "Estado"};
            modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable tabla = new JTable(modelo);
            tabla.setRowHeight(35);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);

            tabla.setGridColor(new Color(94, 116, 73));
            tabla.setSelectionBackground(new Color(251, 232, 138));
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(15, 15, 1140, 410);
            panelTabla.add(scroll);

            // Listener para actualizar paneles al seleccionar una fila
            tabla.getSelectionModel().addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                    int filaSeleccionada = tabla.getSelectedRow();

                    String banco = modelo.getValueAt(filaSeleccionada, 3).toString();
                    String saldo = modelo.getValueAt(filaSeleccionada, 2).toString();

                    lblBancoValor.setText(banco);
                    lblSaldoValor.setText("Q. " + saldo);
                }
            });

            // Al abrir la interfaz, cargar todas las cuentas del usuario
            try {
                Connection con = conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT c.saldo, c.estado, t.nombre AS tipoCuenta, b.nombre AS banco "
                        + "FROM cuentas_bancarias c "
                        + "JOIN tipos_cuentas t ON c.id_tipo_cuenta = t.id_tipo "
                        + "JOIN bancos b ON c.id_banco = b.id_banco "
                        + "WHERE c.id_usuario = ?"
                );
                ps.setInt(1, SesionUsuario.getIdUsuario());
                ResultSet rs = ps.executeQuery();

                modelo.setRowCount(0);

                boolean primeraFila = true;
                while (rs.next()) {
                    Object[] fila = {
                        java.time.LocalDate.now(),
                        rs.getString("tipoCuenta"),
                        rs.getDouble("saldo"),
                        rs.getString("banco"),
                        rs.getString("estado")
                    };
                    modelo.addRow(fila);

                    if (primeraFila) {
                        lblBancoValor.setText(rs.getString("banco"));
                        lblSaldoValor.setText("Q. " + rs.getDouble("saldo"));
                        primeraFila = false;
                    }
                }

                rs.close();
                ps.close();
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void mostrarCuentasPorBanco(int idBanco) {
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT c.saldo, c.estado, t.nombre AS tipoCuenta, b.nombre AS banco "
                    + "FROM cuentas_bancarias c "
                    + "JOIN tipos_cuentas t ON c.id_tipo_cuenta = t.id_tipo "
                    + "JOIN bancos b ON c.id_banco = b.id_banco "
                    + "WHERE c.id_usuario = ? AND c.id_banco = ?"
            );
            ps.setInt(1, SesionUsuario.getIdUsuario());
            ps.setInt(2, idBanco);
            ResultSet rs = ps.executeQuery();

            modelo.setRowCount(0);

            boolean primeraFila = true;
            while (rs.next()) {
                Object[] fila = {
                    java.time.LocalDate.now(),
                    rs.getString("tipoCuenta"),
                    rs.getDouble("saldo"),
                    rs.getString("banco"),
                    rs.getString("estado")
                };
                modelo.addRow(fila);

                if (primeraFila) {
                    lblBancoValor.setText(rs.getString("banco"));
                    lblSaldoValor.setText("Q. " + rs.getDouble("saldo"));
                    primeraFila = false;
                }
            }

            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No tienes cuentas registradas en este banco.");
                lblBancoValor.setText("-");
                lblSaldoValor.setText("Q. 0.00");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private JPanel crearCard(int x, int y, int w, int h) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 38, 35, 210));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.setColor(new Color(251, 232, 138));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 30, 30));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBounds(x, y, w, h);
        return panel;
    }

    private JPanel crearCardSinBorde(int x, int y, int w, int h) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 38, 35, 210));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBounds(x, y, w, h);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BancosConectados().setVisible(true));
    }
}