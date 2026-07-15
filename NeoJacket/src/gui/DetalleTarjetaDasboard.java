package gui;

import funcionalidades.AgregarTarjeta;
import funcionalidades.SupervisionDAO;
import java.awt.*;
import javax.swing.*;


public class DetalleTarjetaDasboard extends JFrame {

    private Image fondo;
    private Image logo;
    private Integer idUsuarioActual;

    private final Color amarilloPastel = new Color(251, 232, 138);

    // ============================
    // TEXTFIELD REDONDEADO
    // ============================
    class RoundedTextField extends JTextField {

        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(amarilloPastel);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN NEO (formulario)
    // ============================
    class BotonNeo extends JButton {

        private Color normal;
        private Color hover;

        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.BOLD, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN SIDEBAR (mismo estilo que Dashboard)
    // ============================
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

    // ============================
    // BOTÓN SIDEBAR estilo Cerrar sesión / Supervisión (verde/rojo/amarillo)
    // ============================
    class BotonNeoSidebar extends JButton {
        private Color normal;
        private Color hover;

        public BotonNeoSidebar(String texto, Color normal, Color hover, Color colorTexto) {
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

    // ============================
    // CONSTRUCTOR
    // ============================
    public DetalleTarjetaDasboard(Integer idUsuarioActual) {

        this.idUsuarioActual = idUsuarioActual;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Agregar Tarjeta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    // ============================
    // PANEL PRINCIPAL
    // ============================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearFormulario();
        }

        // ============================
        // SIDEBAR (idéntico al de Dashboard, NO al de admin)
        // ============================
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
                "Historial"
            };

            int y = 140;
            for (String textoBtn : botonesMenu) {
                BotonSidebarNeo btn = new BotonSidebarNeo(textoBtn);
                btn.setBounds(20, y, 250, 55);

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

                sidebar.add(btn);
                y += 70;
            }

            if (idUsuarioActual != null) {
                SupervisionDAO dao = new SupervisionDAO();
                if (dao.tieneMenoresACargo(idUsuarioActual)) {
                    BotonNeoSidebar btnSupervision = new BotonNeoSidebar(
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

            BotonNeoSidebar btnCerrarSesion = new BotonNeoSidebar(
                    "Cerrar sesión",
                    new Color(191, 76, 58),
                    new Color(214, 100, 80),
                    Color.WHITE);
            btnCerrarSesion.setBounds(20, 800, 250, 55);
            btnCerrarSesion.addActionListener(e -> {
                JOptionPane.showMessageDialog(null,
                        "✅ Sesión cerrada correctamente.\n¡Hasta pronto!",
                        "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            add(sidebar);
        }

        // ============================
        // FORMULARIO (mismo contenido que DatosTarjeta, adaptado)
        // ============================
        private void crearFormulario() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(650, 120, 550, 650);
            panel.setBorder(BorderFactory.createLineBorder(amarilloPastel, 2, true));
            add(panel);

            JLabel titulo = new JLabel("Agregar Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            panel.add(crearLabel("Tipo de tarjeta", 40, 90));
            JTextField txtTipo = crearField(40, 125);
            panel.add(txtTipo);

            panel.add(crearLabel("País de su cuenta o tarjeta", 40, 190));
            JTextField txtPais = crearField(40, 225);
            panel.add(txtPais);

            panel.add(crearLabel("Número de cuenta o tarjeta *", 40, 290));
            JTextField txtNumero = crearField(40, 325);
            panel.add(txtNumero);

            panel.add(crearLabel("Seleccione el banco", 40, 390));
            JComboBox<String> cbBanco = new JComboBox<>(new String[]{"Banco Industrial", "Banco de América Central (BAC)", "Banrural", "Banco G&T Continental"});
            cbBanco.setBounds(40, 425, 450, 50);
            cbBanco.setBackground(new Color(25, 38, 35, 200));
            cbBanco.setForeground(Color.WHITE);
            cbBanco.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            cbBanco.setFocusable(false);
            panel.add(cbBanco);

            Color amarillo = amarilloPastel;
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeo btnGuardar = new BotonNeo("Guardar", amarillo, amarilloHover);
            btnGuardar.setBounds(40, 500, 400, 55);
            panel.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
                String tipo = txtTipo.getText().trim();
                String pais = txtPais.getText().trim();
                String numero = txtNumero.getText().trim();
                String banco = cbBanco.getSelectedItem().toString();

                if (idUsuarioActual == null) {
                    JOptionPane.showMessageDialog(null,
                            "No hay sesión de usuario activa.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Usamos el idUsuario de la sesión directamente: no se
                    // busca por correo/DPI y NO se vuelven a crear las 4
                    // cuentas bancarias (esas ya existen desde el registro
                    // inicial del usuario). Solo se registra la tarjeta.
                    AgregarTarjeta servicio = new AgregarTarjeta();
                    boolean guardado = servicio.registrarTarjeta(
                            idUsuarioActual,
                            tipo,
                            pais,
                            numero,
                            banco);

                    if (guardado) {
                        JOptionPane.showMessageDialog(null,
                                "✅ Tarjeta agregada correctamente.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);

                        new Dashboard(idUsuarioActual).setVisible(true);
                        dispose();
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,
                            "❌ " + ex.getMessage(),
                            "Validación",
                            JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error al guardar los datos de la tarjeta: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            BotonNeo btnVolver = new BotonNeo("Volver al Dashboard", verde, verdeHover);
            btnVolver.setForeground(Color.WHITE);
            btnVolver.setBounds(40, 570, 400, 55);
            panel.add(btnVolver);

            btnVolver.addActionListener(e -> {
                new Dashboard(idUsuarioActual).setVisible(true);
                dispose();
            });
        }

        private JLabel crearLabel(String texto, int x, int y) {
            JLabel lbl = new JLabel(texto);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBounds(x, y, 300, 30);
            return lbl;
        }

        private RoundedTextField crearField(int x, int y) {
            RoundedTextField txt = new RoundedTextField(20);
            txt.setBounds(x, y, 450, 50);
            return txt;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
