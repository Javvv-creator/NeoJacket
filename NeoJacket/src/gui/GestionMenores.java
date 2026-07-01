


package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class GestionMenores extends JFrame {

    private Image fondo;
    private Image logo;

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

    public GestionMenores() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Menores Supervisados");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearPanelPrincipal();
        }

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
                "Gestión de Menores",
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

                if (texto.equals("Gestión de Menores")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }

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

        private void crearPanelPrincipal() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            // BANNER
            JPanel banner = new JPanel();
            banner.setLayout(null);
            banner.setBackground(new Color(25, 38, 35, 230));
            banner.setBounds(0, 0, 1300, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Gestión de Menores Supervisados");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 700, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta los menores supervisados del sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 500, 20);
            banner.add(subtitulo);

            // PANEL FILTROS
            JPanel panelFiltros = new JPanel();
            panelFiltros.setLayout(null);
            panelFiltros.setBackground(new Color(25, 38, 35, 180));
            panelFiltros.setBounds(40, 130, 1180, 100);
            panelFiltros.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 3, true));
            panel.add(panelFiltros);

            JLabel lblIdMenor = new JLabel("ID (Menor)");
            lblIdMenor.setForeground(Color.WHITE);
            lblIdMenor.setBounds(30, 20, 150, 25);
            panelFiltros.add(lblIdMenor);

            JTextField txtIdMenor = new JTextField();
            txtIdMenor.setBounds(150, 15, 200, 35);
            panelFiltros.add(txtIdMenor);

            JLabel lblIdTutor = new JLabel("ID (Tutor)");
            lblIdTutor.setForeground(Color.WHITE);
            lblIdTutor.setBounds(400, 20, 150, 25);
            panelFiltros.add(lblIdTutor);

            JTextField txtIdTutor = new JTextField();
            txtIdTutor.setBounds(500, 15, 200, 35);
            panelFiltros.add(txtIdTutor);

            // PANEL TABLA
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 250, 1180, 330);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            panel.add(panelTabla);

            String[] columnas = {"ID", "MENOR", "ID (TUTOR)", "TUTOR"};
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

            // BOTONES
            BotonNeo btnDetalles = new BotonNeo("Ver detalles");
            btnDetalles.setBounds(40, 600, 180, 45);
            panel.add(btnDetalles);

            BotonNeo btnDesvincular = new BotonNeo("Desvincular");
            btnDesvincular.setBounds(260, 600, 220, 50);
            panel.add(btnDesvincular);

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

