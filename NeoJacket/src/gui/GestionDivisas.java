package gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import funcionalidades.API;

public class GestionDivisas extends JFrame {

    private Image fondo;
    private Image logo;
    private DefaultTableModel modeloTabla;

    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeBotonNormal = new Color(94, 116, 73);

    // Botón personalizado con efecto Hover
    class BotonNeo extends JButton {
        public BotonNeo(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g2.setColor(amarilloPastel);
                setForeground(Color.BLACK);
            } else {
                g2.setColor(new Color(94, 116, 73, 190));
                setForeground(Color.WHITE);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN SIDEBAR NEO
    // ============================
    class BotonSidebarNeo extends JButton {

        private boolean esSeleccionado;

        public BotonSidebarNeo(String texto, boolean esSeleccionado) {
            super(texto);
            this.esSeleccionado = esSeleccionado;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 15));

            if (esSeleccionado) {
                setBackground(amarilloPastel);
                setForeground(Color.BLACK);
            } else {
                setBackground(verdeBotonNormal);
                setForeground(Color.WHITE);
            }

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!esSeleccionado) {
                        setBackground(amarilloPastel);
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!esSeleccionado) {
                        setBackground(verdeBotonNormal);
                        setForeground(Color.WHITE);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            if (!esSeleccionado) {
                g2.setColor(new Color(251, 232, 138, 100));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ============================
    // TARJETA CONTENEDORA REDONDEADA
    // ============================
    private JPanel crearCardPanel(Color colorBorde, int alfa, float grosor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 38, 35, 180));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 22, 22));
                g2.setColor(new Color(colorBorde.getRed(), colorBorde.getGreen(), colorBorde.getBlue(), alfa));
                g2.setStroke(new BasicStroke(grosor));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 22, 22));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(null);
        return card;
    }

    // Constructor de la ventana
    public GestionDivisas() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Divisas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);

        cargarDatosDesdeAPI(); // Carga inicial
    }

    // Panel principal
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null); // Coordenadas libres x, y
            crearSidebar();
            crearContenido();
        }

        // Barra lateral izquierda
        private void crearSidebar() {
            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                }
            };
            sidebar.setOpaque(false);
            sidebar.setLayout(null);
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] botones = {
                "Gestión de Usuarios", "Gestión de Menores",
                "Gestión de Cuentas", "Gestión de Tarjetas",
                "Gestión de Divisas", "Gestión de Transacciones"
            };

            int y = 140;
            for (String texto : botones) {
                boolean activo = texto.equals("Gestión de Divisas");
                BotonSidebarNeo btn = new BotonSidebarNeo(texto, activo);
                btn.setBounds(20, y, 250, 55);

                if (texto.equals("Gestión de Usuarios")) {
                    btn.addActionListener(e -> {
                        new GestionUsuario();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Menores")) {
                    btn.addActionListener(e -> {
                        new GestionMenores();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Tarjetas")) {
                    btn.addActionListener(e -> {
                        new GestionTarjeta();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Divisas")) {
                    btn.addActionListener(e -> {
                        new GestionDivisas();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Transacciones")) {
                    btn.addActionListener(e -> {
                        new GestionTransacciones();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }
            add(sidebar);
        }

        // Contenedor central
        private void crearContenido() {
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setOpaque(false);
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            // BANNER
            JPanel banner = crearCardPanel(amarilloPastel, 230, 1f);
            banner.setBounds(0, 0, 1300, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Gestión de Divisas");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta las divisas disponibles en el sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 600, 20);
            banner.add(subtitulo);

            BotonNeo btnVolver = new BotonNeo("← Volver");
            btnVolver.setBounds(1300 - 160, 32, 120, 45);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            banner.add(btnVolver);

            // Contenedor de la tabla
            JPanel panelTabla = crearCardPanel(Color.WHITE, 100, 1f);
            panelTabla.setBounds(0, 130, 1300, 450);
            panel.add(panelTabla);

            // encabezado
            String[] columnas = {"MONEDA", "TIPO DE CAMBIO (BASE: GTQ)", "ÚLTIMA ACTUALIZACIÓN", "ESTADO", "ACCIONES"};
            
            modeloTabla = new DefaultTableModel(null, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable tabla = new JTable(modeloTabla);
            tabla.setRowHeight(40);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(94, 116, 73));
            tabla.setSelectionBackground(new Color(251, 232, 138));
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(15, 15, 1270, 420);
            panelTabla.add(scroll);

            int bx = 40; int by = 620; int bw = 250; int bh = 50;

            BotonNeo btnActualizar = new BotonNeo("Actualizar tipo de cambio");
            btnActualizar.setBounds(bx, by, bw, bh);
            btnActualizar.addActionListener(e -> cargarDatosDesdeAPI());
            panel.add(btnActualizar);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Carga de datos usando GTQ como base
    private void cargarDatosDesdeAPI() {
        modeloTabla.setRowCount(0);
        modeloTabla.addRow(new Object[]{"Cargando...", "Por favor espere...", "", "", ""});

        new Thread(() -> {
            try {
                // Incluye USD y otras divisas globales de interés
                String[] monedasAInteresar = {"EUR", "MXN", "COP", "ARS", "GBP", "BRL", "USD"};
                String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                // Petición a la API usando GTQ como base original
                Map<String, String> tasasObtenidas = API.obtenerTasas("GTQ", monedasAInteresar);

                SwingUtilities.invokeLater(() -> {
                    modeloTabla.setRowCount(0); 
                    for (String moneda : monedasAInteresar) {
                        String tasa = tasasObtenidas.getOrDefault(moneda, "N/A");
                        modeloTabla.addRow(new Object[]{
                            "GTQ a " + moneda, // Texto dinámico corregido para base GTQ
                            tasa, 
                            fechaActual, 
                            "Activo", 
                            "Disponibilizado"
                        });
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    modeloTabla.setRowCount(0);
                    modeloTabla.addRow(new Object[]{"ERROR", "No se pudo actualizar: " + e.getMessage(), "", "", ""});
                });
            }
        }).start();
    }
}