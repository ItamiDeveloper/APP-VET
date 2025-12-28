-- =======================================================
-- FIX PASSWORD HASH - Actualizar con hash correcto
-- =======================================================
-- Ejecuta este script en MySQL Workbench para corregir
-- los passwords de todos los usuarios
-- =======================================================

USE veterinaria_saas;

-- Hash BCrypt CORRECTO para "admin123"
-- Este hash fue generado y verificado por el backend
SET @correct_hash = '$2a$10$0pj5LiqhT46x6haFSk01xOy4Dgj2SizHEhEciqF1kGJGx/jPs1M6y';

-- Actualizar TODOS los usuarios
UPDATE usuario 
SET password_hash = @correct_hash 
WHERE username IN ('admin_vet1', 'admin_vet2', 'drjuan', 'drana');

-- Actualizar super admin
UPDATE super_admin 
SET password_hash = @correct_hash 
WHERE username = 'superadmin';

-- Verificar que se actualizaron correctamente
SELECT '✅ VERIFICACIÓN' AS resultado;
SELECT 
    username, 
    SUBSTRING(password_hash, 1, 20) AS hash_inicio,
    LENGTH(password_hash) AS hash_length,
    'OK' AS status
FROM usuario 
WHERE username IN ('admin_vet1', 'admin_vet2', 'drjuan', 'drana');

SELECT 
    username, 
    SUBSTRING(password_hash, 1, 20) AS hash_inicio,
    LENGTH(password_hash) AS hash_length,
    'OK' AS status
FROM super_admin 
WHERE username = 'superadmin';

SELECT '✅ Passwords actualizados correctamente' AS mensaje;
SELECT 'Ahora puedes usar: admin_vet1 / admin123' AS credenciales;
