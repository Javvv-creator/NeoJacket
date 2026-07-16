package gui;

import funcionalidades.AgregarTarjeta;
import funcionalidades.CrearUsuario;
import java.awt.*;
import javax.swing.*;

public class DatosTarjeta extends JFrame {

    private Image fondo;
    private Image logo;
    private final String correoUsuario;
    private final String dpiUsuario;
    private final String tipoCuenta;

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

            // Fondo redondeado
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            // Borde redondeado
            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            g2.dispose();
            super.paintComponent(g); 
        }
    }

    // ============================
    // BOTÓN NEO
    // ============================
    class BotonNeo extends JButton {

        private final Color normal;
        private final Color hover;

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

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ============================
    // COMBOBOX NEO (CORREGIDO)
    // ============================
    class ComboNeo extends JComboBox<String> {

        public ComboNeo(String[] items) {
            super(items);
            setOpaque(false);
            setFocusable(false);
            setLightWeightPopupEnabled(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
            setForeground(Color.WHITE);
            setBackground(new Color(25, 38, 35));
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

            // Renderizador corregido para asegurar visibilidad del texto
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    
                    // Usamos super para obtener la configuración base del JLabel
                    JLabel lbl = (JLabel) super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus);

                    lbl.setOpaque(true);
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                    
                    // Colores de los elementos de la lista desplegable
                    if (isSelected) {
                        lbl.setBackground(new Color(60, 85, 70));
                        lbl.setForeground(new Color(251, 232, 138)); // Texto amarillo Neo
                    } else {
                        lbl.setBackground(new Color(25, 38, 35));
                        lbl.setForeground(Color.WHITE); // Texto blanco
                    }
                    
                    lbl.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    return lbl;
                }
            });

            setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton("▼");
                    btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    btn.setForeground(new Color(251, 232, 138));
                    btn.setBackground(new Color(25, 38, 35));
                    btn.setBorder(BorderFactory.createEmptyBorder());
                    btn.setContentAreaFilled(false);
                    btn.setFocusPainted(false);
                    return btn;
                }

                @Override
                protected javax.swing.plaf.basic.ComboPopup createPopup() {
                    javax.swing.plaf.basic.BasicComboPopup popup =
                            new javax.swing.plaf.basic.BasicComboPopup(comboBox) {
                        @Override
                        protected JScrollPane createScroller() {
                            JScrollPane scroller = new JScrollPane(list,
                                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                            
                            scroller.setOpaque(true);
                            scroller.setBackground(new Color(25, 38, 35));
                            scroller.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138)));
                            scroller.getViewport().setOpaque(true);
                            scroller.getViewport().setBackground(new Color(25, 38, 35));
                            return scroller;
                        }
                    };
                    popup.setOpaque(true);
                    popup.setBackground(new Color(25, 38, 35));
                    popup.getList().setBackground(new Color(25, 38, 35));
                    return popup;
                }
            });

            // Forzar que el editor o el texto seleccionado herede el color correcto
            if (getEditor() != null && getEditor().getEditorComponent() != null) {
                getEditor().getEditorComponent().setForeground(Color.WHITE);
                getEditor().getEditorComponent().setBackground(new Color(25, 38, 35));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Pintar fondo del combo principal
            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            // Dibujar borde dorado
            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            g2.dispose();

            // Llamamos a super para que Swing dibuje el texto seleccionado encima del fondo que creamos
            super.paintComponent(g);
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public DatosTarjeta(String correoUsuario, String dpiUsuario, String tipoCuenta) {
        this.correoUsuario = correoUsuario;
        this.dpiUsuario = dpiUsuario;
        this.tipoCuenta = tipoCuenta;

        try {
            java.net.URL urlFondo = getClass().getResource("/gui/image/fondo.png");
            java.net.URL urlLogo = getClass().getResource("/gui/image/logoblanco.png");
            if (urlFondo != null) fondo = new ImageIcon(urlFondo).getImage();
            if (urlLogo != null) logo = new ImageIcon(urlLogo).getImage();
        } catch (Exception e) {
            System.err.println("Error al cargar los recursos de imagen: " + e.getMessage());
        }

        setTitle("Neo Jacket - Datos de Tarjeta");
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

        private JPanel panelFormulario;
        private JLabel lblLogo;

        public FondoPanel() {
            setLayout(null);
            crearFormulario();
        }

        private void crearFormulario() {
            if (logo != null) {
                Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
                lblLogo = new JLabel(new ImageIcon(logoEscalado));
                lblLogo.setBounds(50, 40, 260, 120);
                add(lblLogo);
            }

            panelFormulario = new JPanel();
            panelFormulario.setLayout(null);
            panelFormulario.setBackground(new Color(25, 38, 35, 180));
            panelFormulario.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panelFormulario);

            JLabel titulo = new JLabel("Datos de Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panelFormulario.add(titulo);

            panelFormulario.add(crearLabel("Tipo de tarjeta", 40, 90));
            JTextField txtTipo = crearField(40, 125);
            panelFormulario.add(txtTipo);

            panelFormulario.add(crearLabel("País de su cuenta o tarjeta", 40, 190));
            JTextField txtPais = crearField(40, 225);
            panelFormulario.add(txtPais);

            panelFormulario.add(crearLabel("Número de cuenta o tarjeta *", 40, 290));
            JTextField txtNumero = crearField(40, 325);
            panelFormulario.add(txtNumero);

            panelFormulario.add(crearLabel("Seleccione el banco", 40, 390));
            ComboNeo cbBanco = new ComboNeo(new String[]{"Bi", "bac", "banrural", "gyt"});
            cbBanco.setBounds(40, 425, 450, 50);
            panelFormulario.add(cbBanco);

            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeo btnGuardar = new BotonNeo("Guardar", amarillo, amarilloHover);
            btnGuardar.setBounds(75, 500, 400, 55);
            panelFormulario.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
                String tipo = txtTipo.getText().trim();
                String pais = txtPais.getText().trim();
                String numero = txtNumero.getText().trim();
                String banco = cbBanco.getSelectedItem().toString();

                try {
                    CrearUsuario crear = new CrearUsuario();
                    AgregarTarjeta servicio = new AgregarTarjeta();

                    // 1. Obtener ID del usuario registrado
                    int idUsuario = crear.obtenerIdUsuario(correoUsuario, dpiUsuario);
                    if (idUsuario == -1) {
                        throw new IllegalArgumentException("No se pudo localizar el ID del usuario recién registrado.");
                    }

                    // 2. Obtener el ID del banco basándonos en el nombre seleccionado
                    Integer idBanco = crear.obtenerIdBancoPorNombre(banco);
                    if (idBanco == null) {
                        throw new IllegalArgumentException("El banco seleccionado no existe en el sistema.");
                    }

                    // 3. Crear la cuenta bancaria en la base de datos vinculando el ID de usuario, banco y número de cuenta
                    boolean cuentaCreada = crear.crearCuentaBancaria(idUsuario, idBanco, tipoCuenta, numero);

                    if (cuentaCreada) {
                        // 4. Registrar la tarjeta asociada. 
                        // El método registrarTarjeta internamente llamará a obtenerCuentaIdPorNumeroYBanco
                        // y enlazará automáticamente la tarjeta con la cuenta que acabamos de insertar.
                        boolean tarjetaGuardada = servicio.registrarTarjeta(
                                correoUsuario,
                                dpiUsuario,
                                tipo,
                                pais,
                                numero,
                                banco
                        );

                        if (tarjetaGuardada) {
                            JOptionPane.showMessageDialog(null,
                                    "✅ Cuenta bancaria y Tarjeta asociadas correctamente.",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);

                            new InicioNeo().setVisible(true);
                            dispose();
                        } else {
                            throw new Exception("La cuenta fue creada, pero falló el registro de la tarjeta.");
                        }
                    } else {
                        throw new Exception("No se pudo crear la cuenta bancaria.");
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,
                            "❌ " + ex.getMessage(),
                            "Validación",
                            JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error al procesar el registro: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            BotonNeo btnVolver = new BotonNeo("Volver al inicio", verde, verdeHover);
            btnVolver.setForeground(Color.WHITE);
            btnVolver.setBounds(75, 570, 400, 55);
            panelFormulario.add(btnVolver);

            btnVolver.addActionListener(e -> {
                new InicioNeo().setVisible(true);
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
        public void doLayout() {
            super.doLayout();
            int x = (getWidth() - 550) / 2;
            int y = (getHeight() - 650) / 2;
            if (panelFormulario != null) {
                panelFormulario.setBounds(x, y, 550, 650);
            }
            if (lblLogo != null) {
                lblLogo.setBounds(50, 40, 260, 120);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fondo != null) {
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}