package gui;

import funcionalidades.CrearUsuario;
import funcionalidades.SupervisionDAO;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class RegistroNeo extends JFrame {

    // ============================
    // PALETA DE COLORES
    // ============================
    private static final Color VERDE_OSCURO = new Color(12, 19, 17);
    private static final Color VERDE_CARD = new Color(15, 23, 20, 240);
    private static final Color NEGRO_CAMPO = new Color(10, 14, 13);
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
    private static final int PANEL_H = 800;
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

            g2.setColor(NEGRO_CAMPO);
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
            setEchoChar('*');
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(NEGRO_CAMPO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(hasFocus() ? DORADO_HOVER : DORADO);
            g2.setStroke(new BasicStroke(hasFocus() ? 2f : 1.2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    class PasswordEyeButton extends JButton {
        private final JPasswordField target;
        private boolean mostrando = false;

        public PasswordEyeButton(JPasswordField target) {
            this.target = target;
            setText("👁");
            setFocusable(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(DORADO);
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addActionListener(e -> toggle());
        }

        private void toggle() {
            mostrando = !mostrando;
            target.setEchoChar(mostrando ? '\0' : '•');
            setText(mostrando ? "🙈" : "👁");
            target.repaint();
        }
    }

    // ============================================================
    // COMBOBOX REDONDEADO (BORDE DELGADO DORADO Y SELECCIÓN AMARILLA)
    // ============================================================
    class NeoComboBox<T> extends JComboBox<T> {

        public NeoComboBox(T[] items) {
            super(items);
            setOpaque(false);
            setFocusable(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
            
            // Margen para que el texto interior no toque los bordes redondeados
            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

            // Renderer personalizado con selección amarilla brillante y texto alineado
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    
                    if (index == -1) {
                        lbl.setOpaque(false);
                        lbl.setForeground(Color.WHITE);
                        lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
                    } else {
                        lbl.setOpaque(true);
                        if (isSelected) {
                            lbl.setBackground(DORADO);
                            lbl.setForeground(Color.BLACK); // Contraste óptimo
                        } else {
                            lbl.setBackground(NEGRO_CAMPO);
                            lbl.setForeground(Color.WHITE);
                        }
                        lbl.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                    }
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                    return lbl;
                }
            });

            // UI personalizada para renderizar la flecha
            setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton arrow = new JButton() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            // Botón transparente para integrarse al fondo redondeado sin romperlo
                            g2.setColor(new Color(0, 0, 0, 0));
                            g2.fillRect(0, 0, getWidth(), getHeight());

                            // Línea divisoria muy sutil antes de la flecha
                            g2.setColor(new Color(251, 232, 138, 50));
                            g2.drawLine(0, 8, 0, getHeight() - 9);

                            // Pequeño triángulo indicador (Dorado elegante)
                            g2.setColor(DORADO);
                            int[] xPoints = {getWidth() / 2 - 5, getWidth() / 2, getWidth() / 2 + 5};
                            int[] yPoints = {getHeight() / 2 - 2, getHeight() / 2 + 3, getHeight() / 2 - 2};
                            g2.fillPolygon(xPoints, yPoints, 3);

                            g2.dispose();
                        }
                    };
                    arrow.setContentAreaFilled(false);
                    arrow.setBorderPainted(false);
                    arrow.setFocusPainted(false);
                    arrow.setPreferredSize(new Dimension(34, FIELD_H));
                    arrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return arrow;
                }

                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    // Evitamos pintura nativa rectangular de fondo
                }

                @Override
                protected ComboPopup createPopup() {
                    BasicComboPopup popup = (BasicComboPopup) super.createPopup();
                    // Borde de la lista desplegable coincidente
                    popup.setBorder(BorderFactory.createLineBorder(DORADO, 1));
                    return popup;
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. Pintar fondo negro redondeado
            g2.setColor(NEGRO_CAMPO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            // 2. Pintar borde dorado ultra delgado (1.2 píxeles de grosor, como los textfields)
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
                public int getIconWidth() { return 18; }
                @Override
                public int getIconHeight() { return 18; }
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

            // sombra suave
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRoundRect(6, 8, getWidth() - 6, getHeight() - 8, 28, 28);

            // relleno principal
            g2.setColor(VERDE_CARD);
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 8, 28, 28);

            // borde dorado
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
            if (panelReg == null) return;
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
            PasswordEyeButton btnMostrarPass = new PasswordEyeButton(txtPass);
            btnMostrarPass.setBounds(COL1_X + FIELD_W - 34, y + 28, 24, 24);
            panelReg.add(btnMostrarPass);

            panelReg.add(crearLabel("Tipo de cuenta", COL2_X, y));
            NeoComboBox<String> cbTipo = new NeoComboBox<>(new String[]{"Monetaria", "Ahorro", "Corriente"});
            cbTipo.setBounds(COL2_X, y + 24, FIELD_W, FIELD_H);
            panelReg.add(cbTipo);

            // ---------- FILA 2.5: Confirmar contraseña ----------
            y += 100;
            panelReg.add(crearLabel("Confirmar contraseña", COL1_X, y));
            RoundedPassField txtPassConfirm = new RoundedPassField(20);
            txtPassConfirm.setBounds(COL1_X, y + 24, FIELD_W, FIELD_H);
            panelReg.add(txtPassConfirm);
            PasswordEyeButton btnMostrarPassConfirm = new PasswordEyeButton(txtPassConfirm);
            btnMostrarPassConfirm.setBounds(COL1_X + FIELD_W - 34, y + 28, 24, 24);
            panelReg.add(btnMostrarPassConfirm);

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
            NeoComboBox<String> cbGenero = new NeoComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
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

            // ---------- BOTONES ALINEADOS ----------
            int[] btnYArr = {yF5[0] + 100};
            int btnH = 50;

            BotonNeo btnRegresar = new BotonNeo("Regresar", VERDE_BOTON, VERDE_BOTON_HOVER);
            btnRegresar.setForeground(Color.WHITE);
            btnRegresar.setBounds(COL1_X, btnYArr[0], FIELD_W, btnH);
            panelReg.add(btnRegresar);
            btnRegresar.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });

            BotonNeo btnIngresar = new BotonNeo("Registrarse", DORADO, DORADO_HOVER);
            btnIngresar.setBounds(COL2_X, btnYArr[0], FIELD_W, btnH);
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
                
                // Mover botones
                int nBtnY = nY5 + 100;
                btnRegresar.setBounds(COL1_X, nBtnY, FIELD_W, btnH);
                btnIngresar.setBounds(COL2_X, nBtnY, FIELD_W, btnH);
                
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
                
                // Restaurar botones
                btnRegresar.setBounds(COL1_X, btnYArr[0], FIELD_W, btnH);
                btnIngresar.setBounds(COL2_X, btnYArr[0], FIELD_W, btnH);
                
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

                String passwordConfirm = new String(txtPassConfirm.getPassword());
                if (!password.equals(passwordConfirm)) {
                    JOptionPane.showMessageDialog(null,
                            "Las contraseñas no coinciden. Verifica que sean exactamente iguales.",
                            "Contraseña inválida", JOptionPane.WARNING_MESSAGE);
                    return;
                }

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
                                "✅ Cuenta creada correctamente.\nYa puedes iniciar sesión con tu correo y contraseña.",
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