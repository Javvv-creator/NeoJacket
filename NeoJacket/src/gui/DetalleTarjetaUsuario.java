package gui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.swing.*;
import funcionalidades.SesionUsuario;
import funcionalidades.ServicioTarjeta;
import gui.components.BotonNeo;
import gui.components.RoundedTextField;

/**
 * Pantalla de detalle de UNA tarjeta específica del usuario, abierta al
 * hacer clic en "Ver" junto a una tarjeta listada en el Dashboard.
 * Solo lectura + accesos directos a Bloquear/Desbloquear esa tarjeta.
 */
public class DetalleTarjetaUsuario extends JFrame {

    private final int idTarjeta;
    private Image fondo;
    private Image logo;

    private RoundedTextField txtNumero;
    private RoundedTextField txtTipo;
    private RoundedTextField txtBanco;
    private RoundedTextField txtPais;
    private RoundedTextField txtEstado;
    private RoundedTextField txtCreada;

    public DetalleTarjetaUsuario(int idTarjeta) {
        this.idTarjeta = idTarjeta;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Detalle de Tarjeta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());

        cargarDetalle();
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearContenido();
        }

        private void crearContenido() {
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 190));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    g2.dispose();
                }
            };
            panel.setLayout(null);
            panel.setOpaque(false);
            panel.setBounds(460, 90, 1000, 780);
            add(panel);

            Image logoEscalado = logo.getScaledInstance(220, 95, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(30, 20, 220, 95);
            panel.add(lblLogo);

            JLabel titulo = new JLabel("Detalle de Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 120, 500, 40);
            panel.add(titulo);

            Color amarillo = new Color(251, 232, 138);
            int colIzqX = 30, colDerX = 520, fieldOffsetX = 160, fieldWidth = 320, rowHeight = 80;
            int yIzq = 200, yDer = 200;

            txtNumero = agregarCampo(panel, "Número:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);
            yIzq += rowHeight;
            txtTipo = agregarCampo(panel, "Tipo:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);
            yIzq += rowHeight;
            txtBanco = agregarCampo(panel, "Banco:", colIzqX, yIzq, fieldOffsetX, fieldWidth, amarillo);

            txtPais = agregarCampo(panel, "País:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);
            yDer += rowHeight;
            txtEstado = agregarCampo(panel, "Estado:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);
            yDer += rowHeight;
            txtCreada = agregarCampo(panel, "Creada el:", colDerX, yDer, fieldOffsetX, fieldWidth, amarillo);

            BotonNeo btnBloquear = new BotonNeo("Bloquear tarjeta");
            btnBloquear.setBounds(30, 540, 280, 55);
            btnBloquear.addActionListener(e -> {
                new BloquearTarjeta(idTarjeta);
                dispose();
            });
            panel.add(btnBloquear);

            BotonNeo btnDesbloquear = new BotonNeo("Desbloquear tarjeta");
            btnDesbloquear.setBounds(330, 540, 280, 55);
            btnDesbloquear.addActionListener(e -> {
                new DesbloquearTarjeta(idTarjeta);
                dispose();
            });
            panel.add(btnDesbloquear);

            BotonNeo btnRegresar = new BotonNeo("← Regresar al Dashboard");
            btnRegresar.setBounds(30, 620, 400, 55);
            btnRegresar.addActionListener(e -> {
                new Dashboard(SesionUsuario.getIdUsuario()).setVisible(true);
                dispose();
            });
            panel.add(btnRegresar);
        }

        private RoundedTextField agregarCampo(JPanel panel, String etiqueta, int xLabel, int y,
                                               int offsetX, int width, Color color) {
            JLabel lbl = new JLabel(etiqueta);
            lbl.setForeground(color);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setBounds(xLabel, y, offsetX - 10, 25);
            panel.add(lbl);

            RoundedTextField txt = new RoundedTextField(20);
            txt.setEditable(false);
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

    private void cargarDetalle() {
        Map<String, Object> detalle = new ServicioTarjeta().obtenerDetalleTarjeta(idTarjeta);
        if (detalle.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la tarjeta con ID " + idTarjeta,
                    "Tarjeta no encontrada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtNumero.setText(String.valueOf(detalle.get("numero_tarjeta")));
        txtTipo.setText(String.valueOf(detalle.get("tipo_tarjeta")));
        txtBanco.setText(String.valueOf(detalle.get("banco")));
        txtPais.setText(String.valueOf(detalle.get("pais")));
        Object estado = detalle.get("estado");
        txtEstado.setText(estado != null ? estado.toString().toUpperCase() : "");

        Object creadoEn = detalle.get("creado_en");
        if (creadoEn instanceof java.util.Date) {
            txtCreada.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format((java.util.Date) creadoEn));
        } else {
            txtCreada.setText("No disponible");
        }
    }
}
