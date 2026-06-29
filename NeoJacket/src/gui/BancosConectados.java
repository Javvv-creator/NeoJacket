package gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class BancosConectados extends JFrame {

    private Image fondo;
    private Image logo;

    public BancosConectados() {

    fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
    logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

    setTitle("Neo Jacket - Bancos Conectados");
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    setContentPane(new FondoPanel());

    setVisible(true);
}

    // Panel de fondo
    class FondoPanel extends JPanel {
        public FondoPanel() {
            setLayout(null);
            crearSidebar(this);
            crearContenido(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Sidebar vertical
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
                if (texto.equals("Transferencias")) {
                    btn.addActionListener(e -> { 
                        new Transferencias().setVisible(true);
                        dispose(); 
                    });
                }
                if (texto.equals("Divisas")) {
                    btn.addActionListener(e -> { 
                        new GestionDivisas().setVisible(true);
                        dispose(); 
                    });
                }
                if (texto.equals("Historial")) {
                    btn.addActionListener(e -> { 
                        new Historial().setVisible(true);
                        dispose(); 
                    });
                }

        sidebar.add(btn);
        y += 60;
    }

    // ESTA ES LA ÚNICA LÍNEA QUE DEBE EXISTIR
    panel.add(sidebar);
}   

    // Contenido central
    private void crearContenido(JPanel panel) {
        // Grupo para que solo se seleccione un banco
        ButtonGroup grupoBancos = new ButtonGroup();
        
        

        // Panel Banco Industrial
        JPanel panelIndustrial = crearCard(350, 80, 200, 150);
        panelIndustrial.setLayout(null);
        ImageIcon iconIndustrial = new ImageIcon(getClass().getResource("/gui/image/banco_industrial.png"));
        JRadioButton btnIndustrial = new JRadioButton(iconIndustrial);
        btnIndustrial.setBounds(15, 20, 170, 100);
        btnIndustrial.setBackground(new Color(25, 38, 35));
        grupoBancos.add(btnIndustrial);
        panelIndustrial.add(btnIndustrial);
        panel.add(panelIndustrial);

        // Panel Banrural
        JPanel panelBanrural = crearCard(600, 80, 200, 150);
        panelBanrural.setLayout(null);
        ImageIcon iconBanrural = new ImageIcon(getClass().getResource("/gui/image/banrural.png"));
        JRadioButton btnBanrural = new JRadioButton(iconBanrural);
        btnBanrural.setBounds(15, 20, 170, 100);
        btnBanrural.setBackground(new Color(25, 38, 35));
        grupoBancos.add(btnBanrural);
        panel.add(panelBanrural);
        panel.add(panelBanrural);

        // Panel BAC Credomatic
        JPanel panelBAC = crearCard(850, 80, 200, 150);
        panelBAC.setLayout(null);
        ImageIcon iconBAC = new ImageIcon(getClass().getResource("/gui/image/bac.png"));
        JRadioButton btnBAC = new JRadioButton(iconBAC);
        btnBAC.setBounds(15, 20, 170, 100);
        btnBAC.setBackground(new Color(25, 38, 35));
        grupoBancos.add(btnBAC);
        panelBAC.add(btnBAC);
        panel.add(panelBAC);

        // Panel G&T Continental
        JPanel panelGYT = crearCard(1100, 80, 200, 150);
        panelGYT.setLayout(null);
        ImageIcon iconGYT = new ImageIcon(getClass().getResource("/gui/image/gyt.png"));
        JRadioButton btnGYT = new JRadioButton(iconGYT);
        btnGYT.setBounds(15, 20, 170, 100);
        btnGYT.setBackground(new Color(25, 38, 35));
        grupoBancos.add(btnGYT);
        panelGYT.add(btnGYT);
        panel.add(panelGYT);

        // Panel de "Saldo Disponible"
        JPanel panelSaldo = crearCard(350, 260, 400, 150);
        panelSaldo.setLayout(null);
        JLabel lblSaldoTitulo = new JLabel("Saldo Disponible:");
        lblSaldoTitulo.setForeground(Color.WHITE);
        lblSaldoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSaldoTitulo.setBounds(20, 20, 200, 30);
        panelSaldo.add(lblSaldoTitulo);
        JLabel lblSaldoValor = new JLabel("Q. 500.00");
        lblSaldoValor.setForeground(new Color(251, 232, 138));
        lblSaldoValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblSaldoValor.setBounds(20, 60, 300, 40);
        panelSaldo.add(lblSaldoValor);
        panel.add(panelSaldo);

        // Panel de "Nombre del Banco"
        JPanel panelBanco = crearCard(800, 260, 400, 150);
        panelBanco.setLayout(null);
        JLabel lblBancoTitulo = new JLabel("Nombre del Banco:");
        lblBancoTitulo.setForeground(Color.WHITE);
        lblBancoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBancoTitulo.setBounds(20, 20, 250, 30);
        panelBanco.add(lblBancoTitulo);
        JLabel lblBancoValor = new JLabel("Banco Banrural");
        lblBancoValor.setForeground(new Color(251, 232, 138));
        lblBancoValor.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBancoValor.setBounds(20, 60, 300, 30);
        panelBanco.add(lblBancoValor);
        panel.add(panelBanco);

        // Panel de tabla de movimientos
        JPanel panelTabla = crearCard(350, 440, 850, 400);
        panelTabla.setLayout(new BorderLayout());
        String[] columnas = {"Fecha", "Tipo", "Monto"};
        Object[][] datos = {
            {"27/06/2026", "Depósito", "Q. 200.00"},
            {"28/06/2026", "Retiro", "Q. 100.00"}
        };
        JTable tabla = new JTable(datos, columnas);
        tabla.setBackground(new Color(25, 38, 35));
        tabla.setForeground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2));
        panelTabla.add(scroll, BorderLayout.CENTER);

        panel.add(panelTabla);
        
        
    }

    // Método para crear paneles elegantes con bordes redondeados

    private JPanel crearCard(int x, int y, int w, int h) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo oscuro translúcido
                g2.setColor(new Color(25, 38, 35, 210));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));

                // Borde amarillo
                g2.setColor(new Color(251, 232, 138));
                g2.setStroke(new BasicStroke(2));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 30, 30));

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setBounds(x, y, w, h);
        return panel;
    }

    // Método main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BancosConectados();
        });
    }
}

