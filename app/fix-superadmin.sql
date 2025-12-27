-- Script para insertar/verificar Super Admin
-- Ejecuta esto en tu cliente MySQL (phpMyAdmin, MySQL Workbench, HeidiSQL, etc.)

USE veterinaria_saas;

-- Verificar si existe el super admin
SELECT 'VERIFICANDO SUPER ADMIN...' AS mensaje;
SELECT * FROM super_admin WHERE username = 'superadmin';

-- Si la query anterior no retorna nada, ejecuta este INSERT:
-- (Si ya existe, va a dar error, pero no importa)

INSERT INTO super_admin (username, password_hash, email, nombres, apellidos, telefono, estado) 
VALUES ('superadmin', '$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe', 'admin@appvet.com', 'Admin', 'Sistema', '999999999', 'ACTIVO')
ON DUPLICATE KEY UPDATE 
    password_hash = '$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe',
    estado = 'ACTIVO';

-- Verificar después de insertar
SELECT 'SUPER ADMIN DESPUÉS DE INSERT:' AS mensaje;
SELECT id_super_admin, username, email, nombres, apellidos, estado, 
       LENGTH(password_hash) AS hash_length,
       LEFT(password_hash, 20) AS hash_preview
FROM super_admin 
WHERE username = 'superadmin';

-- Si quieres ver TODOS los super admins:
SELECT * FROM super_admin;
