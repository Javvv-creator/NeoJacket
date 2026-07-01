package gui;

import java.awt.*;
import javax.swing.*;

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
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

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
                    if (textoBtn.equals("Bancos conectados")) {
                        new BancosConectados().setVisible(true);
                    } else if (textoBtn.equals("Transferencias")) {
                        new Transferencias().setVisible(true);
                    } else if (textoBtn.equals("Divisas")) {
                        new Divisas().setVisible(true);
                    } else if (textoBtn.equals("Historial")) {
                        new Historial().setVisible(true);
                    }
                    dispose();
                });
                sidebar.add(btn);
                y += 70;
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

            add(sidebar);
        }

        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 180));
            contenedor.setBounds(350, 60, 1300, 760); // bajamos todo un poco
            add(contenedor);

            // Barra superior con pestañas
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(94, 116, 73, 200));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 434, 55);
            btnTab1.setBackground(new Color(251, 232, 138, 200));
            btnTab1.setForeground(Color.WHITE);
            btnTab1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            barraSuperior.add(btnTab1);

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 434, 55);
            btnTab2.setBackground(new Color(25, 38, 35, 100));
            btnTab2.setForeground(Color.BLACK);
            btnTab2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab2.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });
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

            // ============================
            // Carta con el formulario
            // ============================
            JPanel cartaCampos = new JPanel();
            cartaCampos.setLayout(null);
            cartaCampos.setBounds(400, 100, 500, 500); // centrada y más abajo
            cartaCampos.setBackground(new Color(25, 38, 35, 150));
            cartaCampos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2));
            contenedor.add(cartaCampos);

            // Formulario dentro de la carta
            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(30, 30, 400, 25);
            cartaCampos.add(lblBanco);

            String[] opcionesBancos = {"Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(30, 60, 400, 40);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(25, 38, 35));
            cbBancos.setForeground(Color.WHITE);
            cartaCampos.add(cbBancos);

            JLabel lblMonto = new JLabel("Monto a ingresar");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(tituloCampos);
            lblMonto.setBounds(30, 120, 400, 25);
            cartaCampos.add(lblMonto);

            JTextFieldBordeAmarillo txtMonto = new JTextFieldBordeAmarillo();
            txtMonto.setBounds(30, 150, 400, 40);
            txtMonto.setFont(textoInputs);
            cartaCampos.add(txtMonto);

            JLabel lblTarjeta = new JLabel("Número de tarjeta");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(30, 210, 400, 25);
            cartaCampos.add(lblTarjeta);

            JTextFieldBordeAmarillo txtTarjeta = new JTextFieldBordeAmarillo();
            txtTarjeta.setBounds(30, 240, 400, 40);
            txtTarjeta.setFont(textoInputs);
            cartaCampos.add(txtTarjeta);

            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(30, 300, 400, 25);
            cartaCampos.add(lblDescripcion);

            JTextFieldBordeAmarillo txtDescripcion = new JTextFieldBordeAmarillo();
            txtDescripcion.setBounds(30, 330, 400, 40);
            txtDescripcion.setFont(textoInputs);
            cartaCampos.add(txtDescripcion);

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
            btnGuardar.setBounds(30, 400, 400, 50);
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cartaCampos.add(btnGuardar);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
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
