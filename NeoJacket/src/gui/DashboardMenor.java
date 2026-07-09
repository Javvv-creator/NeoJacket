package gui;

import funcionalidades.SupervisionDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardMenor extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    private Integer idMenor;

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

    public DashboardMenor() { this(null); }

    public DashboardMenor(Integer idMenor) {
        this.idMenor = idMenor;
        initComponents();
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo  = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();
        setTitle("Neo Jacket - Dashboard (Menor supervisado)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
    }

    private boolean tienePermisoAprobado(String tipoAccion) {
        if (idMenor == null) return false;
        return new SupervisionDAO().tienePermisoAprobado(idMenor, tipoAccion);
    }

    private void mostrarFuncionBloqueada(String tipoAccion, String descripcion, double monto) {
        if (idMenor == null) {
            JOptionPane.showMessageDialog(this,
                "Esta función no está disponible para tu tipo de cuenta.",
                "Acceso restringido", JOptionPane.WARNING_MESSAGE);
            return;
        }
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
            dao.registrarAccionBloqueada(idMenor, tipoAccion);
            boolean enviada = dao.crearSolicitudPermiso(idMenor, idAdulto, tipoAccion, descripcion, monto);
            if (enviada) {
                JOptionPane.showMessageDialog(this,
                    "✅ Solicitud enviada a tu tutor.\nPodrás realizar esta acción cuando la apruebe.",
                    "Solicitud enviada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo enviar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            lblAviso.setBounds(20, 125, 250, 18);
            sidebar.add(lblAviso);

            String[] botonesMenu = {"Transferencias", "Divisas", "Historial"};
            int y = 155;
            for (String textoBtn : botonesMenu) {
                boolean bloqueado = textoBtn.equals("Transferencias") && !tienePermisoAprobado("transferencia");
                JButton btn = new JButton(bloqueado ? textoBtn + "  🔒" : textoBtn);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setBackground(bloqueado ? new Color(60, 60, 60) : new Color(94, 116, 73));
                btn.setForeground(bloqueado ? new Color(180, 180, 180) : Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

                if (textoBtn.equals("Transferencias")) {
                    btn.addActionListener(e -> {
                        if (tienePermisoAprobado("transferencia")) { new TransferenciasMenor(idMenor).setVisible(true); dispose(); }
                        else mostrarFuncionBloqueada("transferencia", "Realizar una transferencia", 0);
                    });
                }
                if (textoBtn.equals("Divisas")) {
                    btn.addActionListener(e -> { new DivisasMenor(idMenor).setVisible(true); dispose(); });
                }
                if (textoBtn.equals("Historial")) {
                    btn.addActionListener(e -> { new HistorialMenor(idMenor).setVisible(true); dispose(); });
                }

                sidebar.add(btn);
                y += 70;
            }

            JButton btnCerrarSesion = new JButton("Cerrar sesión");
            btnCerrarSesion.setBounds(20, 800, 250, 55);
            btnCerrarSesion.setFocusPainted(false);
            btnCerrarSesion.setBorderPainted(false);
            btnCerrarSesion.setBackground(new Color(191, 76, 58));
            btnCerrarSesion.setForeground(Color.WHITE);
            btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCerrarSesion.addActionListener(e -> {
                JOptionPane.showMessageDialog(null,
                    "✅ Sesión cerrada correctamente.\n¡Hasta pronto!",
                    "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);
            sidebar.add(btnCerrarSesion);

            add(sidebar);
        }

        private void crearContenido() {
            Font tituloTarjeta = new Font("Segoe UI", Font.BOLD, 16);
            Font texto = new Font("Segoe UI", Font.PLAIN, 14);
            Color amarilloPastel = new Color(251, 232, 138);

            JPanel contenedor = new JPanel();
            contenedor.setLayout(null);
            contenedor.setBackground(new Color(25, 38, 35, 150));
            contenedor.setBounds(350, 20, 1250, 950);
            add(contenedor);

            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1250, 60);
            barraSuperior.setBackground(amarilloPastel);
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            JLabel lblAviso = new JLabel("Modo cuenta supervisada — algunas funciones están restringidas");
            lblAviso.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblAviso.setForeground(new Color(25, 38, 35));
            lblAviso.setBounds(20, 0, 800, 60);
            barraSuperior.add(lblAviso);

            // TARJETA CUENTAS
            RoundedPanel cuentas = new RoundedPanel();
            cuentas.setBounds(40, 90, 1160, 120);
            cuentas.setBackground(new Color(25, 38, 35, 180));
            cuentas.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            cuentas.setLayout(null);
            contenedor.add(cuentas);

            JLabel lblCuentas = new JLabel("Tus Cuentas");
            lblCuentas.setForeground(amarilloPastel);
            lblCuentas.setFont(tituloTarjeta);
            lblCuentas.setBounds(20, 10, 200, 30);
            cuentas.add(lblCuentas);

            double saldoTotal = 0.0;
            if (idMenor != null) saldoTotal = new SupervisionDAO().obtenerSaldoTotal(idMenor);
            JLabel saldo = new JLabel(String.format("Saldo Disponible: Q %.2f", saldoTotal));
            saldo.setForeground(Color.WHITE);
            saldo.setFont(texto);
            saldo.setBounds(20, 50, 300, 30);
            cuentas.add(saldo);

            // TARJETA TRANSFERENCIAS
            boolean transAprobada = tienePermisoAprobado("transferencia");
            RoundedPanel transferencias = new RoundedPanel();
            transferencias.setBounds(40, 240, 560, 180);
            transferencias.setBackground(new Color(25, 38, 35, 180));
            transferencias.setBorder(BorderFactory.createLineBorder(
                transAprobada ? new Color(150, 230, 150) : Color.WHITE, 1, true));
            transferencias.setLayout(null);
            contenedor.add(transferencias);

            JLabel lblTransfer = new JLabel(transAprobada ? "Transferencias  ✅" : "Transferencias  🔒");
            lblTransfer.setForeground(amarilloPastel);
            lblTransfer.setFont(tituloTarjeta);
            lblTransfer.setBounds(15, 10, 300, 25);
            transferencias.add(lblTransfer);

            JLabel infoTransfer = new JLabel(transAprobada ? "Acción aprobada por tu tutor" : "Requiere autorización del tutor");
            infoTransfer.setForeground(transAprobada ? new Color(150, 230, 150) : new Color(200, 200, 200));
            infoTransfer.setFont(texto);
            infoTransfer.setBounds(20, 50, 500, 20);
            transferencias.add(infoTransfer);

            BotonNeo btnTransfer = new BotonNeo(transAprobada ? "Ir a Transferencias →" : "Solicitar permiso");
            btnTransfer.setBounds(20, 110, 240, 40);
            btnTransfer.addActionListener(e -> {
                if (tienePermisoAprobado("transferencia")) { new TransferenciasMenor(idMenor).setVisible(true); dispose(); }
                else mostrarFuncionBloqueada("transferencia", "Realizar una transferencia", 0);
            });
            transferencias.add(btnTransfer);

            // TARJETA DIVISAS
            RoundedPanel divisas = new RoundedPanel();
            divisas.setBounds(620, 240, 580, 180);
            divisas.setBackground(new Color(25, 38, 35, 180));
            divisas.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            divisas.setLayout(null);
            contenedor.add(divisas);

            JLabel lblDivisas = new JLabel("Cambio de Divisas");
            lblDivisas.setForeground(amarilloPastel);
            lblDivisas.setFont(tituloTarjeta);
            lblDivisas.setBounds(15, 10, 300, 25);
            divisas.add(lblDivisas);

            JLabel infoDivisas = new JLabel("Consulta tipos de cambio en tiempo real");
            infoDivisas.setForeground(new Color(200, 200, 200));
            infoDivisas.setFont(texto);
            infoDivisas.setBounds(20, 50, 500, 20);
            divisas.add(infoDivisas);

            BotonNeo btnDivisas = new BotonNeo("Ver tipos de cambio →");
            btnDivisas.setBounds(20, 110, 240, 40);
            btnDivisas.addActionListener(e -> { new DivisasMenor(idMenor).setVisible(true); dispose(); });
            divisas.add(btnDivisas);

            // TARJETA HISTORIAL con JTable
            RoundedPanel historial = new RoundedPanel();
            historial.setBounds(40, 450, 1160, 460);
            historial.setBackground(new Color(25, 38, 35, 180));
            historial.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            historial.setLayout(null);
            contenedor.add(historial);

            JLabel lblHistorial = new JLabel("Mis Movimientos Recientes");
            lblHistorial.setForeground(amarilloPastel);
            lblHistorial.setFont(tituloTarjeta);
            lblHistorial.setBounds(20, 15, 400, 28);
            historial.add(lblHistorial);

            String[] columnas = {"Tipo", "Monto", "Moneda", "Estado", "Fecha"};
            DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable tabla = new JTable(modeloTabla);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setFont(texto);
            tabla.setRowHeight(34);
            tabla.setShowGrid(false);
            tabla.setIntercellSpacing(new Dimension(0, 0));
            tabla.getTableHeader().setBackground(new Color(94, 116, 73));
            tabla.getTableHeader().setForeground(Color.WHITE);
            tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            tabla.setSelectionBackground(new Color(94, 116, 73, 120));
            tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    setForeground(Color.WHITE);
                    setBackground(sel ? new Color(94, 116, 73, 150)
                        : row % 2 == 0 ? new Color(25, 38, 35, 200) : new Color(35, 55, 45, 200));
                    setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    return this;
                }
            });

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBounds(20, 55, 1120, 350);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1));
            historial.add(scroll);

            JLabel lblEstado = new JLabel("Cargando...");
            lblEstado.setForeground(new Color(180, 180, 180));
            lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblEstado.setBounds(20, 415, 600, 20);
            historial.add(lblEstado);

            JButton btnRefrescar = new JButton("Refrescar") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(94, 116, 73));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnRefrescar.setBounds(1020, 410, 120, 32);
            btnRefrescar.setForeground(Color.WHITE);
            btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnRefrescar.setFocusPainted(false);
            btnRefrescar.setContentAreaFilled(false);
            btnRefrescar.setBorderPainted(false);
            btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            historial.add(btnRefrescar);

            Runnable cargarHistorial = () -> {
                modeloTabla.setRowCount(0);
                if (idMenor == null) { lblEstado.setText("No se puede cargar el historial."); return; }
                SupervisionDAO dao = new SupervisionDAO();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                List<SupervisionDAO.MovimientoCuenta> movs = dao.obtenerTransaccionesRecientes(idMenor, 50);
                if (movs.isEmpty()) {
                    lblEstado.setText("No hay movimientos registrados aún.");
                } else {
                    for (SupervisionDAO.MovimientoCuenta m : movs) {
                        modeloTabla.addRow(new Object[]{
                            m.tipoTransaccion,
                            String.format("Q %.2f", m.monto),
                            m.moneda, m.estado,
                            m.fecha != null ? sdf.format(m.fecha) : "—"
                        });
                    }
                    lblEstado.setText(movs.size() + " movimiento(s) encontrado(s).");
                }
            };

            cargarHistorial.run();
            btnRefrescar.addActionListener(e -> cargarHistorial.run());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class RoundedPanel extends JPanel {
        public RoundedPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardMenor().setVisible(true));
    }
}