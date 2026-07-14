package gui;

import funcionalidades.SesionUsuario;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import main.CRUD.CRUD;
import main.Conexion.conexion;

/**
 * Ventana principal para la gestión de depósito/ingreso de fondos en la
 * aplicación. Diseñada bajo un patrón de interfaz premium con renderizado
 * personalizado (Custom Painting).
 *
 * CORREGIDO:
 *  - El botón "Guardar" ahora muestra un mensaje de error explícito cuando
 *    crud.agregarFondos(...) devuelve false. Antes, si fallaba, no pasaba
 *    absolutamente nada (ni error ni confirmación), lo que hacía parecer
 *    que la aplicación "no dejaba ingresar" fondos sin explicación alguna.
 *  - Se limpian los campos del formulario tras un depósito exitoso.
 */
public class AgregarFondos extends JFrame {

    private Image fondo;
    private Image logo;
    Font tituloCampos = new Font("Segoe UI", Font.BOLD, 15);
    Font textoInputs = new Font("Segoe UI", Font.PLAIN, 14);

    public AgregarFondos() {
        // Carga de recursos gráficos del tema visual
        fondo = new ImageIcon(getClass().getResource("/gui/image/fondo.png")).getImage();
        logo = new ImageIcon(getClass().getResource("/gui/image/logoblanco.png")).getImage();

        // Configuración de la ventana maximizada y comportamiento de cierre
        setTitle("Neo Jacket - Agregar Fondos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Se asigna el panel personalizado como contenedor base para permitir el fondo completo
        setContentPane(new FondoPanel());
    }

    /**
     * Panel contenedor principal que maneja la distribución espacial de la UI y
     * el renderizado del fondo de la pantalla.
     */
    class FondoPanel extends JPanel {

        public FondoPanel() {
            // Se utiliza Layout nulo para un control de coordenadas absoluto (Diseño Pixel-Perfect)
            setLayout(null);

            // Construcción secuencial de la UI para respetar las capas visuales (Sidebar va primero)
            crearSidebar(this);
            crearContenido();
        }

        /**
         * Construye la barra de navegación lateral (Sidebar) con esquinas
         * redondeadas y gestiona el enrutamiento de ventanas del sistema.
         */
        private void crearSidebar(JPanel panel) {
            JPanel sidebar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    // Renderizado personalizado: Fondo oscuro translúcido con bordes suavizados
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                    g2.dispose();
                }
            };

            sidebar.setOpaque(false);
            sidebar.setBounds(20, 20, 300, 950);
            sidebar.setLayout(null);

            Color amarillo = new Color(251, 232, 138);
            Color fondoTransparente = new Color(0, 0, 0, 0);
            Color amarilloBorde = new Color(251, 232, 138);

            // Escalado del logotipo corporativo
            Image logoEscalado = logo.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setBounds(20, 10, 250, 110);
            sidebar.add(lblLogo);

            String[] opciones = {
                "Saldos",
                "Bancos Conectados",
                "Transferencias",
                "Historial"
            };

            int y = 140;

            // Bucle dinámico para la construcción y estilización de los botones del menú
            for (String texto : opciones) {
                JButton btn = new JButton(texto) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        // Dibuja el fondo del botón interactivo y su contorno estético
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                        // Si el ratón no está encima, dibuja un contorno sutil
                        if (getBackground() != amarillo) {
                            g2.setColor(amarilloBorde);
                            g2.setStroke(new BasicStroke(1f));
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                        }

                        g2.dispose();
                        super.paintComponent(g);
                    }
                };

                // Normalización de la apariencia del botón para remover estilos nativos del S.O.
                btn.setBounds(20, y, 250, 46);
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setOpaque(false);
                btn.setForeground(Color.WHITE);
                btn.setBackground(fondoTransparente);

                Font fuenteActual = btn.getFont();
                btn.setFont(new Font(fuenteActual.getName(), fuenteActual.getStyle(), fuenteActual.getSize() + 2));

                // 🔄 EVENTOS MOUSE: Feedback visual reactivo (Hover) al usuario
                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        btn.setBackground(amarillo);
                        btn.setForeground(Color.BLACK); // Invierte colores para legibilidad
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        btn.setBackground(fondoTransparente);
                        btn.setForeground(Color.WHITE);
                    }
                });

                // 🔄 LOGICA DE NAVEGACIÓN: Enrutamiento entre módulos liberando recursos de la ventana anterior
                if (texto.equals("Saldos")) {
                    btn.addActionListener(e -> {
                        new Saldos().setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Bancos Conectados")) {
                    btn.addActionListener(e -> {
                        new BancosConectados().setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Transferencias")) {
                    btn.addActionListener(e -> {
                        new Transferencias().setVisible(true);
                        dispose();
                    });
                }
                if (texto.equals("Historial")) {
                    btn.addActionListener(e -> {
                        new Historial().setVisible(true);
                        dispose();
                    });
                }

                // Botón de salida con advertencia cromática (Rojo)
                JButton btnCerrarSesion = new JButton("Cerrar sesión");
                btnCerrarSesion.setBounds(20, 880, 250, 55);
                btnCerrarSesion.setFocusPainted(false);
                btnCerrarSesion.setBorderPainted(false);
                btnCerrarSesion.setBackground(new Color(191, 76, 58));
                btnCerrarSesion.setForeground(Color.WHITE);
                btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnCerrarSesion.addActionListener(e -> {
                    new InicioNeo().setVisible(true);
                    dispose();
                });
                sidebar.add(btnCerrarSesion);

                sidebar.add(btn);
                y += 68; // Incremento de desplazamiento vertical uniforme
            }

            panel.add(sidebar);
        }

        /**
         * Crea el área de contenido principal, las pestañas de navegación
         * interna (Tabs) y el formulario de transacciones financieras.
         */
        private void crearContenido() {
            // Contenedor unificado para preservar la paleta visual oscura y simétrica
            JPanel contenedor = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(25, 38, 35, 220));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            contenedor.setLayout(null);
            contenedor.setOpaque(false);
            contenedor.setBounds(350, 60, 1300, 760);
            add(contenedor);

            // Barra superior integrada para albergar el sistema de pestañas secundarias
            JPanel barraSuperior = new JPanel();
            barraSuperior.setBounds(0, 0, 1300, 55);
            barraSuperior.setBackground(new Color(23, 32, 29));
            barraSuperior.setLayout(null);
            contenedor.add(barraSuperior);

            // Pestaña Activa (Estado actual resaltado en Amarillo)
            JButton btnTab1 = new JButton("Agregar Fondos");
            btnTab1.setBounds(0, 0, 434, 55);
            btnTab1.setBackground(new Color(251, 232, 138));
            btnTab1.setForeground(Color.BLACK);
            btnTab1.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnTab1.setFocusPainted(false);
            btnTab1.setBorder(BorderFactory.createEmptyBorder());
            barraSuperior.add(btnTab1);

            // Pestañas Inactivas (Cambian de flujo al ser clickeadas)
            JButton btnTab2 = crearBotonPestaña("Actualizar Saldos", 433);
            btnTab2.addActionListener(e -> {
                new ActualizarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab2);

            JButton btnTab3 = crearBotonPestaña("Consultar Saldos", 866);
            btnTab3.addActionListener(e -> {
                new ConsultarSaldos().setVisible(true);
                dispose();
            });
            barraSuperior.add(btnTab3);

            // Tarjeta central contenedora del formulario (Eleva los componentes visualmente)
            JPanel cartaCampos = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(20, 28, 25, 120));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(251, 232, 138, 80)); // Marco estilizado fino
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    g2.dispose();
                }
            };
            cartaCampos.setLayout(null);
            cartaCampos.setOpaque(false);
            cartaCampos.setBounds(425, 110, 450, 550);
            contenedor.add(cartaCampos);

            // --- DISEÑO INTERNO DEL FORMULARIO ---
            JLabel lblBanco = new JLabel("Selecciona tu banco");
            lblBanco.setForeground(Color.WHITE);
            lblBanco.setFont(tituloCampos);
            lblBanco.setBounds(35, 25, 380, 25);
            cartaCampos.add(lblBanco);

            // Nombres de banco tal como están registrados en la tabla `bancos`
            String[] opcionesBancos = {"Banco Industrial", "Banrural", "Banco de América Central (BAC)", "Banco G&T Continental"};
            JComboBox<String> cbBancos = new JComboBox<>(opcionesBancos);
            cbBancos.setBounds(35, 55, 380, 45);
            cbBancos.setFont(textoInputs);
            cbBancos.setBackground(new Color(13, 18, 16));
            cbBancos.setForeground(Color.WHITE);
            cbBancos.setBorder(BorderFactory.createLineBorder(new Color(251, 232, 138, 120), 1));

            // 🎨 RENDERER PERSONALIZADO: Modifica el comportamiento de la lista desplegable del JComboBox
            cbBancos.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    if (isSelected) {
                        label.setBackground(new Color(251, 232, 138)); // Fila seleccionada/Hover en Amarillo
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(new Color(13, 18, 16));
                        label.setForeground(Color.WHITE);
                    }
                    return label;
                }
            });
            cartaCampos.add(cbBancos);

            JLabel lblMonto = new JLabel("Monto a ingresar");
            lblMonto.setForeground(Color.WHITE);
            lblMonto.setFont(tituloCampos);
            lblMonto.setBounds(35, 125, 380, 25);
            cartaCampos.add(lblMonto);

            JTextFieldBordeAmarillo txtMonto = new JTextFieldBordeAmarillo();
            txtMonto.setBounds(35, 155, 380, 45);
            txtMonto.setFont(textoInputs);
            cartaCampos.add(txtMonto);

            // Campo de número de tarjeta (necesario para resolver la cuenta destino del depósito)
            JLabel lblTarjeta = new JLabel("Número de tarjeta");
            lblTarjeta.setForeground(Color.WHITE);
            lblTarjeta.setFont(tituloCampos);
            lblTarjeta.setBounds(35, 225, 380, 25);
            cartaCampos.add(lblTarjeta);

            JTextFieldBordeAmarillo txtTarjeta = new JTextFieldBordeAmarillo();
            txtTarjeta.setBounds(35, 255, 380, 45);
            txtTarjeta.setFont(textoInputs);
            cartaCampos.add(txtTarjeta);

            JLabel lblDescripcion = new JLabel("Descripción");
            lblDescripcion.setForeground(Color.WHITE);
            lblDescripcion.setFont(tituloCampos);
            lblDescripcion.setBounds(35, 325, 380, 25);
            cartaCampos.add(lblDescripcion);

            JTextFieldBordeAmarillo txtDescripcion = new JTextFieldBordeAmarillo();
            txtDescripcion.setBounds(35, 355, 380, 45);
            txtDescripcion.setFont(textoInputs);
            cartaCampos.add(txtDescripcion);

            // Botón interactivo de envío (Guardar)
            JButton btnGuardar = new JButton("Guardar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btnGuardar.setBounds(35, 450, 380, 50);
            btnGuardar.setBackground(new Color(251, 232, 138));
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setContentAreaFilled(false);
            btnGuardar.setBorderPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btnGuardar.setBackground(new Color(255, 245, 180)); // Brillo en hover
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btnGuardar.setBackground(new Color(251, 232, 138));
                }
            });
            cartaCampos.add(btnGuardar);

            // ⚙️ LÓGICA DE CONTROLADORES Y PERSISTENCIA DE BASE DE DATOS
            // DIAGNÓSTICO: cada paso tiene su propio try/catch con una etiqueta
            // ("[PASO n]") para que el mensaje de error y la consola te digan
            // EXACTAMENTE en qué punto está fallando, en vez de un solo catch
            // genérico que mezcla todo.
            btnGuardar.addActionListener(e -> {
                Connection con = null;
                PreparedStatement psBanco = null;
                ResultSet rsBanco = null;
                PreparedStatement psTarjeta = null;
                ResultSet rsTarjeta = null;

                // ---------- PASO 1: Validar monto ----------
                double monto;
                String montoTexto = txtMonto.getText().trim();
                if (montoTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debes ingresar un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    monto = Double.parseDouble(montoTexto);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 1 - Parseo de monto] El monto debe ser un número válido.\nValor ingresado: \"" + montoTexto + "\"",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ---------- PASO 2: Variables de contexto ----------
                int idUsuario;
                try {
                    idUsuario = SesionUsuario.getIdUsuario();
                    if (idUsuario <= 0) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 2 - Sesión] No hay un idUsuario válido en la sesión (valor: " + idUsuario + ").\n"
                                + "Es posible que la sesión no se haya iniciado correctamente.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 2 - Sesión] Error al obtener el usuario en sesión: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                String bancoSeleccionado = (String) cbBancos.getSelectedItem();
                String descripcion = txtDescripcion.getText();
                String numeroTarjeta = txtTarjeta.getText().trim();

                // ---------- PASO 3: Validar número de tarjeta ----------
                if (numeroTarjeta.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debes ingresar un número de tarjeta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!numeroTarjeta.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 3 - Validación de tarjeta] El número de tarjeta debe contener 16 dígitos numéricos.\n"
                            + "Valor ingresado: \"" + numeroTarjeta + "\" (longitud: " + numeroTarjeta.length() + ")",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ---------- PASO 4: Mapeo banco → nombre en BD ----------
                String nombreBD = "";
                switch (bancoSeleccionado) {
                    case "Banco Industrial":
                        nombreBD = "Banco Industrial";
                        break;
                    case "Banco de América Central (BAC)":
                        nombreBD = "Banco de América Central (BAC)";
                        break;
                    case "Banrural":
                        nombreBD = "Banrural";
                        break;
                    case "Banco G&T Continental":
                        nombreBD = "Banco G&T Continental";
                        break;
                }
                if (nombreBD.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "[PASO 4 - Mapeo de banco] No se pudo mapear el banco seleccionado (\"" + bancoSeleccionado + "\") a un nombre de BD.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // ---------- PASO 5: Conexión a la base de datos ----------
                    try {
                        con = conexion.getConexion();
                        if (con == null) {
                            JOptionPane.showMessageDialog(this,
                                    "[PASO 5 - Conexión] conexion.getConexion() devolvió null. No hay conexión a la base de datos.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 5 - Conexión] No se pudo conectar a la base de datos: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        return;
                    }

                    // ---------- PASO 6: Obtener idBanco ----------
                    int idBanco;
                    try {
                        psBanco = con.prepareStatement("SELECT id_banco FROM bancos WHERE nombre = ?");
                        psBanco.setString(1, nombreBD);
                        rsBanco = psBanco.executeQuery();

                        if (!rsBanco.next()) {
                            JOptionPane.showMessageDialog(this,
                                    "[PASO 6 - Banco no encontrado] No existe un banco en la tabla `bancos` con nombre = \"" + nombreBD + "\".\n"
                                    + "Verifica que el nombre coincida EXACTAMENTE (mayúsculas, tildes, espacios) con la BD.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        idBanco = rsBanco.getInt("id_banco");
                    } catch (java.sql.SQLException ex) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 6 - SQL al buscar banco] " + ex.getMessage()
                                + "\nSQLState: " + ex.getSQLState() + " | Código: " + ex.getErrorCode(),
                                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        return;
                    }

                    // ---------- PASO 7: Validar que la tarjeta exista y pertenezca al usuario/banco ----------
                    // CORREGIDO: ya NO se depende de tarjetas_bancarias.id_cuenta (que puede
                    // estar NULL). Solo se confirma que la tarjeta exista, pertenezca al
                    // usuario, esté vinculada a este banco y esté activa.
                    try {
                        psTarjeta = con.prepareStatement(
                                "SELECT id_tarjeta FROM tarjetas_bancarias "
                                + "WHERE numero_tarjeta = ? AND id_usuario = ? AND id_banco = ? AND estado = 'activa'"
                        );
                        psTarjeta.setString(1, numeroTarjeta);
                        psTarjeta.setInt(2, idUsuario);
                        psTarjeta.setInt(3, idBanco);

                        rsTarjeta = psTarjeta.executeQuery();

                        if (!rsTarjeta.next()) {
                            JOptionPane.showMessageDialog(this,
                                    "[PASO 7 - Tarjeta no encontrada] No hay ninguna fila en tarjetas_bancarias que cumpla:\n"
                                    + "numero_tarjeta = \"" + numeroTarjeta + "\"\n"
                                    + "id_usuario = " + idUsuario + "\n"
                                    + "id_banco = " + idBanco + "\n"
                                    + "estado = 'activa'\n\n"
                                    + "Revisa si la tarjeta existe, si pertenece a este usuario, si está vinculada a ESTE banco, "
                                    + "y si su estado es exactamente 'activa'.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (java.sql.SQLException ex) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 7 - SQL al validar tarjeta] " + ex.getMessage()
                                + "\nSQLState: " + ex.getSQLState() + " | Código: " + ex.getErrorCode(),
                                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        return;
                    }

                    // ---------- PASO 7b: Obtener idCuenta directamente de cuentas_bancarias ----------
                    // CORREGIDO: la cuenta se busca por id_usuario + id_banco en cuentas_bancarias
                    // (donde realmente vive el saldo), en vez de depender del enlace
                    // tarjetas_bancarias.id_cuenta.
                    int idCuenta;
                    PreparedStatement psCuenta = null;
                    ResultSet rsCuenta = null;
                    try {
                        psCuenta = con.prepareStatement(
                                "SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ?"
                        );
                        psCuenta.setInt(1, idUsuario);
                        psCuenta.setInt(2, idBanco);
                        rsCuenta = psCuenta.executeQuery();

                        if (!rsCuenta.next()) {
                            JOptionPane.showMessageDialog(this,
                                    "[PASO 7b - Cuenta no encontrada] La tarjeta existe, pero no hay ninguna fila en "
                                    + "cuentas_bancarias con id_usuario = " + idUsuario + " e id_banco = " + idBanco + ".\n"
                                    + "Es necesario que el usuario tenga una cuenta creada en ese banco.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        idCuenta = rsCuenta.getInt("id_cuenta");
                    } catch (java.sql.SQLException ex) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 7b - SQL al buscar cuenta] " + ex.getMessage()
                                + "\nSQLState: " + ex.getSQLState() + " | Código: " + ex.getErrorCode(),
                                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        return;
                    } finally {
                        try { if (rsCuenta != null) rsCuenta.close(); } catch (Exception ignored) {}
                        try { if (psCuenta != null) psCuenta.close(); } catch (Exception ignored) {}
                    }

                    // Debug de auditoría interna por consola
                    System.out.println("[DEBUG] Monto: " + monto);
                    System.out.println("[DEBUG] Usuario: " + idUsuario);
                    System.out.println("[DEBUG] Banco: " + idBanco);
                    System.out.println("[DEBUG] idCuenta: " + idCuenta);
                    System.out.println("[DEBUG] Descripción: " + descripcion);

                    // ---------- PASO 8: Insertar fondos (CRUD.agregarFondos) ----------
                    try {
                        CRUD crud = new CRUD();
                        boolean ok = crud.agregarFondos(idUsuario, idBanco, idCuenta, monto, descripcion);
                        if (ok) {
                            JOptionPane.showMessageDialog(this, "Fondo agregado con éxito");
                            txtMonto.setText("");
                            txtTarjeta.setText("");
                            txtDescripcion.setText("");
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "[PASO 8 - agregarFondos devolvió false] El UPDATE no afectó ninguna fila.\n"
                                    + "idCuenta=" + idCuenta + ", idUsuario=" + idUsuario + ", idBanco=" + idBanco + "\n\n"
                                    + "Esto pasa si cuentas_bancarias no tiene una fila cuyo id_cuenta+id_usuario+id_banco "
                                    + "coincidan exactamente con estos valores. Revisa la consola para más detalle.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "[PASO 8 - Excepción en agregarFondos] " + ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }

                } finally {
                    // Cierre seguro de recursos (siempre, incluso en retornos tempranos)
                    try { if (rsTarjeta != null) rsTarjeta.close(); } catch (Exception ignored) {}
                    try { if (psTarjeta != null) psTarjeta.close(); } catch (Exception ignored) {}
                    try { if (rsBanco != null) rsBanco.close(); } catch (Exception ignored) {}
                    try { if (psBanco != null) psBanco.close(); } catch (Exception ignored) {}
                    try { if (con != null) con.close(); } catch (Exception ignored) {}
                }
            });

        }

        /**
         * Helper funcional encargado de estandarizar la creación visual de las
         * pestañas superiores inactivas.
         */
        private JButton crearBotonPestaña(String texto, int xPos) {
            JButton btn = new JButton(texto);
            btn.setBounds(xPos, 0, 434, 55);
            btn.setBackground(new Color(16, 22, 20));
            btn.setForeground(new Color(150, 150, 150));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setForeground(new Color(150, 150, 150));
                }
            });
            return btn;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Renderiza la imagen de fondo estirándola uniformemente al tamaño del contenedor
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Componente Personalizado (Custom Input): Clona el comportamiento de un
     * JTextField pero inyecta un rediseño de bordes redondeados y colores
     * integrados a la paleta Neo Jacket.
     */
    class JTextFieldBordeAmarillo extends JTextField {

        public JTextFieldBordeAmarillo() {
            setOpaque(false); // Desactiva el fondo nativo para evitar interferencias con el renderizado2D
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Padding interno para el texto
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE); // Cursor de escritura blanco
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibuja el cuerpo del input
            g2.setColor(new Color(13, 18, 16));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            // Dibuja la línea de contorno fina en color amarillo translúcido
            g2.setColor(new Color(251, 232, 138, 130));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            g2.dispose();
            super.paintComponent(g); // Ejecuta el renderizado del texto por encima de nuestro diseño gráfico
        }
    }

    public static void main(String[] args) {
        // Hilo de despacho de eventos de Swing (Asegura la estabilidad de ejecución de la UI thread)
        SwingUtilities.invokeLater(() -> {
            new AgregarFondos().setVisible(true);
        });
    }
}