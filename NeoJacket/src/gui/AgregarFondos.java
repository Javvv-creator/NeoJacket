package gui;

import javax.swing.*;
import java.awt.*;

public class AgregarFondos extends JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public AgregarFondos() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Agregar Fondos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

    // ============================
    // PANEL PRINCIPAL CON FONDO
    // ============================
    class FondoPanel extends JPanel {
        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
        }
        
        
        

        // ==========================================
        // DISEÑO DEL MENÚ LATERAL (SIDEBAR)
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

            JButton btnSaldosActivo = new JButton("Saldos");
            btnSaldosActivo.setBounds(20, 140, 250, 55);
            btnSaldosActivo.setFocusPainted(false);
            btnSaldosActivo.setBorderPainted(false);
            btnSaldosActivo.setBackground(new Color(251, 232, 138));
            btnSaldosActivo.setForeground(Color.BLACK);
            btnSaldosActivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            sidebar.add(btnSaldosActivo);

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

        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 180));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // Formulario
            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(50, 40, 400, 25);
            contenedor.add(lblBanco);

            String[] opcionesBancos = {"Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(50, 70, 400, 40);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(25, 38, 35));
            cbBancos.setForeground(Color.WHITE);
            contenedor.add(cbBancos);

            JLabel lblMonto = new JLabel("Monto a ingresar");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(tituloCampos);
            lblMonto.setBounds(50, 130, 400, 25);
            contenedor.add(lblMonto);

            JTextFieldBordeAmarillo txtMonto = new JTextFieldBordeAmarillo();
            txtMonto.setBounds(50, 160, 400, 40);
            txtMonto.setFont(textoInputs);
            contenedor.add(txtMonto);

            JLabel lblTarjeta = new JLabel("Número de tarjeta");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(50, 220, 400, 25);
            contenedor.add(lblTarjeta);

            JTextFieldBordeAmarillo txtTarjeta = new JTextFieldBordeAmarillo();
            txtTarjeta.setBounds(50, 250, 400, 40);
            txtTarjeta.setFont(textoInputs);
            contenedor.add(txtTarjeta);

            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(50, 310, 400, 25);
            contenedor.add(lblDescripcion);

            JTextFieldBordeAmarillo txtDescripcion = new JTextFieldBordeAmarillo();
            txtDescripcion.setBounds(50, 340, 400, 40);
            txtDescripcion.setFont(textoInputs);
            contenedor.add(txtDescripcion);

            JButton btnGuardar = new JButton("Guardar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(251, 232, 138));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnGuardar.setBounds(50, 410, 400, 50);
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            contenedor.add(btnGuardar);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ============================
    // CAMPO DE TEXTO CON BORDE AMARILLO
    // ============================
    class JTextFieldBordeAmarillo extends JTextField {
        public JTextFieldBordeAmarillo() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo oscuro
            g2.setColor(new Color(25, 38, 35));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            // Borde amarillo
            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ============================
    // MAIN
    // ============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AgregarFondos().setVisible(true);
        });
    }
}
