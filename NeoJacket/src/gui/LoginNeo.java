package gui;

import funcionalidades.SesionUsuario;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginNeo extends JFrame {

    // ============================
    // PALETA DE COLORES (constantes para mantener consistencia)
    // ============================
    private static final Color VERDE_OSCURO   = new Color(25, 38, 35, 200);
    private static final Color AMARILLO       = new Color(251, 232, 138);
    private static final Color AMARILLO_HOVER = new Color(255, 245, 180);
    private static final Color PLACEHOLDER    = new Color(255, 255, 255, 140);

    private Image fondo;
    private Image logo;
    private FondoPanel fondoPanel;

    // ============================
    // TEXTFIELD REDONDEADO CON PLACEHOLDER
    // ============================
    class RoundedTextField extends JTextField {
        private String placeholder;
        private boolean focused = false;

        public RoundedTextField(int size, String placeholder) {
            super(size);
            this.placeholder = placeholder;
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));

            addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { focused = true; repaint(); }
                @Override public void focusLost(FocusEvent e) { focused = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(VERDE_OSCURO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            g2.setColor(focused ? AMARILLO_HOVER : AMARILLO);
            g2.setStroke(new BasicStroke(focused ? 2f : 1f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);

            super.paintComponent(g);

            if (getText().isEmpty() && !focused) {
                g2.setColor(PLACEHOLDER);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, getInsets().left, textY);
            }
            g2.dispose();
        }
    }

    // ============================
    // PASSFIELD REDONDEADO CON PLACEHOLDER
    // ============================
    class RoundedPassField extends JPasswordField {
        private String placeholder;
        private boolean focused = false;

        public RoundedPassField(int size, String placeholder) {
            super(size);
            this.placeholder = placeholder;
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));

            addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { focused = true; repaint(); }
                @Override public void focusLost(FocusEvent e) { focused = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(VERDE_OSCURO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            g2.setColor(focused ? AMARILLO_HOVER : AMARILLO);
            g2.setStroke(new BasicStroke(focused ? 2f : 1f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);

            boolean vacio = getPassword().length == 0;
            super.paintComponent(g);

            if (vacio && !focused) {
                g2.setColor(PLACEHOLDER);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, getInsets().left, textY);
            }
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

            boolean presionado = getModel().isPressed();
            boolean hover = getModel().isRollover();

            g2.setColor(presionado ? AMARILLO : (hover ? AMARILLO_HOVER : AMARILLO));
            g2.fillRoundRect(0, 0, getWidth(), presionado ? getHeight() - 2 : getHeight(), 25, 25);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public LoginNeo() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fondoPanel = new FondoPanel();
        setContentPane(fondoPanel);
        setVisible(true);
    }

    // ============================
    // PANEL PRINCIPAL
    // ============================
    class FondoPanel extends JPanel {

        private JPanel panelLogin;
        private static final int PANEL_ANCHO = 420;
        private static final int PANEL_ALTO  = 450;

        public FondoPanel() {
            setLayout(null);
            crearLogin();

            // Recalcula el centrado del panel de login cada vez que la ventana cambia de tamaño
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    centrarPanelLogin();
                }
            });
        }

        private void centrarPanelLogin() {
            if (panelLogin == null) return;
            int x = (getWidth() - PANEL_ANCHO) / 2;
            int y = (getHeight() - PANEL_ALTO) / 2;
            panelLogin.setBounds(x, y, PANEL_ANCHO, PANEL_ALTO);
        }

        private void crearLogin() {

            // LOGO ARRIBA
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL SEMITRANSPARENTE, AHORA CENTRADO EN LA PANTALLA (no fijo a la derecha)
            panelLogin = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    // Fondo con esquinas redondeadas + sombra sutil para dar profundidad
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0, 0, 0, 60));
                    g2.fillRoundRect(4, 6, getWidth() - 4, getHeight() - 4, 20, 20);
                    g2.setColor(VERDE_OSCURO);
                    g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 20, 20);
                    g2.setColor(AMARILLO);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(0, 0, getWidth() - 7, getHeight() - 7, 20, 20);
                    g2.dispose();
                }
            };
            panelLogin.setLayout(null);
            panelLogin.setOpaque(false);
            add(panelLogin);

            // TÍTULOS
            JLabel titulo = new JLabel("NEO JACKET BANCA EN LÍNEA");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 25, 380, 40);
            panelLogin.add(titulo);

            // SUBTÍTULO
            JLabel subtitulo = new JLabel("EL FUTURO BANCARIO EN TUS MANOS");
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitulo.setForeground(new Color(255, 255, 255, 200));
            subtitulo.setBounds(40, 60, 380, 30);
            panelLogin.add(subtitulo);

            // CAMPO USUARIO (con placeholder, sin label separado)
            RoundedTextField txtUsuario = new RoundedTextField(20, "Nombre de usuario");
            txtUsuario.setBounds(40, 130, 330, 50);
            panelLogin.add(txtUsuario);

            // CAMPO CONTRASEÑA
            RoundedPassField txtPass = new RoundedPassField(20, "Contraseña");
            txtPass.setBounds(40, 200, 330, 50);
            panelLogin.add(txtPass);

            // BOTÓN INICIAR SESIÓN
            BotonNeo btnLogin = new BotonNeo("→ Iniciar sesión");
            btnLogin.setBounds(40, 280, 330, 55);
            panelLogin.add(btnLogin);

            // Mensaje de estado (errores) integrado en el panel, en vez de solo diálogos modales
            JLabel lblEstado = new JLabel(" ");
            lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblEstado.setForeground(new Color(255, 120, 120));
            lblEstado.setBounds(40, 345, 340, 25);
            panelLogin.add(lblEstado);

            // =========================================================
            // BOTÓN REGRESAR EN LA PARTE INFERIOR IZQUIERDA
            // =========================================================
            BotonNeo btnRegresar = new BotonNeo("← Regresar");
            int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;
            btnRegresar.setBounds(50, altoPantalla - 140, 180, 50);
            add(btnRegresar);

            btnRegresar.addActionListener(e -> {
                new InicioNeo();
                dispose();
            });

            // ACCIÓN DE LOGIN
            Runnable accionLogin = () -> {
                String nombre = txtUsuario.getText().trim();
                String password = new String(txtPass.getPassword());

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                lblEstado.setText(" ");

                try {
                    funcionalidades.IniciarSesion auth = new funcionalidades.IniciarSesion();
                    String rol = auth.iniciarSesion(nombre, password);

                     if (rol != null) {
                        int idUsuario = auth.obtenerIdUsuario(nombre, password); // este método debe devolver el id real
                        if (idUsuario > 0) {
                            SesionUsuario.setIdUsuario(idUsuario); // 🔹 aquí guardas el id en sesión
                            System.out.println("Usuario en sesión: " + SesionUsuario.getIdUsuario());
                        }

                         if ("ADMIN".equals(rol)) {
                            new PanelControlAdmin().setVisible(true);
                            dispose();
                        } else {
                            funcionalidades.SupervisionDAO dao = new funcionalidades.SupervisionDAO();
                            boolean esMenor = idUsuario > 0 && dao.esMenorSupervisado(idUsuario);

                            if (esMenor) {
                                dao.registrarSesionMenor(idUsuario, "inicio_sesion");
                                new DashboardMenor(idUsuario).setVisible(true);
                            } else {
                                new Dashboard(idUsuario).setVisible(true);
                            }
                            dispose();
                        }
                    } else {
                       JOptionPane.showMessageDialog(null,
                                "Credenciales incorrectas o cuenta inactiva.",
                                "Error de autenticación",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (IllegalArgumentException ex) {
                    lblEstado.setText(ex.getMessage());
                } catch (Exception ex) {
                    lblEstado.setText("Error al iniciar sesión: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            };

            btnLogin.addActionListener(e -> accionLogin.run());

            // Permite iniciar sesión presionando Enter desde cualquiera de los dos campos
            KeyAdapter enterListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        accionLogin.run();
                    }
                }
            };
            txtUsuario.addKeyListener(enterListener);
            txtPass.addKeyListener(enterListener);

            // Centra el panel al iniciar (antes de que se dispare el primer resize)
            SwingUtilities.invokeLater(this::centrarPanelLogin);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}