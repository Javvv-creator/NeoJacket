package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class GestionTarjeta extends JFrame {

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
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
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

    // ============================
    // CONSTRUCTOR
    // ============================
    public GestionTarjeta() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Tarjetas");
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
            crearSidebar();
            crearContenido();
        }

        // ============================
        // SIDEBAR
        // ============================
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
                "Gestión de Usuarios",
                "Gestión de Menores Supervisados",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;

            for (String texto : botones) {
                JButton btn = new JButton(texto);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);

                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);

                if (texto.equals("Gestión de Tarjetas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                }

                // VIAJES
                if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                }

                if (texto.equals("Gestión de Tarjetas")) {
                    btn.addActionListener(e -> {
                        new GestionTarjeta();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            add(sidebar);
        }

        // ============================
        // CONTENIDO PRINCIPAL
        // ============================
        private void crearContenido() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Gestión de Tarjetas");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta las tarjetas registradas del sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 600, 20);
            panel.add(subtitulo);

            Color amarillo = new Color(251, 232, 138);

            // ============================
            // PANEL FILTROS
            // ============================
            JPanel panelFiltros = new JPanel();
            panelFiltros.setLayout(null);
            panelFiltros.setBackground(new Color(25, 38, 35, 180));
            panelFiltros.setBounds(40, 130, 1180, 120);
            panelFiltros.setBorder(BorderFactory.createLineBorder(amarillo, 2, true));
            panel.add(panelFiltros);

            JLabel lblId = new JLabel("ID Tarjeta");
            lblId.setForeground(amarillo);
            lblId.setBounds(30, 10, 200, 20);
            panelFiltros.add(lblId);

            RoundedTextField txtId = new RoundedTextField(20);
            txtId.setBounds(30, 40, 250, 40);
            panelFiltros.add(txtId);

            JLabel lblTipo = new JLabel("Tipo");
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(430, 10, 150, 20);
            panelFiltros.add(lblTipo);

            JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos"});
            cbTipo.setBounds(430, 40, 250, 40);
            panelFiltros.add(cbTipo);

            JLabel lblEstado = new JLabel("Estado");
            lblEstado.setForeground(amarillo);
            lblEstado.setBounds(730, 10, 150, 20);
            panelFiltros.add(lblEstado);

            JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Todos"});
            cbEstado.setBounds(730, 40, 250, 40);
            panelFiltros.add(cbEstado);

            // ============================
            // TABLA
            // ============================
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 270, 1180, 330);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            panel.add(panelTabla);

            String[] columnas = {"ID", "TARJETA", "PROPIETARIO", "TIPO", "ESTADO"};

            Object[][] datos = {
                {"", "", "", "", ""},
                {"", "", "", "", ""},
                {"", "", "", "", ""}
            };

            JTable tabla = new JTable(datos, columnas);
            tabla.setRowHeight(35);
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
            scroll.setBounds(10, 10, 1160, 310);
            panelTabla.add(scroll);

            // ============================
            // BOTONES ABAJO
            // ============================
            int bx = 40;
            int by = 630;
            int bw = 250;
            int bh = 50;

            BotonNeo btnDetalles = new BotonNeo("Ver detalles");
            btnDetalles.setBounds(bx, by, bw, bh);
            btnDetalles.addActionListener(e -> {
                new DetalleTarjeta();
                dispose();
            });
            panel.add(btnDetalles);

            BotonNeo btnBloquear = new BotonNeo("Bloquear Tarjeta");
            btnBloquear.setBounds(bx + 300, by, bw, bh);
            btnBloquear.addActionListener(e -> {
                new BloquearTarjeta();
                dispose();
            });
            panel.add(btnBloquear);

            BotonNeo btnDesbloquear = new BotonNeo("Desbloquear Tarjeta");
            btnDesbloquear.setBounds(bx + 600, by, bw, bh);
            btnDesbloquear.addActionListener(e -> {
                new DesbloquearTarjeta();
                dispose();
            });
            panel.add(btnDesbloquear);

            // ============================
            // BOTÓN VOLVER
            // ============================
            JButton btnVolver = new JButton("Volver");
            btnVolver.setBounds(1080, 20, 120, 40);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            panel.add(btnVolver);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

