-- ========================================
-- ACTUALIZAR CONTRASEÑAS CON HASH CORRECTO
-- ========================================
-- Este script actualiza los usuarios con el hash BCrypt correcto
-- que funciona con Spring Security
-- ========================================

USE veterinaria_saas;

-- Hash BCrypt correcto para "admin123"
-- Este es el hash que el sistema PasswordVerifier usa y valida
SET @correct_hash = '$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe';

-- Actualizar todos los usuarios con la contraseña correcta
UPDATE usuario 
SET password_hash = @correct_hash
WHERE username IN ('admin', 'drjuan', 'drana');

-- Actualizar super admin
UPDATE super_admin
SET password_hash = @correct_hash
WHERE username = 'superadmin';

-- Verificar actualización
SELECT 'USUARIOS ACTUALIZADOS:' AS resultado;
SELECT 
    u.id_usuario,
    u.username,
    u.nombres,
    u.apellidos,
    t.codigo_tenant AS tenant,
    LENGTH(u.password_hash) AS hash_length
FROM usuario u
INNER JOIN tenant t ON u.id_tenant = t.id_tenant
ORDER BY u.id_usuario;

SELECT '' AS separador;
SELECT 'SUPER ADMIN ACTUALIZADO:' AS resultado;
SELECT 
    id_super_admin,
    username,
    nombres,
    apellidos,
    LENGTH(password_hash) AS hash_length
FROM super_admin;

SELECT '' AS separador;
SELECT '✅ Contraseñas actualizadas correctamente' AS mensaje;
SELECT 'Credenciales: admin / admin123' AS info;
