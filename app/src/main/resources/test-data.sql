-- =======================================================
-- DATOS DE PRUEBA PARA VETERINARIA_SAAS
-- Ejecutar después de setup-complete.sql
-- =======================================================

USE VETERINARIA_SAAS;

-- Insertar más clientes de prueba
INSERT INTO CLIENTE (id_veterinaria, nombres, apellidos, tipo_documento, documento, telefono, email, direccion, estado) VALUES
(1, 'María', 'González Pérez', 'DNI', '12345678', '987654321', 'maria.gonzalez@email.com', 'Av. Arequipa 123', 'ACTIVO'),
(1, 'Carlos', 'Rodríguez López', 'DNI', '87654321', '987654322', 'carlos.rodriguez@email.com', 'Jr. Lima 456', 'ACTIVO'),
(1, 'Ana', 'Martínez Silva', 'DNI', '45678912', '987654323', 'ana.martinez@email.com', 'Av. Brasil 789', 'ACTIVO'),
(1, 'Pedro', 'Sánchez Torres', 'DNI', '78912345', '987654324', 'pedro.sanchez@email.com', 'Jr. Cusco 321', 'ACTIVO'),
(1, 'Lucía', 'Fernández Ruiz', 'DNI', '32145698', '987654325', 'lucia.fernandez@email.com', 'Av. Venezuela 654', 'ACTIVO');

-- Insertar mascotas de prueba
INSERT INTO MASCOTA (id_veterinaria, id_cliente, id_raza, nombre, sexo, fecha_nacimiento, color, estado) VALUES
(1, 1, 1, 'Max', 'M', '2020-05-15', 'Dorado', 'ACTIVO'),
(1, 1, 2, 'Luna', 'H', '2021-08-20', 'Negro', 'ACTIVO'),
(1, 2, 3, 'Rocky', 'M', '2019-03-10', 'Marrón', 'ACTIVO'),
(1, 2, 7, 'Michi', 'M', '2022-01-15', 'Gris', 'ACTIVO'),
(1, 3, 5, 'Coco', 'H', '2021-11-25', 'Blanco', 'ACTIVO'),
(1, 3, 8, 'Pelusa', 'H', '2020-07-30', 'Naranja', 'ACTIVO'),
(1, 4, 1, 'Toby', 'M', '2022-04-12', 'Café', 'ACTIVO'),
(1, 4, 9, 'Simba', 'M', '2021-09-05', 'Anaranjado', 'ACTIVO'),
(1, 5, 4, 'Gordo', 'M', '2020-12-20', 'Blanco y café', 'ACTIVO'),
(1, 5, 6, 'Firulais', 'M', '2019-06-18', 'Tricolor', 'ACTIVO');

-- Insertar citas de prueba (algunas pasadas, algunas futuras)
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

-- Insertar historias clínicas
INSERT INTO HISTORIA_CLINICA (id_veterinaria, id_mascota, id_doctor, fecha_atencion, diagnostico, tratamiento, observaciones) VALUES
(1, 1, 1, '2025-11-15 09:00:00', 'Animal sano, peso adecuado', 'Vacuna Séxtuple aplicada', 'Próxima vacuna en 6 meses'),
(1, 2, 1, '2025-11-20 10:30:00', 'Control de rutina normal', 'Desparasitación interna', 'Peso: 4.5 kg. Todo normal'),
(1, 3, 1, '2025-11-25 14:00:00', 'Parasitosis intestinal leve', 'Antiparasitario oral por 3 días', 'Control en 15 días');

-- Insertar ventas de ejemplo
INSERT INTO VENTA (id_veterinaria, id_cliente, fecha, total, metodo_pago, estado) VALUES
(1, 1, '2025-11-15 09:30:00', 85.50, 'EFECTIVO', 'PAGADA'),
(1, 2, '2025-11-20 11:00:00', 138.00, 'TARJETA', 'PAGADA'),
(1, 3, '2025-11-25 14:30:00', 53.00, 'EFECTIVO', 'PAGADA'),
(1, 4, '2025-12-01 10:00:00', 145.50, 'TARJETA', 'PAGADA'),
(1, 5, '2025-12-05 15:30:00', 220.00, 'TRANSFERENCIA', 'PAGADA');

-- Insertar detalles de venta
INSERT INTO DETALLE_VENTA (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES
-- Venta 1
(1, 1, 2, 25.50, 51.00),
(1, 4, 1, 35.00, 35.00),
-- Venta 2
(2, 2, 3, 18.00, 54.00),
(2, 5, 3, 28.00, 84.00),
-- Venta 3
(3, 2, 1, 18.00, 18.00),
(3, 4, 1, 35.00, 35.00),
-- Venta 4
(4, 1, 1, 25.50, 25.50),
(5, 3, 1, 120.00, 120.00);

-- Insertar inventario inicial
INSERT INTO INVENTARIO (id_veterinaria, id_producto, stock_actual, stock_minimo, stock_maximo) VALUES
(1, 1, 50, 10, 100),
(1, 2, 30, 5, 80),
(1, 3, 15, 5, 30),
(1, 4, 25, 5, 50),
(1, 5, 40, 10, 80);

-- Insertar algunas compras
INSERT INTO COMPRA (id_veterinaria, id_proveedor, fecha, total, estado) VALUES
(1, 1, '2025-11-01 10:00:00', 1500.00, 'PAGADA'),
(1, 1, '2025-11-15 14:30:00', 850.00, 'PAGADA');

-- Insertar detalles de compra
INSERT INTO DETALLE_COMPRA (id_compra, id_producto, cantidad, costo_unitario, subtotal) VALUES
(1, 1, 50, 18.00, 900.00),
(1, 2, 30, 12.00, 360.00),
(1, 5, 20, 20.00, 400.00),
(2, 3, 10, 85.00, 850.00);

COMMIT;

-- Verificar datos insertados
SELECT 'Datos de prueba insertados exitosamente!' AS Mensaje;
SELECT 
    (SELECT COUNT(*) FROM CLIENTE WHERE id_veterinaria = 1) AS TotalClientes,
    (SELECT COUNT(*) FROM MASCOTA WHERE id_veterinaria = 1) AS TotalMascotas,
    (SELECT COUNT(*) FROM CITA WHERE id_veterinaria = 1) AS TotalCitas,
    (SELECT COUNT(*) FROM VENTA WHERE id_veterinaria = 1) AS TotalVentas,
    (SELECT COUNT(*) FROM INVENTARIO WHERE id_veterinaria = 1) AS ProductosEnInventario;
