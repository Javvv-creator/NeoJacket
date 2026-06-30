package gui;

import gui.Dashboard;
import gui.Saldos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class BancosConectados extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    
    // Fuentes globales para mantener la coherencia visual
    Font tituloPanel = new Font("Segoe UI", Font.BOLD, 16);
    Font textoNormal = new Font("Segoe UI", Font.PLAIN, 14);
    Font textoDestacado = new Font("Segoe UI", Font.BOLD, 14);

    public BancosConectados() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Bancos Conectados");
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

            JButton btnSaldos = new JButton("Saldos");
            btnSaldos.setBounds(20, 140, 250, 55);
            btnSaldos.setFocusPainted(false);
            btnSaldos.setBorderPainted(false);
            btnSaldos.setBackground(new Color(94, 116, 73));
            btnSaldos.setForeground(Color.WHITE);
            btnSaldos.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnSaldos.addActionListener(e -> {
                new Saldos().setVisible(true);
                dispose();
            });
            sidebar.add(btnSaldos);

            // Botón Bancos Conectados (Activo)
            JButton btnBancos = new JButton("Bancos conectados");
            btnBancos.setBounds(20, 210, 250, 55);
            btnBancos.setFocusPainted(false);
            btnBancos.setBorderPainted(false);
            btnBancos.setBackground(new Color(251, 232, 138));
            btnBancos.setForeground(Color.BLACK);
            btnBancos.setFont(new Font("Segoe UI", Font.BOLD, 14));
            sidebar.add(btnBancos);

            String[] botonesMenu = {"Transferencias", "Divisas", "Historial"};
            int y = 280;

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
            // Contenedor principal translúcido
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // ------------------------------------------
            // PANEL 1: BANCOS CONECTADOS (Superior Izquierda)
            // ------------------------------------------
            PanelRedondeado p1Lista = new PanelRedondeado();
            p1Lista.setBounds(60, 60, 560, 220);
            p1Lista.setLayout(null);
            contenedor.add(p1Lista);

            JLabel lblP1Titulo = new JLabel("Bancos Conectados");
            lblP1Titulo.setForeground(Color.WHITE);
            lblP1Titulo.setFont(tituloPanel);
            lblP1Titulo.setBounds(30, 20, 500, 25);
            p1Lista.add(lblP1Titulo);

            String[] listaBancos = {"• Banco Banrural", "• Banco G&T", "• Banco Industrial"};
            int yLista = 65;
            for (String banco : listaBancos) {
                JLabel lblBancoItem = new JLabel(banco);
                lblBancoItem.setForeground(Color.WHITE);
                lblBancoItem.setFont(textoNormal);
                lblBancoItem.setBounds(50, yLista, 460, 25);
                p1Lista.add(lblBancoItem);
                yLista += 35;
            }

            // ------------------------------------------
            // PANEL 2: DETALLE DE SALDOS (Superior Derecha)
            // ------------------------------------------
            PanelRedondeado p2Saldos = new PanelRedondeado();
            p2Saldos.setBounds(680, 60, 560, 220);
            p2Saldos.setLayout(null);
            contenedor.add(p2Saldos);

            JLabel lblP2Titulo = new JLabel("Saldos");
            lblP2Titulo.setForeground(Color.WHITE);
            lblP2Titulo.setFont(tituloPanel);
            lblP2Titulo.setBounds(0, 20, 560, 25);
            lblP2Titulo.setHorizontalAlignment(SwingConstants.CENTER);
            p2Saldos.add(lblP2Titulo);

            JLabel lblSaldoDisp = new JLabel("Saldo Disponible:");
            lblSaldoDisp.setForeground(Color.WHITE);
            lblSaldoDisp.setFont(textoDestacado);
            lblSaldoDisp.setBounds(50, 75, 200, 25);
            p2Saldos.add(lblSaldoDisp);

            JLabel lblMontoSald = new JLabel("Q. 500.00");
            lblMontoSald.setForeground(Color.WHITE);
            lblMontoSald.setFont(textoNormal);
            lblMontoSald.setBounds(380, 75, 150, 25);
            p2Saldos.add(lblMontoSald);

            JLabel lblUltimaAct = new JLabel("Última Actualización:");
            lblUltimaAct.setForeground(Color.WHITE);
            lblUltimaAct.setFont(textoDestacado);
            lblUltimaAct.setBounds(50, 135, 200, 25);
            p2Saldos.add(lblUltimaAct);

            JLabel lblFechaAct = new JLabel("7/06/2026");
            lblFechaAct.setForeground(Color.WHITE);
            lblFechaAct.setFont(textoNormal);
            lblFechaAct.setBounds(380, 135, 150, 25);
            p2Saldos.add(lblFechaAct);

            // ------------------------------------------
            // PANEL 3: SELECCIÓN Y ESTADO (Inferior Izquierda)
            // ------------------------------------------
            PanelRedondeado p3Seleccion = new PanelRedondeado();
            p3Seleccion.setBounds(60, 310, 560, 390);
            p3Seleccion.setLayout(null);
            contenedor.add(p3Seleccion);

            JLabel lblSelBanco = new JLabel("Seleccione su banco");
            lblSelBanco.setForeground(Color.WHITE);
            lblSelBanco.setFont(tituloPanel);
            lblSelBanco.setBounds(30, 25, 500, 25);
            p3Seleccion.add(lblSelBanco);

            JTextFieldRedondeado txtSelComboEmulado = new JTextFieldRedondeado();
            txtSelComboEmulado.setBounds(30, 65, 500, 45);
            txtSelComboEmulado.setText(" Banco Industrial");
            txtSelComboEmulado.setEditable(false);
            txtSelComboEmulado.setFont(textoNormal);
            p3Seleccion.add(txtSelComboEmulado);

            // Sección de detalles integrados en el mismo bloque izquierdo inferior
            String[][] infoDetalle = {
                {"Nombre del Banco:", "Banco Banrural"},
                {"Tipo de Cuenta:", "Monetaria"},
                {"Estado:", "Conectado"}
            };
            int yInfo = 150;
            for (String[] fila : infoDetalle) {
                JLabel lblTituloFila = new JLabel(fila[0]);
                lblTituloFila.setForeground(Color.WHITE);
                lblTituloFila.setFont(textoDestacado);
                lblTituloFila.setBounds(30, yInfo, 200, 25);
                p3Seleccion.add(lblTituloFila);

                JLabel lblValorFila = new JLabel(fila[1]);
                lblValorFila.setForeground(Color.WHITE);
                lblValorFila.setFont(textoNormal);
                lblValorFila.setBounds(250, yInfo, 280, 25);
                p3Seleccion.add(lblValorFila);
                
                yInfo += 55;
            }

            // ------------------------------------------
            // PANEL 4: TABLA DE MOVIMIENTOS (Inferior Derecha)
            // ------------------------------------------
            PanelRedondeado p4Movimientos = new PanelRedondeado();
            p4Movimientos.setBounds(680, 310, 560, 390);
            p4Movimientos.setLayout(null);
            contenedor.add(p4Movimientos);

            JLabel lblMovimientos = new JLabel("Movimientos");
            lblMovimientos.setForeground(Color.WHITE);
            lblMovimientos.setFont(tituloPanel);
            lblMovimientos.setBounds(0, 25, 560, 25);
            lblMovimientos.setHorizontalAlignment(SwingConstants.CENTER);
            p4Movimientos.add(lblMovimientos);

            String[] columnas = {"Fecha", "Tipo", "Monto"};
            Object[][] datos = {
                {"04/06/2026", "Depósito", "Q. 1,200.00"},
                {"05/06/2026", "Retiro", "-Q. 450.00"},
                {"07/06/2026", "Intereses", "Q. 15.50"},
                {"", "", ""},
                {"", "", ""}
            };

            DefaultTableModel model = new DefaultTableModel(datos, columnas);
            JTable tablaMovs = new JTable(model);
            tablaMovs.setBackground(new Color(44, 59, 49));
            tablaMovs.setForeground(Color.WHITE);
            tablaMovs.setGridColor(new Color(100, 120, 105));
            tablaMovs.setRowHeight(40);
            tablaMovs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tablaMovs.setSelectionBackground(new Color(251, 232, 138, 100));

            JTableHeader header = tablaMovs.getTableHeader();
            header.setBackground(new Color(35, 50, 40));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setBorder(BorderFactory.createLineBorder(new Color(100, 120, 105)));

            JScrollPane scrollTabla = new JScrollPane(tablaMovs);
            scrollTabla.setBounds(30, 75, 500, 280);
            scrollTabla.setOpaque(false);
            scrollTabla.getViewport().setOpaque(false);
            scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(100, 120, 105)));
            p4Movimientos.add(scrollTabla);
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
            java.util.logging.Logger.getLogger(BancosConectados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new BancosConectados().setVisible(true);
        });
    }
}
