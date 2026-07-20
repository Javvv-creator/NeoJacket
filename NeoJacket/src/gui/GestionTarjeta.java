package gui;

import funcionalidades.ServicioTarjeta;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class GestionTarjeta extends JFrame {

    private Image fondo;
    private Image logo;

    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeFondoCampos = new Color(20, 32, 30);
    private final Color verdeBotonNormal = new Color(94, 116, 73);

    // ============================
    // TEXTFIELD REDONDEADO
    // ============================
    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(13, 20, 18, 230));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.setColor(new Color(251, 232, 138, 170));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN NEO
    // ============================
    class BotonNeo extends JButton {
        public BotonNeo(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g2.setColor(amarilloPastel);
                setForeground(Color.BLACK);
            } else {
                g2.setColor(new Color(94, 116, 73, 190));
                setForeground(Color.WHITE);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN SIDEBAR NEO
    // ============================
    class BotonSidebarNeo extends JButton {

        private boolean esSeleccionado;

        public BotonSidebarNeo(String texto, boolean esSeleccionado) {
            super(texto);
            this.esSeleccionado = esSeleccionado;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 15));

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

    // ============================
    // TARJETA CONTENEDORA REDONDEADA
    // ============================
    private JPanel crearCardPanel(Color colorBorde, int alfa, float grosor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 38, 35, 180));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 22, 22));
                g2.setColor(new Color(colorBorde.getRed(), colorBorde.getGreen(), colorBorde.getBlue(), alfa));
                g2.setStroke(new BasicStroke(grosor));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 22, 22));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(null);
        return card;
    }

    // ============================
    // ESTILIZADOR DE JCOMBOBOX
    // ============================
    private void estilizarComboBox(JComboBox<String> combo) {
        combo.setOpaque(false);
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setFocusable(false);

        // Desactivar opacidad en el renderizador del componente principal
        if (combo.getRenderer() instanceof JComponent) {
            ((JComponent) combo.getRenderer()).setOpaque(false);
        }

        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(amarilloPastel);
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

            @Override
            public void paint(Graphics g, JComponent c) {
                // Forzar letras siempre blancas en el cuadro de selección
                c.setForeground(Color.WHITE);
                super.paint(g, c);
            }
        });

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(6, 12, 6, 12));
                
                if (isSelected) {
                    lbl.setBackground(amarilloPastel);
                    lbl.setForeground(Color.BLACK);
                    lbl.setOpaque(true);
                } else {
                    lbl.setBackground(verdeFondoCampos);
                    lbl.setForeground(Color.WHITE);
                    // Evita pintar un rectángulo blanco de fondo en la caja no desplegada
                    lbl.setOpaque(index != -1);
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
                
                // Fondo oscuro translúcido redondo idéntico al de tus RoundedTextField
                g2.setColor(new Color(13, 20, 18, 230));
                g2.fillRoundRect(x, y, width - 1, height - 1, 14, 14);
                
                g2.setColor(isHover[0] || combo.hasFocus() ? amarilloPastel : new Color(251, 232, 138, 170));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(x, y, width - 1, height - 1, 14, 14);
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

    // ============================
    // CONSTRUCTOR
    // ============================
    public GestionTarjeta() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Tarjetas");
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

        private ServicioTarjeta servicio;
        private DefaultTableModel modeloTabla;
        private JTable tabla;
        private TableRowSorter<DefaultTableModel> sorter;
        private RoundedTextField txtIdFiltro;
        private JComboBox<String> cbTipo;
        private JComboBox<String> cbEstado;

        public FondoPanel() {
            servicio = new ServicioTarjeta();
            setLayout(null);
            crearSidebar();
            crearContenido();
            cargarTarjetas();
        }

        // ============================
        // SIDEBAR (NAVEGACIÓN COMPLETA ORIGINAL)
        // ============================
        private void crearSidebar() {

            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                }
            };
            sidebar.setOpaque(false);
            sidebar.setLayout(null);
            sidebar.setBounds(20, 20, 300, 950);

            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            // 1. Gestión de Usuarios
            BotonSidebarNeo btnUsuarios = new BotonSidebarNeo("Gestión de Usuarios", false);
            btnUsuarios.setBounds(20, 140, 250, 55);
            btnUsuarios.addActionListener(e -> {
                new GestionUsuario();
                dispose();
            });
            sidebar.add(btnUsuarios);

            // 2. Gestión de Menores Supervisados
            BotonSidebarNeo btnMenores = new BotonSidebarNeo("Gestión de Menores Supervis...", false);
            btnMenores.setBounds(20, 210, 250, 55);
            btnMenores.addActionListener(e -> {
                new GestionMenores();
                dispose();
            });
            sidebar.add(btnMenores);

            // 3. Gestión de Cuentas
            BotonSidebarNeo btnCuentas = new BotonSidebarNeo("Gestión de Cuentas", false);
            btnCuentas.setBounds(20, 280, 250, 55);
            btnCuentas.addActionListener(e -> {
                new GestionCuentas();
                dispose();
            });
            sidebar.add(btnCuentas);

            // 4. Gestión de Tarjetas (Seleccionada / Activa)
            BotonSidebarNeo btnTarjetas = new BotonSidebarNeo("Gestión de Tarjetas", true);
            btnTarjetas.setBounds(20, 350, 250, 55);
            btnTarjetas.addActionListener(e -> {
                new GestionTarjeta();
                dispose();
            });
            sidebar.add(btnTarjetas);

            // 5. Gestión de Divisas
            BotonSidebarNeo btnDivisas = new BotonSidebarNeo("Gestión de Divisas", false);
            btnDivisas.setBounds(20, 420, 250, 55);
            btnDivisas.addActionListener(e -> {
                new GestionDivisas();
                dispose();
            });
            sidebar.add(btnDivisas);

            // 6. Gestión de Transacciones
            BotonSidebarNeo btnTransacciones = new BotonSidebarNeo("Gestión de Transacciones", false);
            btnTransacciones.setBounds(20, 490, 250, 55);
            btnTransacciones.addActionListener(e -> {
                new GestionTransacciones();
                dispose();
            });
            sidebar.add(btnTransacciones);

            add(sidebar);
        }

        // ============================
        // CONTENIDO PRINCIPAL
        // ============================
        private void crearContenido() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setOpaque(false);
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            // BANNER
            JPanel banner = crearCardPanel(amarilloPastel, 230, 1f);
            banner.setBounds(0, 0, 1300, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Gestión de Tarjetas");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta las tarjetas registradas del sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 600, 20);
            banner.add(subtitulo);

            BotonNeo btnVolver = new BotonNeo("← Volver");
            btnVolver.setBounds(1300 - 160, 32, 120, 45);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            banner.add(btnVolver);

            Color amarillo = amarilloPastel;

            // ============================
            // PANEL FILTROS
            // ============================
            JPanel panelFiltros = crearCardPanel(amarilloPastel, 210, 1f);
            panelFiltros.setBounds(0, 130, 1300, 120);
            panel.add(panelFiltros);

            JLabel lblId = new JLabel("ID Tarjeta");
            lblId.setForeground(amarillo);
            lblId.setBounds(30, 10, 200, 20);
            panelFiltros.add(lblId);

            txtIdFiltro = new RoundedTextField(20);
            txtIdFiltro.setBounds(30, 42, 250, 42);
            panelFiltros.add(txtIdFiltro);

            JLabel lblTipo = new JLabel("Tipo");
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(430, 10, 150, 20);
            panelFiltros.add(lblTipo);

            cbTipo = new JComboBox<>(new String[]{"Todos"});
            estilizarComboBox(cbTipo);
            cbTipo.setBounds(430, 42, 250, 42);
            panelFiltros.add(cbTipo);

            JLabel lblEstado = new JLabel("Estado");
            lblEstado.setForeground(amarillo);
            lblEstado.setBounds(730, 10, 150, 20);
            panelFiltros.add(lblEstado);

            cbEstado = new JComboBox<>(new String[]{"Todos"});
            estilizarComboBox(cbEstado);
            cbEstado.setBounds(730, 42, 250, 42);
            panelFiltros.add(cbEstado);

            BotonNeo btnBuscar = new BotonNeo("Buscar");
            btnBuscar.setBounds(1030, 15, 130, 35);
            btnBuscar.addActionListener(e -> filtrarTabla());
            panelFiltros.add(btnBuscar);

            BotonNeo btnLimpiar = new BotonNeo("Limpiar");
            btnLimpiar.setBounds(1030, 58, 130, 35);
            btnLimpiar.addActionListener(e -> limpiarFiltros());
            panelFiltros.add(btnLimpiar);

            // ============================
            // TABLA
            // ============================
            JPanel panelTabla = crearCardPanel(Color.WHITE, 100, 1f);
            panelTabla.setBounds(0, 270, 1300, 330);
            panel.add(panelTabla);

            String[] columnas = {"ID", "TARJETA", "PROPIETARIO", "TIPO", "ESTADO"};

            modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tabla = new JTable(modeloTabla);
            tabla.setRowHeight(35);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(94, 116, 73));
            tabla.setSelectionBackground(new Color(251, 232, 138));
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);
            tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            sorter = new TableRowSorter<>(modeloTabla);
            tabla.setRowSorter(sorter);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(15, 15, 1270, 300);
            panelTabla.add(scroll);

            // ============================
            // BOTONES ABAJO
            // ============================
            int bx = 40;
            int by = 630;
            int bw = 250;
            int bh = 50;

            BotonNeo btnDetalles = new BotonNeo("Ver detalles");
            btnDetalles.setBounds(bx, by, bw, bh);
            btnDetalles.addActionListener(e -> {
                int idTarjeta = obtenerIdSeleccionado();
                if (idTarjeta < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Selecciona una tarjeta para ver el detalle.",
                            "Seleccionar tarjeta",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                new DetalleTarjeta(idTarjeta);
                dispose();
            });
            panel.add(btnDetalles);

            BotonNeo btnBloquear = new BotonNeo("Bloquear Tarjeta");
            btnBloquear.setBounds(bx + 300, by, bw, bh);
            btnBloquear.addActionListener(e -> {
                int idTarjeta = obtenerIdSeleccionado();
                if (idTarjeta < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Selecciona una tarjeta para bloquear.",
                            "Seleccionar tarjeta",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                new BloquearTarjeta(idTarjeta);
                dispose();
            });
            panel.add(btnBloquear);

            BotonNeo btnDesbloquear = new BotonNeo("Desbloquear Tarjeta");
            btnDesbloquear.setBounds(bx + 600, by, bw, bh);
            btnDesbloquear.addActionListener(e -> {
                int idTarjeta = obtenerIdSeleccionado();
                if (idTarjeta < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Selecciona una tarjeta para desbloquear.",
                            "Seleccionar tarjeta",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                new DesbloquearTarjeta(idTarjeta);
                dispose();
            });
            panel.add(btnDesbloquear);
        }

        private void cargarTarjetas() {
            modeloTabla.setRowCount(0);
            Object[][] datos = servicio.listarTarjetas();
            for (Object[] fila : datos) {
                modeloTabla.addRow(fila);
            }
            actualizarFiltros();
        }

        private void actualizarFiltros() {
            Set<String> tipos = new LinkedHashSet<>();
            Set<String> estados = new LinkedHashSet<>();
            tipos.add("Todos");
            estados.add("Todos");

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                tipos.add(String.valueOf(modeloTabla.getValueAt(i, 3)));
                estados.add(String.valueOf(modeloTabla.getValueAt(i, 4)));
            }

            cbTipo.setModel(new DefaultComboBoxModel<>(tipos.toArray(new String[0])));
            cbEstado.setModel(new DefaultComboBoxModel<>(estados.toArray(new String[0])));
        }

        private void filtrarTabla() {
            List<RowFilter<Object, Object>> filtros = new ArrayList<>();
            String idTexto = txtIdFiltro.getText().trim();
            if (!idTexto.isEmpty()) {
                filtros.add(RowFilter.regexFilter("^" + Pattern.quote(idTexto) + "$", 0));
            }
            if (!"Todos".equals(cbTipo.getSelectedItem())) {
                filtros.add(RowFilter.regexFilter(String.valueOf(cbTipo.getSelectedItem()), 3));
            }
            if (!"Todos".equals(cbEstado.getSelectedItem())) {
                filtros.add(RowFilter.regexFilter(String.valueOf(cbEstado.getSelectedItem()), 4));
            }

            sorter.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
        }

        private void limpiarFiltros() {
            txtIdFiltro.setText("");
            cbTipo.setSelectedIndex(0);
            cbEstado.setSelectedIndex(0);
            sorter.setRowFilter(null);
        }

        private int obtenerIdSeleccionado() {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                return -1;
            }
            int modeloFila = tabla.convertRowIndexToModel(fila);
            Object valor = modeloTabla.getValueAt(modeloFila, 0);
            if (valor instanceof Integer) {
                return (Integer) valor;
            }
            try {
                return Integer.parseInt(String.valueOf(valor));
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}