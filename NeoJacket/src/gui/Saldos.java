package gui;

import java.awt.*; // Importación directa de la interfaz AgregarFondos
import javax.swing.*;

public class Saldos extends javax.swing.JFrame {

    // ==========================================
    // ATRIBUTOS MULTIMEDIA
    // ==========================================
    private Image fondo;
    private Image logo;

    // ==========================================
    // CLASE INTERNA: BOTÓN PERSONALIZADO NEO
    // ==========================================
    class BotonNeo extends JButton {
        public BotonNeo(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getModel().isRollover()
                    ? new Color(251, 232, 138, 220)
                    : new Color(94, 116, 73, 190));
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ==========================================
    // CONSTRUCTOR DE LA VENTANA
    // ==========================================
    public Saldos() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Saldos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new FondoPanel());
    }

    // ==========================================
    // PANEL DE CAPA PRINCIPAL
    // ==========================================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
        }

        // ==========================================
        // DISEÑO DEL MENÚ LATERAL (SIDEBAR)
        // ==========================================
        private void crearSidebar() {
            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            JButton btnSaldosActivo = new JButton("Saldos");
            btnSaldosActivo.setBounds(20, 140, 250, 55);
            btnSaldosActivo.setFocusPainted(false);
            btnSaldosActivo.setBorderPainted(false);
            btnSaldosActivo.setBackground(new Color(251, 232, 138));
            btnSaldosActivo.setForeground(Color.BLACK);
            btnSaldosActivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            sidebar.add(btnSaldosActivo);

            String[] botonesMenu = {"Bancos conectados", "Transferencias", "Divisas", "Historial"};
            int y = 210;

            for (String textoBtn : botonesMenu) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                btn.addActionListener(e -> {
                    if (textoBtn.equals("Bancos conectados")) {
                        new BancosConectados().setVisible(true);
                    } else if (textoBtn.equals("Transferencias")) {
                        new Transferencias().setVisible(true);
                    } else if (textoBtn.equals("Divisas")) {
                        new Divisas().setVisible(true);
                    } else if (textoBtn.equals("Historial")) {
                        new Historial().setVisible(true);
                    }
                    dispose();
                });
                
                sidebar.add(btn);
                y += 70;
            }

            JButton btnCerrarSesion = new JButton("Cerrar sesión");
            btnCerrarSesion.setBounds(20, 880, 250, 55);
            btnCerrarSesion.setFocusPainted(false);
            btnCerrarSesion.setBorderPainted(false);
            btnCerrarSesion.setBackground(new Color(191, 76, 58));
            btnCerrarSesion.setForeground(Color.WHITE);
            btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnCerrarSesion.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            add(sidebar);
        }

        // ==========================================
        // DISEÑO DEL PANEL DE CONTENIDO CENTRAL
        // ==========================================
        private void crearContenido() {
            Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 34);
            Font subTitulo = new Font("Segoe UI", Font.PLAIN, 14);
            Font tituloTarjeta = new Font("Segoe UI", Font.BOLD, 16);
            Color amarilloPastel = new Color(251, 232, 138);

            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // Sub-Panel: Pestañas de Navegación Superior
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(94, 116, 73, 200));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            // Pestaña superior 1: Agregar fondos
            JButton btnTab1 = crearBotonPestaña("Agregar Fondos", 0);
            btnTab1.addActionListener(e -> {
                new AgregarFondos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab1);

            // Pestaña superior 2: Actualizar saldos
            JButton btnTab2 = crearBotonPestaña("Actualizar Saldos", 433);
            btnTab2.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab2);

            // Pestaña superior 3: Consultar saldos
            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 866);
            btnTab3.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab3);

            // Etiquetas de Encabezado Principal
            JLabel lblControlSaldos = new JLabel("Control de Saldos");
            lblControlSaldos.setForeground(Color.WHITE);
            lblControlSaldos.setFont(tituloSeccion);
            lblControlSaldos.setBounds(40, 85, 500, 45);
            contenedor.add(lblControlSaldos);

            JLabel lblDescripcion = new JLabel("En este apartado puedes gestionar y mantener al día el dinero de tu cuenta a través de tres funciones principales:");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(subTitulo);
            lblDescripcion.setBounds(40, 135, 900, 25);
            contenedor.add(lblDescripcion);

            int cardWidth = 370;
            int cardHeight = 250;
            int cardY = 200;

            // Tarjeta 1: Sección de Ingreso de Capital (Lleva a AgregarFondos)
            BotonNeo cardAgregar = new BotonNeo("");
            cardAgregar.setBounds(40, cardY, cardWidth, cardHeight);
            cardAgregar.setLayout(null);
            contenedor.add(cardAgregar);

            JLabel t1Titulo = new JLabel("Agregar Fondos");
            t1Titulo.setForeground(amarilloPastel);
            t1Titulo.setFont(tituloTarjeta);
            t1Titulo.setBounds(20, 20, 240, 25);
            cardAgregar.add(t1Titulo);

            JTextArea t1Texto = crearAreaTexto("Utiliza esta opción cada vez que realices una recarga con tu tarjeta o desees inyectar nuevo capital a tu cuenta.");
            t1Texto.setBounds(20, 60, 330, 160);
            cardAgregar.add(t1Texto);
            
            cardAgregar.addActionListener(e -> {
                new AgregarFondos().setVisible(true);
                dispose(); 
            });

            // Tarjeta 2: Sección de Modificación Manual
            BotonNeo cardActualizar = new BotonNeo("");
            cardActualizar.setBounds(450, cardY, cardWidth, cardHeight);
            cardActualizar.setLayout(null);
            contenedor.add(cardActualizar);

            JLabel t2Titulo = new JLabel("Actualizar Saldos");
            t2Titulo.setForeground(amarilloPastel);
            t2Titulo.setFont(tituloTarjeta);
            t2Titulo.setBounds(20, 20, 240, 25);
            cardActualizar.add(t2Titulo);

            JTextArea t2Texto = crearAreaTexto("Para mantener tu información financiera al día, registra aquí de forma manual cada uno de los gastos o compras que vayas realizando.");
            t2Texto.setBounds(20, 60, 330, 160);
            cardActualizar.add(t2Texto);

            cardActualizar.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });

            // Tarjeta 3: Sección de Historial e Informes
            BotonNeo cardConsultar = new BotonNeo("");
            cardConsultar.setBounds(860, cardY, cardWidth, cardHeight);
            cardConsultar.setLayout(null);
            contenedor.add(cardConsultar);

            JLabel t3Titulo = new JLabel("Consultar Saldos");
            t3Titulo.setForeground(amarilloPastel);
            t3Titulo.setFont(tituloTarjeta);
            t3Titulo.setBounds(20, 20, 240, 25);
            cardConsultar.add(t3Titulo);

            JTextArea t3Texto = crearAreaTexto("Accede de forma rápida para revisar las fechas exactas en las que agregaste fondos o actualizaste tus saldos.");
            t3Texto.setBounds(20, 60, 330, 160);
            cardConsultar.add(t3Texto);
            
            cardConsultar.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
        }

        // ==========================================
        // MÉTODOS AUXILIARES Y CONVERSORES
        // ==========================================
        private JButton crearBotonPestaña(String texto, int xPos) {
            JButton btn = new JButton(texto);
            btn.setBounds(xPos, 0, 434, 55);
            btn.setBackground(new Color(25, 38, 35, 100));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(251, 232, 138, 200));
                    btn.setForeground(Color.BLACK);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(25, 38, 35, 100));
                    btn.setForeground(Color.WHITE);
                }
            });
            return btn;
        }

        private JTextArea crearAreaTexto(String contenidoText) {
            JTextArea area = new JTextArea(contenidoText);
            area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            area.setForeground(new Color(230, 235, 230));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            area.setOpaque(false);
            area.setFocusable(false);
            
            area.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    Component p = area.getParent();
                    if (p instanceof JButton) {
                        ((JButton) p).doClick();
                    }
                }
            });
            return area;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    

    // ==========================================
    // COMPONENTES DE RESPALDO (IDE GENERATED)
    // ==========================================
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        pack();
    }

    // ==========================================
    // EJECUCIÓN PRINCIPAL (MAIN)
    // ==========================================
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new Saldos().setVisible(true);
        });
    }
}
