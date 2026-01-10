-- ================================================
-- SCRIPT DE VERIFICACIÓN DE DATOS
-- Ejecutar en MySQL Workbench o línea de comandos
-- ================================================

USE veterinaria_saas;

-- ================================================
-- 1. VERIFICAR TENANTS REGISTRADOS
-- ================================================
SELECT '============================================' as '';
SELECT '1. TENANTS REGISTRADOS' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    id_tenant as 'ID',
    codigo_unico as 'Código',
    nombre_comercial as 'Nombre',
    estado as 'Estado',
    plan as 'Plan'
FROM tenant
ORDER BY id_tenant;

-- ================================================
-- 2. VERIFICAR USUARIOS DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '2. USUARIOS DEL TENANT 1 (VET001)' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    id_usuario as 'ID',
    username as 'Usuario',
    email as 'Email',
    nombres as 'Nombres',
    apellidos as 'Apellidos',
    rol_nombre as 'Rol',
    estado as 'Estado'
FROM usuario
WHERE id_tenant = 1
ORDER BY id_usuario;

-- ================================================
-- 3. VERIFICAR PROVEEDORES DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '3. PROVEEDORES DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    id_proveedor as 'ID',
    nombre as 'Nombre',
    contacto as 'Contacto',
    telefono as 'Teléfono',
    email as 'Email',
    direccion as 'Dirección'
FROM proveedor
WHERE id_tenant = 1
ORDER BY id_proveedor;

-- ================================================
-- 4. VERIFICAR COMPRAS DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '4. COMPRAS DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    c.id_compra as 'ID',
    p.nombre as 'Proveedor',
    c.fecha as 'Fecha',
    c.total as 'Total',
    c.estado as 'Estado'
FROM compra c
LEFT JOIN proveedor p ON c.id_proveedor = p.id_proveedor
WHERE c.id_tenant = 1
ORDER BY c.fecha DESC
LIMIT 10;

SELECT CONCAT('Total de compras: ', COUNT(*)) as 'Resumen'
FROM compra
WHERE id_tenant = 1;

-- ================================================
-- 5. VERIFICAR CLIENTES DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '5. CLIENTES DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    id_cliente as 'ID',
    CONCAT(nombres, ' ', apellidos) as 'Nombre Completo',
    documento as 'Documento',
    telefono as 'Teléfono',
    email as 'Email'
FROM cliente
WHERE id_tenant = 1
ORDER BY id_cliente;

-- ================================================
-- 6. VERIFICAR VENTAS DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '6. VENTAS DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    v.id_venta as 'ID',
    CONCAT(cl.nombres, ' ', cl.apellidos) as 'Cliente',
    v.fecha as 'Fecha',
    v.total as 'Total',
    v.metodo_pago as 'Método Pago',
    v.estado as 'Estado'
FROM venta v
LEFT JOIN cliente cl ON v.id_cliente = cl.id_cliente
WHERE v.id_tenant = 1
ORDER BY v.fecha DESC
LIMIT 10;

SELECT CONCAT('Total de ventas: ', COUNT(*)) as 'Resumen'
FROM venta
WHERE id_tenant = 1;

-- ================================================
-- 7. VERIFICAR MASCOTAS DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '7. MASCOTAS DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    m.id_mascota as 'ID',
    m.nombre as 'Nombre',
    e.nombre as 'Especie',
    r.nombre as 'Raza',
    CONCAT(cl.nombres, ' ', cl.apellidos) as 'Dueño'
FROM mascota m
LEFT JOIN especie e ON m.id_especie = e.id_especie
LEFT JOIN raza r ON m.id_raza = r.id_raza
LEFT JOIN cliente cl ON m.id_cliente = cl.id_cliente
WHERE m.id_tenant = 1
ORDER BY m.id_mascota;

-- ================================================
-- 8. VERIFICAR CITAS DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '8. CITAS DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    c.id_cita as 'ID',
    m.nombre as 'Mascota',
    CONCAT(cl.nombres, ' ', cl.apellidos) as 'Cliente',
    CONCAT(d.nombres, ' ', d.apellidos) as 'Doctor',
    c.fecha_hora as 'Fecha/Hora',
    c.motivo as 'Motivo',
    c.estado as 'Estado'
FROM cita c
LEFT JOIN mascota m ON c.id_mascota = m.id_mascota
LEFT JOIN cliente cl ON c.id_cliente = cl.id_cliente
LEFT JOIN doctor d ON c.id_doctor = d.id_doctor
WHERE c.id_tenant = 1
ORDER BY c.fecha_hora DESC
LIMIT 10;

SELECT CONCAT('Total de citas: ', COUNT(*)) as 'Resumen'
FROM cita
WHERE id_tenant = 1;

-- ================================================
-- 9. VERIFICAR HISTORIAS CLÍNICAS DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '9. HISTORIAS CLÍNICAS DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    h.id_historia as 'ID',
    m.nombre as 'Mascota',
    CONCAT(cl.nombres, ' ', cl.apellidos) as 'Cliente',
    CONCAT(d.nombres, ' ', d.apellidos) as 'Doctor',
    h.fecha_atencion as 'Fecha',
    h.diagnostico as 'Diagnóstico'
FROM historia_clinica h
LEFT JOIN mascota m ON h.id_mascota = m.id_mascota
LEFT JOIN cliente cl ON m.id_cliente = cl.id_cliente
LEFT JOIN doctor d ON h.id_doctor = d.id_doctor
WHERE h.id_tenant = 1
ORDER BY h.fecha_atencion DESC
LIMIT 10;

SELECT CONCAT('Total de historias: ', COUNT(*)) as 'Resumen'
FROM historia_clinica
WHERE id_tenant = 1;

-- ================================================
-- 10. VERIFICAR PRODUCTOS EN INVENTARIO DEL TENANT 1
-- ================================================
SELECT '============================================' as '';
SELECT '10. PRODUCTOS EN INVENTARIO DEL TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    i.id_inventario as 'ID',
    p.nombre as 'Producto',
    c.nombre as 'Categoría',
    i.cantidad_actual as 'Stock',
    i.precio_unitario as 'Precio',
    i.fecha_vencimiento as 'Vencimiento'
FROM inventario i
LEFT JOIN producto p ON i.id_producto = p.id_producto
LEFT JOIN categoria c ON p.id_categoria = c.id_categoria
WHERE i.id_tenant = 1
ORDER BY i.cantidad_actual ASC
LIMIT 10;

SELECT CONCAT('Total de productos: ', COUNT(*)) as 'Resumen'
FROM inventario
WHERE id_tenant = 1;

-- ================================================
-- RESUMEN GENERAL
-- ================================================
SELECT '============================================' as '';
SELECT 'RESUMEN GENERAL - TENANT 1' as 'SECCIÓN';
SELECT '============================================' as '';

SELECT 
    'Usuarios' as 'Tabla',
    COUNT(*) as 'Total'
FROM usuario
WHERE id_tenant = 1
UNION ALL
SELECT 'Proveedores', COUNT(*) FROM proveedor WHERE id_tenant = 1
UNION ALL
SELECT 'Compras', COUNT(*) FROM compra WHERE id_tenant = 1
UNION ALL
SELECT 'Clientes', COUNT(*) FROM cliente WHERE id_tenant = 1
UNION ALL
SELECT 'Ventas', COUNT(*) FROM venta WHERE id_tenant = 1
UNION ALL
SELECT 'Mascotas', COUNT(*) FROM mascota WHERE id_tenant = 1
UNION ALL
SELECT 'Citas', COUNT(*) FROM cita WHERE id_tenant = 1
UNION ALL
SELECT 'Historias', COUNT(*) FROM historia_clinica WHERE id_tenant = 1
UNION ALL
SELECT 'Productos', COUNT(*) FROM inventario WHERE id_tenant = 1;

-- ================================================
-- FIN DEL SCRIPT
-- ================================================
