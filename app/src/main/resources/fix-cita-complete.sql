-- =======================================================
-- FIX COMPLETO: Arreglar tabla CITA - Reinsertar datos
-- =======================================================

USE VETERINARIA_SAAS;

-- 1. Ver estructura actual
DESC CITA;

-- 2. Eliminar todas las citas existentes
DELETE FROM CITA;

-- 3. Modificar la columna estado correctamente
ALTER TABLE CITA
MODIFY COLUMN estado ENUM('PENDIENTE','ATENDIDA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE';

-- 4. Verificar que el cambio se aplicó
DESC CITA;

-- 5. Reinsertar las citas de prueba
INSERT INTO CITA (id_veterinaria, id_mascota, id_doctor, fecha_hora, motivo, estado) VALUES
-- Citas pasadas (atendidas)
(1, 1, 1, '2025-11-15 09:00:00', 'Vacunación anual', 'ATENDIDA'),
(1, 2, 1, '2025-11-20 10:30:00', 'Control de rutina', 'ATENDIDA'),
(1, 3, 1, '2025-11-25 14:00:00', 'Desparasitación', 'ATENDIDA'),
-- Citas próximas (pendientes)
(1, 4, 1, '2025-12-10 09:00:00', 'Vacunación antirrábica', 'PENDIENTE'),
(1, 5, 1, '2025-12-10 11:00:00', 'Control post-operatorio', 'PENDIENTE'),
(1, 6, 1, '2025-12-12 10:00:00', 'Chequeo general', 'PENDIENTE'),
(1, 7, 1, '2025-12-15 15:30:00', 'Vacunación puppy', 'PENDIENTE'),
(1, 8, 1, '2025-12-18 09:30:00', 'Control de parásitos', 'PENDIENTE'),
-- Citas futuras
(1, 9, 1, '2025-12-20 11:00:00', 'Limpieza dental', 'PENDIENTE'),
(1, 10, 1, '2025-12-22 14:00:00', 'Consulta dermatológica', 'PENDIENTE'),
(1, 1, 1, '2025-12-25 10:00:00', 'Control de vacunas', 'PENDIENTE'),
(1, 2, 1, '2025-12-28 16:00:00', 'Chequeo geriátrico', 'PENDIENTE'),
-- Algunas canceladas
(1, 3, 1, '2025-12-08 09:00:00', 'Control', 'CANCELADA');

COMMIT;

-- 6. Verificar los datos
SELECT 
    COUNT(*) as TotalCitas,
    SUM(CASE WHEN estado = 'PENDIENTE' THEN 1 ELSE 0 END) as Pendientes,
    SUM(CASE WHEN estado = 'ATENDIDA' THEN 1 ELSE 0 END) as Atendidas,
    SUM(CASE WHEN estado = 'CANCELADA' THEN 1 ELSE 0 END) as Canceladas
FROM CITA;

SELECT 'Tabla CITA arreglada y datos reinsertados exitosamente!' AS Mensaje;
