-- =======================================================
-- VETERINARIA SAAS - MULTI-TENANT ARCHITECTURE
-- Sistema SaaS para Gestión Veterinaria
-- =======================================================
-- Versión: 2.0 - Multi-Tenant
-- Fecha: 27 Diciembre 2025
-- =======================================================

DROP DATABASE IF EXISTS veterinaria_saas;

CREATE DATABASE veterinaria_saas
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE veterinaria_saas;

-- ===========================================
-- NIVEL 1: TABLAS GLOBALES DEL SISTEMA SAAS
-- ===========================================

-- Tabla de planes disponibles para el SaaS
CREATE TABLE plan (
  id_plan                     INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL,
  descripcion                 TEXT,
  precio_mensual              DECIMAL(10,2) NOT NULL,
  precio_anual                DECIMAL(10,2),
  max_usuarios                INT NOT NULL DEFAULT 5,
  max_doctores                INT NOT NULL DEFAULT 5,
  max_mascotas                INT NOT NULL DEFAULT 100,
  max_almacenamiento_mb       INT NOT NULL DEFAULT 1024,
  tiene_reportes_avanzados    BOOLEAN NOT NULL DEFAULT FALSE,
  tiene_api_acceso            BOOLEAN NOT NULL DEFAULT FALSE,
  tiene_soporte_prioritario   BOOLEAN NOT NULL DEFAULT FALSE,
  orden_visualizacion         INT NOT NULL DEFAULT 0,
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Tabla de Super Administradores (Admins del SaaS)
CREATE TABLE super_admin (
  id_super_admin              INT AUTO_INCREMENT PRIMARY KEY,
  username                    VARCHAR(50) NOT NULL UNIQUE,
  password_hash               VARCHAR(255) NOT NULL,
  email                       VARCHAR(100) NOT NULL UNIQUE,
  nombres                     VARCHAR(100) NOT NULL,
  apellidos                   VARCHAR(100) NOT NULL,
  telefono                    VARCHAR(30),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ultimo_acceso               DATETIME,
  INDEX idx_username (username),
  INDEX idx_email (email)
) ENGINE=InnoDB;

-- Tabla de Tenants (Cada veterinaria es un tenant)
CREATE TABLE tenant (
  id_tenant                   INT AUTO_INCREMENT PRIMARY KEY,
  codigo_tenant               VARCHAR(50) NOT NULL UNIQUE COMMENT 'Código único para subdomain o identificación',
  nombre_comercial            VARCHAR(150) NOT NULL,
  razon_social                VARCHAR(200),
  ruc                         VARCHAR(20),
  telefono                    VARCHAR(30),
  email_contacto              VARCHAR(100) NOT NULL,
  direccion                   VARCHAR(255),
  pais                        VARCHAR(50) DEFAULT 'Perú',
  ciudad                      VARCHAR(100),
  
  -- Configuración del tenant
  id_plan_actual              INT NOT NULL,
  estado_suscripcion          ENUM('TRIAL','ACTIVO','SUSPENDIDO','CANCELADO') NOT NULL DEFAULT 'TRIAL',
  fecha_registro              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_activacion            DATETIME,
  fecha_suspension            DATETIME,
  dias_trial                  INT NOT NULL DEFAULT 15,
  
  -- Propietario/Administrador principal
  nombre_propietario          VARCHAR(150) NOT NULL,
  email_propietario           VARCHAR(100) NOT NULL,
  telefono_propietario        VARCHAR(30),
  
  -- Uso y límites
  usuarios_activos            INT NOT NULL DEFAULT 0,
  doctores_activos            INT NOT NULL DEFAULT 0,
  mascotas_registradas        INT NOT NULL DEFAULT 0,
  almacenamiento_usado_mb     INT NOT NULL DEFAULT 0,
  
  -- Metadata
  logo_url                    VARCHAR(255),
  color_primario              VARCHAR(7) DEFAULT '#3B82F6',
  color_secundario            VARCHAR(7) DEFAULT '#10B981',
  
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_tenant_plan
    FOREIGN KEY (id_plan_actual) REFERENCES plan(id_plan),
  INDEX idx_codigo_tenant (codigo_tenant),
  INDEX idx_email_propietario (email_propietario),
  INDEX idx_estado_suscripcion (estado_suscripcion)
) ENGINE=InnoDB;

-- Historial de suscripciones del tenant
CREATE TABLE suscripcion (
  id_suscripcion              INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_plan                     INT NOT NULL,
  fecha_inicio                DATE NOT NULL,
  fecha_fin                   DATE NOT NULL,
  metodo_pago                 VARCHAR(50),
  monto_pagado                DECIMAL(10,2),
  estado                      ENUM('PENDIENTE','ACTIVO','VENCIDO','CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
  notas                       TEXT,
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_suscripcion_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_suscripcion_plan
    FOREIGN KEY (id_plan) REFERENCES plan(id_plan),
  INDEX idx_tenant_fecha (id_tenant, fecha_inicio, fecha_fin),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- Historial de pagos
CREATE TABLE pago (
  id_pago                     INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_suscripcion              INT,
  monto                       DECIMAL(10,2) NOT NULL,
  metodo_pago                 VARCHAR(50) NOT NULL,
  estado                      ENUM('PENDIENTE','COMPLETADO','FALLIDO','REEMBOLSADO') NOT NULL DEFAULT 'PENDIENTE',
  referencia_pago             VARCHAR(100),
  fecha_pago                  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_procesamiento         DATETIME,
  notas                       TEXT,
  
  CONSTRAINT fk_pago_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_pago_suscripcion
    FOREIGN KEY (id_suscripcion) REFERENCES suscripcion(id_suscripcion),
  INDEX idx_tenant_fecha (id_tenant, fecha_pago),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- ===========================================
-- NIVEL 2: TABLAS BASE COMPARTIDAS (CATÁLOGOS)
-- ===========================================

CREATE TABLE rol (
  id_rol                      INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(50) NOT NULL UNIQUE,
  descripcion                 VARCHAR(255),
  nivel_acceso                INT NOT NULL DEFAULT 1 COMMENT '1=Admin, 2=Veterinario, 3=Recepcionista'
) ENGINE=InnoDB;

CREATE TABLE especie (
  id_especie                  INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL,
  nombre_cientifico           VARCHAR(150),
  descripcion                 VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE raza (
  id_raza                     INT AUTO_INCREMENT PRIMARY KEY,
  id_especie                  INT NOT NULL,
  nombre                      VARCHAR(100) NOT NULL,
  descripcion                 VARCHAR(255),
  
  CONSTRAINT fk_raza_especie
    FOREIGN KEY (id_especie) REFERENCES especie(id_especie),
  INDEX idx_especie (id_especie)
) ENGINE=InnoDB;

CREATE TABLE categoria_producto (
  id_categoria                INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL,
  descripcion                 VARCHAR(255),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- ===========================================
-- NIVEL 3: TABLAS POR TENANT (MULTI-TENANT)
-- ===========================================

-- Usuarios del tenant (NO incluye super admins)
CREATE TABLE usuario (
  id_usuario                  INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_rol                      INT NOT NULL,
  username                    VARCHAR(50) NOT NULL,
  password_hash               VARCHAR(255) NOT NULL,
  email                       VARCHAR(100) NOT NULL,
  nombres                     VARCHAR(100) NOT NULL,
  apellidos                   VARCHAR(100) NOT NULL,
  telefono                    VARCHAR(30),
  avatar_url                  VARCHAR(255),
  estado                      ENUM('ACTIVO','INACTIVO','SUSPENDIDO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ultimo_acceso               DATETIME,
  
  CONSTRAINT fk_usuario_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_usuario_rol
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
  UNIQUE KEY unique_username_tenant (id_tenant, username),
  UNIQUE KEY unique_email_tenant (id_tenant, email),
  INDEX idx_tenant_estado (id_tenant, estado)
) ENGINE=InnoDB;

CREATE TABLE refresh_token (
  id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_usuario                  INT,
  id_super_admin              INT,
  token                       VARCHAR(255) NOT NULL UNIQUE,
  expiry_date                 DATETIME NOT NULL,
  tipo_usuario                ENUM('USUARIO','SUPER_ADMIN') NOT NULL,
  
  CONSTRAINT fk_refresh_token_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
  CONSTRAINT fk_refresh_token_super_admin
    FOREIGN KEY (id_super_admin) REFERENCES super_admin(id_super_admin) ON DELETE CASCADE,
  INDEX idx_token (token),
  INDEX idx_expiry (expiry_date)
) ENGINE=InnoDB;

-- Doctores del tenant
CREATE TABLE doctor (
  id_doctor                   INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_usuario                  INT,
  nombres                     VARCHAR(100) NOT NULL,
  apellidos                   VARCHAR(100) NOT NULL,
  colegiatura                 VARCHAR(50),
  especialidad                VARCHAR(100),
  telefono                    VARCHAR(30),
  email                       VARCHAR(100),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  
  CONSTRAINT fk_doctor_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_doctor_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL,
  INDEX idx_tenant_estado (id_tenant, estado)
) ENGINE=InnoDB;

-- Clientes del tenant
CREATE TABLE cliente (
  id_cliente                  INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  nombres                     VARCHAR(100) NOT NULL,
  apellidos                   VARCHAR(100) NOT NULL,
  tipo_documento              ENUM('DNI','CE','PASAPORTE','OTRO') DEFAULT 'DNI',
  numero_documento            VARCHAR(20),
  telefono                    VARCHAR(30),
  email                       VARCHAR(100),
  direccion                   VARCHAR(255),
  fecha_registro              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  
  CONSTRAINT fk_cliente_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  INDEX idx_tenant_estado (id_tenant, estado),
  INDEX idx_documento (numero_documento)
) ENGINE=InnoDB;

-- Mascotas del tenant
CREATE TABLE mascota (
  id_mascota                  INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_cliente                  INT NOT NULL,
  id_raza                     INT NOT NULL,
  nombre                      VARCHAR(100) NOT NULL,
  sexo                        ENUM('MACHO','HEMBRA','DESCONOCIDO') DEFAULT 'MACHO',
  fecha_nacimiento            DATE,
  color                       VARCHAR(50),
  peso_kg                     DECIMAL(5,2),
  microchip                   VARCHAR(50),
  foto_url                    VARCHAR(255),
  observaciones               TEXT,
  estado                      ENUM('ACTIVO','FALLECIDO','ADOPTADO','PERDIDO') NOT NULL DEFAULT 'ACTIVO',
  fecha_registro              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_mascota_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_mascota_cliente
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE,
  CONSTRAINT fk_mascota_raza
    FOREIGN KEY (id_raza) REFERENCES raza(id_raza),
  INDEX idx_tenant_cliente (id_tenant, id_cliente),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- Citas del tenant
CREATE TABLE cita (
  id_cita                     INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_mascota                  INT NOT NULL,
  id_cliente                  INT NOT NULL,
  id_doctor                   INT NOT NULL,
  fecha_hora                  DATETIME NOT NULL,
  duracion_minutos            INT NOT NULL DEFAULT 30,
  motivo                      VARCHAR(255),
  observaciones               TEXT,
  estado                      ENUM('PENDIENTE','CONFIRMADA','ATENDIDA','CANCELADA','NO_ASISTIO') NOT NULL DEFAULT 'PENDIENTE',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_cita_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_cita_mascota
    FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE,
  CONSTRAINT fk_cita_cliente
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE,
  CONSTRAINT fk_cita_doctor
    FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
  INDEX idx_tenant_fecha (id_tenant, fecha_hora),
  INDEX idx_doctor_fecha (id_doctor, fecha_hora),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- Historia clínica del tenant
CREATE TABLE historia_clinica (
  id_historia                 INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_mascota                  INT NOT NULL,
  id_doctor                   INT NOT NULL,
  id_cita                     INT,
  fecha_atencion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  motivo_consulta             VARCHAR(255),
  anamnesis                   TEXT,
  examen_fisico               TEXT,
  diagnostico                 TEXT,
  tratamiento                 TEXT,
  examenes_solicitados        TEXT,
  observaciones               TEXT,
  proxima_cita                DATE,
  
  CONSTRAINT fk_historia_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_historia_mascota
    FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE,
  CONSTRAINT fk_historia_doctor
    FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
  CONSTRAINT fk_historia_cita
    FOREIGN KEY (id_cita) REFERENCES cita(id_cita) ON DELETE SET NULL,
  INDEX idx_tenant_mascota (id_tenant, id_mascota),
  INDEX idx_fecha (fecha_atencion)
) ENGINE=InnoDB;

-- Productos (por tenant o globales)
CREATE TABLE producto (
  id_producto                 INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT COMMENT 'NULL = producto global del sistema',
  id_categoria                INT NOT NULL,
  codigo                      VARCHAR(50),
  nombre                      VARCHAR(150) NOT NULL,
  descripcion                 TEXT,
  es_medicamento              BOOLEAN NOT NULL DEFAULT FALSE,
  requiere_receta             BOOLEAN NOT NULL DEFAULT FALSE,
  precio_compra               DECIMAL(10,2),
  precio_venta                DECIMAL(10,2) NOT NULL,
  unidad_medida               VARCHAR(20) DEFAULT 'UND',
  estado                      ENUM('ACTIVO','INACTIVO','AGOTADO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_producto_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_producto_categoria
    FOREIGN KEY (id_categoria) REFERENCES categoria_producto(id_categoria),
  INDEX idx_tenant_codigo (id_tenant, codigo),
  INDEX idx_categoria (id_categoria)
) ENGINE=InnoDB;

-- Inventario del tenant
CREATE TABLE inventario (
  id_inventario               INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_producto                 INT NOT NULL,
  stock_actual                INT NOT NULL DEFAULT 0,
  stock_minimo                INT NOT NULL DEFAULT 0,
  stock_maximo                INT NOT NULL DEFAULT 0,
  fecha_ultimo_ingreso        DATETIME,
  fecha_ultima_salida         DATETIME,
  
  CONSTRAINT fk_inventario_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_inventario_producto
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto) ON DELETE CASCADE,
  UNIQUE KEY unique_tenant_producto (id_tenant, id_producto),
  INDEX idx_stock_minimo (stock_actual, stock_minimo)
) ENGINE=InnoDB;

-- Proveedores del tenant
CREATE TABLE proveedor (
  id_proveedor                INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  nombre                      VARCHAR(150) NOT NULL,
  ruc                         VARCHAR(20),
  telefono                    VARCHAR(30),
  email                       VARCHAR(100),
  direccion                   VARCHAR(255),
  contacto_nombre             VARCHAR(100),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  
  CONSTRAINT fk_proveedor_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  INDEX idx_tenant_estado (id_tenant, estado)
) ENGINE=InnoDB;

-- Compras del tenant
CREATE TABLE compra (
  id_compra                   INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_proveedor                INT NOT NULL,
  numero_comprobante          VARCHAR(50),
  fecha                       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  subtotal                    DECIMAL(10,2) NOT NULL DEFAULT 0,
  igv                         DECIMAL(10,2) NOT NULL DEFAULT 0,
  total                       DECIMAL(10,2) NOT NULL,
  metodo_pago                 VARCHAR(50),
  estado                      ENUM('PENDIENTE','PAGADA','ANULADA') NOT NULL DEFAULT 'PENDIENTE',
  observaciones               TEXT,
  
  CONSTRAINT fk_compra_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_compra_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
  INDEX idx_tenant_fecha (id_tenant, fecha),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

CREATE TABLE detalle_compra (
  id_detalle_compra           INT AUTO_INCREMENT PRIMARY KEY,
  id_compra                   INT NOT NULL,
  id_producto                 INT NOT NULL,
  cantidad                    INT NOT NULL,
  precio_unitario             DECIMAL(10,2) NOT NULL,
  subtotal                    DECIMAL(10,2) NOT NULL,
  
  CONSTRAINT fk_detalle_compra_compra
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE CASCADE,
  CONSTRAINT fk_detalle_compra_producto
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
) ENGINE=InnoDB;

-- Ventas del tenant
CREATE TABLE venta (
  id_venta                    INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_cliente                  INT,
  numero_comprobante          VARCHAR(50),
  fecha                       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  subtotal                    DECIMAL(10,2) NOT NULL DEFAULT 0,
  igv                         DECIMAL(10,2) NOT NULL DEFAULT 0,
  descuento                   DECIMAL(10,2) NOT NULL DEFAULT 0,
  total                       DECIMAL(10,2) NOT NULL,
  metodo_pago                 VARCHAR(50),
  estado                      ENUM('PENDIENTE','PAGADA','ANULADA') NOT NULL DEFAULT 'PENDIENTE',
  observaciones               TEXT,
  
  CONSTRAINT fk_venta_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_venta_cliente
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE SET NULL,
  INDEX idx_tenant_fecha (id_tenant, fecha),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

CREATE TABLE detalle_venta (
  id_detalle_venta            INT AUTO_INCREMENT PRIMARY KEY,
  id_venta                    INT NOT NULL,
  id_producto                 INT NOT NULL,
  cantidad                    INT NOT NULL,
  precio_unitario             DECIMAL(10,2) NOT NULL,
  descuento                   DECIMAL(10,2) NOT NULL DEFAULT 0,
  subtotal                    DECIMAL(10,2) NOT NULL,
  
  CONSTRAINT fk_detalle_venta_venta
    FOREIGN KEY (id_venta) REFERENCES venta(id_venta) ON DELETE CASCADE,
  CONSTRAINT fk_detalle_venta_producto
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
) ENGINE=InnoDB;

-- Recetas del tenant
CREATE TABLE receta (
  id_receta                   INT AUTO_INCREMENT PRIMARY KEY,
  id_historia                 INT NOT NULL,
  id_tenant                   INT NOT NULL,
  id_doctor                   INT NOT NULL,
  id_mascota                  INT NOT NULL,
  fecha_emision               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  observaciones               TEXT,
  estado                      ENUM('ACTIVA','DISPENSADA','ANULADA') NOT NULL DEFAULT 'ACTIVA',
  
  CONSTRAINT fk_receta_historia
    FOREIGN KEY (id_historia) REFERENCES historia_clinica(id_historia) ON DELETE CASCADE,
  CONSTRAINT fk_receta_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_receta_doctor
    FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
  CONSTRAINT fk_receta_mascota
    FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE,
  INDEX idx_tenant_fecha (id_tenant, fecha_emision)
) ENGINE=InnoDB;

CREATE TABLE detalle_receta (
  id_detalle_receta           INT AUTO_INCREMENT PRIMARY KEY,
  id_receta                   INT NOT NULL,
  id_producto                 INT NOT NULL,
  dosis                       VARCHAR(100) NOT NULL,
  frecuencia                  VARCHAR(100),
  duracion                    VARCHAR(100),
  indicaciones                TEXT,
  
  CONSTRAINT fk_detalle_receta_receta
    FOREIGN KEY (id_receta) REFERENCES receta(id_receta) ON DELETE CASCADE,
  CONSTRAINT fk_detalle_receta_producto
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
) ENGINE=InnoDB;

-- Archivos adjuntos
CREATE TABLE archivo_adjunto (
  id_archivo                  INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_historia                 INT,
  id_mascota                  INT NOT NULL,
  nombre_archivo              VARCHAR(255) NOT NULL,
  ruta_archivo                VARCHAR(500) NOT NULL,
  tipo_archivo                VARCHAR(50),
  tamano_kb                   INT,
  descripcion                 VARCHAR(255),
  fecha_subida                DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  subido_por                  INT NOT NULL,
  
  CONSTRAINT fk_archivo_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_archivo_historia
    FOREIGN KEY (id_historia) REFERENCES historia_clinica(id_historia) ON DELETE CASCADE,
  CONSTRAINT fk_archivo_mascota
    FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE,
  CONSTRAINT fk_archivo_usuario
    FOREIGN KEY (subido_por) REFERENCES usuario(id_usuario),
  INDEX idx_tenant_mascota (id_tenant, id_mascota)
) ENGINE=InnoDB;

-- Notificaciones
CREATE TABLE notificacion (
  id_notificacion             INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_usuario                  INT NOT NULL,
  titulo                      VARCHAR(150) NOT NULL,
  mensaje                     TEXT NOT NULL,
  tipo                        ENUM('INFO','ALERTA','RECORDATORIO','SISTEMA') DEFAULT 'INFO',
  url_accion                  VARCHAR(255),
  leido                       BOOLEAN NOT NULL DEFAULT FALSE,
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_lectura               DATETIME,
  
  CONSTRAINT fk_notificacion_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_notificacion_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
  INDEX idx_usuario_leido (id_usuario, leido),
  INDEX idx_fecha (fecha_creacion)
) ENGINE=InnoDB;

-- Auditoría
CREATE TABLE auditoria (
  id_auditoria                BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT,
  id_usuario                  INT,
  id_super_admin              INT,
  tabla_afectada              VARCHAR(100) NOT NULL,
  id_registro                 INT NOT NULL,
  accion                      ENUM('INSERT','UPDATE','DELETE','LOGIN','LOGOUT') NOT NULL,
  valores_anteriores          JSON,
  valores_nuevos              JSON,
  ip_address                  VARCHAR(45),
  user_agent                  VARCHAR(255),
  fecha_hora                  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_auditoria_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  CONSTRAINT fk_auditoria_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL,
  CONSTRAINT fk_auditoria_super_admin
    FOREIGN KEY (id_super_admin) REFERENCES super_admin(id_super_admin) ON DELETE SET NULL,
  INDEX idx_tenant_fecha (id_tenant, fecha_hora),
  INDEX idx_tabla_accion (tabla_afectada, accion)
) ENGINE=InnoDB;

-- ===========================================
-- INSERTAR DATOS INICIALES
-- ===========================================

-- Super Admin principal
INSERT INTO super_admin (username, password_hash, email, nombres, apellidos, telefono, estado) VALUES
('superadmin', '$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe', 'admin@appvet.com', 'Admin', 'Sistema', '999999999', 'ACTIVO');

-- Roles del sistema
INSERT INTO rol (nombre, descripcion, nivel_acceso) VALUES
('ROLE_ADMIN', 'Administrador de Veterinaria', 1),
('ROLE_VETERINARIO', 'Veterinario', 2),
('ROLE_RECEPCIONISTA', 'Recepcionista', 3),
('ROLE_ASISTENTE', 'Asistente Veterinario', 3);

-- Planes disponibles
INSERT INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_doctores, max_mascotas, max_almacenamiento_mb, tiene_reportes_avanzados, tiene_api_acceso, tiene_soporte_prioritario, orden_visualizacion, estado) VALUES
('Básico', 'Ideal para veterinarias pequeñas que están comenzando', 49.99, 499.99, 3, 2, 50, 512, FALSE, FALSE, FALSE, 1, 'ACTIVO'),
('Profesional', 'Perfecto para veterinarias en crecimiento', 99.99, 999.99, 10, 5, 200, 2048, TRUE, FALSE, FALSE, 2, 'ACTIVO'),
('Empresarial', 'Para grandes clínicas y hospitales veterinarios', 199.99, 1999.99, 30, 15, 1000, 10240, TRUE, TRUE, TRUE, 3, 'ACTIVO'),
('Enterprise', 'Solución personalizada sin límites', 399.99, 3999.99, 100, 50, 5000, 51200, TRUE, TRUE, TRUE, 4, 'ACTIVO');

-- Especies y razas
INSERT INTO especie (nombre, nombre_cientifico, descripcion) VALUES
('Perro', 'Canis familiaris', 'Mamífero carnívoro doméstico'),
('Gato', 'Felis catus', 'Mamífero carnívoro doméstico'),
('Ave', 'Aves', 'Clase de vertebrados'),
('Conejo', 'Oryctolagus cuniculus', 'Mamífero lagomorfo'),
('Reptil', 'Reptilia', 'Clase de vertebrados'),
('Roedor', 'Rodentia', 'Orden de mamíferos');

INSERT INTO raza (id_especie, nombre, descripcion) VALUES
-- Perros
(1, 'Labrador Retriever', 'Raza grande, amigable y activa'),
(1, 'Pastor Alemán', 'Raza grande, inteligente y protectora'),
(1, 'Golden Retriever', 'Raza grande, cariñosa y familiar'),
(1, 'Bulldog', 'Raza mediana, tranquila y leal'),
(1, 'Chihuahua', 'Raza pequeña, alerta y vivaz'),
(1, 'Mestizo', 'Raza mixta'),
-- Gatos
(2, 'Persa', 'Gato de pelo largo y rostro plano'),
(2, 'Siamés', 'Gato esbelto con marcas distintivas'),
(2, 'Maine Coon', 'Gato de gran tamaño y pelo largo'),
(2, 'Británico de pelo corto', 'Gato robusto y tranquilo'),
(2, 'Mestizo', 'Gato sin raza definida');

-- Categorías de productos
INSERT INTO categoria_producto (nombre, descripcion, estado) VALUES
('Medicamentos', 'Productos farmacéuticos veterinarios', 'ACTIVO'),
('Alimentos', 'Alimentos para mascotas', 'ACTIVO'),
('Accesorios', 'Accesorios y juguetes', 'ACTIVO'),
('Higiene', 'Productos de higiene y limpieza', 'ACTIVO'),
('Suplementos', 'Suplementos nutricionales', 'ACTIVO'),
('Vacunas', 'Vacunas y biológicos', 'ACTIVO');

-- Tenant de demostración (opcional)
INSERT INTO tenant (codigo_tenant, nombre_comercial, razon_social, ruc, telefono, email_contacto, direccion, ciudad, id_plan_actual, estado_suscripcion, nombre_propietario, email_propietario, telefono_propietario, dias_trial, estado) VALUES
('vet-demo', 'Veterinaria Demo', 'Veterinaria Demo SAC', '20123456789', '987654321', 'contacto@vetdemo.com', 'Av. Principal 123', 'Lima', 2, 'ACTIVO', 'Juan Pérez', 'juan@vetdemo.com', '987654321', 30, 'ACTIVO');

-- Suscripción activa para el tenant demo
INSERT INTO suscripcion (id_tenant, id_plan, fecha_inicio, fecha_fin, metodo_pago, monto_pagado, estado) VALUES
(1, 2, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'TARJETA', 99.99, 'ACTIVO');

-- Usuario admin del tenant demo
INSERT INTO usuario (id_tenant, id_rol, username, password_hash, email, nombres, apellidos, estado) VALUES
(1, 1, 'admin', '$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe', 'admin@vetdemo.com', 'Juan', 'Pérez', 'ACTIVO');

COMMIT;

-- ===========================================
-- VERIFICACIÓN FINAL
-- ===========================================
SELECT 'Base de datos Multi-Tenant creada exitosamente!' AS mensaje;
SELECT COUNT(*) AS total_tablas FROM information_schema.tables WHERE table_schema = 'veterinaria_saas';
SELECT 'Super Admin creado' AS tipo, username, email FROM super_admin
UNION ALL
SELECT 'Tenant Demo' AS tipo, codigo_tenant AS username, nombre_comercial AS email FROM tenant;
