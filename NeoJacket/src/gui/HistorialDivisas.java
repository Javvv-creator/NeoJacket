package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class HistorialDivisas extends JFrame {

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
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN NEO
    // ============================
    class BotonNeo extends JButton {
        private Color normal;
        private Color hover;

        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.BLACK);
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

    // ============================
    // CONSTRUCTOR
    // ============================
    public HistorialDivisas() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Historial de Divisas");
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
            crearHistorial();
        }

        private void crearHistorial() {

            // LOGO
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL PRINCIPAL
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(350, 120, 1200, 700);
            panel.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panel);

            JLabel titulo = new JLabel("Historial de cambios");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            // BUSCADOR
            RoundedTextField txtBuscar = new RoundedTextField(20);
            txtBuscar.setBounds(850, 25, 300, 40);
            txtBuscar.setText("Buscar divisas...");
            panel.add(txtBuscar);

            // TABLA
            String[] columnas = {"MONEDA", "TIPO ANTERIOR", "TIPO NUEVO", "FECHA", "USUARIO"};

            Object[][] datos = {
                {"USD", "7.70", "7.75", "30/05/2026", "Admin1"},
                {"GTM", "1.00", "1.00", "31/05/2026", "Admin2"},
                {"MXN", "0.43", "0.45", "31/05/2026", "Admin1"}
            };

            JTable tabla = new JTable(datos, columnas);
            tabla.setRowHeight(40);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(94, 116, 73));
            tabla.setSelectionBackground(new Color(251, 232, 138));
            tabla.setSelectionForeground(Color.BLACK);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(40, 90, 1120, 500);
            panel.add(scroll);

            // BOTONES
            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            BotonNeo btnDetalles = new BotonNeo("Ver detalles", verde, verdeHover);
            btnDetalles.setForeground(Color.WHITE);
            btnDetalles.setBounds(40, 620, 200, 50);
            panel.add(btnDetalles);

            btnDetalles.addActionListener(e -> {
                JOptionPane.showMessageDialog(null,
                        "Aquí iría la vista de detalles (si la deseas la hacemos).",
                        "Detalles",
                        JOptionPane.INFORMATION_MESSAGE);
            });

            BotonNeo btnRegresar = new BotonNeo("Regresar", verde, verdeHover);
            btnRegresar.setForeground(Color.WHITE);
            btnRegresar.setBounds(260, 620, 200, 50);
            panel.add(btnRegresar);

            btnRegresar.addActionListener(e -> {
                new GestionDivisas();
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
