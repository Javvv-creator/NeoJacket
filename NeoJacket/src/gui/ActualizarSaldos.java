package gui;

import javax.swing.*;
import java.awt.*;

public class ActualizarSaldos extends JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public ActualizarSaldos() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Actualizar Saldos");
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

        private void crearSidebar() {
            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] botonesMenu = {"Saldos", "Bancos conectados", "Transferencias", "Divisas", "Historial"};
            int y = 140;
            for (String textoBtn : botonesMenu) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
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
            
              JPanel cartaCampos = new JPanel();
            cartaCampos.setLayout(null);
            cartaCampos.setBounds(50, 80, 600, 300);
            cartaCampos.setBackground(new Color(25, 38, 35, 150));
            cartaCampos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2));

            // Barra superior con pestañas
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(94, 116, 73, 200));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 434, 55);
            btnTab1.setBackground(new Color(25, 38, 35, 100));
            btnTab1.setForeground(Color.WHITE);
            btnTab1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab1.addActionListener(e -> {
                new AgregarFondos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab1);

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 434, 55);
            btnTab2.setBackground(new Color(251, 232, 138, 200));
            btnTab2.setForeground(Color.BLACK);
            btnTab2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            barraSuperior.add(btnTab2);

            JButton btnTab3 = new JButton("Consultar Saldos");
            btnTab3.setBounds(866, 0, 434, 55);
            btnTab3.setBackground(new Color(25, 38, 35, 100));
            btnTab3.setForeground(Color.WHITE);
            btnTab3.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab3.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab3);

           

            // Banco dirigido
            JLabel lblBanco = new JLabel("Banco dirigido");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(20, 20, 200, 25);
            cartaCampos.add(lblBanco);

            String[] opcionesBancos = {"Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(20, 50, 550, 35);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(25, 38, 35));
            cbBancos.setForeground(Color.WHITE);
            cbBancos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 1));
            cartaCampos.add(cbBancos);

            // Monto actual
            JLabel lblMontoActual = new JLabel("Monto actual");
            lblMontoActual.setForeground(Color.WHITE);
            lblMontoActual.setFont(tituloCampos);
            lblMontoActual.setBounds(20, 100, 200, 25);
            cartaCampos.add(lblMontoActual);

            JTextField txtMontoActual = new JTextField("500");
            txtMontoActual.setBounds(20, 130, 550, 35);
            txtMontoActual.setBackground(new Color(25, 38, 35));
            txtMontoActual.setForeground(Color.WHITE);
            txtMontoActual.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 1));
            cartaCampos.add(txtMontoActual);

            // Nuevo monto
            JLabel lblNuevoMonto = new JLabel("Nuevo monto");
            lblNuevoMonto.setForeground(Color.WHITE);
            lblNuevoMonto.setFont(tituloCampos);
            lblNuevoMonto.setBounds(20, 180, 200, 25);
            cartaCampos.add(lblNuevoMonto);

            JTextField txtNuevoMonto = new JTextField();
            txtNuevoMonto.setBounds(20, 210, 550, 35);
            txtNuevoMonto.setBackground(new Color(25, 38, 35));
            txtNuevoMonto.setForeground(Color.WHITE);
            txtNuevoMonto.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 1));
            cartaCampos.add(txtNuevoMonto);

            contenedor.add(cartaCampos);

            // ============================
            // Panel Movimientos disponibles
            // ============================
            JPanel panelMovimientos = new JPanel();
            panelMovimientos.setLayout(null);
            panelMovimientos.setBounds(700, 80, 550, 200);
            panelMovimientos.setBackground(new Color(25, 38, 35, 150));
            panelMovimientos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2));

            JLabel lblMov = new JLabel("Movimientos disponibles");
            lblMov.setForeground(Color.WHITE);
            lblMov.setFont(tituloCampos);
            lblMov.setBounds(20, 10, 400, 25);
            panelMovimientos.add(lblMov);

            JLabel lblMontoIng = new JLabel("Monto ingresado: 500");
            lblMontoIng.setForeground(Color.WHITE);
            lblMontoIng.setBounds(20, 50, 400, 25);
            panelMovimientos.add(lblMontoIng);

            JLabel lblFecha = new JLabel("Fecha: DD/MM/AAAA");
            lblFecha.setForeground(Color.WHITE);
            lblFecha.setBounds(20, 80, 400, 25);
            panelMovimientos.add(lblFecha);

            contenedor.add(panelMovimientos);

            // ============================
            // Panel Motivo de la actualización
            // ============================
            JPanel panelMotivo = new JPanel();
            panelMotivo.setLayout(null);
            panelMotivo.setBounds(700, 300, 550, 100);
            panelMotivo.setBackground(new Color(25, 38, 35, 150));
            panelMotivo.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2));

            JLabel lblMotivo = new JLabel("Motivo de la actualización");
            lblMotivo.setForeground(Color.WHITE);
            lblMotivo.setFont(tituloCampos);
            lblMotivo.setBounds(20, 10, 400, 25);
            panelMotivo.add(lblMotivo);

            JTextField txtMotivo = new JTextField();
            txtMotivo.setBounds(20, 40, 500, 40);
            txtMotivo.setBackground(new Color(25, 38, 35));
            txtMotivo.setForeground(Color.WHITE);
            txtMotivo.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 1));
            panelMotivo.add(txtMotivo);

            contenedor.add(panelMotivo);

            // ============================
            // Botón Guardar
            // ============================
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
            btnGuardar.setBounds(50, 420, 600, 50);
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

    // ===============================
    // MAIN
    // ===============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ActualizarSaldos().setVisible(true);
        });
    }
}