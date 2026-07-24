package gui;

import java.awt.*;
import javax.swing.*;
import gui.components.RoundedTextFieldGrande;
import gui.components.BotonNeoColor;

public class DatosPersonales extends JFrame {

    private Image fondo;
    private Image logo;

    private RoundedTextFieldGrande txtNombreCompleto;
    private RoundedTextFieldGrande txtDpiCui;
    private String tipoCuenta;
    private RoundedTextFieldGrande txtFechaNacimiento;
    private RoundedTextFieldGrande txtCorreoElectronico;
    private RoundedTextFieldGrande txtTelefono;

    // ============================
    // CONSTRUCTOR
    // ============================
    public DatosPersonales(String tipoCuenta) {

    this.tipoCuenta = tipoCuenta;

    fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
    logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

    setTitle("Neo Jacket - Datos Personales");
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

            JLabel titulo = new JLabel("Datos Personales");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            // CAMPOS
            panel.add(crearLabel("Nombre Completo *", 40, 90));
            txtNombreCompleto = crearField(40, 125);
            panel.add(txtNombreCompleto);

            panel.add(crearLabel("DPI / CUI *", 40, 190));
            txtDpiCui = crearField(40, 225);
            panel.add(txtDpiCui);

            panel.add(crearLabel("Fecha de nacimiento", 40, 290));
            txtFechaNacimiento = crearField(40, 325);
            panel.add(txtFechaNacimiento);

            panel.add(crearLabel("Correo electrónico", 40, 390));
            txtCorreoElectronico = crearField(40, 425);
            panel.add(txtCorreoElectronico);

            panel.add(crearLabel("Teléfono", 40, 490));
            txtTelefono = crearField(40, 525);
            panel.add(txtTelefono);

            // BOTÓN SIGUIENTE
            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeoColor btnSiguiente = new BotonNeoColor("Siguiente", amarillo, amarilloHover);
            btnSiguiente.setBounds(40, 590, 400, 55);
            panel.add(btnSiguiente);
            

            btnSiguiente.addActionListener(e -> {
                String nombre = txtNombreCompleto.getText().trim();
                String dpi = txtDpiCui.getText().trim();
                String correo = txtCorreoElectronico.getText().trim();

                if (nombre.isEmpty() || dpi.isEmpty() || correo.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Por favor complete los campos Nombre, DPI/CUI y Correo electrónico.",
                            "Campos obligatorios",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                new DatosTarjeta(correo, dpi, tipoCuenta);
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

        private RoundedTextFieldGrande crearField(int x, int y) {
            RoundedTextFieldGrande txt = new RoundedTextFieldGrande(20);
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

