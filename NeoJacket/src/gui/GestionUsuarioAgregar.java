



package gui;

import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class GestionUsuarioAgregar extends JFrame {
    
        private Image fondo;
        private Image logo;
     

    class BotonNeo extends JButton {

    public BotonNeo(String texto) {

        super(texto);

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);

        setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover()) {

            g2.setColor(
                    new Color(
                            251,
                            232,
                            138,
                            220));

        } else {

            g2.setColor(
                    new Color(
                            94,
                            116,
                            73,
                            190));
        }

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                18,
                18);

        g2.setColor(
                new Color(
                        255,
                        255,
                        255,
                        80));

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                18,
                18);

        super.paintComponent(g);

        g2.dispose();
    }
}
    public GestionUsuarioAgregar() {

        fondo = new ImageIcon(
                getClass().getResource("/gui/image/fondo.png"))
                .getImage();

        logo = new ImageIcon(
                getClass().getResource("/gui/image/logoblanco.png"))
                .getImage();

        setTitle("Agregar de usuarios");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());

        setVisible(true);
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {

            setLayout(null);

            crearSidebar();
            crearPanelPrincipal();
        }

        private void crearSidebar() {

            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado =
                    logo.getScaledInstance(
                            250,
                            110,
                            Image.SCALE_SMOOTH);

            JLabel lblLogo =
                    new JLabel(
                            new ImageIcon(
                                    logoEscalado));

            lblLogo.setBounds(
                    20,
                    10,
                    250,
                    110);

            sidebar.add(lblLogo);

            String[] botones = {
                "Gestión de Usuarios",
                "Gestión de Menores",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;

            for (String texto : botones) {

                JButton btn =
                        new JButton(texto);

                btn.setBounds(
                        20,
                        y,
                        250,
                        55);

                btn.setFocusPainted(false);
                btn.setBorderPainted(false);

                if (texto.equals("Agregar de usuarios")) {

                    btn.setBackground(
                            new Color(
                                    251,
                                    232,
                                    138));

                    btn.setForeground(
                            Color.BLACK);

                } else {

                    btn.setBackground(
                            new Color(
                                    94,
                                    116,
                                    73));

                    btn.setForeground(
                            Color.WHITE);
                }

                sidebar.add(btn);

                y += 70;
            }

            add(sidebar);
        }

        private void crearPanelPrincipal() {

            JPanel panel = new JPanel();
            panel.setLayout(null);

            panel.setBackground(
        new Color(
                25,
                38,
                35,
                150));

            panel.setBounds(
                    350,
                    60,
                    1300,
                    760);

            add(panel);
            
            JPanel banner = new JPanel();
banner.setLayout(null);

banner.setBackground(
        new Color(
                25,
                38,
                35,
                230));

banner.setBounds(
        0,
        0,
        1300,
        110);

panel.add(banner);

            JLabel titulo =
                    new JLabel(
                            "Agregar de usuarios");

            titulo.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            34));

            titulo.setForeground(
                    Color.WHITE);

            titulo.setBounds(
                    30,
                    20,
                    500,
                    40);

            banner.add(titulo);

            JLabel subtitulo =
                    new JLabel(
                            "Completa la información para registrar un nuevo usuario");

            subtitulo.setForeground(
                    Color.WHITE);

            subtitulo.setBounds(
                    30,
                    65,
                    400,
                    20);

            banner.add(subtitulo);

            JLabel lblId =
                    new JLabel(
                            "ID Usuario");

            lblId.setForeground(
                    Color.WHITE);

            lblId.setBounds(
                    40,
                    130,
                    100,
                    25);

            panel.add(lblId);

            JTextField txtId =
                    new JTextField();

            txtId.setBounds(
                    130,
                    125,
                    200,
                    35);

            panel.add(txtId);

            JLabel lblTipo =
                    new JLabel(
                            "Tipo");

            lblTipo.setForeground(
                    Color.WHITE);

            lblTipo.setBounds(
                    430,
                    130,
                    50,
                    25);

            panel.add(lblTipo);

            JComboBox<String> cbTipo =
                    new JComboBox<>(
                            new String[]{
                                "Todos"
                            });

            cbTipo.setBounds(
                    490,
                    125,
                    180,
                    35);

            panel.add(cbTipo);

            JLabel lblEstado =
                    new JLabel(
                            "Estado");

            lblEstado.setForeground(
                    Color.WHITE);

            lblEstado.setBounds(
                    780,
                    130,
                    60,
                    25);

            panel.add(lblEstado);

            JComboBox<String> cbEstado =
                    new JComboBox<>(
                            new String[]{
                                "Todos"
                            });

            cbEstado.setBounds(
                    850,
                    125,
                    180,
                    35);

            panel.add(cbEstado);

            String[] columnas = {
                "ID",
                "Usuario",
                "Tipo",
                "Estado"
            };

            Object[][] datos = {};

            JTable tabla =
                    new JTable(
                            datos,
                            columnas);

            tabla.setRowHeight(35);
            
            
            tabla.setBackground(
        new Color(
                25,
                38,
                35));

tabla.setForeground(
        Color.WHITE);

tabla.setGridColor(
        new Color(
                94,
                116,
                73));

tabla.setSelectionBackground(
        new Color(
                251,
                232,
                138));

tabla.setSelectionForeground(
        Color.BLACK);

tabla.setShowGrid(true);

            JTableHeader header =
                    tabla.getTableHeader();

            header.setBackground(
                    new Color(
                            94,
                            116,
                            73));

            header.setForeground(
        Color.WHITE);

header.setFont(
        new Font(
                "Segoe UI",
                Font.BOLD,
                14));

            JScrollPane scroll =
                    new JScrollPane(
                            tabla);
            
            scroll.getViewport()
        .setBackground(
                new Color(
                        25,
                        38,
                        35));

            scroll.setBounds(
                    40,
                    220,
                    1180,
                    280);

            panel.add(scroll);

            BotonNeo btnDetalles =
        new BotonNeo(
                "Ver detalles");

            btnDetalles.setBounds(
                    40,
                    530,
                    180,
                    45);

            panel.add(btnDetalles);
            
            
            btnDetalles.setBackground(
        new Color(
                60,
                65,
                60));

            BotonNeo btnAgregar =
        new BotonNeo(
                "Agregar usuario");

            btnAgregar.setBounds(
                    50,
                    620,
                    220,
                    50);

            panel.add(btnAgregar);
            
            btnAgregar.addActionListener(e -> {

    new GestionUsuarioAgregar();

    dispose();
});

           BotonNeo btnEditar =
        new BotonNeo(
                "Editar usuario");

            btnEditar.setBounds(
                    500,
                    620,    
                    220,
                    50);

            panel.add(btnEditar);

            BotonNeo btnCancelar =
        new BotonNeo(
                "Cancelar");

            btnCancelar.setBounds(
                    950,
                    620,
                    220,
                    50);

            panel.add(btnCancelar);
            
            btnCancelar.addActionListener(e -> {

    new GestionUsuario();

    dispose();
});

            JButton btnVolver =
                    new JButton(
                            "Volver");

            btnVolver.setBounds(
                    1080,
                    20,
                    120,
                    40);

            btnVolver.addActionListener(e -> {

                new PanelControlAdmin();

                dispose();
            });

            panel.add(btnVolver);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            g.drawImage(
                    fondo,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this);
        }
    }
}


