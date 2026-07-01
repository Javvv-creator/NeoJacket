package gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class BancosConectados extends JFrame {

    private Image fondo;
    private Image logo;

    Font tituloPanel = new Font("Segoe UI", Font.BOLD, 16);
    Font textoNormal = new Font("Segoe UI", Font.PLAIN, 14);

    public BancosConectados() {
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
            crearSidebar(this);
            crearContenido(this);
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

        sidebar.add(btn);
        y += 60;
    }

    // ESTA ES LA ÚNICA LÍNEA QUE DEBE EXISTIR
    panel.add(sidebar);
}


    // Contenido central
private void crearContenido(JPanel panel) {
    
    JLabel lblTitulo = new JLabel("BANCOS CONECTADOS");
lblTitulo.setForeground(Color.WHITE);
lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
lblTitulo.setBounds(450, 20, 400, 40);
panel.add(lblTitulo);

JLabel lblSubtitulo = new JLabel("AQUI PUEDES GESTIONAR EL CONTROL DE TUS BANCOS");
lblSubtitulo.setForeground(Color.WHITE);
lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
lblSubtitulo.setBounds(450, 60, 500, 30);
panel.add(lblSubtitulo);

    // Grupo para que solo se seleccione un banco
    ButtonGroup grupoBancos = new ButtonGroup();

    // Ajuste de coordenadas X para centrar más los cuadros
    int offsetX = 100; // mueve todo un poco a la derecha

    // Panel Banco Industrial
    JPanel panelIndustrial = crearCard(350 + offsetX, 120, 200, 150);
    panelIndustrial.setLayout(null);
    Image imgIndustrial = new ImageIcon(getClass().getResource("/gui/image/banco_industrial.png")).getImage();
    Image imgEscaladaIndustrial = imgIndustrial.getScaledInstance(220, 150, Image.SCALE_SMOOTH);
    
    JRadioButton btnIndustrial = new JRadioButton(new ImageIcon(imgEscaladaIndustrial));
    btnIndustrial.setBounds((panelIndustrial.getWidth() - 220) / 2,20,220, 150);
    btnIndustrial.setBounds(2, 20, 170, 100);
    btnIndustrial.setBackground(new Color(25, 38, 35));
    grupoBancos.add(btnIndustrial);
    panelIndustrial.add(btnIndustrial);
    panel.add(panelIndustrial);
    
    JRadioButton rbIndustrial = new JRadioButton("Banco Industrial");
rbIndustrial.setForeground(Color.WHITE);
rbIndustrial.setBackground(new Color(25, 38, 35));
rbIndustrial.setBounds(40, 125, 150, 20);
grupoBancos.add(rbIndustrial);
panelIndustrial.add(rbIndustrial);


    

    // Panel Banrural
    JPanel panelBanrural = crearCard(600 + offsetX, 120, 200, 150);
    panelBanrural.setLayout(null);
    Image imgBanrural = new ImageIcon(getClass().getResource("/gui/image/banrural.png")).getImage();
    Image imgEscaladaBanrural = imgBanrural.getScaledInstance(220, 150, Image.SCALE_SMOOTH);
    JRadioButton btnBanrural = new JRadioButton(new ImageIcon(imgEscaladaBanrural));
    btnBanrural.setBounds((panelBanrural.getWidth() - 220) / 2,20,220, 150);
    btnBanrural.setBounds(15, 20, 170, 100);
    btnBanrural.setBackground(new Color(25, 38, 35));
    grupoBancos.add(btnBanrural);
    panelBanrural.add(btnBanrural);
    panel.add(panelBanrural);
    
    JRadioButton rbBanrural = new JRadioButton("Banrural");
rbBanrural.setForeground(Color.WHITE);
rbBanrural.setBackground(new Color(25, 38, 35));
rbBanrural.setBounds(40, 125, 150, 20);
grupoBancos.add(rbBanrural);
panelBanrural.add(rbBanrural);


    // Panel BAC Credomatic
    JPanel panelBAC = crearCard(850 + offsetX, 120, 200, 150);
    panelBAC.setLayout(null);
    Image imgBAC = new ImageIcon(getClass().getResource("/gui/image/bac.png")).getImage();
    Image imgEscaladaBAC = imgBAC.getScaledInstance(220, 150, Image.SCALE_SMOOTH);
    JRadioButton btnBAC = new JRadioButton(new ImageIcon(imgEscaladaBAC));
    btnBAC.setBounds((panelBAC.getWidth() - 220) / 2,(panelBAC.getHeight() - 150) / 2,220, 150);
    btnBAC.setBounds(15, 20, 170, 100);
    btnBAC.setBackground(new Color(25, 38, 35));
    grupoBancos.add(btnBAC);
    panelBAC.add(btnBAC);
    panel.add(panelBAC);
    
   JRadioButton rbBac = new JRadioButton("Ban Credomatic");
rbBac.setForeground(Color.WHITE);
rbBac.setBackground(new Color(25, 38, 35));
rbBac.setBounds(40, 125, 150, 20);
grupoBancos.add(rbBac);
panelBAC.add(rbBac);


    // Panel G&T Continental
    JPanel panelGYT = crearCard(1100 + offsetX, 120, 200, 150);
    panelGYT.setLayout(null);
    Image imgGYT = new ImageIcon(getClass().getResource("/gui/image/gyt.png")).getImage();
    Image imgEscaladaGYT = imgGYT.getScaledInstance(220, 150, Image.SCALE_SMOOTH);
    JRadioButton btnGYT = new JRadioButton(new ImageIcon(imgEscaladaGYT));
    btnGYT.setBounds((panelGYT.getWidth() - 220) / 2,(panelGYT.getHeight() - 150) / 2,220, 150);
    btnGYT.setBounds(15, 20, 170, 100);
    btnGYT.setBackground(new Color(25, 38, 35));
    grupoBancos.add(btnGYT);
    panelGYT.add(btnGYT);
    panel.add(panelGYT);
    
       JRadioButton rbGYT = new JRadioButton("G&T Continental");
rbGYT.setForeground(Color.WHITE);
rbGYT.setBackground(new Color(25, 38, 35));
rbGYT.setBounds(40, 125, 150, 20);
grupoBancos.add(rbGYT);
panelGYT.add(rbGYT);
    
    JButton btnFlujo = new JButton("→ Flujo de Bancos");
btnFlujo.setBounds(1380, 450, 250, 50); // ajusta coordenadas según tu layout
btnFlujo.setFocusPainted(false);
btnFlujo.setForeground(Color.BLACK);
btnFlujo.setBackground(new Color(251, 232, 138)); // estilo amarillo
btnFlujo.setFont(new Font("Segoe UI", Font.BOLD, 16));
panel.add(btnFlujo);


    // Panel de "Saldo Disponible" (sin borde amarillo)
    JPanel panelSaldo = crearCardSinBorde(350 + offsetX, 320, 400, 150);
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

    // Panel de "Nombre del Banco" (sin borde amarillo)
    JPanel panelBanco = crearCardSinBorde(800 + offsetX, 320, 400, 150);
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

    // PANEL 2 — TABLA DE RESULTADOS (con borde blanco)
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(450, 510, 1180, 380);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            panel.add(panelTabla);
 
            String[] columnas = {"Fecha", "Actividad", "Monto", "Saldo Restante", "Estado"};
 
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
 
            JTable tabla = new JTable(modelo);
            tabla.setRowHeight(35);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(94, 116, 73));
            tabla.setSelectionBackground(new Color(251, 232, 138));
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);
 
            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
 
            JScrollPane scroll = new JScrollPane(tabla);
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(10, 10, 1160, 310);
            panelTabla.add(scroll);
            
}

    @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // -------------------------
    // Métodos auxiliares fuera de FondoPanel
    // -------------------------
    private JPanel crearCard(int x, int y, int w, int h) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 38, 35, 210));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.setColor(new Color(251, 232, 138));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 30, 30));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBounds(x, y, w, h);
        return panel;
    }

    private JPanel crearCardSinBorde(int x, int y, int w, int h) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 38, 35, 210));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBounds(x, y, w, h);
        return panel;
    }

    // -------------------------
    // Main
    // -------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BancosConectados().setVisible(true));
    }
}