package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class GestionCuentas extends JFrame {

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

            if (getModel().isRollover()) {
                g2.setColor(new Color(251, 232, 138, 220));
            } else {
                g2.setColor(new Color(94, 116, 73, 190));
            }

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
    public GestionCuentas() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Cuentas");
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
            crearPanelPrincipal();
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

                if (texto.equals("Gestión de Cuentas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }

                // VIAJES
                if (texto.equals("Gestión de Usuarios")) {
                    btn.addActionListener(e -> {
                        new GestionUsuario();
                        dispose();
                    });
                }

                if (texto.equals("Gestión de Menores Supervisados")) {
                    btn.addActionListener(e -> {
                        new GestionMenores();
                        dispose();
                    });
                }

                if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            add(sidebar);
        }

        // ============================
        // PANEL PRINCIPAL
        // ============================
        private void crearPanelPrincipal() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            // ============================
            // BANNER
            // ============================
            JPanel banner = new JPanel();
            banner.setLayout(null);
            banner.setBackground(new Color(25, 38, 35, 230));
            banner.setBounds(0, 0, 1300, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Gestión de Cuentas");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta las cuentas registradas del sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 500, 20);
            banner.add(subtitulo);

            // ============================
            // PANEL FILTROS
            // ============================
            JPanel panelFiltros = new JPanel();
            panelFiltros.setLayout(null);
            panelFiltros.setBackground(new Color(25, 38, 35, 180));
            panelFiltros.setBounds(40, 130, 1180, 120);
            panelFiltros.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            panel.add(panelFiltros);

            Color amarillo = new Color(251, 232, 138);

            JLabel lblCuenta = new JLabel("Número de cuenta");
            lblCuenta.setForeground(amarillo);
            lblCuenta.setBounds(30, 10, 200, 20);
            panelFiltros.add(lblCuenta);

            JLabel lblTipo = new JLabel("Tipo");
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(430, 10, 150, 20);
            panelFiltros.add(lblTipo);

            JLabel lblEstado = new JLabel("Estado");
            lblEstado.setForeground(amarillo);
            lblEstado.setBounds(730, 10, 150, 20);
            panelFiltros.add(lblEstado);

            RoundedTextField txtCuenta = new RoundedTextField(20);
            txtCuenta.setBounds(30, 40, 250, 40);
            panelFiltros.add(txtCuenta);

            JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos"});
            cbTipo.setBounds(430, 40, 250, 40);
            panelFiltros.add(cbTipo);

            JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Todos"});
            cbEstado.setBounds(730, 40, 250, 40);
            panelFiltros.add(cbEstado);

           // ============================
// BOTONES ABAJO
// ============================
int bx = 40;
int by = 700;   // ← ABAJO DEL TODO
int bw = 220;
int bh = 50;

BotonNeo btnExplorar = new BotonNeo("Explorar lista");
btnExplorar.setBounds(bx, by, bw, bh);
panel.add(btnExplorar);

BotonNeo btnCrear = new BotonNeo("Crear cuenta");
btnCrear.setBounds(bx + 240, by, bw, bh);
panel.add(btnCrear);

BotonNeo btnBloquear = new BotonNeo("Bloquear cuenta");
btnBloquear.setBounds(bx + 480, by, bw, bh);
panel.add(btnBloquear);

btnBloquear.addActionListener(e -> {
    new BloquearCuenta();
    dispose();
});

BotonNeo btnDesbloquear = new BotonNeo("Desbloquear cuenta");
btnDesbloquear.setBounds(bx + 720, by, bw, bh);
panel.add(btnDesbloquear);

btnDesbloquear.addActionListener(e -> {
    new DesbloquearCuenta();
    dispose();
});

BotonNeo btnInfo = new BotonNeo("Información cuenta");
btnInfo.setBounds(bx + 960, by, bw, bh);
panel.add(btnInfo);
btnInfo.addActionListener(e -> {
    new DetalleCuenta();
    dispose();
});
            // ============================
            // TABLA
            // ============================
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 330, 1180, 330);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            panel.add(panelTabla);

            String[] columnas = {"CUENTA", "PROPIETARIO", "SALDO", "ESTADO"};

            Object[][] datos = {
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""}
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
