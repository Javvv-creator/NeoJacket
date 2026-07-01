package gui;

import java.awt.*;
import javax.swing.*;

public class AgregarDivisa extends JFrame {

    private Image fondo;
    private Image logo;

    // ============================
    // TEXTFIELD REDONDEADO
    // ============================
    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN NEO
    // ============================
    class BotonNeo extends JButton {
        private Color normal;
        private Color hover;

        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public AgregarDivisa() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Actualizar Divisa");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    // ============================
    // PANEL PRINCIPAL
    // ============================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearFormulario();
        }

        private void crearFormulario() {

            // LOGO
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(650, 120, 550, 600);
            panel.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panel);

            JLabel titulo = new JLabel("Actualizar tipo de cambio");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            // CAMPOS
            panel.add(crearLabel("Moneda:", 40, 90));
            RoundedTextField txtMoneda = crearField(40, 125);
            txtMoneda.setText("USD – Dólar Americano");
            txtMoneda.setEditable(false);
            panel.add(txtMoneda);

            panel.add(crearLabel("Tipo actual:", 40, 190));
            RoundedTextField txtActual = crearField(40, 225);
            txtActual.setText("7.75");
            txtActual.setEditable(false);
            panel.add(txtActual);

            panel.add(crearLabel("Nuevo tipo de cambio:", 40, 290));
            RoundedTextField txtNuevo = crearField(40, 325);
            panel.add(txtNuevo);

            panel.add(crearLabel("Fecha:", 40, 390));
            RoundedTextField txtFecha = crearField(40, 425);
            txtFecha.setText("31/05/2026");
            txtFecha.setEditable(false);
            panel.add(txtFecha);

            panel.add(crearLabel("Usuario:", 40, 490));
            RoundedTextField txtUsuario = crearField(40, 525);
            txtUsuario.setText("Admin1");
            txtUsuario.setEditable(false);
            panel.add(txtUsuario);

            // ============================
            // BOTONES (CORREGIDOS)
            // ============================
            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            BotonNeo btnCancelar = new BotonNeo("Cancelar", verde, verdeHover);
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setBounds(40, 520, 200, 55); // ← AHORA SÍ SE VE
            panel.add(btnCancelar);

            btnCancelar.addActionListener(e -> {
                new GestionDivisas();
                dispose();
            });

            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeo btnGuardar = new BotonNeo("Guardar cambios", amarillo, amarilloHover);
            btnGuardar.setBounds(260, 520, 250, 55); // ← AHORA SÍ SE VE
            panel.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
                JOptionPane.showMessageDialog(null,
                        "Tipo de cambio actualizado",
                        "Divisas",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }

        private JLabel crearLabel(String texto, int x, int y) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBounds(x, y, 300, 30);
            return lbl;
        }

        private RoundedTextField crearField(int x, int y) {
            RoundedTextField txt = new RoundedTextField(20);
            txt.setBounds(x, y, 450, 50);
            return txt;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

