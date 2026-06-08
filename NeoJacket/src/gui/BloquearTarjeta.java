package gui;

import java.awt.*;
import javax.swing.*;

public class BloquearTarjeta extends JFrame {

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

    // ============================
    // TEXTAREA REDONDEADA
    // ============================
    class RoundedArea extends JTextArea {
        public RoundedArea() {
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setLineWrap(true);
            setWrapStyleWord(true);
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

    // ============================
    // BOTÓN NEO
    // ============================
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

    // ============================
    // CONSTRUCTOR
    // ============================
    public BloquearTarjeta() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Bloquear Tarjeta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
            crearSidebar();
            crearContenido();
        }

        // ============================
        // SIDEBAR
        // ============================
        private void crearSidebar() {

            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] botones = {
                "Gestión de Usuarios",
                "Gestión de Menores Supervisados",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;

            for (String texto : botones) {
                JButton btn = new JButton(texto);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);

                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);

                if (texto.equals("Gestión de Tarjetas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                }

                if (texto.equals("Gestión de Tarjetas")) {
                    btn.addActionListener(e -> {
                        new GestionTarjeta();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            add(sidebar);
        }

        // ============================
        // CONTENIDO PRINCIPAL
        // ============================
        private void crearContenido() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Bloquear Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            Color amarillo = new Color(251, 232, 138);

            // ============================
            // DATOS DE LA TARJETA
            // ============================
            JLabel lblId = new JLabel("ID Tarjeta:");
            lblId.setForeground(amarillo);
            lblId.setBounds(30, 100, 200, 25);
            panel.add(lblId);

            RoundedTextField txtId = new RoundedTextField(20);
            txtId.setBounds(250, 95, 300, 40);
            panel.add(txtId);

            JLabel lblNum = new JLabel("Número de tarjeta:");
            lblNum.setForeground(amarillo);
            lblNum.setBounds(30, 160, 200, 25);
            panel.add(lblNum);

            RoundedTextField txtNum = new RoundedTextField(20);
            txtNum.setBounds(250, 155, 300, 40);
            panel.add(txtNum);

            JLabel lblProp = new JLabel("Propietario:");
            lblProp.setForeground(amarillo);
            lblProp.setBounds(30, 220, 200, 25);
            panel.add(lblProp);

            RoundedTextField txtProp = new RoundedTextField(20);
            txtProp.setBounds(250, 215, 300, 40);
            panel.add(txtProp);

            JLabel lblTipo = new JLabel("Tipo de tarjeta:");
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(30, 280, 200, 25);
            panel.add(lblTipo);

            RoundedTextField txtTipo = new RoundedTextField(20);
            txtTipo.setBounds(250, 275, 300, 40);
            panel.add(txtTipo);

            // ============================
            // MOTIVO DEL BLOQUEO
            // ============================
            JLabel lblMotivo = new JLabel("Motivo del bloqueo:");
            lblMotivo.setForeground(amarillo);
            lblMotivo.setBounds(30, 350, 300, 25);
            panel.add(lblMotivo);

            JComboBox<String> cbMotivo = new JComboBox<>(new String[]{
                "Seleccione motivo",
                "Actividad sospechosa",
                "Solicitud del cliente",
                "Incumplimiento de normas"
            });
            cbMotivo.setBounds(30, 380, 350, 40);
            panel.add(cbMotivo);

            // ============================
            // OBSERVACIONES
            // ============================
            JLabel lblObs = new JLabel("Observaciones (opcional):");
            lblObs.setForeground(amarillo);
            lblObs.setBounds(30, 450, 300, 25);
            panel.add(lblObs);

            RoundedArea txtObs = new RoundedArea();
            JScrollPane scroll = new JScrollPane(txtObs);
            scroll.setBounds(30, 480, 520, 150);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            panel.add(scroll);

            // ============================
            // BOTONES
            // ============================
            BotonNeo btnCancelar = new BotonNeo("Cancelar");
            btnCancelar.setBounds(30, 660, 200, 50);
            btnCancelar.addActionListener(e -> {
                new GestionTarjeta();
                dispose();
            });
            panel.add(btnCancelar);

            BotonNeo btnBloquear = new BotonNeo("Bloquear Tarjeta");
            btnBloquear.setBounds(250, 660, 250, 50);
            panel.add(btnBloquear);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

