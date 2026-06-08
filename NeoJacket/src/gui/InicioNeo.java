package gui;

import java.awt.*;
import javax.swing.*;

public class InicioNeo extends JFrame {

    private Image fondo;

    // ============================
    // BOTÓN REDONDEADO
    // ============================
    class BotonNeo extends JButton {
        private Color colorNormal;
        private Color colorHover;

        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.colorNormal = normal;
            this.colorHover = hover;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getModel().isRollover() ? colorHover : colorNormal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public InicioNeo() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoinicio.png")).getImage();

        setTitle("Neo Jacket - Inicio");
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
            crearPanelPersonas();
            crearContenido();
        }

        // ============================
        // PANEL SUPERIOR "PERSONAS"
        // ============================
        private void crearPanelPersonas() {

            JPanel panelPersonas = new JPanel();
            panelPersonas.setLayout(null);
            panelPersonas.setBackground(new Color(25, 38, 35, 160)); // semitransparente
            panelPersonas.setBounds(0, 0, getWidth(), 80);

            JLabel lblPersonas = new JLabel("Personas");
            lblPersonas.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblPersonas.setForeground(Color.WHITE);
            lblPersonas.setBounds(40, 20, 300, 40);
            panelPersonas.add(lblPersonas);

            add(panelPersonas);
        }

        // ============================
        // CONTENIDO CENTRAL
        // ============================
        private void crearContenido() {

            // MENSAJE PRINCIPAL
            JLabel mensaje1 = new JLabel("Tu banca, más simple, más segura, más tú.");
            mensaje1.setFont(new Font("Segoe UI", Font.BOLD, 40));
            mensaje1.setForeground(Color.WHITE);
            mensaje1.setBounds(350, 200, 1000, 50);
            add(mensaje1);

            JLabel mensaje2 = new JLabel("Gestiona tus cuentas, realiza pagos y transferencias desde un solo lugar.");
            mensaje2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            mensaje2.setForeground(Color.WHITE);
            mensaje2.setBounds(350, 260, 1000, 40);
            add(mensaje2);

            // ============================
            // BOTONES ABAJO
            // ============================
            Color verdeTrans = new Color(25, 38, 35, 180);
            Color verdeHover = new Color(94, 116, 73, 220);

            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeo btnIniciar = new BotonNeo("Iniciar Sesión", verdeTrans, verdeHover);
            btnIniciar.setBounds(450, 450, 250, 60);
            btnIniciar.addActionListener(e -> {
                new LoginNeo();
                dispose();
            });
            add(btnIniciar);

            BotonNeo btnCliente = new BotonNeo("Hazte Cliente", amarillo, amarilloHover);
            btnCliente.setForeground(Color.BLACK);
            btnCliente.setBounds(750, 450, 250, 60);
            btnCliente.addActionListener(e -> {
                new RegistroNeo();
                dispose();
            });
            add(btnCliente);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
