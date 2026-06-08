package gui;

import java.awt.*;
import javax.swing.*;

public class RegistroNeo extends JFrame {

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
    public RegistroNeo() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Registro");
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
            crearRegistro();
        }

        private void crearRegistro() {

            // LOGO ARRIBA
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL SEMITRANSPARENTE CENTRADO
            JPanel panelReg = new JPanel();
            panelReg.setLayout(null);
            panelReg.setBackground(new Color(25, 38, 35, 180));
            panelReg.setBounds(700, 120, 500, 650);
            panelReg.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panelReg);

            // TÍTULO
            JLabel titulo = new JLabel("NEO JACKET - BANCA EN LÍNEA");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panelReg.add(titulo);

            // CAMPOS
            JLabel lblUsuario = new JLabel("Usuario");
            lblUsuario.setForeground(Color.WHITE);
            lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblUsuario.setBounds(40, 90, 200, 30);
            panelReg.add(lblUsuario);

            RoundedTextField txtUsuario = new RoundedTextField(20);
            txtUsuario.setBounds(40, 125, 400, 50);
            panelReg.add(txtUsuario);

            JLabel lblPass = new JLabel("Contraseña");
            lblPass.setForeground(Color.WHITE);
            lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblPass.setBounds(40, 190, 200, 30);
            panelReg.add(lblPass);

            RoundedPassField txtPass = new RoundedPassField(20);
            txtPass.setBounds(40, 225, 400, 50);
            panelReg.add(txtPass);

            JLabel lblTipoCuenta = new JLabel("Tipo de cuenta");
            lblTipoCuenta.setForeground(Color.WHITE);
            lblTipoCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblTipoCuenta.setBounds(40, 290, 200, 30);
            panelReg.add(lblTipoCuenta);

            JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Monetaria"});
            cbTipo.setBounds(40, 325, 400, 50);
            panelReg.add(cbTipo);

            JLabel lblPerfil = new JLabel("Tipo de perfil");
            lblPerfil.setForeground(Color.WHITE);
            lblPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblPerfil.setBounds(40, 390, 200, 30);
            panelReg.add(lblPerfil);

            JRadioButton rbAdulto = new JRadioButton("Adulto");
            rbAdulto.setBounds(40, 425, 100, 30);
            rbAdulto.setOpaque(false);
            rbAdulto.setForeground(Color.WHITE);

            JRadioButton rbMenor = new JRadioButton("Menor supervisado");
            rbMenor.setBounds(150, 425, 200, 30);
            rbMenor.setOpaque(false);
            rbMenor.setForeground(Color.WHITE);

            ButtonGroup grupo = new ButtonGroup();
            grupo.add(rbAdulto);
            grupo.add(rbMenor);

            panelReg.add(rbAdulto);
            panelReg.add(rbMenor);

            JLabel lblIdent = new JLabel("Número de identificación");
            lblIdent.setForeground(Color.WHITE);
            lblIdent.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblIdent.setBounds(40, 470, 300, 30);
            panelReg.add(lblIdent);

            RoundedTextField txtIdent = new RoundedTextField(20);
            txtIdent.setBounds(40, 505, 400, 50);
            panelReg.add(txtIdent);

            // BOTÓN AGREGAR TUTOR (más pequeño)
            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            BotonNeo btnTutor = new BotonNeo("Agregar cuenta tutor", verde, verdeHover);
            btnTutor.setForeground(Color.WHITE);
            btnTutor.setBounds(40, 565, 250, 45);
            panelReg.add(btnTutor);
            
            btnTutor.addActionListener(e -> {
    new RegistroTutor();
    dispose();
});

            // BOTÓN INGRESAR
            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeo btnIngresar = new BotonNeo("Ingresar", amarillo, amarilloHover);
            btnIngresar.setBounds(40, 620, 400, 55);
            panelReg.add(btnIngresar);

            btnIngresar.addActionListener(e -> {
    new DatosPersonales();
    dispose();
});
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
