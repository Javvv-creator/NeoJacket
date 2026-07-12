package funcionalidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;

import main.CRUD.CRUD;
import main.Conexion.conexion;

public class CrearUsuario {

    private final CRUD crud;

    public CrearUsuario() {
        this.crud = new CRUD();
    }

    /**
     * Versión que se adapta a tus campos actuales de RegistroNeo. - txtUsuario
     * -> nombre (usaremos todo como nombre) - txtIdent -> dpiNumero Lo demás lo
     * dejamos como "faltante" y bloqueamos el registro si falta información.
     */
    public boolean crearDesdeRegistroNeo(
            String nombre,
            String apellido,
            String password,
            String dpiNumero,
            String correo,
            String telefono,
            String fechaNacimiento,
            String perfil,
            String tipoCuenta,
            String genero
    ) {
        boolean esMenor = "Menor supervisado".equals(perfil);

        try {
            // Para el menor no se valida ni se requiere DPI
            if (esMenor) {
                validarCamposObligatorios(nombre, apellido, password, correo, telefono, fechaNacimiento, genero);
            } else {
                validarCamposObligatorios(nombre, apellido, password, dpiNumero, correo, telefono, fechaNacimiento, genero);
                validarDpi(dpiNumero);
                // Verificar DPI duplicado solo para adultos
                String dpiEncontrado = crud.buscarUsuario2(dpiNumero.trim());
                if (dpiEncontrado != null) {
                    JOptionPane.showMessageDialog(null,
                            "❌ El DPI ya está registrado: " + dpiEncontrado,
                            "Error - Crear Usuario",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            validarPassword(password);
            validarCorreo(correo);
            validarTelefono(telefono);
            validarFechaNacimiento(fechaNacimiento);
            validarGenero(genero);

            // Para menores el DPI se guarda como null
            String dpiFinal = esMenor ? null : dpiNumero.trim();

            crud.nuevoUsuario(
                    nombre.trim(),
                    apellido.trim(),
                    correo.trim(),
                    telefono.trim(),
                    fechaNacimiento.trim(),
                    genero.trim(),
                    password,
                    dpiFinal,
                    perfil // CORREGIDO: pasa el perfil para que se guarde en BD
            );

            JOptionPane.showMessageDialog(null,
                    "✅ Usuario creado correctamente en Neo Jacket.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                    "❌ " + ex.getMessage(),
                    "Error - Crear Usuario",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // ============================
    // VALIDACIONES
    // ============================
    private void validarCamposObligatorios(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                throw new IllegalArgumentException("Todos los campos deben estar completos. Falta un dato requerido.");
            }
        }
    }

    public boolean crearCuentaBancaria(int idUsuario, int idBanco, String tipoCuenta) {
        try {
            Connection con = conexion.getConexion();

            // Obtener id_tipo_cuenta desde la tabla tipos_cuentas
            PreparedStatement psTipo = con.prepareStatement(
                    "SELECT id_tipo FROM tipos_cuentas WHERE nombre = ?"
            );
            psTipo.setString(1, tipoCuenta);
            ResultSet rsTipo = psTipo.executeQuery();

            int idTipoCuenta = -1;
            if (rsTipo.next()) {
                idTipoCuenta = rsTipo.getInt("id_tipo");
            }
            rsTipo.close();
            psTipo.close();

            if (idTipoCuenta == -1) {
                throw new Exception("Tipo de cuenta no válido: " + tipoCuenta);
            }

            // Insertar nueva cuenta bancaria
            PreparedStatement psCuenta = con.prepareStatement(
                    "INSERT INTO cuentas_bancarias (id_usuario, id_banco, id_tipo_cuenta, numero_cuenta, saldo, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?)"
            );
            psCuenta.setInt(1, idUsuario);
            psCuenta.setInt(2, idBanco);
            psCuenta.setInt(3, idTipoCuenta);
            psCuenta.setString(4, generarNumeroCuenta()); // método auxiliar para generar número único
            psCuenta.setDouble(5, 0.00); // saldo inicial
            psCuenta.setString(6, "activa");

            int filas = psCuenta.executeUpdate();

// 🔹 Verificación en consola
            System.out.println("Cuenta creada -> Usuario: " + idUsuario
                    + ", Banco: " + idBanco
                    + ", Tipo: " + tipoCuenta
                    + ", Filas insertadas: " + filas);

            psCuenta.close();
            con.close();

            return filas > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error al crear cuenta",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerIdUsuario(String correo, String dpiNumero) {
        int idUsuario = -1;
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT id_usuario FROM usuarios WHERE correo = ? OR dpi_numero = ?"
            );
            ps.setString(1, correo);
            ps.setString(2, dpiNumero);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idUsuario;
    }
// Método para recuperar el id_cuenta de la cuenta creada para un usuario y banco

    public int obtenerIdCuenta(int idUsuario, int idBanco) {
        int idCuenta = -1;
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ? ORDER BY id_cuenta DESC LIMIT 1"
            );
 
            ps.setInt(1, idUsuario);
            ps.setInt(2, idBanco);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idCuenta = rs.getInt("id_cuenta");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idCuenta;
    }

// Método auxiliar para generar número de cuenta ficticio
    private String generarNumeroCuenta() {
        return "NC-" + System.currentTimeMillis();
    }

    private void validarFechaNacimiento(String fechaNacimiento) {
        try {
            LocalDate.parse(fechaNacimiento); // YYYY-MM-DD
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha de nacimiento debe estar en formato YYYY-MM-DD.");
        }
    }

    private void validarCorreo(String correo) {
        if (!correo.contains("@") || !correo.contains(".")) {
            throw new IllegalArgumentException("El correo no parece válido.");
        }
    }

    private void validarTelefono(String telefono) {
        String t = telefono.trim();
        if (!t.matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono debe contener solo números.");
        }
        if (t.length() < 7 || t.length() > 15) {
            throw new IllegalArgumentException("El teléfono tiene una longitud inválida.");
        }
    }

    private void validarDpi(String dpiNumero) {
        String d = dpiNumero.trim();
        if (!d.matches("\\d+")) {
            throw new IllegalArgumentException("El DPI debe contener solo números.");
        }
        if (d.length() != 13) {
            throw new IllegalArgumentException("El DPI debe tener exactamente 13 dígitos.");
        }
    }

    private void validarPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    private void validarGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El género es obligatorio.");
        }
        String g = genero.trim();
        if (!g.equals("M") && !g.equals("F") && !g.equals("Otro")) {
            throw new IllegalArgumentException("El género debe ser 'M', 'F' o 'Otro'.");
        }
    }
}
