# 🧪 PRUEBAS DE ENDPOINTS - VetSaaS API

## Estado del Servidor
✅ Servidor corriendo en: http://localhost:8080
✅ Swagger UI: http://localhost:8080/swagger-ui.html
✅ Compilación: 132 archivos, 0 errores

---

## 🔐 FASE 1: PRUEBAS DE AUTENTICACIÓN

### Test 1.1: Login Super Admin ⭐ PRIORITY
**Endpoint:** `POST /api/auth/super-admin/login`

**Request Body:**
```json
{
  "username": "superadmin",
  "password": "admin123"
}
```

**Resultado Esperado:**
- Status: 200 OK
- Response debe contener:
  - `token`: JWT token string
  - `type`: "Bearer"
  - `username`: "superadmin"
  - `tenantId`: null (super admin no tiene tenant)

**Validaciones:**
- [ ] Status code es 200
- [ ] Token existe y no está vacío
- [ ] Type es "Bearer"
- [ ] Username es correcto
- [ ] TenantId es null
- [ ] Token se puede decodificar en jwt.io
- [ ] Token contiene claim `userType: SUPER_ADMIN`

---

### Test 1.2: Login Usuario Tenant (vet-demo)
**Endpoint:** `POST /api/auth/tenant/login`

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Resultado Esperado:**
- Status: 200 OK
- Response debe contener:
  - `token`: JWT token string
  - `type`: "Bearer"
  - `username`: "admin"
  - `tenantId`: "1" (string)

**Validaciones:**
- [ ] Status code es 200
- [ ] Token existe y no está vacío
- [ ] Type es "Bearer"
- [ ] Username es correcto
- [ ] TenantId es "1"
- [ ] Token se puede decodificar en jwt.io
- [ ] Token contiene claim `tenantId: 1`
- [ ] Token contiene claim `userType: USUARIO`

---

### Test 1.3: Login con Credenciales Incorrectas
**Endpoint:** `POST /api/auth/super-admin/login`

**Request Body:**
```json
{
  "username": "superadmin",
  "password": "wrongpassword"
}
```

**Resultado Esperado:**
- Status: 401 Unauthorized
- Error message: "Credenciales inválidas" o "Bad credentials"

---

### Test 1.4: Login Usuario No Existente
**Endpoint:** `POST /api/auth/tenant/login`

**Request Body:**
```json
{
  "username": "noexiste",
  "password": "cualquiera"
}
```

**Resultado Esperado:**
- Status: 401 Unauthorized
- Error message indicando usuario no encontrado

---

## 📊 FASE 2: PRUEBAS DE PLANES (Sin Autenticación)

### Test 2.1: Obtener Planes Públicos
**Endpoint:** `GET /api/public/planes`

**Headers:**
- Ninguno (endpoint público)

**Resultado Esperado:**
- Status: 200 OK
- Array con 4 planes:
  - Básico ($49.99)
  - Profesional ($99.99)
  - Premium ($149.99)
  - Enterprise ($299.99)

**Validaciones:**
- [ ] Status code es 200
- [ ] Response es un array
- [ ] Array contiene 4 elementos
- [ ] Cada plan tiene: id, nombre, precioMensual, estado
- [ ] Todos los planes tienen estado ACTIVO

---

## 🔒 FASE 3: PRUEBAS DE PLANES (Super Admin)

### Preparación:
1. Ejecutar Test 1.1 (Login Super Admin)
2. Copiar el token de la response
3. Click en botón "Authorize" en Swagger UI
4. Pegar: `Bearer {token}`

### Test 3.1: Obtener Todos los Planes (Autenticado)
**Endpoint:** `GET /api/super-admin/planes`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 200 OK
- Array con 4 planes (incluso los INACTIVOS si existen)

---

### Test 3.2: Obtener Plan por ID
**Endpoint:** `GET /api/super-admin/planes/1`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 200 OK
- Response: Objeto Plan con id=1 (Básico)

---

### Test 3.3: Crear Nuevo Plan
**Endpoint:** `POST /api/super-admin/planes`

**Headers:**
- `Authorization: Bearer {token_super_admin}`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "nombre": "Plan Prueba",
  "descripcion": "Plan creado desde Swagger para testing",
  "precioMensual": 79.99,
  "precioAnual": 799.99,
  "maxUsuarios": 5,
  "maxDoctores": 3,
  "maxMascotas": 100,
  "maxAlmacenamientoMb": 1024,
  "tieneReportesAvanzados": true,
  "tieneApiAcceso": false,
  "tieneSoportePrioritario": false,
  "ordenVisualizacion": 5,
  "estado": "ACTIVO"
}
```

**Resultado Esperado:**
- Status: 201 Created
- Response: Plan creado con id asignado

**Validaciones:**
- [ ] Status code es 201
- [ ] Response contiene id (no null)
- [ ] nombre es "Plan Prueba"
- [ ] precioMensual es 79.99

---

### Test 3.4: Actualizar Plan (PUT)
**Endpoint:** `PUT /api/super-admin/planes/5`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Request Body:**
```json
{
  "nombre": "Plan Prueba Actualizado",
  "descripcion": "Plan modificado",
  "precioMensual": 89.99,
  "precioAnual": 899.99,
  "maxUsuarios": 10,
  "maxDoctores": 5,
  "maxMascotas": 150,
  "maxAlmacenamientoMb": 2048,
  "tieneReportesAvanzados": true,
  "tieneApiAcceso": true,
  "tieneSoportePrioritario": false,
  "ordenVisualizacion": 5,
  "estado": "ACTIVO"
}
```

**Resultado Esperado:**
- Status: 200 OK
- Response: Plan actualizado con nuevos valores

---

### Test 3.5: Cambiar Estado del Plan (PATCH)
**Endpoint:** `PATCH /api/super-admin/planes/5/estado?estado=INACTIVO`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 200 OK
- Response: Plan con estado cambiado a INACTIVO

---

### Test 3.6: Eliminar Plan
**Endpoint:** `DELETE /api/super-admin/planes/5`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 204 No Content (sin body)
- O Status: 200 OK con mensaje de confirmación

---

## 🏢 FASE 4: PRUEBAS DE TENANTS (Registro Público)

### Test 4.1: Registro de Nueva Veterinaria
**Endpoint:** `POST /api/public/tenants/register`

**Headers:**
- `Content-Type: application/json`

**Request Body:**
```json
{
  "tenant": {
    "nombre": "Veterinaria Testing",
    "ruc": "20987654321",
    "telefono": "987654321",
    "direccion": "Av. Test 123, Lima",
    "email": "testing@vet.com"
  },
  "plan": {
    "idPlan": 1,
    "duracionMeses": 1
  },
  "adminUser": {
    "username": "admintest",
    "password": "test123",
    "email": "admintest@vet.com",
    "nombres": "Admin",
    "apellidos": "Testing"
  }
}
```

**Resultado Esperado:**
- Status: 201 Created
- Response debe contener:
  - `tenantId`: ID del tenant creado
  - `adminUsername`: "admintest"
  - `mensaje`: Confirmación de registro

**Validaciones:**
- [ ] Status code es 201
- [ ] tenantId no es null
- [ ] adminUsername es correcto
- [ ] Se puede hacer login con "admintest" / "test123"

---

## 🏢 FASE 5: PRUEBAS DE TENANTS (Super Admin)

### Test 5.1: Listar Todos los Tenants
**Endpoint:** `GET /api/super-admin/tenants`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 200 OK
- Array con al menos 2 tenants (vet-demo + testing)

---

### Test 5.2: Obtener Tenant por ID
**Endpoint:** `GET /api/super-admin/tenants/1`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 200 OK
- Objeto tenant con id=1

---

### Test 5.3: Actualizar Tenant
**Endpoint:** `PUT /api/super-admin/tenants/2`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Request Body:**
```json
{
  "nombre": "Veterinaria Testing MODIFICADA",
  "ruc": "20987654321",
  "telefono": "999888777",
  "direccion": "Av. Modificada 456, Lima",
  "email": "testing@vet.com"
}
```

**Resultado Esperado:**
- Status: 200 OK
- Tenant actualizado

---

### Test 5.4: Cambiar Estado de Tenant
**Endpoint:** `PATCH /api/super-admin/tenants/2/estado?estado=INACTIVO`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 200 OK
- Tenant con estado INACTIVO

---

### Test 5.5: Ver Suscripción del Tenant
**Endpoint:** `GET /api/super-admin/tenants/1/suscripcion`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 200 OK
- Objeto suscripción con:
  - idSuscripcion
  - idPlan
  - fechaInicio
  - fechaFin
  - estado: ACTIVO

---

### Test 5.6: Actualizar Suscripción
**Endpoint:** `PUT /api/super-admin/tenants/1/suscripcion`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Request Body:**
```json
{
  "idPlan": 2,
  "duracionMeses": 3
}
```

**Resultado Esperado:**
- Status: 200 OK
- Suscripción actualizada con nuevo plan

---

### Test 5.7: Eliminar Tenant
**Endpoint:** `DELETE /api/super-admin/tenants/2`

**Headers:**
- `Authorization: Bearer {token_super_admin}`

**Resultado Esperado:**
- Status: 204 No Content
- Tenant eliminado (CASCADE DELETE de suscripción y usuario admin)

---

## 🚫 FASE 6: PRUEBAS DE SEGURIDAD

### Test 6.1: Acceder a Endpoint de Super Admin con Token de Tenant
**Preparación:**
1. Login con usuario tenant: `POST /api/auth/tenant/login` (admin/admin123)
2. Copiar token
3. Intentar acceder a endpoint de super admin

**Endpoint:** `GET /api/super-admin/planes`

**Headers:**
- `Authorization: Bearer {token_tenant}`

**Resultado Esperado:**
- Status: 403 Forbidden
- Error: "Access Denied" o similar

---

### Test 6.2: Acceder sin Token
**Endpoint:** `GET /api/super-admin/planes`

**Headers:**
- Ninguno

**Resultado Esperado:**
- Status: 401 Unauthorized
- Error: Token requerido

---

### Test 6.3: Acceder con Token Inválido
**Endpoint:** `GET /api/super-admin/planes`

**Headers:**
- `Authorization: Bearer invalid_token_12345`

**Resultado Esperado:**
- Status: 401 Unauthorized
- Error: Token inválido

---

### Test 6.4: Acceder con Token Expirado
**Nota:** Requiere esperar a que expire el token (según configuración de JWT)

**Endpoint:** `GET /api/super-admin/planes`

**Headers:**
- `Authorization: Bearer {token_expirado}`

**Resultado Esperado:**
- Status: 401 Unauthorized
- Error: Token expirado

---

## 📋 RESUMEN DE RESULTADOS

### Endpoints Probados: 0/27

#### ✅ Funcionando:
- [ ] POST /api/auth/super-admin/login
- [ ] POST /api/auth/tenant/login
- [ ] GET /api/public/planes
- [ ] GET /api/super-admin/planes
- [ ] GET /api/super-admin/planes/{id}
- [ ] POST /api/super-admin/planes
- [ ] PUT /api/super-admin/planes/{id}
- [ ] PATCH /api/super-admin/planes/{id}/estado
- [ ] DELETE /api/super-admin/planes/{id}
- [ ] POST /api/public/tenants/register
- [ ] GET /api/super-admin/tenants
- [ ] GET /api/super-admin/tenants/{id}
- [ ] PUT /api/super-admin/tenants/{id}
- [ ] PATCH /api/super-admin/tenants/{id}/estado
- [ ] GET /api/super-admin/tenants/{id}/suscripcion
- [ ] PUT /api/super-admin/tenants/{id}/suscripcion
- [ ] DELETE /api/super-admin/tenants/{id}

#### ❌ Con Errores:
- Ninguno (pendiente de prueba)

#### ⏸️ No Probados:
- Todos (pendiente)

---

## 🎯 PRÓXIMOS PASOS

1. **INMEDIATO:** Probar Test 1.1 (Login Super Admin)
2. Verificar que el token se genere correctamente
3. Decodificar token en jwt.io
4. Probar Test 1.2 (Login Tenant)
5. Ejecutar pruebas de endpoints protegidos
6. Validar seguridad (FASE 6)
7. Documentar cualquier error encontrado

---

## 📝 NOTAS

### Cambios Implementados:
- ✅ AuthController simplificado (132 archivos compilados)
- ✅ Creado SuperAdminUserDetails para super admins
- ✅ Separados UserDetailsService para usuarios y super admins
- ✅ Autenticación manual con PasswordEncoder.matches()
- ✅ Eliminado uso de AuthenticationManager (causaba conflicto)

### Credenciales de Prueba:
- **Super Admin:** superadmin / admin123
- **Tenant Admin (vet-demo):** admin / admin123
- **Base de Datos:** veterinaria_saas @ localhost:3306

### Herramientas:
- Swagger UI: http://localhost:8080/swagger-ui.html
- JWT Decoder: https://jwt.io
- Cliente REST alternativo: test-endpoints.http (VS Code REST Client)

---

## 🐛 LOG DE ERRORES ENCONTRADOS

### Error 1: Bad Credentials (RESUELTO)
**Fecha:** 2025-12-27 17:02
**Error:** `BadCredentialsException: Bad credentials` al hacer login de super admin
**Causa:** AuthenticationManager solo usaba CustomUserDetailsService (tabla usuario), no SuperAdminUserDetailsService (tabla super_admin)
**Solución:** Reescribir AuthController para usar directamente los UserDetailsService sin AuthenticationManager, validando passwords manualmente con PasswordEncoder.matches()
**Status:** ✅ RESUELTO

---

**Última actualización:** 2025-12-27 17:10
**Tester:** Eduardo
**Versión Backend:** 0.0.1-SNAPSHOT (132 archivos compilados)
