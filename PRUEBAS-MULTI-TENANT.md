# 🧪 PLAN DE PRUEBAS MULTI-TENANT
## Validación de Aislamiento de Datos por Veterinaria

---

## 📋 OBJETIVO

Verificar que **cada veterinaria (tenant) tenga sus propios datos completamente aislados** y que no pueda ver ni modificar datos de otras veterinarias.

---

## 🗄️ PASO 1: PREPARAR DATOS DE PRUEBA EN LA BASE DE DATOS

### A. Crear 2 Veterinarias de Prueba

```sql
-- Veterinaria 1: "Clínica Veterinaria Patitas Felices"
INSERT INTO tenant (
    codigo_tenant, 
    nombre_comercial, 
    razon_social, 
    ruc, 
    direccion, 
    telefono, 
    email, 
    fecha_registro, 
    estado
) VALUES (
    'patitas-felices', 
    'Clínica Veterinaria Patitas Felices', 
    'Patitas Felices S.A.C.', 
    '20123456789', 
    'Av. Los Veterinarios 123, Lima', 
    '987654321', 
    'contacto@patitasfelices.com', 
    NOW(), 
    'ACTIVO'
);

-- Veterinaria 2: "Centro Veterinario Amigos Peludos"
INSERT INTO tenant (
    codigo_tenant, 
    nombre_comercial, 
    razon_social, 
    ruc, 
    direccion, 
    telefono, 
    email, 
    fecha_registro, 
    estado
) VALUES (
    'amigos-peludos', 
    'Centro Veterinario Amigos Peludos', 
    'Amigos Peludos E.I.R.L.', 
    '20987654321', 
    'Jr. Mascotas 456, Lima', 
    '912345678', 
    'info@amigospeludos.com', 
    NOW(), 
    'ACTIVO'
);
```

### B. Crear Usuarios para Cada Veterinaria

```sql
-- Usuario Admin para Veterinaria 1 (id_tenant = 1)
-- Password: admin123 (ya encriptado: $2a$10$...)
INSERT INTO usuario (
    username, 
    password_hash, 
    nombres, 
    apellidos, 
    email, 
    id_rol, 
    id_tenant, 
    estado, 
    fecha_creacion
) VALUES (
    'admin_patitas', 
    '$2a$10$N2vH9K3xN1yEXYxXxXxXxOXxXxXxXxXxXxXxXxXxXxXxXxXxXxXxXx', 
    'Admin', 
    'Patitas Felices', 
    'admin@patitasfelices.com', 
    1, -- Rol Administrador
    1, -- Tenant 1
    'ACTIVO', 
    NOW()
);

-- Usuario Admin para Veterinaria 2 (id_tenant = 2)
INSERT INTO usuario (
    username, 
    password_hash, 
    nombres, 
    apellidos, 
    email, 
    id_rol, 
    id_tenant, 
    estado, 
    fecha_creacion
) VALUES (
    'admin_amigos', 
    '$2a$10$N2vH9K3xN1yEXYxXxXxXxOXxXxXxXxXxXxXxXxXxXxXxXxXxXxXxXx', 
    'Admin', 
    'Amigos Peludos', 
    'admin@amigospeludos.com', 
    1, -- Rol Administrador
    2, -- Tenant 2
    'ACTIVO', 
    NOW()
);
```

### C. Crear Doctores para Cada Veterinaria

```sql
-- Doctores para Veterinaria 1
INSERT INTO doctor (nombres, apellidos, colegiatura, especialidad, telefono, email, id_tenant, estado)
VALUES 
    ('Dr. Juan', 'Pérez García', 'CMP-12345', 'Medicina General', '987111111', 'juan.perez@patitasfelices.com', 1, 'ACTIVO'),
    ('Dra. María', 'López Ruiz', 'CMP-12346', 'Cirugía', '987111112', 'maria.lopez@patitasfelices.com', 1, 'ACTIVO');

-- Doctores para Veterinaria 2
INSERT INTO doctor (nombres, apellidos, colegiatura, especialidad, telefono, email, id_tenant, estado)
VALUES 
    ('Dr. Carlos', 'Rodríguez Soto', 'CMP-54321', 'Dermatología', '987222221', 'carlos.rodriguez@amigospeludos.com', 2, 'ACTIVO'),
    ('Dra. Ana', 'Martínez Díaz', 'CMP-54322', 'Pediatría', '987222222', 'ana.martinez@amigospeludos.com', 2, 'ACTIVO');
```

### D. Crear Clientes para Cada Veterinaria

```sql
-- Clientes para Veterinaria 1
INSERT INTO cliente (nombres, apellidos, tipo_documento, numero_documento, telefono, email, direccion, id_tenant, estado, fecha_registro)
VALUES 
    ('Pedro', 'González', 'DNI', '12345678', '987333331', 'pedro@email.com', 'Calle Los Olivos 123', 1, 'ACTIVO', NOW()),
    ('Laura', 'Sánchez', 'DNI', '87654321', '987333332', 'laura@email.com', 'Av. Las Flores 456', 1, 'ACTIVO', NOW()),
    ('Roberto', 'Torres', 'DNI', '11223344', '987333333', 'roberto@email.com', 'Jr. Los Pinos 789', 1, 'ACTIVO', NOW());

-- Clientes para Veterinaria 2
INSERT INTO cliente (nombres, apellidos, tipo_documento, numero_documento, telefono, email, direccion, id_tenant, estado, fecha_registro)
VALUES 
    ('Sofia', 'Ramírez', 'DNI', '99887766', '987444441', 'sofia@email.com', 'Calle Las Palmeras 321', 2, 'ACTIVO', NOW()),
    ('Miguel', 'Flores', 'DNI', '55443322', '987444442', 'miguel@email.com', 'Av. Los Jardines 654', 2, 'ACTIVO', NOW()),
    ('Carmen', 'Vega', 'DNI', '66554433', '987444443', 'carmen@email.com', 'Jr. Las Rosas 987', 2, 'ACTIVO', NOW());
```

### E. Crear Mascotas para Cada Veterinaria

```sql
-- Mascotas para clientes de Veterinaria 1
-- (Asumiendo id_cliente de Veterinaria 1: 1, 2, 3)
INSERT INTO mascota (nombre, id_especie, id_raza, fecha_nacimiento, sexo, color, peso, id_cliente, id_tenant, estado, fecha_registro)
VALUES 
    ('Max', 1, 1, '2020-05-15', 'MACHO', 'Dorado', 25.5, 1, 1, 'ACTIVO', NOW()),
    ('Luna', 2, 7, '2021-03-10', 'HEMBRA', 'Gris', 4.2, 2, 1, 'ACTIVO', NOW()),
    ('Rocky', 1, 2, '2019-08-20', 'MACHO', 'Negro', 30.0, 3, 1, 'ACTIVO', NOW()),
    ('Michi', 2, 8, '2022-01-05', 'HEMBRA', 'Naranja', 3.5, 1, 1, 'ACTIVO', NOW());

-- Mascotas para clientes de Veterinaria 2
-- (Asumiendo id_cliente de Veterinaria 2: 4, 5, 6)
INSERT INTO mascota (nombre, id_especie, id_raza, fecha_nacimiento, sexo, color, peso, id_cliente, id_tenant, estado, fecha_registro)
VALUES 
    ('Toby', 1, 3, '2020-11-25', 'MACHO', 'Blanco', 20.0, 4, 2, 'ACTIVO', NOW()),
    ('Pelusa', 2, 9, '2021-06-30', 'HEMBRA', 'Blanco', 5.0, 5, 2, 'ACTIVO', NOW()),
    ('Firulais', 1, 1, '2018-12-10', 'MACHO', 'Marrón', 28.5, 6, 2, 'ACTIVO', NOW()),
    ('Minina', 2, 7, '2022-04-15', 'HEMBRA', 'Gris', 3.8, 4, 2, 'ACTIVO', NOW());
```

---

## 🧪 PASO 2: EJECUTAR PRUEBAS DE AISLAMIENTO

### ✅ **PRUEBA 1: Login y Dashboard**

#### Veterinaria 1 (Patitas Felices)
1. Login: `admin_patitas` / `admin123`
2. **Verificar Dashboard:**
   - Total Clientes: **3**
   - Total Mascotas: **4**
   - Total Citas: **0** (aún no creadas)
   - Total Ingresos: **S/. 0.00**

#### Veterinaria 2 (Amigos Peludos)
1. Logout y Login: `admin_amigos` / `admin123`
2. **Verificar Dashboard:**
   - Total Clientes: **3**
   - Total Mascotas: **4**
   - Total Citas: **0**
   - Total Ingresos: **S/. 0.00**

**✅ Resultado Esperado:** Cada veterinaria ve SOLO sus propios números.

---

### ✅ **PRUEBA 2: Módulo Clientes**

#### Veterinaria 1
1. Ir a `/clientes`
2. **Verificar que aparezcan SOLO:**
   - Pedro González
   - Laura Sánchez
   - Roberto Torres
3. **NO deben aparecer:** Sofia, Miguel, Carmen

#### Veterinaria 2
1. Ir a `/clientes`
2. **Verificar que aparezcan SOLO:**
   - Sofia Ramírez
   - Miguel Flores
   - Carmen Vega
3. **NO deben aparecer:** Pedro, Laura, Roberto

**✅ Resultado Esperado:** Aislamiento total de clientes por tenant.

---

### ✅ **PRUEBA 3: Módulo Mascotas**

#### Veterinaria 1
1. Ir a `/mascotas`
2. **Verificar que aparezcan SOLO:**
   - Max (Cliente: Pedro González)
   - Luna (Cliente: Laura Sánchez)
   - Rocky (Cliente: Roberto Torres)
   - Michi (Cliente: Pedro González)

#### Veterinaria 2
1. Ir a `/mascotas`
2. **Verificar que aparezcan SOLO:**
   - Toby (Cliente: Sofia Ramírez)
   - Pelusa (Cliente: Miguel Flores)
   - Firulais (Cliente: Carmen Vega)
   - Minina (Cliente: Sofia Ramírez)

**✅ Resultado Esperado:** Cada veterinaria ve solo mascotas de sus clientes.

---

### ✅ **PRUEBA 4: Crear Citas**

#### Veterinaria 1
1. Ir a `/citas`
2. Crear cita:
   - Cliente: **Pedro González** (de su tenant)
   - Mascota: **Max**
   - Doctor: **Dr. Juan Pérez** (de su tenant)
   - Fecha: Hoy + 1 día
   - Estado: PENDIENTE

#### Veterinaria 2
1. Ir a `/citas`
2. Crear cita:
   - Cliente: **Sofia Ramírez** (de su tenant)
   - Mascota: **Toby**
   - Doctor: **Dr. Carlos Rodríguez** (de su tenant)
   - Fecha: Hoy + 1 día
   - Estado: PENDIENTE

3. **Verificar:** Veterinaria 1 NO ve la cita de Veterinaria 2 y viceversa.

---

### ✅ **PRUEBA 5: Módulo Inventario**

#### Veterinaria 1
1. Ir a `/inventario`
2. Crear producto: "Vacuna Antirrábica - Lote A"
3. Stock: 50 unidades

#### Veterinaria 2
1. Ir a `/inventario`
2. Crear producto: "Vacuna Antirrábica - Lote B"
3. Stock: 30 unidades

4. **Verificar:** Cada veterinaria ve SOLO su propio inventario.

---

### ✅ **PRUEBA 6: Módulo Ventas**

#### Veterinaria 1
1. Ir a `/ventas`
2. Crear venta:
   - Cliente: Pedro González
   - Producto: Vacuna Antirrábica - Lote A
   - Total: S/. 150.00

#### Veterinaria 2
1. Ir a `/ventas`
2. Crear venta:
   - Cliente: Sofia Ramírez
   - Producto: Vacuna Antirrábica - Lote B
   - Total: S/. 200.00

3. **Verificar Dashboard:**
   - Veterinaria 1: Total Ingresos = **S/. 150.00**
   - Veterinaria 2: Total Ingresos = **S/. 200.00**

---

### ✅ **PRUEBA 7: Módulo Historias Clínicas**

#### Veterinaria 1
1. Ir a `/historias`
2. Crear historia para **Max** (mascota de su tenant)
3. Agregar diagnóstico, tratamiento

#### Veterinaria 2
1. Ir a `/historias`
2. Crear historia para **Toby** (mascota de su tenant)

3. **Verificar:** Cada veterinaria ve SOLO historias de sus mascotas.

---

### ✅ **PRUEBA 8: Módulo Usuarios**

#### Veterinaria 1
1. Ir a `/usuarios`
2. **Verificar que aparezca SOLO:**
   - Admin Patitas Felices
3. Crear nuevo usuario: "Recepcionista Patitas"

#### Veterinaria 2
1. Ir a `/usuarios`
2. **Verificar que aparezca SOLO:**
   - Admin Amigos Peludos
3. Crear nuevo usuario: "Recepcionista Amigos"

4. **Verificar:** Cada veterinaria administra SOLO sus usuarios.

---

### ✅ **PRUEBA 9: Intento de Acceso Cross-Tenant (Seguridad)**

#### Prueba Avanzada con DevTools
1. Login como Veterinaria 1
2. Abrir DevTools → Network
3. Intentar acceder manualmente a un cliente de Veterinaria 2:
   ```
   GET http://localhost:8080/api/clientes/4
   ```
4. **Resultado Esperado:** 
   - **403 Forbidden** o
   - **404 Not Found** (el cliente no existe en su tenant)

---

## 📊 TABLA DE RESULTADOS

| Módulo | Veterinaria 1 | Veterinaria 2 | Aislamiento | Estado |
|--------|---------------|---------------|-------------|--------|
| Dashboard | 3 clientes, 4 mascotas | 3 clientes, 4 mascotas | ✅ | [ ] |
| Clientes | 3 clientes | 3 clientes | ✅ | [ ] |
| Mascotas | 4 mascotas | 4 mascotas | ✅ | [ ] |
| Citas | 1 cita | 1 cita | ✅ | [ ] |
| Historias | 1 historia | 1 historia | ✅ | [ ] |
| Inventario | 1 producto | 1 producto | ✅ | [ ] |
| Ventas | S/. 150 | S/. 200 | ✅ | [ ] |
| Usuarios | 2 usuarios | 2 usuarios | ✅ | [ ] |
| Reportes | Solo su data | Solo su data | ✅ | [ ] |
| Cross-Tenant | 403/404 | 403/404 | ✅ | [ ] |

---

## 🚨 PROBLEMAS ENCONTRADOS

### Problema 1: [Describir si se encontró algún problema]
- **Descripción:**
- **Pasos para reproducir:**
- **Resultado esperado:**
- **Resultado actual:**

### Problema 2:
- ...

---

## ✅ CONCLUSIÓN

- [ ] **Aislamiento de datos verificado:** Cada tenant ve solo sus datos
- [ ] **Seguridad validada:** No hay acceso cross-tenant
- [ ] **Dashboard corregido:** Muestra 4 tarjetas correctas (Clientes, Mascotas, Citas, Ingresos)
- [ ] **Todos los módulos probados**
- [ ] **Sistema listo para multi-tenant**

---

**Fecha de prueba:** _______________  
**Realizado por:** _______________  
**Resultado general:** ⭐⭐⭐⭐⭐
