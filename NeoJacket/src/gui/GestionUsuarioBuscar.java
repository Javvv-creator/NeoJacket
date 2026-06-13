package gui;
 
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import main.Conexion.conexion;
 
public class GestionUsuarioBuscar extends JFrame {
 
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
 
    public GestionUsuarioBuscar() {
 
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();
 
        setTitle("Buscar Usuario");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
 
                if (texto.equals("Gestión de Usuarios")) {
                    btn.setBackground(new Color(251, 232, 138));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(94, 116, 73));
                    btn.setForeground(Color.WHITE);
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
 
            JLabel titulo = new JLabel("Buscar usuarios");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            banner.add(titulo);
 
            JLabel subtitulo = new JLabel("Ingrese los criterios de búsqueda del usuario");
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 500, 20);
            banner.add(subtitulo);
 
            // PANEL 1 — CRITERIOS DE BÚSQUEDA (con borde amarillo)
            JPanel panelBusqueda = new JPanel();
            panelBusqueda.setLayout(null);
            panelBusqueda.setBackground(new Color(25, 38, 35, 180));
            panelBusqueda.setBounds(40, 130, 1180, 80);
            panelBusqueda.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 3, true));
            panel.add(panelBusqueda);
 
            // Campo: ID de Usuario
            JLabel lblId = new JLabel("ID de Usuario");
            lblId.setForeground(Color.WHITE);
            lblId.setBounds(30, 25, 120, 25);
            panelBusqueda.add(lblId);
 
            JTextField txtId = new JTextField();
            txtId.setBounds(155, 20, 180, 35);
            panelBusqueda.add(txtId);
 
            // Campo: DPI
            JLabel lblDpi = new JLabel("DPI de Usuario");
            lblDpi.setForeground(Color.WHITE);
            lblDpi.setBounds(370, 25, 130, 25);
            panelBusqueda.add(lblDpi);
 
            JTextField txtDpi = new JTextField();
            txtDpi.setBounds(505, 20, 180, 35);
            panelBusqueda.add(txtDpi);
 
            // Campo: Nombre de Usuario
            JLabel lblNombre = new JLabel("Nombre");
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setBounds(720, 25, 80, 25);
            panelBusqueda.add(lblNombre);
 
            JTextField txtNombre = new JTextField();
            txtNombre.setBounds(805, 20, 200, 35);
            panelBusqueda.add(txtNombre);
 
            // Botón Buscar dentro del panel de criterios
            BotonNeo btnBuscar = new BotonNeo("Buscar");
            btnBuscar.setBounds(1040, 15, 110, 45);
            panelBusqueda.add(btnBuscar);
 
            // PANEL 2 — TABLA DE RESULTADOS (con borde blanco)
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 230, 1180, 330);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            panel.add(panelTabla);
 
            String[] columnas = {"ID", "Usuario", "Tipo", "DPI", "Estado"};
 
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
 
            JTable tabla = new JTable(modelo);
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
 
            // PANEL 3 — DETALLE Y CAMBIO DE ESTADO (con borde amarillo)
            JPanel panelDetalle = new JPanel();
            panelDetalle.setLayout(null);
            panelDetalle.setBackground(new Color(25, 38, 35, 180));
            panelDetalle.setBounds(40, 575, 1180, 160);
            panelDetalle.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            panel.add(panelDetalle);
 
            // Fila 1: ID, Nombre, Tipo, DPI
            JLabel lblDetalleId = new JLabel("ID:");
            lblDetalleId.setForeground(new Color(251, 232, 138));
            lblDetalleId.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleId.setBounds(20, 15, 40, 20);
            panelDetalle.add(lblDetalleId);
 
            JLabel valDetalleId = new JLabel("—");
            valDetalleId.setForeground(Color.WHITE);
            valDetalleId.setBounds(60, 15, 130, 20);
            panelDetalle.add(valDetalleId);
 
            JLabel lblDetalleNombre = new JLabel("Nombre:");
            lblDetalleNombre.setForeground(new Color(251, 232, 138));
            lblDetalleNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleNombre.setBounds(210, 15, 70, 20);
            panelDetalle.add(lblDetalleNombre);
 
            JLabel valDetalleNombre = new JLabel("—");
            valDetalleNombre.setForeground(Color.WHITE);
            valDetalleNombre.setBounds(285, 15, 180, 20);
            panelDetalle.add(valDetalleNombre);
 
            JLabel lblDetalleTipo = new JLabel("Tipo:");
            lblDetalleTipo.setForeground(new Color(251, 232, 138));
            lblDetalleTipo.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleTipo.setBounds(480, 15, 45, 20);
            panelDetalle.add(lblDetalleTipo);
 
            JLabel valDetalleTipo = new JLabel("—");
            valDetalleTipo.setForeground(Color.WHITE);
            valDetalleTipo.setBounds(530, 15, 130, 20);
            panelDetalle.add(valDetalleTipo);
 
            JLabel lblDetalleDpi = new JLabel("DPI:");
            lblDetalleDpi.setForeground(new Color(251, 232, 138));
            lblDetalleDpi.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleDpi.setBounds(675, 15, 40, 20);
            panelDetalle.add(lblDetalleDpi);
 
            JLabel valDetalleDpi = new JLabel("—");
            valDetalleDpi.setForeground(Color.WHITE);
            valDetalleDpi.setBounds(720, 15, 180, 20);
            panelDetalle.add(valDetalleDpi);
 
            // Fila 2: Estado actual + Nuevo estado + botón Aplicar
            JLabel lblDetalleEstado = new JLabel("Estado actual:");
            lblDetalleEstado.setForeground(new Color(251, 232, 138));
            lblDetalleEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDetalleEstado.setBounds(20, 65, 115, 25);
            panelDetalle.add(lblDetalleEstado);
 
            JLabel valDetalleEstado = new JLabel("—");
            valDetalleEstado.setForeground(Color.WHITE);
            valDetalleEstado.setBounds(140, 65, 150, 25);
            panelDetalle.add(valDetalleEstado);
 
            JLabel lblNuevoEstado = new JLabel("Nuevo estado:");
            lblNuevoEstado.setForeground(new Color(251, 232, 138));
            lblNuevoEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblNuevoEstado.setBounds(310, 65, 115, 25);
            panelDetalle.add(lblNuevoEstado);
 
            String[] estados = {"Activo", "Inactivo", "Suspendido", "Bloqueado"};
            JComboBox<String> cmbEstado = new JComboBox<>(estados);
            cmbEstado.setBounds(430, 60, 180, 35);
            cmbEstado.setBackground(new Color(25, 38, 35));
            cmbEstado.setForeground(Color.WHITE);
            panelDetalle.add(cmbEstado);
 
            BotonNeo btnAplicar = new BotonNeo("Aplicar cambio");
            btnAplicar.setBounds(630, 55, 170, 42);
            panelDetalle.add(btnAplicar);
 
            // ---- LÓGICA: actualizar detalle al seleccionar fila ----
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
 
            // ---- LÓGICA: botón Aplicar cambio ----
            btnAplicar.addActionListener(e -> {
                if (tabla.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(this,
                        "Por favor, seleccione un usuario de la tabla antes de aplicar el cambio.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
 
                String nuevoEstado = (String) cmbEstado.getSelectedItem();
                String usuario = valDetalleNombre.getText();
 
                int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de cambiar el estado de \"" + usuario + "\" a \"" + nuevoEstado + "\"?",
                    "Confirmar cambio de estado",
                    JOptionPane.YES_NO_OPTION);
 
                if (confirmacion == JOptionPane.YES_OPTION) {
                    String idUsuario = valDetalleId.getText();
                    try (Connection con = conexion.getConexion();
                         PreparedStatement ps = con.prepareStatement(
                             "UPDATE usuarios SET estado = ? WHERE id_usuario = ?")) {
 
                        ps.setString(1, nuevoEstado.toLowerCase());
                        ps.setString(2, idUsuario);
                        int filas = ps.executeUpdate();
 
                        if (filas > 0) {
                            valDetalleEstado.setText(nuevoEstado);
                            // Actualizar también la celda en la tabla
                            int fila = tabla.getSelectedRow();
                            if (fila != -1) modelo.setValueAt(nuevoEstado.toLowerCase(), fila, 4);
 
                            JOptionPane.showMessageDialog(this,
                                "Estado de \"" + usuario + "\" actualizado a \"" + nuevoEstado + "\" correctamente.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        }
 
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this,
                            "Error al actualizar el estado:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });
 
            // ---- LÓGICA: botón Buscar ----
            btnBuscar.addActionListener(e -> {
                String idText     = txtId.getText().trim();
                String dpiText    = txtDpi.getText().trim();
                String nombreText = txtNombre.getText().trim();
 
                if (idText.isEmpty() && dpiText.isEmpty() && nombreText.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Por favor, ingrese al menos un criterio de búsqueda (ID, DPI o Nombre).",
                        "Campo requerido",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
 
                // Limpiar tabla y detalle
                modelo.setRowCount(0);
                valDetalleId.setText("—");
                valDetalleNombre.setText("—");
                valDetalleTipo.setText("—");
                valDetalleDpi.setText("—");
                valDetalleEstado.setText("—");
 
                // Construir query dinámico según campos llenos
                StringBuilder sql = new StringBuilder(
                    "SELECT u.id_usuario, u.nombre, r.nombre_rol, u.dpi_numero, u.estado " +
                    "FROM usuarios u " +
                    "LEFT JOIN roles r ON u.id_rol = r.id_rol " +
                    "WHERE 1=1 "
                );
 
                if (!idText.isEmpty())     sql.append("AND u.id_usuario = ? ");
                if (!dpiText.isEmpty())    sql.append("AND u.dpi_numero = ? ");
                if (!nombreText.isEmpty()) sql.append("AND u.nombre LIKE ? ");
 
                try (Connection con = conexion.getConexion();
                     PreparedStatement ps = con.prepareStatement(sql.toString())) {
 
                    int idx = 1;
                    if (!idText.isEmpty())     ps.setString(idx++, idText);
                    if (!dpiText.isEmpty())    ps.setString(idx++, dpiText);
                    if (!nombreText.isEmpty()) ps.setString(idx++, "%" + nombreText + "%");
 
                    ResultSet rs = ps.executeQuery();
                    boolean hayResultados = false;
 
                    while (rs.next()) {
                        hayResultados = true;
                        modelo.addRow(new Object[]{
                            rs.getString("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("nombre_rol") != null ? rs.getString("nombre_rol") : "—",
                            rs.getString("dpi_numero") != null ? rs.getString("dpi_numero") : "—",
                            rs.getString("estado")
                        });
                    }
 
                    if (!hayResultados) {
                        JOptionPane.showMessageDialog(this,
                            "No se encontraron usuarios con los criterios indicados.",
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error al consultar la base de datos:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
 
            // BOTONES de navegación
            BotonNeo btnAgregar = new BotonNeo("Agregar usuario");
            btnAgregar.setBounds(40, 745, 220, 50);
            panel.add(btnAgregar);
 
            btnAgregar.addActionListener(e -> {
                new GestionUsuarioAgregar();
                dispose();
            });
 
            BotonNeo btnEditar = new BotonNeo("Editar usuario");
            btnEditar.setBounds(300, 745, 220, 50);
            panel.add(btnEditar);
 
            btnEditar.addActionListener(e -> {
                new GestionUsuarioEditar();
                dispose();
            });
 
            BotonNeo btnDesactivar = new BotonNeo("Desactivar usuario");
            btnDesactivar.setBounds(560, 745, 220, 50);
            panel.add(btnDesactivar);
 
            btnDesactivar.addActionListener(e -> {
                new GestionUsuarioDesactivar();
                dispose();
            });
 
            // Botón Volver
            JButton btnVolver = new JButton("Volver");
            btnVolver.setBounds(1080, 20, 120, 40);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });
            panel.add(btnVolver);
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}