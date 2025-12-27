-- ============================================
-- FIX: Actualizar password hash correcto para admin123
-- ============================================
-- PROBLEMA: El hash actual en la BD NO corresponde a "admin123"
-- SOLUCIÓN: Actualizar con un hash nuevo y verificado
-- ============================================

USE veterinaria_saas;

-- PASO 1: Verificar estado actual
SELECT '=== ANTES DE LA ACTUALIZACIÓN ===' AS mensaje;

SELECT 'Super Admin actual:' AS info;
SELECT id_super_admin, username, email, estado, 
       LENGTH(password_hash) AS hash_length,
       LEFT(password_hash, 30) AS hash_preview
FROM super_admin 
WHERE username = 'superadmin';

SELECT 'Usuario admin del tenant actual:' AS info;
SELECT id_usuario, username, email, id_tenant, estado,
       LENGTH(password_hash) AS hash_length,
       LEFT(password_hash, 30) AS hash_preview
FROM usuario 
WHERE username = 'admin';

-- PASO 2: Actualizar con hash CORRECTO para "admin123"
-- Este hash fue generado y verificado con BCryptPasswordEncoder

UPDATE super_admin 
SET password_hash = '$2a$10$ZNtd9U6DaVwur5aJnaSXr.yHhVEGDdqjvABqPpOUiVPAPuZpAkgFu'
WHERE username = 'superadmin';

UPDATE usuario 
SET password_hash = '$2a$10$ZNtd9U6DaVwur5aJnaSXr.yHhVEGDdqjvABqPpOUiVPAPuZpAkgFu'
WHERE username = 'admin' AND id_tenant = 1;

-- PASO 3: Verificar después de actualizar
SELECT '=== DESPUÉS DE LA ACTUALIZACIÓN ===' AS mensaje;

SELECT 'Super Admin actualizado:' AS info;
SELECT id_super_admin, username, email, estado,
       LENGTH(password_hash) AS hash_length,
       LEFT(password_hash, 30) AS hash_preview
FROM super_admin 
WHERE username = 'superadmin';

SELECT 'Usuario admin actualizado:' AS info;
SELECT id_usuario, username, email, id_tenant, estado,
       LENGTH(password_hash) AS hash_length,
       LEFT(password_hash, 30) AS hash_preview
FROM usuario 
WHERE username = 'admin';

COMMIT;

SELECT '✅ ACTUALIZACIÓN COMPLETADA' AS resultado;
SELECT 'Credenciales de acceso:' AS info;
SELECT 'Super Admin: superadmin / admin123' AS credencial
UNION ALL
SELECT 'Tenant Admin: admin / admin123' AS credencial;
