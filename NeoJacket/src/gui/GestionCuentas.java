package gui;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class GestionCuentas extends JFrame {

    private Image fondo;
    private Image logo;

    // COMPONENTES PRINCIPALES
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private RoundedTextField txtCuenta;
    private JComboBox<String> cbTipo;
    private JComboBox<String> cbEstado;

    // ============================
    // COMPONENTE: TEXTFIELD REDONDEADO
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
    // COMPONENTE: BOTÓN ESTILO NEO
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

            if (getModel().isRollover()) {
                g2.setColor(new Color(251, 232, 138, 220));
                setForeground(Color.BLACK);
            } else {
                g2.setColor(new Color(94, 116, 73, 190));
                setForeground(Color.WHITE);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // COMPONENTE: BOTÓN SIDEBAR NEO
    // ============================
    class BotonSidebarNeo extends JButton {
        private final boolean activo;

        public BotonSidebarNeo(String texto, boolean activo) {
            super(texto);
            this.activo = activo;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setForeground(activo ? Color.BLACK : Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (activo) {
                g2.setColor(new Color(251, 232, 138));
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(251, 232, 138, 160));
            } else {
                g2.setColor(new Color(94, 116, 73, 190));
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR PRINCIPAL
    // ============================
    public GestionCuentas() {
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        setTitle("Gestión de Cuentas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new FondoPanel());
        setVisible(true);
    }

    // ============================
    // PANEL PRINCIPAL CON FONDO
    // ============================
    class FondoPanel extends JPanel {

        public FondoPanel() {
            setLayout(null);
            crearSidebar();
            crearPanelPrincipal();
            cargarCuentas(null, "Todos", "Todos");
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

            // Títulos del menú lateral exactamente como estaban al inicio
            String[] botones = {
                "Gestión de Usuarios",
                "Gestión de Menores Supervisados",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;

            for (String texto : botones) {
                boolean activo = texto.equals("Gestión de Cuentas");
                BotonSidebarNeo btn = new BotonSidebarNeo(texto, activo);
                btn.setBounds(20, y, 250, 55);

                btn.addActionListener(e -> {
                    if (texto.equals("Gestión de Cuentas")) {
                        aplicarFiltros();
                    } else if (texto.equals("Gestión de Usuarios")) {
                        new GestionUsuario();
                        dispose(); // Cierra la ventana actual al viajar
                    } else if (texto.equals("Gestión de Menores Supervisados")) {
                        new GestionMenores();
                        dispose(); // Cierra la ventana actual al viajar
                    } else if (texto.equals("Gestión de Tarjetas")) {
                        new GestionTarjeta();
                        dispose(); // Cierra la ventana actual al viajar
                    } else if (texto.equals("Gestión de Divisas")) {
                        new GestionDivisas();
                        dispose(); // Cierra la ventana actual al viajar
                    } else if (texto.equals("Gestión de Transacciones")) {
                        new GestionTransacciones();
                        dispose(); // Cierra la ventana actual al viajar
                    }
                });

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

            // BANNER SUPERIOR
            JPanel banner = new JPanel();
            banner.setLayout(null);
            banner.setBackground(new Color(25, 38, 35, 230));
            banner.setBounds(0, 0, 1300, 110);
            panel.add(banner);

            JLabel titulo = new JLabel("Gestión de Cuentas");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 500, 40);
            banner.add(titulo);

            JLabel subtitulo = new JLabel("Administra y consulta las cuentas registradas del sistema");
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitulo.setForeground(Color.WHITE);
            subtitulo.setBounds(30, 65, 500, 20);
            banner.add(subtitulo);

            // PANEL FILTROS
            JPanel panelFiltros = new JPanel();
            panelFiltros.setLayout(null);
            panelFiltros.setBackground(new Color(25, 38, 35, 180));
            panelFiltros.setBounds(40, 130, 1180, 120);
            panelFiltros.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            panel.add(panelFiltros);

            Color amarillo = new Color(251, 232, 138);

            JLabel lblCuenta = new JLabel("Número de cuenta");
            lblCuenta.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblCuenta.setForeground(amarillo);
            lblCuenta.setBounds(30, 15, 200, 20);
            panelFiltros.add(lblCuenta);

            JLabel lblTipo = new JLabel("Tipo");
            lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblTipo.setForeground(amarillo);
            lblTipo.setBounds(420, 15, 150, 20);
            panelFiltros.add(lblTipo);

            JLabel lblEstado = new JLabel("Estado");
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblEstado.setForeground(amarillo);
            lblEstado.setBounds(800, 15, 150, 20);
            panelFiltros.add(lblEstado);

            txtCuenta = new RoundedTextField(20);
            txtCuenta.setBounds(30, 45, 350, 40);
            panelFiltros.add(txtCuenta);

            // JCOMBOBOX ESTILIZADOS SIN FONDO BLANCO
            cbTipo = new JComboBox<>();
            estilizarComboBox(cbTipo);
            cbTipo.setBounds(420, 45, 340, 40);
            panelFiltros.add(cbTipo);
            cargarTipos();

            cbEstado = new JComboBox<>(new String[]{"Todos", "activa", "bloqueada"});
            estilizarComboBox(cbEstado);
            cbEstado.setBounds(800, 45, 340, 40);
            panelFiltros.add(cbEstado);

            // BOTONES DE ACCIÓN (ABAJO)
            int bx = 40;
            int by = 700;
            int bw = 220;
            int bh = 50;

            BotonNeo btnExplorar = new BotonNeo("Explorar lista");
            btnExplorar.setBounds(bx, by, bw, bh);
            panel.add(btnExplorar);
            btnExplorar.addActionListener(e -> {
                txtCuenta.setText("");
                cbTipo.setSelectedIndex(0);
                cbEstado.setSelectedIndex(0);
                cargarCuentas(null, "Todos", "Todos");
            });

            BotonNeo btnCrear = new BotonNeo("Crear cuenta");
            btnCrear.setBounds(bx + 240, by, bw, bh);
            panel.add(btnCrear);
            btnCrear.addActionListener(e -> {
                // Abre el diálogo de forma modal (No cierra la ventana principal)
                new DialogCrearCuenta(GestionCuentas.this);
            });

            BotonNeo btnBloquear = new BotonNeo("Bloquear cuenta");
            btnBloquear.setBounds(bx + 480, by, bw, bh);
            panel.add(btnBloquear);
            btnBloquear.addActionListener(e -> {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(this, "Por favor, selecciona una cuenta de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);
                int idCuenta = (int) modeloTabla.getValueAt(filaModelo, 0);
                String numCuenta = (String) modeloTabla.getValueAt(filaModelo, 1);
               
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas bloquear la cuenta " + numCuenta + "?", "Confirmar Bloqueo", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    actualizarEstadoCuenta(idCuenta, "bloqueada");
                }
            });

            BotonNeo btnDesbloquear = new BotonNeo("Desbloquear cuenta");
            btnDesbloquear.setBounds(bx + 720, by, bw, bh);
            panel.add(btnDesbloquear);
            btnDesbloquear.addActionListener(e -> {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(this, "Por favor, selecciona una cuenta de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);
                int idCuenta = (int) modeloTabla.getValueAt(filaModelo, 0);
                String numCuenta = (String) modeloTabla.getValueAt(filaModelo, 1);
               
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas desbloquear la cuenta " + numCuenta + "?", "Confirmar Desbloqueo", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    actualizarEstadoCuenta(idCuenta, "activa");
                }
            });

            BotonNeo btnInfo = new BotonNeo("Información cuenta");
            btnInfo.setBounds(bx + 960, by, bw, bh);
            panel.add(btnInfo);
            btnInfo.addActionListener(e -> {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(this, "Por favor, selecciona una cuenta de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);
                int idCuenta = (int) modeloTabla.getValueAt(filaModelo, 0);

                // Abre la información detallada
                new DetalleCuenta(idCuenta);
            });

            // DISPARADORES DE FILTRO REACTIVOS EN TIEMPO REAL
            cbTipo.addActionListener(e -> aplicarFiltros());
            cbEstado.addActionListener(e -> aplicarFiltros());
            txtCuenta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltros(); }
            });

            // PANEL TABLA
            JPanel panelTabla = new JPanel();
            panelTabla.setLayout(null);
            panelTabla.setBackground(new Color(25, 38, 35, 180));
            panelTabla.setBounds(40, 290, 1180, 370);
            panelTabla.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            panel.add(panelTabla);

            String[] columnas = {"ID", "CUENTA", "PROPIETARIO", "TIPO", "SALDO", "ESTADO"};

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

            // Ocultar ID del renderizado
            tabla.removeColumn(tabla.getColumnModel().getColumn(0));

            JTableHeader header = tabla.getTableHeader();
            header.setBackground(new Color(94, 116, 73));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(10, 10, 1160, 350);
            panelTabla.add(scroll);

            // BOTÓN VOLVER (Cierra la ventana actual)
            BotonNeo btnVolver = new BotonNeo("Volver");
            btnVolver.setBounds(1080, 20, 120, 40);
            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose(); // Cierra esta interfaz para no acumular ventanas
            });
            panel.add(btnVolver);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ============================
    // ESTILIZADOR DE JCOMBOBOX (Remueve fondo blanco nativo)
    // ============================
    private void estilizarComboBox(JComboBox<String> cb) {
        cb.setBackground(new Color(25, 38, 35));
        cb.setForeground(Color.WHITE);
        cb.setBorder(new LineBorder(new Color(251, 232, 138), 1));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    l.setBackground(new Color(251, 232, 138));
                    l.setForeground(Color.BLACK);
                } else {
                    l.setBackground(new Color(25, 38, 35));
                    l.setForeground(Color.WHITE);
                }
                return l;
            }
        });
    }

    // ============================
    // CARGAR CATEGORÍAS DE TIPOS DE CUENTA
    // ============================
    private void cargarTipos() {
        cbTipo.addItem("Todos");
        String sql = "SELECT nombre FROM tipos_cuentas ORDER BY nombre";

        try (Connection con = main.Conexion.conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cbTipo.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los tipos de cuenta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================
    // CONTROLADOR DE FILTROS
    // ============================
    private void aplicarFiltros() {
        String numeroCuenta = txtCuenta.getText().trim();
        String tipo = (String) cbTipo.getSelectedItem();
        String estado = (String) cbEstado.getSelectedItem();
        cargarCuentas(numeroCuenta.isEmpty() ? null : numeroCuenta, tipo, estado);
    }

    // ============================
    // CONSULTA FILTRADA DE BASE DE DATOS (Sin campos inexistentes)
    // ============================
    private void cargarCuentas(String numeroCuenta, String tipo, String estado) {
        modeloTabla.setRowCount(0);

        StringBuilder sql = new StringBuilder(
                "SELECT c.id_cuenta, c.numero_cuenta, "
                + "CONCAT(u.nombre, ' ', u.apellido) AS propietario, "
                + "t.nombre AS tipo, c.saldo, c.estado "
                + "FROM cuentas_bancarias c "
                + "JOIN usuarios u ON c.id_usuario = u.id_usuario "
                + "JOIN tipos_cuentas t ON c.id_tipo_cuenta = t.id_tipo "
                + "WHERE 1 = 1");

        List<String> parametros = new ArrayList<>();

        if (numeroCuenta != null && !numeroCuenta.isEmpty()) {
            sql.append(" AND c.numero_cuenta LIKE ?");
            parametros.add("%" + numeroCuenta + "%");
        }

        if (tipo != null && !tipo.equals("Todos")) {
            sql.append(" AND t.nombre = ?");
            parametros.add(tipo);
        }

        if (estado != null && !estado.equals("Todos")) {
            sql.append(" AND c.estado = ?");
            parametros.add(estado);
        }

        sql.append(" ORDER BY c.id_cuenta DESC");

        try (Connection con = main.Conexion.conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setString(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                        rs.getInt("id_cuenta"),
                        rs.getString("numero_cuenta"),
                        rs.getString("propietario"),
                        rs.getString("tipo"),
                        rs.getBigDecimal("saldo"),
                        rs.getString("estado")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las cuentas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================
    // ACCIÓN DIRECTA: CAMBIO DE ESTADO
    // ============================
    private void actualizarEstadoCuenta(int idCuenta, String nuevoEstado) {
        String sql = "UPDATE cuentas_bancarias SET estado = ? WHERE id_cuenta = ?";
        try (Connection con = main.Conexion.conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
           
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCuenta);
           
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas > 0) {
                JOptionPane.showMessageDialog(this, "El estado de la cuenta se ha actualizado a: " + nuevoEstado, "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                aplicarFiltros(); // Refresca en caliente
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la cuenta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================
    // MODAL: CREAR NUEVA CUENTA
    // ============================
    class DialogCrearCuenta extends JDialog {

        public DialogCrearCuenta(JFrame padre) {
            super(padre, true);

            setTitle("Crear Cuenta");
            setSize(560, 560);
            setLocationRelativeTo(padre);
            setResizable(false);

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35));
            panel.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 3, true));
            setContentPane(panel);

            JLabel titulo = new JLabel("Nueva Cuenta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(30, 20, 400, 35);
            panel.add(titulo);

            int y = 70;
            int alturaCampo = 60;

            // Propietario
            JLabel lblUsuario = new JLabel("Propietario");
            lblUsuario.setForeground(Color.WHITE);
            lblUsuario.setBounds(30, y, 200, 20);
            panel.add(lblUsuario);

            JComboBox<ComboItem> cbUsuario = new JComboBox<>();
            cbUsuario.setBounds(30, y + 22, 480, 32);
            cbUsuario.setBackground(new Color(25, 38, 35));
            cbUsuario.setForeground(Color.WHITE);
            panel.add(cbUsuario);
            y += alturaCampo;

            // Banco
            JLabel lblBanco = new JLabel("Banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setBounds(30, y, 200, 20);
            panel.add(lblBanco);

            JComboBox<ComboItem> cbBanco = new JComboBox<>();
            cbBanco.setBounds(30, y + 22, 480, 32);
            cbBanco.setBackground(new Color(25, 38, 35));
            cbBanco.setForeground(Color.WHITE);
            panel.add(cbBanco);
            y += alturaCampo;

            // Tipo de Cuenta
            JLabel lblTipoCuenta = new JLabel("Tipo de cuenta");
            lblTipoCuenta.setForeground(Color.WHITE);
            lblTipoCuenta.setBounds(30, y, 200, 20);
            panel.add(lblTipoCuenta);

            JComboBox<ComboItem> cbTipoCuenta = new JComboBox<>();
            cbTipoCuenta.setBounds(30, y + 22, 480, 32);
            cbTipoCuenta.setBackground(new Color(25, 38, 35));
            cbTipoCuenta.setForeground(Color.WHITE);
            panel.add(cbTipoCuenta);
            y += alturaCampo;

            // Moneda
            JLabel lblMoneda = new JLabel("Moneda");
            lblMoneda.setForeground(Color.WHITE);
            lblMoneda.setBounds(30, y, 200, 20);
            panel.add(lblMoneda);

            JComboBox<String> cbMoneda = new JComboBox<>();
            cbMoneda.setBounds(30, y + 22, 480, 32);
            cbMoneda.setBackground(new Color(25, 38, 35));
            cbMoneda.setForeground(Color.WHITE);
            panel.add(cbMoneda);
            y += alturaCampo;

            // Número de Cuenta
            JLabel lblNumero = new JLabel("Número de cuenta");
            lblNumero.setForeground(Color.WHITE);
            lblNumero.setBounds(30, y, 300, 20);
            panel.add(lblNumero);

            JTextField txtNumeroCuenta = new JTextField();
            txtNumeroCuenta.setBounds(30, y + 22, 480, 32);
            panel.add(txtNumeroCuenta);
            y += alturaCampo;

            // Saldo Inicial
            JLabel lblSaldo = new JLabel("Saldo inicial");
            lblSaldo.setForeground(Color.WHITE);
            lblSaldo.setBounds(30, y, 200, 20);
            panel.add(lblSaldo);

            JTextField txtSaldo = new JTextField();
            txtSaldo.setText("0.00");
            txtSaldo.setBounds(30, y + 22, 480, 32);
            panel.add(txtSaldo);
            y += alturaCampo;

            cargarUsuarios(cbUsuario);
            cargarBancos(cbBanco);
            cargarTiposCuentaCombo(cbTipoCuenta);
            cargarMonedas(cbMoneda);

            BotonNeo btnGuardar = new BotonNeo("Guardar Cuenta");
            btnGuardar.setBounds(170, y + 10, 200, 45);
            panel.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
                if (cbUsuario.getSelectedItem() == null || cbBanco.getSelectedItem() == null
                        || cbTipoCuenta.getSelectedItem() == null || cbMoneda.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Debes completar todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String numeroCuenta = txtNumeroCuenta.getText().trim();
                String saldoTexto = txtSaldo.getText().trim();

                if (numeroCuenta.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El número de cuenta es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double saldo;
                try {
                    saldo = Double.parseDouble(saldoTexto);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El saldo inicial debe ser un número válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idUsuario = ((ComboItem) cbUsuario.getSelectedItem()).getId();
                int idBanco = ((ComboItem) cbBanco.getSelectedItem()).getId();
                int idTipoCuenta = ((ComboItem) cbTipoCuenta.getSelectedItem()).getId();
                String moneda = (String) cbMoneda.getSelectedItem();

                String sql = "INSERT INTO cuentas_bancarias "
                        + "(id_usuario, id_banco, id_tipo_cuenta, moneda, numero_cuenta, saldo, estado) "
                        + "VALUES (?, ?, ?, ?, ?, ?, 'activa')";

                try (Connection con = main.Conexion.conexion.getConexion();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setInt(1, idUsuario);
                    ps.setInt(2, idBanco);
                    ps.setInt(3, idTipoCuenta);
                    ps.setString(4, moneda);
                    ps.setString(5, numeroCuenta);
                    ps.setDouble(6, saldo);

                    int filas = ps.executeUpdate();
                    if (filas > 0) {
                        JOptionPane.showMessageDialog(this, "Cuenta creada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Solo cierra el JDialog modal, NO la ventana principal
                        aplicarFiltros(); // Refresca inmediatamente la lista principal
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al crear la cuenta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            setVisible(true);
        }

        private void cargarUsuarios(JComboBox<ComboItem> combo) {
            String sql = "SELECT id_usuario, nombre, apellido FROM usuarios ORDER BY nombre";
            try (Connection con = main.Conexion.conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    combo.addItem(new ComboItem(rs.getInt("id_usuario"), rs.getString("nombre") + " " + rs.getString("apellido")));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarBancos(JComboBox<ComboItem> combo) {
            String sql = "SELECT id_banco, nombre FROM bancos WHERE activo = TRUE ORDER BY nombre";
            try (Connection con = main.Conexion.conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    combo.addItem(new ComboItem(rs.getInt("id_banco"), rs.getString("nombre")));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar bancos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarTiposCuentaCombo(JComboBox<ComboItem> combo) {
            String sql = "SELECT id_tipo, nombre FROM tipos_cuentas ORDER BY nombre";
            try (Connection con = main.Conexion.conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    combo.addItem(new ComboItem(rs.getInt("id_tipo"), rs.getString("nombre")));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar tipos de cuenta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarMonedas(JComboBox<String> combo) {
            String sql = "SELECT codigo FROM monedas WHERE activa = TRUE ORDER BY codigo";
            try (Connection con = main.Conexion.conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    combo.addItem(rs.getString("codigo"));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar monedas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ============================
    // CLASE AUXILIAR
    // ============================
    private static class ComboItem {
        private final int id;
        private final String texto;

        public ComboItem(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        public int getId() { return id; }
        @Override
        public String toString() { return texto; }
    }
}