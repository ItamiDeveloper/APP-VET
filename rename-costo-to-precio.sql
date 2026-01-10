-- Renombrar columna costoUnitario a precioUnitario en DETALLE_COMPRA
-- Esto mantiene consistencia con el frontend y con DETALLE_VENTA

USE veterinaria_saas;

ALTER TABLE DETALLE_COMPRA 
CHANGE COLUMN costoUnitario precioUnitario DECIMAL(10,2) NOT NULL;

SELECT 'Columna renombrada exitosamente' AS resultado;
