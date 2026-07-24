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
            crearPanelRedes(); // El panel "Personas" ha sido removido por completo
            crearContenido();
        }

        // ============================
        // PANEL DE REDES SOCIALES (100% CENTRADO HORIZONTAL)
        // ============================
        private void crearPanelRedes() {
            // GridBagLayout garantiza que los componentes no se arrastren a la izquierda
            JPanel contenedorIzquierdo = new JPanel(new GridBagLayout());
            contenedorIzquierdo.setOpaque(false);
            
            // Ubicación en la pantalla (X=40 para dejar margen limpio, Y=200 para centrarlo verticalmente en su zona)
            contenedorIzquierdo.setBounds(40, 200, 280, 420); 

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.CENTER; // Alineación central absoluta
            gbc.fill = GridBagConstraints.NONE;

            // 1. TÍTULO REDES
            JLabel lblTituloRedes = new JLabel("Visita nuestras redes sociales", SwingConstants.CENTER);
            lblTituloRedes.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTituloRedes.setForeground(Color.WHITE);
            
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 15, 0); // Separación inferior de 15px
            contenedorIzquierdo.add(lblTituloRedes, gbc);

            // 2. PANEL DE BOTONES
            JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 12)); 
            panelBotones.setOpaque(false);
            panelBotones.setPreferredSize(new Dimension(220, 180)); // Tamaño exacto de los botones

            Color colorRedes = new Color(25, 38, 35, 140);
            Color colorRedesHover = new Color(94, 116, 73, 200);

            BotonNeo btnInstagram = new BotonNeo("Instagram", colorRedes, colorRedesHover);
            btnInstagram.addActionListener(e -> abrirEnlace("https://www.instagram.com/neo_jacket.gt/"));
            panelBotones.add(btnInstagram);

            BotonNeo btnTikTok = new BotonNeo("TikTok", colorRedes, colorRedesHover);
            btnTikTok.addActionListener(e -> abrirEnlace("https://www.tiktok.com"));
            panelBotones.add(btnTikTok);

            BotonNeo btnFacebook = new BotonNeo("Facebook", colorRedes, colorRedesHover);
            btnFacebook.addActionListener(e -> abrirEnlace("https://www.facebook.com/profile.php"));
            panelBotones.add(btnFacebook);

            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 25, 0); // Separación inferior de 25px
            contenedorIzquierdo.add(panelBotones, gbc);

            // 3. TEXTO CONTACTO
            JLabel lblContacto = new JLabel("O contáctanos a nuestro número", SwingConstants.CENTER);
            lblContacto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lblContacto.setForeground(new Color(200, 200, 200)); 
            
            gbc.gridy = 2;
            gbc.insets = new Insets(0, 0, 6, 0); // Separación de 6px antes del número
            contenedorIzquierdo.add(lblContacto, gbc);

            // 4. NÚMERO DE TELÉFONO
            JLabel lblTelefono = new JLabel("4906-6594", SwingConstants.CENTER);
            lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 19));
            lblTelefono.setForeground(new Color(251, 232, 138)); 
            
            gbc.gridy = 3;
            gbc.insets = new Insets(0, 0, 0, 0);
            contenedorIzquierdo.add(lblTelefono, gbc);

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
            mensaje1.setBounds(360, 200, 1000, 50);
            add(mensaje1);

            JLabel mensaje2 = new JLabel("Gestiona tus cuentas, realiza pagos y transferencias desde un solo lugar.");
            mensaje2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            mensaje2.setForeground(Color.WHITE);
            mensaje2.setBounds(360, 260, 1000, 40);
            add(mensaje2);

            // ============================
            // BOTONES PRINCIPALES DE ACCESO
            // ============================
            Color verdeTrans = new Color(25, 38, 35, 180);
            Color verdeHover = new Color(94, 116, 73, 220);

            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            // BOTÓN INICIAR SESIÓN
            BotonNeo btnIniciar = new BotonNeo("Iniciar Sesión", verdeTrans, verdeHover);
            btnIniciar.setBounds(450, 450, 250, 60);
            btnIniciar.addActionListener(e -> {
                new LoginNeo(); 
                dispose();      
            });
            add(btnIniciar);

            // BOTÓN HAZTE CLIENTE
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