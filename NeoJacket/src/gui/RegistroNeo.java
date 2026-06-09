package gui;

import funcionalidades.CrearUsuario;
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
            setFont(new Font("Segoe UI", Font.BOLD, 16)); // Reducido un toque a 16 para que quepa bien el texto
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

            // PANEL SEMITRANSPARENTE CENTRADO (LA ENCUESTA)
            JPanel panelReg = new JPanel();
            panelReg.setLayout(null);
            panelReg.setBackground(new Color(25, 38, 35, 180));
            panelReg.setBounds(620, 120, 600, 760);
            panelReg.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panelReg);

            // TÍTULO
            JLabel titulo = new JLabel("NEO JACKET - BANCA EN LÍNEA");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 520, 40);
            panelReg.add(titulo);

            // CAMPOS COL1
            JLabel lblUsuario = new JLabel("Nombre");
            lblUsuario.setForeground(Color.WHITE);
            lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblUsuario.setBounds(40, 80, 220, 30);
            panelReg.add(lblUsuario);

            RoundedTextField txtUsuario = new RoundedTextField(20);
            txtUsuario.setBounds(40, 115, 240, 45);
            panelReg.add(txtUsuario);

            // PALETA DE COLORES REUTILIZABLES
            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            JLabel lblPass = new JLabel("Contraseña");
            lblPass.setForeground(Color.WHITE);
            lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblPass.setBounds(40, 175, 220, 30);
            panelReg.add(lblPass);

            RoundedPassField txtPass = new RoundedPassField(20);
            txtPass.setBounds(40, 210, 240, 45);
            panelReg.add(txtPass);

            JLabel lblPerfil = new JLabel("Tipo de perfil");
            lblPerfil.setForeground(Color.WHITE);
            lblPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblPerfil.setBounds(40, 270, 220, 30);
            panelReg.add(lblPerfil);

            JRadioButton rbAdulto = new JRadioButton("Adulto");
            rbAdulto.setBounds(40, 305, 100, 30);
            rbAdulto.setOpaque(false);
            rbAdulto.setForeground(Color.WHITE);

            JRadioButton rbMenor = new JRadioButton("Menor supervisado");
            rbMenor.setBounds(150, 305, 180, 30);
            rbMenor.setOpaque(false);
            rbMenor.setForeground(Color.WHITE);

            ButtonGroup grupo = new ButtonGroup();
            grupo.add(rbAdulto);
            grupo.add(rbMenor);
            rbAdulto.setSelected(true);

            panelReg.add(rbAdulto);
            panelReg.add(rbMenor);

            JLabel lblCorreo = new JLabel("Correo electrónico");
            lblCorreo.setForeground(Color.WHITE);
            lblCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblCorreo.setBounds(40, 345, 220, 30);
            panelReg.add(lblCorreo);

            RoundedTextField txtCorreo = new RoundedTextField(20);
            txtCorreo.setBounds(40, 380, 240, 45);
            panelReg.add(txtCorreo);

            JLabel lblFecha = new JLabel("Fecha de nacimiento");
            lblFecha.setForeground(Color.WHITE);
            lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblFecha.setBounds(40, 440, 220, 30);
            panelReg.add(lblFecha);

            RoundedTextField txtFecha = new RoundedTextField(20);
            txtFecha.setBounds(40, 475, 240, 45);
            panelReg.add(txtFecha);

            // CAMPOS COL2
            JLabel lblApellido = new JLabel("Apellidos");
            lblApellido.setForeground(Color.WHITE);
            lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblApellido.setBounds(320, 80, 220, 30);
            panelReg.add(lblApellido);

            RoundedTextField txtApellido = new RoundedTextField(20);
            txtApellido.setBounds(320, 115, 240, 45);
            panelReg.add(txtApellido);

            JLabel lblTipoCuenta = new JLabel("Tipo de cuenta");
            lblTipoCuenta.setForeground(Color.WHITE);
            lblTipoCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblTipoCuenta.setBounds(320, 175, 220, 30);
            panelReg.add(lblTipoCuenta);

            JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Monetaria"});
            cbTipo.setBounds(320, 210, 240, 45);
            panelReg.add(cbTipo);

            JLabel lblGenero = new JLabel("Género");
            lblGenero.setForeground(Color.WHITE);
            lblGenero.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblGenero.setBounds(320, 270, 220, 30);
            panelReg.add(lblGenero);

            JComboBox<String> cbGenero = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
            cbGenero.setBounds(320, 305, 240, 45);
            panelReg.add(cbGenero);

            JLabel lblTelefono = new JLabel("Teléfono");
            lblTelefono.setForeground(Color.WHITE);
            lblTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblTelefono.setBounds(320, 365, 220, 30);
            panelReg.add(lblTelefono);

            RoundedTextField txtTelefono = new RoundedTextField(20);
            txtTelefono.setBounds(320, 400, 240, 45);
            panelReg.add(txtTelefono);

            JLabel lblIdent = new JLabel("Número de identificación");
            lblIdent.setForeground(Color.WHITE);
            lblIdent.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblIdent.setBounds(320, 460, 240, 30);
            panelReg.add(lblIdent);

            RoundedTextField txtIdent = new RoundedTextField(20);
            txtIdent.setBounds(320, 495, 240, 45);
            panelReg.add(txtIdent);

            JLabel lblFechaHint = new JLabel("(YYYY-MM-DD)");
            lblFechaHint.setForeground(new Color(200, 200, 200));
            lblFechaHint.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblFechaHint.setBounds(40, 525, 220, 20);
            panelReg.add(lblFechaHint);

            // ====================================================================
            // SECCIÓN NUEVA: 3 BOTONES ALINEADOS ADENTRO DE LA ENCUESTA (Y = 600)
            // ====================================================================
            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            // 1. Botón Agregar Tutor (Izquierda)
            BotonNeo btnTutor = new BotonNeo("Cuenta tutor", verde, verdeHover);
            btnTutor.setForeground(Color.WHITE);
            btnTutor.setBounds(40, 600, 160, 50);
            panelReg.add(btnTutor);

            btnTutor.addActionListener(e -> {
                new RegistroTutor().setVisible(true);
                dispose();
            });

            // 2. Botón Regresar (Centro)
            BotonNeo btnRegresar = new BotonNeo("Regresar", amarillo, amarilloHover);
            btnRegresar.setBounds(220, 600, 160, 50);
            panelReg.add(btnRegresar); // Se agrega a panelReg

            btnRegresar.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });

            // 3. Botón Registrarse (Derecha)
            BotonNeo btnIngresar = new BotonNeo("Registrarse", amarillo, amarilloHover);
            btnIngresar.setBounds(400, 600, 160, 50);
            panelReg.add(btnIngresar);
            // ====================================================================

            btnIngresar.addActionListener(e -> {
                String nombre = txtUsuario.getText();
                String apellido = txtApellido.getText();
                String password = new String(txtPass.getPassword());
                String correo = txtCorreo.getText();
                String telefono = txtTelefono.getText();
                String fechaNacimiento = txtFecha.getText();
                String dpiNumero = txtIdent.getText();
                String generoSeleccionado = cbGenero.getSelectedItem() != null ? cbGenero.getSelectedItem().toString() : "Otro";
                String genero = "Otro";
                if ("Masculino".equalsIgnoreCase(generoSeleccionado)) {
                    genero = "M";
                } else if ("Femenino".equalsIgnoreCase(generoSeleccionado)) {
                    genero = "F";
                }
                String perfil = rbAdulto.isSelected() ? "Adulto" : rbMenor.isSelected() ? "Menor supervisado" : "Adulto";
                String tipoCuenta = cbTipo.getSelectedItem() != null ? cbTipo.getSelectedItem().toString() : "Monetaria";

                CrearUsuario crearUsuario = new CrearUsuario();
                boolean creado = crearUsuario.crearDesdeRegistroNeo(nombre, apellido, password, dpiNumero, correo, telefono, fechaNacimiento, perfil, tipoCuenta, genero);
                if (creado) {
                    new InicioNeo().setVisible(true);
                    dispose();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}