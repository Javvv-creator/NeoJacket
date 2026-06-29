package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.JTableHeader;

public class ConsultarSaldos extends JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public ConsultarSaldos() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Consultar Saldos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

    // ============================
    // PANEL PRINCIPAL CON FONDO
    // ============================
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

            String[] botonesMenu = {"Saldos", "Bancos conectados", "Transferencias", "Divisas", "Historial"};
            int y = 140;
            for (String textoBtn : botonesMenu) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                sidebar.add(btn);
                y += 70;
            }
            add(sidebar);
        }

        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 180));
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);
            
            

            // Barra superior con pestañas
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(94, 116, 73, 200));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 434, 55);
            btnTab1.setBackground(new Color(25, 38, 35, 100));
            btnTab1.setForeground(Color.WHITE);
            btnTab1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab1.addActionListener(e -> {
                new AgregarFondos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab1);

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 434, 55);
            btnTab2.setBackground(new Color(25, 38, 35, 100));
            btnTab2.setForeground(Color.WHITE);
            btnTab2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab2.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab2);

            JButton btnTab3 = new JButton("Consultar Saldos");
            btnTab3.setBounds(866, 0, 434, 65);
            btnTab3.setBackground(new Color(251, 232, 138, 200));
            btnTab3.setForeground(Color.BLACK);
            btnTab3.setFont(new Font("Segoe UI", Font.BOLD, 14));
            barraSuperior.add(btnTab3);
            
            // Panel con borde amarillo y fondo transparente
JPanel panelBanco = new JPanel();
panelBanco.setLayout(null);
panelBanco.setBounds(40, 70, 1170, 110); // posición y tamaño
panelBanco.setBackground(new Color(25, 38, 35, 150)); // fondo oscuro semi-transparente
panelBanco.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2)); // borde amarillo

// Label dentro del panel
JLabel lblBanco = new JLabel("Selecciona tu banco");
lblBanco.setForeground(new Color(251, 232, 138));
lblBanco.setFont(new Font("Segoe UI", Font.BOLD, 15));
lblBanco.setBounds(20, 15, 400, 35);
panelBanco.add(lblBanco);

// ComboBox dentro del panel
String[] opcionesBancos = {"Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"};
JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
cbBancos.setBounds(20, 45, 400, 40);
cbBancos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
cbBancos.setBackground(new Color(25, 38, 35));
cbBancos.setForeground(Color.WHITE);
panelBanco.add(cbBancos);

// Agregar el panel al contenedor principal
contenedor.add(panelBanco);


            // PANEL 2 — TABLA DE RESULTADOS (con borde blanco)
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 230, 1180, 330);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            contenedor.add(panelTabla);
 
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


            // Botón Consultar Detalles
            JButton btnConsultar = new JButton("Consultar Detalles") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(251, 232, 138));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnConsultar.setBounds(50, 640, 400, 50);
            btnConsultar.setForeground(Color.BLACK);
            btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnConsultar.setFocusPainted(false);
            btnConsultar.setContentAreaFilled(false);
            btnConsultar.setBorderPainted(false);
            btnConsultar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            contenedor.add(btnConsultar);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ============================
    // MAIN
    // ============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConsultarSaldos().setVisible(true);
        });
    }
}
