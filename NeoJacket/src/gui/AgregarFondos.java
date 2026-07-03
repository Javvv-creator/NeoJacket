package gui;

import funcionalidades.SesionUsuario;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.CRUD.CRUD;
import main.Conexion.conexion;

public class AgregarFondos extends JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public AgregarFondos() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Agregar Fondos");
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
            
            Color amarillo = new Color(251, 232, 138);
            Color fondoTransparente = new Color(0, 0, 0, 0); 
            Color amarilloBorde = new Color(251, 232, 138);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] opciones = {
                "Saldos",
                "Bancos Conectados",
                "Transferencias",
                "Historial"
            };

            int y = 140;

            for (String texto : opciones) {
                JButton btn = new JButton(texto) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                        
                        if (getBackground() != amarillo) {
                            g2.setColor(amarilloBorde);
                            g2.setStroke(new BasicStroke(1f));
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                        }
                        
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };

                btn.setBounds(20, y, 250, 46);
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false); 
                btn.setBorderPainted(false);     
                btn.setOpaque(false);
                btn.setForeground(Color.WHITE);
                btn.setBackground(fondoTransparente);

                Font fuenteActual = btn.getFont();
                btn.setFont(new Font(fuenteActual.getName(), fuenteActual.getStyle(), fuenteActual.getSize() + 2));

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        btn.setBackground(amarillo);
                        btn.setForeground(Color.BLACK);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        btn.setBackground(fondoTransparente);
                        btn.setForeground(Color.WHITE);
                    }
                });

                if (texto.equals("Saldos")) {
                    btn.addActionListener(e -> { new Saldos().setVisible(true); dispose(); });
                }
                if (texto.equals("Bancos Conectados")) {
                    btn.addActionListener(e -> { new BancosConectados().setVisible(true); dispose(); });
                }
                if (texto.equals("Transferencias")) {
                    btn.addActionListener(e -> { new Transferencias().setVisible(true); dispose(); });
                }
                if (texto.equals("Historial")) {
                    btn.addActionListener(e -> { new Historial().setVisible(true); dispose(); });
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
                y += 68;
            }

            panel.add(sidebar);
        }

        private void crearContenido() {
            // Contenedor principal con el color e intensidad exacta de tu Sidebar
            JPanel contenedor = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 220)); 
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            contenedor.setLayout(null);
            contenedor.setOpaque(false);
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // Barra superior integrada con paleta oscura uniforme
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(23, 32, 29));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            // Pestaña Activa con Estilo Destacado
            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 434, 55);
            btnTab1.setBackground(new Color(251, 232, 138));
            btnTab1.setForeground(Color.BLACK);
            btnTab1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab1.setFocusPainted(false);
            btnTab1.setBorder(BorderFactory.createEmptyBorder());
            barraSuperior.add(btnTab1);

            JButton btnTab2 = crearBotonPestaña("Actualizar Saldos", 433);
            btnTab2.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab2);

            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 866);
            btnTab3.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab3);

            // Carta contenedora del Formulario con Marco Delgado (1f)
            JPanel cartaCampos = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(20, 28, 25, 120));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(251, 232, 138, 80)); // Línea del marco delgada
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    g2.dispose();
                }
            };
            cartaCampos.setLayout(null);
            cartaCampos.setOpaque(false);
            cartaCampos.setBounds(425, 110, 450, 550); // Centrado perfectamente
            contenedor.add(cartaCampos);

            // Formulario y Componentes del panel (Centrados simétricamente)
            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(35, 25, 380, 25);
            cartaCampos.add(lblBanco);

            String[] opcionesBancos = {"Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(35, 55, 380, 45);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(13, 18, 16));
            cbBancos.setForeground(Color.WHITE);
            cbBancos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1));
            
            // Renderizador personalizado para que las opciones se tornen amarillas en hover
            cbBancos.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    if (isSelected) {
                        label.setBackground(new Color(251, 232, 138));
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(new Color(13, 18, 16));
                        label.setForeground(Color.WHITE);
                    }
                    return label;
                }
            });
            cartaCampos.add(cbBancos);

            JLabel lblMonto = new JLabel("Monto a ingresar");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(tituloCampos);
            lblMonto.setBounds(35, 125, 380, 25);
            cartaCampos.add(lblMonto);

            JTextFieldBordeAmarillo txtMonto = new JTextFieldBordeAmarillo();
            txtMonto.setBounds(35, 155, 380, 45);
            txtMonto.setFont(textoInputs);
            cartaCampos.add(txtMonto);

            JLabel lblTarjeta = new JLabel("Número de tarjeta");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(35, 225, 380, 25);
            cartaCampos.add(lblTarjeta);

            JTextFieldBordeAmarillo txtTarjeta = new JTextFieldBordeAmarillo();
            txtTarjeta.setBounds(35, 255, 380, 45);
            txtTarjeta.setFont(textoInputs);
            cartaCampos.add(txtTarjeta);

            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(35, 325, 380, 25);
            cartaCampos.add(lblDescripcion);

            JTextFieldBordeAmarillo txtDescripcion = new JTextFieldBordeAmarillo();
            txtDescripcion.setBounds(35, 355, 380, 45);
            txtDescripcion.setFont(textoInputs);
            cartaCampos.add(txtDescripcion);

            // Botón guardar interactivo
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
            btnGuardar.setBounds(35, 450, 380, 50);
            btnGuardar.setBackground(new Color(251, 232, 138));
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) { btnGuardar.setBackground(new Color(255, 245, 180)); }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) { btnGuardar.setBackground(new Color(251, 232, 138)); }
            });
            cartaCampos.add(btnGuardar);

            // Lógica intacta de base de datos
            btnGuardar.addActionListener(e -> {
                try {
                    CRUD crud = new CRUD();
                    String montoTexto = txtMonto.getText().trim();
                    if (montoTexto.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Debes ingresar un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double monto;
                    try {
                        monto = Double.parseDouble(montoTexto);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int idUsuario = SesionUsuario.getIdUsuario(); 
                    String bancoSeleccionado = (String) cbBancos.getSelectedItem();
                    String descripcion = txtDescripcion.getText();

                    Connection con = conexion.getConexion();
                    PreparedStatement psBanco = con.prepareStatement("SELECT id_banco FROM bancos WHERE nombre = ?");
                    psBanco.setString(1, bancoSeleccionado);
                    ResultSet rsBanco = psBanco.executeQuery();

                    if (rsBanco.next()) {
                        int idBanco = rsBanco.getInt("id_banco");
                        
                        System.out.println("Monto: " + monto);
                        System.out.println("Usuario: " + idUsuario);
                        System.out.println("Banco: " + bancoSeleccionado);
                        System.out.println("Descripción: " + descripcion);
                        System.out.println("Usuario en sesión: " + idUsuario);

                        boolean ok = crud.agregarFondos(idUsuario, idBanco, monto, descripcion);
                        if (ok) {
                            JOptionPane.showMessageDialog(this, "Fondo agregado con éxito");
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
            btn.setBackground(new Color(16, 22, 20));
            btn.setForeground(new Color(150, 150, 150));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) { btn.setForeground(Color.WHITE); }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) { btn.setForeground(new Color(150, 150, 150)); }
            });
            return btn;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class JTextFieldBordeAmarillo extends JTextField {
        public JTextFieldBordeAmarillo() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(13, 18, 16));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            g2.setColor(new Color(251, 232, 138, 130)); // Línea delgada para los inputs
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AgregarFondos().setVisible(true);
        });
    }
}