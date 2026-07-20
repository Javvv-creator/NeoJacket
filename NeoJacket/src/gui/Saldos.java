package gui;

import java.awt.*; 
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
                    ? new Color(251, 232, 138, 220) // Hover amarillo
                    : new Color(25, 38, 35, 200));  // Fondo oscuro

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(new Color(251, 232, 138)); // Línea amarilla delgada
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
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
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
            crearSidebar(this);
            crearContenido();
        }

        // ==========================================
        // DISEÑO DEL MENÚ LATERAL (SIDEBAR)
        // ==========================================
        private void crearSidebar(JPanel panel) {

            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                    g2.dispose();
                }
            };

            sidebar.setOpaque(false);
            sidebar.setBounds(20, 20, 300, 950);
            sidebar.setLayout(null);
            
            Color amarillo = new Color(251, 232, 138);
            Color fondoTransparente = new Color(0, 0, 0, 0); 
            Color amarilloBorde = new Color(251, 232, 138);

            // Logo
            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] opciones = {
                "Saldos",
                "Bancos Conectados",
                "Transferencias",
                "Historial"
            };

            int y = 140;

            for (String texto : opciones) {
                
                JButton btn = new JButton(texto) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                        
                        if (getBackground() != amarillo) {
                            g2.setColor(amarilloBorde);
                            g2.setStroke(new BasicStroke(1f));
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                        }
                        
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };

                btn.setBounds(20, y, 250, 46);
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false); 
                btn.setBorderPainted(false);     
                btn.setOpaque(false);
                btn.setForeground(Color.WHITE);
                btn.setBackground(fondoTransparente);

                Font fuenteActual = btn.getFont();
                btn.setFont(new Font(fuenteActual.getName(), fuenteActual.getStyle(), fuenteActual.getSize() + 2));

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        btn.setBackground(amarillo);
                        btn.setForeground(Color.BLACK);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        btn.setBackground(fondoTransparente);
                        btn.setForeground(Color.WHITE);
                    }
                });

                if (texto.equals("Bancos Conectados")) {
                    btn.addActionListener(e -> { 
                        new BancosConectados().setVisible(true);
                        dispose(); 
                    });
                }
                if (texto.equals("Transferencias")) {
                    btn.addActionListener(e -> { 
                        new Transferencias().setVisible(true);
                        dispose(); 
                    });
                }
                sidebar.add(btn);
                y += 68;
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

            // Botón Supervisión — aparece solo si el usuario tiene menores a cargo.
            // Se coloca justo debajo del último botón del menú (Historial).
            funcionalidades.SupervisionDAO daoSup = new funcionalidades.SupervisionDAO();
            int idSesion = funcionalidades.SesionUsuario.getIdUsuario();
            if (idSesion > 0 && daoSup.tieneMenoresACargo(idSesion)) {
                JButton btnSupervision = new JButton("Supervisión");
                btnSupervision.setBounds(20, y, 250, 55);
                btnSupervision.setFocusPainted(false);
                btnSupervision.setBorderPainted(false);
                btnSupervision.setBackground(new Color(251, 232, 138));
                btnSupervision.setForeground(new Color(25, 38, 35));
                btnSupervision.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnSupervision.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnSupervision.addActionListener(e -> {
                    new PanelSupervision(idSesion).setVisible(true);
                    dispose();
                });
                sidebar.add(btnSupervision);
            }

            panel.add(sidebar);
        }

        // ==========================================
        // DISEÑO DEL PANEL DE CONTENIDO CENTRAL
        // ==========================================
        private void crearContenido() {
            Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 34);
            Font subTitulo = new Font("Segoe UI", Font.PLAIN, 14);
            Font tituloTarjeta = new Font("Segoe UI", Font.BOLD, 16);
            Color amarilloPastel = new Color(251, 232, 138);

            // DETALLE ACTUALIZADO: El contenedor ahora tiene exactamente la misma opacidad y color del Sidebar
            JPanel contenedor = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 220)); // Color idéntico al Sidebar, ideal para que no se vea tan oscuro
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            contenedor.setLayout(null);
            contenedor.setOpaque(false); // Necesario para que respete el paintComponent personalizado
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // BARRA SUPERIOR: Fondo oscuro uniforme de baja saturación integrado
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(23, 32, 29)); 
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
            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 867); 
            btnTab3.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab3);

            // Etiquetas de Encabezado Principal
            JLabel lblControlSaldos = new JLabel("CONTROL DE SALDOS");
            lblControlSaldos.setFont(tituloSeccion);
            lblControlSaldos.setForeground(Color.WHITE);
            lblControlSaldos.setBounds(40, 85, 500, 45);
            contenedor.add(lblControlSaldos);

            JLabel lblDescripcion = new JLabel("En este apartado puedes gestionar y mantener al día el dinero de tu cuenta a través de tres funciones principales:");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(subTitulo);
            lblDescripcion.setBounds(40, 135, 1000, 35);
            contenedor.add(lblDescripcion);

            int cardWidth = 330;
            int cardHeight = 280;
            int cardY = 250;

            // Tarjeta 1: Sección de Ingreso de Capital (Lleva a AgregarFondos)
            BotonNeo cardAgregar = new BotonNeo("");
            cardAgregar.setBounds(80, cardY, cardWidth, cardHeight);
            cardAgregar.setLayout(null);
            contenedor.add(cardAgregar);

            JLabel t1Titulo = new JLabel("Agregar Fondos");
            t1Titulo.setForeground(amarilloPastel);
            t1Titulo.setFont(tituloTarjeta);
            t1Titulo.setBounds(20, 20, 240, 25);
            cardAgregar.add(t1Titulo);

            JTextArea t1Texto = crearAreaTexto("Utiliza esta opción cada vez que realices una recarga con tu tarjeta o desees inyectar nuevo capital a tu cuenta.");
            t1Texto.setBounds(10, 50, 320, 150);
            cardAgregar.add(t1Texto);
            
            Image img = new ImageIcon(getClass().getResource("/gui/image/agregarFondos.png")).getImage();
            Image imgEscalada = img.getScaledInstance(260, 200, Image.SCALE_SMOOTH); 
            JLabel iconAgregar = new JLabel(new ImageIcon(imgEscalada));
            iconAgregar.setBounds(60, 130, 180, 150); 
            cardAgregar.add(iconAgregar);

            cardAgregar.addActionListener(e -> {
                new AgregarFondos().setVisible(true);
                dispose(); 
            });

            // Tarjeta 2: Sección de Modificación Manual
            BotonNeo cardActualizar = new BotonNeo("");
            cardActualizar.setBounds(490, cardY, cardWidth, cardHeight);
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
            
            Image imgActualizar = new ImageIcon(getClass().getResource("/gui/image/actualizarSaldos.png")).getImage();
            Image imgEscaladaActualizar = imgActualizar.getScaledInstance(250, 190, Image.SCALE_SMOOTH);
            JLabel iconActualizar = new JLabel(new ImageIcon(imgEscaladaActualizar));
            iconActualizar.setBounds(60, 130, 180, 150);
            cardActualizar.add(iconActualizar);

            cardActualizar.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });

            // Tarjeta 3: Sección de Historial e Informes
            BotonNeo cardConsultar = new BotonNeo("");
            cardConsultar.setBounds(900, cardY, cardWidth, cardHeight);
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
            
            Image imgConsultar = new ImageIcon(getClass().getResource("/gui/image/consultarSaldo.png")).getImage();
            Image imgEscaladaConsultar = imgConsultar.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            JLabel iconConsultar = new JLabel(new ImageIcon(imgEscaladaConsultar));
            iconConsultar.setBounds(60, 130, 180, 150);
            cardConsultar.add(iconConsultar);
            
            cardConsultar.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            
            // Botón Regresar
            Saldos.BotonNeo btnRegresar = new Saldos.BotonNeo("← Regresar");
            int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;
            btnRegresar.setBounds(50, altoPantalla - 140, 180, 50); 
            contenedor.add(btnRegresar);

            btnRegresar.addActionListener(e -> {
                new Dashboard().setVisible(true); 
                dispose();                   
            });
        }

        // ==========================================
        // MÉTODOS AUXILIARES Y CONVERSORES
        // ==========================================
        // ESTILO PESTAÑA: Letras grises nítidas que cambian a blanco puro en Hover
        private JButton crearBotonPestaña(String texto, int xPos) {
            JButton btn = new JButton(texto);
            btn.setBounds(xPos, 0, 434, 55);
            btn.setBackground(new Color(16, 22, 20)); // Fondo oscuro integrado
            btn.setForeground(new Color(150, 150, 150)); // Gris desactivado
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setForeground(Color.WHITE); // Se ilumina a blanco al pasar el mouse
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setForeground(new Color(150, 150, 150)); // Regresa a gris
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