package gui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import funcionalidades.SupervisionDAO;

public class HistorialMenor extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;

    // TIPOGRAFÍAS DE LA IDENTIDAD VISUAL
    private final Font tituloPantalla = new Font("Segoe UI", Font.BOLD, 34);
    private final Font descripcionPantalla = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 16);
    private final Font etiquetaCampos = new Font("Segoe UI", Font.BOLD, 13);
    private final Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);
    
    // PALETA DE COLORES CORPORATIVA (VERDE OSCURO Y AMARILLO)
    private final Color verdeCorporativoFondo = new Color(20, 32, 29, 215); 
    private final Color amarilloPastel = new Color(251, 232, 138);

    // --- COMPONENTES GLOBALES CON CAMPOS EN "--" PARA CONTROLADOR DE HISTORIAL ---
    // Bloque 1: Mis Cuentas (Resumen rápido en Historial)
    public JLabel lblCtaPrincipal, lblCtaAhorros;
    
    // Bloque 2: Resumen Financiero
    public JLabel lblSaldoDisponible, lblSaldoRetenido, lblIngresosMes, lblGastosMes, lblUltimaActResumen;

    // Bloque 3: Bancos Conectados
    public JLabel lblBanco1, lblBanco2, lblBanco3;

    // Bloque 4: Transferencias Recientes
    public JLabel lblTransReciente1, lblTransReciente2, lblTransReciente3, lblTransReciente4;

    // Bloque 5: Cambio de Divisas
    public JLabel lblDivUSD, lblDivEUR, lblDivMXN, lblUltimaActDivisas;

    // Bloque 6: Historial de Actividad (El foco de esta pantalla)
    public JLabel lblHist1_Titulo, lblHist1_Meta, lblHist2_Titulo, lblHist2_Meta, 
                  lblHist3_Titulo, lblHist3_Meta, lblHist4_Titulo, lblHist4_Meta;

    private Integer idMenor;

    public HistorialMenor() {
        this(null);
    }

    public HistorialMenor(Integer idMenor) {
        this.idMenor = idMenor;
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Historial de Actividad");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new FondoPanel());

        // Cargar datos de BD si es menor supervisado
        if (idMenor != null) cargarDatosMenor();
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
                    (ev.dispositivo != null ? ev.dispositivo : "—") + "  •  " +
                    (ev.ocurridoEn != null ? sdf.format(ev.ocurridoEn) : ""));
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

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar(this);
            crearContenido();
        }

       private void crearSidebar(JPanel panel) {
    JPanel sidebar = new JPanel();
    sidebar.setLayout(null);
    sidebar.setBackground(new Color(25, 38, 35, 220));
    sidebar.setBounds(20, 20, 300, 870);

    Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
    JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
    lblLogo.setBounds(20, 10, 250, 110);
    sidebar.add(lblLogo);

    JLabel lblAviso = new JLabel("Cuenta supervisada");
    lblAviso.setForeground(new Color(251, 232, 138));
    lblAviso.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lblAviso.setHorizontalAlignment(SwingConstants.CENTER);
    lblAviso.setBounds(20, 122, 250, 18);
    sidebar.add(lblAviso);

    String[] botones = {"Transferencias", "Divisas", "Historial"};
    int y = 150;
    for (String nombre : botones) {
        JButton btn = new JButton(nombre);
        btn.setBounds(20, y, 250, 55);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (nombre.equals("Historial")) {
            btn.setBackground(new Color(251, 232, 138));
            btn.setForeground(Color.BLACK);
        } else {
            btn.setBackground(new Color(94, 116, 73));
            btn.setForeground(Color.WHITE);
        }
        btn.addActionListener(e -> {
            if (nombre.equals("Transferencias")) { new TransferenciasMenor(idMenor).setVisible(true); dispose(); }
            if (nombre.equals("Divisas"))        { new DivisasMenor(idMenor).setVisible(true); dispose(); }
        });
        sidebar.add(btn);
        y += 70;
    }

    JButton btnRegresar = new JButton("← Regresar") {
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(60, 60, 60));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose(); super.paintComponent(g);
        }
    };
    btnRegresar.setBounds(20, 760, 250, 50);
    btnRegresar.setForeground(Color.WHITE);
    btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
    btnRegresar.setFocusPainted(false);
    btnRegresar.setContentAreaFilled(false);
    btnRegresar.setBorderPainted(false);
    btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnRegresar.addActionListener(e -> { new DashboardMenor(idMenor).setVisible(true); dispose(); });
    sidebar.add(btnRegresar);

    panel.add(sidebar);
}

        private void crearContenido() {
            PanelContenedorVerde contenedor = new PanelContenedorVerde();
            contenedor.setBounds(350, 40, 1300, 910);
            add(contenedor);

            JLabel lblTitulo = new JLabel("HISTORIAL DE ACTIVIDAD");
            lblTitulo.setFont(tituloPantalla);
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setBounds(40, 25, 600, 45);
            contenedor.add(lblTitulo);

            JLabel lblDesc = new JLabel("Consulta el registro completo de movimientos, transferencias y estados de cuenta.");
            lblDesc.setFont(descripcionPantalla);
            lblDesc.setForeground(new Color(230, 235, 230));
            lblDesc.setBounds(40, 68, 1000, 25);
            contenedor.add(lblDesc);

            int col1X = 40;
            int col2X = 660;
            int anchoBloque = 600;

            // =================================================================
            // FILA 1: MIS CUENTAS (IZQ) vs RESUMEN FINANCIERO (DER)
            // =================================================================
            PanelBloqueEstilizado pMisCuentas = new PanelBloqueEstilizado();
            pMisCuentas.setBounds(col1X, 110, anchoBloque, 230);
            contenedor.add(pMisCuentas);

            JLabel lblTitleCuentas = new JLabel("CUENTAS");
            lblTitleCuentas.setForeground(amarilloPastel);
            lblTitleCuentas.setFont(tituloSeccion);
            lblTitleCuentas.setBounds(25, 15, 300, 22);
            pMisCuentas.add(lblTitleCuentas);

            JLabel lblTagPrincipal = new JLabel("Principal");
            lblTagPrincipal.setForeground(new Color(180, 190, 180));
            lblTagPrincipal.setFont(etiquetaCampos);
            lblTagPrincipal.setBounds(25, 55, 200, 20);
            pMisCuentas.add(lblTagPrincipal);

            lblCtaPrincipal = new JLabel("--");
            lblCtaPrincipal.setForeground(Color.WHITE);
            lblCtaPrincipal.setFont(textoInputs);
            lblCtaPrincipal.setBounds(25, 78, 550, 22);
            pMisCuentas.add(lblCtaPrincipal);

            JLabel lblTagAhorros = new JLabel("Ahorros");
            lblTagAhorros.setForeground(new Color(180, 190, 180));
            lblTagAhorros.setFont(etiquetaCampos);
            lblTagAhorros.setBounds(25, 125, 200, 20);
            pMisCuentas.add(lblTagAhorros);

            lblCtaAhorros = new JLabel("--");
            lblCtaAhorros.setForeground(Color.WHITE);
            lblCtaAhorros.setFont(textoInputs);
            lblCtaAhorros.setBounds(25, 148, 550, 22);
            pMisCuentas.add(lblCtaAhorros);

            // -----------------------------------------------------------------
            PanelBloqueEstilizado pResumenFin = new PanelBloqueEstilizado();
            pResumenFin.setBounds(col2X, 110, anchoBloque, 230);
            contenedor.add(pResumenFin);

            JLabel lblTitleResumen = new JLabel("RESUMEN FINANCIERO");
            lblTitleResumen.setForeground(amarilloPastel);
            lblTitleResumen.setFont(tituloSeccion);
            lblTitleResumen.setBounds(25, 15, 300, 22);
            pResumenFin.add(lblTitleResumen);

            lblSaldoDisponible = new JLabel("Saldo disponible: --");
            lblSaldoDisponible.setForeground(Color.WHITE);
            lblSaldoDisponible.setFont(textoInputs);
            lblSaldoDisponible.setBounds(25, 55, 550, 22);
            pResumenFin.add(lblSaldoDisponible);

            lblIngresosMes = new JLabel("Ingresos del mes: --");
            lblIngresosMes.setForeground(new Color(130, 220, 130));
            lblIngresosMes.setFont(textoInputs);
            lblIngresosMes.setBounds(25, 90, 550, 22);
            pResumenFin.add(lblIngresosMes);

            lblGastosMes = new JLabel("Gastos del mes: --");
            lblGastosMes.setForeground(new Color(240, 120, 120));
            lblGastosMes.setFont(textoInputs);
            lblGastosMes.setBounds(25, 125, 550, 22);
            pResumenFin.add(lblGastosMes);

            lblUltimaActResumen = new JLabel("Última actualización: --");
            lblUltimaActResumen.setForeground(new Color(150, 160, 150));
            lblUltimaActResumen.setFont(descripcionPantalla);
            lblUltimaActResumen.setBounds(25, 160, 550, 22);
            pResumenFin.add(lblUltimaActResumen);


            // =================================================================
            // FILA 2: BANCOS CONECTADOS (IZQ) vs TRANSFERENCIAS RECIENTES (DER)
            // =================================================================
            PanelBloqueEstilizado pBancosCon = new PanelBloqueEstilizado();
            pBancosCon.setBounds(col1X, 360, anchoBloque, 190);
            contenedor.add(pBancosCon);

            JLabel lblTitleBancos = new JLabel("BANCOS CONECTADOS");
            lblTitleBancos.setForeground(amarilloPastel);
            lblTitleBancos.setFont(tituloSeccion);
            lblTitleBancos.setBounds(25, 15, 300, 22);
            pBancosCon.add(lblTitleBancos);

            lblBanco1 = new JLabel("✔ --");
            lblBanco1.setForeground(Color.WHITE);
            lblBanco1.setFont(textoInputs);
            lblBanco1.setBounds(25, 55, 550, 22);
            pBancosCon.add(lblBanco1);

            lblBanco2 = new JLabel("✔ --");
            lblBanco2.setForeground(Color.WHITE);
            lblBanco2.setFont(textoInputs);
            lblBanco2.setBounds(25, 90, 550, 22);
            pBancosCon.add(lblBanco2);

            lblBanco3 = new JLabel("✔ --");
            lblBanco3.setForeground(Color.WHITE);
            lblBanco3.setFont(textoInputs);
            lblBanco3.setBounds(25, 125, 550, 22);
            pBancosCon.add(lblBanco3);

            // -----------------------------------------------------------------
            PanelBloqueEstilizado pTransRec = new PanelBloqueEstilizado();
            pTransRec.setBounds(col2X, 360, anchoBloque, 190);
            contenedor.add(pTransRec);

            JLabel lblTitleTrans = new JLabel("TRANSFERENCIAS RECIENTES");
            lblTitleTrans.setForeground(amarilloPastel);
            lblTitleTrans.setFont(tituloSeccion);
            lblTitleTrans.setBounds(25, 15, 300, 22);
            pTransRec.add(lblTitleTrans);

            lblTransReciente1 = new JLabel("--");
            lblTransReciente1.setForeground(Color.WHITE);
            lblTransReciente1.setFont(textoInputs);
            lblTransReciente1.setBounds(25, 55, 550, 22);
            pTransRec.add(lblTransReciente1);

            lblTransReciente2 = new JLabel("--");
            lblTransReciente2.setForeground(Color.WHITE);
            lblTransReciente2.setFont(textoInputs);
            lblTransReciente2.setBounds(25, 85, 550, 22);
            pTransRec.add(lblTransReciente2);

            lblTransReciente3 = new JLabel("--");
            lblTransReciente3.setForeground(Color.WHITE);
            lblTransReciente3.setFont(textoInputs);
            lblTransReciente3.setBounds(25, 115, 550, 22);
            pTransRec.add(lblTransReciente3);

            lblTransReciente4 = new JLabel("--");
            lblTransReciente4.setForeground(Color.WHITE);
            lblTransReciente4.setFont(textoInputs);
            lblTransReciente4.setBounds(25, 145, 550, 22);
            pTransRec.add(lblTransReciente4);


            // =================================================================
            // FILA 3: CAMBIO DE DIVISAS (IZQ) vs HISTORIAL DE ACTIVIDAD (DER)
            // =================================================================
            PanelBloqueEstilizado pCambioDiv = new PanelBloqueEstilizado();
            pCambioDiv.setBounds(col1X, 570, anchoBloque, 310);
            contenedor.add(pCambioDiv);

            JLabel lblTitleDiv = new JLabel("CAMBIO DE DIVISAS");
            lblTitleDiv.setForeground(amarilloPastel);
            lblTitleDiv.setFont(tituloSeccion);
            lblTitleDiv.setBounds(25, 15, 300, 22);
            pCambioDiv.add(lblTitleDiv);

            lblDivUSD = new JLabel("USD          --");
            lblDivUSD.setForeground(Color.WHITE);
            lblDivUSD.setFont(textoInputs);
            lblDivUSD.setBounds(25, 60, 550, 22);
            pCambioDiv.add(lblDivUSD);

            lblDivEUR = new JLabel("EUR          --");
            lblDivEUR.setForeground(Color.WHITE);
            lblDivEUR.setFont(textoInputs);
            lblDivEUR.setBounds(25, 100, 550, 22);
            pCambioDiv.add(lblDivEUR);

            lblDivMXN = new JLabel("MXN          --");
            lblDivMXN.setForeground(Color.WHITE);
            lblDivMXN.setFont(textoInputs);
            lblDivMXN.setBounds(25, 140, 550, 22);
            pCambioDiv.add(lblDivMXN);

            lblUltimaActDivisas = new JLabel("Actualizado: --");
            lblUltimaActDivisas.setForeground(new Color(150, 160, 150));
            lblUltimaActDivisas.setFont(descripcionPantalla);
            lblUltimaActDivisas.setBounds(25, 260, 550, 22);
            pCambioDiv.add(lblUltimaActDivisas);

            // -----------------------------------------------------------------
            PanelBloqueEstilizado pHistorialAct = new PanelBloqueEstilizado();
            pHistorialAct.setBounds(col2X, 570, anchoBloque, 310);
            contenedor.add(pHistorialAct);

            JLabel lblTitleHist = new JLabel("HISTORIAL DE DIVISAS");
            lblTitleHist.setForeground(amarilloPastel);
            lblTitleHist.setFont(tituloSeccion);
            lblTitleHist.setBounds(25, 15, 300, 22);
            pHistorialAct.add(lblTitleHist);

            // Registro 1
            lblHist1_Titulo = new JLabel("--");
            lblHist1_Titulo.setForeground(Color.WHITE);
            lblHist1_Titulo.setFont(textoInputs);
            lblHist1_Titulo.setBounds(25, 50, 550, 22);
            pHistorialAct.add(lblHist1_Titulo);

            lblHist1_Meta = new JLabel("--");
            lblHist1_Meta.setForeground(new Color(150, 160, 150));
            lblHist1_Meta.setFont(descripcionPantalla);
            lblHist1_Meta.setBounds(25, 70, 550, 20);
            pHistorialAct.add(lblHist1_Meta);

            // Registro 2
            lblHist2_Titulo = new JLabel("--");
            lblHist2_Titulo.setForeground(Color.WHITE);
            lblHist2_Titulo.setFont(textoInputs);
            lblHist2_Titulo.setBounds(25, 110, 550, 22);
            pHistorialAct.add(lblHist2_Titulo);

            lblHist2_Meta = new JLabel("--");
            lblHist2_Meta.setForeground(new Color(150, 160, 150));
            lblHist2_Meta.setFont(descripcionPantalla);
            lblHist2_Meta.setBounds(25, 130, 550, 20);
            pHistorialAct.add(lblHist2_Meta);

            // Registro 3
            lblHist3_Titulo = new JLabel("--");
            lblHist3_Titulo.setForeground(Color.WHITE);
            lblHist3_Titulo.setFont(textoInputs);
            lblHist3_Titulo.setBounds(25, 170, 550, 22);
            pHistorialAct.add(lblHist3_Titulo);

            lblHist3_Meta = new JLabel("--");
            lblHist3_Meta.setForeground(new Color(150, 160, 150));
            lblHist3_Meta.setFont(descripcionPantalla);
            lblHist3_Meta.setBounds(25, 190, 550, 20);
            pHistorialAct.add(lblHist3_Meta);

            // Registro 4
            lblHist4_Titulo = new JLabel("--");
            lblHist4_Titulo.setForeground(Color.WHITE);
            lblHist4_Titulo.setFont(textoInputs);
            lblHist4_Titulo.setBounds(25, 230, 550, 22);
            pHistorialAct.add(lblHist4_Titulo);

            lblHist4_Meta = new JLabel("--");
            lblHist4_Meta.setForeground(new Color(150, 160, 150));
            lblHist4_Meta.setFont(descripcionPantalla);
            lblHist4_Meta.setBounds(25, 250, 550, 20);
            pHistorialAct.add(lblHist4_Meta);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class PanelContenedorVerde extends JPanel {
        public PanelContenedorVerde() { setLayout(null); setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(verdeCorporativoFondo);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class PanelBloqueEstilizado extends JPanel {
        public PanelBloqueEstilizado() { setLayout(null); setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 100)); 
            g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
            g2.setColor(new Color(251, 232, 138, 140)); 
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g2.dispose();
            super.paintComponent(g);
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
            java.util.logging.Logger.getLogger(HistorialMenor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new HistorialMenor().setVisible(true);
        });
    }
}