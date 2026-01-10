-- ============================================
-- INICIALIZAR DATOS BÁSICOS PARA SISTEMA VETERINARIA SAAS
-- ============================================
-- Este script crea los datos mínimos necesarios para:
-- 1. Registro de nuevas veterinarias (landing page)
-- 2. Roles de usuario
-- 3. Planes de suscripción
-- ============================================

USE veterinaria_saas;

-- ============================================
-- 1. CREAR ROLES SI NO EXISTEN
-- ============================================

-- Verificar y crear rol SUPER_ADMIN
INSERT INTO rol (nombre, descripcion)
SELECT 'SUPER_ADMIN', 'Super Administrador del sistema - gestiona todos los tenants'
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'SUPER_ADMIN');

-- Verificar y crear rol ADMIN
INSERT INTO rol (nombre, descripcion)
SELECT 'ADMIN', 'Administrador del tenant - gestión completa de la veterinaria'
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'ADMIN');

-- Verificar y crear rol VETERINARIO
INSERT INTO rol (nombre, descripcion)
SELECT 'VETERINARIO', 'Veterinario - acceso a historias clínicas, citas y tratamientos'
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'VETERINARIO');

-- Verificar y crear rol RECEPCIONISTA
INSERT INTO rol (nombre, descripcion)
SELECT 'RECEPCIONISTA', 'Recepcionista - gestión de citas, clientes y ventas básicas'
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'RECEPCIONISTA');

-- ============================================
-- 2. CREAR PLANES DE SUSCRIPCIÓN
-- ============================================

-- Plan GRATIS (Trial)
INSERT INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_clientes, max_mascotas, max_historias, estado)
SELECT 
    'GRATIS',
    'Plan de prueba gratuito por 30 días. Ideal para evaluar el sistema.',
    0.00,
    0.00,
    2,
    50,
    100,
    200,
    'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM plan WHERE nombre = 'GRATIS');

-- Plan BÁSICO
INSERT INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_clientes, max_mascotas, max_historias, estado)
SELECT 
    'BÁSICO',
    'Plan ideal para veterinarias pequeñas. Funcionalidades esenciales.',
    49.99,
    499.99,
    5,
    200,
    500,
    1000,
    'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM plan WHERE nombre = 'BÁSICO');

-- Plan PROFESIONAL
INSERT INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_clientes, max_mascotas, max_historias, estado)
SELECT 
    'PROFESIONAL',
    'Plan completo para veterinarias en crecimiento. Todas las funcionalidades.',
    99.99,
    999.99,
    15,
    1000,
    2500,
    5000,
    'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM plan WHERE nombre = 'PROFESIONAL');

-- Plan EMPRESARIAL
INSERT INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_clientes, max_mascotas, max_historias, estado)
SELECT 
    'EMPRESARIAL',
    'Plan sin límites para cadenas de veterinarias. Soporte prioritario.',
    199.99,
    1999.99,
    NULL,
    NULL,
    NULL,
    NULL,
    'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM plan WHERE nombre = 'EMPRESARIAL');

-- ============================================
-- 3. CREAR CATEGORÍAS DE PRODUCTOS BÁSICAS
-- ============================================

-- Verificar que existe la tabla categoria_producto
INSERT INTO categoria_producto (nombre, descripcion, estado)
SELECT 'Medicamentos', 'Medicamentos veterinarios', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM categoria_producto WHERE nombre = 'Medicamentos');

INSERT INTO categoria_producto (nombre, descripcion, estado)
SELECT 'Alimentos', 'Alimentos para mascotas', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM categoria_producto WHERE nombre = 'Alimentos');

INSERT INTO categoria_producto (nombre, descripcion, estado)
SELECT 'Accesorios', 'Accesorios y juguetes', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM categoria_producto WHERE nombre = 'Accesorios');

INSERT INTO categoria_producto (nombre, descripcion, estado)
SELECT 'Vacunas', 'Vacunas y sueros', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM categoria_producto WHERE nombre = 'Vacunas');

INSERT INTO categoria_producto (nombre, descripcion, estado)
SELECT 'Higiene', 'Productos de higiene y cuidado', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM categoria_producto WHERE nombre = 'Higiene');

-- ============================================
-- 4. VERIFICACIÓN DE DATOS CREADOS
-- ============================================

-- Mostrar roles creados
SELECT 'ROLES DISPONIBLES:' as Info;
SELECT id_rol, nombre, descripcion FROM rol ORDER BY id_rol;

-- Mostrar planes creados
SELECT 'PLANES DE SUSCRIPCIÓN:' as Info;
SELECT id_plan, nombre, precio_mensual, max_usuarios, max_clientes, estado FROM plan ORDER BY precio_mensual;

-- Mostrar categorías creadas
SELECT 'CATEGORÍAS DE PRODUCTOS:' as Info;
SELECT id_categoria, nombre, descripcion FROM categoria_producto WHERE estado = 'ACTIVO';

-- Verificar si existe tenant de ejemplo
SELECT 'TENANTS REGISTRADOS:' as Info;
SELECT id_tenant, codigo_tenant, nombre_comercial, estado, estado_suscripcion FROM tenant;

-- ============================================
-- RESULTADO ESPERADO
-- ============================================
-- Después de ejecutar este script:
-- ✓ 4 Roles creados (SUPER_ADMIN, ADMIN, VETERINARIO, RECEPCIONISTA)
-- ✓ 4 Planes creados (GRATIS, BÁSICO, PROFESIONAL, EMPRESARIAL)
-- ✓ 5 Categorías creadas (Medicamentos, Alimentos, Accesorios, Vacunas, Higiene)
-- ✓ Sistema listo para registro de nuevas veterinarias desde landing page
-- ============================================
