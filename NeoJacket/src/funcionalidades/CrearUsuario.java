package funcionalidades;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;

import main.CRUD.CRUD;

public class CrearUsuario {

    private final CRUD crud;

    public CrearUsuario() {
        this.crud = new CRUD();
    }

    /**
     * Versión que se adapta a tus campos actuales de RegistroNeo.
     * - txtUsuario -> nombre (usaremos todo como nombre)
     * - txtIdent -> dpiNumero
     * Lo demás lo dejamos como "faltante" y bloqueamos el registro si falta información.
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
        try {
            validarCamposObligatorios(nombre, apellido, password, dpiNumero, correo, telefono, fechaNacimiento, genero);
            validarDpi(dpiNumero);
            validarPassword(password);
            validarCorreo(correo);
            validarTelefono(telefono);
            validarFechaNacimiento(fechaNacimiento);
            validarGenero(genero);

            String dpiEncontrado = crud.buscarUsuario2(dpiNumero.trim());
            if (dpiEncontrado != null) {
                JOptionPane.showMessageDialog(null,
                        "❌ El DPI ya está registrado: " + dpiEncontrado,
                        "Error - Crear Usuario",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            crud.nuevoUsuario(
                    nombre.trim(),
                    apellido.trim(),
                    correo.trim(),
                    telefono.trim(),
                    fechaNacimiento.trim(),
                    genero.trim(),
                    password,
                    dpiNumero.trim()
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