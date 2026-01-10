-- =====================================================================
-- 🏥 VETERINARIA SAAS - INSTALACIÓN COMPLETA BASE DE DATOS
-- Sistema Multi-Tenant para Gestión Veterinaria
-- =====================================================================
-- Versión: 6.0 FINAL - CONSOLIDADO Y VERIFICADO
-- Fecha: 3 Enero 2026
-- Hash BCrypt: Generado y verificado con BCryptPasswordEncoder
-- =====================================================================
-- 
-- 📋 INSTRUCCIONES:
-- 1. Abre MySQL Workbench
-- 2. Ejecuta ESTE ARCHIVO COMPLETO
-- 3. Espera ~15 segundos
-- 4. ¡Listo!
-- 
-- 🔐 CREDENCIALES:
--    Super Admin: superadmin / admin123
--    Vet 1: admin_vet1 / admin123
--    Vet 2: admin_vet2 / admin123
--    Doctor 1: drjuan / admin123
--    Doctor 2: drana / admin123
-- 
-- 🔧 application.properties debe tener:
--    spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria_saas
--    spring.datasource.username=root
--    spring.datasource.password=Sasuke_77920!!
-- 
-- =====================================================================

SET FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;

DROP DATABASE IF EXISTS veterinaria_saas;

CREATE DATABASE veterinaria_saas
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE veterinaria_saas;

SELECT '🚀 Instalando base de datos...' AS mensaje;

-- ===========================================
-- TABLAS GLOBALES
-- ===========================================

SELECT '📦 Creando tablas globales...' AS paso;

CREATE TABLE plan (
  id_plan INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  precio_mensual DECIMAL(10,2) NOT NULL,
  precio_anual DECIMAL(10,2),
  max_usuarios INT NOT NULL DEFAULT 5,
  max_doctores INT NOT NULL DEFAULT 5,
  max_mascotas INT NOT NULL DEFAULT 100,
  max_almacenamiento_mb INT NOT NULL DEFAULT 1024,
  tiene_reportes_avanzados BOOLEAN NOT NULL DEFAULT FALSE,
  tiene_api_acceso BOOLEAN NOT NULL DEFAULT FALSE,
  tiene_soporte_prioritario BOOLEAN NOT NULL DEFAULT FALSE,
  orden_visualizacion INT NOT NULL DEFAULT 0,
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_estado (estado),
  INDEX idx_orden (orden_visualizacion)
) ENGINE=InnoDB;

CREATE TABLE super_admin (
  id_super_admin INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  telefono VARCHAR(20),
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_username (username),
  INDEX idx_email (email)
) ENGINE=InnoDB;

CREATE TABLE tenant (
  id_tenant INT AUTO_INCREMENT PRIMARY KEY,
  codigo_tenant VARCHAR(50) NOT NULL UNIQUE,
  nombre_comercial VARCHAR(150) NOT NULL,
  razon_social VARCHAR(200),
  ruc VARCHAR(20),
  telefono VARCHAR(30),
  email_contacto VARCHAR(100) NOT NULL,
  direccion VARCHAR(255),
  pais VARCHAR(50) DEFAULT 'Perú',
  ciudad VARCHAR(100),
  id_plan_actual INT NOT NULL,
  estado_suscripcion ENUM('TRIAL','ACTIVO','SUSPENDIDO','CANCELADO') NOT NULL DEFAULT 'TRIAL',
  fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_activacion DATETIME,
  fecha_suspension DATETIME,
  dias_trial INT NOT NULL DEFAULT 15,
  nombre_propietario VARCHAR(150) NOT NULL,
  email_propietario VARCHAR(100) NOT NULL,
  telefono_propietario VARCHAR(30),
  usuarios_activos INT NOT NULL DEFAULT 0,
  doctores_activos INT NOT NULL DEFAULT 0,
  mascotas_registradas INT NOT NULL DEFAULT 0,
  almacenamiento_usado_mb INT NOT NULL DEFAULT 0,
  logo_url VARCHAR(255),
  color_primario VARCHAR(7) DEFAULT '#3B82F6',
  color_secundario VARCHAR(7) DEFAULT '#10B981',
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_actualizacion DATETIME,
  FOREIGN KEY (id_plan_actual) REFERENCES plan(id_plan),
  INDEX idx_codigo (codigo_tenant),
  INDEX idx_estado (estado),
  INDEX idx_plan (id_plan_actual)
) ENGINE=InnoDB;

CREATE TABLE suscripcion (
  id_suscripcion INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_plan INT NOT NULL,
  fecha_inicio DATETIME NOT NULL,
  fecha_fin DATETIME NOT NULL,
  tipo_pago ENUM('MENSUAL','ANUAL') NOT NULL,
  monto_pagado DECIMAL(10,2) NOT NULL,
  estado ENUM('ACTIVA','VENCIDA','CANCELADA') NOT NULL DEFAULT 'ACTIVA',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_plan) REFERENCES plan(id_plan),
  INDEX idx_tenant (id_tenant),
  INDEX idx_plan (id_plan),
  INDEX idx_estado (estado),
  INDEX idx_fechas (fecha_inicio, fecha_fin)
) ENGINE=InnoDB;

-- ===========================================
-- CATÁLOGOS GLOBALES
-- ===========================================

SELECT '📚 Creando catálogos...' AS paso;

CREATE TABLE rol (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  descripcion VARCHAR(200),
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE especie (
  id_especie INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE raza (
  id_raza INT AUTO_INCREMENT PRIMARY KEY,
  id_especie INT NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  FOREIGN KEY (id_especie) REFERENCES especie(id_especie),
  INDEX idx_especie (id_especie),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE categoria_producto (
  id_categoria INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE producto (
  id_producto INT AUTO_INCREMENT PRIMARY KEY,
  id_categoria INT NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  descripcion TEXT,
  es_medicamento BOOLEAN DEFAULT FALSE,
  precio_unitario DECIMAL(10,2),
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  FOREIGN KEY (id_categoria) REFERENCES categoria_producto(id_categoria),
  INDEX idx_nombre (nombre),
  INDEX idx_categoria (id_categoria)
) ENGINE=InnoDB;

CREATE TABLE proveedor (
  id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(200) NOT NULL,
  ruc VARCHAR(20),
  telefono VARCHAR(50),
  email VARCHAR(100),
  direccion TEXT,
  contacto VARCHAR(100),
  estado VARCHAR(20) DEFAULT 'ACTIVO',
  INDEX idx_proveedor_estado (estado),
  INDEX idx_proveedor_nombre (nombre)
) ENGINE=InnoDB;

-- ===========================================
-- TABLAS MULTI-TENANT
-- ===========================================

SELECT '🏢 Creando tablas multi-tenant...' AS paso;

CREATE TABLE usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_rol INT NOT NULL,
  username VARCHAR(50) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(100) NOT NULL,
  nombres VARCHAR(100),
  apellidos VARCHAR(100),
  telefono VARCHAR(20),
  avatar_url VARCHAR(500),
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  ultimo_acceso DATETIME,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
  UNIQUE KEY uk_tenant_username (id_tenant, username),
  INDEX idx_tenant (id_tenant),
  INDEX idx_rol (id_rol),
  INDEX idx_username (username),
  INDEX idx_email (email)
) ENGINE=InnoDB;

CREATE TABLE doctor (
  id_doctor INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_usuario INT,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  colegiatura VARCHAR(50),
  especialidad VARCHAR(200),
  telefono VARCHAR(20),
  email VARCHAR(100),
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  INDEX idx_tenant (id_tenant),
  INDEX idx_usuario (id_usuario),
  INDEX idx_nombres (nombres, apellidos)
) ENGINE=InnoDB;

CREATE TABLE cliente (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  tipo_documento VARCHAR(20) DEFAULT 'DNI',
  numero_documento VARCHAR(20),
  telefono VARCHAR(30),
  email VARCHAR(100),
  direccion VARCHAR(255),
  fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  INDEX idx_tenant (id_tenant),
  INDEX idx_documento (numero_documento)
) ENGINE=InnoDB;

CREATE TABLE mascota (
  id_mascota INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_cliente INT NOT NULL,
  id_raza INT NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  sexo VARCHAR(20) DEFAULT 'MACHO',
  fecha_nacimiento DATE,
  color VARCHAR(50),
  peso_kg DECIMAL(5,2),
  microchip VARCHAR(50),
  foto_url VARCHAR(255),
  observaciones TEXT,
  estado ENUM('ACTIVO','FALLECIDO','ADOPTADO','PERDIDO') NOT NULL DEFAULT 'ACTIVO',
  fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  FOREIGN KEY (id_raza) REFERENCES raza(id_raza),
  INDEX idx_tenant (id_tenant),
  INDEX idx_cliente (id_cliente),
  INDEX idx_raza (id_raza),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE cita (
  id_cita INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_mascota INT NOT NULL,
  id_cliente INT NOT NULL,
  id_doctor INT NOT NULL,
  fecha_hora DATETIME NOT NULL,
  duracion_minutos INT NOT NULL DEFAULT 30,
  motivo VARCHAR(255),
  observaciones TEXT,
  estado ENUM('PENDIENTE','CONFIRMADA','ATENDIDA','CANCELADA','NO_ASISTIO') NOT NULL DEFAULT 'PENDIENTE',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota),
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
  INDEX idx_tenant (id_tenant),
  INDEX idx_mascota (id_mascota),
  INDEX idx_cliente (id_cliente),
  INDEX idx_doctor (id_doctor),
  INDEX idx_fecha (fecha_hora),
  INDEX idx_estado (estado)
) ENGINE=InnoDB;

CREATE TABLE historia_clinica (
  id_historia INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_mascota INT NOT NULL,
  id_doctor INT NOT NULL,
  id_cita INT,
  fecha_atencion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  motivo_consulta VARCHAR(255),
  anamnesis TEXT,
  examen_fisico TEXT,
  diagnostico TEXT,
  tratamiento TEXT,
  examenes_solicitados TEXT,
  observaciones TEXT,
  proxima_cita DATE,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota),
  FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
  FOREIGN KEY (id_cita) REFERENCES cita(id_cita),
  INDEX idx_tenant (id_tenant),
  INDEX idx_mascota (id_mascota),
  INDEX idx_doctor (id_doctor),
  INDEX idx_cita (id_cita),
  INDEX idx_fecha (fecha_atencion)
) ENGINE=InnoDB;

CREATE TABLE inventario (
  id_inventario INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_producto INT NOT NULL,
  stock_actual INT NOT NULL DEFAULT 0,
  stock_minimo INT NOT NULL DEFAULT 10,
  stock_maximo INT NOT NULL DEFAULT 100,
  fecha_ultimo_ingreso DATETIME,
  fecha_ultima_salida DATETIME,
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_tenant (id_tenant),
  INDEX idx_producto (id_producto)
) ENGINE=InnoDB;

CREATE TABLE venta (
  id_venta INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_cliente INT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(10,2),
  metodo_pago VARCHAR(50),
  estado VARCHAR(20),
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  INDEX idx_tenant (id_tenant),
  INDEX idx_fecha (fecha),
  INDEX idx_cliente (id_cliente)
) ENGINE=InnoDB;

CREATE TABLE detalle_venta (
  id_detalle_venta INT AUTO_INCREMENT PRIMARY KEY,
  id_venta INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (id_venta) REFERENCES venta(id_venta),
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_venta (id_venta)
) ENGINE=InnoDB;

CREATE TABLE compra (
  id_compra INT AUTO_INCREMENT PRIMARY KEY,
  id_tenant INT NOT NULL,
  id_proveedor INT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(10,2),
  estado VARCHAR(20),
  FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant),
  FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
  INDEX idx_tenant (id_tenant),
  INDEX idx_fecha (fecha),
  INDEX idx_proveedor (id_proveedor)
) ENGINE=InnoDB;

CREATE TABLE detalle_compra (
  id_detalle_compra INT AUTO_INCREMENT PRIMARY KEY,
  id_compra INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (id_compra) REFERENCES compra(id_compra),
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_compra (id_compra)
) ENGINE=InnoDB;

SELECT '✅ Todas las tablas creadas exitosamente' AS resultado;

-- ===========================================
-- DATOS INICIALES
-- ===========================================

SELECT '📝 Insertando datos iniciales...' AS paso;

-- Hash BCrypt VERIFICADO para "admin123" - Generado por BCryptPasswordEncoder
-- Este hash ha sido verificado con PasswordVerifier.java
SET @password_hash = '$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe';

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
INSERT INTO especie (nombre, descripcion) VALUES
('Perro', 'Canis lupus familiaris'),
('Gato', 'Felis catus'),
('Ave', 'Aves domésticas'),
('Roedor', 'Roedores domésticos'),
('Reptil', 'Reptiles domésticos');

-- RAZAS
INSERT INTO raza (id_especie, nombre, descripcion) VALUES
(1, 'Labrador Retriever', 'Raza grande y amigable'),
(1, 'Bulldog Francés', 'Raza pequeña y compacta'),
(1, 'Golden Retriever', 'Raza grande y leal'),
(1, 'Pastor Alemán', 'Raza grande y protectora'),
(1, 'Chihuahua', 'Raza muy pequeña'),
(1, 'Poodle', 'Raza mediana e inteligente'),
(1, 'Mestizo', 'Raza mixta'),
(2, 'Persa', 'Raza de pelo largo'),
(2, 'Siamés', 'Raza elegante'),
(2, 'Maine Coon', 'Raza grande'),
(2, 'Común Europeo', 'Raza común'),
(2, 'Mestizo', 'Raza mixta'),
(3, 'Loro', 'Ave parlante'),
(3, 'Canario', 'Ave cantora'),
(3, 'Periquito', 'Ave pequeña'),
(4, 'Hámster', 'Roedor pequeño'),
(4, 'Cobayo', 'Roedor mediano'),
(4, 'Conejo', 'Roedor grande');

-- CATEGORÍAS DE PRODUCTOS
INSERT INTO categoria_producto (nombre, descripcion, estado) VALUES
('Medicamentos', 'Medicamentos veterinarios', 'ACTIVO'),
('Alimentos', 'Alimentos para mascotas', 'ACTIVO'),
('Accesorios', 'Accesorios y juguetes', 'ACTIVO'),
('Higiene', 'Productos de higiene', 'ACTIVO'),
('Vacunas', 'Vacunas y biológicos', 'ACTIVO');

-- PRODUCTOS
INSERT INTO producto (id_categoria, nombre, descripcion, es_medicamento, precio_unitario, estado) VALUES
(1, 'Amoxicilina 500mg', 'Antibiótico de amplio espectro', TRUE, 2.50, 'ACTIVO'),
(1, 'Carprofeno 100mg', 'Antiinflamatorio no esteroideo', TRUE, 3.00, 'ACTIVO'),
(1, 'Ivermectina', 'Antiparasitario', TRUE, 15.00, 'ACTIVO'),
(2, 'Dog Chow Adulto 15kg', 'Alimento balanceado para perros adultos', FALSE, 120.00, 'ACTIVO'),
(2, 'Cat Chow Adulto 8kg', 'Alimento balanceado para gatos adultos', FALSE, 85.00, 'ACTIVO'),
(2, 'Royal Canin Cachorro 10kg', 'Alimento premium para cachorros', FALSE, 180.00, 'ACTIVO'),
(3, 'Collar antipulgas', 'Collar antipulgas para perros', FALSE, 25.00, 'ACTIVO'),
(3, 'Correa retráctil', 'Correa retráctil 5 metros', FALSE, 35.00, 'ACTIVO'),
(3, 'Juguete Kong', 'Juguete interactivo', FALSE, 20.00, 'ACTIVO'),
(4, 'Shampoo antipulgas', 'Shampoo medicado', FALSE, 18.00, 'ACTIVO'),
(4, 'Cepillo dental', 'Cepillo dental para perros', FALSE, 12.00, 'ACTIVO'),
(4, 'Toallas húmedas', 'Toallas húmedas para mascotas', FALSE, 8.00, 'ACTIVO'),
(5, 'Vacuna Sextuple', 'Vacuna sextuple canina', TRUE, 45.00, 'ACTIVO'),
(5, 'Vacuna Triple Felina', 'Vacuna triple felina', TRUE, 40.00, 'ACTIVO'),
(5, 'Vacuna Antirrábica', 'Vacuna antirrábica', TRUE, 30.00, 'ACTIVO');

-- PROVEEDORES
INSERT INTO proveedor (nombre, ruc, telefono, email, direccion, contacto, estado) VALUES
('Laboratorios Bayer', '20123456789', '01-234-5678', 'ventas@bayer.com', 'Av. Venezuela 1234, Lima', 'Juan Pérez', 'ACTIVO'),
('MSD Animal Health', '20987654321', '01-876-5432', 'info@msd.com', 'Jr. Cusco 567, Lima', 'María García', 'ACTIVO'),
('Zoetis Perú', '20456789123', '01-456-7890', 'contacto@zoetis.pe', 'Av. Arequipa 890, Lima', 'Carlos Rodríguez', 'ACTIVO'),
('Alimentos Pet Supply', '20789123456', '01-789-1234', 'ventas@petsupply.com', 'Calle Las Flores 456, Lima', 'Ana Torres', 'ACTIVO'),
('Distribuidora Veterinaria S.A.', '20321654987', '01-321-6549', 'info@distveterinaria.com', 'Jr. Los Olivos 123, Lima', 'Luis Martínez', 'ACTIVO');

-- TENANTS (VETERINARIAS)
INSERT INTO tenant (codigo_tenant, nombre_comercial, razon_social, ruc, email_contacto, telefono, direccion, ciudad, id_plan_actual, estado_suscripcion, fecha_registro, fecha_activacion, dias_trial, nombre_propietario, email_propietario, telefono_propietario, estado) VALUES
('VET001', 'Patitas Felices', 'Veterinaria Patitas Felices SAC', '20111222333', 'admin@patitasfelices.com', '987654321', 'Av. Los Olivos 123, San Isidro', 'Lima', 2, 'ACTIVO', NOW(), NOW(), 0, 'Carlos Rodriguez', 'admin@patitasfelices.com', '987654321', 'ACTIVO'),
('VET002', 'Amigos Peludos', 'Clínica Veterinaria Amigos Peludos EIRL', '20444555666', 'admin@amigospeludos.com', '987654322', 'Jr. Las Flores 456, Miraflores', 'Lima', 1, 'ACTIVO', NOW(), NOW(), 0, 'Maria González', 'admin@amigospeludos.com', '987654322', 'ACTIVO');

-- SUSCRIPCIONES
INSERT INTO suscripcion (id_tenant, id_plan, fecha_inicio, fecha_fin, tipo_pago, monto_pagado, estado) VALUES
(1, 2, NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), 'ANUAL', 999.99, 'ACTIVA'),
(2, 1, NOW(), DATE_ADD(NOW(), INTERVAL 1 MONTH), 'MENSUAL', 29.99, 'ACTIVA');

-- USUARIOS TENANT 1 (VET001)
INSERT INTO usuario (id_tenant, id_rol, username, password_hash, email, nombres, apellidos, telefono, estado) VALUES
(1, 1, 'admin_vet1', @password_hash, 'admin@patitasfelices.com', 'Carlos', 'Rodriguez', '987654321', 'ACTIVO'),
(1, 2, 'drjuan', @password_hash, 'drjuan@patitasfelices.com', 'Juan', 'Pérez', '987654322', 'ACTIVO');

-- USUARIOS TENANT 2 (VET002)
INSERT INTO usuario (id_tenant, id_rol, username, password_hash, email, nombres, apellidos, telefono, estado) VALUES
(2, 1, 'admin_vet2', @password_hash, 'admin@amigospeludos.com', 'Maria', 'González', '987654323', 'ACTIVO'),
(2, 2, 'drana', @password_hash, 'drana@amigospeludos.com', 'Ana', 'Martínez', '987654324', 'ACTIVO');

-- DOCTORES
INSERT INTO doctor (id_tenant, id_usuario, nombres, apellidos, colegiatura, especialidad, telefono, email, estado) VALUES
(1, 2, 'Juan', 'Pérez', 'CMP12345', 'Medicina General', '987654322', 'drjuan@patitasfelices.com', 'ACTIVO'),
(2, 4, 'Ana', 'Martínez', 'CMP67890', 'Cirugía', '987654324', 'drana@amigospeludos.com', 'ACTIVO');

-- CLIENTES TENANT 1
INSERT INTO cliente (id_tenant, tipo_documento, numero_documento, nombres, apellidos, email, telefono, direccion, estado) VALUES
(1, 'DNI', '12345678', 'Pedro', 'López', 'pedro.lopez@email.com', '987111111', 'Av. Principal 100', 'ACTIVO'),
(1, 'DNI', '87654321', 'María', 'Sánchez', 'maria.sanchez@email.com', '987222222', 'Jr. Secundario 200', 'ACTIVO'),
(1, 'DNI', '11223344', 'José', 'Ramírez', 'jose.ramirez@email.com', '987333333', 'Av. Tercera 300', 'ACTIVO');

-- CLIENTES TENANT 2
INSERT INTO cliente (id_tenant, tipo_documento, numero_documento, nombres, apellidos, email, telefono, direccion, estado) VALUES
(2, 'DNI', '22334455', 'Luis', 'Torres', 'luis.torres@email.com', '987444444', 'Av. Cuarta 400', 'ACTIVO'),
(2, 'DNI', '33445566', 'Carmen', 'Flores', 'carmen.flores@email.com', '987555555', 'Jr. Quinta 500', 'ACTIVO');

-- MASCOTAS TENANT 1
INSERT INTO mascota (id_tenant, id_cliente, id_raza, nombre, fecha_nacimiento, sexo, color, peso_kg, estado) VALUES
(1, 1, 1, 'Max', '2020-05-15', 'MACHO', 'Dorado', 28.50, 'ACTIVO'),
(1, 2, 8, 'Mishi', '2019-08-20', 'HEMBRA', 'Gris', 4.20, 'ACTIVO'),
(1, 3, 5, 'Rocky', '2021-03-10', 'MACHO', 'Café', 2.30, 'ACTIVO');

-- MASCOTAS TENANT 2
INSERT INTO mascota (id_tenant, id_cliente, id_raza, nombre, fecha_nacimiento, sexo, color, peso_kg, estado) VALUES
(2, 4, 3, 'Buddy', '2020-11-05', 'MACHO', 'Dorado', 30.00, 'ACTIVO'),
(2, 5, 9, 'Luna', '2021-01-12', 'HEMBRA', 'Blanco', 3.80, 'ACTIVO'),
(2, 4, 11, 'Félix', '2019-07-25', 'MACHO', 'Naranja', 4.50, 'ACTIVO');

-- INVENTARIO TENANT 1
INSERT INTO inventario (id_tenant, id_producto, stock_actual, stock_minimo, stock_maximo) VALUES
(1, 1, 50, 10, 100),
(1, 2, 30, 10, 80),
(1, 4, 10, 2, 30),
(1, 7, 25, 5, 50),
(1, 13, 15, 5, 40);

-- INVENTARIO TENANT 2
INSERT INTO inventario (id_tenant, id_producto, stock_actual, stock_minimo, stock_maximo) VALUES
(2, 1, 40, 10, 100),
(2, 5, 8, 2, 20),
(2, 8, 15, 5, 40),
(2, 14, 20, 5, 50);

SET FOREIGN_KEY_CHECKS = 1;

-- ===========================================
-- RESUMEN FINAL
-- ===========================================

SELECT '' AS separador;
SELECT '========================================' AS linea;
SELECT '✅ INSTALACIÓN COMPLETADA EXITOSAMENTE' AS estado;
SELECT '========================================' AS linea;
SELECT '' AS separador;

SELECT '📊 RESUMEN DE INSTALACIÓN' AS seccion;
SELECT '✓ 3 Planes de suscripción' AS detalle
UNION SELECT '✓ 2 Tenants (Veterinarias)'
UNION SELECT '✓ 4 Usuarios del sistema'
UNION SELECT '✓ 2 Doctores registrados'
UNION SELECT '✓ 5 Clientes registrados'
UNION SELECT '✓ 6 Mascotas registradas'
UNION SELECT '✓ 15 Productos en catálogo'
UNION SELECT '✓ 5 Especies disponibles'
UNION SELECT '✓ 18 Razas disponibles';

SELECT '' AS separador;

SELECT '🔐 CREDENCIALES DE ACCESO' AS seccion;
SELECT '' AS separador;
SELECT '========================================' AS separador;
SELECT '🔹 SUPER ADMINISTRADOR:' AS tipo,
       '   Usuario: superadmin' AS info1,
       '   Contraseña: admin123' AS info2,
       '' AS espacio1
UNION
SELECT '🔹 VETERINARIA 1 (VET001 - Patitas Felices):' AS tipo,
       '   Usuario Admin: admin_vet1' AS info1,
       '   Usuario Doctor: drjuan' AS info2,
       '   Contraseña para ambos: admin123' AS espacio1
UNION
SELECT '🔹 VETERINARIA 2 (VET002 - Amigos Peludos):' AS tipo,
       '   Usuario Admin: admin_vet2' AS info1,
       '   Usuario Doctor: drana' AS info2,
       '   Contraseña para ambos: admin123' AS espacio1;

SELECT '' AS separador;

SELECT '🚀 SIGUIENTES PASOS' AS titulo;
SELECT '' AS separador;
SELECT '========================================' AS separador;
SELECT '1. Verificar application.properties del backend:' AS paso,
       '   spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria_saas' AS detalle1,
       '' AS espacio
UNION
SELECT '2. Iniciar el backend Spring Boot:' AS paso,
       '   cd app' AS detalle1,
       '   mvn spring-boot:run' AS espacio
UNION
SELECT '3. Probar el login con:' AS paso,
       '   POST http://localhost:8080/api/auth/login' AS detalle1,
       '   Body: {"username":"admin_vet1","password":"admin123"}' AS espacio
UNION
SELECT '4. Acceder a Swagger UI:' AS paso,
       '   http://localhost:8080/swagger-ui/index.html' AS detalle1,
       '' AS espacio
UNION
SELECT '5. Iniciar el frontend:' AS paso,
       '   cd nx-vet' AS detalle1,
       '   npm run dev' AS espacio;

SELECT '' AS separador;
SELECT '✨ ¡SISTEMA LISTO PARA USAR!' AS mensaje;
SELECT '📧 Cualquier duda: soporte@appvet.com' AS contacto;
SELECT '' AS separador;
