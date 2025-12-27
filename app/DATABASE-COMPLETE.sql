-- =======================================================
-- 🏥 VETERINARIA SAAS - INSTALACIÓN COMPLETA
-- Sistema Multi-Tenant para Gestión Veterinaria
-- =======================================================
-- Versión: 2.0 Final
-- Fecha: 27 Diciembre 2025
-- Autor: Eduardo - APP-VET
-- =======================================================
--
-- 📋 CONTENIDO DE ESTE SCRIPT:
--   1. Eliminación y creación de base de datos
--   2. 28 tablas del sistema multi-tenant
--   3. Datos iniciales (planes, roles, especies, razas, categorías)
--   4. Tenant de demostración
--   5. Usuarios de prueba con passwords correctos
--
-- 🔐 CREDENCIALES PREDETERMINADAS:
--   Super Admin:  superadmin / admin123
--   Tenant Admin: admin / admin123
--
-- ⚙️ INSTRUCCIONES:
--   1. Ejecuta este script en MySQL Server 8.0+
--   2. Espera a que termine (puede tomar 10-30 segundos)
--   3. Verifica el mensaje de éxito al final
--   4. Inicia tu servidor Spring Boot
--   5. Prueba en: http://localhost:8080/swagger-ui.html
--
-- =======================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS veterinaria_saas;

CREATE DATABASE veterinaria_saas
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE veterinaria_saas;

-- ===========================================
-- 📊 NIVEL 1: TABLAS GLOBALES DEL SAAS
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
) ENGINE=InnoDB COMMENT='Planes de suscripción disponibles';

-- Super Administradores (gestión global del SaaS)
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
  INDEX idx_email (email),
  INDEX idx_estado (estado)
) ENGINE=InnoDB COMMENT='Administradores del sistema SaaS';

-- Tenants (veterinarias clientes)
CREATE TABLE tenant (
  id_tenant                   INT AUTO_INCREMENT PRIMARY KEY,
  codigo_tenant               VARCHAR(50) NOT NULL UNIQUE COMMENT 'Código único para identificación',
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
  
  -- Personalización
  logo_url                    VARCHAR(255),
  color_primario              VARCHAR(7) DEFAULT '#3B82F6',
  color_secundario            VARCHAR(7) DEFAULT '#10B981',
  
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_actualizacion         DATETIME ON UPDATE CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_tenant_plan
    FOREIGN KEY (id_plan_actual) REFERENCES plan(id_plan),
  INDEX idx_codigo_tenant (codigo_tenant),
  INDEX idx_email_propietario (email_propietario),
  INDEX idx_estado_suscripcion (estado_suscripcion),
  INDEX idx_estado (estado)
) ENGINE=InnoDB COMMENT='Veterinarias (tenants del sistema)';

-- Historial de suscripciones
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
) ENGINE=InnoDB COMMENT='Historial de suscripciones';

-- Pagos
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
  INDEX idx_estado (estado),
  INDEX idx_referencia (referencia_pago)
) ENGINE=InnoDB COMMENT='Registro de pagos';

-- ===========================================
-- 📚 NIVEL 2: CATÁLOGOS COMPARTIDOS
-- ===========================================

-- Roles del sistema
CREATE TABLE rol (
  id_rol                      INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(50) NOT NULL UNIQUE,
  descripcion                 VARCHAR(255),
  nivel_acceso                INT NOT NULL DEFAULT 1 COMMENT '1=Admin, 2=Veterinario, 3=Recepcionista, 4=Asistente',
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB COMMENT='Roles de usuario';

-- Especies de animales
CREATE TABLE especie (
  id_especie                  INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL,
  nombre_cientifico           VARCHAR(150),
  descripcion                 VARCHAR(255),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB COMMENT='Catálogo de especies';

-- Razas por especie
CREATE TABLE raza (
  id_raza                     INT AUTO_INCREMENT PRIMARY KEY,
  id_especie                  INT NOT NULL,
  nombre                      VARCHAR(100) NOT NULL,
  descripcion                 VARCHAR(255),
  
  CONSTRAINT fk_raza_especie
    FOREIGN KEY (id_especie) REFERENCES especie(id_especie),
  INDEX idx_especie (id_especie),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB COMMENT='Catálogo de razas';

-- Categorías de productos
CREATE TABLE categoria_producto (
  id_categoria                INT AUTO_INCREMENT PRIMARY KEY,
  nombre                      VARCHAR(100) NOT NULL,
  descripcion                 VARCHAR(255),
  estado                      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  INDEX idx_nombre (nombre),
  INDEX idx_estado (estado)
) ENGINE=InnoDB COMMENT='Categorías de productos';

-- ===========================================
-- 👥 NIVEL 3: USUARIOS Y SEGURIDAD
-- ===========================================

-- Usuarios por tenant
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
  INDEX idx_tenant_estado (id_tenant, estado),
  INDEX idx_username (username)
) ENGINE=InnoDB COMMENT='Usuarios por tenant';

-- Tokens de refresco
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
) ENGINE=InnoDB COMMENT='Tokens JWT de refresco';

-- ===========================================
-- 🏥 NIVEL 4: MÓDULO CLÍNICO
-- ===========================================

-- Doctores/Veterinarios
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
  INDEX idx_tenant_estado (id_tenant, estado),
  INDEX idx_colegiatura (colegiatura)
) ENGINE=InnoDB COMMENT='Veterinarios';

-- Clientes (dueños de mascotas)
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
  INDEX idx_documento (numero_documento),
  INDEX idx_nombre (nombres, apellidos)
) ENGINE=InnoDB COMMENT='Clientes (dueños de mascotas)';

-- Mascotas/Pacientes
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
  INDEX idx_estado (estado),
  INDEX idx_nombre (nombre),
  INDEX idx_microchip (microchip)
) ENGINE=InnoDB COMMENT='Mascotas (pacientes)';

-- Citas/Agendamiento
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
  INDEX idx_estado (estado),
  INDEX idx_mascota (id_mascota)
) ENGINE=InnoDB COMMENT='Citas médicas';

-- Historia Clínica
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
) ENGINE=InnoDB COMMENT='Historias clínicas';

-- Recetas médicas
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
  INDEX idx_tenant_fecha (id_tenant, fecha_emision),
  INDEX idx_estado (estado)
) ENGINE=InnoDB COMMENT='Recetas médicas';

-- Archivos adjuntos (estudios, imágenes)
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
  INDEX idx_tenant_mascota (id_tenant, id_mascota),
  INDEX idx_fecha (fecha_subida)
) ENGINE=InnoDB COMMENT='Archivos adjuntos (estudios, radiografías, etc.)';

-- ===========================================
-- 💊 NIVEL 5: MÓDULO INVENTARIO Y FARMACIA
-- ===========================================

-- Productos
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
  INDEX idx_categoria (id_categoria),
  INDEX idx_estado (estado),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB COMMENT='Catálogo de productos y medicamentos';

-- Inventario por tenant
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
) ENGINE=InnoDB COMMENT='Control de inventario';

-- Detalle de recetas (medicamentos prescritos)
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
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_receta (id_receta)
) ENGINE=InnoDB COMMENT='Detalle de medicamentos en recetas';

-- ===========================================
-- 💰 NIVEL 6: MÓDULO COMPRAS Y VENTAS
-- ===========================================

-- Proveedores
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
  INDEX idx_tenant_estado (id_tenant, estado),
  INDEX idx_nombre (nombre)
) ENGINE=InnoDB COMMENT='Proveedores';

-- Compras (entradas)
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
  INDEX idx_estado (estado),
  INDEX idx_numero_comprobante (numero_comprobante)
) ENGINE=InnoDB COMMENT='Compras a proveedores';

-- Detalle de compras
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
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_compra (id_compra)
) ENGINE=InnoDB COMMENT='Detalle de compras';

-- Ventas (salidas)
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
  INDEX idx_estado (estado),
  INDEX idx_numero_comprobante (numero_comprobante)
) ENGINE=InnoDB COMMENT='Ventas a clientes';

-- Detalle de ventas
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
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  INDEX idx_venta (id_venta)
) ENGINE=InnoDB COMMENT='Detalle de ventas';

-- ===========================================
-- 📢 NIVEL 7: NOTIFICACIONES Y AUDITORÍA
-- ===========================================

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
) ENGINE=InnoDB COMMENT='Notificaciones del sistema';

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
  INDEX idx_tabla_accion (tabla_afectada, accion),
  INDEX idx_fecha (fecha_hora)
) ENGINE=InnoDB COMMENT='Registro de auditoría';

SET FOREIGN_KEY_CHECKS = 1;

-- ===========================================
-- 🌱 DATOS INICIALES
-- ===========================================

-- Super Admin del sistema (password: admin123)
INSERT INTO super_admin (username, password_hash, email, nombres, apellidos, telefono, estado) VALUES
('superadmin', '$2a$10$ZNtd9U6DaVwur5aJnaSXr.yHhVEGDdqjvABqPpOUiVPAPuZpAkgFu', 'admin@appvet.com', 'Admin', 'Sistema', '999999999', 'ACTIVO');

-- Roles predefinidos
INSERT INTO rol (nombre, descripcion, nivel_acceso) VALUES
('ROLE_ADMIN', 'Administrador de Veterinaria con acceso completo', 1),
('ROLE_VETERINARIO', 'Veterinario con acceso clínico', 2),
('ROLE_RECEPCIONISTA', 'Recepcionista con acceso a citas y clientes', 3),
('ROLE_ASISTENTE', 'Asistente veterinario con acceso limitado', 4);

-- Planes de suscripción
INSERT INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_doctores, max_mascotas, max_almacenamiento_mb, tiene_reportes_avanzados, tiene_api_acceso, tiene_soporte_prioritario, orden_visualizacion, estado) VALUES
('Básico', 'Ideal para veterinarias pequeñas que están comenzando. Incluye funcionalidades básicas de gestión clínica, citas y clientes.', 49.99, 499.99, 3, 2, 50, 512, FALSE, FALSE, FALSE, 1, 'ACTIVO'),
('Profesional', 'Perfecto para veterinarias en crecimiento. Incluye reportes avanzados, más usuarios y mayor capacidad de almacenamiento.', 99.99, 999.99, 10, 5, 200, 2048, TRUE, FALSE, FALSE, 2, 'ACTIVO'),
('Empresarial', 'Para grandes clínicas y hospitales veterinarios. Acceso API, soporte prioritario y capacidad ampliada.', 199.99, 1999.99, 30, 15, 1000, 10240, TRUE, TRUE, TRUE, 3, 'ACTIVO'),
('Enterprise', 'Solución enterprise sin límites. Personalización completa, integración avanzada y soporte dedicado 24/7.', 399.99, 3999.99, 100, 50, 5000, 51200, TRUE, TRUE, TRUE, 4, 'ACTIVO');

-- Especies comunes
INSERT INTO especie (nombre, nombre_cientifico, descripcion) VALUES
('Perro', 'Canis familiaris', 'Mamífero carnívoro doméstico'),
('Gato', 'Felis catus', 'Mamífero carnívoro doméstico'),
('Ave', 'Aves', 'Clase de vertebrados con plumas'),
('Conejo', 'Oryctolagus cuniculus', 'Mamífero lagomorfo'),
('Reptil', 'Reptilia', 'Clase de vertebrados con escamas'),
('Roedor', 'Rodentia', 'Orden de mamíferos con incisivos'),
('Pez', 'Pisces', 'Vertebrados acuáticos'),
('Tortuga', 'Testudines', 'Reptiles con caparazón');

-- Razas populares
INSERT INTO raza (id_especie, nombre, descripcion) VALUES
-- Perros
(1, 'Labrador Retriever', 'Raza grande, amigable, activa y familiar'),
(1, 'Pastor Alemán', 'Raza grande, inteligente, protectora y leal'),
(1, 'Golden Retriever', 'Raza grande, cariñosa, inteligente y paciente'),
(1, 'Bulldog', 'Raza mediana, tranquila, leal y resistente'),
(1, 'Chihuahua', 'Raza pequeña, alerta, vivaz y valiente'),
(1, 'Beagle', 'Raza mediana, amigable y enérgica'),
(1, 'Poodle', 'Raza inteligente, hipoalergénica'),
(1, 'Mestizo', 'Raza mixta o sin raza definida'),
-- Gatos
(2, 'Persa', 'Gato de pelo largo y rostro plano'),
(2, 'Siamés', 'Gato esbelto con marcas distintivas'),
(2, 'Maine Coon', 'Gato de gran tamaño y pelo largo'),
(2, 'Británico de pelo corto', 'Gato robusto, tranquilo y cariñoso'),
(2, 'Mestizo', 'Gato sin raza definida'),
(2, 'Bengalí', 'Gato con patrón manchado o marmolado'),
-- Aves
(3, 'Canario', 'Ave pequeña canora'),
(3, 'Loro', 'Ave inteligente y sociable'),
(3, 'Periquito', 'Pequeño loro australiano'),
-- Conejos
(4, 'Conejo Enano', 'Raza pequeña de compañía'),
(4, 'Conejo Gigante', 'Raza grande, tranquila');

-- Categorías de productos
INSERT INTO categoria_producto (nombre, descripcion, estado) VALUES
('Medicamentos', 'Productos farmacéuticos veterinarios (antibióticos, antiinflamatorios, etc.)', 'ACTIVO'),
('Vacunas', 'Vacunas y biológicos para prevención', 'ACTIVO'),
('Alimentos', 'Alimentos balanceados para mascotas', 'ACTIVO'),
('Accesorios', 'Accesorios, juguetes y productos de confort', 'ACTIVO'),
('Higiene', 'Productos de higiene, limpieza y cuidado', 'ACTIVO'),
('Suplementos', 'Suplementos nutricionales y vitaminas', 'ACTIVO'),
('Antiparasitarios', 'Productos contra parásitos internos y externos', 'ACTIVO'),
('Material Quirúrgico', 'Instrumental y material para cirugía', 'ACTIVO');

-- ===========================================
-- 🎭 TENANT DE DEMOSTRACIÓN
-- ===========================================

-- Veterinaria Demo para pruebas
INSERT INTO tenant (
  codigo_tenant, nombre_comercial, razon_social, ruc, telefono, 
  email_contacto, direccion, ciudad, id_plan_actual, estado_suscripcion,
  nombre_propietario, email_propietario, telefono_propietario, 
  dias_trial, estado, fecha_activacion
) VALUES (
  'vet-demo', 
  'Veterinaria Demo', 
  'Veterinaria Demo SAC', 
  '20123456789', 
  '987654321',
  'contacto@vetdemo.com', 
  'Av. Principal 123, Miraflores', 
  'Lima', 
  2, -- Plan Profesional
  'ACTIVO',
  'Dr. Juan Pérez', 
  'juan@vetdemo.com', 
  '987654321',
  30,
  'ACTIVO',
  CURRENT_TIMESTAMP
);

-- Suscripción activa para el tenant demo
INSERT INTO suscripcion (id_tenant, id_plan, fecha_inicio, fecha_fin, metodo_pago, monto_pagado, estado) VALUES
(1, 2, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'TARJETA', 99.99, 'ACTIVO');

-- Usuario administrador del tenant demo (password: admin123)
INSERT INTO usuario (id_tenant, id_rol, username, password_hash, email, nombres, apellidos, telefono, estado) VALUES
(1, 1, 'admin', '$2a$10$ZNtd9U6DaVwur5aJnaSXr.yHhVEGDdqjvABqPpOUiVPAPuZpAkgFu', 'admin@vetdemo.com', 'Juan', 'Pérez García', '987654321', 'ACTIVO');

-- Doctor vinculado al usuario admin
INSERT INTO doctor (id_tenant, id_usuario, nombres, apellidos, colegiatura, especialidad, estado) VALUES
(1, 1, 'Juan', 'Pérez García', 'CMP-12345', 'Medicina General Veterinaria', 'ACTIVO');

-- Cliente demo
INSERT INTO cliente (id_tenant, nombres, apellidos, tipo_documento, numero_documento, telefono, email, direccion, estado) VALUES
(1, 'María', 'González López', 'DNI', '12345678', '999888777', 'maria@email.com', 'Jr. Los Pinos 456', 'ACTIVO');

-- Mascota demo
INSERT INTO mascota (id_tenant, id_cliente, id_raza, nombre, sexo, fecha_nacimiento, color, peso_kg, estado) VALUES
(1, 1, 1, 'Max', 'MACHO', '2020-05-15', 'Dorado', 28.5, 'ACTIVO');

-- Proveedor demo
INSERT INTO proveedor (id_tenant, nombre, ruc, telefono, email, direccion, contacto_nombre, estado) VALUES
(1, 'Distribuidora Veterinaria SAC', '20987654321', '987123456', 'ventas@distveterinaria.com', 'Jr. Comercio 456, Lima', 'Carlos Ruiz', 'ACTIVO');

-- Producto demo
INSERT INTO producto (id_tenant, id_categoria, codigo, nombre, descripcion, es_medicamento, precio_compra, precio_venta, unidad_medida, estado) VALUES
(1, 1, 'MED001', 'Antibiótico Amoxicilina 500mg', 'Antibiótico de amplio espectro para tratamiento de infecciones bacterianas', TRUE, 18.00, 25.50, 'UND', 'ACTIVO'),
(1, 7, 'PAR001', 'Antiparasitario Interno', 'Desparasitante oral para perros y gatos', TRUE, 12.00, 18.00, 'UND', 'ACTIVO'),
(1, 3, 'ALI001', 'Alimento Premium Adulto 15kg', 'Alimento balanceado para perros adultos de razas grandes', FALSE, 95.00, 120.00, 'UND', 'ACTIVO');

-- Inventario demo
INSERT INTO inventario (id_tenant, id_producto, stock_actual, stock_minimo, stock_maximo) VALUES
(1, 1, 50, 10, 100),
(1, 2, 30, 5, 50),
(1, 3, 15, 3, 30);

COMMIT;

-- ===========================================
-- ✅ VERIFICACIÓN DE INSTALACIÓN
-- ===========================================

SELECT '✅ ¡BASE DE DATOS CREADA EXITOSAMENTE!' AS estado;
SELECT '' AS separador;

SELECT '📊 RESUMEN DE INSTALACIÓN' AS seccion;
SELECT COUNT(*) AS total_tablas FROM information_schema.tables WHERE table_schema = 'veterinaria_saas';
SELECT '' AS separador;

SELECT '🔐 CREDENCIALES CREADAS' AS seccion;
SELECT 
  'Super Admin' AS tipo,
  username AS usuario,
  'admin123' AS contraseña,
  email AS correo,
  CONCAT(nombres, ' ', apellidos) AS nombre_completo,
  estado AS estado
FROM super_admin
UNION ALL
SELECT 
  'Admin Tenant Demo' AS tipo,
  u.username AS usuario,
  'admin123' AS contraseña,
  u.email AS correo,
  CONCAT(u.nombres, ' ', u.apellidos) AS nombre_completo,
  u.estado AS estado
FROM usuario u
WHERE u.id_tenant = 1 AND u.id_rol = 1;
SELECT '' AS separador;

SELECT '📦 PLANES DISPONIBLES' AS seccion;
SELECT 
  nombre,
  CONCAT('S/', precio_mensual) AS precio_mes,
  CONCAT('S/', precio_anual) AS precio_año,
  max_usuarios AS usuarios,
  max_doctores AS doctores,
  max_mascotas AS mascotas,
  estado
FROM plan
ORDER BY orden_visualizacion;
SELECT '' AS separador;

SELECT '🏥 TENANT DE DEMOSTRACIÓN' AS seccion;
SELECT 
  codigo_tenant AS codigo,
  nombre_comercial AS nombre,
  estado_suscripcion AS suscripcion,
  (SELECT nombre FROM plan WHERE id_plan = t.id_plan_actual) AS plan,
  estado
FROM tenant t;
SELECT '' AS separador;

SELECT '📋 CATÁLOGOS INICIALES' AS seccion;
SELECT 'Especies' AS catalogo, COUNT(*) AS cantidad FROM especie
UNION ALL
SELECT 'Razas' AS catalogo, COUNT(*) AS cantidad FROM raza
UNION ALL
SELECT 'Roles' AS catalogo, COUNT(*) AS cantidad FROM rol
UNION ALL
SELECT 'Categorías de Productos' AS catalogo, COUNT(*) AS cantidad FROM categoria_producto;
SELECT '' AS separador;

SELECT '🚀 PRÓXIMOS PASOS' AS seccion;
SELECT '1. Inicia tu servidor Spring Boot' AS instruccion
UNION ALL SELECT '2. Accede a: http://localhost:8080/swagger-ui.html'
UNION ALL SELECT '3. Prueba el login de Super Admin'
UNION ALL SELECT '4. Prueba el login de Admin del Tenant Demo'
UNION ALL SELECT '5. Explora los 14 endpoints disponibles';
SELECT '' AS separador;

SELECT '✨ ¡TODO LISTO PARA COMENZAR!' AS mensaje;
