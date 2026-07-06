package gui;

import java.awt.*;
import javax.swing.*;
import funcionalidades.SupervisionDAO;

public class ResumenTransferencia extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

    Font tituloPanel = new Font("Segoe UI", Font.BOLD, 16);
    Font etiquetaCampos = new Font("Segoe UI", Font.PLAIN, 13);
    Font textoInputs = new Font("Segoe UI", Font.BOLD, 13);

    private Integer idMenor;
    private double monto;

    public ResumenTransferencia() {
        this(null, 0.0);
    }

    public ResumenTransferencia(Integer idMenor, double monto) {
        this.idMenor = idMenor;
        this.monto   = monto;
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Resumen de Transferencia");
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

            String[] nombresMenu = {"Saldos", "Bancos conectados", "Transferencias", "Divisas", "Historial"};
            int y = 140;
            for (String nombre : nombresMenu) {
                JButton btn = new JButton(nombre);
                btn.setBounds(20, y, 250, 55);
                
                // Transferencias se mantiene activo (Amarillo corporativo)
                if (nombre.equals("Transferencias")) {
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
                    else if (nombre.equals("Transferencias")) { new Transferencias(idMenor).setVisible(true); dispose(); }
                    else if (nombre.equals("Bancos conectados")) { new BancosConectados().setVisible(true); dispose(); }
                    else if (nombre.equals("Divisas")) { new Divisas(idMenor).setVisible(true); dispose(); }
                    else if (nombre.equals("Historial")) { new Historial(idMenor).setVisible(true); dispose(); }
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

            // Título superior de la sección
            JLabel lblHeader = new JLabel("Resumen de Transferencia");
            lblHeader.setForeground(Color.WHITE);
            lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblHeader.setBounds(50, 25, 400, 30);
            contenedor.add(lblHeader);

            // Dimensión y posicionamiento simétrico de las dos tarjetas de datos
            int panelWidth = 580;
            int panelHeight = 520;
            int xOrigen = 50;
            int xDirigido = 670;

            // ------------------------------------------
            // PANEL IZQUIERDO: BANCOS DE ORIGEN
            // ------------------------------------------
            PanelRedondeado pOrigen = new PanelRedondeado();
            pOrigen.setBounds(xOrigen, 80, panelWidth, panelHeight);
            pOrigen.setLayout(null);
            contenedor.add(pOrigen);

            JLabel lblOtit = new JLabel("Bancos de Origen");
            lblOtit.setForeground(Color.WHITE);
            lblOtit.setFont(tituloPanel);
            lblOtit.setBounds(40, 20, 200, 25);
            pOrigen.add(lblOtit);

            String[] etiquetasO = {"Número de Cuenta", "Nombre", "Tipo de Cuenta", "Banco", "Monto Enviado"};
            String[] datosO = {"012-345678-9", "Juan Alberto Pérez", "Monetaria", "Banco Industrial", "Q. 1,500.00"};
            
            int yOffset = 65;
            for (int i = 0; i < etiquetasO.length; i++) {
                JLabel lbl = new JLabel(etiquetasO[i]);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(etiquetaCampos);
                lbl.setBounds(40, yOffset, 300, 20);
                pOrigen.add(lbl);

                JTextFieldRedondeado txt = new JTextFieldRedondeado();
                txt.setBounds(40, yOffset + 25, 500, 40);
                txt.setText(datosO[i]);
                txt.setFont(textoInputs);
                txt.setEditable(false);
                pOrigen.add(txt);
                yOffset += 85;
            }

            // ------------------------------------------
            // PANEL DERECHO: BANCOS DIRIGIDO
            // ------------------------------------------
            PanelRedondeado pDirigido = new PanelRedondeado();
            pDirigido.setBounds(xDirigido, 80, panelWidth, panelHeight);
            pDirigido.setLayout(null);
            contenedor.add(pDirigido);

            JLabel lblDtit = new JLabel("Banco Dirigido");
            lblDtit.setForeground(Color.WHITE);
            lblDtit.setFont(tituloPanel);
            lblDtit.setBounds(40, 20, 200, 25);
            pDirigido.add(lblDtit);

            String[] etiquetasD = {"Número de Cuenta", "Nombre", "Tipo de Cuenta", "Monto Recibido"};
            String[] datosD = {"987-654321-0", "María del Carmen López", "Ahorro", "Q. 1,500.00"};

            yOffset = 65;
            for (int i = 0; i < etiquetasD.length; i++) {
                JLabel lbl = new JLabel(etiquetasD[i]);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(etiquetaCampos);
                lbl.setBounds(40, yOffset, 300, 20);
                pDirigido.add(lbl);

                JTextFieldRedondeado txt = new JTextFieldRedondeado();
                txt.setBounds(40, yOffset + 25, 500, 40);
                txt.setText(datosD[i]);
                txt.setFont(textoInputs);
                txt.setEditable(false);
                pDirigido.add(txt);
                yOffset += 85;
            }

            // ==========================================
            // BOTONES DE ACCIÓN (Inferiores alineados con los paneles)
            // ==========================================
            
            // Botón Regresar (Alineado al panel izquierdo)
            JButton btnRegresar = new JButton("Regresar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(94, 116, 73)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnRegresar.setBounds(50, 630, 220, 55);
            btnRegresar.setForeground(Color.WHITE);
            btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnRegresar.setFocusPainted(false);
            btnRegresar.setContentAreaFilled(false);
            btnRegresar.setBorderPainted(false);
            btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRegresar.addActionListener(e -> {
                new Transferencias(idMenor).setVisible(true);
                dispose();
            });
            contenedor.add(btnRegresar);

            // Botón Aceptar (Alineado al extremo derecho del panel dirigido)
            JButton btnAceptar = new JButton("Aceptar") {
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
            btnAceptar.setBounds(1030, 630, 220, 55);
            btnAceptar.setBackground(new Color(251, 232, 138)); 
            btnAceptar.setForeground(Color.BLACK);
            btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnAceptar.setFocusPainted(false);
            btnAceptar.setContentAreaFilled(false);
            btnAceptar.setBorderPainted(false);
            btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAceptar.addActionListener(e -> {
                // Segunda validación de límite antes de confirmar
                if (idMenor != null && monto > 0) {
                    SupervisionDAO dao = new SupervisionDAO();
                    String errorLimite = dao.validarLimite(idMenor, monto);
                    if (errorLimite != null) {
                        JOptionPane.showMessageDialog(this,
                            errorLimite,
                            "Límite de gasto excedido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "¡Transferencia realizada con éxito!");
                new Transferencias(idMenor).setVisible(true);
                dispose();
            });
            contenedor.add(btnAceptar);
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
            java.util.logging.Logger.getLogger(ResumenTransferencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new ResumenTransferencia().setVisible(true);
        });
    }
}