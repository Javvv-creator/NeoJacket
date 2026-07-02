package gui;

import javax.swing.*;
import java.awt.*;

public class Historial extends javax.swing.JFrame {

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

    public Historial() {
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Historial de Actividad");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new FondoPanel());
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
        }

        private void crearSidebar() {
            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] menuItems = {"Saldos", "Bancos conectados", "Transferencias"};
            int y = 140;
            for (String textoBtn : menuItems) {
                JButton btn = new JButton(textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btn.addActionListener(e -> {
                    if (textoBtn.equals("Saldos")) new Saldos().setVisible(true);
                    else if (textoBtn.equals("Bancos conectados")) new BancosConectados().setVisible(true);
                    else if (textoBtn.equals("Transferencias")) new Transferencias().setVisible(true);
                    dispose();
                });
                sidebar.add(btn);
                y += 70;
            }

            // El botón de Historial se marca como activo
            JButton btnHistorial = new JButton("Historial");
            btnHistorial.setBounds(20, y, 250, 55);
            btnHistorial.setBackground(amarilloPastel);
            btnHistorial.setForeground(Color.BLACK);
            btnHistorial.setFocusPainted(false);
            btnHistorial.setBorderPainted(false);
            btnHistorial.setFont(new Font("Segoe UI", Font.BOLD, 14));
            sidebar.add(btnHistorial);

            JButton btnCerrarSesion = new JButton("Cerrar sesión");
            btnCerrarSesion.setBounds(20, 880, 250, 55);
            btnCerrarSesion.setFocusPainted(false);
            btnCerrarSesion.setBorderPainted(false);
            btnCerrarSesion.setBackground(new Color(191, 76, 58));
            btnCerrarSesion.setForeground(Color.WHITE);
            btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnCerrarSesion.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            add(sidebar);
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
            java.util.logging.Logger.getLogger(Historial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new Historial().setVisible(true);
        });
    }
}