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

    // Paleta consistente con el resto de la app
    private final Color verdeOscuro = new Color(25, 38, 35);
    private final Color amarilloPastel = new Color(251, 232, 138);
    private final Color verdeBoton = new Color(94, 116, 73);

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
                g2.setColor(new Color(251, 232, 138, 220));
            } else {
                g2.setColor(new Color(94, 116, 73, 190));
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    /**
     * Botón de sidebar estilo "contorno": fondo oscuro semitransparente,
     * borde amarillo delgado y redondeado, texto blanco en negrita — mismo
     * lenguaje visual que el sidebar de Dashboard/Transferencias/Historial
     * del resto de la app. El botón activo se distingue con relleno amarillo.
     */
    class BotonSidebarNeo extends JButton {
        private final boolean activo;

        public BotonSidebarNeo(String texto, boolean activo) {
            super(texto);
            this.activo = activo;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(activo ? Color.BLACK : Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (activo) {
                Color relleno = getModel().isRollover() ? new Color(255, 240, 190) : amarilloPastel;
                g2.setColor(relleno);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            } else {
                Color fondo = getModel().isRollover()
                        ? new Color(94, 116, 73, 90)
                        : new Color(25, 38, 35, 100);
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }

            g2.setColor(new Color(251, 232, 138, activo ? 0 : 200));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Panel contenedor con fondo oscuro semitransparente y borde amarillo
     * delgado y redondeado — reemplaza los BorderFactory.createLineBorder
     * cuadrados/gruesos que había antes.
     */
    class PanelRedondeado extends JPanel {
        private final int radio;
        private final int alphaFondo;

        public PanelRedondeado(int radio, int alphaFondo) {
            this.radio = radio;
            this.alphaFondo = alphaFondo;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(verdeOscuro.getRed(), verdeOscuro.getGreen(), verdeOscuro.getBlue(), alphaFondo));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);

            g2.setColor(new Color(251, 232, 138, 160));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Campo de texto oscuro con borde amarillo delgado y redondeado,
     * mismo estilo que el resto de formularios de la app.
     */
    class CampoTextoOscuro extends JTextField {
        public CampoTextoOscuro() {
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

            g2.setColor(new Color(251, 232, 138, 150));
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Botón "Volver" rediseñado: chip oscuro redondeado con borde amarillo
     * delgado, en vez del JButton plano por defecto que tenía antes.
     */
    class BotonVolver extends JButton {
        public BotonVolver(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(amarilloPastel);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getModel().isRollover() ? new Color(45, 60, 55) : new Color(30, 44, 40));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            g2.setColor(new Color(251, 232, 138, 180));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            g2.dispose();
            super.paintComponent(g);
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
                boolean activo = texto.equals("Gestión de Menores");
                JButton btn = new BotonSidebarNeo(texto, activo);
                btn.setBounds(20, y, 250, 55);

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

            // BANNER — mismo margen y ancho que los paneles de abajo, para que los 3 queden alineados
            PanelRedondeado banner = new PanelRedondeado(18, 230);
            banner.setLayout(null);
            banner.setBounds(40, 0, 1220, 110);
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

            // "Volver" ahora vive DENTRO del banner (no como hermano suelto)
            BotonVolver btnVolver = new BotonVolver("← Volver");
            btnVolver.setBounds(1220 - 150, 30, 120, 45);
            banner.add(btnVolver);

            btnVolver.addActionListener(e -> {
                new PanelControlAdmin();
                dispose();
            });

            // PANEL FILTROS
            PanelRedondeado panelFiltros = new PanelRedondeado(18, 180);
            panelFiltros.setLayout(null);
            panelFiltros.setBounds(40, 130, 1220, 100);
            panel.add(panelFiltros);

            JLabel lblIdMenor = new JLabel("ID (Menor)");
            lblIdMenor.setForeground(Color.WHITE);
            lblIdMenor.setBounds(30, 20, 150, 25);
            panelFiltros.add(lblIdMenor);

            // CORRECCIÓN AQUÍ: Se eliminó la re-declaración local de "JTextField"
            txtIdMenor = new CampoTextoOscuro();
            txtIdMenor.setBounds(150, 15, 200, 40);
            panelFiltros.add(txtIdMenor);

            JLabel lblIdTutor = new JLabel("ID (Tutor)");
            lblIdTutor.setForeground(Color.WHITE);
            lblIdTutor.setBounds(400, 20, 150, 25);
            panelFiltros.add(lblIdTutor);

            // CORRECCIÓN AQUÍ: Se eliminó la re-declaración local de "JTextField"
            txtIdTutor = new CampoTextoOscuro();
            txtIdTutor.setBounds(500, 15, 200, 40);
            panelFiltros.add(txtIdTutor);

            BotonNeo btnBuscar = new BotonNeo("Buscar");
            btnBuscar.setBounds(760, 15, 130, 40);
            panelFiltros.add(btnBuscar);

            BotonNeo btnLimpiar = new BotonNeo("Limpiar");
            btnLimpiar.setBounds(910, 15, 130, 40);
            panelFiltros.add(btnLimpiar);

            // PANEL TABLA
            PanelRedondeado panelTabla = new PanelRedondeado(18, 180);
            panelTabla.setLayout(null);
            panelTabla.setBounds(40, 250, 1220, 330);
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
            scroll.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1, true));
            scroll.getViewport().setBackground(new Color(25, 38, 35));
            scroll.setBounds(10, 10, 1200, 310);
            panelTabla.add(scroll);

            // BOTONES
            BotonNeo btnDetalles = new BotonNeo("Ver detalles");
            btnDetalles.setBounds(40, 600, 180, 45);
            panel.add(btnDetalles);

            BotonNeo btnDesvincular = new BotonNeo("Desvincular");
            btnDesvincular.setBounds(260, 600, 220, 50);
            panel.add(btnDesvincular);

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

            // CORRECCIÓN AQUÍ: Se cambió el filtro a s.id_menor y s.id_adulto para mayor precisión
            if (hayFiltroMenor) {
                sql.append(" AND s.id_menor = ?");
            }
            if (hayFiltroTutor) {
                sql.append(" AND s.id_adulto = ?");
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
                JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
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