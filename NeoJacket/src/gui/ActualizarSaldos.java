package gui;

import java.awt.*;
import javax.swing.*;

public class ActualizarSaldos extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public ActualizarSaldos() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Actualizar Saldos");
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
                    if (textoBtn.equals("Bancos conectados")) {
                        new BancosConectados().setVisible(true);
                    } else if (textoBtn.equals("Transferencias")) {
                        new Transferencias().setVisible(true);
                    } else if (textoBtn.equals("Divisas")) {
                        new Divisas().setVisible(true);
                    } else if (textoBtn.equals("Historial")) {
                        new Historial().setVisible(true);
                    }
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

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 434, 55);
            btnTab2.setBackground(new Color(251, 232, 138, 200)); 
            btnTab2.setForeground(Color.BLACK);
            btnTab2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab2.setFocusPainted(false);
            btnTab2.setBorder(BorderFactory.createEmptyBorder());
            barraSuperior.add(btnTab2);

            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 867);
            btnTab3.addActionListener(e -> {
                new Saldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab3);

            PanelFormularioRedondeado panelForm = new PanelFormularioRedondeado();
            panelForm.setBounds(240, 100, 820, 520);
            panelForm.setLayout(null);
            contenedor.add(panelForm);

            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(25, 20, 770, 25);
            panelForm.add(lblBanco);

            String[] opcionesBancos = {" -- Selecciona una entidad financiera -- ", "Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(25, 50, 770, 45);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(Color.WHITE);
            panelForm.add(cbBancos);

            JLabel lblSaldoDisp = new JLabel("Saldo Disponible");
            lblSaldoDisp.setForeground(Color.WHITE);
            lblSaldoDisp.setFont(tituloCampos);
            lblSaldoDisp.setBounds(25, 115, 770, 25);
            panelForm.add(lblSaldoDisp);

            JTextFieldRedondeado txtSaldoDisp = new JTextFieldRedondeado();
            txtSaldoDisp.setBounds(25, 145, 770, 45);
            txtSaldoDisp.setFont(textoInputs);
            txtSaldoDisp.setEditable(false);
            panelForm.add(txtSaldoDisp);

            JLabel lblMontoGastado = new JLabel("Monto gastado");
            lblMontoGastado.setForeground(Color.WHITE);
            lblMontoGastado.setFont(tituloCampos);
            lblMontoGastado.setBounds(25, 210, 365, 25);
            panelForm.add(lblMontoGastado);

            JTextFieldRedondeado txtMontoGastado = new JTextFieldRedondeado();
            txtMontoGastado.setBounds(25, 240, 365, 45);
            txtMontoGastado.setFont(textoInputs);
            panelForm.add(txtMontoGastado);

            JLabel lblNuevoSaldo = new JLabel("Nuevo saldo después del gasto");
            lblNuevoSaldo.setForeground(Color.WHITE);
            lblNuevoSaldo.setFont(tituloCampos);
            lblNuevoSaldo.setBounds(430, 210, 365, 25);
            panelForm.add(lblNuevoSaldo);

            JTextFieldRedondeado txtNuevoSaldo = new JTextFieldRedondeado();
            txtNuevoSaldo.setBounds(430, 240, 365, 45);
            txtNuevoSaldo.setFont(textoInputs);
            panelForm.add(txtNuevoSaldo);

            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(25, 305, 770, 25);
            panelForm.add(lblDescripcion);

            JTextFieldRedondeado txtDescripcion = new JTextFieldRedondeado();
            txtDescripcion.setBounds(25, 335, 770, 45);
            txtDescripcion.setFont(textoInputs);
            panelForm.add(txtDescripcion);

            JButton btnGuardar = new JButton("Guardar") {
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
            btnGuardar.setBounds(25, 440, 770, 50);
            btnGuardar.setBackground(new Color(251, 232, 138)); 
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnGuardar.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "¡Saldo actualizado correctamente!");
            });
            panelForm.add(btnGuardar);
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
            java.util.logging.Logger.getLogger(ActualizarSaldos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new ActualizarSaldos().setVisible(true);
        });
    }
}
