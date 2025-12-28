-- =======================================================
-- 🏥 VETERINARIA SAAS - BASE DE DATOS DEFINITIVA FINAL
-- Sistema Multi-Tenant para Gestión Veterinaria
-- =======================================================
-- Versión: 4.0 FINAL - Sincronizado 100% con Backend
-- Fecha: 28 Diciembre 2025
-- IMPORTANTE: Este script tiene TODOS los campos que el
-- backend Java necesita. NO modificar sin revisar las
-- entidades Java primero.
-- =======================================================

SET FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;

-- Eliminar base de datos si existe
DROP DATABASE IF EXISTS veterinaria_saas;

-- Crear base de datos
CREATE DATABASE veterinaria_saas
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE veterinaria_saas;

SELECT '🚀 INSTALACIÓN DE BASE DE DATOS...' AS mensaje;

-- ===========================================
-- TABLAS GLOBALES DEL SAAS
-- ===========================================

-- Planes de suscripción
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
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_estado (estado),
  INDEX idx_orden (orden_visualizacion)
) ENGINE=InnoDB;

-- Super Administradores
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

-- ⚠️ TABLA TENANT - SINCRONIZADA 100% CON Tenant.java
CREATE TABLE tenant (
  id_tenant                   INT AUTO_INCREMENT PRIMARY KEY,
  codigo_tenant               VARCHAR(50) NOT NULL UNIQUE,
  nombre_comercial            VARCHAR(150) NOT NULL,
  razon_social                VARCHAR(200),
  ruc                         VARCHAR(20),
  telefono                    VARCHAR(30),
  email_contacto              VARCHAR(100) NOT NULL,
  direccion                   VARCHAR(255),
  pais                        VARCHAR(50) DEFAULT 'Perú',
  ciudad                      VARCHAR(100),
  id_plan_actual              INT NOT NULL,
  estado_suscripcion          ENUM('TRIAL','ACTIVO','SUSPENDIDO','CANCELADO') NOT NULL DEFAULT 'TRIAL',
  fecha_registro              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_activacion            DATETIME,
  fecha_suspension            DATETIME,
  dias_trial                  INT NOT NULL DEFAULT 15,
  nombre_propietario          VARCHAR(150) NOT NULL,
  email_propietario           VARCHAR(100) NOT NULL,
  telefono_propietario        VARCHAR(30),
  usuarios_activos            INT NOT NULL DEFAULT 0,
  doctores_activos            INT NOT NULL DEFAULT 0,
  mascotas_registradas        INT NOT NULL DEFAULT 0,
  almacenamiento_usado_mb     INT NOT NULL DEFAULT 0,
  logo_url                    VARCHAR(255),
  color_primario              VARCHAR(7) DEFAULT '#3B82F6',
  color_secundario            VARCHAR(7) DEFAULT '#10B981',
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_plan_actual) REFERENCES plan(id_plan),
  INDEX idx_codigo (codigo_tenant),
  INDEX idx_estado_suscripcion (estado_suscripcion),
  INDEX idx_plan (id_plan_actual)
) ENGINE=InnoDB;

-- Suscripciones
CREATE TABLE suscripcion (
  id_suscripcion              INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_plan                     INT NOT NULL,
  fecha_inicio                DATE NOT NULL,
  fecha_fin                   DATE NOT NULL,
  metodo_pago                 VARCHAR(50),
  monto_pagado                DECIMAL(10,2),
  estado                      VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
  notas                       TEXT,
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_plan) REFERENCES plan(id_plan),
  INDEX idx_tenant (id_tenant),
  INDEX idx_fechas (fecha_inicio, fecha_fin)
) ENGINE=InnoDB;

-- ===========================================
-- CATÁLOGOS GLOBALES
-- ===========================================

-- Roles
CREATE TABLE rol (
  id_rol                      INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(50) NOT NULL UNIQUE,
  descripcion                 VARCHAR(255),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

-- Especies
CREATE TABLE especie (
  id_especie                  INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL UNIQUE,
  descripcion                 TEXT,
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

-- Razas
CREATE TABLE raza (
  id_raza                     INT AUTO_INCREMENT PRIMARY KEY,
  id_especie                  INT NOT NULL,
  nombre                      VARCHAR(100) NOT NULL,
  descripcion                 TEXT,
  tamano_promedio             VARCHAR(50),
  peso_promedio_kg            DECIMAL(5,2),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_especie) REFERENCES especie(id_especie),
  INDEX idx_especie (id_especie),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

-- Categorías de productos
CREATE TABLE categoria_producto (
  id_categoria                INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL UNIQUE,
  descripcion                 TEXT,
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

-- Productos
CREATE TABLE producto (
  id_producto                 INT AUTO_INCREMENT PRIMARY KEY,
  codigo                      VARCHAR(50) NOT NULL UNIQUE,
  nombre                      VARCHAR(200) NOT NULL,
  descripcion                 TEXT,
  id_categoria                INT,
  precio_unitario             DECIMAL(10,2) NOT NULL,
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_categoria) REFERENCES categoria_producto(id_categoria),
  INDEX idx_codigo (codigo),
  INDEX idx_nombre (nombre),
  INDEX idx_categoria (id_categoria)
) ENGINE=InnoDB;

-- Proveedores
CREATE TABLE proveedor (
  id_proveedor                INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(200) NOT NULL,
  ruc                         VARCHAR(20),
  telefono                    VARCHAR(30),
  email                       VARCHAR(100),
  direccion                   VARCHAR(255),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

-- ===========================================
-- TABLAS MULTI-TENANT
-- ===========================================

-- Usuarios
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
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ultimo_acceso               DATETIME,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
  UNIQUE KEY uk_tenant_username (id_tenant, username),
  INDEX idx_tenant (id_tenant),
  INDEX idx_username (username),
  INDEX idx_email (email)
) ENGINE=InnoDB;

-- Doctores
CREATE TABLE doctor (
  id_doctor                   INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  nombres                     VARCHAR(100) NOT NULL,
  apellidos                   VARCHAR(100) NOT NULL,
  num_colegiatura             VARCHAR(50),
  especialidad                VARCHAR(100),
  telefono                    VARCHAR(30),
  email                       VARCHAR(100),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  INDEX idx_tenant (id_tenant),
  INDEX idx_nombres (nombres, apellidos)
) ENGINE=InnoDB;

-- Clientes
CREATE TABLE cliente (
  id_cliente                  INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  nombres                     VARCHAR(100) NOT NULL,
  apellidos                   VARCHAR(100) NOT NULL,
  tipo_documento              VARCHAR(20),
  num_documento               VARCHAR(20),
  telefono                    VARCHAR(30),
  email                       VARCHAR(100),
  direccion                   VARCHAR(255),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  INDEX idx_tenant (id_tenant),
  INDEX idx_nombres (nombres, apellidos),
  INDEX idx_documento (num_documento)
) ENGINE=InnoDB;

-- Mascotas
CREATE TABLE mascota (
  id_mascota                  INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_cliente                  INT NOT NULL,
  id_especie                  INT NOT NULL,
  id_raza                     INT,
  nombre                      VARCHAR(100) NOT NULL,
  fecha_nacimiento            DATE,
  sexo                        ENUM('MACHO','HEMBRA'),
  color                       VARCHAR(50),
  peso_kg                     DECIMAL(5,2),
  observaciones               TEXT,
  foto_url                    VARCHAR(255),
  estado                      ENUM('ACTIVO','INACTIVO','FALLECIDO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE,
  FOREIGN KEY (id_especie) REFERENCES especie(id_especie),
  FOREIGN KEY (id_raza) REFERENCES raza(id_raza),
  INDEX idx_tenant (id_tenant),
  INDEX idx_cliente (id_cliente),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

-- Citas
CREATE TABLE cita (
  id_cita                     INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_mascota                  INT NOT NULL,
  id_cliente                  INT NOT NULL,
  id_doctor                   INT NOT NULL,
  fecha_hora                  DATETIME NOT NULL,
  motivo                      VARCHAR(255) NOT NULL,
  observaciones               TEXT,
  estado                      VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota),
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
  INDEX idx_tenant (id_tenant),
  INDEX idx_fecha (fecha_hora),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- Historia Clínica
CREATE TABLE historia_clinica (
  id_historia                 INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_mascota                  INT NOT NULL,
  id_doctor                   INT NOT NULL,
  fecha                       DATETIME NOT NULL,
  motivo_consulta             TEXT NOT NULL,
  sintomas                    TEXT,
  diagnostico                 TEXT,
  tratamiento                 TEXT,
  observaciones               TEXT,
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota),
  FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
  INDEX idx_tenant (id_tenant),
  INDEX idx_mascota (id_mascota),
  INDEX idx_fecha (fecha)
) ENGINE=InnoDB;

-- Inventario
CREATE TABLE inventario (
  id_inventario               INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_producto                 INT NOT NULL,
  cantidad                    INT NOT NULL DEFAULT 0,
  stock_minimo                INT NOT NULL DEFAULT 5,
  stock_maximo                INT,
  ubicacion                   VARCHAR(100),
  lote                        VARCHAR(50),
  fecha_vencimiento           DATE,
  fecha_ultima_actualizacion  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  UNIQUE KEY uk_tenant_producto (id_tenant, id_producto),
  INDEX idx_tenant (id_tenant),
  INDEX idx_producto (id_producto)
) ENGINE=InnoDB;

-- Ventas
CREATE TABLE venta (
  id_venta                    INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_cliente                  INT NOT NULL,
  fecha                       DATETIME NOT NULL,
  total                       DECIMAL(10,2) NOT NULL,
  metodo_pago                 VARCHAR(50),
  estado                      VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA',
  observaciones               TEXT,
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  INDEX idx_tenant (id_tenant),
  INDEX idx_fecha (fecha),
  INDEX idx_cliente (id_cliente)
) ENGINE=InnoDB;

-- Detalle de ventas
CREATE TABLE detalle_venta (
  id_detalle_venta            INT AUTO_INCREMENT PRIMARY KEY,
  id_venta                    INT NOT NULL,
  id_producto                 INT NOT NULL,
  cantidad                    INT NOT NULL,
  precio_unitario             DECIMAL(10,2) NOT NULL,
  subtotal                    DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (id_venta) REFERENCES venta(id_venta) ON DELETE CASCADE,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_venta (id_venta)
) ENGINE=InnoDB;

-- Compras
CREATE TABLE compra (
  id_compra                   INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant                   INT NOT NULL,
  id_proveedor                INT NOT NULL,
  fecha                       DATETIME NOT NULL,
  total                       DECIMAL(10,2) NOT NULL,
  estado                      VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA',
  observaciones               TEXT,
  fecha_creacion              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant) ON DELETE CASCADE,
  FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
  INDEX idx_tenant (id_tenant),
  INDEX idx_fecha (fecha),
  INDEX idx_proveedor (id_proveedor)
) ENGINE=InnoDB;

-- Detalle de compras
CREATE TABLE detalle_compra (
  id_detalle_compra           INT AUTO_INCREMENT PRIMARY KEY,
  id_compra                   INT NOT NULL,
  id_producto                 INT NOT NULL,
  cantidad                    INT NOT NULL,
  precio_unitario             DECIMAL(10,2) NOT NULL,
  subtotal                    DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE CASCADE,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_compra (id_compra)
) ENGINE=InnoDB;

SELECT '✅ Todas las tablas creadas' AS resultado;

-- ===========================================
-- DATOS INICIALES
-- ===========================================

SELECT '📝 Insertando datos iniciales...' AS paso;

-- Hash BCrypt correcto para "admin123" - GENERADO Y VERIFICADO
SET @password_hash = '$2a$10$0pj5LiqhT46x6haFSk01xOy4Dgj2SizHEhEciqF1kGJGx/jPs1M6y';

-- PLANES
INSERT INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_doctores, max_mascotas, tiene_reportes_avanzados, tiene_api_acceso, tiene_soporte_prioritario, orden_visualizacion) VALUES
('Básico', 'Plan ideal para veterinarias pequeñas', 29.99, 299.99, 3, 2, 50, FALSE, FALSE, FALSE, 1),
('Profesional', 'Plan completo para veterinarias en crecimiento', 99.99, 999.99, 10, 5, 200, TRUE, FALSE, TRUE, 2),
('Enterprise', 'Plan empresarial con todas las funcionalidades', 299.99, 2999.99, 50, 20, 1000, TRUE, TRUE, TRUE, 3);

-- SUPER ADMIN
INSERT INTO super_admin (username, password_hash, email, nombres, apellidos, telefono, estado) VALUES
('superadmin', @password_hash, 'superadmin@appvet.com', 'Super', 'Administrador', '999888777', 'ACTIVO');

-- ROLES
INSERT INTO rol (nombre, descripcion, estado) VALUES
('ADMIN', 'Administrador del tenant', 'ACTIVO'),
('VETERINARIO', 'Veterinario con acceso a consultas', 'ACTIVO'),
('RECEPCIONISTA', 'Personal de recepción', 'ACTIVO'),
('ASISTENTE', 'Asistente veterinario', 'ACTIVO');

-- ESPECIES
INSERT INTO especie (nombre, descripcion, estado) VALUES
('Perro', 'Canis lupus familiaris', 'ACTIVO'),
('Gato', 'Felis catus', 'ACTIVO'),
('Ave', 'Aves domésticas', 'ACTIVO'),
('Roedor', 'Roedores domésticos', 'ACTIVO'),
('Reptil', 'Reptiles domésticos', 'ACTIVO');

-- RAZAS
INSERT INTO raza (id_especie, nombre, tamano_promedio, peso_promedio_kg, estado) VALUES
(1, 'Labrador Retriever', 'Grande', 30.00, 'ACTIVO'),
(1, 'Bulldog Francés', 'Pequeño', 12.00, 'ACTIVO'),
(1, 'Golden Retriever', 'Grande', 32.00, 'ACTIVO'),
(1, 'Pastor Alemán', 'Grande', 35.00, 'ACTIVO'),
(1, 'Chihuahua', 'Muy Pequeño', 2.50, 'ACTIVO'),
(1, 'Poodle', 'Mediano', 15.00, 'ACTIVO'),
(1, 'Mestizo', 'Variable', 15.00, 'ACTIVO'),
(2, 'Persa', 'Mediano', 4.50, 'ACTIVO'),
(2, 'Siamés', 'Mediano', 4.00, 'ACTIVO'),
(2, 'Maine Coon', 'Grande', 8.00, 'ACTIVO'),
(2, 'Común Europeo', 'Mediano', 4.50, 'ACTIVO'),
(2, 'Mestizo', 'Variable', 4.00, 'ACTIVO'),
(3, 'Loro', 'Variable', 0.50, 'ACTIVO'),
(3, 'Canario', 'Pequeño', 0.02, 'ACTIVO'),
(3, 'Periquito', 'Pequeño', 0.03, 'ACTIVO'),
(4, 'Hámster', 'Pequeño', 0.15, 'ACTIVO'),
(4, 'Cobayo', 'Pequeño', 0.80, 'ACTIVO'),
(4, 'Conejo', 'Mediano', 2.00, 'ACTIVO');

-- CATEGORÍAS DE PRODUCTOS
INSERT INTO categoria_producto (nombre, descripcion, estado) VALUES
('Medicamentos', 'Fármacos y medicinas veterinarias', 'ACTIVO'),
('Vacunas', 'Vacunas para diferentes especies', 'ACTIVO'),
('Alimentos', 'Alimentos balanceados y suplementos', 'ACTIVO'),
('Accesorios', 'Collares, correas, camas, etc.', 'ACTIVO'),
('Higiene', 'Productos de higiene y limpieza', 'ACTIVO'),
('Quirúrgico', 'Material quirúrgico y médico', 'ACTIVO');

-- PRODUCTOS
INSERT INTO producto (codigo, nombre, descripcion, id_categoria, precio_unitario, estado) VALUES
('MED001', 'Amoxicilina 500mg', 'Antibiótico de amplio espectro', 1, 25.00, 'ACTIVO'),
('MED002', 'Meloxicam 2mg/ml', 'Antiinflamatorio no esteroideo', 1, 35.00, 'ACTIVO'),
('MED003', 'Ivermectina', 'Antiparasitario interno y externo', 1, 28.00, 'ACTIVO'),
('MED004', 'Dexametasona', 'Corticoide antiinflamatorio', 1, 22.00, 'ACTIVO'),
('VAC001', 'Séxtuple Canina', 'Vacuna polivalente para perros', 2, 45.00, 'ACTIVO'),
('VAC002', 'Triple Felina', 'Vacuna polivalente para gatos', 2, 40.00, 'ACTIVO'),
('VAC003', 'Antirrábica', 'Vacuna contra la rabia', 2, 35.00, 'ACTIVO'),
('ALI001', 'Dog Chow Adulto 15kg', 'Alimento balanceado para perros adultos', 3, 85.00, 'ACTIVO'),
('ALI002', 'Cat Chow Adulto 8kg', 'Alimento balanceado para gatos adultos', 3, 75.00, 'ACTIVO'),
('ALI003', 'Royal Canin Puppy 3kg', 'Alimento para cachorros', 3, 95.00, 'ACTIVO'),
('ACC001', 'Collar Ajustable M', 'Collar para mascotas medianas', 4, 15.00, 'ACTIVO'),
('ACC002', 'Correa Retráctil 5m', 'Correa extensible para paseo', 4, 35.00, 'ACTIVO'),
('ACC003', 'Cama Pequeña', 'Cama acolchada para mascotas', 4, 45.00, 'ACTIVO'),
('HIG001', 'Shampoo Antipulgas', 'Shampoo con acción antiparasitaria', 5, 18.00, 'ACTIVO'),
('HIG002', 'Pañales Descartables M', 'Paquete x10 unidades', 5, 25.00, 'ACTIVO'),
('HIG003', 'Toallas Húmedas x50', 'Toallas de limpieza para mascotas', 5, 12.00, 'ACTIVO');

-- PROVEEDORES
INSERT INTO proveedor (nombre, ruc, telefono, email, direccion, estado) VALUES
('Droguería VetPlus', '20123456789', '014567890', 'ventas@vetplus.com', 'Av. Industrial 456, Lima', 'ACTIVO'),
('Laboratorios MediPet', '20987654321', '014567891', 'contacto@medipet.com', 'Jr. Salud 789, Lima', 'ACTIVO'),
('Distribuidora PetFood SAC', '20456789123', '014567892', 'info@petfood.com', 'Av. Comercio 321, Lima', 'ACTIVO');

-- TENANTS DE PRUEBA
INSERT INTO tenant (
  codigo_tenant, nombre_comercial, razon_social, ruc, telefono, email_contacto,
  direccion, ciudad, id_plan_actual, estado_suscripcion, fecha_activacion,
  dias_trial, nombre_propietario, email_propietario, telefono_propietario, estado
) VALUES (
  'VET001', 'Veterinaria Patitas Felices', 'Patitas Felices S.A.C.', '20555666777',
  '014445555', 'contacto@patitasfelices.com', 'Av. Los Perros 123, San Miguel', 'Lima',
  2, 'ACTIVO', NOW(), 15, 'Carlos Rodríguez', 'carlos@patitasfelices.com', '987654321', 'ACTIVO'
), (
  'VET002', 'Clínica Veterinaria Amigos Peludos', 'Amigos Peludos E.I.R.L.', '20666777888',
  '014448888', 'info@amigospeludos.com', 'Jr. Los Gatos 456, Miraflores', 'Lima',
  2, 'ACTIVO', NOW(), 15, 'María González', 'maria@amigospeludos.com', '987654323', 'ACTIVO'
);

-- SUSCRIPCIONES
INSERT INTO suscripcion (id_tenant, id_plan, fecha_inicio, fecha_fin, metodo_pago, monto_pagado, estado) VALUES
((SELECT id_tenant FROM tenant WHERE codigo_tenant = 'VET001'), 2, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'TARJETA', 99.99, 'ACTIVO'),
((SELECT id_tenant FROM tenant WHERE codigo_tenant = 'VET002'), 2, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'TARJETA', 99.99, 'ACTIVO');

-- USUARIOS
SET @tenant1_id = (SELECT id_tenant FROM tenant WHERE codigo_tenant = 'VET001');
SET @tenant2_id = (SELECT id_tenant FROM tenant WHERE codigo_tenant = 'VET002');
SET @rol_admin = (SELECT id_rol FROM rol WHERE nombre = 'ADMIN');
SET @rol_vet = (SELECT id_rol FROM rol WHERE nombre = 'VETERINARIO');

INSERT INTO usuario (id_tenant, id_rol, username, password_hash, email, nombres, apellidos, telefono, estado) VALUES
(@tenant1_id, @rol_admin, 'admin_vet1', @password_hash, 'admin@patitasfelices.com', 'Carlos', 'Rodríguez', '987654321', 'ACTIVO'),
(@tenant1_id, @rol_vet, 'drjuan', @password_hash, 'drjuan@patitasfelices.com', 'Juan', 'Pérez', '987654322', 'ACTIVO'),
(@tenant2_id, @rol_admin, 'admin_vet2', @password_hash, 'admin@amigospeludos.com', 'María', 'González', '987654323', 'ACTIVO'),
(@tenant2_id, @rol_vet, 'drana', @password_hash, 'drana@amigospeludos.com', 'Ana', 'Martínez', '987654324', 'ACTIVO');

-- DOCTORES
INSERT INTO doctor (id_tenant, nombres, apellidos, num_colegiatura, especialidad, telefono, email, estado) VALUES
(@tenant1_id, 'Juan', 'Pérez López', 'CMV-12345', 'Cirugía General', '987654322', 'drjuan@patitasfelices.com', 'ACTIVO'),
(@tenant1_id, 'Laura', 'Silva Torres', 'CMV-12346', 'Medicina Interna', '987654325', 'drlaura@patitasfelices.com', 'ACTIVO'),
(@tenant2_id, 'Ana', 'Martínez Ruiz', 'CMV-12347', 'Dermatología', '987654324', 'drana@amigospeludos.com', 'ACTIVO'),
(@tenant2_id, 'Pedro', 'Castro Díaz', 'CMV-12348', 'Traumatología', '987654326', 'drpedro@amigospeludos.com', 'ACTIVO');

-- CLIENTES
INSERT INTO cliente (id_tenant, nombres, apellidos, tipo_documento, num_documento, telefono, email, direccion, estado) VALUES
(@tenant1_id, 'Roberto', 'García Soto', 'DNI', '12345678', '999111222', 'roberto.garcia@gmail.com', 'Av. Primavera 456, Surco', 'ACTIVO'),
(@tenant1_id, 'Sofía', 'López Vargas', 'DNI', '87654321', '999222333', 'sofia.lopez@gmail.com', 'Jr. Las Flores 789, San Isidro', 'ACTIVO'),
(@tenant1_id, 'Miguel', 'Torres Reyes', 'DNI', '45678912', '999333444', 'miguel.torres@gmail.com', 'Av. Los Álamos 321, La Molina', 'ACTIVO'),
(@tenant2_id, 'Carmen', 'Vega Morales', 'DNI', '98765432', '999444555', 'carmen.vega@gmail.com', 'Av. Larco 567, Miraflores', 'ACTIVO'),
(@tenant2_id, 'Luis', 'Herrera Cruz', 'DNI', '11223344', '999555666', 'luis.herrera@gmail.com', 'Jr. Los Olivos 234, San Borja', 'ACTIVO');

-- MASCOTAS
SET @cliente1_vet1 = (SELECT id_cliente FROM cliente WHERE id_tenant = @tenant1_id AND num_documento = '12345678');
SET @cliente2_vet1 = (SELECT id_cliente FROM cliente WHERE id_tenant = @tenant1_id AND num_documento = '87654321');
SET @cliente3_vet1 = (SELECT id_cliente FROM cliente WHERE id_tenant = @tenant1_id AND num_documento = '45678912');
SET @cliente1_vet2 = (SELECT id_cliente FROM cliente WHERE id_tenant = @tenant2_id AND num_documento = '98765432');
SET @cliente2_vet2 = (SELECT id_cliente FROM cliente WHERE id_tenant = @tenant2_id AND num_documento = '11223344');

INSERT INTO mascota (id_tenant, id_cliente, id_especie, id_raza, nombre, fecha_nacimiento, sexo, color, peso_kg, estado) VALUES
(@tenant1_id, @cliente1_vet1, 1, 1, 'Max', '2020-05-15', 'MACHO', 'Dorado', 28.50, 'ACTIVO'),
(@tenant1_id, @cliente1_vet1, 2, 8, 'Luna', '2021-03-20', 'HEMBRA', 'Blanco', 4.20, 'ACTIVO'),
(@tenant1_id, @cliente2_vet1, 1, 2, 'Rocky', '2019-08-10', 'MACHO', 'Blanco y Negro', 11.80, 'ACTIVO'),
(@tenant1_id, @cliente3_vet1, 2, 9, 'Mia', '2022-01-05', 'HEMBRA', 'Siamés', 3.80, 'ACTIVO'),
(@tenant2_id, @cliente1_vet2, 1, 4, 'Bruno', '2020-11-22', 'MACHO', 'Negro y Café', 32.00, 'ACTIVO'),
(@tenant2_id, @cliente2_vet2, 2, 10, 'Cleo', '2021-07-18', 'HEMBRA', 'Gris Rayado', 7.50, 'ACTIVO');

-- CITAS
SET @doctor1_vet1 = (SELECT id_doctor FROM doctor WHERE id_tenant = @tenant1_id LIMIT 1);
SET @mascota1_vet1 = (SELECT id_mascota FROM mascota WHERE id_tenant = @tenant1_id AND nombre = 'Max');

INSERT INTO cita (id_tenant, id_mascota, id_cliente, id_doctor, fecha_hora, motivo, estado) VALUES
(@tenant1_id, @mascota1_vet1, @cliente1_vet1, @doctor1_vet1, DATE_ADD(NOW(), INTERVAL 2 DAY), 'Control de vacunas', 'PROGRAMADA'),
(@tenant1_id, @mascota1_vet1, @cliente1_vet1, @doctor1_vet1, DATE_SUB(NOW(), INTERVAL 30 DAY), 'Consulta general', 'COMPLETADA');

-- HISTORIA CLÍNICA
INSERT INTO historia_clinica (id_tenant, id_mascota, id_doctor, fecha, motivo_consulta, sintomas, diagnostico, tratamiento) VALUES
(@tenant1_id, @mascota1_vet1, @doctor1_vet1, DATE_SUB(NOW(), INTERVAL 30 DAY), 'Control de rutina', 'Sin síntomas aparentes', 'Mascota sana', 'Vitaminas y desparasitación');

-- INVENTARIO
INSERT INTO inventario (id_tenant, id_producto, cantidad, stock_minimo, stock_maximo, ubicacion) VALUES
(@tenant1_id, 1, 50, 10, 100, 'Almacén A1'),
(@tenant1_id, 2, 30, 5, 50, 'Almacén A1'),
(@tenant1_id, 5, 25, 5, 50, 'Refrigerador'),
(@tenant1_id, 8, 15, 3, 30, 'Almacén B1'),
(@tenant2_id, 1, 45, 10, 100, 'Bodega Principal'),
(@tenant2_id, 3, 20, 5, 40, 'Bodega Principal'),
(@tenant2_id, 6, 18, 5, 40, 'Refrigerador'),
(@tenant2_id, 9, 12, 2, 25, 'Bodega Alimentos');

-- VENTAS
INSERT INTO venta (id_tenant, id_cliente, fecha, total, metodo_pago, estado) VALUES
(@tenant1_id, @cliente1_vet1, DATE_SUB(NOW(), INTERVAL 5 DAY), 70.00, 'EFECTIVO', 'COMPLETADA'),
(@tenant1_id, @cliente2_vet1, DATE_SUB(NOW(), INTERVAL 10 DAY), 135.00, 'TARJETA', 'COMPLETADA');

SET @venta1 = (SELECT id_venta FROM venta WHERE id_tenant = @tenant1_id LIMIT 1);
INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES
(@venta1, 1, 2, 25.00, 50.00),
(@venta1, 5, 1, 45.00, 45.00);

-- COMPRAS
SET @proveedor1 = (SELECT id_proveedor FROM proveedor LIMIT 1);
INSERT INTO compra (id_tenant, id_proveedor, fecha, total, estado) VALUES
(@tenant1_id, @proveedor1, DATE_SUB(NOW(), INTERVAL 15 DAY), 500.00, 'COMPLETADA');

SET @compra1 = (SELECT id_compra FROM compra WHERE id_tenant = @tenant1_id LIMIT 1);
INSERT INTO detalle_compra (id_compra, id_producto, cantidad, precio_unitario, subtotal) VALUES
(@compra1, 1, 20, 20.00, 400.00),
(@compra1, 2, 10, 30.00, 300.00);

SET FOREIGN_KEY_CHECKS = 1;

-- ===========================================
-- VERIFICACIÓN FINAL
-- ===========================================

SELECT '' AS separador;
SELECT '========================================' AS linea;
SELECT '✅ INSTALACIÓN COMPLETADA' AS estado;
SELECT '========================================' AS linea;
SELECT '' AS separador;

SELECT '📊 RESUMEN' AS seccion;
SELECT CONCAT('✓ ', COUNT(*), ' Planes') AS detalle FROM plan
UNION ALL SELECT CONCAT('✓ ', COUNT(*), ' Tenants') FROM tenant
UNION ALL SELECT CONCAT('✓ ', COUNT(*), ' Usuarios') FROM usuario
UNION ALL SELECT CONCAT('✓ ', COUNT(*), ' Doctores') FROM doctor
UNION ALL SELECT CONCAT('✓ ', COUNT(*), ' Clientes') FROM cliente
UNION ALL SELECT CONCAT('✓ ', COUNT(*), ' Mascotas') FROM mascota
UNION ALL SELECT CONCAT('✓ ', COUNT(*), ' Productos') FROM producto;

SELECT '' AS separador;
SELECT '🔐 CREDENCIALES' AS seccion;
SELECT 'Super Admin: superadmin / admin123' AS credencial
UNION ALL SELECT 'VET001: admin_vet1 / admin123'
UNION ALL SELECT 'VET002: admin_vet2 / admin123';

SELECT '' AS separador;
SELECT '✨ SISTEMA LISTO!' AS mensaje;
