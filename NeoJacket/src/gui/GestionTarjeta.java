package gui;

import funcionalidades.ServicioTarjeta;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class GestionTarjeta extends JFrame {

    private Image fondo;
    private Image logo;

    // ============================
    // TEXTFIELD REDONDEADO
    // ============================
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
        // SIDEBAR
        // ============================
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

                btn.setBackground(new Color(94, 116, 73));
                btn.setForeground(Color.WHITE);

                if (texto.equals("Gestión de Tarjetas")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
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

        // ============================
        // CONTENIDO PRINCIPAL
        // ============================
        private void crearContenido() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            JLabel titulo = new JLabel("Gestión de Tarjetas");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            panel.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta las tarjetas registradas del sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 600, 20);
            panel.add(subtitulo);

            Color amarillo = new Color(251, 232, 138);

            // ============================
            // PANEL FILTROS
            // ============================
            JPanel panelFiltros = new JPanel();
            panelFiltros.setLayout(null);
            panelFiltros.setBackground(new Color(25, 38, 35, 180));
            panelFiltros.setBounds(40, 130, 1180, 120);
            panelFiltros.setBorder(BorderFactory.createLineBorder(amarillo, 2, true));
            panel.add(panelFiltros);

            JLabel lblId = new JLabel("ID Tarjeta");
            lblId.setForeground(amarillo);
            lblId.setBounds(30, 10, 200, 20);
            panelFiltros.add(lblId);

            txtIdFiltro = new RoundedTextField(20);
            txtIdFiltro.setBounds(30, 40, 250, 40);
            panelFiltros.add(txtIdFiltro);

            JLabel lblTipo = new JLabel("Tipo");
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(430, 10, 150, 20);
            panelFiltros.add(lblTipo);

            cbTipo = new JComboBox<>(new String[]{"Todos"});
            cbTipo.setBounds(430, 40, 250, 40);
            panelFiltros.add(cbTipo);

            JLabel lblEstado = new JLabel("Estado");
            lblEstado.setForeground(amarillo);
            lblEstado.setBounds(730, 10, 150, 20);
            panelFiltros.add(lblEstado);

            cbEstado = new JComboBox<>(new String[]{"Todos"});
            cbEstado.setBounds(730, 40, 250, 40);
            panelFiltros.add(cbEstado);

            BotonNeo btnBuscar = new BotonNeo("Buscar");
            btnBuscar.setBounds(1010, 40, 120, 40);
            btnBuscar.addActionListener(e -> filtrarTabla());
            panelFiltros.add(btnBuscar);

            BotonNeo btnLimpiar = new BotonNeo("Limpiar");
            btnLimpiar.setBounds(1010, 80, 120, 40);
            btnLimpiar.addActionListener(e -> limpiarFiltros());
            panelFiltros.add(btnLimpiar);

            // ============================
            // TABLA
            // ============================
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 270, 1180, 330);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
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
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(10, 10, 1160, 310);
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

            // ============================
            // BOTÓN VOLVER
            // ============================
            JButton btnVolver = new JButton("Volver");
            btnVolver.setBounds(1080, 20, 120, 40);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            panel.add(btnVolver);
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


