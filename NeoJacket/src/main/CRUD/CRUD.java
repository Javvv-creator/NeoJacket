package main.CRUD;

import java.sql.*;
import javax.swing.JOptionPane;
// Importamos la clase conexion desde su paquete
import main.Conexion.conexion;

public class CRUD {

    // Metodo Crear - Javier
    public boolean nuevoUsuario(String nombre, String apellido, String correo, String telefono,
            String fechaNacimiento, String genero, String password, String dpiNumero) {
        return nuevoUsuario(nombre, apellido, correo, telefono, fechaNacimiento, genero, password, dpiNumero, "Adulto");
    }

    public boolean nuevoUsuario(String nombre, String apellido, String correo, String telefono,
            String fechaNacimiento, String genero, String password, String dpiNumero, String perfil) {

        String sql = "INSERT INTO usuarios (id_rol, nombre, apellido, correo, telefono, fecha_nacimiento, genero, password_hash, dpi_numero, estado, perfil) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = conexion.getConexion();
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, 2); // Cliente fijo
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, correo);
            ps.setString(5, telefono);
            ps.setDate(6, java.sql.Date.valueOf(fechaNacimiento));
            ps.setString(7, genero);
            ps.setString(8, password);
            if (dpiNumero == null || dpiNumero.trim().isEmpty()) {
                ps.setNull(9, java.sql.Types.VARCHAR);
            } else {
                ps.setString(9, dpiNumero);
            }
            ps.setString(10, "activo");
            ps.setString(11, perfil != null ? perfil : "Adulto"); // CORREGIDO: guarda el perfil real

            ps.executeUpdate();
            System.out.println("El usuario fue creado con éxito en Neo Jacket.");

            ps.close();
            con.close();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al crear el usuario: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    // Metodo Editar - Javier
    public void editarUsuario(int idUsuario, String nombre, String apellido, String correo,
            String telefono, String genero, String estado) {

        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, telefono = ?, genero = ?, estado = ? WHERE id_usuario = ?";

        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, correo);
            ps.setString(4, telefono);
            ps.setString(5, genero);
            ps.setString(6, estado);
            ps.setInt(7, idUsuario);

            ps.executeUpdate();
            System.out.println("El usuario con ID " + idUsuario + " fue actualizado con éxito en Neo Jacket.");

            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error de SQL al editar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo Eliminar - Gallardo
    public void eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            System.out.println("El usuario con ID " + idUsuario + " fue eliminado con éxito de Neo Jacket.");

            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error de SQL al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo Desactivar - Gallardo
    public void desactivarUsuario(int idUsuario) {
        String sql = "UPDATE usuarios SET estado = 'inactivo' WHERE id_usuario = ?";

        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            System.out.println("La cuenta del usuario con ID " + idUsuario + " ahora está inactiva en Neo Jacket.");

            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error de SQL al desactivar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Integer buscarUsuario(int idUsuario) {
        Integer idEncontrado = null;
        String sql = "SELECT id_usuario FROM usuarios WHERE id_usuario = ?";

        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Asignamos el valor real de la base de datos a la variable que vamos a retornar
                idEncontrado = rs.getInt("id_usuario");
                System.out.println("El usuario con ID " + idEncontrado + " fue encontrado con éxito en Neo Jacket.");
            } else {
                System.out.println("No se encontró ningún usuario con ID " + idUsuario);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error de SQL al buscar el usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return idEncontrado; // Ahora sí devuelve el ID si existe, o null si no
    }

    public String buscarUsuario2(String dpiNumero) {
        String dpiEncontrado = null;
        String sql = "SELECT dpi_numero FROM usuarios WHERE dpi_numero = ?";

        try {
            Connection con = conexion.getConexion();
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, dpiNumero);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dpiEncontrado = rs.getString("dpi_numero");
                System.out.println("El usuario con el DPI: " + dpiEncontrado + " fue encontrado con éxito en Neo Jacket.");
            } else {
                System.out.println("No se encontró ningún usuario con DPI: " + dpiNumero);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error de SQL al buscar el usuario por DPI: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return dpiEncontrado; // Devuelve el DPI encontrado o null
    }

    // =============================
// MÉTODO: Agregar Fondos
// =============================
    public boolean agregarFondos(int idUsuario, int idBanco, int idCuenta, double monto, String descripcion) {
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE cuentas_bancarias SET saldo = saldo + ? WHERE id_cuenta = ? AND id_usuario = ? AND id_banco = ?"
            );
            ps.setDouble(1, monto);
            ps.setInt(2, idCuenta);
            ps.setInt(3, idUsuario);
            ps.setInt(4, idBanco);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                // Registrar transacción
                PreparedStatement psTrans = con.prepareStatement(
                        "INSERT INTO transacciones (id_cuenta_origen, id_usuario_realizador, tipo_transaccion, monto, moneda_origen, estado) "
                        + "VALUES (?, ?, 'deposito', ?, 'GTQ', 'completada')"
                );
                psTrans.setInt(1, idCuenta);
                psTrans.setInt(2, idUsuario);
                psTrans.setDouble(3, monto);
                psTrans.executeUpdate();
                psTrans.close();
            }

            ps.close();
            con.close();
            return filas > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // =============================
// MÉTODO: Actualizar Saldos
// =============================
    public boolean actualizarSaldo(int idUsuario, int idBanco, int idCuenta, double montoActual, double nuevoMonto, String motivo) {
        try {
            Connection con = conexion.getConexion();

            // 1. Validar que el monto actual coincida con el saldo en BD
            PreparedStatement psValidar = con.prepareStatement(
                    "SELECT saldo FROM cuentas_bancarias WHERE id_cuenta = ? AND id_usuario = ? AND id_banco = ?"
            );
            psValidar.setInt(1, idCuenta);
            psValidar.setInt(2, idUsuario);
            psValidar.setInt(3, idBanco);

            ResultSet rs = psValidar.executeQuery();
            if (!rs.next()) {
                rs.close();
                psValidar.close();
                con.close();
                return false; // No existe la cuenta
            }

            double saldoBD = rs.getDouble("saldo");
            rs.close();
            psValidar.close();

            if (saldoBD != montoActual) {
                JOptionPane.showMessageDialog(null,
                        "El monto actual ingresado no coincide con el saldo registrado en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                con.close();
                return false;
            }

            // 2. Actualizar el saldo con el nuevo monto
            PreparedStatement psUpdate = con.prepareStatement(
                    "UPDATE cuentas_bancarias SET saldo = ? WHERE id_cuenta = ? AND id_usuario = ? AND id_banco = ?"
            );
            psUpdate.setDouble(1, nuevoMonto);
            psUpdate.setInt(2, idCuenta);
            psUpdate.setInt(3, idUsuario);
            psUpdate.setInt(4, idBanco);

            int filas = psUpdate.executeUpdate();
            psUpdate.close();

            // 3. Registrar transacción de actualización
            if (filas > 0) {
                PreparedStatement psTrans = con.prepareStatement(
                        "INSERT INTO transacciones (id_cuenta_origen, id_usuario_realizador, tipo_transaccion, monto, descripcion, moneda_origen, estado) "
                        + "VALUES (?, ?, 'actualizacion', ?, ?, 'GTQ', 'completada')"
                );
                psTrans.setInt(1, idCuenta);
                psTrans.setInt(2, idUsuario);
                psTrans.setDouble(3, nuevoMonto);
                psTrans.setString(4, motivo != null ? motivo : "");
                psTrans.executeUpdate();
                psTrans.close();
            }

            con.close();
            return filas > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // =============================
// MÉTODO: Consultar Saldo
// =============================
    public double consultarSaldo(int idUsuario, int idBanco, int idCuenta) {
        double saldo = -1; // valor por defecto si no encuentra nada
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT saldo FROM cuentas_bancarias WHERE id_cuenta = ? AND id_usuario = ? AND id_banco = ?"
            );
            ps.setInt(1, idCuenta);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idBanco);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return saldo;
    }

// =============================
// MÉTODO: Consultar Transacciones
// =============================
    public ResultSet consultarTransacciones(int idUsuario, int idBanco, int idCuenta) {
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT creado_en, tipo_transaccion, monto, saldoRestante, estado "
                    + "FROM transacciones "
                    + "WHERE id_usuario_realizador = ? AND id_cuenta_origen = ? "
                    + "ORDER BY creado_en DESC"
            );
            ps.setInt(1, idUsuario);
            ps.setInt(2, idCuenta);

            return ps.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
