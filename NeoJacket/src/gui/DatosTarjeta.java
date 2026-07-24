package gui;

import funcionalidades.AgregarTarjeta;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.*;

public class DatosTarjeta extends JFrame {

    private Image fondo;
    private Image logo;
    private String correoUsuario;
    private String dpiUsuario;
    private String tipoCuenta;

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

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // CAMPO DE TARJETA ENMASCARADO (estilo password, con "*")
    // ============================
    class RoundedCardField extends JPasswordField {

        public RoundedCardField(int size) {
            super(size);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            // Deja espacio a la derecha para el botón del ojo
            setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 46));
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
            setEchoChar('*'); // oculto por defecto: ****************
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(new Color(251, 232, 138));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN "OJO" PARA MOSTRAR/OCULTAR LA TARJETA
    // ============================
    class BotonOjo extends JToggleButton {

        private final Color colorIcono = new Color(251, 232, 138);

        public BotonOjo() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setToolTipText("Mostrar número");
            addItemListener(e -> setToolTipText(isSelected() ? "Ocultar número" : "Mostrar número"));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorIcono);
            g2.setStroke(new BasicStroke(2f));

            int w = getWidth();
            int h = getHeight();
            int cx = w / 2;
            int cy = h / 2;
            int eyeW = 20;
            int eyeH = 12;

            if (isSelected()) {
                // Ojo abierto: número visible
                g2.drawOval(cx - eyeW / 2, cy - eyeH / 2, eyeW, eyeH);
                g2.fillOval(cx - 2, cy - 2, 4, 4);
            } else {
                // Ojo cerrado (con línea diagonal): número oculto
                g2.drawArc(cx - eyeW / 2, cy - eyeH / 2, eyeW, eyeH, 0, 180);
                g2.drawLine(cx - eyeW / 2 - 2, cy + 6, cx + eyeW / 2 + 2, cy - 6);
            }
            g2.dispose();
        }
    }

    // ============================
    // BOTÓN NEO
    // ============================
    class BotonNeo extends JButton {

        private Color normal;
        private Color hover;

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

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // ============================
    // COMBOBOX NEO
    // ============================
    class ComboNeo extends JComboBox<String> {

        // Colores centralizados para que el popup y el botón de flecha
        // usen siempre el mismo tono oscuro y nunca se vea blanco.
        private final Color colorFondo = new Color(25, 38, 35);
        private final Color colorSeleccion = new Color(60, 85, 70);
        private final Color colorAcento = new Color(251, 232, 138);

        public ComboNeo(String[] items) {
            super(items);
            setOpaque(false);
            setEditable(false);
            setFocusable(false);
            setLightWeightPopupEnabled(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 18));
            setForeground(Color.WHITE);
            setBackground(colorFondo);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

            // Forzamos estos valores también a nivel de UIManager para que
            // el look and feel no pinte de blanco ningún borde/sombra
            // interna del combo o de su popup.
            UIManager.put("ComboBox.background", colorFondo);
            UIManager.put("ComboBox.foreground", Color.WHITE);
            UIManager.put("ComboBox.selectionBackground", colorSeleccion);
            UIManager.put("ComboBox.selectionForeground", colorAcento);
            UIManager.put("ComboBox.buttonBackground", colorFondo);
            UIManager.put("ComboBox.buttonShadow", colorFondo);
            UIManager.put("ComboBox.buttonDarkShadow", colorFondo);
            UIManager.put("ComboBox.buttonHighlight", colorFondo);
            UIManager.put("PopupMenu.background", colorFondo);
            UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(colorAcento));
            UIManager.put("List.background", colorFondo);
            UIManager.put("List.selectionBackground", colorSeleccion);
            UIManager.put("List.selectionForeground", colorAcento);
            UIManager.put("ScrollBar.background", colorFondo);
            UIManager.put("ScrollBar.track", colorFondo);

            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    JLabel lbl = (JLabel) super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus);

                    list.setOpaque(true);
                    list.setBackground(colorFondo);
                    list.setSelectionBackground(colorSeleccion);
                    list.setSelectionForeground(colorAcento);

                    lbl.setOpaque(true);
                    lbl.setBackground(isSelected ? colorSeleccion : colorFondo);
                    lbl.setForeground(isSelected ? colorAcento : Color.WHITE);
                    lbl.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                    return lbl;
                }
            });

            // Repinta toda la ventana justo después de que el popup se
            // cierra/abre. Esto elimina cualquier residuo ("cuadro blanco")
            // que algunos look and feel dejan al componer capas ligeras
            // (lightweight popups) sobre un fondo pintado a mano.
            addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    SwingUtilities.invokeLater(() -> repintarVentana());
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    SwingUtilities.invokeLater(() -> repintarVentana());
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    SwingUtilities.invokeLater(() -> repintarVentana());
                }

                private void repintarVentana() {
                    Window ventana = SwingUtilities.getWindowAncestor(ComboNeo.this);
                    if (ventana != null) {
                        ventana.repaint();
                    }
                }
            });

            setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton("▼");
                    btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    btn.setForeground(colorAcento);
                    btn.setBackground(colorFondo);
                    btn.setBorder(BorderFactory.createEmptyBorder());
                    btn.setContentAreaFilled(false);
                    // Clave: si el botón queda "opaque", Swing pinta un
                    // fondo blanco por defecto antes de dibujar la flecha.
                    btn.setOpaque(false);
                    btn.setFocusPainted(false);
                    return btn;
                }

                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    // Evita el rectángulo blanco del fondo nativo al abrir/cerrar el combo
                }

                @Override
                protected javax.swing.plaf.basic.ComboPopup createPopup() {
                    javax.swing.plaf.basic.BasicComboPopup popup =
                            new javax.swing.plaf.basic.BasicComboPopup(comboBox) {
                        @Override
                        protected JScrollPane createScroller() {
                            JScrollPane scroller = new JScrollPane(list,
                                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) {
                                @Override
                                protected void paintComponent(Graphics g) {
                                    g.setColor(colorFondo);
                                    g.fillRect(0, 0, getWidth(), getHeight());
                                    super.paintComponent(g);
                                }
                            };
                            scroller.setOpaque(true);
                            scroller.setBackground(colorFondo);
                            scroller.setBorder(BorderFactory.createEmptyBorder());
                            scroller.setViewportBorder(BorderFactory.createEmptyBorder());
                            scroller.getViewport().setOpaque(true);
                            scroller.getViewport().setBackground(colorFondo);
                            scroller.getVerticalScrollBar().setOpaque(true);
                            scroller.getVerticalScrollBar().setBackground(colorFondo);
                            return scroller;
                        }
                    };
                    popup.setOpaque(true);
                    popup.setBackground(colorFondo);
                    // Único borde visible: el marco amarillo, sin bordes
                    // adicionales del look and feel que dejaban una franja
                    // blanca alrededor de la lista.
                    popup.setBorder(BorderFactory.createLineBorder(colorAcento));
                    popup.getList().setOpaque(true);
                    popup.getList().setBackground(colorFondo);
                    popup.getList().setSelectionBackground(colorSeleccion);
                    popup.getList().setSelectionForeground(colorAcento);
                    return popup;
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(25, 38, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            super.paintComponent(g2);

            // Borde amarillo, igual que en los campos de texto, para que
            // el combo se vea consistente cuando está cerrado.
            g2.setColor(colorAcento);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            g2.dispose();
        }
    }

    // ============================
    // CONSTRUCTOR
    // ============================
    public DatosTarjeta(String correoUsuario, String dpiUsuario, String tipoCuenta) {

        this.correoUsuario = correoUsuario;
        this.dpiUsuario = dpiUsuario;
        this.tipoCuenta = tipoCuenta;

        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

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

        public FondoPanel() {
            setLayout(null);
            crearFormulario();
        }

        private void crearFormulario() {

            // LOGO
            Image logoEscalado = logo.getScaledInstance(260, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(50, 40, 260, 120);
            add(lblLogo);

            // PANEL
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(25, 38, 35, 180));
            panel.setBounds(650, 120, 550, 650);
            panel.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138), 2, true));
            add(panel);

            JLabel titulo = new JLabel("Datos de Tarjeta");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titulo.setForeground(Color.WHITE);
            titulo.setBounds(40, 20, 400, 40);
            panel.add(titulo);

            // CAMPOS EDITABLES
            panel.add(crearLabel("Tipo de tarjeta", 40, 90));
            ComboNeo cbTipo = new ComboNeo(new String[]{"Débito", "Crédito"});
            cbTipo.setBounds(40, 125, 450, 50);
            panel.add(cbTipo);

            panel.add(crearLabel("País de su cuenta o tarjeta", 40, 190));
            ComboNeo cbPais = new ComboNeo(new String[]{"Guatemala"});
            cbPais.setBounds(40, 225, 450, 50);
            panel.add(cbPais);

            panel.add(crearLabel("Número de tarjeta * (16 dígitos)", 40, 290));
            RoundedCardField txtNumero = crearField(40, 325, panel);
            panel.add(txtNumero);

            panel.add(crearLabel("Seleccione el banco", 40, 390));
            ComboNeo cbBanco = new ComboNeo(new String[]{"Bi", "bac", "banrural", "gyt"});
            cbBanco.setBounds(40, 425, 450, 50);
            panel.add(cbBanco);

            // BOTÓN GUARDAR
            Color amarillo = new Color(251, 232, 138);
            Color amarilloHover = new Color(255, 245, 180);

            BotonNeo btnGuardar = new BotonNeo("Guardar", amarillo, amarilloHover);
            btnGuardar.setBounds(75, 500, 400, 55);
            panel.add(btnGuardar);

            btnGuardar.addActionListener(e -> {
                String tipo = cbTipo.getSelectedItem().toString();
                String pais = cbPais.getSelectedItem().toString();
                String numero = new String(txtNumero.getPassword()).trim();
                String banco = cbBanco.getSelectedItem().toString();

                // Validación estricta: exactamente 16 dígitos, solo números.
                // (el DocumentFilter del campo ya bloquea letras/símbolos y
                // corta la entrada en 16 caracteres, esta es la doble
                // verificación final antes de guardar)
                if (numero.length() != 16 || !numero.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(null,
                            "El número de tarjeta debe contener exactamente 16 dígitos numéricos.",
                            "Validación",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    AgregarTarjeta servicio = new AgregarTarjeta();

                    // Se pasa tipoCuenta en lugar de la variable inexistente 'cuenta'
                    boolean guardado = servicio.registrarTarjeta(
                            correoUsuario,
                            dpiUsuario,
                            tipo,
                            pais,
                            numero,
                            banco,
                            tipoCuenta);

                    if (guardado) {
                        JOptionPane.showMessageDialog(null,
                                "✅ Tarjeta y cuenta bancaria asociadas correctamente.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);

                        new InicioNeo().setVisible(true);
                        dispose();
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,
                            "❌ " + ex.getMessage(),
                            "Validación",
                            JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error al guardar los datos de la tarjeta: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            // BOTÓN VOLVER AL INICIO
            Color verde = new Color(94, 116, 73, 200);
            Color verdeHover = new Color(120, 150, 90);

            BotonNeo btnVolver = new BotonNeo("Volver al inicio", verde, verdeHover);
            btnVolver.setForeground(Color.WHITE);
            btnVolver.setBounds(75, 570, 400, 55);
            panel.add(btnVolver);

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

        // Crea el campo de tarjeta (enmascarado con "*") + el botón de ojo
        // superpuesto en su borde derecho para mostrar/ocultar el número.
        private RoundedCardField crearField(int x, int y, JPanel contenedor) {
            RoundedCardField txt = new RoundedCardField(20);
            txt.setBounds(x, y, 450, 50);
            configurarCampoNumerico(txt);

            BotonOjo btnOjo = new BotonOjo();
            btnOjo.setBounds(x + 450 - 44, y + 3, 40, 44);
            btnOjo.addItemListener(e -> {
                txt.setEchoChar(btnOjo.isSelected() ? (char) 0 : '*');
            });

            contenedor.add(btnOjo);
            // Aseguramos que el botón quede siempre encima del campo de
            // texto (índice 0 = frente) para que sea clickeable.
            contenedor.setComponentZOrder(btnOjo, 0);

            return txt;
        }

        // Filtro estricto: solo dígitos 0-9, máximo 16 caracteres.
        // Cualquier letra, espacio, guion o símbolo se descarta al pegar
        // o escribir, y una vez llega a 16 dígitos no permite más.
        private void configurarCampoNumerico(JTextField txt) {
            AbstractDocument doc = (AbstractDocument) txt.getDocument();
            doc.setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                        throws BadLocationException {
                    replace(fb, offset, 0, string, attr);
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    String limpio = text == null ? "" : text.replaceAll("\\D", "");
                    if (limpio.isEmpty()) {
                        return;
                    }
                    int longitudActual = fb.getDocument().getLength() - length;
                    int espacioDisponible = 16 - longitudActual;
                    if (espacioDisponible <= 0) {
                        return;
                    }
                    if (limpio.length() > espacioDisponible) {
                        limpio = limpio.substring(0, espacioDisponible);
                    }
                    fb.replace(offset, length, limpio, attrs);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}