package gui;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

import funcionalidades.ServicioTransaccion;

public class DetallesTransaccion extends JFrame {

    private Image fondo;
    private Image logo;
    private final long idTransaccion;
    private final ServicioTransaccion servicio = new ServicioTransaccion();

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
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class BotonNeo extends JButton {
        private Color normal, hover;
        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public DetallesTransaccion(long idTransaccion) {
        this.idTransaccion = idTransaccion;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Detalles de Transacción");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new FondoPanel());
        setVisible(true);

        cargarDetalle();
    }

    private RoundedTextField txtIdTransaccion;
    private RoundedTextField txtFecha;
    private RoundedTextField txtTipo;
    private RoundedTextField txtCuentaOrigen;
    private RoundedTextField txtCuentaDestino;
    private RoundedTextField txtEstado;
    private RoundedTextField txtUsuario;
    private RoundedTextField txtDescripcion;

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearDetalles();
        }

    private void crearDetalles() {

            // LOGO
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(650, 120, 550, 840);
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            add(panel);

            JLabel titulo = new JLabel("Detalles de la Transacción");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            // CAMPOS
            panel.add(crearLabel("ID Transacción:", 40, 90));
            txtIdTransaccion = crearField("", 40, 125);
            panel.add(txtIdTransaccion);

            panel.add(crearLabel("Fecha:", 40, 190));
            txtFecha = crearField("", 40, 225);
            panel.add(txtFecha);

            panel.add(crearLabel("Tipo:", 40, 290));
            txtTipo = crearField("", 40, 325);
            panel.add(txtTipo);

            panel.add(crearLabel("Cuenta de origen:", 40, 390));
            txtCuentaOrigen = crearField("", 40, 425);
            panel.add(txtCuentaOrigen);

            panel.add(crearLabel("Cuenta de destino:", 40, 490));
            txtCuentaDestino = crearField("", 40, 525);
            panel.add(txtCuentaDestino);

            panel.add(crearLabel("Estado:", 40, 560));
            txtEstado = crearField("", 40, 595);
            panel.add(txtEstado);

            panel.add(crearLabel("Realizada por:", 40, 630));
            txtUsuario = crearField("", 40, 665);
            panel.add(txtUsuario);

            panel.add(crearLabel("Descripción:", 40, 700));
            txtDescripcion = crearField("", 40, 735);
            panel.add(txtDescripcion);

            // BOTÓN REGRESAR
            Color gris = new Color(120, 120, 120);
            Color grisHover = new Color(160, 160, 160);

            BotonNeo btnRegresar = new BotonNeo("Regresar", gris, grisHover);
            btnRegresar.setBounds(40, 780, 400, 55);
            panel.add(btnRegresar);

            btnRegresar.addActionListener(e -> {
                new GestionTransacciones();
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

        private RoundedTextField crearField(String texto, int x, int y) {
            RoundedTextField txt = new RoundedTextField(20);
            txt.setBounds(x, y, 450, 50);
            txt.setText(texto);
            txt.setEditable(false);
            txt.setForeground(Color.WHITE);
            txt.setCaretColor(Color.WHITE);
            return txt;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void cargarDetalle() {
        Map<String, Object> detalle = servicio.obtenerDetalleTransaccion(idTransaccion);
        txtIdTransaccion.setText(String.valueOf(detalle.getOrDefault("id_transaccion", "")));
        txtFecha.setText(String.valueOf(detalle.getOrDefault("fecha", "")));
        txtTipo.setText(String.valueOf(detalle.getOrDefault("tipo_transaccion", "")));
        txtCuentaOrigen.setText(String.valueOf(detalle.getOrDefault("cuenta_origen", "")));
        txtCuentaDestino.setText(String.valueOf(detalle.getOrDefault("cuenta_destino", "")));
        txtEstado.setText(String.valueOf(detalle.getOrDefault("estado", "")));
        txtUsuario.setText(String.valueOf(detalle.getOrDefault("usuario", "")));
        txtDescripcion.setText(String.valueOf(detalle.getOrDefault("descripcion", "")));
    }
}

