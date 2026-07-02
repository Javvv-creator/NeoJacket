package gui;

import funcionalidades.SesionUsuario;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.CRUD.CRUD;
import main.Conexion.conexion;

public class ActualizarSaldos extends JFrame {

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
            crearSidebar(this);
            crearContenido();
        }
      private void crearSidebar(JPanel panel) {

    JPanel sidebar = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(25, 38, 35, 220));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            g2.dispose();
        }
    };

    sidebar.setOpaque(false);
    sidebar.setBounds(20, 20, 300, 950);
    sidebar.setLayout(null);

    // Logo
    Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
    JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
    lblLogo.setBounds(20, 10, 250, 110);
    sidebar.add(lblLogo);

    String[] opciones = {
        "Saldos",
        "Bancos Conectados",
        "Transferencias",
        "Divisas",
        "Historial"
    };

    int y = 140;

    for (String texto : opciones) {

        JButton btn = new JButton(texto);

        btn.setBounds(20, y, 250, 50);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(25,38,35));
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(251,232,138));
                btn.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(25,38,35));
                btn.setForeground(Color.WHITE);
            }

        });
// Enrutador de acciones para la navegación lateral
                if (texto.equals("Saldos")) {
                    btn.addActionListener(e -> { 
                        new Saldos().setVisible(true);
                        dispose(); 
                    });
                }
                if (texto.equals("Bancos Conectados")) {
                    btn.addActionListener(e -> { 
                        new BancosConectados().setVisible(true);
                        dispose(); 
                    });
                }
                if (texto.equals("Transferencias")) {
                    btn.addActionListener(e -> { 
                        new Transferencias().setVisible(true);
                        dispose(); 
                    });
                }
                if (texto.equals("Divisas")) {
                    btn.addActionListener(e -> { 
                        new Divisas().setVisible(true);
                        dispose(); 
                    });
                }
                if (texto.equals("Historial")) {
                    btn.addActionListener(e -> { 
                        new Historial().setVisible(true);
                        dispose(); 
                    });
                }
                
                 JButton btnCerrarSesion = new JButton("Cerrar sesión");
            btnCerrarSesion.setBounds(20, 880, 250, 55);
            btnCerrarSesion.setFocusPainted(false);
            btnCerrarSesion.setBorderPainted(false);
            btnCerrarSesion.setBackground(new Color(191, 76, 58));
            btnCerrarSesion.setForeground(Color.WHITE);
            btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnCerrarSesion.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

        sidebar.add(btn);
        y += 60;
    }

    // ESTA ES LA ÚNICA LÍNEA QUE DEBE EXISTIR
    panel.add(sidebar);
}

       // -------------------------
        // Contenido principal
        // -------------------------
        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // Barra superior
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(94, 116, 73, 200));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);
 
            JButton btnTab1 = crearBotonPestaña("Agregar Fondos", 0);
            btnTab1.addActionListener(e -> { new AgregarFondos().setVisible(true); dispose(); });
            barraSuperior.add(btnTab1);

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 434, 55);
            btnTab2.setBackground(new Color(251, 232, 138, 200));
            btnTab2.setForeground(Color.BLACK);
            barraSuperior.add(btnTab2);

            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 867);
            btnTab3.addActionListener(e -> { new ConsultarSaldos().setVisible(true); dispose(); });
            barraSuperior.add(btnTab3);

            // Panel formulario
            PanelFormularioRedondeado panelForm = new PanelFormularioRedondeado();
            panelForm.setBounds(240, 100, 820, 520);
            panelForm.setLayout(null);
            contenedor.add(panelForm);

            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(25, 20, 770, 25);
            panelForm.add(lblBanco);

            JComboBox<String> cbBancos = new JComboBox<>(new String[]{
                "Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"
            });
            cbBancos.setBounds(25, 50, 770, 45);
            cbBancos.setFont(textoInputs);
            panelForm.add(cbBancos);

            JLabel lblMontoGastado = new JLabel("Monto gastado");
            lblMontoGastado.setForeground(Color.WHITE);
            lblMontoGastado.setFont(tituloCampos);
            lblMontoGastado.setBounds(25, 115, 365, 25);
            panelForm.add(lblMontoGastado);

            JTextFieldRedondeado txtMontoGastado = new JTextFieldRedondeado();
            txtMontoGastado.setBounds(25, 145, 365, 45);
            txtMontoGastado.setFont(textoInputs);
            panelForm.add(txtMontoGastado);

            JLabel lblNuevoSaldo = new JLabel("Nuevo saldo después del gasto");
            lblNuevoSaldo.setForeground(Color.WHITE);
            lblNuevoSaldo.setFont(tituloCampos);
            lblNuevoSaldo.setBounds(430, 115, 365, 25);
            panelForm.add(lblNuevoSaldo);

            JTextFieldRedondeado txtNuevoSaldo = new JTextFieldRedondeado();
            txtNuevoSaldo.setBounds(430, 145, 365, 45);
            txtNuevoSaldo.setFont(textoInputs);
            panelForm.add(txtNuevoSaldo);

            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(25, 210, 770, 25);
            panelForm.add(lblDescripcion);

            JTextFieldRedondeado txtDescripcion = new JTextFieldRedondeado();
            txtDescripcion.setBounds(25, 240, 770, 45);
            txtDescripcion.setFont(textoInputs);
            panelForm.add(txtDescripcion);

            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.setBounds(25, 320, 770, 50);
            btnGuardar.setBackground(new Color(251, 232, 138));
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            panelForm.add(btnGuardar);

            // Acción Guardar
            btnGuardar.addActionListener(e -> {
                try {
                    CRUD crud = new CRUD();

                    int idUsuario = SesionUsuario.getIdUsuario();
                    String bancoSeleccionado = (String) cbBancos.getSelectedItem();
                    double montoActual = Double.parseDouble(txtMontoGastado.getText());
                    double nuevoMonto = Double.parseDouble(txtNuevoSaldo.getText());
                    String motivo = txtDescripcion.getText();

                    Connection con = conexion.getConexion();
                    PreparedStatement psBanco = con.prepareStatement("SELECT id_banco FROM bancos WHERE nombre = ?");
                    psBanco.setString(1, bancoSeleccionado);
                    ResultSet rsBanco = psBanco.executeQuery();

                    if (rsBanco.next()) {
                        int idBanco = rsBanco.getInt("id_banco");

                        boolean ok = crud.actualizarSaldo(idUsuario, idBanco, montoActual, nuevoMonto, motivo);
                        if (ok) {
                            JOptionPane.showMessageDialog(this, "Saldo actualizado con éxito");
                        }
                    }

                    rsBanco.close();
                    psBanco.close();
                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
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
