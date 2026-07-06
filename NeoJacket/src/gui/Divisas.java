package gui;

import java.awt.*;
import javax.swing.*;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import funcionalidades.API;

public class Divisas extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

    Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 18);
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    private Integer idMenor;
    private javax.swing.table.DefaultTableModel modeloTabla;

    public Divisas() {
        this(null);
    }

    public Divisas(Integer idMenor) {
        this.idMenor = idMenor;
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Divisas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new FondoPanel());
        cargarTasas();
    }

    private void cargarTasas() {
        if (modeloTabla == null) return;
        modeloTabla.setRowCount(0);
        modeloTabla.addRow(new Object[]{"Cargando...", "Por favor espere...", ""});
        new Thread(() -> {
            try {
                String[] monedas = {"USD", "EUR", "MXN", "COP", "ARS", "GBP", "BRL"};
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                Map<String, String> tasas = API.obtenerTasas("GTQ", monedas);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    modeloTabla.setRowCount(0);
                    for (String moneda : monedas) {
                        modeloTabla.addRow(new Object[]{
                            "GTQ → " + moneda,
                            tasas.getOrDefault(moneda, "N/A"),
                            fecha
                        });
                    }
                });
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    modeloTabla.setRowCount(0);
                    modeloTabla.addRow(new Object[]{"Error", e.getMessage(), ""});
                });
            }
        }).start();
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
                    if (nombre.equals("Transferencias")) { new Transferencias(idMenor).setVisible(true); dispose(); }
                    if (nombre.equals("Bancos conectados")) { new BancosConectados().setVisible(true); dispose(); }
                    if (nombre.equals("Historial")) { new Historial(idMenor).setVisible(true); dispose(); }
                });

                sidebar.add(btn);
                y += 70;
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

            add(sidebar);
        }

        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            JLabel lblHeader = new JLabel("Divisas");
            lblHeader.setForeground(Color.WHITE);
            lblHeader.setFont(tituloSeccion);
            lblHeader.setBounds(60, 20, 200, 30);
            contenedor.add(lblHeader);

            JLabel lblDesc = new JLabel("Tipos de cambio en tiempo real — GTQ como base");
            lblDesc.setForeground(new Color(200, 210, 200));
            lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDesc.setBounds(60, 52, 600, 20);
            contenedor.add(lblDesc);

            // ── TABLA DE TASAS ────────────────────────────────────────────────────
            PanelRedondeado pTabla = new PanelRedondeado();
            pTabla.setBounds(40, 90, 1220, 380);
            pTabla.setLayout(null);
            contenedor.add(pTabla);

            JLabel lblTitTabla = new JLabel("Tasas de cambio actuales");
            lblTitTabla.setForeground(new Color(251, 232, 138));
            lblTitTabla.setFont(tituloCampos);
            lblTitTabla.setBounds(20, 15, 300, 22);
            pTabla.add(lblTitTabla);

            String[] columnas = {"Par de divisas", "Tasa (1 GTQ)", "Última actualización"};
            modeloTabla = new javax.swing.table.DefaultTableModel(columnas, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            javax.swing.JTable tabla = new javax.swing.JTable(modeloTabla);
            tabla.setBackground(new Color(20, 32, 29));
            tabla.setForeground(Color.WHITE);
            tabla.setFont(textoInputs);
            tabla.setRowHeight(38);
            tabla.setShowGrid(false);
            tabla.setIntercellSpacing(new Dimension(0, 0));
            tabla.getTableHeader().setBackground(new Color(94, 116, 73));
            tabla.getTableHeader().setForeground(Color.WHITE);
            tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            tabla.setSelectionBackground(new Color(251, 232, 138, 80));
            tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(javax.swing.JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    setForeground(Color.WHITE);
                    setBackground(sel ? new Color(94, 116, 73, 150)
                        : row % 2 == 0 ? new Color(20, 32, 29) : new Color(30, 46, 42));
                    setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                    return this;
                }
            });

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBounds(20, 48, 1180, 295);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 60), 1));
            pTabla.add(scroll);

            JButton btnRefrescar = new JButton("⟳  Refrescar") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover() ? new Color(251, 232, 138) : new Color(94, 116, 73));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnRefrescar.setBounds(1060, 350, 140, 22);
            btnRefrescar.setForeground(Color.WHITE);
            btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnRefrescar.setFocusPainted(false);
            btnRefrescar.setContentAreaFilled(false);
            btnRefrescar.setBorderPainted(false);
            btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRefrescar.addActionListener(e -> cargarTasas());
            pTabla.add(btnRefrescar);

            // ── CALCULADORA ───────────────────────────────────────────────────────
            PanelRedondeado pCalc = new PanelRedondeado();
            pCalc.setBounds(40, 490, 1220, 220);
            pCalc.setLayout(null);
            contenedor.add(pCalc);

            JLabel lblCalc = new JLabel("Calculadora rápida");
            lblCalc.setForeground(new Color(251, 232, 138));
            lblCalc.setFont(tituloCampos);
            lblCalc.setBounds(20, 15, 300, 22);
            pCalc.add(lblCalc);

            JLabel lblMonto = new JLabel("Monto en GTQ:");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(textoInputs);
            lblMonto.setBounds(20, 55, 140, 20);
            pCalc.add(lblMonto);

            JTextFieldRedondeado txtMonto = new JTextFieldRedondeado();
            txtMonto.setBounds(165, 48, 220, 38);
            pCalc.add(txtMonto);

            JLabel lblMoneda = new JLabel("Divisa destino:");
            lblMoneda.setForeground(Color.WHITE);
            lblMoneda.setFont(textoInputs);
            lblMoneda.setBounds(420, 55, 130, 20);
            pCalc.add(lblMoneda);

            String[] opcionesMoneda = {"USD", "EUR", "MXN", "COP", "ARS", "GBP", "BRL"};
            JComboBox<String> cmbMoneda = new JComboBox<>(opcionesMoneda);
            cmbMoneda.setBounds(555, 48, 120, 38);
            cmbMoneda.setBackground(new Color(20, 32, 29));
            cmbMoneda.setForeground(Color.WHITE);
            cmbMoneda.setFont(textoInputs);
            cmbMoneda.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1));
            pCalc.add(cmbMoneda);

            JLabel lblResultado = new JLabel("Resultado: —");
            lblResultado.setForeground(Color.WHITE);
            lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblResultado.setBounds(20, 140, 900, 28);
            pCalc.add(lblResultado);

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
            btnConvertir.setBounds(710, 43, 200, 50);
            btnConvertir.setBackground(new Color(251, 232, 138));
            btnConvertir.setForeground(Color.BLACK);
            btnConvertir.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnConvertir.setFocusPainted(false);
            btnConvertir.setContentAreaFilled(false);
            btnConvertir.setBorderPainted(false);
            btnConvertir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnConvertir.addActionListener(e -> {
                try {
                    double monto = Double.parseDouble(txtMonto.getText().trim().replace(",", "."));
                    String monedaSel = (String) cmbMoneda.getSelectedItem();
                    for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                        String par = (String) modeloTabla.getValueAt(i, 0);
                        if (par != null && par.contains(monedaSel)) {
                            double tasa = Double.parseDouble((String) modeloTabla.getValueAt(i, 1));
                            lblResultado.setText(String.format(
                                "Q %.2f GTQ  =  %.4f %s", monto, monto * tasa, monedaSel));
                            return;
                        }
                    }
                    lblResultado.setText("Tasa no disponible. Recarga primero.");
                } catch (NumberFormatException ex) {
                    lblResultado.setText("Ingresa un monto numérico válido.");
                }
            });
            pCalc.add(btnConvertir);
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