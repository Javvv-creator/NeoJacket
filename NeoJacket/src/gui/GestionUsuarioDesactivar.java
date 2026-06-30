package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class GestionUsuarioDesactivar extends JFrame {

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

    public GestionUsuarioDesactivar() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Desactivar Usuario");
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

                if (texto.equals("Gestión de Usuarios")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
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

            JPanel banner = new JPanel();
            banner.setLayout(null);
            banner.setBackground(new Color(25, 38, 35, 230));
            banner.setBounds(0, 0, 1300, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Desactivar usuarios");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Seleccione el usuario que desea desactivar");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 500, 20);
            banner.add(subtitulo);

            // PANEL 1 — ID USUARIO (con borde amarillo)
            JPanel panelId = new JPanel();
            panelId.setLayout(null);
            panelId.setBackground(new Color(25, 38, 35, 180));
            panelId.setBounds(40, 130, 1180, 80);

            panelId.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 3, true));

            panel.add(panelId);

            JLabel lblId = new JLabel("ID de Usuario");
            lblId.setForeground(Color.WHITE);
            lblId.setBounds(30, 25, 150, 25);
            panelId.add(lblId);

            JTextField txtId = new JTextField();
            txtId.setBounds(150, 20, 200, 35);
            panelId.add(txtId);

            //  agregue el camo dpi 
            JLabel lblDpi = new JLabel("DPI de Usuario");
            lblDpi.setForeground(Color.WHITE);
            lblDpi.setBounds(400, 25, 150, 25);
            panelId.add(lblDpi);

            JTextField txtDpi = new JTextField();
            txtDpi.setBounds(520, 20, 200, 35);
            panelId.add(txtDpi);
            // ================================================================

            // PANEL 2 — TABLA (con borde blanco)
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 230, 1180, 330);

            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));

            panel.add(panelTabla);

            String[] columnas = {"ID", "Usuario", "Tipo", "Estado"};
            Object[][] datos = {};

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
            btnDetalles.setBounds(40, 580, 180, 45);
            panel.add(btnDetalles);

            BotonNeo btnAgregar = new BotonNeo("Agregar usuario");
            btnAgregar.setBounds(260, 580, 220, 50);
            panel.add(btnAgregar);

            btnAgregar.addActionListener(e -> {
                new GestionUsuarioAgregar();
                dispose();
            });

            BotonNeo btnEditar = new BotonNeo("Editar usuario");
            btnEditar.setBounds(520, 580, 220, 50);
            panel.add(btnEditar);

            btnEditar.addActionListener(e -> {
                new GestionUsuarioEditar();
                dispose();
            });

            BotonNeo btnDesactivar = new BotonNeo("Desactivar usuario");
            btnDesactivar.setBounds(780, 580, 220, 50);
            panel.add(btnDesactivar);

            // === sccion psrws rl boton  ===
            btnDesactivar.addActionListener(e -> {
                String idText = txtId.getText().trim();
                String dpiText = txtDpi.getText().trim();

                // Validación  del campo DPI no esté vacío
                if (dpiText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor, ingrese el DPI del usuario que desea desactivar.", 
                        "Campo requerido", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea desactivar al usuario con DPI: " + dpiText + "?", 
                    "Confirmar Desactivación", 
                    JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Lógica para cambiar el estado BD/Sistema...
                    
                    JOptionPane.showMessageDialog(this, 
                        "Usuario con DPI " + dpiText + " desactivado correctamente.", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    
                    txtId.setText("");
                    txtDpi.setText("");
                }
            });

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
