package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class GestionTransacciones extends JFrame {

    private Image fondo;
    private Image logo;

    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class BotonNeo extends JButton {
        private Color normal, hover;
        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public GestionTransacciones() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Gestión de Transacciones");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearInterfaz();
        }

        private void crearInterfaz() {

            // LOGO
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(350, 120, 1200, 700);
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            add(panel);

            JLabel titulo = new JLabel("Gestión de Transacciones");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 500, 40);
            panel.add(titulo);

            // BUSCADOR
            RoundedTextField txtBuscar = new RoundedTextField(20);
            txtBuscar.setBounds(850, 25, 300, 40);
            txtBuscar.setText("Buscar transacciones...");
            panel.add(txtBuscar);

            // TABLA
            String[] columnas = {"ID", "Fecha", "Cuenta", "Tipo", "Monto"};
            Object[][] datos = {
                {"TX-2024-001", "30/05/2026", "Cuenta Ahorros", "Transferencia", "Q1,250.00"},
                {"TX-2024-002", "30/05/2026", "Cuenta Corriente", "Depósito", "Q2,500.00"}
            };

            JTable tabla = new JTable(datos, columnas);
            tabla.setRowHeight(40);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(Color.GRAY);
            tabla.setSelectionBackground(Color.GRAY);
            tabla.setSelectionForeground(Color.BLACK);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(Color.GRAY);
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(40, 90, 1120, 500);
            panel.add(scroll);

            // BOTONES
            Color gris = new Color(120, 120, 120);
            Color grisHover = new Color(160, 160, 160);

            BotonNeo btnDetalles = new BotonNeo("Ver detalles", gris, grisHover);
            btnDetalles.setBounds(40, 620, 200, 50);
            panel.add(btnDetalles);

            btnDetalles.addActionListener(e -> {
                new DetallesTransaccion();
                dispose();
            });

            
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

