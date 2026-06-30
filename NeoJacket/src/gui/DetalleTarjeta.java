package gui;

import java.awt.*;
import javax.swing.*;

public class DetalleTarjeta extends JFrame {

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
    public DetalleTarjeta() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Detalle de Tarjeta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
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

            JLabel titulo = new JLabel("Detalle de Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            JLabel subtitulo = new JLabel("Consulta los detalles completos de la tarjeta seleccionada");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 600, 20);
            panel.add(subtitulo);

            Color amarillo = new Color(251, 232, 138);

            // ============================
            // DATOS DE LA TARJETA
            // ============================
            int y = 120;

            panel.add(crearLabel("ID Tarjeta:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Número de tarjeta:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Propietario:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Tipo de tarjeta:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Banco:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Estado:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Fecha de creación:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Última actualización:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            y += 60;
            panel.add(crearLabel("Última transacción:", 30, y, amarillo));
            panel.add(crearField(250, y - 5));

            // ============================
            // BOTÓN REGRESAR
            // ============================
            BotonNeo btnRegresar = new BotonNeo("Regresar");
            btnRegresar.setBounds(30, 700, 200, 50);
            btnRegresar.addActionListener(e -> {
                new GestionTarjeta();
                dispose();
            });
            panel.add(btnRegresar);
        }

        private JLabel crearLabel(String texto, int x, int y, Color color) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(color);
            lbl.setBounds(x, y, 250, 25);
            return lbl;
        }

        private RoundedTextField crearField(int x, int y) {
            RoundedTextField txt = new RoundedTextField(20);
            txt.setBounds(x, y, 350, 40);
            return txt;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

