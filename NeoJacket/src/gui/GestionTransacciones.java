package gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import funcionalidades.ServicioTransaccion;

public class GestionTransacciones extends JFrame {

    private Image fondo;
    private Image logo;
    private final ServicioTransaccion servicioTransaccion = new ServicioTransaccion();

    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeBotonNormal = new Color(94, 116, 73);

    class RoundedTextField extends JTextField {
        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 12));
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(13, 20, 18, 230));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.setColor(new Color(251, 232, 138, 170));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class BotonNeo extends JButton {
        private Color normal, hover;
        public BotonNeo(String texto, Color normal, Color hover) {
            super(texto);
            this.normal = normal;
            this.hover = hover;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
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
                g2.setColor(new Color(25, 38, 35, 190));
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

    public GestionTransacciones() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Neo Jacket - Gestión de Transacciones");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearInterfaz();
        }

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

            String[] botones = {
                "Gestión de Usuarios",
                "Gestión de Menores",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;
            for (String texto : botones) {
                boolean activo = texto.equals("Gestión de Transacciones");
                BotonSidebarNeo btn = new BotonSidebarNeo(texto, activo);
                btn.setBounds(20, y, 250, 55);

                if (texto.equals("Gestión de Usuarios")) {
                    btn.addActionListener(e -> {
                        new GestionUsuario();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Menores")) {
                    btn.addActionListener(e -> {
                        new GestionMenores();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Tarjetas")) {
                    btn.addActionListener(e -> {
                        new GestionTarjeta();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Divisas")) {
                    btn.addActionListener(e -> {
                        new GestionDivisas();
                        dispose();
                    });
                } else if (texto.equals("Gestión de Transacciones")) {
                    btn.addActionListener(e -> {
                        new GestionTransacciones();
                        dispose();
                    });
                }

                sidebar.add(btn);
                y += 70;
            }

            add(sidebar);
        }

        private void crearInterfaz() {

            // PANEL
            JPanel panel = crearCardPanel(new Color(150, 150, 150), 140, 1f);
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Gestión de Transacciones");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 25, 500, 40);
            panel.add(titulo);

            // BUSCADOR
            RoundedTextField txtBuscar = new RoundedTextField(20);
            txtBuscar.setBounds(850, 25, 300, 42);
            txtBuscar.setText("Buscar transacciones...");
            panel.add(txtBuscar);

            BotonNeo btnVolver = new BotonNeo("← Volver", new Color(40, 55, 50), amarilloPastel);
            btnVolver.setBounds(1160, 25, 100, 42);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            panel.add(btnVolver);

            // TABLA
            String[] columnas = {"ID", "Fecha", "Cuenta", "Tipo", "Monto"};

            DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable tabla = new JTable(modeloTabla);
            tabla.setRowHeight(40);
            tabla.setBackground(new Color(25, 38, 35));
            tabla.setForeground(Color.WHITE);
            tabla.setGridColor(new Color(94, 116, 73));
            tabla.setSelectionBackground(amarilloPastel);
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 15));

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
            tabla.setRowSorter(sorter);

            JPanel panelTabla = crearCardPanel(Color.WHITE, 90, 1f);
            panelTabla.setBounds(40, 90, 1220, 520);
            panel.add(panelTabla);

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(15, 15, 1190, 490);
            panelTabla.add(scroll);

            cargarTransacciones(modeloTabla);

            // BOTONES
            BotonNeo btnDetalles = new BotonNeo("Ver detalles", verdeBotonNormal, amarilloPastel);
            btnDetalles.setBounds(40, 630, 200, 50);
            panel.add(btnDetalles);

            btnDetalles.addActionListener(e -> {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Selecciona una transacción de la lista.",
                            "Sin selección",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);
                long idTransaccion = (long) modeloTabla.getValueAt(filaModelo, 0);
                new DetallesTransaccion(idTransaccion);
                dispose();
            });

            txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { filtrar(); }
                @Override
                public void removeUpdate(DocumentEvent e) { filtrar(); }
                @Override
                public void changedUpdate(DocumentEvent e) { filtrar(); }

                private void filtrar() {
                    String texto = txtBuscar.getText();
                    if (texto.trim().isEmpty() || texto.equals("Buscar transacciones...")) {
                        sorter.setRowFilter(null);
                    } else {
                        try {
                            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                        } catch (PatternSyntaxException ex) {
                            sorter.setRowFilter(null);
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }

        private void cargarTransacciones(DefaultTableModel modeloTabla) {
            modeloTabla.setRowCount(0);
            Object[][] transacciones = servicioTransaccion.listarTransacciones();
            for (Object[] fila : transacciones) {
                modeloTabla.addRow(fila);
            }
        }
    }
}