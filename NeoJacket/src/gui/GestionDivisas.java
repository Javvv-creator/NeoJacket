package gui;

import java.awt.*;
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

    // Campo de texto con diseño redondeado
    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 200)); // Fondo oscuro
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(251, 232, 138)); // Borde dorado
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Botón personalizado con efecto Hover
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

    // Constructor de la ventana
    public GestionDivisas() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Divisas");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
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
            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] botones = {
                "Gestión de Usuarios", "Gestión de Menores Supervisados",
                "Gestión de Cuentas", "Gestión de Tarjetas",
                "Gestión de Divisas", "Gestión de Transacciones"
            };

            int y = 140;
            for (String texto : botones) {
                JButton btn = new JButton(texto);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);

                if (texto.equals("Gestión de Divisas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
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
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Gestión de Divisas");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta las divisas disponibles en el sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 600, 20);
            panel.add(subtitulo);

            // Contenedor de la tabla
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 130, 1180, 450);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
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
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(10, 10, 1160, 430);
            panelTabla.add(scroll);

            int bx = 40; int by = 620; int bw = 250; int bh = 50;

            BotonNeo btnActualizar = new BotonNeo("Actualizar tipo de cambio");
            btnActualizar.setBounds(bx, by, bw, bh);
            btnActualizar.addActionListener(e -> cargarDatosDesdeAPI());
            panel.add(btnActualizar);


            JButton btnVolver = new JButton("Volver");
            btnVolver.setBounds(1080, 20, 120, 40);
            panel.add(btnVolver);
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
