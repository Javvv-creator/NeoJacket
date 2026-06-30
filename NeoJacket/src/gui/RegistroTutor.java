package gui;

import java.awt.*;
import javax.swing.*;

public class RegistroTutor extends JFrame {

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
            setBorder(BorderFactory.createEmptyBorder(8, 40, 8, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // PASSFIELD REDONDEADO
    // ============================
    class RoundedPassField extends JPasswordField {
        public RoundedPassField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 40, 8, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

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
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color amarillo = new Color(251, 232, 138);
            Color hover = new Color(255, 245, 180);

            g2.setColor(getModel().isRollover() ? hover : amarillo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public RegistroTutor() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Cuenta Tutor");
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
            crearTutor();
        }

        private void crearTutor() {

            // LOGO ARRIBA
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL SEMITRANSPARENTE CENTRADO
            JPanel panelTutor = new JPanel();
            panelTutor.setLayout(null);
            panelTutor.setBackground(new Color(25, 38, 35, 180));
            panelTutor.setBounds(750, 150, 420, 450);
            panelTutor.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panelTutor);

            // TÍTULO
            JLabel titulo = new JLabel("Cuenta Tutor");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panelTutor.add(titulo);

            // CAMPOS
            JLabel lblUsuario = new JLabel("Usuario");
            lblUsuario.setForeground(Color.WHITE);
            lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblUsuario.setBounds(40, 90, 200, 30);
            panelTutor.add(lblUsuario);

            RoundedTextField txtUsuario = new RoundedTextField(20);
            txtUsuario.setBounds(40, 125, 330, 50);
            panelTutor.add(txtUsuario);

            JLabel lblPass = new JLabel("Contraseña");
            lblPass.setForeground(Color.WHITE);
            lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblPass.setBounds(40, 190, 200, 30);
            panelTutor.add(lblPass);

            RoundedPassField txtPass = new RoundedPassField(20);
            txtPass.setBounds(40, 225, 330, 50);
            panelTutor.add(txtPass);

            JLabel lblIdent = new JLabel("Número de identificación");
            lblIdent.setForeground(Color.WHITE);
            lblIdent.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblIdent.setBounds(40, 290, 300, 30);
            panelTutor.add(lblIdent);

            RoundedTextField txtIdent = new RoundedTextField(20);
            txtIdent.setBounds(40, 325, 330, 50);
            panelTutor.add(txtIdent);

            // BOTÓN INGRESAR
            BotonNeo btnIngresar = new BotonNeo("Ingresar");
            btnIngresar.setBounds(40, 390, 330, 55);
            panelTutor.add(btnIngresar);


        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

