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
    // COMBOBOX NEO
    // ============================
    class ComboNeo extends JPanel {

        private String seleccionado;
        private final JLabel lblValor;
        private final JPopupMenu popup;

        public ComboNeo(String[] opciones) {
            this.seleccionado = opciones.length > 0 ? opciones[0] : "";
            setOpaque(false);
            setLayout(new BorderLayout());
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            lblValor = new JLabel(seleccionado);
            lblValor.setForeground(Color.WHITE);
            lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblValor.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            add(lblValor, BorderLayout.CENTER);

            JLabel lblFlecha = new JLabel("▼");
            lblFlecha.setForeground(amarilloPastel);
            lblFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblFlecha.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
            add(lblFlecha, BorderLayout.EAST);

            popup = new JPopupMenu();
            popup.setBackground(new Color(25, 38, 35));
            popup.setBorder(BorderFactory.createLineBorder(amarilloPastel, 1));

            // CLAVE
            popup.setLightWeightPopupEnabled(false);

            for (String opcion : opciones) {
                JMenuItem item = new JMenuItem(opcion);
                item.setOpaque(true);
                item.setBackground(new Color(25, 38, 35));
                item.setForeground(Color.WHITE);
                item.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

                item.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        item.setBackground(amarilloPastel);
                        item.setForeground(Color.BLACK);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        item.setBackground(new Color(25, 38, 35));
                        item.setForeground(Color.WHITE);
                    }
                });

                item.addActionListener(e -> {
                    seleccionado = opcion;
                    lblValor.setText(opcion);
                });

                popup.add(item);
            }

            popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {}

                @Override
                public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        Window ventana = SwingUtilities.getWindowAncestor(ComboNeo.this);
                        if (ventana != null) {
                            ventana.repaint();
                        }
                    });
                }

                @Override
                public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
            });

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    popup.setPopupSize(getWidth(), popup.getPreferredSize().height);
                    popup.show(ComboNeo.this, 0, getHeight());
                }
            });
        }

        public String getSelectedItem() {
            return seleccionado;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(25, 38, 35, 220));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.setColor(amarilloPastel);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            g2.dispose();
            super.paintComponent(g);
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
                "Historial",
                "Agregar Tarjeta"
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
        // FORMULARIO (ahora con el campo "Tipo de cuenta" agregado)
        // ============================
        private void crearFormulario() {

            int anchoPanel = 550;
            int altoPanel = 780;

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(650, 90, anchoPanel, altoPanel);
            panel.setBorder(BorderFactory.createLineBorder(amarilloPastel, 2, true));
            add(panel);

            JLabel titulo = new JLabel("Agregar Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            panel.add(crearLabel("Tipo de tarjeta", 40, 90));
            ComboNeo cbTipo = new ComboNeo(new String[]{"Débito", "Crédito"});
            cbTipo.setBounds(40, 125, 450, 50);
            panel.add(cbTipo);

            panel.add(crearLabel("País de su cuenta o tarjeta", 40, 190));
            ComboNeo cbPais = new ComboNeo(new String[]{"Guatemala"});
            cbPais.setBounds(40, 225, 450, 50);
            panel.add(cbPais);

            panel.add(crearLabel("Número de cuenta o tarjeta *", 40, 290));
            JTextField txtNumero = crearField(40, 325);
            panel.add(txtNumero);

            panel.add(crearLabel("Seleccione el banco", 40, 390));
            ComboNeo cbBanco = new ComboNeo(new String[]{"Banco Industrial", "Banco de América Central (BAC)", "Banrural", "Banco G&T Continental"});
            cbBanco.setBounds(40, 425, 450, 50);
            panel.add(cbBanco);

            // --- NUEVO: Tipo de cuenta (mapea directo con tipos_cuentas) ---
            panel.add(crearLabel("Tipo de cuenta", 40, 490));
            ComboNeo cbTipoCuenta = new ComboNeo(new String[]{"Cuenta Monetaria", "Cuenta de Ahorro"});
            cbTipoCuenta.setBounds(40, 525, 450, 50);
            panel.add(cbTipoCuenta);

            Color amarillo = amarilloPastel;
            Color amarilloHover = new Color(255, 245, 180);

            // --- Botones centrados: ancho 400 dentro de un panel de 550 ---
            int anchoBoton = 400;
            int xCentrado = (anchoPanel - anchoBoton) / 2;

            BotonNeo btnGuardar = new BotonNeo("Guardar", amarillo, amarilloHover);
            btnGuardar.setBounds(xCentrado, 610, anchoBoton, 55);
            panel.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
                String tipo = cbTipo.getSelectedItem();
                String pais = cbPais.getSelectedItem();
                String numero = txtNumero.getText().trim();
                String banco = cbBanco.getSelectedItem();
                String tipoCuenta = cbTipoCuenta.getSelectedItem();

                if (idUsuarioActual == null) {
                    JOptionPane.showMessageDialog(null,
                            "No hay sesión de usuario activa.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    AgregarTarjeta servicio = new AgregarTarjeta();
                    boolean guardado = servicio.registrarTarjeta(
                            idUsuarioActual,
                            tipo,
                            pais,
                            numero,
                            banco,
                            tipoCuenta);

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
            btnVolver.setBounds(xCentrado, 680, anchoBoton, 55);
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