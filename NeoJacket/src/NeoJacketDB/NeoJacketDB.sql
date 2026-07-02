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
    ('EUR', 'Euro', '€', TRUE);

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
    ('Bi', 'Bi', 'Guatemala', TRUE),
    ('bac', 'bac', 'Guatemala', TRUE),
    ('banrural', 'banrural', 'Guatemala', TRUE),
    ('gyt', 'gyt', 'Guatemala', TRUE);

CREATE TABLE tipos_cuentas (
    id_tipo TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE, 
    es_credito BOOLEAN DEFAULT FALSE    
);

INSERT IGNORE INTO tipos_cuentas (nombre, es_credito) VALUES
    ('Ahorro', FALSE),
    ('Monetaria', FALSE),
    ('Corriente', FALSE),
    ('Tarjeta de crédito', TRUE);

CREATE TABLE cuentas_bancarias (
    id_cuenta INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED NOT NULL,
    id_banco SMALLINT UNSIGNED NOT NULL,
    id_tipo_cuenta TINYINT UNSIGNED NOT NULL,
    moneda CHAR(3) NOT NULL DEFAULT 'GTQ',
    numero_cuenta VARCHAR(30) NOT NULL,
    saldo DECIMAL(18,2) DEFAULT 0.00,
    CONSTRAINT fk_cuentas_usuarios FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_cuentas_bancos FOREIGN KEY (id_banco) REFERENCES bancos(id_banco),
    CONSTRAINT fk_cuentas_tipos FOREIGN KEY (id_tipo_cuenta) REFERENCES tipos_cuentas(id_tipo),
    CONSTRAINT fk_cuentas_monedas FOREIGN KEY (moneda) REFERENCES monedas(codigo),
    UNIQUE (id_banco, numero_cuenta),
    estado ENUM('activa', 'bloqueada') not null default 'activa'
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
    id_cuenta_origen INT UNSIGNED NOT NULL,
    id_cuenta_destino INT UNSIGNED NULL, 
    id_usuario_realizador INT UNSIGNED NOT NULL,
    id_categoria INT UNSIGNED NULL,      
    tipo_transaccion ENUM('transferencia', 'retiro', 'deposito', 'pago_servicio') NOT NULL DEFAULT 'transferencia',
    monto DECIMAL(18,2) NOT NULL,
    moneda_origen CHAR(3) NOT NULL,
    moneda_destino CHAR(3) NULL,
    tasa_cambio_historica DECIMAL(14,6) DEFAULT 1.000000, 
    estado ENUM('pendiente','completada','rechazada','fondos_insuficientes') DEFAULT 'pendiente',
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

select * from Usuarios;