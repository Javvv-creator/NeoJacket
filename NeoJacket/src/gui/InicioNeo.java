package gui;

import java.awt.*;
import java.net.URI;
import javax.swing.*;

public class InicioNeo extends JFrame {

    private Image fondo;

    // ============================
    // BOTÓN REDONDEADO (ESTILO NEO)
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
            crearPanelPersonas();
            crearPanelRedes(); 
            crearContenido();
        }

        // ============================
        // PANEL SUPERIOR "PERSONAS"
        // ============================
        private void crearPanelPersonas() {
            JPanel panelPersonas = new JPanel();
            panelPersonas.setLayout(null);
            panelPersonas.setBackground(new Color(25, 38, 35, 160)); 
            panelPersonas.setBounds(0, 0, getWidth(), 80);

            JLabel lblPersonas = new JLabel("Personas");
            lblPersonas.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblPersonas.setForeground(Color.WHITE);
            lblPersonas.setBounds(40, 20, 300, 40);
            panelPersonas.add(lblPersonas);

            add(panelPersonas);
        }

        // ============================
        // PANEL DE REDES SOCIALES Y CONTACTO
        // ============================
        private void crearPanelRedes() {
            JPanel contenedorIzquierdo = new JPanel();
            contenedorIzquierdo.setLayout(new BoxLayout(contenedorIzquierdo, BoxLayout.Y_AXIS));
            contenedorIzquierdo.setOpaque(false);
            contenedorIzquierdo.setBounds(40, 180, 250, 390);

            JLabel lblTituloRedes = new JLabel("Visita nuestras redes sociales");
            lblTituloRedes.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTituloRedes.setForeground(Color.WHITE);
            lblTituloRedes.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedorIzquierdo.add(lblTituloRedes);
            
            contenedorIzquierdo.add(Box.createVerticalStrut(15)); 

            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new GridLayout(3, 1, 0, 12)); 
            panelBotones.setOpaque(false);
            panelBotones.setMaximumSize(new Dimension(220, 180));

            Color colorRedes = new Color(25, 38, 35, 140);
            Color colorRedesHover = new Color(94, 116, 73, 200);

            BotonNeo btnInstagram = new BotonNeo("Instagram", colorRedes, colorRedesHover);
            btnInstagram.addActionListener(e -> abrirEnlace("https://www.instagram.com/neo_jacketgt?igsh=MWt6ZXdjbjR2eW5mNA=="));
            panelBotones.add(btnInstagram);

            BotonNeo btnTikTok = new BotonNeo("TikTok", colorRedes, colorRedesHover);
            btnTikTok.addActionListener(e -> abrirEnlace("https://www.tiktok.com"));
            panelBotones.add(btnTikTok);

            BotonNeo btnFacebook = new BotonNeo("Facebook", colorRedes, colorRedesHover);
            btnFacebook.addActionListener(e -> abrirEnlace("https://www.facebook.com"));
            panelBotones.add(btnFacebook);

            contenedorIzquierdo.add(panelBotones);

            contenedorIzquierdo.add(Box.createVerticalStrut(20)); 

            JLabel lblContacto = new JLabel("O contáctanos a nuestro número");
            lblContacto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblContacto.setForeground(new Color(200, 200, 200)); 
            lblContacto.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedorIzquierdo.add(lblContacto);

            JLabel lblTelefono = new JLabel("4906-6594");
            lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 19));
            lblTelefono.setForeground(new Color(251, 232, 138)); 
            lblTelefono.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedorIzquierdo.add(lblTelefono);

            add(contenedorIzquierdo);
        }

        private void abrirEnlace(String url) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(url));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // ============================
        // CONTENIDO CENTRAL (TEXTOS Y ACCESO)
        // ============================
        private void crearContenido() {
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
            // BOTONES PRINCIPALES DE ACCESO
            // ============================
            Color verdeTrans = new Color(25, 38, 35, 180);
            Color verdeHover = new Color(94, 116, 73, 220);

            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            // BOTÓN INICIAR SESIÓN (CORREGIDO)
            BotonNeo btnIniciar = new BotonNeo("Iniciar Sesión", verdeTrans, verdeHover);
            btnIniciar.setBounds(450, 450, 250, 60);
            btnIniciar.addActionListener(e -> {
                new LoginNeo(); // <--- Ya no está comentado, ahora sí inicializa tu login
                dispose();      // Cierra la pantalla actual de inicio
            });
            add(btnIniciar);

            // BOTÓN HAZTE CLIENTE (CORREGIDO)
            BotonNeo btnCliente = new BotonNeo("Hazte Cliente", amarillo, amarilloHover);
            btnCliente.setForeground(Color.BLACK);
            btnCliente.setBounds(750, 450, 250, 60);
            btnCliente.addActionListener(e -> {
                new RegistroNeo(); // <--- Ya no está comentado, ahora sí inicializa tu registro
                dispose();         // Cierra la pantalla actual de inicio
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
