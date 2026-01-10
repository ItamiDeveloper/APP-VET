-- ============================================
-- INICIALIZAR DATOS BÁSICOS (VERSIÓN SIMPLIFICADA)
-- ============================================
-- INSTRUCCIONES:
-- 1. Abre MySQL Workbench
-- 2. Conecta a localhost:3306 con usuario root
-- 3. Selecciona la base de datos veterinaria_saas
-- 4. Copia y pega ESTE script completo
-- 5. Ejecuta todo (Ctrl+Shift+Enter)
-- ============================================

USE veterinaria_saas;

-- ROLES
INSERT IGNORE INTO rol (nombre, descripcion) VALUES
('SUPER_ADMIN', 'Super Administrador del sistema - gestiona todos los tenants'),
('ADMIN', 'Administrador del tenant - gestión completa de la veterinaria'),
('VETERINARIO', 'Veterinario - acceso a historias clínicas, citas y tratamientos'),
('RECEPCIONISTA', 'Recepcionista - gestión de citas, clientes y ventas básicas');

-- PLANES
INSERT IGNORE INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_clientes, max_mascotas, max_historias, estado) VALUES
('GRATIS', 'Plan de prueba gratuito por 30 días', 0.00, 0.00, 2, 50, 100, 200, 'ACTIVO'),
('BÁSICO', 'Plan ideal para veterinarias pequeñas', 49.99, 499.99, 5, 200, 500, 1000, 'ACTIVO'),
('PROFESIONAL', 'Plan completo para veterinarias en crecimiento', 99.99, 999.99, 15, 1000, 2500, 5000, 'ACTIVO'),
('EMPRESARIAL', 'Plan sin límites para cadenas de veterinarias', 199.99, 1999.99, NULL, NULL, NULL, NULL, 'ACTIVO');

-- CATEGORÍAS
INSERT IGNORE INTO categoria_producto (nombre, descripcion, estado) VALUES
('Medicamentos', 'Medicamentos veterinarios', 'ACTIVO'),
('Alimentos', 'Alimentos para mascotas', 'ACTIVO'),
('Accesorios', 'Accesorios y juguetes', 'ACTIVO'),
('Vacunas', 'Vacunas y sueros', 'ACTIVO'),
('Higiene', 'Productos de higiene y cuidado', 'ACTIVO');

-- VERIFICAR RESULTADOS
SELECT 'ROLES:' AS Tabla, COUNT(*) AS Total FROM rol
UNION ALL
SELECT 'PLANES:', COUNT(*) FROM plan
UNION ALL
SELECT 'CATEGORÍAS:', COUNT(*) FROM categoria_producto WHERE estado = 'ACTIVO';

SELECT '¡Script ejecutado exitosamente! Ahora puedes registrar veterinarias.' AS Resultado;
