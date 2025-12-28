-- Agregar columnas faltantes a la tabla tenant
USE veterinaria_saas;

-- Agregar color_primario y color_secundario si no existen
ALTER TABLE tenant 
ADD COLUMN IF NOT EXISTS color_primario VARCHAR(7) DEFAULT '#1976d2',
ADD COLUMN IF NOT EXISTS color_secundario VARCHAR(7) DEFAULT '#dc004e';

-- Verificar estructura
DESCRIBE tenant;

SELECT 'Columnas agregadas exitosamente' AS resultado;
