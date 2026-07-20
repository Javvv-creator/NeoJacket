package gui;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import funcionalidades.SupervisionDAO;
import funcionalidades.SesionUsuario;
import funcionalidades.CrearUsuario;
import funcionalidades.DashboardDAO;

public class Dashboard extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    private Integer idUsuarioActual;

    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeTarjeta = new Color(25, 38, 35, 180);
    private final Color verdeCard = new Color(20, 32, 30, 190);

    // Variables para almacenar los números aleatorios generados (respaldo si no hay datos reales)
    private final String numeroCuentaSimulado;
    private final String numeroTarjetaSimulado;

    // ==========================================
    // DATOS REALES DEL USUARIO EN SESIÓN
    // ==========================================
    private final DashboardDAO dashboardDAO = new DashboardDAO();
    private int idUsuarioReal;
    private String nombreUsuario;
    private double saldoTotal;
    private int numCuentas;
    private int numTransferencias;
    private List<String> bancosVinculados;
    private List<DashboardDAO.Movimiento> movimientosRecientes;
    private List<DashboardDAO.Movimiento> actividadReciente;
    private String numeroCuentaMostrar;
    private List<DashboardDAO.TarjetaResumen> tarjetasActivas;

    // ==========================================
    // TEXTFIELD REDONDEADO
    // ==========================================
    class RoundedTextField extends JTextField {

        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ==========================================
    // BOTÓN DE NAVEGACIÓN DEL SIDEBAR
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
    // BOTÓN NEO 
    // ==========================================
    class BotonNeo extends JButton {

        private Color normal;
        private Color hover;
        private Color colorTexto;

        // Constructor original (usa la paleta verde/dorada por defecto)
        public BotonNeo(String texto) {
            this(texto, new Color(94, 116, 73, 190), new Color(251, 232, 138, 220), Color.WHITE);
        }

        // Constructor flexible, para botones con su propia paleta (ej. Cerrar sesión, Supervisión)
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

    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public Dashboard() {
        this(null);
    }

    public Dashboard(Integer idUsuarioActual) {
        this.idUsuarioActual = idUsuarioActual;

        // Generar datos simulados de forma aleatoria para Cuenta y Tarjeta (solo respaldo)
        this.numeroCuentaSimulado = "4500-" + (int) (Math.random() * 9000 + 1000) + "-" + (int) (Math.random() * 9000 + 1000) + "-02";
        this.numeroTarjetaSimulado = "4152 " + (int) (Math.random() * 9000 + 1000) + " " + (int) (Math.random() * 9000 + 1000) + " " + (int) (Math.random() * 9000 + 1000);

        // El id real siempre sale de la sesión; si por alguna razón te pasaron
        // un idUsuarioActual explícito (compatibilidad con código anterior), se respeta.
        this.idUsuarioReal = (idUsuarioActual != null) ? idUsuarioActual : SesionUsuario.getIdUsuario();

        cargarDatosReales();

        initComponents();

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

    // ==========================================
    // CARGA DE DATOS REALES DESDE LA BASE DE DATOS
    // ==========================================
    private void cargarDatosReales() {
        if (idUsuarioReal <= 0) {
            // No hay sesión válida: se usan valores por defecto para no romper la UI
            nombreUsuario = "Usuario";
            saldoTotal = 0.0;
            numCuentas = 0;
            numTransferencias = 0;
            bancosVinculados = new java.util.ArrayList<>();
            movimientosRecientes = new java.util.ArrayList<>();
            actividadReciente = new java.util.ArrayList<>();
            numeroCuentaMostrar = numeroCuentaSimulado;
            tarjetasActivas = new java.util.ArrayList<>();
            tarjetasActivas.add(tarjetaSimulada());
            return;
        }

        nombreUsuario = dashboardDAO.obtenerNombreCompleto(idUsuarioReal);
        saldoTotal = dashboardDAO.obtenerSaldoTotal(idUsuarioReal);
        numCuentas = dashboardDAO.obtenerNumeroCuentas(idUsuarioReal);
        numTransferencias = dashboardDAO.obtenerNumeroTransferencias(idUsuarioReal);

        bancosVinculados = new CrearUsuario().obtenerBancosVinculados(idUsuarioReal);
        movimientosRecientes = dashboardDAO.obtenerMovimientosRecientes(idUsuarioReal, 5);
        actividadReciente = dashboardDAO.obtenerActividadReciente(idUsuarioReal, 6);

        // Si el usuario aún no tiene cuenta real registrada, se usa el respaldo simulado
        String cuentaReal = dashboardDAO.obtenerNumeroCuentaPrincipal(idUsuarioReal);
        numeroCuentaMostrar = (cuentaReal != null && !cuentaReal.isEmpty()) ? cuentaReal : numeroCuentaSimulado;

        // TODAS las tarjetas activas del usuario (hasta 4). Si aún no tiene
        // ninguna, se muestra la simulada como respaldo (igual que antes).
        tarjetasActivas = dashboardDAO.obtenerTarjetasActivas(idUsuarioReal);
        if (tarjetasActivas == null || tarjetasActivas.isEmpty()) {
            tarjetasActivas = new java.util.ArrayList<>();
            tarjetasActivas.add(tarjetaSimulada());
        }
    }

    // Tarjeta de respaldo (sin registro real en BD) cuando el usuario todavía
    // no tiene ninguna tarjeta agregada. idTarjeta = -1 marca que no es real,
    // así el Dashboard sabe que no debe abrir un detalle para ella.
    private DashboardDAO.TarjetaResumen tarjetaSimulada() {
        DashboardDAO.TarjetaResumen t = new DashboardDAO.TarjetaResumen();
        t.idTarjeta = -1;
        t.numero = numeroTarjetaSimulado;
        return t;
    }

    // ==========================================
    // PANEL PRINCIPAL
    // ==========================================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearContenido();
        }

        // ==========================================
        // SIDEBAR Y ENLACES DE CONTROL
        // ==========================================
        private void crearSidebar() {
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
            sidebar.setLayout(null);
            sidebar.setBounds(20, 20, 300, 870);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] botonesMenu = {
                "Saldos",
                "Bancos conectados",
                "Transferencias",
                "Historial",
                "Agregar Tarjeta"
            };

            int y = 140;
            for (String textoBtn : botonesMenu) {
                BotonSidebarNeo btn = new BotonSidebarNeo(textoBtn);
                btn.setBounds(20, y, 250, 55);

                // Enrutador de acciones para la navegación lateral
                if (textoBtn.equals("Saldos")) {
                    btn.addActionListener(e -> {
                        new Saldos().setVisible(true);
                        dispose();
                    });
                }
                if (textoBtn.equals("Bancos conectados")) {
                    btn.addActionListener(e -> {
                        new BancosConectados().setVisible(true);
                        dispose();
                    });
                }
                if (textoBtn.equals("Transferencias")) {
                    btn.addActionListener(e -> {
                        new Transferencias().setVisible(true);
                        dispose();
                    });
                }
                if (textoBtn.equals("Historial")) {
                    btn.addActionListener(e -> {
                        new Historial().setVisible(true);
                        dispose();
                    });
                }
                if (textoBtn.equals("Agregar Tarjeta")) {
                    btn.addActionListener(e -> {
                        new DetalleTarjetaDasboard(idUsuarioActual);
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            // Botón Supervisión — aparece solo si el usuario tiene menores a cargo
            if (idUsuarioActual != null) {
                SupervisionDAO dao = new SupervisionDAO();
                if (dao.tieneMenoresACargo(idUsuarioActual)) {
                    BotonNeo btnSupervision = new BotonNeo(
                            "Supervisión",
                            new Color(251, 232, 138),
                            new Color(255, 245, 180),
                            new Color(25, 38, 35));
                    btnSupervision.setBounds(20, y, 250, 55);
                    btnSupervision.addActionListener(e -> {
                        new PanelSupervision(idUsuarioActual).setVisible(true);
                        dispose();
                    });
                    sidebar.add(btnSupervision);
                }
            }

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
        }

        // ==========================================
        // CONTENIDO PRINCIPAL  (estética tipo panel de administrador)
        // ==========================================
        private void crearContenido() {
            Font fontLabelChico = new Font("Segoe UI", Font.BOLD, 13);
            Font fontTituloGrande = new Font("Segoe UI", Font.BOLD, 30);
            Font fontSubtitulo = new Font("Segoe UI", Font.PLAIN, 13);
            Font fontTituloTarjeta = new Font("Segoe UI", Font.BOLD, 16);
            Font fontValorGrande = new Font("Segoe UI", Font.BOLD, 26);
            Font fontTexto = new Font("Segoe UI", Font.PLAIN, 14);

            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setOpaque(false);
            contenedor.setBounds(350, 20, 1250, 950);
            add(contenedor);

            // ---------------------------------------------------
            // FILA 1: Tarjeta de bienvenida + tarjeta de usuario
            // ---------------------------------------------------
            RoundedPanel bienvenida = crearPanel(30, 25, 800, 140, verdeTarjeta, null);
            contenedor.add(bienvenida);

            JLabel lblBienvenido = new JLabel("BIENVENIDO");
            lblBienvenido.setForeground(amarilloPastel);
            lblBienvenido.setFont(fontLabelChico);
            lblBienvenido.setBounds(30, 20, 300, 20);
            bienvenida.add(lblBienvenido);

            JLabel lblPanelTitulo = new JLabel("Panel de Usuario");
            lblPanelTitulo.setForeground(Color.WHITE);
            lblPanelTitulo.setFont(fontTituloGrande);
            lblPanelTitulo.setBounds(30, 42, 500, 40);
            bienvenida.add(lblPanelTitulo);

            JLabel lblResumen = new JLabel("Resumen general de tu cuenta");
            lblResumen.setForeground(Color.LIGHT_GRAY);
            lblResumen.setFont(fontSubtitulo);
            lblResumen.setBounds(30, 88, 400, 20);
            bienvenida.add(lblResumen);

            // Tarjeta de usuario, con marco amarillo delgado
            RoundedPanel usuarioCard = crearPanel(850, 25, 370, 140, verdeCard, amarilloPastel);
            contenedor.add(usuarioCard);

            JLabel lblUsuarioTitulo = new JLabel("Usuario");
            lblUsuarioTitulo.setForeground(Color.WHITE);
            lblUsuarioTitulo.setFont(fontTituloTarjeta);
            lblUsuarioTitulo.setBounds(25, 22, 250, 28);
            usuarioCard.add(lblUsuarioTitulo);

            JLabel lblUsuarioValor = new JLabel(nombreUsuario);
            lblUsuarioValor.setForeground(amarilloPastel);
            lblUsuarioValor.setFont(fontValorGrande);
            lblUsuarioValor.setBounds(25, 55, 320, 40);
            usuarioCard.add(lblUsuarioValor);

            // ---------------------------------------------------
            // FILA 2: Tarjetas de resumen (stat cards) con marco amarillo delgado
            // ---------------------------------------------------
            int statY = 190, statH = 115, statGap = 20;
            int statW = (1190 - 3 * statGap) / 4;

            String saldoFormateado = String.format("Q %,.2f", saldoTotal);

            String[][] stats = {
                {"Saldo Disponible", saldoFormateado},
                {"Cuentas", String.valueOf(numCuentas)},
                {"Bancos Conectados", String.valueOf(bancosVinculados.size())},
                {"Transferencias", String.valueOf(numTransferencias)}
            };

            int sx = 30;
            for (String[] stat : stats) {
                RoundedPanel statCard = crearPanel(sx, statY, statW, statH, verdeCard, amarilloPastel);
                contenedor.add(statCard);

                JLabel lblTitulo = new JLabel(stat[0]);
                lblTitulo.setForeground(Color.WHITE);
                lblTitulo.setFont(fontTituloTarjeta);
                lblTitulo.setBounds(18, 16, statW - 30, 24);
                statCard.add(lblTitulo);

                JLabel lblValor = new JLabel(stat[1]);
                lblValor.setForeground(amarilloPastel);
                lblValor.setFont(fontValorGrande);
                lblValor.setBounds(18, 50, statW - 30, 40);
                statCard.add(lblValor);

                sx += statW + statGap;
            }

            // ---------------------------------------------------
            // FILA 3: Detalle de Cuenta / Tarjeta  +  Actividad Reciente
            // ---------------------------------------------------
            int rowY = 335, rowH = 620;

            // ---- Panel izquierdo: Detalle de Cuenta y Tarjeta ----
            RoundedPanel detalle = crearPanel(30, rowY, 740, rowH, verdeTarjeta, null);
            contenedor.add(detalle);

            JLabel lblDetalleTitulo = new JLabel("Detalle de Cuenta y Tarjeta");
            lblDetalleTitulo.setForeground(amarilloPastel);
            lblDetalleTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblDetalleTitulo.setBounds(30, 25, 400, 30);
            detalle.add(lblDetalleTitulo);

            JLabel lblSaldoLabel = new JLabel("Saldo Disponible");
            lblSaldoLabel.setForeground(Color.LIGHT_GRAY);
            lblSaldoLabel.setFont(fontTexto);
            lblSaldoLabel.setBounds(30, 75, 300, 22);
            detalle.add(lblSaldoLabel);

            JLabel lblSaldoValor = new JLabel(saldoFormateado);
            lblSaldoValor.setForeground(Color.WHITE);
            lblSaldoValor.setFont(new Font("Segoe UI", Font.BOLD, 40));
            lblSaldoValor.setBounds(30, 98, 500, 55);
            detalle.add(lblSaldoValor);

            // ---------------------------------------------------
            // Sub-tarjeta con Número de Cuenta + UNA fila por cada
            // Número de Tarjeta que el usuario tenga (hasta 4), cada
            // una con su propio botón "Copiar". Va creciendo conforme
            // el usuario agregue tarjetas nuevas.
            // ---------------------------------------------------
            int filaAlto = 30;
            int numeroCardAltura = 20 + filaAlto + (tarjetasActivas.size() * filaAlto) + 10;
            RoundedPanel numeroCard = crearPanel(30, 175, 680, numeroCardAltura, verdeCard, null);
            detalle.add(numeroCard);

            JLabel lblCuentaText = new JLabel("Número de Cuenta: " + numeroCuentaMostrar);
            lblCuentaText.setForeground(Color.LIGHT_GRAY);
            lblCuentaText.setFont(fontTexto);
            lblCuentaText.setBounds(20, 15, 500, 25);
            numeroCard.add(lblCuentaText);

            int filaY = 15 + filaAlto;
            int numeroTarjetaIdx = 1;
            for (DashboardDAO.TarjetaResumen tarjeta : tarjetasActivas) {
                String numeroTarjeta = tarjeta.numero;

                JLabel lblTarjetaText = new JLabel("Número de Tarjeta " + numeroTarjetaIdx + ": " + numeroTarjeta);
                lblTarjetaText.setForeground(Color.WHITE);
                lblTarjetaText.setFont(fontTexto);
                lblTarjetaText.setBounds(20, filaY, 420, 25);
                numeroCard.add(lblTarjetaText);

                // Botón "Ver": abre el detalle de ESA tarjeta específica. Solo
                // disponible para tarjetas reales (idTarjeta > 0); la tarjeta
                // simulada de respaldo (idTarjeta == -1) no existe en BD.
                if (tarjeta.idTarjeta > 0) {
                    final int idTarjetaParaVer = tarjeta.idTarjeta;
                    JButton btnVer = new JButton("Ver") {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(getModel().isRollover() ? new Color(120, 150, 90) : new Color(94, 116, 73));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                            g2.dispose();
                            super.paintComponent(g);
                        }
                    };
                    btnVer.setBounds(445, filaY - 3, 90, 30);
                    btnVer.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    btnVer.setForeground(Color.WHITE);
                    btnVer.setFocusPainted(false);
                    btnVer.setContentAreaFilled(false);
                    btnVer.setBorderPainted(false);
                    btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnVer.addActionListener(e -> {
                        new DetalleTarjetaUsuario(idTarjetaParaVer).setVisible(true);
                        dispose();
                    });
                    numeroCard.add(btnVer);
                }

                final String numeroParaCopiar = numeroTarjeta;
                JButton btnCopiar = new JButton("Copiar") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getModel().isRollover() ? new Color(255, 245, 180) : amarilloPastel);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                btnCopiar.setBounds(550, filaY - 3, 110, 30);
                btnCopiar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnCopiar.setForeground(Color.BLACK);
                btnCopiar.setFocusPainted(false);
                btnCopiar.setContentAreaFilled(false);
                btnCopiar.setBorderPainted(false);
                btnCopiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnCopiar.addActionListener(e -> {
                    String tarjetaLimpia = numeroParaCopiar.replace(" ", "");
                    StringSelection stringSelection = new StringSelection(tarjetaLimpia);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    JOptionPane.showMessageDialog(this, "¡Número de tarjeta copiado al portapapeles!", "Copiado", JOptionPane.INFORMATION_MESSAGE);
                });
                numeroCard.add(btnCopiar);

                filaY += filaAlto;
                numeroTarjetaIdx++;
            }

            // Todo lo que sigue (Bancos Vinculados / Movimientos Recientes)
            // se posiciona en base a dónde terminó la tarjeta numeroCard,
            // para que no se encimen sin importar cuántas tarjetas haya.
            int siguienteSeccionY = 175 + numeroCardAltura + 20;

            // Lista de bancos vinculados dentro del mismo panel de detalle
            JLabel lblBancosTitulo = new JLabel("Bancos Vinculados");
            lblBancosTitulo.setForeground(amarilloPastel);
            lblBancosTitulo.setFont(fontTituloTarjeta);
            lblBancosTitulo.setBounds(30, siguienteSeccionY, 300, 25);
            detalle.add(lblBancosTitulo);

            if (bancosVinculados.isEmpty()) {
                JLabel sinBancos = new JLabel("Aún no tienes bancos vinculados");
                sinBancos.setForeground(Color.LIGHT_GRAY);
                sinBancos.setFont(fontTexto);
                sinBancos.setBounds(30, siguienteSeccionY + 35, 300, 22);
                detalle.add(sinBancos);
            } else {
                int by = siguienteSeccionY + 35;
                for (String banco : bancosVinculados) {
                    JLabel lblBanco = new JLabel("•  " + banco);
                    lblBanco.setForeground(Color.WHITE);
                    lblBanco.setFont(fontTexto);
                    lblBanco.setBounds(30, by, 320, 22);
                    detalle.add(lblBanco);
                    by += 28;
                }
            }

            // Mini gráfica de barras con los montos de las últimas transferencias reales
            JLabel lblMiniGrafica = new JLabel("Movimientos Recientes");
            lblMiniGrafica.setForeground(amarilloPastel);
            lblMiniGrafica.setFont(fontTituloTarjeta);
            lblMiniGrafica.setBounds(370, siguienteSeccionY, 300, 25);
            detalle.add(lblMiniGrafica);

            if (movimientosRecientes.isEmpty()) {
                JLabel sinMovs = new JLabel("Aún no hay transferencias registradas");
                sinMovs.setForeground(Color.LIGHT_GRAY);
                sinMovs.setFont(fontTexto);
                sinMovs.setBounds(370, siguienteSeccionY + 35, 320, 40);
                detalle.add(sinMovs);
            } else {
                SimpleDateFormat sdfCorto = new SimpleDateFormat("dd/MM");
                double maxMonto = 1;
                for (DashboardDAO.Movimiento m : movimientosRecientes) {
                    maxMonto = Math.max(maxMonto, m.monto);
                }
                int n = movimientosRecientes.size();
                int[] valores = new int[n];
                String[] etiquetas = new String[n];
                for (int i = 0; i < n; i++) {
                    DashboardDAO.Movimiento m = movimientosRecientes.get(i);
                    valores[i] = (int) Math.max(8, (m.monto / maxMonto) * 100);
                    etiquetas[i] = m.fecha != null ? sdfCorto.format(m.fecha) : "-";
                }
                detalle.add(crearMiniGrafica(370, siguienteSeccionY + 30, 340, 160, valores, etiquetas));
            }

            // ---- Panel derecho: Actividad Reciente ----
            RoundedPanel actividad = crearPanel(790, rowY, 430, rowH, verdeTarjeta, null);
            contenedor.add(actividad);

            JLabel lblActividadTitulo = new JLabel("Actividad Reciente");
            lblActividadTitulo.setForeground(amarilloPastel);
            lblActividadTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblActividadTitulo.setBounds(25, 25, 350, 30);
            actividad.add(lblActividadTitulo);

            SimpleDateFormat sdfLarga = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            if (actividadReciente.isEmpty()) {
                JLabel sinActividad = new JLabel("Aún no tienes movimientos registrados.");
                sinActividad.setForeground(Color.LIGHT_GRAY);
                sinActividad.setFont(fontTexto);
                sinActividad.setBounds(25, 80, 380, 30);
                actividad.add(sinActividad);
            } else {
                // --- Lo último que se agregó, destacado en grande ---
                DashboardDAO.Movimiento ultimo = actividadReciente.get(0);

                RoundedPanel destacado = crearPanel(25, 70, 380, 100, verdeCard, amarilloPastel);
                actividad.add(destacado);

                JLabel lblUltimoTipo = new JLabel(capitalizar(ultimo.tipo));
                lblUltimoTipo.setForeground(amarilloPastel);
                lblUltimoTipo.setFont(new Font("Segoe UI", Font.BOLD, 24));
                lblUltimoTipo.setBounds(18, 12, 340, 34);
                destacado.add(lblUltimoTipo);

                JLabel lblUltimoMonto = new JLabel(String.format("%s %,.2f  •  %s",
                        ultimo.moneda != null ? ultimo.moneda : "Q", ultimo.monto,
                        ultimo.estado != null ? ultimo.estado : ""));
                lblUltimoMonto.setForeground(Color.WHITE);
                lblUltimoMonto.setFont(fontTexto);
                lblUltimoMonto.setBounds(18, 48, 340, 22);
                destacado.add(lblUltimoMonto);

                JLabel lblUltimoFecha = new JLabel(ultimo.fecha != null ? sdfLarga.format(ultimo.fecha) : "");
                lblUltimoFecha.setForeground(Color.LIGHT_GRAY);
                lblUltimoFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblUltimoFecha.setBounds(18, 72, 340, 20);
                destacado.add(lblUltimoFecha);

                // --- Resto de la actividad, en formato de lista compacta ---
                int ay = 190;
                for (int i = 1; i < actividadReciente.size(); i++) {
                    DashboardDAO.Movimiento m = actividadReciente.get(i);
                    String linea = String.format("•  %s — %s %,.2f  (%s)",
                            capitalizar(m.tipo), m.moneda != null ? m.moneda : "Q", m.monto,
                            m.fecha != null ? sdfLarga.format(m.fecha) : "-");
                    JLabel lbl = new JLabel(linea);
                    lbl.setForeground(Color.WHITE);
                    lbl.setFont(fontTexto);
                    lbl.setBounds(25, ay, 390, 24);
                    actividad.add(lbl);
                    ay += 38;
                }
            }
        }

        // ==========================================
        // HELPER: capitaliza el tipo de transacción (ej. "transferencia" -> "Transferencia")
        // ==========================================
        private String capitalizar(String texto) {
            if (texto == null || texto.isEmpty()) {
                return "Movimiento";
            }
            return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
        }

        // ==========================================
        // HELPER: crea un panel redondeado, con o sin marco delgado
        // ==========================================
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

        // ==========================================
        // HELPER: mini gráfica de barras decorativa (sustituye la gráfica del admin)
        // ==========================================
        private JPanel crearMiniGrafica(int x, int y, int w, int h, int[] valores, String[] etiquetas) {
            JPanel grafica = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int n = valores.length;
                    int gap = 14;
                    int barW = (w - gap * (n + 1)) / n;
                    int maxVal = 100;
                    int baseY = h - 25;

                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    for (int i = 0; i < n; i++) {
                        int barH = (int) ((valores[i] / (double) maxVal) * (h - 45));
                        int barX = gap + i * (barW + gap);
                        int barY = baseY - barH;

                        g2.setColor(amarilloPastel);
                        g2.fillRoundRect(barX, barY, barW, barH, 8, 8);

                        g2.setColor(Color.LIGHT_GRAY);
                        FontMetrics fm = g2.getFontMetrics();
                        int textW = fm.stringWidth(etiquetas[i]);
                        g2.drawString(etiquetas[i], barX + (barW - textW) / 2, h - 8);
                    }
                    g2.dispose();
                }
            };
            grafica.setOpaque(false);
            grafica.setBounds(x, y, w, h);
            return grafica;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ==========================================
    // CONTROL DE BORDES REDONDEADOS
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}