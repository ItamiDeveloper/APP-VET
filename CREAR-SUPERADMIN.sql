-- ============================================
-- CREAR USUARIO SUPERADMIN
-- ============================================
-- Este script crea el usuario SuperAdmin del sistema
-- que puede aprobar solicitudes de nuevas veterinarias
-- ============================================

USE veterinaria_saas;

-- Primero asegurarnos que existe el rol SUPER_ADMIN
INSERT IGNORE INTO rol (nombre, descripcion) VALUES
('SUPER_ADMIN', 'Super Administrador del sistema - gestiona todos los tenants');

-- Crear usuario SuperAdmin
-- Password: admin123 (hash BCrypt)
INSERT INTO usuario (
    id_tenant, 
    id_rol, 
    username, 
    password_hash, 
    email, 
    nombres, 
    apellidos, 
    telefono,
    estado
) VALUES (
    NULL,  -- SuperAdmin no pertenece a ningún tenant
    (SELECT id_rol FROM rol WHERE nombre = 'SUPER_ADMIN'),
    'superadmin',
    '$2a$10$oLJHyLYLScCRvLm/vZSfMuazNV9vq3Ry/VmKLIHpkk1pYeQEksHwy',  -- admin123
    'super@admin.com',
    'Super',
    'Admin',
    '999-9999',
    'ACTIVO'
) ON DUPLICATE KEY UPDATE username=username;

-- Verificar que se creó
SELECT 
    u.id_usuario,
    u.username,
    r.nombre as rol,
    u.email,
    u.estado,
    'Password: admin123' as nota
FROM usuario u
JOIN rol r ON u.id_rol = r.id_rol
WHERE u.username = 'superadmin';

SELECT '✓ Usuario SuperAdmin creado exitosamente' AS Resultado;
SELECT '  Username: superadmin' AS Credenciales;
SELECT '  Password: admin123' AS Credenciales;
SELECT '  Ahora puedes aprobar solicitudes de veterinarias' AS Info;
