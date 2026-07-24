package gui;

import funcionalidades.PanelControlAdminDAO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

public class PanelControlAdmin extends JFrame {

    private Image fondo;
    private Image logo;

    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeTarjeta = new Color(25, 38, 35, 180);
    private final Color verdeCard = new Color(20, 32, 30, 190);

    // DAO con las consultas reales del panel de administración
    private final PanelControlAdminDAO dao = new PanelControlAdminDAO();

    // ==========================================
    // BOTÓN DE NAVEGACIÓN DEL SIDEBAR (CLONADO)
    // ==========================================
    class BotonSidebarNeo extends JButton {

        public BotonSidebarNeo(String texto) {
            super(texto);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setBackground(new Color(0, 0, 0, 0));
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    setBackground(amarilloPastel);
                    setForeground(Color.BLACK);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    setBackground(new Color(0, 0, 0, 0));
                    setForeground(Color.WHITE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            if (getBackground().getAlpha() == 0) {
                g2.setColor(amarilloPastel);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==========================================
    // BOTÓN NEO ACCIONES ESPECIALES (CLONADO)
    // ==========================================
    class BotonNeo extends JButton {

        private Color normal;
        private Color hover;
        private Color colorTexto;

        public BotonNeo(String texto, Color normal, Color hover, Color colorTexto) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            this.colorTexto = colorTexto;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(colorTexto);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public PanelControlAdmin() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("NeoJacket - Panel de Administración");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
        setVisible(true);
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);

            // ==========================================
            // SIDEBAR CON ACABADO NEO RENDERING (35px)
            // ==========================================
            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                    g2.dispose();
                }
            };
            sidebar.setOpaque(false);
            sidebar.setBounds(20, 20, 300, 870);
            sidebar.setLayout(null);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] opciones = {
                "Gestión de Usuarios",
                "Gestión de Menores",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;
            for (String texto : opciones) {
                BotonSidebarNeo btn = new BotonSidebarNeo(texto);
                btn.setBounds(20, y, 250, 55); // Ajustado a alto 55 como el Dashboard

                // Enrutamiento de interfaces
                if (texto.equals("Gestión de Usuarios")) {
                    btn.addActionListener(e -> {
                        new GestionUsuario();
                        dispose();
                    });
                }
                if (texto.equals("Gestión de Menores")) {
                    btn.addActionListener(e -> {
                        new GestionMenores();
                        dispose();
                    });
                }
                if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                }
                if (texto.equals("Gestión de Tarjetas")) {
                    btn.addActionListener(e -> {
                        new GestionTarjeta();
                        dispose();
                    });
                }
                if (texto.equals("Gestión de Divisas")) {
                    btn.addActionListener(e -> {
                        new GestionDivisas();
                        dispose();
                    });
                }
                if (texto.equals("Gestión de Transacciones")) {
                    btn.addActionListener(e -> {
                        new GestionTransacciones();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 78; // Salto vertical uniforme de 70px
            }

            // Botón Cerrar Sesión Estilo Neo
            BotonNeo btnCerrarSesion = new BotonNeo(
                    "Cerrar sesión",
                    new Color(191, 76, 58),
                    new Color(214, 100, 80),
                    Color.WHITE);
            btnCerrarSesion.setBounds(20, 800, 250, 55);
            btnCerrarSesion.addActionListener(e -> {
                JOptionPane.showMessageDialog(null,
                        "Sesión cerrada correctamente.\n¡Hasta pronto!",
                        "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            add(sidebar);

            // ======================
            // PANEL BIENVENIDA
            // ======================
            JPanel bienvenida = crearCard(340, 40, 950, 140);
            bienvenida.setLayout(null);

            JLabel titulo = new JLabel("BIENVENIDO");
            titulo.setForeground(amarilloPastel);
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Consistencia visual chico
            titulo.setBounds(30, 20, 200, 30);
            bienvenida.add(titulo);

            JLabel subtitulo = new JLabel("Panel de Administración");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
            subtitulo.setBounds(30, 45, 450, 40);
            bienvenida.add(subtitulo);

            JLabel desc = new JLabel("Resumen general de la plataforma");
            desc.setForeground(Color.LIGHT_GRAY);
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            desc.setBounds(30, 90, 350, 25);
            bienvenida.add(desc);

            add(bienvenida);

            // ======================
            // TARJETAS ESTADÍSTICAS (datos reales vía PanelControlAdminDAO)
            // ======================
            add(crearTarjeta("Usuarios", String.valueOf(dao.obtenerTotalUsuarios()), 340, 200));
            add(crearTarjeta("Cuentas", String.valueOf(dao.obtenerTotalCuentas()), 600, 200));
            add(crearTarjeta("Tarjetas", String.valueOf(dao.obtenerTotalTarjetas()), 860, 200));
            add(crearTarjeta("Menores", String.valueOf(dao.obtenerTotalMenores()), 1120, 200));
            add(crearTarjeta("Transacciones", String.valueOf(dao.obtenerTotalTransacciones()), 1380, 200));

            // ======================
            // PANEL ADMINISTRADOR INFO
            // ======================
            JPanel admin = crearCard(1310, 40, 380, 140);
            admin.setLayout(null);

            JLabel nombre = new JLabel("Administrador");
            nombre.setForeground(Color.WHITE);
            nombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
            nombre.setBounds(25, 30, 250, 30);
            admin.add(nombre);

            JLabel rol = new JLabel("Neo Jacket");
            rol.setForeground(amarilloPastel);
            rol.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            rol.setBounds(25, 65, 200, 25);
            admin.add(rol);

            add(admin);

            // ======================
            // GRAFICA DE BARRAS — Usuarios registrados por mes (últimos 5 meses)
            // Marco amarillo delgado
            // ======================
            JPanel grafica = crearCard(340, 360, 950, 410, amarilloPastel);
            grafica.setLayout(null);

            JLabel tituloGrafica = new JLabel("Usuarios Registrados");
            tituloGrafica.setForeground(Color.WHITE);
            tituloGrafica.setFont(new Font("Segoe UI", Font.BOLD, 22));
            tituloGrafica.setBounds(30, 20, 300, 30);
            grafica.add(tituloGrafica);

            List<PanelControlAdminDAO.DatoMes> datosMeses = dao.obtenerUsuariosPorMes(5);

            JPanel barras = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (datosMeses.isEmpty()) {
                        g2.dispose();
                        return;
                    }

                    int n = datosMeses.size();
                    int anchoBarra = 70;
                    int espacio = (getWidth() - n * anchoBarra) / (n + 1);
                    int baseY = 300;
                    int alturaMaxima = 280;

                    int maxTotal = 1;
                    for (PanelControlAdminDAO.DatoMes d : datosMeses) {
                        maxTotal = Math.max(maxTotal, d.total);
                    }

                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    FontMetrics fm = g2.getFontMetrics();

                    int x = espacio;
                    for (PanelControlAdminDAO.DatoMes d : datosMeses) {
                        int alturaBarra = d.total <= 0 ? 4 : Math.max(8, (int) ((d.total / (double) maxTotal) * alturaMaxima));

                        g2.setColor(amarilloPastel);
                        g2.fillRoundRect(x, baseY - alturaBarra, anchoBarra, alturaBarra, 15, 15);

                        // Total encima de la barra
                        g2.setColor(Color.WHITE);
                        String totalTexto = String.valueOf(d.total);
                        int anchoTexto = fm.stringWidth(totalTexto);
                        g2.drawString(totalTexto, x + (anchoBarra - anchoTexto) / 2, baseY - alturaBarra - 8);

                        // Etiqueta del mes debajo de la barra
                        g2.setColor(Color.LIGHT_GRAY);
                        int anchoEtiqueta = fm.stringWidth(d.etiqueta);
                        g2.drawString(d.etiqueta, x + (anchoBarra - anchoEtiqueta) / 2, baseY + 22);

                        x += anchoBarra + espacio;
                    }
                    g2.dispose();
                }
            };
            barras.setOpaque(false);
            barras.setBounds(20, 60, 900, 320);
            grafica.add(barras);

            add(grafica);

            // ======================
            // ACTIVIDAD RECIENTE (datos reales: login + transacciones + registros)
            // Marco amarillo delgado
            // ======================
            JPanel actividad = crearCard(1310, 360, 380, 410, amarilloPastel);
            actividad.setLayout(null);

            JLabel tituloActividad = new JLabel("Actividad Reciente");
            tituloActividad.setForeground(Color.WHITE);
            tituloActividad.setFont(new Font("Segoe UI", Font.BOLD, 22));
            tituloActividad.setBounds(20, 20, 250, 30);
            actividad.add(tituloActividad);

            List<PanelControlAdminDAO.ActividadItem> eventosRecientes = dao.obtenerActividadReciente(6);
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM HH:mm");

            if (eventosRecientes.isEmpty()) {
                JLabel sinActividad = new JLabel("Aún no hay actividad registrada.");
                sinActividad.setForeground(Color.LIGHT_GRAY);
                sinActividad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                sinActividad.setBounds(30, 75, 320, 25);
                actividad.add(sinActividad);
            } else {
                int yy = 70;
                for (PanelControlAdminDAO.ActividadItem item : eventosRecientes) {
                    JLabel lblTexto = new JLabel("• " + item.texto);
                    lblTexto.setForeground(Color.WHITE);
                    lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    lblTexto.setBounds(30, yy, 320, 22);
                    actividad.add(lblTexto);

                    JLabel lblFecha = new JLabel(item.fecha != null ? sdfHora.format(item.fecha) : "");
                    lblFecha.setForeground(new Color(150, 160, 150));
                    lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    lblFecha.setBounds(46, yy + 20, 300, 18);
                    actividad.add(lblFecha);

                    yy += 55;
                }
            }
            add(actividad);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /** Tarjeta/panel redondeado con borde blanco tenue (uso general). */
    private JPanel crearCard(int x, int y, int w, int h) {
        return crearCard(x, y, w, h, new Color(255, 255, 255, 40));
    }

    /** Tarjeta/panel redondeado con color de borde personalizado (ej. amarillo). */
    private JPanel crearCard(int x, int y, int w, int h, Color colorBorde) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 38, 35, 210));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBounds(x, y, w, h);
        return panel;
    }

    private JPanel crearTarjeta(String titulo, String valor, int x, int y) {
        JPanel card = crearCard(x, y, 230, 130); // Compactado un poco para evitar desbordes horizontales
        card.setLayout(null);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(20, 15, 180, 25);
        card.add(lblTitulo);

        JLabel lblValor = new JLabel(valor);
        lblValor.setForeground(amarilloPastel);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblValor.setBounds(20, 45, 180, 50);
        card.add(lblValor);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PanelControlAdmin();
        });
    }
}