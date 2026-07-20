package gui;

import java.awt.*;
import javax.swing.*;
import gui.components.RoundedTextField;
import gui.components.RoundedArea;
import gui.components.BotonNeo;

public class DesbloquearCuenta extends JFrame {

    private Image fondo;
    private Image logo;

    // ============================
    // CONSTRUCTOR
    // ============================
    public DesbloquearCuenta() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Desbloquear Cuenta");
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
            crearSidebar();
            crearContenido();
        }

        // ============================
        // SIDEBAR
        // ============================
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

                if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            add(sidebar);
        }

        // ============================
        // CONTENIDO PRINCIPAL
        // ============================
        private void crearContenido() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Desbloquear Cuenta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            Color amarillo = new Color(251, 232, 138);

            JLabel lblCuenta = new JLabel("Número de cuenta:");
            lblCuenta.setForeground(amarillo);
            lblCuenta.setBounds(30, 100, 200, 25);
            panel.add(lblCuenta);

            RoundedTextField txtCuenta = new RoundedTextField(20);
            txtCuenta.setBounds(250, 95, 300, 40);
            panel.add(txtCuenta);

            JLabel lblTipo = new JLabel("Tipo de cuenta:");
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(30, 160, 200, 25);
            panel.add(lblTipo);

            RoundedTextField txtTipo = new RoundedTextField(20);
            txtTipo.setBounds(250, 155, 300, 40);
            panel.add(txtTipo);

            JLabel lblProp = new JLabel("Propietario:");
            lblProp.setForeground(amarillo);
            lblProp.setBounds(30, 220, 200, 25);
            panel.add(lblProp);

            RoundedTextField txtProp = new RoundedTextField(20);
            txtProp.setBounds(250, 215, 300, 40);
            panel.add(txtProp);

            JLabel lblEstado = new JLabel("Estado actual:");
            lblEstado.setForeground(amarillo);
            lblEstado.setBounds(30, 280, 200, 25);
            panel.add(lblEstado);

            RoundedTextField txtEstado = new RoundedTextField(20);
            txtEstado.setBounds(250, 275, 300, 40);
            panel.add(txtEstado);

            JLabel lblMotivo = new JLabel("Motivo del bloqueo:");
            lblMotivo.setForeground(amarillo);
            lblMotivo.setBounds(30, 350, 300, 25);
            panel.add(lblMotivo);

            RoundedTextField txtMotivo = new RoundedTextField(20);
            txtMotivo.setBounds(30, 380, 520, 40);
            panel.add(txtMotivo);

            JLabel lblObs = new JLabel("Observaciones (opcional):");
            lblObs.setForeground(amarillo);
            lblObs.setBounds(30, 450, 300, 25);
            panel.add(lblObs);

            RoundedArea txtObs = new RoundedArea();
            JScrollPane scroll = new JScrollPane(txtObs);
            scroll.setBounds(30, 480, 520, 150);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            panel.add(scroll);

            BotonNeo btnCancelar = new BotonNeo("Cancelar");
            btnCancelar.setBounds(30, 660, 200, 50);
            btnCancelar.addActionListener(e -> {
                new GestionCuentas();
                dispose();
            });
            panel.add(btnCancelar);

            BotonNeo btnDesbloquear = new BotonNeo("Desbloquear cuenta");
            btnDesbloquear.setBounds(250, 660, 250, 50);
            panel.add(btnDesbloquear);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

