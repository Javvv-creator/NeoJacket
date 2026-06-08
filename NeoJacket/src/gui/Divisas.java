package gui;

import gui.Dashboard;
import gui.Saldos;
import javax.swing.*;
import java.awt.*;

public class Divisas extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

    Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 18);
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public Divisas() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Divisas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new FondoPanel());
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
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

            String[] nombresBotones = {"Saldos", "Bancos conectados", "Transferencias", "Divisas", "Historial"};
            int y = 140;
            for (String nombre : nombresBotones) {
                JButton btn = new JButton(nombre);
                btn.setBounds(20, y, 250, 55);
                
                // Si es Divisas, ponerlo en AMARILLO (Activo)
                if (nombre.equals("Divisas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }
                
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                btn.addActionListener(e -> {
                    if (nombre.equals("Saldos")) { new Saldos().setVisible(true); dispose(); }
                    if (nombre.equals("Transferencias")) { new Transferencias().setVisible(true); dispose(); }
                    if (nombre.equals("Bancos conectados")) { new BancosConectados().setVisible(true); dispose(); }
                    if (nombre.equals("Historial")) { new Dashboard().setVisible(true); dispose(); }
                });

                sidebar.add(btn);
                y += 70;
            }
            add(sidebar);
        }

        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // Título superior "Divisas"
            JLabel lblHeader = new JLabel("Divisas");
            lblHeader.setForeground(Color.WHITE);
            lblHeader.setFont(tituloSeccion);
            lblHeader.setBounds(60, 40, 200, 30);
            contenedor.add(lblHeader);

            // ==========================================
            // TARJETA DEL FORMULARIO (Centrado y reajustado proporcionalmente)
            // ==========================================
            PanelRedondeado pForm = new PanelRedondeado();
            pForm.setBounds(390, 90, 520, 580);
            pForm.setLayout(null);
            contenedor.add(pForm);

            // 1. Moneda origen
            JLabel lblOrigen = new JLabel("Moneda origen");
            lblOrigen.setForeground(Color.WHITE);
            lblOrigen.setFont(tituloCampos);
            lblOrigen.setBounds(40, 40, 440, 25);
            pForm.add(lblOrigen);

            JTextFieldRedondeado txtOrigen = new JTextFieldRedondeado();
            txtOrigen.setBounds(40, 75, 440, 50);
            txtOrigen.setFont(textoInputs);
            pForm.add(txtOrigen);

            // 2. Moneda destino
            JLabel lblDestino = new JLabel("Moneda destino");
            lblDestino.setForeground(Color.WHITE);
            lblDestino.setFont(tituloCampos);
            lblDestino.setBounds(40, 165, 440, 25);
            pForm.add(lblDestino);

            JTextFieldRedondeado txtDestino = new JTextFieldRedondeado();
            txtDestino.setBounds(40, 200, 440, 50);
            txtDestino.setFont(textoInputs);
            pForm.add(txtDestino);

            // 3. Cantidad
            JLabel lblCantidad = new JLabel("Cantidad");
            lblCantidad.setForeground(Color.WHITE);
            lblCantidad.setFont(tituloCampos);
            lblCantidad.setBounds(40, 295, 440, 25);
            pForm.add(lblCantidad);

            JTextFieldRedondeado txtCantidad = new JTextFieldRedondeado();
            txtCantidad.setBounds(40, 330, 440, 50);
            txtCantidad.setFont(textoInputs);
            pForm.add(txtCantidad);

            // 4. Botón Convertir
            JButton btnConvertir = new JButton("Convertir") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnConvertir.setBounds(135, 450, 250, 60);
            btnConvertir.setBackground(new Color(251, 232, 138)); // Amarillo Neo Jacket
            btnConvertir.setForeground(Color.BLACK);
            btnConvertir.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnConvertir.setFocusPainted(false);
            btnConvertir.setContentAreaFilled(false);
            btnConvertir.setBorderPainted(false);
            btnConvertir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnConvertir.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Procesando conversión de divisas...");
            });
            
            pForm.add(btnConvertir);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class PanelRedondeado extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(new Color(94, 116, 73, 190)); // Fondo verde translúcido unificado
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            
            g2.setColor(new Color(255, 255, 255, 80)); // Borde sutil
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
        }
    }

    class JTextFieldRedondeado extends JTextField {
        public JTextFieldRedondeado() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            g2.dispose();
            super.paintComponent(g);
        }
    }

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

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Divisas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new Divisas().setVisible(true);
        });
    }
}