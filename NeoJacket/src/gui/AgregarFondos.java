package gui;

import javax.swing.*;
import java.awt.*;

public class AgregarFondos extends javax.swing.JFrame {

    // ==========================================
    // VARIABLES DE CONFIGURACIÓN ESTÉTICA
    // ==========================================
    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public AgregarFondos() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Agregar Fondos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new FondoPanel());
    }

    // ==========================================
    // PANEL DE CAPA PRINCIPAL (CON FONDO REUTILIZADO)
    // ==========================================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
        }

        // ==========================================
        // DISEÑO DEL MENÚ LATERAL (SIDEBAR MATCHING)
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

            JButton btnSaldos = new JButton("Saldos");
            btnSaldos.setBounds(20, 140, 250, 55);
            btnSaldos.setFocusPainted(false);
            btnSaldos.setBorderPainted(false);
            btnSaldos.setBackground(new Color(251, 232, 138));
            btnSaldos.setForeground(Color.BLACK);
            btnSaldos.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnSaldos.addActionListener(e -> {
                new Saldos().setVisible(true);
                dispose();
            });
            sidebar.add(btnSaldos);

            String[] botonesMenu = {"Bancos conectados", "Transferencias", "Divisas", "Historial"};
            int y = 210;

            for (String textoBtn : botonesMenu) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                btn.addActionListener(e -> {
                    new Dashboard().setVisible(true);
                    dispose();
                });
                
                sidebar.add(btn);
                y += 70;
            }
            add(sidebar);
        }

        // ==========================================
        // DISEÑO DEL PANEL DE CONTENIDO CENTRAL
        // ==========================================
        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(94, 116, 73, 200));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 434, 55);
            btnTab1.setBackground(new Color(251, 232, 138, 200)); 
            btnTab1.setForeground(Color.BLACK);
            btnTab1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab1.setFocusPainted(false);
            btnTab1.setBorder(BorderFactory.createEmptyBorder());
            barraSuperior.add(btnTab1);

            JButton btnTab2 = crearBotonPestaña("Actualizar Saldos", 433);
            barraSuperior.add(btnTab2);

            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 866);
            barraSuperior.add(btnTab3);

            // ==========================================
            // FORMULARIO DE RECAUDACIÓN (CENTRADO EN PANEL)
            // ==========================================
            PanelFormularioRedondeado panelForm = new PanelFormularioRedondeado();
            panelForm.setBounds(440, 120, 420, 490);
            panelForm.setLayout(null);
            contenedor.add(panelForm);

            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(25, 20, 370, 25);
            panelForm.add(lblBanco);

            String[] opcionesBancos = {" -- Elige un banco disponible -- ", "Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(25, 50, 370, 45);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(Color.WHITE);
            panelForm.add(cbBancos);

            JLabel lblMonto = new JLabel("Monto a ingresar");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(tituloCampos);
            lblMonto.setBounds(25, 115, 370, 25);
            panelForm.add(lblMonto);

            JTextFieldRedondeado txtMonto = new JTextFieldRedondeado();
            txtMonto.setBounds(25, 145, 370, 45);
            txtMonto.setFont(textoInputs);
            panelForm.add(txtMonto);

            JLabel lblTarjeta = new JLabel("Número de tarjeta");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(25, 210, 370, 25);
            panelForm.add(lblTarjeta);

            JTextFieldRedondeado txtTarjeta = new JTextFieldRedondeado();
            txtTarjeta.setBounds(25, 240, 370, 45);
            txtTarjeta.setFont(textoInputs);
            panelForm.add(txtTarjeta);

            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(25, 305, 370, 25);
            panelForm.add(lblDescripcion);

            JTextFieldRedondeado txtDescripcion = new JTextFieldRedondeado();
            txtDescripcion.setBounds(25, 335, 370, 45); 
            txtDescripcion.setFont(textoInputs);
            panelForm.add(txtDescripcion);

            // Botón integrado estéticamente abajo del formulario
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
            btnGuardar.setBounds(25, 410, 370, 50);
            btnGuardar.setBackground(new Color(251, 232, 138)); 
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnGuardar.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "¡Fondos registrados con éxito!");
            });
            panelForm.add(btnGuardar);
        }

        // ==========================================
        // MÉTODOS COMPLEMENTARIOS
        // ==========================================
        private JButton crearBotonPestaña(String texto, int xPos) {
            JButton btn = new JButton(texto);
            btn.setBounds(xPos, 0, 434, 55);
            btn.setBackground(new Color(25, 38, 35, 100));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            
            btn.addActionListener(e -> {
                if (texto.equals("Actualizar Saldos") || texto.equals("Consultar Saldos")) {
                    new Saldos().setVisible(true); 
                    dispose();
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

    // ==========================================
    // CLASES INTERNAS (ESTILIZADO PERSONALIZADO)
    // ==========================================
    class PanelFormularioRedondeado extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(94, 116, 73, 190));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

            g2.dispose();
        }
    }

    class JTextFieldRedondeado extends JTextField {
        public JTextFieldRedondeado() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==========================================
    // CONSTRUCCIÓN INTERNA DE RESPALDO (IDE)
    // ==========================================
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

    // ==========================================
    // EJECUCIÓN PRINCIPAL
    // ==========================================
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new AgregarFondos().setVisible(true);
        });
    }
}
