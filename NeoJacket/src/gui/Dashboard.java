package gui;

import java.awt.*;
import javax.swing.*;
import funcionalidades.SupervisionDAO;

public class Dashboard extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    private Integer idUsuarioActual;

    private final Color amarilloPastel = new Color(251, 232, 138);

    // ==========================================
    // TEXTFIELD REDONDEADO
    // ==========================================
    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ==========================================
    // BOTÓN DE NAVEGACIÓN DEL SIDEBAR
    // ==========================================
    class BotonSidebarNeo extends JButton {

        public BotonSidebarNeo(String texto) {
            super(texto);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setBackground(new Color(0, 0, 0, 0));
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    setBackground(amarilloPastel);
                    setForeground(Color.BLACK);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    setBackground(new Color(0, 0, 0, 0));
                    setForeground(Color.WHITE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            if (getBackground().getAlpha() == 0) {
                g2.setColor(amarilloPastel);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==========================================
    // BOTÓN NEO 
    // ==========================================
    class BotonNeo extends JButton {

        private Color normal;
        private Color hover;
        private Color colorTexto;

        // Constructor original (usa la paleta verde/dorada por defecto)
        public BotonNeo(String texto) {
            this(texto, new Color(94, 116, 73, 190), new Color(251, 232, 138, 220), Color.WHITE);
        }

        // Constructor flexible, para botones con su propia paleta (ej. Cerrar sesión, Supervisión)
        public BotonNeo(String texto, Color normal, Color hover, Color colorTexto) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            this.colorTexto = colorTexto;

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

    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public Dashboard() {
        this(null);
    }

    public Dashboard(Integer idUsuarioActual) {
        this.idUsuarioActual = idUsuarioActual;
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new FondoPanel());
    }

    // ==========================================
    // PANEL PRINCIPAL
    // ==========================================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
        }

        // ==========================================
        // SIDEBAR Y ENLACES DE CONTROL
        // ==========================================
        private void crearSidebar() {
            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                    g2.dispose();
                }
            };
            sidebar.setOpaque(false);
            sidebar.setLayout(null);
            sidebar.setBounds(20, 20, 300, 870);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] botonesMenu = {
                "Saldos",
                "Bancos conectados",
                "Transferencias",
                "Historial"
            };

            int y = 140;
            for (String textoBtn : botonesMenu) {
                BotonSidebarNeo btn = new BotonSidebarNeo(textoBtn);
                btn.setBounds(20, y, 250, 55);

                // Enrutador de acciones para la navegación lateral
                if (textoBtn.equals("Saldos")) {
                    btn.addActionListener(e -> { 
                        new Saldos().setVisible(true);
                        dispose(); 
                    });
                }
                if (textoBtn.equals("Bancos conectados")) {
                    btn.addActionListener(e -> { 
                        new BancosConectados().setVisible(true);
                        dispose(); 
                    });
                }
                if (textoBtn.equals("Transferencias")) {
                    btn.addActionListener(e -> { 
                        new Transferencias().setVisible(true);
                        dispose(); 
                    });
                }
                if (textoBtn.equals("Historial")) {
                    btn.addActionListener(e -> { 
                        new Historial().setVisible(true);
                        dispose(); 
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            // Botón Supervisión — aparece solo si el usuario tiene menores a cargo
            if (idUsuarioActual != null) {
                SupervisionDAO dao = new SupervisionDAO();
                if (dao.tieneMenoresACargo(idUsuarioActual)) {
                    BotonNeo btnSupervision = new BotonNeo(
                            "Supervisión",
                            new Color(251, 232, 138),
                            new Color(255, 245, 180),
                            new Color(25, 38, 35));
                    btnSupervision.setBounds(20, y, 250, 55);
                    btnSupervision.addActionListener(e -> {
                        new PanelSupervision(idUsuarioActual).setVisible(true);
                        dispose();
                    });
                    sidebar.add(btnSupervision);
                }
            }

            BotonNeo btnCerrarSesion = new BotonNeo(
                    "Cerrar sesión",
                    new Color(191, 76, 58),
                    new Color(214, 100, 80),
                    Color.WHITE);
            btnCerrarSesion.setBounds(20, 800, 250, 55);
            btnCerrarSesion.addActionListener(e -> {
                JOptionPane.showMessageDialog(null,
                    "✅ Sesión cerrada correctamente.\n¡Hasta pronto!",
                    "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            add(sidebar);
        }

        // ==========================================
        // CONTENIDO PRINCIPAL
        // ==========================================
        private void crearContenido() {
            Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 34);
            Font tituloTarjeta = new Font("Segoe UI", Font.BOLD, 16);
            Font texto = new Font("Segoe UI", Font.PLAIN, 14);
            Color amarilloPastel = new Color(251, 232, 138);

            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 20, 1250, 950);
            add(contenedor);

            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1250, 60);
            barraSuperior.setBackground(amarilloPastel);
            contenedor.add(barraSuperior);

            JLabel lblTituloDashboard = new JLabel("Panel de Control");
            lblTituloDashboard.setForeground(Color.WHITE);
            lblTituloDashboard.setFont(tituloSeccion);
            lblTituloDashboard.setBounds(40, 15, 400, 45);
            contenedor.add(lblTituloDashboard);

            // TARJETA CUENTAS
            RoundedPanel cuentas = new RoundedPanel();
            cuentas.setBounds(40, 90, 1160, 120);
            cuentas.setBackground(new Color(25, 38, 35, 180));
            cuentas.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            cuentas.setLayout(null);
            contenedor.add(cuentas);

            JLabel lblCuentas = new JLabel("Tus Cuentas");
            lblCuentas.setForeground(amarilloPastel);
            lblCuentas.setFont(tituloTarjeta);
            lblCuentas.setBounds(20, 10, 200, 30);
            cuentas.add(lblCuentas);

            JLabel saldo = new JLabel("Saldo Disponible: Q25,430.50");
            saldo.setForeground(Color.WHITE);
            saldo.setFont(texto);
            saldo.setBounds(20, 50, 300, 30);
            cuentas.add(saldo);

            // TARJETA BANCOS
            RoundedPanel bancos = new RoundedPanel();
            bancos.setBounds(40, 240, 350, 180);
            bancos.setBackground(new Color(25, 38, 35, 180));
            bancos.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            bancos.setLayout(null);
            contenedor.add(bancos);

            JLabel lblBancos = new JLabel("Bancos Conectados");
            lblBancos.setForeground(amarilloPastel);
            lblBancos.setFont(tituloTarjeta);
            lblBancos.setBounds(15, 10, 200, 25);
            bancos.add(lblBancos);

            JLabel banco1 = new JLabel("• Banco Industrial");
            banco1.setForeground(Color.WHITE);
            banco1.setFont(texto);
            banco1.setBounds(20, 50, 200, 20);
            bancos.add(banco1);

            JLabel banco2 = new JLabel("• Banrural");
            banco2.setForeground(Color.WHITE);
            banco2.setFont(texto);
            banco2.setBounds(20, 80, 200, 20);
            bancos.add(banco2);

            // TARJETA TRANSFERENCIAS
            RoundedPanel transferencias = new RoundedPanel();
            transferencias.setBounds(420, 240, 780, 180);
            transferencias.setBackground(new Color(25, 38, 35, 180));
            transferencias.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            transferencias.setLayout(null);
            contenedor.add(transferencias);

            JLabel lblTransfer = new JLabel("Transferencias Recientes");
            lblTransfer.setForeground(amarilloPastel);
            lblTransfer.setFont(tituloTarjeta);
            lblTransfer.setBounds(15, 10, 250, 25);
            transferencias.add(lblTransfer);

            JLabel t1 = new JLabel("Ana López - Q250");
            t1.setForeground(Color.WHITE);
            t1.setFont(texto);
            t1.setBounds(20, 50, 200, 20);
            transferencias.add(t1);

            JLabel t2 = new JLabel("Carlos Ruiz - Q800");
            t2.setForeground(Color.WHITE);
            t2.setFont(texto);
            t2.setBounds(20, 80, 200, 20);
            transferencias.add(t2);

            // TARJETA HISTORIAL
            RoundedPanel historial = new RoundedPanel();
            historial.setBounds(40, 450, 350, 200);
            historial.setBackground(new Color(25, 38, 35, 180));
            historial.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            historial.setLayout(null);
            contenedor.add(historial);

            JLabel lblHistorial = new JLabel("Historial");
            lblHistorial.setForeground(amarilloPastel);
            lblHistorial.setFont(tituloTarjeta);
            lblHistorial.setBounds(15, 10, 100, 25);
            historial.add(lblHistorial);

            JLabel h1 = new JLabel("✓ Inicio de sesión");
            h1.setForeground(Color.WHITE);
            h1.setFont(texto);
            h1.setBounds(20, 50, 200, 20);
            historial.add(h1);

            JLabel h2 = new JLabel("✓ Transferencia");
            h2.setForeground(Color.WHITE);
            h2.setFont(texto);
            h2.setBounds(20, 80, 200, 20);
            historial.add(h2);

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ==========================================
    // CONTROL DE BORDES REDONDEADOS
    // ==========================================
    class RoundedPanel extends JPanel {
        public RoundedPanel() {
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g2);
            g2.dispose();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}