package gui;

import funcionalidades.SupervisionDAO;
import funcionalidades.SupervisionDAO.MenorSupervisado;
import funcionalidades.SupervisionDAO.MovimientoCuenta;
import funcionalidades.SupervisionDAO.EventoSesion;
import funcionalidades.SupervisionDAO.SolicitudPermiso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Pantalla de Supervisión para tutores (perfil Adulto) que tienen una o más
 * cuentas de Menor supervisado a su cargo.
 * Muestra, con datos reales de la base de datos:
 * - Lista de menores supervisados
 * - Saldo total del menor seleccionado
 * - Transacciones recientes del menor
 * - Historial de sesiones (accesos) del menor
 * - Solicitudes de permiso pendientes
 */
public class PanelSupervision extends JFrame {

    private final int idTutor;
    private Image fondo;
    private Image logo;

    private final SupervisionDAO supervisionDAO = new SupervisionDAO();

    private JPanel contenedorDerecho;
    private JLabel lblNombreMenorSel;
    private JLabel lblSaldoMenorSel;
    private JPanel listaTransaccionesPanel;
    private JPanel listaSesionesPanel;
    private JPanel listaSolicitudesPanel; // Atributo agregado para refresco dinámico

    private Integer idMenorSeleccionado = null;

    // ==========================================
    // BOTÓN NEO (mismo estilo que el resto del sistema)
    // ==========================================
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

    // ==========================================
    // PANEL CON BORDES REDONDEADOS
    // ==========================================
    class RoundedPanel extends JPanel {
        public RoundedPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public PanelSupervision(int idTutor) {
        this.idTutor = idTutor;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Supervisión de Cuenta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

    // ==========================================
    // PANEL PRINCIPAL
    // ==========================================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
            cargarMenores();
        }

        private JPanel sidebarListaMenores;

        // ==========================================
        // SIDEBAR: logo + lista de menores a cargo
        // ==========================================
        private void crearSidebar() {
            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            JLabel lblTitulo = new JLabel("Menores a tu cargo");
            lblTitulo.setForeground(new Color(251, 232, 138));
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitulo.setBounds(20, 130, 260, 25);
            sidebar.add(lblTitulo);

            // Panel scrolleable donde se listarán los botones de cada menor
            sidebarListaMenores = new JPanel();
            sidebarListaMenores.setLayout(new BoxLayout(sidebarListaMenores, BoxLayout.Y_AXIS));
            sidebarListaMenores.setOpaque(false);

            JScrollPane scroll = new JScrollPane(sidebarListaMenores);
            scroll.setBounds(15, 165, 270, 650);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            sidebar.add(scroll);

            BotonNeo btnVolver = new BotonNeo("← Volver al Dashboard");
            btnVolver.setBounds(20, 870, 260, 50);
            btnVolver.addActionListener(e -> {
                new Dashboard(idTutor).setVisible(true);
                dispose();
            });
            sidebar.add(btnVolver);

            add(sidebar);
        }

        // ==========================================
        // CONTENIDO PRINCIPAL (detalle del menor)
        // ==========================================
        private void crearContenido() {
            contenedorDerecho = new JPanel();
            contenedorDerecho.setLayout(null);
            contenedorDerecho.setBackground(new Color(25, 38, 35, 150));
            contenedorDerecho.setBounds(350, 20, 1250, 950);
            add(contenedorDerecho);

            Color amarilloPastel = new Color(251, 232, 138);
            Font tituloFont = new Font("Segoe UI", Font.BOLD, 18);

            JPanel barraSuperior = new JPanel();
            barraSuperior.setLayout(null);
            barraSuperior.setBounds(0, 0, 1250, 60);
            barraSuperior.setBackground(amarilloPastel);
            contenedorDerecho.add(barraSuperior);

            lblNombreMenorSel = new JLabel("Selecciona un menor en el panel izquierdo");
            lblNombreMenorSel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblNombreMenorSel.setForeground(new Color(25, 38, 35));
            lblNombreMenorSel.setBounds(20, 0, 900, 60);
            barraSuperior.add(lblNombreMenorSel);

            // TARJETA SALDO
            RoundedPanel tarjetaSaldo = new RoundedPanel();
            tarjetaSaldo.setBounds(40, 90, 1160, 110);
            tarjetaSaldo.setBackground(new Color(25, 38, 35, 180));
            tarjetaSaldo.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            tarjetaSaldo.setLayout(null);
            contenedorDerecho.add(tarjetaSaldo);

            JLabel lblTituloSaldo = new JLabel("Saldo total del menor");
            lblTituloSaldo.setForeground(amarilloPastel);
            lblTituloSaldo.setFont(tituloFont);
            lblTituloSaldo.setBounds(20, 10, 300, 30);
            tarjetaSaldo.add(lblTituloSaldo);

            lblSaldoMenorSel = new JLabel("Q0.00");
            lblSaldoMenorSel.setForeground(Color.WHITE);
            lblSaldoMenorSel.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblSaldoMenorSel.setBounds(20, 45, 400, 40);
            tarjetaSaldo.add(lblSaldoMenorSel);

            // TARJETA TRANSACCIONES
            RoundedPanel tarjetaTrans = new RoundedPanel();
            tarjetaTrans.setBounds(40, 220, 560, 400);
            tarjetaTrans.setBackground(new Color(25, 38, 35, 180));
            tarjetaTrans.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            tarjetaTrans.setLayout(new BorderLayout());
            contenedorDerecho.add(tarjetaTrans);

            JLabel lblTituloTrans = new JLabel("  Transacciones recientes");
            lblTituloTrans.setForeground(amarilloPastel);
            lblTituloTrans.setFont(tituloFont);
            lblTituloTrans.setBorder(new EmptyBorder(15, 5, 10, 5));
            tarjetaTrans.add(lblTituloTrans, BorderLayout.NORTH);

            listaTransaccionesPanel = new JPanel();
            listaTransaccionesPanel.setLayout(new BoxLayout(listaTransaccionesPanel, BoxLayout.Y_AXIS));
            listaTransaccionesPanel.setOpaque(false);
            listaTransaccionesPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

            JScrollPane scrollTrans = new JScrollPane(listaTransaccionesPanel);
            scrollTrans.setOpaque(false);
            scrollTrans.getViewport().setOpaque(false);
            scrollTrans.setBorder(null);
            scrollTrans.getVerticalScrollBar().setUnitIncrement(16);
            tarjetaTrans.add(scrollTrans, BorderLayout.CENTER);

            // TARJETA SESIONES / HISTORIAL DE ACCESO
            RoundedPanel tarjetaSesiones = new RoundedPanel();
            tarjetaSesiones.setBounds(640, 220, 560, 400);
            tarjetaSesiones.setBackground(new Color(25, 38, 35, 180));
            tarjetaSesiones.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            tarjetaSesiones.setLayout(new BorderLayout());
            contenedorDerecho.add(tarjetaSesiones);

            JLabel lblTituloSesiones = new JLabel("  Historial de accesos");
            lblTituloSesiones.setForeground(amarilloPastel);
            lblTituloSesiones.setFont(tituloFont);
            lblTituloSesiones.setBorder(new EmptyBorder(15, 5, 10, 5));
            tarjetaSesiones.add(lblTituloSesiones, BorderLayout.NORTH);

            listaSesionesPanel = new JPanel();
            listaSesionesPanel.setLayout(new BoxLayout(listaSesionesPanel, BoxLayout.Y_AXIS));
            listaSesionesPanel.setOpaque(false);
            listaSesionesPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

            JScrollPane scrollSesiones = new JScrollPane(listaSesionesPanel);
            scrollSesiones.setOpaque(false);
            scrollSesiones.getViewport().setOpaque(false);
            scrollSesiones.setBorder(null);
            scrollSesiones.getVerticalScrollBar().setUnitIncrement(16);
            tarjetaSesiones.add(scrollSesiones, BorderLayout.CENTER);

            // ============================================
            // TARJETA SOLICITUDES PENDIENTES
            // ============================================
            RoundedPanel tarjetaSolicitudes = new RoundedPanel();
            tarjetaSolicitudes.setBounds(40, 640, 1160, 260);
            tarjetaSolicitudes.setBackground(new Color(25, 38, 35, 180));
            tarjetaSolicitudes.setBorder(BorderFactory.createLineBorder(amarilloPastel, 1, true));
            tarjetaSolicitudes.setLayout(new BorderLayout());
            contenedorDerecho.add(tarjetaSolicitudes);

            JLabel lblTituloSol = new JLabel("  Solicitudes de permiso pendientes");
            lblTituloSol.setForeground(amarilloPastel);
            lblTituloSol.setFont(tituloFont);
            lblTituloSol.setBorder(BorderFactory.createEmptyBorder(12, 5, 8, 5));
            tarjetaSolicitudes.add(lblTituloSol, BorderLayout.NORTH);

            listaSolicitudesPanel = new JPanel();
            listaSolicitudesPanel.setLayout(new BoxLayout(listaSolicitudesPanel, BoxLayout.Y_AXIS));
            listaSolicitudesPanel.setOpaque(false);
            listaSolicitudesPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

            JScrollPane scrollSol = new JScrollPane(listaSolicitudesPanel);
            scrollSol.setOpaque(false);
            scrollSol.getViewport().setOpaque(false);
            scrollSol.setBorder(null);
            scrollSol.getVerticalScrollBar().setUnitIncrement(16);
            tarjetaSolicitudes.add(scrollSol, BorderLayout.CENTER);
        }

        // ==========================================
        // CARGA LA LISTA DE MENORES EN EL SIDEBAR
        // ==========================================
        private void cargarMenores() {
            List<MenorSupervisado> menores = supervisionDAO.listarMenoresDeTutor(idTutor);

            if (menores.isEmpty()) {
                JLabel lblVacio = new JLabel("<html>No tienes menores<br>supervisados todavía.</html>");
                lblVacio.setForeground(new Color(200, 200, 200));
                lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lblVacio.setBorder(new EmptyBorder(10, 5, 10, 5));
                sidebarListaMenores.add(lblVacio);
                return;
            }

            for (MenorSupervisado menor : menores) {
                JButton btnMenor = new JButton(menor.getNombreCompleto());
                btnMenor.setAlignmentX(Component.LEFT_ALIGNMENT);
                btnMenor.setMaximumSize(new Dimension(260, 50));
                btnMenor.setBackground(new Color(94, 116, 73));
                btnMenor.setForeground(Color.WHITE);
                btnMenor.setFocusPainted(false);
                btnMenor.setBorderPainted(false);
                btnMenor.setCursor(new Cursor(Cursor.HAND_CURSOR));

                btnMenor.addActionListener(e -> mostrarDetalleMenor(menor));

                sidebarListaMenores.add(btnMenor);
                sidebarListaMenores.add(Box.createVerticalStrut(8));
            }

            // Selecciona automáticamente el primero para no dejar la pantalla vacía
            mostrarDetalleMenor(menores.get(0));
        }

        // ==========================================
        // MUESTRA SALDO + TRANSACCIONES + SESIONES + SOLICITUDES DEL MENOR ELEGIDO
        // ==========================================
        private void mostrarDetalleMenor(MenorSupervisado menor) {
            idMenorSeleccionado = menor.idUsuario;

            lblNombreMenorSel.setText("Supervisando a: " + menor.getNombreCompleto() + "  (" + menor.correo + ")");

            double saldo = supervisionDAO.obtenerSaldoTotal(menor.idUsuario);
            lblSaldoMenorSel.setText(String.format("Q%,.2f", saldo));

            cargarTransacciones(menor.idUsuario);
            cargarSesiones(menor.idUsuario);
            cargarSolicitudes(menor.idUsuario); // Invocación agregada para sincronizar datos
        }

        private void cargarTransacciones(int idMenor) {
            listaTransaccionesPanel.removeAll();

            List<MovimientoCuenta> transacciones = supervisionDAO.obtenerTransaccionesRecientes(idMenor, 20);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            if (transacciones.isEmpty()) {
                JLabel lblVacio = new JLabel("Sin transacciones registradas todavía.");
                lblVacio.setForeground(new Color(200, 200, 200));
                lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                listaTransaccionesPanel.add(lblVacio);
            } else {
                for (MovimientoCuenta mov : transacciones) {
                    String fechaTexto = mov.fecha != null ? sdf.format(mov.fecha) : "Sin fecha";
                    String texto = String.format("<html>%s — %.2f %s<br><span style='color:#cccccc;font-size:11px;'>%s · %s</span></html>",
                            capitalizar(mov.tipoTransaccion), mov.monto, mov.moneda, mov.estado, fechaTexto);

                    JLabel lbl = new JLabel(texto);
                    lbl.setForeground(Color.WHITE);
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    lbl.setBorder(new EmptyBorder(8, 0, 8, 0));
                    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                    listaTransaccionesPanel.add(lbl);
                }
            }

            listaTransaccionesPanel.revalidate();
            listaTransaccionesPanel.repaint();
        }

        private void cargarSesiones(int idMenor) {
            listaSesionesPanel.removeAll();

            List<EventoSesion> sesiones = supervisionDAO.obtenerSesionesRecientes(idMenor, 20);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            if (sesiones.isEmpty()) {
                JLabel lblVacio = new JLabel("Sin actividad de inicio de sesión registrada.");
                lblVacio.setForeground(new Color(200, 200, 200));
                lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                listaSesionesPanel.add(lblVacio);
            } else {
                for (EventoSesion ev : sesiones) {
                    String fechaTexto = ev.ocurridoEn != null ? sdf.format(ev.ocurridoEn) : "Sin fecha";
                    String dispositivo = (ev.dispositivo != null && !ev.dispositivo.isEmpty()) ? ev.dispositivo : "Dispositivo no registrado";
                    String texto = String.format("<html>%s<br><span style='color:#cccccc;font-size:11px;'>%s · %s</span></html>",
                            capitalizar(ev.tipoEvento.replace("_", " ")), dispositivo, fechaTexto);

                    JLabel lbl = new JLabel(texto);
                    lbl.setForeground(Color.WHITE);
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    lbl.setBorder(new EmptyBorder(8, 0, 8, 0));
                    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                    listaSesionesPanel.add(lbl);
                }
            }

            listaSesionesPanel.revalidate();
            listaSesionesPanel.repaint();
        }

        // ==========================================
        // CARGA LAS SOLICITUDES FILTRADAS POR EL MENOR SELECCIONADO
        // ==========================================
        private void cargarSolicitudes(int idMenor) {
            listaSolicitudesPanel.removeAll();

            SupervisionDAO daoSol = new SupervisionDAO();
            // CORREGIDO: filtra por idTutor (adulto) y luego filtra por idMenor seleccionado
            List<SolicitudPermiso> todasSolicitudes = daoSol.listarSolicitudesPendientes(idTutor);
            List<SolicitudPermiso> solicitudes = new java.util.ArrayList<>();
            for (SolicitudPermiso s : todasSolicitudes) {
                if (s.idMenor == idMenor) solicitudes.add(s);
            }
            SimpleDateFormat sdfSol = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            if (solicitudes.isEmpty()) {
                JLabel lblSinSol = new JLabel("No hay solicitudes pendientes para este menor.");
                lblSinSol.setForeground(new Color(200, 200, 200));
                lblSinSol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lblSinSol.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                listaSolicitudesPanel.add(lblSinSol);
            } else {
                for (SolicitudPermiso sol : solicitudes) {
                    JPanel filaSol = new JPanel(null);
                    filaSol.setOpaque(false);
                    filaSol.setMaximumSize(new Dimension(1120, 45));
                    filaSol.setPreferredSize(new Dimension(1120, 45));
                    filaSol.setAlignmentX(Component.LEFT_ALIGNMENT);

                    String fechaSol = sol.creadoEn != null ? sdfSol.format(sol.creadoEn) : "Sin fecha";
                    String textoSol = String.format(
                        "<html><b>%s</b> — %s%s · %s</html>",
                        sol.tipoAccion,
                        sol.descripcion,
                        sol.monto > 0 ? String.format(" (Q%.2f)", sol.monto) : "",
                        fechaSol);

                    JLabel lblSol = new JLabel(textoSol);
                    lblSol.setForeground(Color.WHITE);
                    lblSol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    lblSol.setBounds(0, 5, 750, 35);
                    filaSol.add(lblSol);

                    // Botón APROBAR
                    JButton btnAprobar = new JButton("✔ Aprobar") {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(94, 160, 90));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                            g2.dispose(); super.paintComponent(g);
                        }
                    };
                    btnAprobar.setBounds(760, 5, 140, 32);
                    btnAprobar.setForeground(Color.WHITE);
                    btnAprobar.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    btnAprobar.setFocusPainted(false);
                    btnAprobar.setContentAreaFilled(false);
                    btnAprobar.setBorderPainted(false);
                    btnAprobar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnAprobar.addActionListener(e -> {
                        daoSol.responderSolicitud(sol.idSolicitud, "aprobada");
                        JOptionPane.showMessageDialog(null, "✅ Solicitud aprobada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarSolicitudes(idMenor); // Recarga el flujo visual inmediatamente
                    });
                    filaSol.add(btnAprobar);

                    // Botón RECHAZAR
                    JButton btnRechazar = new JButton("✖ Rechazar") {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(180, 60, 60));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                            g2.dispose(); super.paintComponent(g);
                        }
                    };
                    btnRechazar.setBounds(910, 5, 140, 32);
                    btnRechazar.setForeground(Color.WHITE);
                    btnRechazar.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    btnRechazar.setFocusPainted(false);
                    btnRechazar.setContentAreaFilled(false);
                    btnRechazar.setBorderPainted(false);
                    btnRechazar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnRechazar.addActionListener(e -> {
                        daoSol.responderSolicitud(sol.idSolicitud, "rechazada");
                        JOptionPane.showMessageDialog(null, "❌ Solicitud rechazada.", "Rechazada", JOptionPane.INFORMATION_MESSAGE);
                        cargarSolicitudes(idMenor); // Recarga el flujo visual inmediatamente
                    });
                    filaSol.add(btnRechazar);

                    listaSolicitudesPanel.add(filaSol);
                    listaSolicitudesPanel.add(Box.createVerticalStrut(5));
                }
            }
            listaSolicitudesPanel.revalidate();
            listaSolicitudesPanel.repaint();
        }

        private String capitalizar(String texto) {
            if (texto == null || texto.isEmpty()) return texto;
            return texto.substring(0, 1).toUpperCase() + texto.substring(1);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}