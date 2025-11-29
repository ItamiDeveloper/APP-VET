-- =======================================================
-- SCRIPT COMPLETO DE BASE DE DATOS VETERINARIA_SAAS (MySQL)
-- =======================================================

-- 1. ELIMINAR BASE DE DATOS PREVIA (si existe)
DROP DATABASE IF EXISTS VETERINARIA_SAAS;

-- 2. CREAR BASE DE DATOS
CREATE DATABASE VETERINARIA_SAAS
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 3. SELECCIONAR LA BASE DE DATOS
USE VETERINARIA_SAAS;

-- ===========================================
-- TABLAS BASE
-- ===========================================

CREATE TABLE PLAN (
  id_plan                     INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL,
  precio_mensual              DECIMAL(10,2) NOT NULL,
  max_doctores                INT NOT NULL,
  max_mascotas                INT NOT NULL,
  max_almacenamiento_mb       INT NOT NULL,
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

CREATE TABLE ROL (
  id_rol          INT AUTO_INCREMENT PRIMARY KEY,
  nombre          VARCHAR(50) NOT NULL,
  descripcion     VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE ESPECIE (
  id_especie      INT AUTO_INCREMENT PRIMARY KEY,
  nombre          VARCHAR(100) NOT NULL,
  descripcion     VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE CATEGORIA_PRODUCTO (
  id_categoria    INT AUTO_INCREMENT PRIMARY KEY,
  nombre          VARCHAR(100) NOT NULL,
  descripcion     VARCHAR(255),
  estado          ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

CREATE TABLE PROVEEDOR (
  id_proveedor    INT AUTO_INCREMENT PRIMARY KEY,
  nombre          VARCHAR(150) NOT NULL,
  ruc             VARCHAR(20),
  telefono        VARCHAR(30),
  email           VARCHAR(100),
  direccion       VARCHAR(255),
  estado          ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- ===========================================
-- VETERINARIA Y RELACIONADOS
-- ===========================================

CREATE TABLE VETERINARIA (
  id_veterinaria    INT AUTO_INCREMENT PRIMARY KEY,
  id_plan           INT NOT NULL, -- Plan activo actual
  nombre            VARCHAR(150) NOT NULL,
  ruc               VARCHAR(20),
  telefono          VARCHAR(30),
  direccion         VARCHAR(255),
  -- Las fechas específicas del plan se gestionan mejor en SUSCRIPCION
  estado            ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  CONSTRAINT fk_veterinaria_plan
    FOREIGN KEY (id_plan) REFERENCES PLAN(id_plan)
) ENGINE=InnoDB;

CREATE TABLE SUSCRIPCION (
  id_suscripcion    INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_plan           INT NOT NULL,
  fecha_inicio      DATE NOT NULL,
  fecha_fin         DATE,
  estado            ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO', -- Indica si es el plan activo en este momento
  CONSTRAINT fk_suscripcion_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_suscripcion_plan
    FOREIGN KEY (id_plan) REFERENCES PLAN(id_plan)
) ENGINE=InnoDB;

CREATE TABLE DOCTOR (
  id_doctor         INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  -- id_usuario es clave foránea (FK) para vincularlo a su cuenta de acceso
  id_usuario        INT, 
  nombres           VARCHAR(100) NOT NULL,
  apellidos         VARCHAR(100) NOT NULL,
  colegiatura       VARCHAR(50),
  especialidad      VARCHAR(100),
  estado            ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  CONSTRAINT fk_doctor_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria)
    -- La FK a USUARIO se define más abajo para evitar problemas de orden
) ENGINE=InnoDB;

CREATE TABLE CLIENTE (
  id_cliente        INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  nombres           VARCHAR(100) NOT NULL,
  apellidos         VARCHAR(100) NOT NULL,
  tipo_documento    VARCHAR(50), -- Sugerencia: Añadir para claridad
  documento         VARCHAR(20),
  telefono          VARCHAR(30),
  email             VARCHAR(100),
  direccion         VARCHAR(255),
  estado            ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  CONSTRAINT fk_cliente_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria)
) ENGINE=InnoDB;

CREATE TABLE RAZA (
  id_raza           INT AUTO_INCREMENT PRIMARY KEY,
  id_especie        INT NOT NULL,
  nombre            VARCHAR(100) NOT NULL,
  descripcion       VARCHAR(255),
  CONSTRAINT fk_raza_especie
    FOREIGN KEY (id_especie) REFERENCES ESPECIE(id_especie)
) ENGINE=InnoDB;

CREATE TABLE MASCOTA (
  id_mascota        INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_cliente        INT NOT NULL,
  -- id_especie se ELIMINA porque ya está implícito en id_raza
  id_raza           INT NOT NULL,
  nombre            VARCHAR(100) NOT NULL,
  sexo              ENUM('M','H','OTRO') DEFAULT 'M',
  fecha_nacimiento  DATE,
  color             VARCHAR(50),
  estado            ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  CONSTRAINT fk_mascota_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_mascota_cliente
    FOREIGN KEY (id_cliente) REFERENCES CLIENTE(id_cliente),
  CONSTRAINT fk_mascota_raza
    FOREIGN KEY (id_raza) REFERENCES RAZA(id_raza)
) ENGINE=InnoDB;

-- ===========================================
-- USUARIO / SEGURIDAD / NOTIFICACIONES
-- ===========================================

CREATE TABLE USUARIO (
  id_usuario        INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_rol            INT NOT NULL,
  username          VARCHAR(50) NOT NULL UNIQUE,
  password_hash     VARCHAR(255) NOT NULL,
  email             VARCHAR(100) NOT NULL,
  estado            ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  CONSTRAINT fk_usuario_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_usuario_rol
    FOREIGN KEY (id_rol) REFERENCES ROL(id_rol)
) ENGINE=InnoDB;

-- Agregar FK a USUARIO en la tabla DOCTOR después de crear USUARIO
ALTER TABLE DOCTOR
ADD CONSTRAINT fk_doctor_usuario
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario);


CREATE TABLE NOTIFICACION (
  id_notificacion   INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_usuario        INT NOT NULL,
  titulo            VARCHAR(150) NOT NULL,
  mensaje           TEXT NOT NULL,
  tipo              VARCHAR(50),
  fecha             DATETIME NOT NULL,
  leido             TINYINT(1) NOT NULL DEFAULT 0,
  CONSTRAINT fk_notificacion_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_notificacion_usuario
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE AUDITORIA (
  id_auditoria      INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario        INT NOT NULL,
  tabla_afectada    VARCHAR(100) NOT NULL,
  id_registro       INT NOT NULL,
  accion            VARCHAR(50) NOT NULL,
  fecha_hora        DATETIME NOT NULL,
  detalle           TEXT,
  CONSTRAINT fk_auditoria_usuario
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
) ENGINE=InnoDB;

-- ===========================================
-- HISTORIA CLÍNICA / CITA / RECETA / ARCHIVOS
-- ===========================================

CREATE TABLE CITA (
  id_cita           INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_mascota        INT NOT NULL,
  id_doctor         INT NOT NULL,
  fecha_hora        DATETIME NOT NULL,
  motivo            VARCHAR(255),
  estado            ENUM('PENDIENTE','ATENDIDA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
  CONSTRAINT fk_cita_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_cita_mascota
    FOREIGN KEY (id_mascota) REFERENCES MASCOTA(id_mascota),
  CONSTRAINT fk_cita_doctor
    FOREIGN KEY (id_doctor) REFERENCES DOCTOR(id_doctor)
) ENGINE=InnoDB;

CREATE TABLE HISTORIA_CLINICA (
  id_historia       INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_mascota        INT NOT NULL,
  id_doctor         INT NOT NULL,
  fecha_atencion    DATETIME NOT NULL,
  diagnostico       TEXT,
  tratamiento       TEXT,
  observaciones     TEXT,
  CONSTRAINT fk_historia_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_historia_mascota
    FOREIGN KEY (id_mascota) REFERENCES MASCOTA(id_mascota),
  CONSTRAINT fk_historia_doctor
    FOREIGN KEY (id_doctor) REFERENCES DOCTOR(id_doctor)
) ENGINE=InnoDB;

CREATE TABLE RECETA (
  id_receta         INT AUTO_INCREMENT PRIMARY KEY,
  id_historia       INT NOT NULL,
  id_doctor         INT NOT NULL,
  id_mascota        INT NOT NULL,
  fecha_emision     DATETIME NOT NULL,
  observaciones     TEXT,
  estado            ENUM('ACTIVA','ANULADA') NOT NULL DEFAULT 'ACTIVA',
  CONSTRAINT fk_receta_historia
    FOREIGN KEY (id_historia) REFERENCES HISTORIA_CLINICA(id_historia),
  CONSTRAINT fk_receta_doctor
    FOREIGN KEY (id_doctor) REFERENCES DOCTOR(id_doctor),
  CONSTRAINT fk_receta_mascota
    FOREIGN KEY (id_mascota) REFERENCES MASCOTA(id_mascota)
) ENGINE=InnoDB;

CREATE TABLE ARCHIVO_ADJUNTO (
  id_archivo        INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_historia       INT NOT NULL,
  id_mascota        INT NOT NULL,
  ruta_archivo      VARCHAR(255) NOT NULL,
  tipo              VARCHAR(50),
  descripcion       VARCHAR(255),
  fecha_subida      DATETIME NOT NULL,
  CONSTRAINT fk_archivo_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_archivo_historia
    FOREIGN KEY (id_historia) REFERENCES HISTORIA_CLINICA(id_historia),
  CONSTRAINT fk_archivo_mascota
    FOREIGN KEY (id_mascota) REFERENCES MASCOTA(id_mascota)
) ENGINE=InnoDB;

CREATE TABLE PRODUCTO (
  id_producto       INT AUTO_INCREMENT PRIMARY KEY,
  id_categoria      INT NOT NULL,
  nombre            VARCHAR(150) NOT NULL,
  descripcion       VARCHAR(255),
  es_medicamento    TINYINT(1) NOT NULL DEFAULT 0,
  precio_unitario   DECIMAL(10,2) NOT NULL,
  estado            ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  CONSTRAINT fk_producto_categoria
    FOREIGN KEY (id_categoria) REFERENCES CATEGORIA_PRODUCTO(id_categoria)
) ENGINE=InnoDB;

CREATE TABLE DETALLE_RECETA (
  id_detalle_receta INT AUTO_INCREMENT PRIMARY KEY,
  id_receta         INT NOT NULL,
  id_producto       INT NOT NULL,
  dosis             VARCHAR(100) NOT NULL,
  duracion          VARCHAR(100),
  indicaciones      TEXT,
  CONSTRAINT fk_detalle_receta_receta
    FOREIGN KEY (id_receta) REFERENCES RECETA(id_receta),
  CONSTRAINT fk_detalle_receta_producto
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
) ENGINE=InnoDB;

-- ===========================================
-- INVENTARIO / COMPRAS / VENTAS
-- ===========================================

CREATE TABLE INVENTARIO (
  id_inventario     INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_producto       INT NOT NULL,
  stock_actual      INT NOT NULL DEFAULT 0,
  stock_minimo      INT NOT NULL DEFAULT 0,
  stock_maximo      INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_inventario_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_inventario_producto
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
) ENGINE=InnoDB;

CREATE TABLE COMPRA (
  id_compra         INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_proveedor      INT NOT NULL,
  fecha             DATETIME NOT NULL,
  total             DECIMAL(10,2) NOT NULL,
  estado            ENUM('PENDIENTE','PAGADA','ANULADA') NOT NULL DEFAULT 'PENDIENTE',
  CONSTRAINT fk_compra_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_compra_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES PROVEEDOR(id_proveedor)
) ENGINE=InnoDB;

CREATE TABLE DETALLE_COMPRA (
  id_detalle_compra INT AUTO_INCREMENT PRIMARY KEY,
  id_compra         INT NOT NULL,
  id_producto       INT NOT NULL,
  cantidad          INT NOT NULL,
  costo_unitario    DECIMAL(10,2) NOT NULL,
  subtotal          DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_detalle_compra_compra
    FOREIGN KEY (id_compra) REFERENCES COMPRA(id_compra),
  CONSTRAINT fk_detalle_compra_producto
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
) ENGINE=InnoDB;

CREATE TABLE VENTA (
  id_venta          INT AUTO_INCREMENT PRIMARY KEY,
  id_veterinaria    INT NOT NULL,
  id_cliente        INT NOT NULL,
  fecha             DATETIME NOT NULL,
  total             DECIMAL(10,2) NOT NULL,
  metodo_pago       VARCHAR(50),
  estado            ENUM('PENDIENTE','PAGADA','ANULADA') NOT NULL DEFAULT 'PENDIENTE',
  CONSTRAINT fk_venta_veterinaria
    FOREIGN KEY (id_veterinaria) REFERENCES VETERINARIA(id_veterinaria),
  CONSTRAINT fk_venta_cliente
    FOREIGN KEY (id_cliente) REFERENCES CLIENTE(id_cliente)
) ENGINE=InnoDB;


CREATE TABLE DETALLE_VENTA (
  id_detalle_venta  INT AUTO_INCREMENT PRIMARY KEY,
  id_venta          INT NOT NULL,
  id_producto       INT NOT NULL,
  cantidad          INT NOT NULL,
  precio_unitario   DECIMAL(10,2) NOT NULL,
  subtotal          DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_detalle_venta_venta
    FOREIGN KEY (id_venta) REFERENCES VENTA(id_venta),
  CONSTRAINT fk_detalle_venta_producto
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
) ENGINE=InnoDB;

-- 4. CONSULTA FINAL (para verificar que se creó la tabla VETERINARIA)
SHOW TABLES LIKE 'VETERINARIA';