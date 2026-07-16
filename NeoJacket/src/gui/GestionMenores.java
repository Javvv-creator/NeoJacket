package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.Conexion.conexion;

public class GestionMenores extends JFrame {

    private Image fondo;
    private Image logo;

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

            if (getModel().isRollover()) {
                g2.setColor(new Color(251, 232, 138, 220));
            } else {
                g2.setColor(new Color(94, 116, 73, 190));
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    public GestionMenores() {

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Menores Supervisados");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    class FondoPanel extends JPanel {

        private JTable tabla;
        private DefaultTableModel modeloTabla;
        private JTextField txtIdMenor;
        private JTextField txtIdTutor;

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearPanelPrincipal();
            cargarDatos(null, null);
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

                if (texto.equals("Gestión de Menores")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
                }
                
                 if (texto.equals("Gestión de Menores")) {
                    btn.addActionListener(e -> {
                        new GestionMenores();
                        dispose();
                    });

                 }else if (texto.equals("Gestión de Usuarios")) {
                    btn.addActionListener(e -> {
                        new GestionUsuario();
                        dispose();
                    });
                }else if (texto.equals("Gestión de Cuentas")) {
                    btn.addActionListener(e -> {
                        new GestionCuentas();
                        dispose();
                    });
                }else if (texto.equals("Gestión de Tarjetas")) {
                    btn.addActionListener(e -> {
                        new GestionTarjeta();
                        dispose();
                    });
                }else if (texto.equals("Gestión de Divisas")) {
                    btn.addActionListener(e -> {
                        new GestionDivisas();
                        dispose();
                    });
                }else if (texto.equals("Gestión de Transacciones")) {
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

        private void crearPanelPrincipal() {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 150));
            panel.setBounds(350, 60, 1300, 760);
            add(panel);

            // BANNER
            JPanel banner = new JPanel();
            banner.setLayout(null);
            banner.setBackground(new Color(25, 38, 35, 230));
            banner.setBounds(0, 0, 1300, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Gestión de Menores Supervisados");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 700, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta los menores supervisados del sistema");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 500, 20);
            banner.add(subtitulo);

            // PANEL FILTROS
            JPanel panelFiltros = new JPanel();
            panelFiltros.setLayout(null);
            panelFiltros.setBackground(new Color(25, 38, 35, 180));
            panelFiltros.setBounds(40, 130, 1180, 100);
            panelFiltros.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 3, true));
            panel.add(panelFiltros);

            JLabel lblIdMenor = new JLabel("ID (Menor)");
            lblIdMenor.setForeground(Color.WHITE);
            lblIdMenor.setBounds(30, 20, 150, 25);
            panelFiltros.add(lblIdMenor);

            txtIdMenor = new JTextField();
            txtIdMenor.setBounds(150, 15, 200, 35);
            panelFiltros.add(txtIdMenor);

            JLabel lblIdTutor = new JLabel("ID (Tutor)");
            lblIdTutor.setForeground(Color.WHITE);
            lblIdTutor.setBounds(400, 20, 150, 25);
            panelFiltros.add(lblIdTutor);

            txtIdTutor = new JTextField();
            txtIdTutor.setBounds(500, 15, 200, 35);
            panelFiltros.add(txtIdTutor);

            BotonNeo btnBuscar = new BotonNeo("Buscar");
            btnBuscar.setBounds(760, 15, 130, 35);
            panelFiltros.add(btnBuscar);

            BotonNeo btnLimpiar = new BotonNeo("Limpiar");
            btnLimpiar.setBounds(910, 15, 130, 35);
            panelFiltros.add(btnLimpiar);

            // PANEL TABLA
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 250, 1180, 330);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            panel.add(panelTabla);

            String[] columnas = {"ID", "MENOR", "ID (TUTOR)", "TUTOR"};
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

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(10, 10, 1160, 310);
            panelTabla.add(scroll);

            // BOTONES
            BotonNeo btnDetalles = new BotonNeo("Ver detalles");
            btnDetalles.setBounds(40, 600, 180, 45);
            panel.add(btnDetalles);

            BotonNeo btnDesvincular = new BotonNeo("Desvincular");
            btnDesvincular.setBounds(260, 600, 220, 50);
            panel.add(btnDesvincular);

            JButton btnVolver = new JButton("Volver");
            btnVolver.setBounds(1080, 20, 120, 40);

            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });

            panel.add(btnVolver);

            // LOGICA: Buscar
            btnBuscar.addActionListener(e -> {
                cargarDatos(txtIdMenor.getText().trim(), txtIdTutor.getText().trim());
            });

            // LOGICA: Limpiar
            btnLimpiar.addActionListener(e -> {
                txtIdMenor.setText("");
                txtIdTutor.setText("");
                cargarDatos(null, null);
            });

            // LOGICA: Boton VerDetalles
            btnDetalles.addActionListener(e -> {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(FondoPanel.this, "Por favor, selecciona un menor de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String id = String.valueOf(tabla.getValueAt(filaSeleccionada, 0));
                String menor = String.valueOf(tabla.getValueAt(filaSeleccionada, 1));
                String idTutor = String.valueOf(tabla.getValueAt(filaSeleccionada, 2));
                String tutor = String.valueOf(tabla.getValueAt(filaSeleccionada, 3));

                JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(FondoPanel.this), "Detalles del Menor", true);
                dialogo.setSize(420, 340);
                dialogo.setLocationRelativeTo(FondoPanel.this);
                dialogo.setResizable(false);
                JPanel contenido = new JPanel(null);
                contenido.setBackground(new Color(25, 38, 35));
                dialogo.setContentPane(contenido);

                JLabel lblTitulo = new JLabel("Detalles del Menor");
                lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
                lblTitulo.setForeground(new Color(251, 232, 138));
                lblTitulo.setBounds(20, 15, 350, 30);
                contenido.add(lblTitulo);

                JSeparator sep = new JSeparator();
                sep.setForeground(new Color(94, 116, 73));
                sep.setBounds(20, 50, 370, 2);
                contenido.add(sep);

                String[] etiquetas = {"ID:", "Menor:", "ID (Tutor):", "Tutor:"};
                String[] valores = {id, menor, idTutor, tutor};
                int y1 = 65;
                for (int i = 0; i < etiquetas.length; i++) {
                    JLabel lbl = new JLabel(etiquetas[i]);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    lbl.setForeground(Color.WHITE);
                    lbl.setBounds(30, y1, 120, 28);
                    contenido.add(lbl);
                    JLabel val = new JLabel(valores[i]);
                    val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    val.setForeground(new Color(251, 232, 138));
                    val.setBounds(160, y1, 220, 28);
                    contenido.add(val);
                    y1 += 38;
                }

                JButton btnCerrar = new JButton("Cerrar");
                btnCerrar.setBounds(150, 265, 110, 35);
                btnCerrar.setBackground(new Color(94, 116, 73));
                btnCerrar.setForeground(Color.WHITE);
                btnCerrar.setFocusPainted(false);
                btnCerrar.setBorderPainted(false);
                btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnCerrar.addActionListener(ev -> dialogo.dispose());
                contenido.add(btnCerrar);

                dialogo.setVisible(true);
            });

            // LOGICA: Boton Desvincular
            btnDesvincular.addActionListener(e -> {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(FondoPanel.this, "Por favor, selecciona un menor de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirmar = JOptionPane.showConfirmDialog(
                        FondoPanel.this,
                        "¿Deseas desvincular a este menor de su tutor?",
                        "Confirmar desvinculación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirmar != JOptionPane.YES_OPTION) {
                    return;
                }

                String idSupervision = String.valueOf(tabla.getValueAt(filaSeleccionada, 0));

                String sql = "UPDATE supervisiones SET activa = FALSE WHERE id_supervision = ?";

                try (Connection con = conexion.getConexion();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setInt(1, Integer.parseInt(idSupervision));
                    int filasAfectadas = ps.executeUpdate();

                    if (filasAfectadas > 0) {
                        JOptionPane.showMessageDialog(FondoPanel.this, "Menor desvinculado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatos(txtIdMenor.getText().trim(), txtIdTutor.getText().trim());
                    } else {
                        JOptionPane.showMessageDialog(FondoPanel.this, "No se pudo desvincular el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(FondoPanel.this, "Error al desvincular: " + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        private void cargarDatos(String filtroMenor, String filtroTutor) {

            modeloTabla.setRowCount(0);

            StringBuilder sql = new StringBuilder(
                    "SELECT s.id_supervision, CONCAT(m.nombre, ' ', m.apellido) AS menor, "
                    + "t.id_usuario AS id_tutor, CONCAT(t.nombre, ' ', t.apellido) AS tutor "
                    + "FROM supervisiones s "
                    + "JOIN usuarios m ON s.id_menor = m.id_usuario "
                    + "JOIN usuarios t ON s.id_adulto = t.id_usuario "
                    + "WHERE s.activa = TRUE"
            );

            boolean hayFiltroMenor = filtroMenor != null && !filtroMenor.isEmpty();
            boolean hayFiltroTutor = filtroTutor != null && !filtroTutor.isEmpty();

            if (hayFiltroMenor) {
                sql.append(" AND m.id_usuario = ?");
            }
            if (hayFiltroTutor) {
                sql.append(" AND t.id_usuario = ?");
            }

            try (Connection con = conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql.toString())) {

                int idx = 1;
                if (hayFiltroMenor) {
                    ps.setInt(idx++, Integer.parseInt(filtroMenor));
                }
                if (hayFiltroTutor) {
                    ps.setInt(idx++, Integer.parseInt(filtroTutor));
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modeloTabla.addRow(new Object[]{
                            rs.getInt("id_supervision"),
                            rs.getString("menor"),
                            rs.getInt("id_tutor"),
                            rs.getString("tutor")
                        });
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar los menores: " + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}