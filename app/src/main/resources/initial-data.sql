-- ============================================
-- DATOS INICIALES PARA VETERINARIA_SAAS
-- ============================================

USE VETERINARIA_SAAS;

-- Insertar roles básicos del sistema
INSERT INTO ROL (nombre, descripcion) VALUES
('ROLE_ADMIN', 'Administrador del sistema con acceso completo'),
('ROLE_VETERINARIO', 'Veterinario con acceso a gestión clínica'),
('ROLE_RECEPCIONISTA', 'Recepcionista con acceso a citas y clientes');

-- Insertar un plan básico de prueba
INSERT INTO PLAN (nombre, precio_mensual, max_doctores, max_mascotas, max_almacenamiento_mb, estado) VALUES
('Plan Básico', 99.99, 5, 100, 1024, 'ACTIVO'),
('Plan Profesional', 199.99, 15, 500, 5120, 'ACTIVO'),
('Plan Empresarial', 399.99, 50, 2000, 20480, 'ACTIVO');

-- Insertar especies comunes
INSERT INTO ESPECIE (nombre, descripcion) VALUES
('Perro', 'Canis familiaris'),
('Gato', 'Felis catus'),
('Ave', 'Aves'),
('Conejo', 'Oryctolagus cuniculus'),
('Reptil', 'Reptilia'),
('Roedor', 'Rodentia');

-- Insertar razas comunes de perros
INSERT INTO RAZA (id_especie, nombre, descripcion) VALUES
(1, 'Labrador Retriever', 'Raza grande, amigable y activa'),
(1, 'Pastor Alemán', 'Raza grande, inteligente y protectora'),
(1, 'Golden Retriever', 'Raza grande, cariñosa y familiar'),
(1, 'Bulldog', 'Raza mediana, tranquila y leal'),
(1, 'Chihuahua', 'Raza pequeña, alerta y vivaz'),
(1, 'Mestizo', 'Raza mixta');

-- Insertar razas comunes de gatos
INSERT INTO RAZA (id_especie, nombre, descripcion) VALUES
(2, 'Persa', 'Gato de pelo largo y rostro plano'),
(2, 'Siamés', 'Gato esbelto con marcas distintivas'),
(2, 'Maine Coon', 'Gato de gran tamaño y pelo largo'),
(2, 'Británico de pelo corto', 'Gato robusto y tranquilo'),
(2, 'Mestizo', 'Gato sin raza definida');

-- Insertar categorías de productos
INSERT INTO CATEGORIA_PRODUCTO (nombre, descripcion, estado) VALUES
('Medicamentos', 'Productos farmacéuticos veterinarios', 'ACTIVO'),
('Alimentos', 'Alimentos para mascotas', 'ACTIVO'),
('Accesorios', 'Accesorios y juguetes para mascotas', 'ACTIVO'),
('Higiene', 'Productos de higiene y limpieza', 'ACTIVO'),
('Suplementos', 'Suplementos nutricionales', 'ACTIVO');

-- Insertar una veterinaria de prueba
INSERT INTO VETERINARIA (id_plan, nombre, ruc, telefono, direccion, estado) VALUES
(1, 'Veterinaria Demo', '20123456789', '987654321', 'Av. Principal 123, Lima', 'ACTIVO');

-- Insertar suscripción activa para la veterinaria de prueba
INSERT INTO SUSCRIPCION (id_veterinaria, id_plan, fecha_inicio, fecha_fin, estado) VALUES
(1, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'ACTIVO');

-- Insertar usuario administrador de prueba (password: admin123)
-- Password hash generado con BCrypt para "admin123"

-- Usuario admin (password: admin123)
INSERT INTO USUARIO (id_veterinaria, id_rol, username, password_hash, email, estado) VALUES
(1, 1, 'admin', '$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe', 'admin@vetdemo.com', 'ACTIVO');

-- Usuario veterinario (password: vet123)
INSERT INTO USUARIO (id_veterinaria, id_rol, username, password_hash, email, estado) VALUES
(1, 2, 'veterinario', '$2a$10$mLDxNLK5YRU3TqvGVVhKPu1Q.M1dBhRCL1dpMfWLQAE9FQMxdJT/q', 'veterinario@vetdemo.com', 'ACTIVO');

-- Insertar un doctor vinculado al usuario veterinario
INSERT INTO DOCTOR (id_veterinaria, id_usuario, nombres, apellidos, colegiatura, especialidad, estado) VALUES
(1, 2, 'Juan Carlos', 'Pérez López', 'CMP12345', 'Medicina General', 'ACTIVO');

-- Insertar un proveedor de ejemplo
INSERT INTO PROVEEDOR (nombre, ruc, telefono, email, direccion, estado) VALUES
('Distribuidora Veterinaria SAC', '20987654321', '987123456', 'ventas@distveterinaria.com', 'Jr. Comercio 456, Lima', 'ACTIVO');

-- Insertar productos de ejemplo
INSERT INTO PRODUCTO (id_categoria, nombre, descripcion, es_medicamento, precio_unitario, estado) VALUES
(1, 'Antibiótico Amoxicilina 250mg', 'Antibiótico de amplio espectro', 1, 25.50, 'ACTIVO'),
(1, 'Antiparasitario interno', 'Desparasitante oral', 1, 18.00, 'ACTIVO'),
(2, 'Alimento Premium Adulto 15kg', 'Alimento balanceado para perros adultos', 0, 120.00, 'ACTIVO'),
(3, 'Collar antipulgas', 'Collar con protección por 8 meses', 0, 35.00, 'ACTIVO'),
(4, 'Shampoo medicado', 'Shampoo dermatológico', 0, 28.00, 'ACTIVO');

COMMIT;

SELECT 'Datos iniciales insertados correctamente!' AS mensaje;
