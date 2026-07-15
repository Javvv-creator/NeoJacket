package gui;

import java.awt.*;
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
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
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
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
            g2.dispose();
        }
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
            JPanel sidebar = new JPanel();
            sidebar.setLayout(null);
            sidebar.setBackground(new Color(25, 38, 35, 220));
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
                JButton btn = new JButton(texto);
                btn.setBounds(20, y, 250, 55);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);

                if (texto.equals("Gestión de Transacciones")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }

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
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(350, 120, 1200, 700);
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            add(panel);

            JLabel titulo = new JLabel("Gestión de Transacciones");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 500, 40);
            panel.add(titulo);

            // BUSCADOR
            RoundedTextField txtBuscar = new RoundedTextField(20);
            txtBuscar.setBounds(850, 25, 300, 40);
            txtBuscar.setText("Buscar transacciones...");
            panel.add(txtBuscar);

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
            tabla.setGridColor(Color.GRAY);
            tabla.setSelectionBackground(Color.GRAY);
            tabla.setSelectionForeground(Color.BLACK);
            tabla.setShowGrid(true);

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(Color.GRAY);
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 16));

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
            tabla.setRowSorter(sorter);

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(40, 90, 1120, 500);
            panel.add(scroll);

            cargarTransacciones(modeloTabla);

            // BOTONES
            Color gris = new Color(120, 120, 120);
            Color grisHover = new Color(160, 160, 160);

            BotonNeo btnDetalles = new BotonNeo("Ver detalles", gris, grisHover);
            btnDetalles.setBounds(40, 620, 200, 50);
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

            JButton btnVolver = new JButton("Volver");
            btnVolver.setBounds(1080, 20, 120, 40);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            panel.add(btnVolver);

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


