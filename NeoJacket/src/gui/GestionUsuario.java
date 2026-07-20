package gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import main.Conexion.conexion;

public class GestionUsuario extends JFrame {

    private Image fondo;
    private Image logo;

    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeFondoCampos = new Color(20, 32, 30);
    private final Color verdeBotonNormal = new Color(94, 116, 73);
    private final Color bordeDelgadoBlanco = new Color(255, 255, 255, 100); // Subido un poco el blanco

    // ==========================================
    // BOTÓN DE NAVEGACIÓN DEL SIDEBAR
    // ==========================================
    class BotonSidebarNeo extends JButton {

        private boolean esSeleccionado = false;

        public BotonSidebarNeo(String texto, boolean esSeleccionado) {
            super(texto);
            this.esSeleccionado = esSeleccionado;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 16));

            if (esSeleccionado) {
                setBackground(amarilloPastel);
                setForeground(Color.BLACK);
            } else {
                setBackground(verdeBotonNormal);
                setForeground(Color.WHITE);
            }

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!esSeleccionado) {
                        setBackground(amarilloPastel);
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!esSeleccionado) {
                        setBackground(verdeBotonNormal);
                        setForeground(Color.WHITE);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            if (!esSeleccionado) {
                g2.setColor(new Color(251, 232, 138, 100));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==========================================
    // BOTÓN NEO ACCIONES ESPECIALES
    // ==========================================
    class BotonNeo extends JButton {

        private Color normal;
        private Color hover;
        private Color colorTexto;

        public BotonNeo(String texto) {
            this(texto, new Color(94, 116, 73, 190), new Color(251, 232, 138), Color.WHITE);
        }

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

            boolean isHover = getModel().isRollover();
            g2.setColor(isHover ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(isHover ? new Color(0, 0, 0, 40) : new Color(255, 255, 255, 80));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

            if (isHover) {
                setForeground(Color.BLACK);
            } else {
                setForeground(colorTexto);
            }

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ==========================================
    // MÉTODOS DE ESTILIZADO DE COMPONENTES (INPUTS AMARILLOS)
    // ==========================================
    private void estilizarCampoTexto(JTextField campo) {
        campo.setBackground(verdeFondoCampos);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(amarilloPastel); // Mismo color amarillo de la imagen
                g2.setStroke(new BasicStroke(1.5f)); // Grosor definido
                g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15); // Redondeado idéntico
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0, 0, 0, 0);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        },
                new EmptyBorder(0, 12, 0, 12)
        ));
        campo.setOpaque(false);
    }

    private void estilizarComboBox(JComboBox<String> combo) {
        combo.setBackground(verdeFondoCampos);
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Customizar comportamiento y flecha
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(amarilloPastel); // Flecha amarilla
                        int[] xPoints = {getWidth() / 2 - 5, getWidth() / 2 + 5, getWidth() / 2};
                        int[] yPoints = {getHeight() / 2 - 3, getHeight() / 2 - 3, getHeight() / 2 + 4};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                };
                btn.setContentAreaFilled(false);
                btn.setBorder(BorderFactory.createEmptyBorder());
                return btn;
            }
        });

        // Estilizar las celdas de la lista desplegable (Fondo oscuro, Selección Amarilla)
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(5, 10, 5, 10));
                if (isSelected) {
                    lbl.setBackground(amarilloPastel);
                    lbl.setForeground(Color.BLACK);
                } else {
                    lbl.setBackground(verdeFondoCampos);
                    lbl.setForeground(Color.WHITE);
                }
                return lbl;
            }
        });

        final boolean[] isHover = {false};

        combo.setBorder(new javax.swing.border.Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isHover[0] || combo.hasFocus()) {
                    g2.setColor(amarilloPastel);
                } else {
                    g2.setColor(new Color(251, 232, 138, 200));
                }

                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0, 12, 0, 12);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });

        combo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHover[0] = true;
                combo.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHover[0] = false;
                combo.repaint();
            }
        });
    }

    public GestionUsuario() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Usuarios - NeoJacket");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearPanelPrincipal();
        }

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
            sidebar.setBounds(20, 20, 300, 940);
            sidebar.setLayout(null);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 15, 250, 110);
            sidebar.add(lblLogo);

            String[] botones = {
                "Gestión de Usuarios",
                "Gestión de Menores",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 145;
            for (String texto : botones) {
                boolean esSeleccionado = texto.equals("Gestión de Usuarios");
                BotonSidebarNeo btn = new BotonSidebarNeo(texto, esSeleccionado);
                btn.setBounds(20, y, 250, 55);

                btn.addActionListener(e -> {
                    if (texto.equals("Gestión de Usuarios")) {
                        new GestionUsuario();
                        dispose();
                    } else if (texto.equals("Gestión de Menores")) {
                        new GestionMenores();
                        dispose();
                    } else if (texto.equals("Gestión de Cuentas")) {
                        new GestionCuentas();
                        dispose();
                    } else if (texto.equals("Gestión de Tarjetas")) {
                        new GestionTarjeta();
                        dispose();
                    } else if (texto.equals("Gestión de Divisas")) {
                        new GestionDivisas();
                        dispose();
                    } else if (texto.equals("Gestión de Transacciones")) {
                        new GestionTransacciones();
                        dispose();
                    }
                });

                sidebar.add(btn);
                y += 78;
            }

            BotonNeo btnCerrarSesion = new BotonNeo(
                    "Cerrar sesión",
                    new Color(191, 76, 58),
                    new Color(214, 100, 80),
                    Color.WHITE);
            btnCerrarSesion.setBounds(20, 860, 250, 55);
            btnCerrarSesion.addActionListener(e -> {
                new InicioNeo().setVisible(true);
                dispose();
            });
            sidebar.add(btnCerrarSesion);

            add(sidebar);
        }

        private void crearPanelPrincipal() {
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 150));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                }
            };
            panel.setOpaque(false);
            panel.setLayout(null);
            panel.setBounds(350, 40, 1320, 850);
            add(panel);

            JPanel banner = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 230));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                }
            };
            banner.setOpaque(false);
            banner.setLayout(null);
            banner.setBounds(0, 0, 1320, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Gestión de Usuarios");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Ingrese los criterios de búsqueda del usuario");
            subtitulo.setForeground(Color.LIGHT_GRAY);
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitulo.setBounds(30, 65, 500, 20);
            banner.add(subtitulo);

            BotonNeo btnAgregarUsuario = new BotonNeo("Agregar Usuario");
            btnAgregarUsuario.setBounds(910, 32, 180, 45);
            btnAgregarUsuario.addActionListener(e -> new DialogoAgregarUsuario());
            banner.add(btnAgregarUsuario);

            BotonNeo btnVolver = new BotonNeo("Volver", new Color(40, 55, 50), new Color(60, 80, 75), Color.WHITE);
            btnVolver.setBounds(1115, 32, 120, 45);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            banner.add(btnVolver);

            // PANEL 1 — CRITERIOS (Bordes amarillos subidos a 1.8f y más opacos)
            JPanel panelBusqueda = crearCardPanel(amarilloPastel, 210, 1.8f);
            panelBusqueda.setBounds(40, 140, 1240, 85);
            panel.add(panelBusqueda);

            JLabel lblId = new JLabel("ID de Usuario");
            lblId.setForeground(Color.WHITE);
            lblId.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblId.setBounds(30, 30, 100, 25);
            panelBusqueda.add(lblId);

            JTextField txtId = new JTextField();
            txtId.setBounds(135, 25, 170, 35);
            estilizarCampoTexto(txtId);
            panelBusqueda.add(txtId);

            JLabel lblDpi = new JLabel("DPI de Usuario");
            lblDpi.setForeground(Color.WHITE);
            lblDpi.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDpi.setBounds(340, 30, 110, 25);
            panelBusqueda.add(lblDpi);

            JTextField txtDpi = new JTextField();
            txtDpi.setBounds(455, 25, 170, 35);
            estilizarCampoTexto(txtDpi);
            panelBusqueda.add(txtDpi);

            JLabel lblNombre = new JLabel("Nombre");
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblNombre.setBounds(660, 30, 70, 25);
            panelBusqueda.add(lblNombre);

            JTextField txtNombre = new JTextField();
            txtNombre.setBounds(735, 25, 210, 35);
            estilizarCampoTexto(txtNombre);
            panelBusqueda.add(txtNombre);

            BotonNeo btnBuscar = new BotonNeo("Buscar");
            btnBuscar.setBounds(1085, 20, 120, 45);
            panelBusqueda.add(btnBuscar);

            // PANEL 2 — TABLA DE RESULTADOS (Línea blanca subida un poco menos)
            JPanel panelTabla = crearCardPanel(Color.WHITE, 100, 1.2f);
            panelTabla.setBounds(40, 250, 1240, 380);
            panel.add(panelTabla);

            String[] columnas = {"ID", "Usuario", "Tipo", "DPI", "Estado"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable tabla = new JTable(modelo);
            tabla.setRowHeight(38);
            tabla.setBackground(verdeFondoCampos);
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(255, 255, 255, 20));
            tabla.setSelectionBackground(amarilloPastel);
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(25, 38, 35));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(verdeFondoCampos);
            scroll.setBounds(15, 15, 1210, 350);
            panelTabla.add(scroll);

            // PANEL 3 — DETALLE (Bordes amarillos subidos a 1.8f)
            JPanel panelDetalle = crearCardPanel(amarilloPastel, 210, 1.8f);
            panelDetalle.setBounds(40, 655, 1240, 165);
            panel.add(panelDetalle);

            JLabel lblDetalleId = new JLabel("ID:");
            lblDetalleId.setForeground(amarilloPastel);
            lblDetalleId.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleId.setBounds(30, 20, 40, 20);
            panelDetalle.add(lblDetalleId);

            JLabel valDetalleId = new JLabel("—");
            valDetalleId.setForeground(Color.WHITE);
            valDetalleId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valDetalleId.setBounds(70, 20, 130, 20);
            panelDetalle.add(valDetalleId);

            JLabel lblDetalleNombre = new JLabel("Nombre:");
            lblDetalleNombre.setForeground(amarilloPastel);
            lblDetalleNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleNombre.setBounds(220, 20, 70, 20);
            panelDetalle.add(lblDetalleNombre);

            JLabel valDetalleNombre = new JLabel("—");
            valDetalleNombre.setForeground(Color.WHITE);
            valDetalleNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valDetalleNombre.setBounds(295, 20, 180, 20);
            panelDetalle.add(valDetalleNombre);

            JLabel lblDetalleTipo = new JLabel("Tipo:");
            lblDetalleTipo.setForeground(amarilloPastel);
            lblDetalleTipo.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleTipo.setBounds(500, 20, 45, 20);
            panelDetalle.add(lblDetalleTipo);

            JLabel valDetalleTipo = new JLabel("—");
            valDetalleTipo.setForeground(Color.WHITE);
            valDetalleTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valDetalleTipo.setBounds(550, 20, 130, 20);
            panelDetalle.add(valDetalleTipo);

            JLabel lblDetalleDpi = new JLabel("DPI:");
            lblDetalleDpi.setForeground(amarilloPastel);
            lblDetalleDpi.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleDpi.setBounds(710, 20, 40, 20);
            panelDetalle.add(lblDetalleDpi);

            JLabel valDetalleDpi = new JLabel("—");
            valDetalleDpi.setForeground(Color.WHITE);
            valDetalleDpi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valDetalleDpi.setBounds(755, 20, 180, 20);
            panelDetalle.add(valDetalleDpi);

            JLabel lblDetalleEstado = new JLabel("Estado actual:");
            lblDetalleEstado.setForeground(amarilloPastel);
            lblDetalleEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleEstado.setBounds(30, 75, 115, 25);
            panelDetalle.add(lblDetalleEstado);

            JLabel valDetalleEstado = new JLabel("—");
            valDetalleEstado.setForeground(Color.WHITE);
            valDetalleEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valDetalleEstado.setBounds(145, 75, 130, 25);
            panelDetalle.add(valDetalleEstado);

            JLabel lblNuevoEstado = new JLabel("Nuevo estado:");
            lblNuevoEstado.setForeground(amarilloPastel);
            lblNuevoEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblNuevoEstado.setBounds(300, 75, 115, 25);
            panelDetalle.add(lblNuevoEstado);

            // Declaramos primero el arreglo para que el JComboBox lo pueda leer sin error
            String[] estados = {"Activo", "Inactivo", "Suspendido", "Bloqueado"};
            JComboBox<String> cmbEstado = new JComboBox<>(estados);
            cmbEstado.setBounds(415, 70, 180, 35);
            estilizarComboBox(cmbEstado);
            panelDetalle.add(cmbEstado);

            BotonNeo btnAplicar = new BotonNeo("Aplicar cambio");
            btnAplicar.setBounds(620, 66, 170, 42);
            panelDetalle.add(btnAplicar);

            // ---- INTERACCIONES ----
            tabla.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                    int fila = tabla.getSelectedRow();
                    valDetalleId.setText(tabla.getValueAt(fila, 0) != null ? tabla.getValueAt(fila, 0).toString() : "—");
                    valDetalleNombre.setText(tabla.getValueAt(fila, 1) != null ? tabla.getValueAt(fila, 1).toString() : "—");
                    valDetalleTipo.setText(tabla.getValueAt(fila, 2) != null ? tabla.getValueAt(fila, 2).toString() : "—");
                    valDetalleDpi.setText(tabla.getValueAt(fila, 3) != null ? tabla.getValueAt(fila, 3).toString() : "—");
                    valDetalleEstado.setText(tabla.getValueAt(fila, 4) != null ? tabla.getValueAt(fila, 4).toString() : "—");
                }
            });

            btnAplicar.addActionListener(e -> {
                if (tabla.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario de la tabla.");
                    return;
                }
                String nuevoEstado = (String) cmbEstado.getSelectedItem();
                String usuario = valDetalleNombre.getText();
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Cambiar estado de \"" + usuario + "\" a \"" + nuevoEstado + "\"?");

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET estado = ? WHERE id_usuario = ?")) {
                        ps.setString(1, nuevoEstado.toLowerCase());
                        ps.setString(2, valDetalleId.getText());
                        if (ps.executeUpdate() > 0) {
                            valDetalleEstado.setText(nuevoEstado);
                            int fila = tabla.getSelectedRow();
                            if (fila != -1) {
                                modelo.setValueAt(nuevoEstado.toLowerCase(), fila, 4);
                            }
                            JOptionPane.showMessageDialog(this, "Estado actualizado con éxito.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            btnBuscar.addActionListener(e -> {
                String idText = txtId.getText().trim();
                String dpiText = txtDpi.getText().trim();
                String nombreText = txtNombre.getText().trim();

                if (idText.isEmpty() && dpiText.isEmpty() && nombreText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese al menos un criterio de búsqueda.");
                    return;
                }

                modelo.setRowCount(0);
                StringBuilder sql = new StringBuilder("SELECT u.id_usuario, u.nombre, r.nombre_rol, u.dpi_numero, u.estado FROM usuarios u LEFT JOIN roles r ON u.id_rol = r.id_rol WHERE 1=1 ");
                if (!idText.isEmpty()) {
                    sql.append("AND u.id_usuario = ? ");
                }
                if (!dpiText.isEmpty()) {
                    sql.append("AND u.dpi_numero = ? ");
                }
                if (!nombreText.isEmpty()) {
                    sql.append("AND u.nombre LIKE ? ");
                }

                try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
                    int idx = 1;
                    if (!idText.isEmpty()) {
                        ps.setString(idx++, idText);
                    }
                    if (!dpiText.isEmpty()) {
                        ps.setString(idx++, dpiText);
                    }
                    if (!nombreText.isEmpty()) {
                        ps.setString(idx++, "%" + nombreText + "%");
                    }

                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        modelo.addRow(new Object[]{
                            rs.getString("id_usuario"), rs.getString("nombre"),
                            rs.getString("nombre_rol") != null ? rs.getString("nombre_rol") : "—",
                            rs.getString("dpi_numero") != null ? rs.getString("dpi_numero") : "—",
                            rs.getString("estado")
                        });
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        }

        private JPanel crearCardPanel(Color colorBorde, int alfa, float grosor) {
            JPanel card = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 180));
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 24, 24));
                    g2.setColor(new Color(colorBorde.getRed(), colorBorde.getGreen(), colorBorde.getBlue(), alfa));
                    g2.setStroke(new BasicStroke(grosor));
                    g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 24, 24));
                    g2.dispose();
                }
            };
            card.setOpaque(false);
            card.setLayout(null);
            return card;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ==========================================
    // DIALOGO: AGREGAR USUARIO ESTILIZADO
    // ==========================================
    class DialogoAgregarUsuario extends JDialog {

        public DialogoAgregarUsuario() {
            setTitle("Agregar Usuario");
            setSize(520, 740);
            setLocationRelativeTo(null);
            setModal(true);
            setResizable(false);

            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(amarilloPastel);
                    g2.setStroke(new BasicStroke(1.8f)); // Línea del diálogo robusta
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    g2.dispose();
                }
            };
            panel.setLayout(null);
            setContentPane(panel);

            JLabel titulo = new JLabel("Nuevo Usuario");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 400, 35);
            panel.add(titulo);

            int y = 75;
            int alturaCampo = 62;

            String[] labels = {"Nombre", "Apellido", "Rol", "Correo", "Teléfono", "Fecha de nacimiento (AAAA-MM-DD)", "Género", "Contraseña", "DPI"};
            Component[] campos = new Component[9];

            campos[0] = new JTextField();
            campos[1] = new JTextField();
            campos[2] = new JComboBox<>(new String[]{"Administrador", "Cliente"});
            campos[3] = new JTextField();
            campos[4] = new JTextField();
            campos[5] = new JTextField();
            campos[6] = new JComboBox<>(new String[]{"M", "F", "Otro"});
            campos[7] = new JPasswordField();
            campos[8] = new JTextField();

            for (int i = 0; i < labels.length; i++) {
                JLabel lbl = new JLabel(labels[i]);
                lbl.setForeground(Color.LIGHT_GRAY);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lbl.setBounds(30, y, 400, 20);
                panel.add(lbl);

                campos[i].setBounds(30, y + 22, 440, 35);
                if (campos[i] instanceof JTextField) {
                    estilizarCampoTexto((JTextField) campos[i]);
                }
                if (campos[i] instanceof JComboBox) {
                    estilizarComboBox((JComboBox<String>) campos[i]);
                }
                panel.add(campos[i]);

                y += alturaCampo;
            }

            BotonNeo btnGuardar = new BotonNeo("Guardar Usuario");
            btnGuardar.setBounds(150, y + 10, 200, 45);
            panel.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
                String nombre = ((JTextField) campos[0]).getText().trim();
                String apellido = ((JTextField) campos[1]).getText().trim();
                String rolSeleccionado = (String) ((JComboBox<?>) campos[2]).getSelectedItem();
                String correo = ((JTextField) campos[3]).getText().trim();
                String telefono = ((JTextField) campos[4]).getText().trim();
                String fecha = ((JTextField) campos[5]).getText().trim();
                String genero = (String) ((JComboBox<?>) campos[6]).getSelectedItem();
                String password = new String(((JPasswordField) campos[7]).getPassword());
                String dpi = ((JTextField) campos[8]).getText().trim();

                if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || fecha.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.");
                    return;
                }

                int idRol = "Administrador".equals(rolSeleccionado) ? 1 : 2;

                try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO usuarios (id_rol, nombre, apellido, correo, telefono, fecha_nacimiento, genero, password_hash, dpi_numero) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                    ps.setInt(1, idRol);
                    ps.setString(2, nombre);
                    ps.setString(3, apellido);
                    ps.setString(4, correo);
                    ps.setString(5, telefono.isEmpty() ? null : telefono);
                    ps.setString(6, fecha);
                    ps.setString(7, genero);
                    ps.setString(8, password);
                    ps.setString(9, dpi.isEmpty() ? null : dpi);

                    if (ps.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Usuario agregado correctamente.");
                        dispose();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            setVisible(true);
        }
    }
}
