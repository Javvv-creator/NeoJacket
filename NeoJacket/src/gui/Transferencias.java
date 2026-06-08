package gui;

import gui.Dashboard;
import gui.Saldos;
import javax.swing.*;
import java.awt.*;

public class Transferencias extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

    // Fuentes estables del proyecto Neo Jacket
    Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 15);
    Font etiquetaCampos = new Font("Segoe UI", Font.PLAIN, 14);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public Transferencias() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Transferencias");
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
            btnSaldos.setBackground(new Color(94, 116, 73));
            btnSaldos.setForeground(Color.WHITE);
            btnSaldos.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnSaldos.addActionListener(e -> { 
                new Saldos().setVisible(true); 
                dispose(); 
            });
            sidebar.add(btnSaldos);

            JButton btnBancos = new JButton("Bancos conectados");
            btnBancos.setBounds(20, 210, 250, 55);
            btnBancos.setFocusPainted(false);
            btnBancos.setBorderPainted(false);
            btnBancos.setBackground(new Color(94, 116, 73));
            btnBancos.setForeground(Color.WHITE);
            btnBancos.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnBancos.addActionListener(e -> { 
                new BancosConectados().setVisible(true); 
                dispose(); 
            });
            sidebar.add(btnBancos);

            // Botón Transferencias (¡ACTIVO EN AMARILLO!)
            JButton btnTransferencias = new JButton("Transferencias");
            btnTransferencias.setBounds(20, 280, 250, 55);
            btnTransferencias.setBackground(new Color(251, 232, 138)); 
            btnTransferencias.setForeground(Color.BLACK);
            btnTransferencias.setFocusPainted(false);
            btnTransferencias.setBorderPainted(false);
            btnTransferencias.setFont(new Font("Segoe UI", Font.BOLD, 14));
            sidebar.add(btnTransferencias);

            String[] restoMenu = {"Divisas", "Historial"};
            int y = 350;
            for (String textoBtn : restoMenu) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setBackground(new Color(94, 116, 73)); 
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
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

            // Dimensiones para cuadrar las 3 columnas en la nueva resolución extendida
            int colWidth = 360;
            int colHeight = 45;
            int xCol1 = 40, xCol2 = 440, xCol3 = 840;

            // ------------------------------------------
            // BLOQUE 1: BANCOS DE ORIGEN
            // ------------------------------------------
            PanelFormularioRedondeado pOrigen = new PanelFormularioRedondeado();
            pOrigen.setBounds(40, 40, 1220, 160);
            pOrigen.setLayout(null);
            contenedor.add(pOrigen);

            JLabel lblOrigenTit = new JLabel("Bancos de Origen");
            lblOrigenTit.setForeground(Color.WHITE);
            lblOrigenTit.setFont(tituloSeccion);
            lblOrigenTit.setBounds(40, 15, 300, 22);
            pOrigen.add(lblOrigenTit);

            JLabel lblNumCuentaO = new JLabel("Número de Cuenta");
            lblNumCuentaO.setForeground(Color.WHITE);
            lblNumCuentaO.setFont(etiquetaCampos);
            lblNumCuentaO.setBounds(xCol1, 50, colWidth, 20);
            pOrigen.add(lblNumCuentaO);

            JLabel lblTipoCuentaO = new JLabel("Tipo de Cuenta");
            lblTipoCuentaO.setForeground(Color.WHITE);
            lblTipoCuentaO.setFont(etiquetaCampos);
            lblTipoCuentaO.setBounds(xCol2, 50, colWidth, 20);
            pOrigen.add(lblTipoCuentaO);

            JLabel lblSelBancoO = new JLabel("Seleccione su Banco");
            lblSelBancoO.setForeground(Color.WHITE);
            lblSelBancoO.setFont(etiquetaCampos);
            lblSelBancoO.setBounds(xCol3, 50, colWidth, 20);
            pOrigen.add(lblSelBancoO);

            JTextFieldRedondeado txtNumCuentaO = new JTextFieldRedondeado();
            txtNumCuentaO.setBounds(xCol1, 80, colWidth, colHeight);
            txtNumCuentaO.setFont(textoInputs);
            pOrigen.add(txtNumCuentaO);

            JTextFieldRedondeado txtTipoCuentaO = new JTextFieldRedondeado();
            txtTipoCuentaO.setBounds(xCol2, 80, colWidth, colHeight);
            txtTipoCuentaO.setFont(textoInputs);
            pOrigen.add(txtTipoCuentaO);

            JTextFieldRedondeado txtSelBancoO = new JTextFieldRedondeado();
            txtSelBancoO.setBounds(xCol3, 80, colWidth, colHeight);
            txtSelBancoO.setFont(textoInputs);
            pOrigen.add(txtSelBancoO);

            // ------------------------------------------
            // BLOQUE 2: BANCO DIRIGIDO
            // ------------------------------------------
            PanelFormularioRedondeado pDirigido = new PanelFormularioRedondeado();
            pDirigido.setBounds(40, 230, 1220, 160);
            pDirigido.setLayout(null);
            contenedor.add(pDirigido);

            JLabel lblDirigidoTit = new JLabel("Banco Dirigido");
            lblDirigidoTit.setForeground(Color.WHITE);
            lblDirigidoTit.setFont(tituloSeccion);
            lblDirigidoTit.setBounds(40, 15, 300, 22);
            pDirigido.add(lblDirigidoTit);

            JLabel lblNumCuentaD = new JLabel("Número de Cuenta");
            lblNumCuentaD.setForeground(Color.WHITE);
            lblNumCuentaD.setFont(etiquetaCampos);
            lblNumCuentaD.setBounds(xCol1, 50, colWidth, 20);
            pDirigido.add(lblNumCuentaD);

            JLabel lblTipoCuentaD = new JLabel("Tipo de Cuenta");
            lblTipoCuentaD.setForeground(Color.WHITE);
            lblTipoCuentaD.setFont(etiquetaCampos);
            lblTipoCuentaD.setBounds(xCol2, 50, colWidth, 20);
            pDirigido.add(lblTipoCuentaD);

            JLabel lblSelBancoD = new JLabel("Seleccione su Banco");
            lblSelBancoD.setForeground(Color.WHITE);
            lblSelBancoD.setFont(etiquetaCampos);
            lblSelBancoD.setBounds(xCol3, 50, colWidth, 20);
            pDirigido.add(lblSelBancoD);

            JTextFieldRedondeado txtNumCuentaD = new JTextFieldRedondeado();
            txtNumCuentaD.setBounds(xCol1, 80, colWidth, colHeight);
            txtNumCuentaD.setFont(textoInputs);
            pDirigido.add(txtNumCuentaD);

            JTextFieldRedondeado txtTipoCuentaD = new JTextFieldRedondeado();
            txtTipoCuentaD.setBounds(xCol2, 80, colWidth, colHeight);
            txtTipoCuentaD.setFont(textoInputs);
            pDirigido.add(txtTipoCuentaD);

            JTextFieldRedondeado txtSelBancoD = new JTextFieldRedondeado();
            txtSelBancoD.setBounds(xCol3, 80, colWidth, colHeight);
            txtSelBancoD.setFont(textoInputs);
            pDirigido.add(txtSelBancoD);

            // ------------------------------------------
            // BLOQUE 3: MONTO Y CONFIRMACIÓN
            // ------------------------------------------
            PanelFormularioRedondeado pConfirmacion = new PanelFormularioRedondeado();
            pConfirmacion.setBounds(40, 420, 800, 280);
            pConfirmacion.setLayout(null);
            contenedor.add(pConfirmacion);

            JLabel lblMonto = new JLabel("Ingrese el monto a transferir");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(tituloSeccion);
            lblMonto.setBounds(40, 25, 300, 22);
            pConfirmacion.add(lblMonto);

            JTextFieldRedondeado txtMonto = new JTextFieldRedondeado();
            txtMonto.setBounds(40, 60, 520, 45);
            txtMonto.setFont(textoInputs);
            pConfirmacion.add(txtMonto);

            JButton btnDivisasInline = new JButton("Divisas") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnDivisasInline.setBounds(590, 60, 170, 45);
            btnDivisasInline.setForeground(Color.WHITE);
            btnDivisasInline.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnDivisasInline.setFocusPainted(false);
            btnDivisasInline.setContentAreaFilled(false);
            btnDivisasInline.setBorderPainted(false);
            btnDivisasInline.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDivisasInline.addActionListener(e -> {
                new Divisas().setVisible(true);
                dispose();
            });
            pConfirmacion.add(btnDivisasInline);

            JLabel lblPassword = new JLabel("Confirme su contraseña");
            lblPassword.setForeground(Color.WHITE);
            lblPassword.setFont(tituloSeccion);
            lblPassword.setBounds(40, 140, 300, 22);
            pConfirmacion.add(lblPassword);

            JPasswordFieldRedondeado txtPassword = new JPasswordFieldRedondeado();
            txtPassword.setBounds(40, 175, 720, 45);
            pConfirmacion.add(txtPassword);

            // ==========================================
            // BOTÓN REALIZAR TRANSFERENCIA (Alineado estructuralmente)
            // ==========================================
            JButton btnTransferir = new JButton("<html><center>Realizar<br>Transferencia</center></html>") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnTransferir.setBounds(880, 520, 380, 80);
            btnTransferir.setBackground(new Color(251, 232, 138)); 
            btnTransferir.setForeground(Color.BLACK);
            btnTransferir.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnTransferir.setFocusPainted(false);
            btnTransferir.setContentAreaFilled(false);
            btnTransferir.setBorderPainted(false);
            btnTransferir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnTransferir.addActionListener(e -> {
                new ResumenTransferencia().setVisible(true);
                dispose();
            });
            contenedor.add(btnTransferir);
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

    class JPasswordFieldRedondeado extends JPasswordField {
        public JPasswordFieldRedondeado() {
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
            java.util.logging.Logger.getLogger(Transferencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new Transferencias().setVisible(true);
        });
    }
}