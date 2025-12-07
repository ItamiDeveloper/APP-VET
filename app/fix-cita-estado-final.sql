-- VERIFICAR Y CORREGIR TABLA CITA
USE VETERINARIA_SAAS;

-- 1. Ver estructura actual
DESC CITA;

-- 2. Eliminar todas las citas existentes
DELETE FROM CITA;

-- 3. Modificar columna estado a ENUM con valores correctos
ALTER TABLE CITA 
MODIFY COLUMN estado ENUM('PENDIENTE', 'ATENDIDA', 'CANCELADA') NOT NULL DEFAULT 'PENDIENTE';

-- 4. Verificar que el cambio se aplicó
DESC CITA;

-- 5. Insertar datos de prueba con valores ENUM correctos
INSERT INTO CITA (id_veterinaria, id_mascota, id_doctor, fecha_hora, motivo, estado) VALUES
(1, 1, 1, '2024-12-15 09:00:00', 'Consulta general', 'PENDIENTE'),
(1, 2, 1, '2024-12-15 10:00:00', 'Vacunación', 'PENDIENTE'),
(1, 3, 2, '2024-12-15 11:00:00', 'Revisión dental', 'ATENDIDA'),
(1, 4, 2, '2024-12-16 09:00:00', 'Desparasitación', 'PENDIENTE'),
(1, 5, 1, '2024-12-16 10:00:00', 'Control post-operatorio', 'ATENDIDA'),
(1, 6, 3, '2024-12-16 11:00:00', 'Consulta dermatológica', 'PENDIENTE'),
(1, 7, 3, '2024-12-17 09:00:00', 'Radiografía', 'ATENDIDA'),
(1, 8, 1, '2024-12-17 10:00:00', 'Análisis de sangre', 'PENDIENTE'),
(1, 9, 2, '2024-12-17 11:00:00', 'Limpieza dental', 'CANCELADA'),
(1, 10, 2, '2024-12-18 09:00:00', 'Vacuna antirrábica', 'PENDIENTE'),
(1, 1, 3, '2024-12-18 10:00:00', 'Control mensual', 'PENDIENTE'),
(1, 2, 3, '2024-12-18 11:00:00', 'Castración', 'PENDIENTE'),
(1, 3, 1, '2024-12-19 09:00:00', 'Ecografía', 'PENDIENTE');

-- 6. Verificar datos insertados
SELECT estado, COUNT(*) as cantidad FROM CITA GROUP BY estado;

-- 7. Ver todas las citas
SELECT id_cita, id_mascota, id_doctor, fecha_hora, estado, motivo FROM CITA ORDER BY fecha_hora;

COMMIT;
