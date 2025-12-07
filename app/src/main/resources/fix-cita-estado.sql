-- =======================================================
-- FIX: Cambiar el tipo de columna estado en CITA
-- =======================================================

USE VETERINARIA_SAAS;

-- Modificar la columna estado de CITA para que sea ENUM
ALTER TABLE CITA
MODIFY COLUMN estado ENUM('PENDIENTE','ATENDIDA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE';

-- Verificar el cambio
DESC CITA;

SELECT 'Tipo de columna estado de CITA actualizado exitosamente!' AS Mensaje;
