-- ==================================================================
-- FIX USUARIOS PASSWORD HASHES
-- Run this in MySQL Workbench against VETERINARIA_SAAS database
-- ==================================================================

USE VETERINARIA_SAAS;

-- Delete existing users
DELETE FROM REFRESH_TOKEN WHERE id_usuario IN (1, 2);
DELETE FROM DOCTOR WHERE id_usuario = 2;
DELETE FROM USUARIO WHERE id_usuario IN (1, 2);

-- Re-insert admin user with VERIFIED BCrypt hash for "admin123"
-- Hash generated and tested with Spring Security BCryptPasswordEncoder
INSERT INTO USUARIO (id_usuario, id_veterinaria, id_rol, username, password_hash, email, estado) VALUES
(1, 1, 1, 'admin', '$2a$10$8jrvaLY788QLhrJ6V6hen.f96iP6kNX3vFNJjwHmOnwLzFr.UyYMO', 'admin@vetdemo.com', 'ACTIVO');

-- Re-insert veterinario user with VERIFIED BCrypt hash for "vet123"
INSERT INTO USUARIO (id_usuario, id_veterinaria, id_rol, username, password_hash, email, estado) VALUES
(2, 1, 2, 'veterinario', '$2a$10$3AgQyVfAzLFw.TAvgLRlGu5k1J3XeUIQ99bXiu4hc/qkH3.weZp2a', 'veterinario@vetdemo.com', 'ACTIVO');

-- Re-link doctor to veterinario user
INSERT INTO DOCTOR (id_doctor, id_veterinaria, id_usuario, nombres, apellidos, colegiatura, especialidad, estado) VALUES
(1, 1, 2, 'Juan Carlos', 'Pérez López', 'CMP12345', 'Medicina General', 'ACTIVO');

COMMIT;

-- Verify
SELECT id_usuario, username, password_hash, estado FROM USUARIO;
