DROP DATABASE IF EXISTS neojacket_db;
CREATE DATABASE neojacket_db;
USE neojacket_db;

-- =========================================================================
-- 1. MÓDULO DE ACCESOS Y SEGURIDAD (Roles, Usuarios, Autenticación)
-- =========================================================================

CREATE TABLE roles (
    id_rol INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usuarios (
    id_usuario INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_rol INT UNSIGNED NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    fecha_nacimiento DATE NOT NULL,
    genero ENUM('M','F','Otro'),
    password_hash VARCHAR(255) NOT NULL,
    dpi_numero VARCHAR(20) UNIQUE,
    estado ENUM('activo','inactivo','suspendido','bloqueado') NOT NULL DEFAULT 'activo',
    perfil VARCHAR(30) NOT NULL DEFAULT 'Adulto',
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuarios_roles FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

INSERT INTO roles (id_rol, nombre_rol, descripcion)
VALUES (1, 'Administrador', 'Control total del sistema Neo Jacket');

INSERT INTO roles (id_rol, nombre_rol, descripcion)
VALUES (2, 'Cliente', 'Usuario final que utiliza los servicios bancarios');

CREATE TABLE login (
    id_log INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED NULL,
    correo VARCHAR(150) NOT NULL,
    exitoso BOOLEAN NOT NULL,
    ip_origen VARCHAR(45),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_login_usuarios FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE SET NULL
);

CREATE TABLE sesiones (
    id_registro BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED,
    tipo_evento ENUM('inicio_sesion','cierre_sesion','fallo_password','fallo_usuario_inexistente','fallo_cuenta_bloqueada','sesion_expirada') NOT NULL,
    token_sesion VARCHAR(255) UNIQUE,
    dispositivo VARCHAR(255),
    ip_origen VARCHAR(45),
    motivo_fallo VARCHAR(255),
    expira_en DATETIME,
    cerrada_en DATETIME,
    ocurrido_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sesiones_usuarios FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE SET NULL
);

-- =========================================================================
-- 2. MÓDULO DE CONTROL PARENTAL Y LÍMITES
-- =========================================================================

CREATE TABLE supervisiones (
    id_supervision INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_adulto INT UNSIGNED NOT NULL,
    id_menor INT UNSIGNED NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_supervisiones_adulto FOREIGN KEY (id_adulto) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_supervisiones_menor FOREIGN KEY (id_menor) REFERENCES usuarios(id_usuario),
    UNIQUE (id_adulto, id_menor)
);

CREATE TABLE solicitudes_permiso (
    id_solicitud    INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_menor        INT UNSIGNED NOT NULL,
    id_adulto       INT UNSIGNED NOT NULL,
    tipo_accion     VARCHAR(50)   NOT NULL,
    descripcion     VARCHAR(255)  NOT NULL,
    monto           DECIMAL(14,2) DEFAULT 0.00,
    estado          ENUM('pendiente','aprobada','rechazada') NOT NULL DEFAULT 'pendiente',
    fecha_solicitud TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_respuesta DATETIME      DEFAULT NULL,
    CONSTRAINT fk_solicitud_menor  FOREIGN KEY (id_menor)  REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_solicitud_adulto FOREIGN KEY (id_adulto) REFERENCES usuarios(id_usuario)
);

-- =========================================================================
-- 3. MÓDULO FINANCIERO BASE (Monedas, Tasas de Cambio, Bancos)
-- =========================================================================

CREATE TABLE monedas (
    codigo CHAR(3) PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    simbolo VARCHAR(5) NOT NULL,
    activa BOOLEAN DEFAULT TRUE
);

INSERT IGNORE INTO monedas (codigo, nombre, simbolo, activa) VALUES
    ('GTQ', 'Quetzal', 'Q', TRUE),
    ('USD', 'Dólar estadounidense', '$', TRUE),
    ('EUR', 'Euro', '€', TRUE),
    ('MXN', 'Peso mexicano', '$', TRUE),
    ('COP', 'Peso colombiano', '$', TRUE),
    ('ARS', 'Peso argentino', '$', TRUE),
    ('GBP', 'Libra esterlina', '£', TRUE),
    ('BRL', 'Real brasileño', 'R$', TRUE);

CREATE TABLE tipos_cambio (
    id_tipo_cambio INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    moneda_origen CHAR(3) NOT NULL,
    moneda_destino CHAR(3) NOT NULL,
    tasa DECIMAL(14,6) NOT NULL,
    fuente VARCHAR(100),
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tc_moneda_origen FOREIGN KEY (moneda_origen) REFERENCES monedas(codigo),
    CONSTRAINT fk_tc_moneda_destino FOREIGN KEY (moneda_destino) REFERENCES monedas(codigo),
    UNIQUE (moneda_origen, moneda_destino),
    CONSTRAINT chk_tasa CHECK (tasa > 0)
);

CREATE TABLE bancos (
    id_banco SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    nombre_corto VARCHAR(30) NOT NULL UNIQUE,
    pais VARCHAR(100) DEFAULT 'Guatemala',
    activo BOOLEAN DEFAULT TRUE
);

INSERT IGNORE INTO bancos (nombre, nombre_corto, pais, activo) VALUES
    ('Banco Industrial', 'Bi', 'Guatemala', TRUE),
    ('Banco de América Central (BAC)', 'bac', 'Guatemala', TRUE),
    ('Banrural', 'banrural', 'Guatemala', TRUE),
    ('Banco G&T Continental', 'gyt', 'Guatemala', TRUE);

CREATE TABLE tipos_cuentas (
    id_tipo TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    es_credito BOOLEAN DEFAULT FALSE
);

INSERT IGNORE INTO tipos_cuentas (nombre, es_credito) VALUES
    ('Cuenta de Ahorro', FALSE),
    ('Cuenta Monetaria', FALSE),
    ('Cuenta Corriente', FALSE),
    ('Tarjeta de crédito', TRUE);

CREATE TABLE cuentas_bancarias (
    id_cuenta INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED NOT NULL,
    id_banco SMALLINT UNSIGNED NOT NULL,
    id_tipo_cuenta TINYINT UNSIGNED NOT NULL,
    moneda CHAR(3) NOT NULL DEFAULT 'GTQ',
    numero_cuenta VARCHAR(30) NOT NULL,
    saldo DECIMAL(18,2) DEFAULT 0.00,
    estado ENUM('activa', 'bloqueada') NOT NULL DEFAULT 'activa',
    CONSTRAINT fk_cuentas_usuarios FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_cuentas_bancos FOREIGN KEY (id_banco) REFERENCES bancos(id_banco),
    CONSTRAINT fk_cuentas_tipos FOREIGN KEY (id_tipo_cuenta) REFERENCES tipos_cuentas(id_tipo),
    CONSTRAINT fk_cuentas_monedas FOREIGN KEY (moneda) REFERENCES monedas(codigo),
    UNIQUE (id_banco, numero_cuenta)
);
CREATE TABLE tarjetas_bancarias (
    id_tarjeta INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED NOT NULL,
    id_cuenta INT UNSIGNED NULL,
    id_banco SMALLINT UNSIGNED NOT NULL,
    tipo_tarjeta VARCHAR(80) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    numero_tarjeta VARCHAR(50) NOT NULL UNIQUE,
    estado ENUM('activa','bloqueada','cancelada') DEFAULT 'activa',
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tarjetas_usuarios FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_tarjetas_cuentas FOREIGN KEY (id_cuenta) REFERENCES cuentas_bancarias(id_cuenta) ON DELETE SET NULL,
    CONSTRAINT fk_tarjetas_bancos FOREIGN KEY (id_banco) REFERENCES bancos(id_banco)
);

CREATE TABLE limites (
    id_limite INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_supervision INT UNSIGNED NOT NULL,
    id_cuenta INT UNSIGNED NOT NULL,
    tipo_limite ENUM('diario','semanal','mensual','por_transaccion') NOT NULL,
    monto_maximo DECIMAL(14,2) NOT NULL,
    requiere_pin BOOLEAN DEFAULT FALSE,
    pin_hash VARCHAR(255),
    CONSTRAINT fk_limites_supervisiones FOREIGN KEY (id_supervision) REFERENCES supervisiones(id_supervision) ON DELETE CASCADE,
    CONSTRAINT fk_limites_cuentas FOREIGN KEY (id_cuenta) REFERENCES cuentas_bancarias(id_cuenta) ON DELETE CASCADE
);

-- =========================================================================
-- 4. MÓDULO DE TRANSACCIONES Y ANALÍTICA DE GASTOS
-- =========================================================================

CREATE TABLE categorias_transacciones (
    id_categoria INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(150)
);

CREATE TABLE transacciones (
    id_transaccion BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_cuenta_origen INT UNSIGNED NULL,
    id_cuenta_destino INT UNSIGNED NULL,
    id_usuario_realizador INT UNSIGNED NOT NULL,
    id_categoria INT UNSIGNED NULL,
    tipo_transaccion ENUM('deposito','retiro','actualizacion') NOT NULL DEFAULT 'deposito',
    monto DECIMAL(18,2) NOT NULL,
    moneda_origen CHAR(3) NOT NULL,
    moneda_destino CHAR(3) NULL,
    tasa_cambio_historica DECIMAL(14,6) DEFAULT 1.000000,
    estado ENUM('pendiente','completada','rechazada','fondos_insuficientes') DEFAULT 'pendiente',
    descripcion VARCHAR(255),
    saldoRestante DECIMAL(18,2),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_trans_origen FOREIGN KEY (id_cuenta_origen) REFERENCES cuentas_bancarias(id_cuenta),
    CONSTRAINT fk_trans_destino FOREIGN KEY (id_cuenta_destino) REFERENCES cuentas_bancarias(id_cuenta),
    CONSTRAINT fk_trans_usuario FOREIGN KEY (id_usuario_realizador) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_trans_moneda_origen FOREIGN KEY (moneda_origen) REFERENCES monedas(codigo),
    CONSTRAINT fk_trans_moneda_destino FOREIGN KEY (moneda_destino) REFERENCES monedas(codigo),
    CONSTRAINT fk_trans_categoria FOREIGN KEY (id_categoria) REFERENCES categorias_transacciones(id_categoria),
    CONSTRAINT chk_cuentas_distintas CHECK (id_cuenta_origen <> id_cuenta_destino),
    CONSTRAINT chk_monto_positivo CHECK (monto > 0.00)
);

-- =========================================================================
-- 5. MÓDULO DE AUDITORÍA (Acciones de Administradores / Soporte)
-- =========================================================================

CREATE TABLE auditoria_logs (
    id_log BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_admin INT UNSIGNED NOT NULL,
    accion VARCHAR(150) NOT NULL,
    detalle TEXT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditoria_usuarios FOREIGN KEY (id_admin) REFERENCES usuarios(id_usuario)
);

-- Usuario administrador base (id_rol = 1)
-- La contraseña en texto plano es "123456789"; el valor de abajo es su hash
-- PBKDF2WithHmacSHA256 generado con funcionalidades.PasswordUtil (ver ese archivo).
INSERT INTO usuarios (id_rol, nombre, apellido, correo, fecha_nacimiento, password_hash)
VALUES (1, 'Javier', 'Top', 'javier@ex.com', '2000-01-01', '120000:DsZg4ey6iQOeRTh0gjOkgg==:U311JatMaSRzkNYwLGOv6KPn4y2VnxcU/1LBUFrHaYI=');

-- =========================================================================
-- 6. ÍNDICES EXPLÍCITOS PARA LLAVES FORÁNEAS (Optimización de Rendimiento)
-- =========================================================================

CREATE INDEX idx_usuarios_rol ON usuarios(id_rol);
CREATE INDEX idx_login_usuario ON login(id_usuario);
CREATE INDEX idx_sesiones_usuario ON sesiones(id_usuario);
CREATE INDEX idx_supervisiones_menor ON supervisiones(id_menor);
CREATE INDEX idx_cuentas_usuario ON cuentas_bancarias(id_usuario);
CREATE INDEX idx_limites_cuenta ON limites(id_cuenta);
CREATE INDEX idx_trans_origen ON transacciones(id_cuenta_origen);
CREATE INDEX idx_trans_destino ON transacciones(id_cuenta_destino);
CREATE INDEX idx_trans_usuario ON transacciones(id_usuario_realizador);
CREATE INDEX idx_auditoria_admin ON auditoria_logs(id_admin);
CREATE INDEX idx_solicitudes_adulto ON solicitudes_permiso(id_adulto);
CREATE INDEX idx_solicitudes_menor  ON solicitudes_permiso(id_menor);

-- Índice adicional recomendado: acelera la búsqueda de cuentas por usuario + banco,
-- que es el patrón de consulta usado en AgregarFondos / ConsultarSaldos / ActualizarSaldos
-- (SELECT id_cuenta FROM cuentas_bancarias WHERE id_usuario = ? AND id_banco = ?)
CREATE INDEX idx_cuentas_usuario_banco ON cuentas_bancarias(id_usuario, id_banco);

-- Índice adicional recomendado: acelera la búsqueda de tarjetas por usuario + banco + estado
CREATE INDEX idx_tarjetas_usuario_banco ON tarjetas_bancarias(id_usuario, id_banco, estado);

ALTER TABLE transacciones 
MODIFY COLUMN tipo_transaccion 
ENUM('deposito','retiro','actualizacion','transferencia') NOT NULL DEFAULT 'deposito';

ALTER TABLE cuentas_bancarias
ADD COLUMN fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

select * from usuarios;
INSERT INTO tipos_cuentas (nombre) VALUES ('Ahorro'), ('Monetaria');