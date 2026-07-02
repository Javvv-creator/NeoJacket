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
    
    // ============================
    // BOTONES PRINCIPALES DE ACCESO
    // ============================
    Color verdeTrans = new Color(25, 38, 35, 180);
    Color verdeHover = new Color(94, 116, 73, 220);

    Color amarillo = new Color(251, 232, 138);
    Color amarilloHover = new Color(255, 245, 180);

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

        // Ajustamos la altura a 46 para un look más estilizado
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
        if (texto.equals("Historial")) {
            btn.addActionListener(e -> { 
                new Historial().setVisible(true);
                dispose(); 
            });
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

        sidebar.add(btn);
        
        // Aumentamos el salto a 68 píxeles para darles más separación física
        y += 68;
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

            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // Sub-Panel: Pestañas de Navegación Superior
            JPanel barraSuperior = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(94, 116, 73, 200));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
    }
};
barraSuperior.setBounds(0, 0, 1300, 55);
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
            JLabel lblControlSaldos = new JLabel("CONTROL DE SALDOS");
            lblControlSaldos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblControlSaldos.setForeground(Color.WHITE);
            lblControlSaldos.setFont(tituloSeccion);
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
Image imgEscalada = img.getScaledInstance(220, 190, Image.SCALE_SMOOTH); // más grande
JLabel iconAgregar = new JLabel(new ImageIcon(imgEscalada));
iconAgregar.setBounds(60, 130, 180, 150); // centrado dentro del cuadro
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
Image imgEscaladaActualizar = imgActualizar.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
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
Image imgEscaladaConsultar = imgConsultar.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
JLabel iconConsultar = new JLabel(new ImageIcon(imgEscaladaConsultar));
iconConsultar.setBounds(60, 130, 180, 150);
cardConsultar.add(iconConsultar);
            
            cardConsultar.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            
            // =========================================================
// BOTÓN REGRESAR EN LA PARTE INFERIOR DE LA PANTALLA
// =========================================================
Saldos.BotonNeo btnRegresar = new Saldos.BotonNeo("← Regresar");
    int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;
    btnRegresar.setBounds(50, altoPantalla - 140, 180, 50); // abajo a la izquierda
    contenedor.add(btnRegresar);

    btnRegresar.addActionListener(e -> {
        new Dashboard().setVisible(true); // abre la ventana anterior
        dispose();                   // cierra la actual
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
