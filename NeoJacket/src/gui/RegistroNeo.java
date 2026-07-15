package gui;

import funcionalidades.CrearUsuario;
import funcionalidades.SupervisionDAO;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class RegistroNeo extends JFrame {

    // ============================
    // PALETA DE COLORES
    // ============================
    private static final Color VERDE_OSCURO = new Color(25, 38, 35);
    private static final Color VERDE_CARD = new Color(22, 34, 31, 215);
    private static final Color VERDE_CAMPO = new Color(20, 32, 29, 200);
    private static final Color VERDE_BOTON = new Color(94, 116, 73);
    private static final Color VERDE_BOTON_HOVER = new Color(120, 150, 90);
    private static final Color DORADO = new Color(251, 232, 138);
    private static final Color DORADO_HOVER = new Color(255, 245, 180);
    private static final Color BLANCO_SUAVE = new Color(235, 235, 230);
    private static final Color GRIS_HINT = new Color(190, 195, 190);

    // ============================
    // DIMENSIONES DE LA TARJETA
    // ============================
    private static final int PANEL_W = 640;
    private static final int PANEL_H = 700;
    private static final int MARGIN_X = 40;
    private static final int COL_GAP = 30;
    private static final int FIELD_W = 260;
    private static final int FIELD_H = 46;
    private static final int COL1_X = MARGIN_X;
    private static final int COL2_X = MARGIN_X + FIELD_W + COL_GAP;

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
            setCaretColor(DORADO);
            setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 12));
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(VERDE_CAMPO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(hasFocus() ? DORADO_HOVER : DORADO);
            g2.setStroke(new BasicStroke(hasFocus() ? 2f : 1.2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);

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
            setCaretColor(DORADO);
            setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 12));
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setEchoChar('•');
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(VERDE_CAMPO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(hasFocus() ? DORADO_HOVER : DORADO);
            g2.setStroke(new BasicStroke(hasFocus() ? 2f : 1.2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // COMBOBOX REDONDEADO CORREGIDO
    // ============================
    class RoundedComboBox<T> extends JComboBox<T> {

        public RoundedComboBox(T[] items) {
            super(items);
            setOpaque(false);
            setFocusable(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 10));

            ((JLabel) getRenderer()).setOpaque(false);

            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    
                    if (index == -1) {
                        lbl.setOpaque(false);
                        lbl.setForeground(Color.WHITE);
                    } else {
                        lbl.setOpaque(true);
                        lbl.setBackground(isSelected ? new Color(55, 75, 65) : VERDE_OSCURO);
                        lbl.setForeground(Color.WHITE);
                    }
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                    lbl.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    return lbl;
                }
            });

            setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton arrow = new JButton("▾");
                    arrow.setContentAreaFilled(false);
                    arrow.setBorderPainted(false);
                    arrow.setFocusPainted(false);
                    arrow.setForeground(DORADO);
                    arrow.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    arrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return arrow;
                }

                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(VERDE_CAMPO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(DORADO);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ============================
    // RADIO BUTTON CON ICONO PROPIO
    // ============================
    class NeoRadioButton extends JRadioButton {

        public NeoRadioButton(String texto) {
            super(texto);
            setOpaque(false);
            setForeground(BLANCO_SUAVE);
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setIconTextGap(10);
            getModel().addChangeListener(e -> repaint());
            setIcon(new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillOval(x, y, 18, 18);
                    g2.setColor(DORADO);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(x, y, 18, 18);
                    if (isSelected()) {
                        g2.setColor(DORADO);
                        g2.fillOval(x + 4, y + 4, 10, 10);
                    }
                    g2.dispose();
                }

                @Override
                public int getIconWidth() {
                    return 18;
                }

                @Override
                public int getIconHeight() {
                    return 18;
                }
            });
        }
    }

    // ============================
    // BOTÓN NEO
    // ============================
    class BotonNeo extends JButton {

        private final Color normal;
        private final Color hover;

        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean over = getModel().isRollover();
            g2.setColor(over ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(new Color(0, 0, 0, 40));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // ============================
    // TARJETA CON SOMBRA Y BORDES REDONDEADOS
    // ============================
    class RoundedCardPanel extends JPanel {

        public RoundedCardPanel() {
            setLayout(null);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRoundRect(6, 8, getWidth() - 6, getHeight() - 8, 28, 28);

            g2.setColor(VERDE_CARD);
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 8, 28, 28);

            g2.setColor(DORADO);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 8, getHeight() - 10, 28, 28);

            g2.dispose();
            super.paintComponent(g);
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

        private RoundedCardPanel panelReg;

        public FondoPanel() {
            setLayout(null);
            crearRegistro();

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    centrarTarjeta();
                }
            });
        }

        private void centrarTarjeta() {
            if (panelReg == null) {
                return;
            }
            int px = Math.max(MARGIN_X, (getWidth() - PANEL_W) / 2);
            int py = Math.max(30, (getHeight() - PANEL_H) / 2);
            panelReg.setBounds(px, py, PANEL_W, PANEL_H);
        }

        private void crearRegistro() {

            // LOGO ARRIBA
            Image logoEscalado = logo.getScaledInstance(240, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(48, 36, 240, 110);
            add(lblLogo);

            // TARJETA DE REGISTRO
            panelReg = new RoundedCardPanel();
            panelReg.setBounds((1100 - PANEL_W) / 2, 120, PANEL_W, PANEL_H);
            add(panelReg);

            // TÍTULO
            JLabel titulo = new JLabel("NEO JACKET · BANCA EN LÍNEA");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(MARGIN_X, 22, PANEL_W - 2 * MARGIN_X, 34);
            panelReg.add(titulo);

            JLabel subtitulo = new JLabel("Completa tus datos para crear tu cuenta");
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitulo.setForeground(GRIS_HINT);
            subtitulo.setBounds(MARGIN_X, 54, PANEL_W - 2 * MARGIN_X, 20);
            panelReg.add(subtitulo);

            // ---------- FILA 1: Nombre / Apellidos ----------
            int y = 92;
            panelReg.add(crearLabel("Nombre", COL1_X, y));
            RoundedTextField txtUsuario = new RoundedTextField(20);
            txtUsuario.setBounds(COL1_X, y + 24, FIELD_W, FIELD_H);
            panelReg.add(txtUsuario);

            panelReg.add(crearLabel("Apellidos", COL2_X, y));
            RoundedTextField txtApellido = new RoundedTextField(20);
            txtApellido.setBounds(COL2_X, y + 24, FIELD_W, FIELD_H);
            panelReg.add(txtApellido);

            // ---------- FILA 2: Contraseña / Tipo de cuenta ----------
            y += 100;
            panelReg.add(crearLabel("Contraseña", COL1_X, y));
            RoundedPassField txtPass = new RoundedPassField(20);
            txtPass.setBounds(COL1_X, y + 24, FIELD_W, FIELD_H);
            panelReg.add(txtPass);

            panelReg.add(crearLabel("Tipo de cuenta", COL2_X, y));
            RoundedComboBox<String> cbTipo = new RoundedComboBox<>(new String[]{"Monetaria", "Ahorro", "Corriente"});
            cbTipo.setBounds(COL2_X, y + 24, FIELD_W, FIELD_H);
            panelReg.add(cbTipo);

            // ---------- FILA 3: Tipo de perfil / Género ----------
            y += 100;
            panelReg.add(crearLabel("Tipo de perfil", COL1_X, y));
            NeoRadioButton rbAdulto = new NeoRadioButton("Adulto");
            rbAdulto.setBounds(COL1_X, y + 26, 110, 26);

            NeoRadioButton rbMenor = new NeoRadioButton("Menor supervisado");
            rbMenor.setBounds(COL1_X + 110, y + 26, 170, 26);

            ButtonGroup grupo = new ButtonGroup();
            grupo.add(rbAdulto);
            grupo.add(rbMenor);
            rbAdulto.setSelected(true);

            panelReg.add(rbAdulto);
            panelReg.add(rbMenor);

            panelReg.add(crearLabel("Género", COL2_X, y));
            RoundedComboBox<String> cbGenero = new RoundedComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
            cbGenero.setBounds(COL2_X, y + 24, FIELD_W, FIELD_H);
            panelReg.add(cbGenero);

            // ---------- CAMPO CORREO TUTOR ----------
            int tutorRowH = 80;

            JLabel lblCorreoTutor = crearLabel("Correo del tutor", COL1_X, y + 70);
            lblCorreoTutor.setForeground(DORADO);
            lblCorreoTutor.setVisible(false);
            panelReg.add(lblCorreoTutor);

            RoundedTextField txtCorreoTutor = new RoundedTextField(20);
            txtCorreoTutor.setBounds(COL1_X, y + 92, PANEL_W - 2 * MARGIN_X, FIELD_H);
            txtCorreoTutor.setVisible(false);
            panelReg.add(txtCorreoTutor);

            // ---------- FILA 4: Correo / Teléfono ----------
            int[] yF4 = {y + 100}; 

            JLabel lblCorreoLabel = crearLabel("Correo electrónico", COL1_X, yF4[0]);
            panelReg.add(lblCorreoLabel);
            RoundedTextField txtCorreo = new RoundedTextField(20);
            txtCorreo.setBounds(COL1_X, yF4[0] + 24, FIELD_W, FIELD_H);
            panelReg.add(txtCorreo);

            JLabel lblTelefonoLabel = crearLabel("Teléfono", COL2_X, yF4[0]);
            panelReg.add(lblTelefonoLabel);
            RoundedTextField txtTelefono = new RoundedTextField(20);
            txtTelefono.setBounds(COL2_X, yF4[0] + 24, FIELD_W, FIELD_H);
            panelReg.add(txtTelefono);

            // ---------- FILA 5: Fecha / Identificación ----------
            int[] yF5 = {yF4[0] + 100};

            JLabel lblFechaLabel = crearLabel("Fecha de nacimiento", COL1_X, yF5[0]);
            panelReg.add(lblFechaLabel);
            RoundedTextField txtFecha = new RoundedTextField(20);
            txtFecha.setBounds(COL1_X, yF5[0] + 24, FIELD_W, FIELD_H);
            panelReg.add(txtFecha);

            JLabel lblFechaHint = new JLabel("Formato: AAAA-MM-DD");
            lblFechaHint.setForeground(GRIS_HINT);
            lblFechaHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblFechaHint.setBounds(COL1_X, yF5[0] + 24 + FIELD_H + 4, FIELD_W, 16);
            panelReg.add(lblFechaHint);

            JLabel lblIdentLabel = crearLabel("Número de identificación", COL2_X, yF5[0]);
            panelReg.add(lblIdentLabel);
            RoundedTextField txtIdent = new RoundedTextField(20);
            txtIdent.setBounds(COL2_X, yF5[0] + 24, FIELD_W, FIELD_H);
            panelReg.add(txtIdent);

            lblIdentLabel.setVisible(!rbMenor.isSelected());
            txtIdent.setVisible(!rbMenor.isSelected());

            // ---------- BOTONES CENTRADOS ----------
            int[] btnYArr = {yF5[0] + 100};
            int btnW = 160;
            int btnH = 50;
            int gap = 20;

            // Fórmula para alinear los dos botones exactamente al centro horizontal
            int totalBtnW = (btnW * 2) + gap;
            int startX = (PANEL_W - totalBtnW) / 2;

            BotonNeo btnRegresar = new BotonNeo("Regresar", DORADO, DORADO_HOVER);
            btnRegresar.setBounds(startX, btnYArr[0], btnW, btnH);
            panelReg.add(btnRegresar);
            btnRegresar.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });

            BotonNeo btnIngresar = new BotonNeo("Registrarse", DORADO, DORADO_HOVER);
            btnIngresar.setBounds(startX + btnW + gap, btnYArr[0], btnW, btnH);
            panelReg.add(btnIngresar);

            // ---------- Mostrar/ocultar campo tutor y ajustar filas ----------
            Runnable mostrarTutor = () -> {
                lblCorreoTutor.setVisible(true);
                txtCorreoTutor.setVisible(true);
                lblIdentLabel.setVisible(false);
                txtIdent.setVisible(false);
                // Mover fila 4
                lblCorreoLabel.setBounds(COL1_X, yF4[0] + tutorRowH, FIELD_W, 22);
                txtCorreo.setBounds(COL1_X, yF4[0] + tutorRowH + 24, FIELD_W, FIELD_H);
                lblTelefonoLabel.setBounds(COL2_X, yF4[0] + tutorRowH, FIELD_W, 22);
                txtTelefono.setBounds(COL2_X, yF4[0] + tutorRowH + 24, FIELD_W, FIELD_H);
                // Mover fila 5
                int nY5 = yF4[0] + tutorRowH + 100;
                lblFechaLabel.setBounds(COL1_X, nY5, FIELD_W, 22);
                txtFecha.setBounds(COL1_X, nY5 + 24, FIELD_W, FIELD_H);
                lblFechaHint.setBounds(COL1_X, nY5 + 24 + FIELD_H + 4, FIELD_W, 16);
                lblIdentLabel.setBounds(COL2_X, nY5, FIELD_W, 22);
                txtIdent.setBounds(COL2_X, nY5 + 24, FIELD_W, FIELD_H);
                
                // Mover y mantener botones centrados
                int nBtnY = nY5 + 100;
                btnRegresar.setBounds(startX, nBtnY, btnW, btnH);
                btnIngresar.setBounds(startX + btnW + gap, nBtnY, btnW, btnH);
                
                // Expandir tarjeta
                panelReg.setBounds(panelReg.getX(), panelReg.getY(), PANEL_W, PANEL_H + tutorRowH);
                panelReg.revalidate();
                panelReg.repaint();
            };

            Runnable ocultarTutor = () -> {
                lblCorreoTutor.setVisible(false);
                txtCorreoTutor.setVisible(false);
                lblIdentLabel.setVisible(true);
                txtIdent.setVisible(true);
                // Restaurar fila 4
                lblCorreoLabel.setBounds(COL1_X, yF4[0], FIELD_W, 22);
                txtCorreo.setBounds(COL1_X, yF4[0] + 24, FIELD_W, FIELD_H);
                lblTelefonoLabel.setBounds(COL2_X, yF4[0], FIELD_W, 22);
                txtTelefono.setBounds(COL2_X, yF4[0] + 24, FIELD_W, FIELD_H);
                // Restaurar fila 5
                lblFechaLabel.setBounds(COL1_X, yF5[0], FIELD_W, 22);
                txtFecha.setBounds(COL1_X, yF5[0] + 24, FIELD_W, FIELD_H);
                lblFechaHint.setBounds(COL1_X, yF5[0] + 24 + FIELD_H + 4, FIELD_W, 16);
                lblIdentLabel.setBounds(COL2_X, yF5[0], FIELD_W, 22);
                txtIdent.setBounds(COL2_X, yF5[0] + 24, FIELD_W, FIELD_H);
                
                // Restaurar botones centrados
                btnRegresar.setBounds(startX, btnYArr[0], btnW, btnH);
                btnIngresar.setBounds(startX + btnW + gap, btnYArr[0], btnW, btnH);
                
                // Restaurar tamaño tarjeta
                panelReg.setBounds(panelReg.getX(), panelReg.getY(), PANEL_W, PANEL_H);
                panelReg.revalidate();
                panelReg.repaint();
            };

            rbMenor.addActionListener(e -> mostrarTutor.run());
            rbAdulto.addActionListener(e -> ocultarTutor.run());

            // ---------- ACTION LISTENER DE REGISTRO ----------
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

                String perfil = rbMenor.isSelected() ? "Menor supervisado" : "Adulto";
                String tipoCuenta = cbTipo.getSelectedItem() != null ? cbTipo.getSelectedItem().toString() : "Monetaria";

                if (rbMenor.isSelected()) {
                    String correoTutor = txtCorreoTutor.getText().trim();
                    if (correoTutor.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "Debes ingresar el correo del tutor para registrarte como menor supervisado.",
                                "Campo requerido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    SupervisionDAO dao = new SupervisionDAO();
                    Integer idAdulto = dao.buscarIdUsuarioPorCorreo(correoTutor);
                    if (idAdulto == null) {
                        JOptionPane.showMessageDialog(null,
                                "No se encontró ningún tutor registrado con ese correo.\nVerifica que el tutor ya tenga una cuenta creada.",
                                "Tutor no encontrado", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    CrearUsuario crearUsuario = new CrearUsuario();
                    boolean creado = crearUsuario.crearDesdeRegistroNeo(
                            nombre, apellido, password, dpiNumero, correo, telefono, fechaNacimiento, perfil, tipoCuenta, genero
                    );

                    if (creado) {
                        Integer idMenor = dao.buscarIdUsuarioPorCorreo(correo);
                        if (idMenor != null) {
                            dao.crearSupervision(idAdulto, idMenor);
                        }

                        JOptionPane.showMessageDialog(null,
                                " Cuenta creada correctamente.\nYa puedes iniciar sesión con tu correo y contraseña.",
                                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

                        new InicioNeo().setVisible(true);
                        dispose();
                    }

                } else {
                    CrearUsuario crearUsuario = new CrearUsuario();
                    boolean creado = crearUsuario.crearDesdeRegistroNeo(
                            nombre, apellido, password, dpiNumero, correo, telefono, fechaNacimiento, perfil, tipoCuenta, genero
                    );

                    if (creado) {
                        new DatosTarjeta(correo, dpiNumero, tipoCuenta).setVisible(true);
                        dispose();
                    }
                }
            });
        }

        private JLabel crearLabel(String texto, int x, int y) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(BLANCO_SUAVE);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lbl.setBounds(x, y, FIELD_W, 22);
            return lbl;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

            g2.setColor(new Color(10, 15, 14, 90));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}