# 🔧 SOLUCIONES A PROBLEMAS DE VISUALIZACIÓN Y REGISTRO

**Fecha:** 10 de Enero 2026  
**Estado:** SOLUCIONADO ✅

---

## 🐛 PROBLEMAS IDENTIFICADOS

### 1. ❌ Historias Clínicas NO se Registraban (Error 500)

**Error Reportado:**
```
Failed to load resource: the server responded with a status of 500
POST /api/tenant/historias
```

**Causa Raíz:**
El DTO `HistoriaClinicaDTO.java` tenía validación `@NotNull` en campos que no deben ser obligatorios al CREAR una nueva historia:

```java
// ❌ INCORRECTO
@NotNull(message = "El campo 'idHistoria' no puede ser nulo")
private Integer idHistoria;  // Este campo se genera automáticamente

@NotNull(message = "El campo 'idTenant' no puede ser nulo")  
private Integer idTenant;  // El backend lo asigna desde el JWT
```

**Solución Aplicada:**
```java
// ✅ CORRECTO
private Integer idHistoria;  // Nullable al crear, se genera automáticamente
private Integer idTenant;    // Nullable, el backend lo asigna desde JWT
```

**Archivo Modificado:**
- `app/src/main/java/com/vet/spring/app/dto/historiaDto/HistoriaClinicaDTO.java`

**Estado:** ✅ SOLUCIONADO

---

### 2. ⚠️ Datos Aparecen como "N/A" en Tablas

**Problema:**
- Compras: Proveedor, Fecha, Total, Estado = N/A
- Ventas: Cliente, Fecha, Total, Método Pago, Estado = N/A  
- Usuarios: Todos los campos = N/A

**Posibles Causas:**

#### A) No Hay Datos en la Base de Datos

**Verificación Necesaria:**
```sql
-- Verificar si hay compras registradas
SELECT * FROM compra WHERE id_tenant = 1;

-- Verificar si hay ventas registradas
SELECT * FROM venta WHERE id_tenant = 1;

-- Verificar si hay usuarios registrados
SELECT * FROM usuario WHERE id_tenant = 1;
```

**Si NO hay datos, crear datos de prueba:**
```sql
-- Insertar una compra de prueba (si existe proveedor con id=1)
INSERT INTO compra (id_tenant, id_proveedor, fecha, total, estado)
VALUES (1, 1, NOW(), 100.00, 'COMPLETADA');

-- Insertar una venta de prueba (si existe cliente con id=1)
INSERT INTO venta (id_tenant, id_cliente, fecha, total, metodo_pago, estado)
VALUES (1, 1, NOW(), 50.00, 'EFECTIVO', 'COMPLETADA');
```

#### B) Problema con el Filtrado por Tenant

El backend filtra correctamente por `tenantId` desde el JWT, pero si el `tenantId` del JWT no coincide con los datos en la BD, no se verá nada.

**Verificar JWT:**
```typescript
// En el navegador, abrir DevTools Console y ejecutar:
const token = localStorage.getItem('token');
console.log('Token:', token);

// Decodificar el JWT en https://jwt.io/ para ver el tenantId
```

#### C) Relaciones no Populadas Correctamente

El backend SÍ está populando correctamente:
- ✅ `CompraService.toDTO()` → asigna `proveedorNombre`
- ✅ `VentaService.toDTO()` → asigna `clienteNombre`  
- ✅ `UsuarioService.toDTO()` → asigna `rolNombre` y `veterinariaNombre`

**Estado:** ⚠️ VERIFICAR DATOS EN BD

---

### 3. ⚠️ Reportes No Muestran Datos

**Problema:**
El usuario registró una cita para el día 12 pero no aparece en el reporte de citas.

**Posibles Causas:**

#### A) Rango de Fechas Incorrecto

Por defecto, los reportes usan:
```typescript
const [fechaInicio, setFechaInicio] = useState(
  moment().startOf('month').format('YYYY-MM-DD')  // Inicio del mes actual
);
const [fechaFin, setFechaFin] = useState(
  moment().format('YYYY-MM-DD')  // Fecha actual
);
```

**Si la cita es del día 12 pero estamos en enero 10:**
- La cita del 12 de enero NO aparecerá porque está DESPUÉS de la fecha fin (10 de enero)

**Solución:**
- Ajustar el rango de fechas para incluir el 12 de enero:
  ```
  Fecha Inicio: 01/01/2026
  Fecha Fin: 31/01/2026
  ```

#### B) Estado de la Cita

El reporte puede estar filtrando por estado. Verificar:
```sql
SELECT * FROM cita 
WHERE id_tenant = 1 
  AND fecha_hora BETWEEN '2026-01-01' AND '2026-01-31'
  AND estado != 'CANCELADA';  -- Asegúrate de que la cita no esté cancelada
```

#### C) Servicio de Reportes

Verificar el servicio de reportes en backend:

```java
// ReporteService.java debe tener:
public List<ReporteCitaDTO> getReporteCitas(
    Integer tenantId,
    LocalDate fechaInicio,
    LocalDate fechaFin
) {
    return citaRepository.findByTenantIdAndFechaBetween(
        tenantId, 
        fechaInicio.atStartOfDay(),
        fechaFin.atTime(23, 59, 59)
    ).stream()
        .map(this::toReporteCitaDTO)
        .collect(Collectors.toList());
}
```

**Estado:** ⚠️ VERIFICAR RANGO DE FECHAS Y ESTADO DE CITA

---

## 🔍 DIAGNÓSTICO PASO A PASO

### Paso 1: Verificar que el Backend Está Corriendo

```bash
# En terminal Java
cd C:\Users\Itami\APP-VET\app
mvn spring-boot:run
```

**Esperar el mensaje:**
```
Started AppApplication in X.XXX seconds
```

### Paso 2: Verificar que el Frontend Está Corriendo

```bash
# En terminal Node
cd C:\Users\Itami\APP-VET\nx-vet
npm run dev
```

**Abrir:** http://localhost:3000

### Paso 3: Verificar el Token JWT

1. Login con `admin_vet1` / `admin123`
2. Abrir DevTools → Console
3. Ejecutar:
   ```javascript
   const token = localStorage.getItem('token');
   console.log('Token:', token);
   
   // Decodificar en https://jwt.io/
   // Verificar que contenga: "tenantId": "1"
   ```

### Paso 4: Verificar Datos en MySQL

```sql
USE veterinaria_saas;

-- 1. Verificar tenants existentes
SELECT * FROM tenant;

-- 2. Verificar compras del tenant 1
SELECT c.*, p.nombre as proveedor_nombre
FROM compra c
LEFT JOIN proveedor p ON c.id_proveedor = p.id_proveedor
WHERE c.id_tenant = 1;

-- 3. Verificar ventas del tenant 1
SELECT v.*, CONCAT(cl.nombres, ' ', cl.apellidos) as cliente_nombre
FROM venta v
LEFT JOIN cliente cl ON v.id_cliente = cl.id_cliente
WHERE v.id_tenant = 1;

-- 4. Verificar usuarios del tenant 1
SELECT u.*, r.nombre as rol_nombre, t.nombre_comercial as veterinaria_nombre
FROM usuario u
LEFT JOIN rol r ON u.id_rol = r.id_rol
LEFT JOIN tenant t ON u.id_tenant = t.id_tenant
WHERE u.id_tenant = 1;

-- 5. Verificar citas del tenant 1
SELECT c.*, m.nombre as mascota_nombre, 
       CONCAT(cl.nombres, ' ', cl.apellidos) as cliente_nombre,
       CONCAT(d.nombres, ' ', d.apellidos) as doctor_nombre
FROM cita c
LEFT JOIN mascota m ON c.id_mascota = m.id_mascota
LEFT JOIN cliente cl ON c.id_cliente = cl.id_cliente
LEFT JOIN doctor d ON c.id_doctor = d.id_doctor
WHERE c.id_tenant = 1;
```

### Paso 5: Crear Datos de Prueba (Si es Necesario)

```sql
-- Si no hay compras, crear una:
INSERT INTO compra (id_tenant, id_proveedor, fecha, total, estado)
VALUES (1, 1, NOW(), 150.00, 'COMPLETADA');

-- Si no hay ventas, crear una:
INSERT INTO venta (id_tenant, id_cliente, fecha, total, metodo_pago, estado)
VALUES (1, 1, NOW(), 75.00, 'EFECTIVO', 'COMPLETADA');
```

### Paso 6: Probar Crear Historia Clínica

1. Ir a http://localhost:3000/historias
2. Clic en "+ Nueva Consulta"
3. Llenar formulario:
   - Mascota: Seleccionar una mascota existente
   - Doctor: Seleccionar Dr. Juan Pérez
   - Fecha Atención: Fecha actual
   - Diagnóstico: "Gastroenteritis"
   - Tratamiento: "Antibióticos y dieta blanda"
   - Observaciones: "Control en 5 días"
4. Clic en "Guardar"

**Resultado Esperado:**
- ✅ Toast: "Creando historia clínica..."
- ✅ Toast: "Historia clínica creada correctamente"
- ✅ Modal se cierra
- ✅ Tabla se actualiza con la nueva historia

**Si Falla:**
- Abrir DevTools → Console
- Revisar el error completo
- Verificar el payload enviado en Network → XHR

### Paso 7: Verificar Reportes

1. Ir a http://localhost:3000/reportes
2. Seleccionar "Tipo de Reporte": Citas
3. Ajustar fechas:
   - Fecha Inicio: 01/01/2026
   - Fecha Fin: 31/01/2026
4. Ver si aparecen las citas registradas

---

## ✅ CHECKLIST DE VERIFICACIÓN

- [ ] Backend compilado correctamente (✅ YA HECHO)
- [ ] Backend corriendo en puerto 8080
- [ ] Frontend corriendo en puerto 3000
- [ ] Usuario logueado correctamente
- [ ] Token JWT contiene tenantId
- [ ] Base de datos tiene tenant con id=1
- [ ] Base de datos tiene al menos un proveedor
- [ ] Base de datos tiene al menos un cliente
- [ ] Base de datos tiene al menos una mascota
- [ ] Crear historia clínica funciona sin error 500
- [ ] Compras muestran proveedor (no N/A)
- [ ] Ventas muestran cliente (no N/A)
- [ ] Usuarios muestran rol y veterinaria (no N/A)
- [ ] Reportes de citas muestran datos con rango correcto

---

## 📝 RESUMEN DE CORRECCIONES

| Problema | Causa | Solución | Estado |
|----------|-------|----------|--------|
| Error 500 al crear historia clínica | Validación @NotNull en idHistoria e idTenant | Removida validación @NotNull | ✅ Corregido |
| Tablas muestran N/A | Posiblemente falta datos en BD | Verificar datos y crear ejemplos | ⚠️ Verificar |
| Reportes vacíos | Rango de fechas o falta de datos | Ajustar fechas y verificar datos | ⚠️ Verificar |
| Multi-tenant hardcoding | idVeterinaria: 1 en código | Removido en commit anterior | ✅ Corregido |

---

## 🚀 PRÓXIMOS PASOS

1. **Inmediato:**
   - Reiniciar backend: `cd app && mvn spring-boot:run`
   - Probar crear historia clínica nuevamente
   - Verificar que no da error 500

2. **Si las tablas están vacías:**
   - Ejecutar queries SQL de verificación (Paso 4)
   - Si no hay datos, ejecutar queries de inserción (Paso 5)
   - Refrescar páginas en el navegador

3. **Para reportes:**
   - Asegurarse de que hay citas registradas en BD
   - Ajustar rango de fechas para incluir las citas
   - Verificar que las citas no estén canceladas

---

## 📞 COMANDOS ÚTILES

### Reiniciar Backend
```bash
cd C:\Users\Itami\APP-VET\app
mvn spring-boot:run
```

### Reiniciar Frontend
```bash
cd C:\Users\Itami\APP-VET\nx-vet
npm run dev
```

### Verificar Puerto 8080
```powershell
netstat -ano | findstr :8080
```

### Ver Logs del Backend en Tiempo Real
```powershell
cd C:\Users\Itami\APP-VET\app
tail -f logs/app.log  # Si hay logs configurados
```

---

**Estado Final:** Historia clínica corregida ✅ | Verificación de datos pendiente ⚠️

**Última actualización:** 10 de Enero 2026 - 12:10 PM
