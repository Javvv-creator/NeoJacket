package funcionalidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import main.Conexion.conexion;

public class AgregarTarjeta {

    // ============================
    // Usado en el REGISTRO INICIAL (aún no hay sesión, se busca por correo/DPI)
    // ============================
    public boolean registrarTarjeta(String correoUsuario,
            String dpiUsuario,
            String tipoTarjeta,
            String pais,
            String numeroCuenta,
            String bancoNombre) {

        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario es obligatorio.");
        }
        if (dpiUsuario == null || dpiUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El DPI del usuario es obligatorio.");
        }
        validarCamposTarjeta(tipoTarjeta, pais, numeroCuenta, bancoNombre);

        Connection con = conexion.getConexion();
        if (con == null) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo establecer conexión con la base de datos.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            createTarjetasTableIfNotExists(con);

            Integer idUsuario = obtenerUsuarioId(con, correoUsuario, dpiUsuario);
            if (idUsuario == null) {
                throw new IllegalArgumentException("No se encontró el usuario registrado con el correo o DPI proporcionado.");
            }

            return insertarTarjeta(con, idUsuario, tipoTarjeta, pais, numeroCuenta, bancoNombre);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar la tarjeta: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        } finally {
            cerrarConexion(con);
        }
    }

    // ============================
    // Usado desde el DASHBOARD (usuario ya logueado, se usa el idUsuario de sesión)
    // No necesita correo ni DPI porque ya sabemos quién es.
    // ============================
    public boolean registrarTarjeta(int idUsuario,
            String tipoTarjeta,
            String pais,
            String numeroCuenta,
            String bancoNombre) {

        validarCamposTarjeta(tipoTarjeta, pais, numeroCuenta, bancoNombre);

        Connection con = conexion.getConexion();
        if (con == null) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo establecer conexión con la base de datos.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            createTarjetasTableIfNotExists(con);
            return insertarTarjeta(con, idUsuario, tipoTarjeta, pais, numeroCuenta, bancoNombre);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar la tarjeta: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        } finally {
            cerrarConexion(con);
        }
    }

    // ============================
    // HELPER COMÚN: hace el insert una vez que ya se tiene el idUsuario
    // ============================
    private boolean insertarTarjeta(Connection con, int idUsuario, String tipoTarjeta,
            String pais, String numeroCuenta, String bancoNombre) throws SQLException {

        Integer idBanco = obtenerBancoId(con, bancoNombre);
        if (idBanco == null) {
            throw new IllegalArgumentException("No se encontró el banco especificado. Verifique el nombre del banco.");
        }

        Integer idCuenta = obtenerCuentaIdPorNumeroYBanco(con, numeroCuenta, idBanco);

        String sql = "INSERT INTO tarjetas_bancarias "
                + "(id_usuario, id_cuenta, id_banco, tipo_tarjeta, pais, numero_tarjeta, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'activa')";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            if (idCuenta != null) {
                ps.setInt(2, idCuenta);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setInt(3, idBanco);
            ps.setString(4, tipoTarjeta);
            ps.setString(5, pais);
            ps.setString(6, numeroCuenta);
            ps.executeUpdate();
        }

        return true;
    }

    private void validarCamposTarjeta(String tipoTarjeta, String pais, String numeroCuenta, String bancoNombre) {
        if (tipoTarjeta == null || tipoTarjeta.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de tarjeta es obligatorio.");
        }
        if (pais == null || pais.trim().isEmpty()) {
            throw new IllegalArgumentException("El país de la tarjeta es obligatorio.");
        }
        if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cuenta o tarjeta es obligatorio.");
        }
        if (bancoNombre == null || bancoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del banco es obligatorio.");
        }
    }

    private void cerrarConexion(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Integer obtenerUsuarioId(Connection con, String correoUsuario, String dpiUsuario) throws SQLException {
        String sql = "SELECT id_usuario FROM usuarios WHERE correo = ? OR dpi_numero = ? LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correoUsuario);
            ps.setString(2, dpiUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        }
        return null;
    }

    private Integer obtenerBancoId(Connection con, String bancoNombre) throws SQLException {
        String sql = "SELECT id_banco FROM bancos WHERE nombre = ? OR nombre_corto = ? LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bancoNombre.trim());
            ps.setString(2, bancoNombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_banco");
                }
            }
        }
        return null;
    }

    private Integer obtenerCuentaIdPorNumeroYBanco(Connection con, String numeroCuenta, Integer idBanco) throws SQLException {
        String sql = "SELECT id_cuenta FROM cuentas_bancarias WHERE numero_cuenta = ? AND id_banco = ? LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta.trim());
            ps.setInt(2, idBanco);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_cuenta");
                }
            }
        }
        return null;
    }

    private void createTarjetasTableIfNotExists(Connection con) throws SQLException {
        String ddl = "CREATE TABLE IF NOT EXISTS tarjetas_bancarias ("
                + "id_tarjeta INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,"
                + "id_usuario INT UNSIGNED NOT NULL,"
                + "id_cuenta INT UNSIGNED NULL,"
                + "id_banco SMALLINT UNSIGNED NOT NULL,"
                + "tipo_tarjeta VARCHAR(80) NOT NULL,"
                + "pais VARCHAR(100) NOT NULL,"
                + "numero_tarjeta VARCHAR(50) NOT NULL UNIQUE,"
                + "estado ENUM('activa','bloqueada','cancelada') DEFAULT 'activa',"
                + "creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "CONSTRAINT fk_tarjetas_usuarios FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),"
                + "CONSTRAINT fk_tarjetas_cuentas FOREIGN KEY (id_cuenta) REFERENCES cuentas_bancarias(id_cuenta) ON DELETE SET NULL,"
                + "CONSTRAINT fk_tarjetas_bancos FOREIGN KEY (id_banco) REFERENCES bancos(id_banco)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        try (Statement stmt = con.createStatement()) {
            stmt.execute(ddl);
        }
    }

    public void vincularTarjetaConCuenta(String numeroTarjeta, int idCuenta) {
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE tarjetas_bancarias SET id_cuenta = ? WHERE numero_tarjeta = ?"
            );
            ps.setInt(1, idCuenta);
            ps.setString(2, numeroTarjeta);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}