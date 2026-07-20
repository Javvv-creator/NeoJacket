package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import funcionalidades.CrearUsuario;
import funcionalidades.SupervisionDAO;

public class SolicitarfondosMenor extends javax.swing.JFrame {

    private Image fondo;
    private Image logo;
    private Integer idMenor;

    private final Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    private final Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);
    private final Color amarilloPastel = new Color(251, 232, 138);

    public SolicitarfondosMenor() {
        this(null);
    }

    public SolicitarfondosMenor(Integer idMenor) {
        this.idMenor = idMenor;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondoUsuario.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Solicitar Fondos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());
    }

    // --- Mismo patrón de permisos usado en TransferenciasMenor / HistorialMenor / DashboardMenor ---
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

    /** Bancos vinculados al tutor/supervisor del menor (igual que Transferencias-Origen). */
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
            lblAviso.setForeground(amarilloPastel);
            lblAviso.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblAviso.setHorizontalAlignment(SwingConstants.CENTER);
            lblAviso.setBounds(20, 122, 250, 18);
            sidebar.add(lblAviso);

            String[] botones = {"Transferencias", "Historial"};
            int y = 150;
            for (String nombre : botones) {
                boolean bloqueado = nombre.equals("Transferencias") && !tienePermisoAprobado("transferencia");
                JButton btn = new JButton(bloqueado ? nombre + "  🔒" : nombre);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                if (bloqueado) {
                    btn.setBackground(new Color(60, 60, 60));
                    btn.setForeground(new Color(180, 180, 180));
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }
                btn.addActionListener(e -> {
                    if (nombre.equals("Transferencias")) {
                        if (tienePermisoAprobado("transferencia")) { new TransferenciasMenor(idMenor).setVisible(true); dispose(); }
                        else mostrarFuncionBloqueada("transferencia", "Realizar una transferencia", 0);
                    } else if (nombre.equals("Historial")) {
                        new HistorialMenor(idMenor).setVisible(true); dispose();
                    }
                });
                sidebar.add(btn);
                y += 70;
            }

            JButton btnFondosActivo = new JButton("Solicitar fondos");
            btnFondosActivo.setBounds(20, y, 250, 55);
            btnFondosActivo.setFocusPainted(false);
            btnFondosActivo.setBorderPainted(false);
            btnFondosActivo.setBackground(amarilloPastel);
            btnFondosActivo.setForeground(Color.BLACK);
            btnFondosActivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            sidebar.add(btnFondosActivo);

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

            JLabel lblAviso = new JLabel("Modo cuenta supervisada — tu tutor debe aprobar esta solicitud");
            lblAviso.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblAviso.setForeground(new Color(25, 38, 35));
            lblAviso.setBounds(20, 0, 800, 60);
            barraSuperior.add(lblAviso);

            // Tarjeta central del formulario, mismo lenguaje visual que AgregarFondos
            JPanel cartaCampos = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(20, 28, 25, 180));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                    g2.setColor(new Color(251, 232, 138, 80));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                    g2.dispose();
                }
            };
            cartaCampos.setLayout(null);
            cartaCampos.setOpaque(false);
            cartaCampos.setBounds(400, 100, 450, 560);
            contenedor.add(cartaCampos);

            JLabel lblTitulo = new JLabel("Solicitar Fondos a tu Tutor");
            lblTitulo.setForeground(amarilloPastel);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitulo.setBounds(35, 20, 400, 28);
            cartaCampos.add(lblTitulo);

            JLabel lblBanco = new JLabel("Banco de tu tutor");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(35, 65, 380, 25);
            cartaCampos.add(lblBanco);

            JComboBox<String> cbBancos = new JComboBox<>(cargarBancosDelTutor());
            cbBancos.setBounds(35, 95, 380, 45);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(13, 18, 16));
            cbBancos.setForeground(Color.WHITE);
            cbBancos.setFocusable(false);
            cbBancos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1));
            cbBancos.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    if (isSelected) {
                        label.setBackground(amarilloPastel);
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(new Color(13, 18, 16));
                        label.setForeground(Color.WHITE);
                    }
                    return label;
                }
            });
            cartaCampos.add(cbBancos);

            JLabel lblMonto = new JLabel("Monto a solicitar");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(tituloCampos);
            lblMonto.setBounds(35, 165, 380, 25);
            cartaCampos.add(lblMonto);

            JTextFieldSolicitud txtMonto = new JTextFieldSolicitud();
            txtMonto.setBounds(35, 195, 380, 45);
            txtMonto.setFont(textoInputs);
            cartaCampos.add(txtMonto);

            JLabel lblTarjeta = new JLabel("Número de tarjeta (opcional)");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(35, 265, 380, 25);
            cartaCampos.add(lblTarjeta);

            JTextFieldSolicitud txtTarjeta = new JTextFieldSolicitud();
            txtTarjeta.setBounds(35, 295, 380, 45);
            txtTarjeta.setFont(textoInputs);
            cartaCampos.add(txtTarjeta);

            JLabel lblDescripcion = new JLabel("Motivo / Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(35, 365, 380, 25);
            cartaCampos.add(lblDescripcion);

            JTextFieldSolicitud txtDescripcion = new JTextFieldSolicitud();
            txtDescripcion.setBounds(35, 395, 380, 45);
            txtDescripcion.setFont(textoInputs);
            cartaCampos.add(txtDescripcion);

            JLabel lblNota = new JLabel("<html><div style='width:380px;'>Esta solicitud se enviará a tu tutor. "
                + "El saldo se verá reflejado en tu cuenta solo cuando la apruebe.</div></html>");
            lblNota.setForeground(new Color(180, 188, 180));
            lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblNota.setBounds(35, 450, 380, 45);
            cartaCampos.add(lblNota);

            JButton btnEnviar = new JButton("Enviar solicitud") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnEnviar.setBounds(35, 500, 380, 50);
            btnEnviar.setBackground(amarilloPastel);
            btnEnviar.setForeground(Color.BLACK);
            btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnEnviar.setFocusPainted(false);
            btnEnviar.setContentAreaFilled(false);
            btnEnviar.setBorderPainted(false);
            btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEnviar.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { btnEnviar.setBackground(new Color(255, 245, 180)); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { btnEnviar.setBackground(amarilloPastel); }
            });
            cartaCampos.add(btnEnviar);

            btnEnviar.addActionListener(e -> {
                if (idMenor == null) {
                    JOptionPane.showMessageDialog(this, "Esta función no está disponible para tu tipo de cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String montoTexto = txtMonto.getText().trim();
                if (montoTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debes ingresar un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double monto;
                try {
                    monto = Double.parseDouble(montoTexto.replace(",", "."));
                    if (monto <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser un número mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String numeroTarjeta = txtTarjeta.getText().trim();
                if (!numeroTarjeta.isEmpty() && !numeroTarjeta.matches("\\d{4,16}")) {
                    JOptionPane.showMessageDialog(this,
                        "El número de tarjeta debe contener solo dígitos (4 a 16).",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String banco = (String) cbBancos.getSelectedItem();
                String motivo = txtDescripcion.getText().trim();
                if (motivo.isEmpty()) motivo = "Solicitud de fondos";

                StringBuilder descripcionFinal = new StringBuilder(motivo);
                if (banco != null && !banco.startsWith("Sin ")) descripcionFinal.append(" — Banco: ").append(banco);
                if (!numeroTarjeta.isEmpty()) descripcionFinal.append(" — Tarjeta terminada en ")
                        .append(numeroTarjeta.length() >= 4 ? numeroTarjeta.substring(numeroTarjeta.length() - 4) : numeroTarjeta);

                SupervisionDAO dao = new SupervisionDAO();
                Integer idAdulto = dao.obtenerIdAdultoDeMenor(idMenor);
                if (idAdulto == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró un tutor asignado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dao.registrarAccionBloqueada(idMenor, "agregar_fondos");
                boolean enviada = dao.crearSolicitudPermiso(idMenor, idAdulto, "agregar_fondos",
                        descripcionFinal.toString(), monto);

                if (enviada) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Solicitud de fondos enviada a tu tutor.\nTe llegará el saldo cuando la apruebe.",
                        "Solicitud enviada", JOptionPane.INFORMATION_MESSAGE);
                    txtMonto.setText("");
                    txtTarjeta.setText("");
                    txtDescripcion.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo enviar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /** Input estilizado idéntico al usado en TransferenciasMenor/AgregarFondos. */
    class JTextFieldSolicitud extends JTextField {
        public JTextFieldSolicitud() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(13, 18, 16));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            g2.setColor(new Color(251, 232, 138, 130));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}