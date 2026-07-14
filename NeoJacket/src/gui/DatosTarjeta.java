package gui;

import funcionalidades.AgregarTarjeta;
import funcionalidades.CrearUsuario;
import java.awt.*;
import javax.swing.*;

public class DatosTarjeta extends JFrame {

    private Image fondo;
    private Image logo;
    private String correoUsuario;
    private String dpiUsuario;
    private String tipoCuenta;

    // ============================
    // TEXTFIELD REDONDEADO
    // ============================
    class RoundedTextField extends JTextField {

        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN NEO
    // ============================
    class BotonNeo extends JButton {

        private Color normal;
        private Color hover;

        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public DatosTarjeta(String correoUsuario, String dpiUsuario, String tipoCuenta) {

        this.correoUsuario = correoUsuario;
        this.dpiUsuario = dpiUsuario;
        this.tipoCuenta = tipoCuenta;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Datos de Tarjeta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    // ============================
    // PANEL PRINCIPAL
    // ============================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearFormulario();
        }

        private void crearFormulario() {

            // LOGO
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(650, 120, 550, 650);
            panel.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panel);

            JLabel titulo = new JLabel("Datos de Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            // CAMPOS EDITABLES
            panel.add(crearLabel("Tipo de tarjeta", 40, 90));
            JTextField txtTipo = crearField(40, 125);
            panel.add(txtTipo);

            panel.add(crearLabel("País de su cuenta o tarjeta", 40, 190));
            JTextField txtPais = crearField(40, 225);
            panel.add(txtPais);

            panel.add(crearLabel("Número de cuenta o tarjeta *", 40, 290));
            JTextField txtNumero = crearField(40, 325);
            panel.add(txtNumero);

            panel.add(crearLabel("Seleccione el banco", 40, 390));
            JComboBox<String> cbBanco = new JComboBox<>(new String[]{"Banco Industrial", "Banco de América Central (BAC)", "Banrural", "Banco G&T Continental"});
            cbBanco.setBounds(40, 425, 450, 50);
            cbBanco.setBackground(new Color(25, 38, 35, 200));
            cbBanco.setForeground(Color.WHITE);
            cbBanco.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            cbBanco.setFocusable(false);
            panel.add(cbBanco);

            // BOTÓN GUARDAR
            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeo btnGuardar = new BotonNeo("Guardar", amarillo, amarilloHover);
            btnGuardar.setBounds(40, 500, 400, 55);
            panel.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
            String tipo = txtTipo.getText().trim();
            String pais = txtPais.getText().trim();
            String numero = txtNumero.getText().trim();
            String banco = cbBanco.getSelectedItem().toString();

            try {
                CrearUsuario crear = new CrearUsuario();
                int idUsuario = crear.obtenerIdUsuario(correoUsuario, dpiUsuario);

                if (idUsuario == -1) {
                    JOptionPane.showMessageDialog(null,
                            "No se encontró el usuario. Verifica el registro.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 1️⃣ PRIMERO: crear las 4 cuentas bancarias
                crear.crearCuentaBancaria(idUsuario, 1, tipoCuenta, numero);
                crear.crearCuentaBancaria(idUsuario, 2, tipoCuenta, numero);
                crear.crearCuentaBancaria(idUsuario, 3, tipoCuenta, numero);
                crear.crearCuentaBancaria(idUsuario, 4, tipoCuenta, numero);

                // 2️⃣ DESPUÉS: registrar la tarjeta, ahora sí encuentra la cuenta correspondiente
                AgregarTarjeta servicio = new AgregarTarjeta();
                boolean guardado = servicio.registrarTarjeta(
                        correoUsuario,
                        dpiUsuario,
                        tipo,
                        pais,
                        numero,
                        banco);

                if (guardado) {
                    JOptionPane.showMessageDialog(null,
                            "✅ Tarjeta y cuentas bancarias creadas correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    new InicioNeo().setVisible(true);
                    dispose();
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null,
                        "❌ " + ex.getMessage(),
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error al guardar los datos de la tarjeta: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

            // BOTÓN VOLVER AL INICIO
            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            BotonNeo btnVolver = new BotonNeo("Volver al inicio", verde, verdeHover);
            btnVolver.setForeground(Color.WHITE);
            btnVolver.setBounds(40, 570, 400, 55);
            panel.add(btnVolver);

            btnVolver.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });
        }

        private JLabel crearLabel(String texto, int x, int y) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBounds(x, y, 300, 30);
            return lbl;
        }

        private RoundedTextField crearField(int x, int y) {
            RoundedTextField txt = new RoundedTextField(20);
            txt.setBounds(x, y, 450, 50);
            return txt;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
