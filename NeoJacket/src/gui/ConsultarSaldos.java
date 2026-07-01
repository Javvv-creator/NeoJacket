package gui;

import funcionalidades.SesionUsuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.CRUD.CRUD;
import main.Conexion.conexion;

public class ConsultarSaldos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public ConsultarSaldos() {
        initComponents();

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Consultar Saldos");
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
        // Método para crear el menú lateral
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

            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 433, 55);
            btnTab1.addActionListener(e -> { new AgregarFondos().setVisible(true); dispose(); });
            barraSuperior.add(btnTab1);

            JButton btnTab2 = new JButton("Actualizar Saldos");
            btnTab2.setBounds(433, 0, 433, 55);
            btnTab2.addActionListener(e -> { new ActualizarSaldos().setVisible(true); dispose(); });
            barraSuperior.add(btnTab2);

            JButton btnTab3 = new JButton("Consultar Saldos");
            btnTab3.setBounds(866, 0, 433, 55);
            btnTab3.setBackground(new Color(251, 232, 138, 200));
            btnTab3.setForeground(Color.BLACK);
            barraSuperior.add(btnTab3);

            // ComboBox bancos
            JComboBox<String> cbBancos = new JComboBox<>(new String[]{
                "Banco Industrial", "Banrural", "BAC Credomatic", "G&T Continental"
            });
            cbBancos.setBounds(30, 60, 300, 40);
            contenedor.add(cbBancos);

            // Botón consultar
            JButton btnConsultar = new JButton("Consultar");
            btnConsultar.setBounds(350, 60, 150, 40);
            contenedor.add(btnConsultar);

            // Tabla
            String[] columnas = {"Fecha", "Actividad", "Monto", "Saldo Restante", "Estado"};
            modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            tabla = new JTable(modelo);
            tabla.setRowHeight(40);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(35, 50, 40));
            header.setForeground(Color.WHITE);

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBounds(30, 120, 790, 350);
            contenedor.add(scroll);

            // Acción consultar
            btnConsultar.addActionListener(e -> {
                try {
                    CRUD crud = new CRUD();
                    int idUsuario = SesionUsuario.getIdUsuario();
                    String bancoSeleccionado = (String) cbBancos.getSelectedItem();

                    Connection con = conexion.getConexion();
                    PreparedStatement psBanco = con.prepareStatement("SELECT id_banco FROM bancos WHERE nombre = ?");
                    psBanco.setString(1, bancoSeleccionado);
                    ResultSet rsBanco = psBanco.executeQuery();

                    if (rsBanco.next()) {
                        int idBanco = rsBanco.getInt("id_banco");

                        double saldo = crud.consultarSaldo(idUsuario, idBanco);
                        JOptionPane.showMessageDialog(this, "Saldo actual: " + saldo);

                        ResultSet rsTrans = crud.consultarTransacciones(idUsuario, idBanco);
                        modelo.setRowCount(0);

                        while (rsTrans.next()) {
                            Object[] fila = {
                                rsTrans.getTimestamp("creado_en"),
                                rsTrans.getString("tipo_transaccion"),
                                rsTrans.getDouble("monto"),
                                rsTrans.getDouble("saldoRestante"),
                                rsTrans.getString("estado")
                            };
                            modelo.addRow(fila);
                        }
                        rsTrans.close();
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

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // -------------------------
    // initComponents y main
    // -------------------------
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new ConsultarSaldos().setVisible(true));
    }
}
