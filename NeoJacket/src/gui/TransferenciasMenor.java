package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import funcionalidades.CrearUsuario;
import funcionalidades.SupervisionDAO;

public class TransferenciasMenor extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    private Integer idMenor;

    // TIPOGRAFÍAS DE LA IDENTIDAD VISUAL
    private final Font tituloPantalla = new Font("Segoe UI", Font.BOLD, 34);
    private final Font descripcionPantalla = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font tituloSeccion = new Font("Segoe UI", Font.BOLD, 16);
    private final Font etiquetaCampos = new Font("Segoe UI", Font.BOLD, 13);
    private final Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);
    
    // PALETA DE COLORES CORPORATIVA REVISADA (VERDE OSCURO Y AMARILLO)
    private final Color verdeCorporativoFondo = new Color(20, 32, 29, 215); 
    private final Color amarilloPastel = new Color(251, 232, 138);

    // --- COMPONENTES DECLARADOS GLOBALMENTE PARA SU ACCESO ---
    public JComboBoxOscuro cmbSelBancoO, cmbTipoCuentaO, cmbSelBancoD, cmbTipoCuentaD, cmbMonedaO, cmbMonedaD;
    public JTextFieldOscuro txtNumCuentaO, txtNumCuentaD, txtMonto;
    public JPasswordFieldOscuro txtPassword;
    public JLabel lblSaldoO, lblTitularD, lblTipoCambio, lblDestinatarioRecibe;
    
    // Labels del bloque Resumen
    public JLabel lblO_Cuenta, lblO_Nombre, lblO_Tipo, lblO_Banco, lblO_Monto;
    public JLabel lblD_Cuenta, lblD_Nombre, lblD_Tipo, lblD_Banco, lblD_Monto;
    public JButton btnTransferir, btnCancelar, btnImprimir; 

    public TransferenciasMenor() {
        this(null);
    }

    public Integer getIdMenor() {
        return idMenor;
    }

    public TransferenciasMenor(Integer idMenor) {
        this.idMenor = idMenor;
        initComponents();
        
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Transferencias");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- GUARDA DE SEGURIDAD ---
        // Sin importar por dónde se haya navegado hasta aquí, si el permiso de
        // "transferencia" no está aprobado, NUNCA se construye el formulario
        // real de transferencias.
        if (idMenor != null && !tienePermisoAprobado()) {
            setContentPane(new PanelAccesoRestringido());
            SwingUtilities.invokeLater(this::mostrarFuncionBloqueada);
            return;
        }

        setContentPane(new FondoPanel());

        // --- INICIALIZACIÓN DE LA FUNCIONALIDAD REAL (idéntico patrón que gui.Transferencias) ---
        new funcionalidades.TransferenciasMenor(this);
    }

    private boolean tienePermisoAprobado() {
        if (idMenor == null) return false;
        return new SupervisionDAO().tienePermisoAprobado(idMenor, "transferencia");
    }

    private void mostrarFuncionBloqueada() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "Esta función no está disponible para tu tipo de cuenta.\n\n" +
            "¿Deseas enviar una solicitud de permiso a tu tutor?",
            "Acceso restringido", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            SupervisionDAO dao = new SupervisionDAO();
            Integer idAdulto = dao.obtenerIdAdultoDeMenor(idMenor);
            if (idAdulto == null) {
                JOptionPane.showMessageDialog(this, "No se encontró un tutor asignado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dao.registrarAccionBloqueada(idMenor, "transferencia");
            boolean enviada = dao.crearSolicitudPermiso(idMenor, idAdulto, "transferencia", "Realizar una transferencia", 0);
            if (enviada) {
                JOptionPane.showMessageDialog(this,
                    "✅ Solicitud enviada a tu tutor.\nPodrás realizar esta acción cuando la apruebe.",
                    "Solicitud enviada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo enviar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Pantalla mínima mostrada cuando alguien llega a esta ventana sin tener
     * el permiso de "transferencia" aprobado por su tutor. Reemplaza por
     * completo al formulario de transferencias — así nunca queda expuesto.
     */
    class PanelAccesoRestringido extends JPanel {
        public PanelAccesoRestringido() {
            setLayout(new GridBagLayout());
            setBackground(new Color(20, 32, 29));

            JPanel tarjeta = new JPanel();
            tarjeta.setPreferredSize(new Dimension(480, 260));
            tarjeta.setBackground(new Color(25, 38, 35));
            tarjeta.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90), 1, true));
            tarjeta.setLayout(new GridBagLayout());

            JPanel stack = new JPanel();
            stack.setOpaque(false);
            stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));

            JLabel lblCandado = new JLabel("🔒");
            lblCandado.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            lblCandado.setForeground(new Color(200, 200, 200));
            lblCandado.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblTitulo = new JLabel("Acceso restringido");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblTitulo.setForeground(amarilloPastel);
            lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblDetalle = new JLabel("<html><div style='text-align:center; width:340px;'>"
                + "Las transferencias no están disponibles para tu tipo de cuenta "
                + "hasta que tu tutor apruebe el permiso.</div></html>");
            lblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDetalle.setForeground(new Color(200, 205, 200));
            lblDetalle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnRegresar = new JButton("← Regresar al Dashboard");
            btnRegresar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnRegresar.setFocusPainted(false);
            btnRegresar.setBackground(new Color(94, 116, 73));
            btnRegresar.setForeground(Color.WHITE);
            btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRegresar.addActionListener(e -> { new DashboardMenor(idMenor).setVisible(true); dispose(); });

            stack.add(lblCandado);
            stack.add(Box.createRigidArea(new Dimension(0, 12)));
            stack.add(lblTitulo);
            stack.add(Box.createRigidArea(new Dimension(0, 10)));
            stack.add(lblDetalle);
            stack.add(Box.createRigidArea(new Dimension(0, 20)));
            stack.add(btnRegresar);

            tarjeta.add(stack);
            add(tarjeta);
        }
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

            String[] botones = {"Transferencias", "Historial"};
            int y = 150;
            for (String nombre : botones) {
                JButton btn = new JButton(nombre);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                if (nombre.equals("Transferencias")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }
                btn.addActionListener(e -> {
                    if (nombre.equals("Historial")) { new HistorialMenor(idMenor).setVisible(true); dispose(); }
                });
                sidebar.add(btn);
                y += 70;
            }

            JButton btnFondos = new JButton("Solicitar fondos");
            btnFondos.setBounds(20, y, 250, 55);
            btnFondos.setFocusPainted(false);
            btnFondos.setBorderPainted(false);
            btnFondos.setBackground(new Color(94, 116, 73));
            btnFondos.setForeground(Color.WHITE);
            btnFondos.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnFondos.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnFondos.addActionListener(e -> { new SolicitarfondosMenor(idMenor).setVisible(true); dispose(); });
            sidebar.add(btnFondos);

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

            add(sidebar);
        }

        private void crearContenido() {
            PanelContenedorVerde contenedor = new PanelContenedorVerde();
            contenedor.setBounds(350, 40, 1300, 910);
            add(contenedor);

            JLabel lblTitulo = new JLabel("TRANSFERENCIAS");
            lblTitulo.setFont(tituloPantalla);
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setBounds(40, 30, 600, 45);
            contenedor.add(lblTitulo);

            JLabel lblDesc = new JLabel("Mueve tu dinero de forma segura configurando los parámetros de tu transferencia.");
            lblDesc.setFont(descripcionPantalla);
            lblDesc.setForeground(new Color(230, 235, 230));
            lblDesc.setBounds(40, 75, 1000, 25);
            contenedor.add(lblDesc);

            String[] opcionesCuentas = {"Cuenta de Ahorro", "Cuenta Monetaria"};
            String[] opcionesBancosOrigen = cargarBancosDelTutor();
            String[] opcionesBancosDestino = {"Banco Industrial", "Banco de América Central (BAC)", "Banrural", "Banco G&T Continental"};
            int inputWidth = 540;
            int inputHeight = 42;

            // ---------------------------------------------------
            // COLUMNA IZQUIERDA - BLOQUE 1: ORIGEN
            // ---------------------------------------------------
            PanelBloqueEstilizado pOrigen = new PanelBloqueEstilizado();
            pOrigen.setBounds(40, 120, 600, 310);
            contenedor.add(pOrigen);

            JLabel lblOrigenSecc = new JLabel("ORIGEN");
            lblOrigenSecc.setForeground(amarilloPastel);
            lblOrigenSecc.setFont(tituloSeccion);
            lblOrigenSecc.setBounds(30, 15, 200, 22);
            pOrigen.add(lblOrigenSecc);

            JLabel lblBancoO = new JLabel("Banco");
            lblBancoO.setForeground(Color.WHITE);
            lblBancoO.setFont(etiquetaCampos);
            lblBancoO.setBounds(30, 48, 200, 20);
            pOrigen.add(lblBancoO);

            cmbSelBancoO = new JComboBoxOscuro(opcionesBancosOrigen);
            cmbSelBancoO.setBounds(30, 72, inputWidth, inputHeight);
            pOrigen.add(cmbSelBancoO);

            JLabel lblNumCuentaO = new JLabel("Cuenta");
            lblNumCuentaO.setForeground(Color.WHITE);
            lblNumCuentaO.setFont(etiquetaCampos);
            lblNumCuentaO.setBounds(30, 126, 200, 20);
            pOrigen.add(lblNumCuentaO);

            txtNumCuentaO = new JTextFieldOscuro();
            txtNumCuentaO.setBounds(30, 150, inputWidth, inputHeight);
            pOrigen.add(txtNumCuentaO);

            JLabel lblTipoCuentaO = new JLabel("Tipo");
            lblTipoCuentaO.setForeground(Color.WHITE);
            lblTipoCuentaO.setFont(etiquetaCampos);
            lblTipoCuentaO.setBounds(30, 204, 200, 20);
            pOrigen.add(lblTipoCuentaO);

            cmbTipoCuentaO = new JComboBoxOscuro(opcionesCuentas);
            cmbTipoCuentaO.setBounds(30, 228, inputWidth, inputHeight);
            pOrigen.add(cmbTipoCuentaO);

            lblSaldoO = new JLabel("Saldo: Q0.00");
            lblSaldoO.setForeground(Color.WHITE);
            lblSaldoO.setFont(etiquetaCampos);
            lblSaldoO.setBounds(30, 278, 540, 20);
            pOrigen.add(lblSaldoO);


            // ---------------------------------------------------
            // COLUMNA DERECHA - BLOQUE 2: DESTINO
            // ---------------------------------------------------
            PanelBloqueEstilizado pDestino = new PanelBloqueEstilizado();
            pDestino.setBounds(660, 120, 600, 310);
            contenedor.add(pDestino);

            JLabel lblDestinoSecc = new JLabel("DESTINO");
            lblDestinoSecc.setForeground(amarilloPastel);
            lblDestinoSecc.setFont(tituloSeccion);
            lblDestinoSecc.setBounds(30, 15, 200, 22);
            pDestino.add(lblDestinoSecc);

            JLabel lblBancoD = new JLabel("Banco");
            lblBancoD.setForeground(Color.WHITE);
            lblBancoD.setFont(etiquetaCampos);
            lblBancoD.setBounds(30, 48, 200, 20);
            pDestino.add(lblBancoD);

            cmbSelBancoD = new JComboBoxOscuro(opcionesBancosDestino);
            cmbSelBancoD.setBounds(30, 72, inputWidth, inputHeight);
            pDestino.add(cmbSelBancoD);

            JLabel lblNumCuentaD = new JLabel("Cuenta");
            lblNumCuentaD.setForeground(Color.WHITE);
            lblNumCuentaD.setFont(etiquetaCampos);
            lblNumCuentaD.setBounds(30, 126, 200, 20);
            pDestino.add(lblNumCuentaD);

            txtNumCuentaD = new JTextFieldOscuro();
            txtNumCuentaD.setBounds(30, 150, inputWidth, inputHeight);
            pDestino.add(txtNumCuentaD);

            JLabel lblTipoCuentaD = new JLabel("Tipo");
            lblTipoCuentaD.setForeground(Color.WHITE);
            lblTipoCuentaD.setFont(etiquetaCampos);
            lblTipoCuentaD.setBounds(30, 204, 200, 20);
            pDestino.add(lblTipoCuentaD);

            cmbTipoCuentaD = new JComboBoxOscuro(opcionesCuentas);
            cmbTipoCuentaD.setBounds(30, 228, inputWidth, inputHeight);
            pDestino.add(cmbTipoCuentaD);

            lblTitularD = new JLabel("Titular: --");
            lblTitularD.setForeground(Color.WHITE);
            lblTitularD.setFont(etiquetaCampos);
            lblTitularD.setBounds(30, 278, 400, 20);
            pDestino.add(lblTitularD);


            // ---------------------------------------------------
            // COLUMNA IZQUIERDA - BLOQUE 3: DETALLES (MONTO / PASS)
            // ---------------------------------------------------
            PanelBloqueEstilizado pDetalles = new PanelBloqueEstilizado();
            pDetalles.setBounds(40, 450, 600, 250);
            contenedor.add(pDetalles);

            JLabel lblDetallesSecc = new JLabel("DETALLES");
            lblDetallesSecc.setForeground(amarilloPastel);
            lblDetallesSecc.setFont(tituloSeccion);
            lblDetallesSecc.setBounds(30, 15, 200, 22);
            pDetalles.add(lblDetallesSecc);

            JLabel lblMonto = new JLabel("Monto");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(etiquetaCampos);
            lblMonto.setBounds(30, 48, 200, 20);
            pDetalles.add(lblMonto);

            txtMonto = new JTextFieldOscuro();
            txtMonto.setBounds(30, 72, inputWidth, inputHeight);
            pDetalles.add(txtMonto);

            JLabel lblPassword = new JLabel("Contraseña");
            lblPassword.setForeground(Color.WHITE);
            lblPassword.setFont(etiquetaCampos);
            lblPassword.setBounds(30, 134, 200, 20);
            pDetalles.add(lblPassword);

            txtPassword = new JPasswordFieldOscuro();
            txtPassword.setBounds(30, 158, inputWidth, inputHeight);
            pDetalles.add(txtPassword);


            // ---------------------------------------------------
            // COLUMNA DERECHA - BLOQUE 4: CONVERSIÓN DE DIVISAS
            // ---------------------------------------------------
            PanelBloqueEstilizado pDivisas = new PanelBloqueEstilizado();
            pDivisas.setBounds(660, 450, 600, 250);
            contenedor.add(pDivisas);

            JLabel lblDivisasSecc = new JLabel("CONVERSIÓN DE DIVISAS");
            lblDivisasSecc.setForeground(amarilloPastel);
            lblDivisasSecc.setFont(tituloSeccion);
            lblDivisasSecc.setBounds(30, 15, 300, 22);
            pDivisas.add(lblDivisasSecc);

            String[] mOrigen = {"GTQ", "USD", "EUR"};
            String[] mDestino = {"GTQ", "USD", "EUR"};

            JLabel lblMonedaO = new JLabel("Moneda origen");
            lblMonedaO.setForeground(Color.WHITE);
            lblMonedaO.setFont(etiquetaCampos);
            lblMonedaO.setBounds(30, 48, 240, 20);
            pDivisas.add(lblMonedaO);

            cmbMonedaO = new JComboBoxOscuro(mOrigen);
            cmbMonedaO.setBounds(30, 72, 240, inputHeight);
            pDivisas.add(cmbMonedaO);

            JLabel lblMonedaD = new JLabel("Moneda destino");
            lblMonedaD.setForeground(Color.WHITE);
            lblMonedaD.setFont(etiquetaCampos); 
            lblMonedaD.setBounds(330, 48, 240, 20);
            pDivisas.add(lblMonedaD);

            cmbMonedaD = new JComboBoxOscuro(mDestino);
            cmbMonedaD.setBounds(330, 72, 240, inputHeight);
            pDivisas.add(cmbMonedaD);

            lblTipoCambio = new JLabel("Tipo de cambio: 1 GTQ = 1.00 GTQ");
            lblTipoCambio.setForeground(Color.WHITE);
            lblTipoCambio.setFont(etiquetaCampos);
            lblTipoCambio.setBounds(30, 140, 400, 20);
            pDivisas.add(lblTipoCambio);

            lblDestinatarioRecibe = new JLabel("El destinatario recibe: 0.00 GTQ");
            lblDestinatarioRecibe.setForeground(amarilloPastel);
            lblDestinatarioRecibe.setFont(tituloSeccion);
            lblDestinatarioRecibe.setBounds(30, 180, 400, 25);
            pDivisas.add(lblDestinatarioRecibe);


            // ----------------------------------------------
            // PARTE INFERIOR - BLOQUE 5: RESUMEN Y ACCIONES 
            // ----------------------------------------------
            PanelBloqueEstilizado pResumen = new PanelBloqueEstilizado();
            pResumen.setBounds(40, 720, 1220, 175); 
            contenedor.add(pResumen);

            JLabel lblResumenSecc = new JLabel("RESUMEN DE TRANSACCIÓN");
            lblResumenSecc.setForeground(amarilloPastel);
            lblResumenSecc.setFont(tituloSeccion);
            lblResumenSecc.setBounds(30, 12, 300, 22);
            pResumen.add(lblResumenSecc);

            JLabel lblSubOrigen = new JLabel("Banco de Origen");
            lblSubOrigen.setForeground(amarilloPastel);
            lblSubOrigen.setFont(etiquetaCampos);
            lblSubOrigen.setBounds(30, 42, 250, 20);
            pResumen.add(lblSubOrigen);

            lblO_Cuenta = new JLabel("Cuenta: --");
            lblO_Cuenta.setForeground(Color.WHITE);
            lblO_Cuenta.setFont(descripcionPantalla);
            lblO_Cuenta.setBounds(30, 64, 250, 20);
            pResumen.add(lblO_Cuenta);

            lblO_Nombre = new JLabel("Nombre: --");
            lblO_Nombre.setForeground(Color.WHITE);
            lblO_Nombre.setFont(descripcionPantalla);
            lblO_Nombre.setBounds(30, 84, 250, 20);
            pResumen.add(lblO_Nombre);

            lblO_Tipo = new JLabel("Tipo: --");
            lblO_Tipo.setForeground(Color.WHITE);
            lblO_Tipo.setFont(descripcionPantalla);
            lblO_Tipo.setBounds(30, 104, 250, 20);
            pResumen.add(lblO_Tipo);

            lblO_Banco = new JLabel("Banco: --");
            lblO_Banco.setForeground(Color.WHITE);
            lblO_Banco.setFont(descripcionPantalla);
            lblO_Banco.setBounds(30, 124, 250, 20);
            pResumen.add(lblO_Banco);

            lblO_Monto = new JLabel("Monto enviado: --");
            lblO_Monto.setForeground(Color.WHITE);
            lblO_Monto.setFont(descripcionPantalla);
            lblO_Monto.setBounds(30, 144, 250, 20);
            pResumen.add(lblO_Monto);


            JLabel lblSubDestino = new JLabel("Banco Destino");
            lblSubDestino.setForeground(amarilloPastel);
            lblSubDestino.setFont(etiquetaCampos);
            lblSubDestino.setBounds(450, 42, 250, 20);
            pResumen.add(lblSubDestino);

            lblD_Cuenta = new JLabel("Cuenta: --");
            lblD_Cuenta.setForeground(Color.WHITE);
            lblD_Cuenta.setFont(descripcionPantalla);
            lblD_Cuenta.setBounds(450, 64, 250, 20);
            pResumen.add(lblD_Cuenta);

            lblD_Nombre = new JLabel("Nombre: --");
            lblD_Nombre.setForeground(Color.WHITE);
            lblD_Nombre.setFont(descripcionPantalla);
            lblD_Nombre.setBounds(450, 84, 250, 20);
            pResumen.add(lblD_Nombre);

            lblD_Tipo = new JLabel("Tipo: --");
            lblD_Tipo.setForeground(Color.WHITE);
            lblD_Tipo.setFont(descripcionPantalla);
            lblD_Tipo.setBounds(450, 104, 250, 20);
            pResumen.add(lblD_Tipo);

            lblD_Banco = new JLabel("Banco: --");
            lblD_Banco.setForeground(Color.WHITE);
            lblD_Banco.setFont(descripcionPantalla);
            lblD_Banco.setBounds(450, 124, 250, 20);
            pResumen.add(lblD_Banco);

            lblD_Monto = new JLabel("Monto recibido: --");
            lblD_Monto.setForeground(Color.WHITE);
            lblD_Monto.setFont(descripcionPantalla);
            lblD_Monto.setBounds(450, 144, 250, 20);
            pResumen.add(lblD_Monto);


            // --- BOTONES DE ACCIÓN ---
            
            // Botón Imprimir (Comienza invisible y vacío por defecto)
            btnImprimir = new JButton("Imprimir comprobante");
            btnImprimir.setBounds(940, 15, 250, 35);
            btnImprimir.setForeground(Color.WHITE);
            btnImprimir.setFont(etiquetaCampos);
            btnImprimir.setContentAreaFilled(false);
            btnImprimir.setBorderPainted(false);
            btnImprimir.setFocusPainted(false);
            btnImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnImprimir.setVisible(false); // <--- Oculto de fábrica
            pResumen.add(btnImprimir);

            btnImprimir.addActionListener(e -> {
                try {
                    // Validar que ya se haya hecho la transacción
                    if (lblO_Monto.getText().contains("--") || lblD_Monto.getText().contains("--")) {
                        JOptionPane.showMessageDialog(this,
                                "Primero debes realizar la transferencia antes de imprimir el comprobante.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validar que el campo no esté vacío
                    String montoTexto = txtMonto.getText().trim();
                    if (montoTexto.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Debes ingresar un monto válido.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Convertir y formatear con separador de miles y dos decimales
                    double montoValor = Double.parseDouble(montoTexto.replace(",", "."));
                    java.text.DecimalFormat df = new java.text.DecimalFormat("Q#,##0.00");
                    String montoFormateado = df.format(montoValor);

                    // Actualizar labels
                    lblO_Monto.setText("Monto enviado: " + montoFormateado);
                    lblD_Monto.setText("Monto recibido: " + montoFormateado);

                    // Construir el texto del comprobante
                    String texto = "********************************\n"
                            + "       NEOJACKET\n"
                            + "********************************\n\n"
                            + "COMPROBANTE DE TRANSFERENCIA\n\n"
                            + "No. Operacion : " + java.util.UUID.randomUUID() + "\n"
                            + "Fecha         : " + java.time.LocalDate.now() + "\n"
                            + "Hora          : " + java.time.LocalTime.now() + "\n\n"
                            + "CUENTA ORIGEN\n"
                            + "********************************\n"
                            + lblO_Nombre.getText() + "\n"
                            + lblO_Cuenta.getText() + "\n"
                            + lblO_Banco.getText() + "\n"
                            + lblO_Tipo.getText() + "\n"
                            + lblO_Monto.getText() + "\n"
                            + "CUENTA DESTINO\n"
                            + "********************************\n"
                            + lblD_Nombre.getText() + "\n"
                            + lblD_Cuenta.getText() + "\n"
                            + lblD_Banco.getText() + "\n"
                            + lblD_Tipo.getText() + "\n"
                            + lblD_Monto.getText() + "\n\n"
                            + "Estado        : TRANSACCION EXITOSA\n\n"
                            + "Codigo Auth.  : " + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase() + "\n"
                            + "Referencia    : REF-" + java.time.LocalDate.now() + "-" + System.currentTimeMillis() + "\n\n"
                            + "********************************\n"
                            + "Este documento es un comprobante\n"
                            + "generado desde Java (ESC/POS)\n"
                            + "********************************\n\n"
                            + "¡Síguenos en nuestra red social!\n\n";

                    // Mandar a imprimir
                    funcionalidades.ImpresoraComprobante.imprimir(texto, "POS-80C");

                    // Limpiar campos SOLO después de imprimir
                    txtNumCuentaO.setText("");
                    txtNumCuentaD.setText("");
                    txtMonto.setText("");
                    txtPassword.setText("");

                    JOptionPane.showMessageDialog(this, "Comprobante enviado a la impresora.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "El monto debe ser un número válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            btnCancelar = new JButton("Cancelar");
            btnCancelar.setBounds(940, 52, 250, 35);
            btnCancelar.setForeground(new Color(240, 100, 100));
            btnCancelar.setFont(etiquetaCampos);
            btnCancelar.setContentAreaFilled(false);
            btnCancelar.setBorderPainted(false);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pResumen.add(btnCancelar);

            btnTransferir = new JButton("Realizar transferencia") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isRollover()) {
                        g2.setColor(new Color(251, 232, 138, 220));
                    } else {
                        g2.setColor(amarilloPastel);
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnTransferir.setBounds(940, 95, 250, 45);
            btnTransferir.setBackground(amarilloPastel); 
            btnTransferir.setForeground(Color.BLACK);
            btnTransferir.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTransferir.setFocusPainted(false);
            btnTransferir.setContentAreaFilled(false);
            btnTransferir.setBorderPainted(false);
            btnTransferir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // La validación de límite + ejecución real de la transferencia
            // se conecta desde funcionalidades.TransferenciasMenor (ver constructor de esta clase).

            pResumen.add(btnTransferir);
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

    /**
     * Trae los bancos vinculados a la cuenta del TUTOR/supervisor del menor
     * (no del menor). Estos son los bancos disponibles como origen, ya que
     * el dinero de una cuenta supervisada proviene de las cuentas que el
     * tutor tiene conectadas. Si el tutor conecta un banco nuevo (ej.
     * Banrural) después de crear la cuenta, aparecerá aquí automáticamente.
     */
    private String[] cargarBancosDelTutor() {
        if (idMenor == null) {
            return new String[]{"Sin bancos vinculados"};
        }
        SupervisionDAO daoSup = new SupervisionDAO();
        Integer idAdulto = daoSup.obtenerIdAdultoDeMenor(idMenor);
        if (idAdulto == null) {
            return new String[]{"Sin tutor asignado"};
        }
        CrearUsuario crear = new CrearUsuario();
        List<String> bancos = crear.obtenerBancosVinculados(idAdulto);
        if (bancos.isEmpty()) {
            return new String[]{"Sin bancos vinculados"};
        }
        return bancos.toArray(new String[0]);
    }

    public class JComboBoxOscuro extends JComboBox<String> {
        public JComboBoxOscuro(String[] items) {
            super(items);
            setFont(textoInputs);
            setForeground(Color.WHITE);
            setBackground(new Color(12, 20, 18));
            setFocusable(false);
            setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1));
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    if (isSelected) {
                        label.setBackground(amarilloPastel);
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(new Color(20, 32, 29));
                        label.setForeground(Color.WHITE);
                    }
                    return label;
                }
            });
        }
    }

    public class JTextFieldOscuro extends JTextField {
        public JTextFieldOscuro() {
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setFont(textoInputs);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(12, 20, 18, 230)); 
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.setColor(new Color(251, 232, 138, 120));
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public class JPasswordFieldOscuro extends JPasswordField {
        public JPasswordFieldOscuro() {
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setFont(textoInputs);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(12, 20, 18, 230));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.setColor(new Color(251, 232, 138, 120));
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
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
            java.util.logging.Logger.getLogger(TransferenciasMenor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            new TransferenciasMenor().setVisible(true);
        });
    }
}