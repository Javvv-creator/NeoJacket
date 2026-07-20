package gui;

import funcionalidades.HistorialController;
import funcionalidades.SesionUsuario;
import funcionalidades.SupervisionDAO;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;

public class Historial extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

    // TIPOGRAFÍAS DE LA IDENTIDAD VISUAL
    private final Font fontLabelChico = new Font("Segoe UI", Font.BOLD, 13);
    private final Font fontTituloGrande = new Font("Segoe UI", Font.BOLD, 30);
    private final Font fontSubtitulo = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 16);
    private final Font etiquetaCampos = new Font("Segoe UI", Font.BOLD, 13);
    private final Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font descripcionPantalla = new Font("Segoe UI", Font.PLAIN, 14);

    // PALETA DE COLORES (mismo lenguaje visual que el Dashboard)
    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeTarjeta = new Color(25, 38, 35, 180);
    private final Color verdeCard = new Color(20, 32, 30, 190);

    // --- COMPONENTES GLOBALES QUE RELLENA HistorialController / cargarDatosMenor ---
    // Bloque 1: Mis Cuentas
    public JLabel lblCtaPrincipal, lblCtaAhorros;

    // Bloque 2: Resumen Financiero (SIN Saldo Retenido, eliminado a petición)
    public JLabel lblSaldoDisponible, lblIngresosMes, lblGastosMes, lblUltimaActResumen;

    // Bloque 3: Bancos Conectados
    public JLabel lblBanco1, lblBanco2, lblBanco3;

    // Bloque 4: Transferencias Recientes
    public JLabel lblTransReciente1, lblTransReciente2, lblTransReciente3, lblTransReciente4;

    // Bloque 5: Cambio de Divisas
    public JLabel lblDivUSD, lblDivEUR, lblDivMXN, lblUltimaActDivisas;

    // Bloque 6: Historial de Actividad
    public JLabel lblHist1_Titulo, lblHist1_Meta, lblHist2_Titulo, lblHist2_Meta,
            lblHist3_Titulo, lblHist3_Meta, lblHist4_Titulo, lblHist4_Meta;

    private Integer idMenor;

    public Historial() {
        this(null);
    }

    public Historial(Integer idMenor) {
        this.idMenor = idMenor;
        initComponents();

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Historial de Actividad");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());

        // ==========================================
        // CARGA DE DATOS: menor supervisado vs. usuario adulto normal
        // ==========================================
        if (idMenor != null) {
            cargarDatosMenor();
        } else {
            // Antes esto nunca se llamaba, por eso todo se quedaba en "--"
            new HistorialController(this).cargarDatosPantalla();
        }
    }

    private void cargarDatosMenor() {
        SupervisionDAO dao = new SupervisionDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Transacciones recientes → lblTransReciente1-4
        List<SupervisionDAO.MovimientoCuenta> trans = dao.obtenerTransaccionesRecientes(idMenor, 4);
        JLabel[] lblsTrans = {lblTransReciente1, lblTransReciente2, lblTransReciente3, lblTransReciente4};
        for (int i = 0; i < lblsTrans.length; i++) {
            if (lblsTrans[i] == null) continue;
            if (i < trans.size()) {
                SupervisionDAO.MovimientoCuenta m = trans.get(i);
                lblsTrans[i].setText(String.format("%s  —  Q %.2f  [%s]  %s",
                        m.tipoTransaccion, m.monto, m.estado,
                        m.fecha != null ? sdf.format(m.fecha) : ""));
            } else {
                lblsTrans[i].setText("Sin movimientos registrados");
            }
        }

        // Sesiones recientes → lblHist1-4
        List<SupervisionDAO.EventoSesion> sesiones = dao.obtenerSesionesRecientes(idMenor, 4);
        JLabel[] lblsTit = {lblHist1_Titulo, lblHist2_Titulo, lblHist3_Titulo, lblHist4_Titulo};
        JLabel[] lblsMeta = {lblHist1_Meta, lblHist2_Meta, lblHist3_Meta, lblHist4_Meta};
        for (int i = 0; i < lblsTit.length; i++) {
            if (lblsTit[i] == null) continue;
            if (i < sesiones.size()) {
                SupervisionDAO.EventoSesion ev = sesiones.get(i);
                lblsTit[i].setText(ev.tipoEvento.replace("_", " ").toUpperCase());
                lblsMeta[i].setText(
                        (ev.dispositivo != null ? ev.dispositivo : "—") + "  •  "
                                + (ev.ocurridoEn != null ? sdf.format(ev.ocurridoEn) : ""));
            } else {
                lblsTit[i].setText("Sin accesos registrados");
                if (lblsMeta[i] != null) lblsMeta[i].setText("");
            }
        }

        // Saldo total → lblCtaPrincipal
        if (lblCtaPrincipal != null) {
            lblCtaPrincipal.setText(String.format("Saldo total: Q %.2f", dao.obtenerSaldoTotal(idMenor)));
        }
    }

    // ==========================================
    // BOTÓN DE ACCIÓN DEL SIDEBAR (Supervisión / Cerrar sesión / Regresar)
    // ==========================================
    class BotonAccionNeo extends JButton {
        private Color normal;
        private Color hover;

        public BotonAccionNeo(String texto, Color normal, Color hover, Color colorTexto) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
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

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar(this);
            crearContenido();
        }

        // ==========================================
        // SIDEBAR 
        // ==========================================
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
            sidebar.setBounds(20, 20, 300, 870);
            sidebar.setLayout(null);

            Color amarillo = amarilloPastel;
            Color fondoTransparente = new Color(0, 0, 0, 0);
            Color amarilloBorde = amarilloPastel;

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] opciones = {
                "Saldos",
                "Bancos Conectados",
                "Transferencias",
                "Historial",
                "Agregar Tarjeta"
            };

            int y = 140;

            for (String texto : opciones) {

                JButton btn = new JButton(texto) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                        if (getBackground() != amarillo) {
                            g2.setColor(amarilloBorde);
                            g2.setStroke(new BasicStroke(1f));
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                        }

                        g2.dispose();
                        super.paintComponent(g);
                    }
                };

                btn.setBounds(20, y, 250, 46);
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setOpaque(false);
                btn.setForeground(Color.WHITE);
                btn.setBackground(fondoTransparente);

                Font fuenteActual = btn.getFont();
                btn.setFont(new Font(fuenteActual.getName(), fuenteActual.getStyle(), fuenteActual.getSize() + 2));

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        btn.setBackground(amarillo);
                        btn.setForeground(Color.BLACK);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        btn.setBackground(fondoTransparente);
                        btn.setForeground(Color.WHITE);
                    }
                });

                if (texto.equals("Saldos")) {
                    btn.addActionListener(e -> {
                        new AgregarFondos().setVisible(true);
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
                        new Transferencias(idMenor).setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Agregar Tarjeta")) {
                    btn.addActionListener(e -> {
                        int idUsuario = SesionUsuario.getIdUsuario();
                        new DetalleTarjetaDasboard(idUsuario);
                        dispose();
                    });
                }
                // "Historial" no lleva listener: ya estamos en esta pantalla

                sidebar.add(btn);
                y += 68;
            }

            // Botón Supervisión — aparece justo debajo del último botón del
            // menú, solo si el usuario tiene menores a cargo.
            SupervisionDAO daoSup = new SupervisionDAO();
            int idSesion = SesionUsuario.getIdUsuario();
            if (idSesion > 0 && daoSup.tieneMenoresACargo(idSesion)) {
                BotonAccionNeo btnSupervision = new BotonAccionNeo(
                        "Supervisión",
                        amarilloPastel,
                        new Color(255, 245, 180),
                        new Color(25, 38, 35));
                btnSupervision.setBounds(20, y, 250, 55);
                btnSupervision.addActionListener(e -> {
                    new PanelSupervision(idSesion).setVisible(true);
                    dispose();
                });
                sidebar.add(btnSupervision);
            }

            // Botón Regresar al Dashboard — ahora en el sidebar, arriba de Cerrar sesión
            BotonAccionNeo btnRegresarDashboard = new BotonAccionNeo(
                    "← Regresar al Dashboard",
                    new Color(94, 116, 73, 220),
                    new Color(120, 150, 90),
                    Color.WHITE);
            btnRegresarDashboard.setBounds(20, 730, 250, 55);
            btnRegresarDashboard.addActionListener(e -> {
                new Dashboard(SesionUsuario.getIdUsuario()).setVisible(true);
                dispose();
            });
            sidebar.add(btnRegresarDashboard);

            BotonAccionNeo btnCerrarSesion = new BotonAccionNeo(
                    "Cerrar sesión",
                    new Color(191, 76, 58),
                    new Color(214, 100, 80),
                    Color.WHITE);
            btnCerrarSesion.setBounds(20, 800, 250, 55);
            btnCerrarSesion.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            panel.add(sidebar);
        }

        // ==========================================
        // CONTENIDO PRINCIPAL (estética tipo Dashboard: paneles redondeados
        // con marco amarillo delgado solo en los paneles más importantes)
        // ==========================================
        private void crearContenido() {
            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setOpaque(false);
            contenedor.setBounds(350, 20, 1300, 950);
            add(contenedor);


            // ---------------------------------------------------
            // HEADER: "BIENVENIDO / Historial de Actividad"
            // ---------------------------------------------------
            RoundedPanel bienvenida = crearPanel(30, 20, 1240, 100, verdeTarjeta, null);
            contenedor.add(bienvenida);

            JLabel lblBienvenido = new JLabel("BIENVENIDO");
            lblBienvenido.setForeground(amarilloPastel);
            lblBienvenido.setFont(fontLabelChico);
            lblBienvenido.setBounds(30, 15, 400, 20);
            bienvenida.add(lblBienvenido);

            JLabel lblTitulo = new JLabel("Historial de Actividad");
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setFont(fontTituloGrande);
            lblTitulo.setBounds(30, 36, 600, 40);
            bienvenida.add(lblTitulo);

            JLabel lblDesc = new JLabel("Consulta el registro completo de movimientos, transferencias y estados de cuenta.");
            lblDesc.setForeground(Color.LIGHT_GRAY);
            lblDesc.setFont(fontSubtitulo);
            lblDesc.setBounds(30, 76, 900, 20);
            bienvenida.add(lblDesc);

            int col1X = 30;
            int col2X = 650;
            int anchoBloque = 600;

            // =================================================================
            // FILA 1: MIS CUENTAS 
            // =================================================================
            int row1Y = 140, row1H = 210;

            RoundedPanel pMisCuentas = crearPanel(col1X, row1Y, anchoBloque, row1H, verdeTarjeta, null);
            contenedor.add(pMisCuentas);

            addTitulo(pMisCuentas, "CUENTAS");

            JLabel lblTagPrincipal = crearEtiqueta("Principal", 25, 50);
            pMisCuentas.add(lblTagPrincipal);

            lblCtaPrincipal = crearValor("--", 25, 73, 550);
            pMisCuentas.add(lblCtaPrincipal);

            JLabel lblTagAhorros = crearEtiqueta("Ahorros", 25, 115);
            pMisCuentas.add(lblTagAhorros);

            lblCtaAhorros = crearValor("--", 25, 138, 550);
            pMisCuentas.add(lblCtaAhorros);

            // -----------------------------------------------------------------
            RoundedPanel pResumenFin = crearPanel(col2X, row1Y, anchoBloque, row1H, verdeCard, amarilloPastel);
            contenedor.add(pResumenFin);

            addTitulo(pResumenFin, "RESUMEN FINANCIERO");

            lblSaldoDisponible = crearValor("Saldo disponible: --", 25, 52, 550);
            pResumenFin.add(lblSaldoDisponible);

            lblIngresosMes = new JLabel("Ingresos del mes: --");
            lblIngresosMes.setForeground(new Color(130, 220, 130));
            lblIngresosMes.setFont(textoInputs);
            lblIngresosMes.setBounds(25, 84, 550, 22);
            pResumenFin.add(lblIngresosMes);

            lblGastosMes = new JLabel("Gastos del mes: --");
            lblGastosMes.setForeground(new Color(240, 120, 120));
            lblGastosMes.setFont(textoInputs);
            lblGastosMes.setBounds(25, 116, 550, 22);
            pResumenFin.add(lblGastosMes);

            lblUltimaActResumen = new JLabel("Última actualización: --");
            lblUltimaActResumen.setForeground(new Color(150, 160, 150));
            lblUltimaActResumen.setFont(descripcionPantalla);
            lblUltimaActResumen.setBounds(25, 150, 550, 22);
            pResumenFin.add(lblUltimaActResumen);

            // =================================================================
            // FILA 2: BANCOS CONECTADOS (IZQ)  vs  TRANSFERENCIAS RECIENTES (DER)
            // =================================================================
            int row2Y = row1Y + row1H + 20, row2H = 190;

            RoundedPanel pBancosCon = crearPanel(col1X, row2Y, anchoBloque, row2H, verdeTarjeta, null);
            contenedor.add(pBancosCon);

            addTitulo(pBancosCon, "BANCOS CONECTADOS");

            lblBanco1 = crearValor("✔ --", 25, 52, 550);
            pBancosCon.add(lblBanco1);
            lblBanco2 = crearValor("✔ --", 25, 84, 550);
            pBancosCon.add(lblBanco2);
            lblBanco3 = crearValor("✔ --", 25, 116, 550);
            pBancosCon.add(lblBanco3);

            // -----------------------------------------------------------------
            RoundedPanel pTransRec = crearPanel(col2X, row2Y, anchoBloque, row2H, verdeTarjeta, null);
            contenedor.add(pTransRec);

            addTitulo(pTransRec, "TRANSFERENCIAS RECIENTES");

            lblTransReciente1 = crearValor("--", 25, 52, 550);
            pTransRec.add(lblTransReciente1);
            lblTransReciente2 = crearValor("--", 25, 80, 550);
            pTransRec.add(lblTransReciente2);
            lblTransReciente3 = crearValor("--", 25, 108, 550);
            pTransRec.add(lblTransReciente3);
            lblTransReciente4 = crearValor("--", 25, 136, 550);
            pTransRec.add(lblTransReciente4);

            // =================================================================
            // FILA 3: CAMBIO DE DIVISAS (IZQ)  vs  HISTORIAL DE ACTIVIDAD (DER, marco dorado)
            // =================================================================
            int row3Y = row2Y + row2H + 20, row3H = 300;

            RoundedPanel pCambioDiv = crearPanel(col1X, row3Y, anchoBloque, row3H, verdeTarjeta, null);
            contenedor.add(pCambioDiv);

            addTitulo(pCambioDiv, "ÚLTIMO CAMBIO DE DIVISAS");

            lblDivUSD = crearValor("Moneda origen:   --", 25, 55, 550);
            pCambioDiv.add(lblDivUSD);
            lblDivEUR = crearValor("Moneda destino:  --", 25, 95, 550);
            pCambioDiv.add(lblDivEUR);
            lblDivMXN = crearValor("Tasa aplicada:   --", 25, 135, 550);
            pCambioDiv.add(lblDivMXN);

            lblUltimaActDivisas = new JLabel("Actualizado: --");
            lblUltimaActDivisas.setForeground(new Color(150, 160, 150));
            lblUltimaActDivisas.setFont(descripcionPantalla);
            lblUltimaActDivisas.setBounds(25, 255, 550, 22);
            pCambioDiv.add(lblUltimaActDivisas);

            // -----------------------------------------------------------------
            RoundedPanel pHistorialAct = crearPanel(col2X, row3Y, anchoBloque, row3H, verdeCard, amarilloPastel);
            contenedor.add(pHistorialAct);

            addTitulo(pHistorialAct, "HISTORIAL DE ACTIVIDAD");

            lblHist1_Titulo = crearValor("--", 25, 50, 550);
            pHistorialAct.add(lblHist1_Titulo);
            lblHist1_Meta = crearMeta("--", 25, 72);
            pHistorialAct.add(lblHist1_Meta);

            lblHist2_Titulo = crearValor("--", 25, 105, 550);
            pHistorialAct.add(lblHist2_Titulo);
            lblHist2_Meta = crearMeta("--", 25, 127);
            pHistorialAct.add(lblHist2_Meta);

            lblHist3_Titulo = crearValor("--", 25, 160, 550);
            pHistorialAct.add(lblHist3_Titulo);
            lblHist3_Meta = crearMeta("--", 25, 182);
            pHistorialAct.add(lblHist3_Meta);

            lblHist4_Titulo = crearValor("--", 25, 215, 550);
            pHistorialAct.add(lblHist4_Titulo);
            lblHist4_Meta = crearMeta("--", 25, 237);
            pHistorialAct.add(lblHist4_Meta);
        }

        // ==========================================
        // HELPERS DE CONSTRUCCIÓN DE UI
        // ==========================================
        private void addTitulo(JPanel panel, String texto) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(amarilloPastel);
            lbl.setFont(tituloSeccion);
            lbl.setBounds(25, 15, 400, 22);
            panel.add(lbl);
        }

        private JLabel crearEtiqueta(String texto, int x, int y) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(new Color(180, 190, 180));
            lbl.setFont(etiquetaCampos);
            lbl.setBounds(x, y, 200, 20);
            return lbl;
        }

        private JLabel crearValor(String texto, int x, int y, int ancho) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(textoInputs);
            lbl.setBounds(x, y, ancho, 22);
            return lbl;
        }

        private JLabel crearMeta(String texto, int x, int y) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(new Color(150, 160, 150));
            lbl.setFont(descripcionPantalla);
            lbl.setBounds(x, y, 550, 20);
            return lbl;
        }

        // Panel redondeado con o sin marco amarillo delgado (mismo helper que en Dashboard)
        private RoundedPanel crearPanel(int x, int y, int w, int h, Color fondoColor, Color colorBorde) {
            RoundedPanel panel = new RoundedPanel();
            panel.setBounds(x, y, w, h);
            panel.setBackground(fondoColor);
            panel.setLayout(null);
            if (colorBorde != null) {
                panel.setBorder(BorderFactory.createLineBorder(colorBorde, 1, true));
            } else {
                panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1, true));
            }
            return panel;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ==========================================
    // PANEL REDONDEADO (idéntico al usado en Dashboard.java)
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Historial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new Historial().setVisible(true);
        });
    }
}