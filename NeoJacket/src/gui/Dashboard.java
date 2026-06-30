package gui;

import gui.Saldos; // Importación directa de la interfaz de Saldos
import javax.swing.*;
import java.awt.*;

public class Dashboard extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

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
    // BOTÓN NEO
    // ==========================================
    class BotonNeo extends JButton {
        public BotonNeo(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover()
                    ? new Color(251, 232, 138, 220)
                    : new Color(94, 116, 73, 190));
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
            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] botonesMenu = {
                "Saldos",
                "Bancos",
                "Transferencias",
                "Divisas",
                "Historial"
            };

            int y = 140;
            for (String textoBtn : botonesMenu) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);

                // Enrutador de acciones para la navegación lateral
                if (textoBtn.equals("Saldos")) {
                    btn.addActionListener(e -> { 
                        new Saldos().setVisible(true);
                        dispose(); 
                    });
                }
                if (textoBtn.equals("Bancos")) {
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
                if (textoBtn.equals("Divisas")) {
                    btn.addActionListener(e -> { 
                        new GestionDivisas().setVisible(true);
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
            add(sidebar);
        }

        // ==========================================
        // CONTENIDO PRINCIPAL
        // ==========================================
        private void crearContenido() {
            Font titulo = new Font("Segoe UI", Font.BOLD, 18);
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

            // TARJETA CUENTAS
            RoundedPanel cuentas = new RoundedPanel();
            cuentas.setBounds(40, 90, 1160, 120);
            cuentas.setBackground(new Color(25, 38, 35, 180));
            cuentas.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            cuentas.setLayout(null);
            contenedor.add(cuentas);

            JLabel lblCuentas = new JLabel("Tus Cuentas");
            lblCuentas.setForeground(amarilloPastel);
            lblCuentas.setFont(titulo);
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
            lblBancos.setFont(titulo);
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
            lblTransfer.setFont(titulo);
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
            lblHistorial.setFont(titulo);
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

            // TARJETA DIVISAS
            RoundedPanel divisas = new RoundedPanel();
            divisas.setBounds(420, 450, 780, 200);
            divisas.setBackground(new Color(25, 38, 35, 180));
            divisas.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            divisas.setLayout(null);
            contenedor.add(divisas);

            JLabel lblDivisasTarjeta = new JLabel("Cambio de Divisas");
            lblDivisasTarjeta.setForeground(amarilloPastel);
            lblDivisasTarjeta.setFont(titulo);
            lblDivisasTarjeta.setBounds(15, 10, 200, 25);
            divisas.add(lblDivisasTarjeta);

            RoundedTextField txtCantidad = new RoundedTextField(20);
            txtCantidad.setBounds(20, 55, 180, 35);
            divisas.add(txtCantidad);

            BotonNeo btnConvertir = new BotonNeo("Convertir");
            btnConvertir.setBounds(220, 55, 130, 35);
            divisas.add(btnConvertir);

            JLabel resultado = new JLabel("1 USD = Q7.72");
            resultado.setForeground(Color.WHITE);
            resultado.setFont(texto);
            resultado.setBounds(20, 110, 200, 20);
            divisas.add(resultado);
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
