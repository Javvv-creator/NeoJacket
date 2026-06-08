package gui;

import java.awt.*;
import javax.swing.*;

public class BloquearCuenta extends JFrame {

    private Image fondo;
    private Image logo;

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

    public BloquearCuenta() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Bloquear Cuenta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
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

                if (texto.equals("Gestión de Cuentas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                }

                if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            add(sidebar);
        }

        private void crearContenido() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Bloquear Cuenta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            Color amarillo = new Color(251, 232, 138);

            JLabel lblCuenta = new JLabel("Número de cuenta:");
            lblCuenta.setForeground(amarillo);
            lblCuenta.setBounds(30, 100, 200, 25);
            panel.add(lblCuenta);

            RoundedTextField txtCuenta = new RoundedTextField(20);
            txtCuenta.setBounds(250, 95, 300, 40);
            panel.add(txtCuenta);

            JLabel lblProp = new JLabel("Propietario:");
            lblProp.setForeground(amarillo);
            lblProp.setBounds(30, 160, 200, 25);
            panel.add(lblProp);

            RoundedTextField txtProp = new RoundedTextField(20);
            txtProp.setBounds(250, 155, 300, 40);
            panel.add(txtProp);

            JLabel lblTipo = new JLabel("Tipo de cuenta:");
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(30, 220, 200, 25);
            panel.add(lblTipo);

            RoundedTextField txtTipo = new RoundedTextField(20);
            txtTipo.setBounds(250, 215, 300, 40);
            panel.add(txtTipo);

            JLabel lblMotivo = new JLabel("Motivo del bloqueo:");
            lblMotivo.setForeground(amarillo);
            lblMotivo.setBounds(30, 300, 300, 25);
            panel.add(lblMotivo);

            JComboBox<String> cbMotivo = new JComboBox<>(new String[]{
                "Seleccione motivo",
                "Actividad sospechosa",
                "Solicitud del cliente",
                "Incumplimiento de normas"
            });
            cbMotivo.setBounds(30, 330, 350, 40);
            panel.add(cbMotivo);

            JLabel lblObs = new JLabel("Observaciones (opcional):");
            lblObs.setForeground(amarillo);
            lblObs.setBounds(30, 400, 300, 25);
            panel.add(lblObs);

            RoundedArea txtObs = new RoundedArea();
            JScrollPane scroll = new JScrollPane(txtObs);
            scroll.setBounds(30, 430, 500, 150);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            panel.add(scroll);

            BotonNeo btnCancelar = new BotonNeo("Cancelar");
            btnCancelar.setBounds(30, 620, 200, 50);
            btnCancelar.addActionListener(e -> {
                new GestionCuentas();
                dispose();
            });
            panel.add(btnCancelar);

            BotonNeo btnBloquear = new BotonNeo("Bloquear cuenta");
            btnBloquear.setBounds(250, 620, 250, 50);
            panel.add(btnBloquear);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

