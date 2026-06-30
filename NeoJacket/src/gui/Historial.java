package gui;

import gui.Dashboard;
import gui.Saldos;
import javax.swing.*;
import java.awt.*;

public class Historial extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

    // Fuentes corporativas de Neo Jacket
    Font tituloPanel = new Font("Segoe UI", Font.BOLD, 16);
    Font textoMuestra = new Font("Segoe UI", Font.PLAIN, 13);

    public Historial() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Historial");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
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
                
                // ¡Historial se mantiene activo en amarillo!
                if (nombre.equals("Historial")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }
                
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btn.addActionListener(e -> {
                    if (nombre.equals("Saldos")) { new Saldos().setVisible(true); dispose(); }
                    if (nombre.equals("Bancos conectados")) { new BancosConectados().setVisible(true); dispose(); }
                    if (nombre.equals("Transferencias")) { new Transferencias().setVisible(true); dispose(); }
                    if (nombre.equals("Divisas")) { new Divisas().setVisible(true); dispose(); }
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

            // Título superior del panel de control
            JLabel lblHeader = new JLabel("Historial General");
            lblHeader.setForeground(Color.WHITE);
            lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblHeader.setBounds(50, 25, 400, 30);
            contenedor.add(lblHeader);

            // ------------------------------------------
            // PANEL 1: Tus Cuentas (Superior Largo)
            // ------------------------------------------
            PanelRedondeado pTusCuentas = new PanelRedondeado();
            pTusCuentas.setBounds(50, 80, 1200, 180);
            pTusCuentas.setLayout(null);
            contenedor.add(pTusCuentas);

            JLabel lblCuentasTit = new JLabel("Tus Cuentas");
            lblCuentasTit.setForeground(Color.WHITE);
            lblCuentasTit.setFont(tituloPanel);
            lblCuentasTit.setBounds(30, 20, 200, 25);
            pTusCuentas.add(lblCuentasTit);
            
            JLabel lblCuentasSub = new JLabel("No hay cuentas inactivas o archivadas registradas recientemente.");
            lblCuentasSub.setForeground(new Color(170, 185, 175));
            lblCuentasSub.setFont(textoMuestra);
            lblCuentasSub.setBounds(30, 65, 600, 20);
            pTusCuentas.add(lblCuentasSub);

            // ------------------------------------------
            // PANEL 2: Bancos Conectados (Centro Izquierda)
            // ------------------------------------------
            PanelRedondeado pBancosCon = new PanelRedondeado();
            pBancosCon.setBounds(50, 285, 380, 200);
            pBancosCon.setLayout(null);
            contenedor.add(pBancosCon);

            JLabel lblBancosTit = new JLabel("Bancos Conectados");
            lblBancosTit.setForeground(Color.WHITE);
            lblBancosTit.setFont(tituloPanel);
            lblBancosTit.setBounds(25, 20, 220, 25);
            pBancosCon.add(lblBancosTit);

            // ------------------------------------------
            // PANEL 3: Transferencias Recientes (Centro Derecha)
            // ------------------------------------------
            PanelRedondeado pTransRecientes = new PanelRedondeado();
            pTransRecientes.setBounds(460, 285, 790, 200);
            pTransRecientes.setLayout(null);
            contenedor.add(pTransRecientes);

            JLabel lblTransTit = new JLabel("Transferencias Recientes");
            lblTransTit.setForeground(Color.WHITE);
            lblTransTit.setFont(tituloPanel);
            lblTransTit.setBounds(25, 20, 300, 25);
            pTransRecientes.add(lblTransTit);

            // ------------------------------------------
            // PANEL 4: Historial de Actividad (Inferior Izquierda)
            // ------------------------------------------
            PanelRedondeado pHistActividad = new PanelRedondeado();
            pHistActividad.setBounds(50, 510, 580, 210);
            pHistActividad.setLayout(null);
            contenedor.add(pHistActividad);

            JLabel lblHistActTit = new JLabel("Historial de Actividad");
            lblHistActTit.setForeground(Color.WHITE);
            lblHistActTit.setFont(tituloPanel);
            lblHistActTit.setBounds(25, 20, 300, 25);
            pHistActividad.add(lblHistActTit);

            // ------------------------------------------
            // PANEL 5: Cambio de Divisas (Inferior Derecha)
            // ------------------------------------------
            PanelRedondeado pCambioDivisas = new PanelRedondeado();
            pCambioDivisas.setBounds(660, 510, 590, 210);
            pCambioDivisas.setLayout(null);
            contenedor.add(pCambioDivisas);

            JLabel lblDivisasTit = new JLabel("Cambio de Divisas");
            lblDivisasTit.setForeground(Color.WHITE);
            lblDivisasTit.setFont(tituloPanel);
            lblDivisasTit.setBounds(25, 20, 300, 25);
            pCambioDivisas.add(lblDivisasTit);
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
            
            g2.setColor(new Color(94, 116, 73, 190)); 
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            
            g2.setColor(new Color(255, 255, 255, 80)); 
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();
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
            java.util.logging.Logger.getLogger(Historial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new Historial().setVisible(true);
        });
    }
}
