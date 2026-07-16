package gui;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class DetalleCuenta extends JFrame {

    private Image fondo;
    private Image logo;
    private final int idCuenta;

    // Campos como variables de instancia para poder llenarlos
    private RoundedTextField txtNumeroCuenta;
    private RoundedTextField txtPropietario;
    private RoundedTextField txtTipoCuenta;
    private RoundedTextField txtBanco;
    private RoundedTextField txtFechaCreacion;
    private RoundedTextField txtSaldo;
    private RoundedTextField txtEstado;
    private RoundedTextField txtMoneda;
    private RoundedTextField txtCodigoCuenta;
    private RoundedTextField txtUltimaActualizacion;
    private RoundedTextField txtUltimaTransaccion;

    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setEditable(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class BotonNeo extends JButton {
        public BotonNeo(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover()
                    ? new Color(251, 232, 138, 220)
                    : new Color(94, 116, 73, 190));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public DetalleCuenta(int idCuenta) {

        this.idCuenta = idCuenta;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Información de Cuenta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE para no cerrar todo el programa al regresar
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);

        cargarDetalle();
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

            String[] botones = {
                "Gestión de Usuarios",
                "Gestión de Menores Supervisados",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;

            for (String texto : botones) {
                JButton btn = new JButton(texto);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);

                if (texto.equals("Gestión de Cuentas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                }

                if (texto.equals("Gestión de Usuarios")) {
                    btn.addActionListener(e -> {
                        new GestionUsuario();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Menores Supervisados")) {
                    btn.addActionListener(e -> {
                        new GestionMenores();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Tarjetas")) {
                    btn.addActionListener(e -> {
                        new GestionTarjeta();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Divisas")) {
                    btn.addActionListener(e -> {
                        new GestionDivisas();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Transacciones")) {
                    btn.addActionListener(e -> {
                        new GestionTransacciones();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            add(sidebar);
        }

        private void crearContenido() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Información de Cuenta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            JLabel subtitulo = new JLabel("Consulta los detalles completos de la cuenta seleccionada");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 600, 20);
            panel.add(subtitulo);

            Color amarillo = new Color(251, 232, 138);

            // ============================
            // DOS COLUMNAS
            // ============================
            int colIzqX = 30;
            int colDerX = 700;
            int fieldOffsetX = 220;
            int fieldWidth = 350;
            int rowHeight = 65;

            int yIzq = 130;
            int yDer = 130;

            txtNumeroCuenta = agregarCampo(panel, "Número de cuenta:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);
            yIzq += rowHeight;
            txtPropietario = agregarCampo(panel, "Propietario:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);
            yIzq += rowHeight;
            txtTipoCuenta = agregarCampo(panel, "Tipo de cuenta:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);
            yIzq += rowHeight;
            txtBanco = agregarCampo(panel, "Banco:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);
            yIzq += rowHeight;
            txtFechaCreacion = agregarCampo(panel, "Fecha de creación:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);
            yIzq += rowHeight;
            txtSaldo = agregarCampo(panel, "Saldo actual:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);

            txtEstado = agregarCampo(panel, "Estado:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);
            yDer += rowHeight;
            txtMoneda = agregarCampo(panel, "Moneda:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);
            yDer += rowHeight;
            txtCodigoCuenta = agregarCampo(panel, "Código de cuenta:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);
            yDer += rowHeight;
            txtUltimaActualizacion = agregarCampo(panel, "Última actualización:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);
            yDer += rowHeight;
            txtUltimaTransaccion = agregarCampo(panel, "Última transacción:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);

            // ============================
            // BOTÓN REGRESAR
            // ============================
            BotonNeo btnRegresar = new BotonNeo("Regresar");
            btnRegresar.setBounds(30, 620, 200, 50);
            btnRegresar.addActionListener(e -> {
                new GestionCuentas();
                dispose();
            });
            panel.add(btnRegresar);
        }

        private RoundedTextField agregarCampo(JPanel panel, String etiqueta, int xLabel, int y,
                                               int offsetX, int width, Color color) {
            JLabel lbl = new JLabel(etiqueta);
            lbl.setForeground(color);
            lbl.setBounds(xLabel, y, offsetX - 10, 25);
            panel.add(lbl);

            RoundedTextField txt = new RoundedTextField(20);
            txt.setBounds(xLabel + offsetX, y - 5, width, 40);
            panel.add(txt);
            return txt;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ============================
    // CARGAR DATOS DE LA CUENTA DESDE LA BASE DE DATOS (Corregido)
    // ============================
    private void cargarDetalle() {

        // Se removieron las columnas c.fecha_creacion, c.fecha_actualizacion y c.ultima_transaccion
        String sql = "SELECT c.numero_cuenta, CONCAT(u.nombre, ' ', u.apellido) AS propietario, "
                + "t.nombre AS tipo_cuenta, b.nombre AS banco, c.saldo, "
                + "c.estado, c.moneda, c.id_cuenta "
                + "FROM cuentas_bancarias c "
                + "JOIN usuarios u ON c.id_usuario = u.id_usuario "
                + "JOIN tipos_cuentas t ON c.id_tipo_cuenta = t.id_tipo "
                + "JOIN bancos b ON c.id_banco = b.id_banco "
                + "WHERE c.id_cuenta = ?";

        try (Connection con = main.Conexion.conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCuenta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtNumeroCuenta.setText(rs.getString("numero_cuenta"));
                    txtPropietario.setText(rs.getString("propietario"));
                    txtTipoCuenta.setText(rs.getString("tipo_cuenta"));
                    txtBanco.setText(rs.getString("banco"));
                    txtSaldo.setText("$ " + rs.getBigDecimal("saldo").toString());
                    txtEstado.setText(rs.getString("estado").toUpperCase());
                    txtMoneda.setText(rs.getString("moneda"));
                    txtCodigoCuenta.setText(String.valueOf(rs.getInt("id_cuenta")));
                   
                    // Llenamos por defecto los campos cuyas columnas no existen físicamente en la BD
                    txtFechaCreacion.setText("No disponible");
                    txtUltimaActualizacion.setText("No disponible");
                    txtUltimaTransaccion.setText("No disponible");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el detalle: " + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
