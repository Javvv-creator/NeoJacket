package gui;

import funcionalidades.SesionUsuario;
import java.awt.*;
import javax.swing.*;

public class LoginNeo extends JFrame {

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
    public LoginNeo() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Login");
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
            crearLogin();
        }

        private void crearLogin() {

            // LOGO ARRIBA
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL SEMITRANSPARENTE CENTRADO
            JPanel panelLogin = new JPanel();
            panelLogin.setLayout(null);
            panelLogin.setBackground(new Color(25, 38, 35, 180));
            panelLogin.setBounds(750, 150, 420, 450);
            panelLogin.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panelLogin);

            // TÍTULOS
            JLabel titulo = new JLabel("NEO JACKET BANCA EN LÍNEA");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panelLogin.add(titulo);

            // SUBTÍTULO
            JLabel subtitulo = new JLabel("EL FUTURO BANCARIO EN TUS MANOS");
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(40, 55, 400, 30);
            panelLogin.add(subtitulo);

            // CAMPOS
            JLabel lblUsuario = new JLabel("Nombre");
            lblUsuario.setForeground(Color.WHITE);
            lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblUsuario.setBounds(40, 120, 200, 30);
            panelLogin.add(lblUsuario);

            RoundedTextField txtUsuario = new RoundedTextField(20);
            txtUsuario.setBounds(40, 155, 330, 50);
            panelLogin.add(txtUsuario);

            JLabel lblPass = new JLabel("Contraseña");
            lblPass.setForeground(Color.WHITE);
            lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblPass.setBounds(40, 225, 200, 30);
            panelLogin.add(lblPass);

            RoundedPassField txtPass = new RoundedPassField(20);
            txtPass.setBounds(40, 260, 330, 50);
            panelLogin.add(txtPass);

            // BOTÓN INICIAR SESIÓN
            BotonNeo btnLogin = new BotonNeo("→ Iniciar sesión");
            btnLogin.setBounds(40, 340, 330, 55);
            panelLogin.add(btnLogin);
            
            

            // =========================================================
            // BOTÓN REGRESAR EN LA PARTE INFERIOR DE LA PANTALLA
            // =========================================================
            BotonNeo btnRegresar = new BotonNeo("← Regresar");
            // Obtenemos el alto de la pantalla actual para colocarlo abajo de forma segura
            int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;
            // Lo posicionamos a 50px de la izquierda y restamos unos 140px para que libre la barra de tareas
            btnRegresar.setBounds(50, altoPantalla - 140, 180, 50);
            add(btnRegresar);

            // Acción asignada de manera funcional para regresar al InicioNeo
            btnRegresar.addActionListener(e -> {
                new InicioNeo(); // Crea y muestra la ventana de inicio anterior
                dispose();       // Cierra y destruye la ventana de login actual
            });
            // =========================================================

            // ACCIÓN / LOGIN: Usar la lógica de autenticación del backend
            btnLogin.addActionListener(e -> {
                String nombre = txtUsuario.getText().trim();
                String password = new String(txtPass.getPassword());

                try {
                   funcionalidades.IniciarSesion auth = new funcionalidades.IniciarSesion();
String rol = auth.iniciarSesion(nombre, password);

if (rol != null) {
    int idUsuario = auth.obtenerIdUsuario(nombre, password);
    if (idUsuario > 0) {
        SesionUsuario.setIdUsuario(idUsuario);
    }

    // DEBUG TEMPORAL
    System.out.println("=== DEBUG LOGIN ===");
    System.out.println("ID: " + idUsuario);
    System.out.println("ES MENOR: " + new funcionalidades.SupervisionDAO().esMenorSupervisado(idUsuario));
    System.out.println("==================");

    if ("ADMIN".equals(rol)) {
        new PanelControlAdmin().setVisible(true);
        dispose();
    } else {
        // Detectar si es menor por el campo perfil en usuarios
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
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al iniciar sesión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
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