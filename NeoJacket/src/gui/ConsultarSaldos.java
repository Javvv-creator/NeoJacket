package gui;

import gui.Dashboard;
import gui.Saldos;
import gui.AgregarFondos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ConsultarSaldos extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public ConsultarSaldos() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Consultar Saldos");
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

            JButton btnSaldos = new JButton("Saldos");
            btnSaldos.setBounds(20, 140, 250, 55);
            btnSaldos.setFocusPainted(false);
            btnSaldos.setBorderPainted(false);
            btnSaldos.setBackground(new Color(251, 232, 138));
            btnSaldos.setForeground(Color.BLACK);
            btnSaldos.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnSaldos.addActionListener(e -> {
                new Saldos().setVisible(true);
                dispose();
            });
            sidebar.add(btnSaldos);

            String[] botonesMenu = {"Bancos conectados", "Transferencias", "Divisas", "Historial"};
            int y = 210;

            for (String textoBtn : botonesMenu) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                btn.addActionListener(e -> {
                    new Dashboard().setVisible(true);
                    dispose();
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

            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(94, 116, 73, 200));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            JButton btnTab1 = crearBotonPestaña("Agregar Fondos", 0);
            btnTab1.addActionListener(e -> { 
                new AgregarFondos().setVisible(true); 
                dispose(); 
            });
            barraSuperior.add(btnTab1);

            JButton btnTab2 = crearBotonPestaña("Actualizar Saldos", 433);
            btnTab2.addActionListener(e -> { 
                new ActualizarSaldos().setVisible(true); 
                dispose(); 
            });
            barraSuperior.add(btnTab2);

            JButton btnTab3 = new JButton("Consultar Saldos");
            btnTab3.setBounds(867, 0, 433, 55);
            btnTab3.setBackground(new Color(251, 232, 138, 200)); 
            btnTab3.setForeground(Color.BLACK);
            btnTab3.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab3.setFocusPainted(false);
            btnTab3.setBorder(BorderFactory.createEmptyBorder());
            barraSuperior.add(btnTab3);

            PanelFormularioRedondeado panelTabla = new PanelFormularioRedondeado();
            panelTabla.setBounds(225, 95, 850, 580);
            panelTabla.setLayout(null);
            contenedor.add(panelTabla);

            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(30, 20, 300, 25);
            panelTabla.add(lblBanco);

            JTextFieldRedondeado cbBancosEmulado = new JTextFieldRedondeado();
            cbBancosEmulado.setBounds(30, 50, 790, 45);
            cbBancosEmulado.setText(" Banco Industrial");
            cbBancosEmulado.setEditable(false);
            panelTabla.add(cbBancosEmulado);

            String[] columnas = {"Fecha", "Actividad / Descripción", "Monto", "Saldo Restante"};
            Object[][] datos = {
                {"01/10/2023", "Pago de Planilla", "Q5,000.00", "Q15,430.50"},
                {"05/10/2023", "Compra Supermercado", "-Q450.00", "Q14,980.50"},
                {"10/10/2023", "Transferencia Recibida", "Q1,200.00", "Q16,180.50"},
                {"", "", "", ""}, 
                {"", "", "", ""}
            };

            DefaultTableModel model = new DefaultTableModel(datos, columnas);
            JTable tabla = new JTable(model);
            
            tabla.setBackground(new Color(44, 59, 49)); 
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(100, 120, 105));
            tabla.setRowHeight(40);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tabla.setSelectionBackground(new Color(254, 237, 142, 100));

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(35, 50, 40));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setBorder(BorderFactory.createLineBorder(new Color(100, 120, 105)));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBounds(30, 120, 790, 350);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(100, 120, 105)));
            panelTabla.add(scroll);

            JButton btnDetalles = new JButton("Consultar Detalles") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnDetalles.setBounds(30, 495, 790, 50);
            btnDetalles.setBackground(new Color(251, 232, 138)); 
            btnDetalles.setForeground(Color.BLACK);
            btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnDetalles.setFocusPainted(false);
            btnDetalles.setContentAreaFilled(false);
            btnDetalles.setBorderPainted(false);
            btnDetalles.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnDetalles.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Mostrando historial detallado...");
            });
            panelTabla.add(btnDetalles);
        }

        private JButton crearBotonPestaña(String texto, int xPos) {
            JButton btn = new JButton(texto);
            btn.setBounds(xPos, 0, 434, 55);
            btn.setBackground(new Color(25, 38, 35, 100));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            return btn;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class PanelFormularioRedondeado extends JPanel {
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
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
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
            java.util.logging.Logger.getLogger(ConsultarSaldos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new ConsultarSaldos().setVisible(true);
        });
    }
}