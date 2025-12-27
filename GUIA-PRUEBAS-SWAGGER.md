# 📋 Guía de Pruebas con Swagger UI - VetSaaS

## 🎯 Objetivo
Probar sistemáticamente todos los endpoints del sistema multi-tenant usando Swagger UI para validar:
- ✅ Seguridad JWT
- ✅ Aislamiento entre tenants
- ✅ Funcionalidad de registro público
- ✅ Gestión de super administrador

---

## 🚀 Paso 1: Acceder a Swagger UI

1. **Iniciar el servidor** (si no está corriendo):
   ```bash
   cd c:\Users\Eduardo\APP-VET\app
   .\mvnw.cmd spring-boot:run
   ```

2. **Abrir Swagger UI en el navegador**:
   ```
   http://localhost:8080/swagger-ui.html
   ```
   
   O también puede funcionar:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

3. **Verificar que aparezcan las siguientes secciones**:
   - 🏥 **Planes** - Gestión de planes de suscripción
   - 🏢 **Tenants (Veterinarias)** - Registro y gestión de veterinarias
   - 👤 **Usuarios, Mascotas, Citas, etc.**

---

## 📌 Fase 1: Endpoints Públicos (Sin Autenticación)

### ✅ 1.1 Obtener Planes Activos

**Endpoint**: `GET /api/public/planes`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. Click en "Execute"

**Resultado esperado**:
```json
[
  {
    "idPlan": 1,
    "nombrePlan": "Básico",
    "descripcion": "Plan inicial para veterinarias pequeñas",
    "precioMensual": 29.99,
    "precioAnual": 299.99,
    "maxUsuarios": 3,
    "maxDoctores": 2,
    "maxMascotas": 100,
    "estado": "ACTIVO"
  },
  // ... 3 planes más
]
```

**Validación**: ✅ Debe retornar 4 planes ordenados (Básico, Profesional, Empresarial, Enterprise)

---

### ✅ 1.2 Registrar Nueva Veterinaria

**Endpoint**: `POST /api/public/tenants/register`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. Pegar el siguiente JSON en el Request body:

```json
{
  "codigoTenant": "vet-prueba-2025",
  "nombreComercial": "Veterinaria Mascota Feliz",
  "razonSocial": "Mascota Feliz S.A.C.",
  "ruc": "20123456789",
  "telefono": "+51 987654321",
  "email": "contacto@mascotafeliz.com",
  "direccion": "Av. Los Perros 456",
  "ciudad": "Lima",
  "pais": "Perú",
  "idPlan": 2,
  "nombresOwner": "María",
  "apellidosOwner": "García Rodríguez",
  "emailOwner": "maria.garcia@mascotafeliz.com",
  "telefonoOwner": "+51 987654322",
  "usernameAdmin": "admin.maria",
  "passwordAdmin": "Maria2025!"
}
```

4. Click en "Execute"

**Resultado esperado**:
- **Status**: `201 Created`
- **Response**: Objeto TenantDTO con el tenant creado
- **Verificar que incluya**:
  ```json
  {
    "idTenant": 2,
    "codigoTenant": "vet-prueba-2025",
    "nombreComercial": "Veterinaria Mascota Feliz",
    "estadoSuscripcion": "TRIAL",
    "usuariosActivos": 1,
    "planNombre": "Profesional",
    "maxUsuarios": 5
  }
  ```

**Validación**: 
- ✅ Estado 201 Created
- ✅ estadoSuscripcion = "TRIAL"
- ✅ usuariosActivos = 1 (el admin creado)
- ✅ planActual corresponde al plan seleccionado

---

## 🔐 Fase 2: Autenticación Super Admin

### ✅ 2.1 Login como Super Admin

**Endpoint**: `POST /api/auth/super-admin/login`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. Pegar el siguiente JSON:

```json
{
  "username": "superadmin",
  "password": "admin123"
}
```

4. Click en "Execute"

**Resultado esperado**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyVHlwZSI6IlNVUEVSX0FETUlOIiwic3ViIjoic3VwZXJhZG1pbiIsImlhdCI6MTcwMzY3MjQwMCwiZXhwIjoxNzAzNjc2MDAwfQ...",
  "type": "Bearer",
  "username": "superadmin",
  "roles": ["ROLE_SUPER_ADMIN"]
}
```

**Acción importante**:
1. **Copiar el valor del campo `token`** (sin las comillas)
2. **Click en el botón "Authorize" 🔓** (arriba a la derecha en Swagger)
3. En el modal que aparece:
   - **Value**: Pegar `Bearer {tu-token-aquí}`
   - Ejemplo: `Bearer eyJhbGciOiJIUzI1NiJ9...`
4. Click en "Authorize"
5. Click en "Close"

**Validación**: 
- ✅ El candado 🔒 ahora debe estar cerrado
- ✅ Los endpoints protegidos ahora deben funcionar

---

## 🛡️ Fase 3: Endpoints Protegidos Super Admin

### ✅ 3.1 Listar Todos los Planes (Incluyendo Inactivos)

**Endpoint**: `GET /api/super-admin/planes`

**Acción**:
1. Verificar que el token esté autorizado (candado cerrado 🔒)
2. Click en el endpoint
3. Click en "Try it out"
4. Click en "Execute"

**Resultado esperado**:
- **Status**: `200 OK`
- **Response**: Lista con todos los planes (activos e inactivos)

**Validación**: ✅ Debe incluir los 4 planes del sistema

---

### ✅ 3.2 Listar Todas las Veterinarias

**Endpoint**: `GET /api/super-admin/tenants`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. Click en "Execute"

**Resultado esperado**:
```json
[
  {
    "idTenant": 1,
    "codigoTenant": "vet-demo",
    "nombreComercial": "Veterinaria Demo",
    "estadoSuscripcion": "ACTIVO",
    "usuariosActivos": 1,
    "doctoresActivos": 0,
    "mascotasRegistradas": 0
  },
  {
    "idTenant": 2,
    "codigoTenant": "vet-prueba-2025",
    "nombreComercial": "Veterinaria Mascota Feliz",
    "estadoSuscripcion": "TRIAL",
    "usuariosActivos": 1,
    "doctoresActivos": 0,
    "mascotasRegistradas": 0
  }
]
```

**Validación**: 
- ✅ Debe mostrar "vet-demo" (pre-existente)
- ✅ Debe mostrar "vet-prueba-2025" (recién creado)

---

### ✅ 3.3 Obtener Detalles de una Veterinaria

**Endpoint**: `GET /api/super-admin/tenants/{id}`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. En el campo `id`, ingresar: `2`
4. Click en "Execute"

**Resultado esperado**:
- **Status**: `200 OK`
- **Response**: Detalles completos de "Veterinaria Mascota Feliz"
  ```json
  {
    "idTenant": 2,
    "codigoTenant": "vet-prueba-2025",
    "nombreComercial": "Veterinaria Mascota Feliz",
    "razonSocial": "Mascota Feliz S.A.C.",
    "ruc": "20123456789",
    "email": "contacto@mascotafeliz.com",
    "planNombre": "Profesional",
    "maxUsuarios": 5,
    "usuariosActivos": 1
  }
  ```

---

### ✅ 3.4 Crear un Nuevo Plan

**Endpoint**: `POST /api/super-admin/planes`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. Pegar el siguiente JSON:

```json
{
  "nombrePlan": "Plan Premium VIP",
  "descripcion": "Plan exclusivo para clínicas veterinarias grandes",
  "precioMensual": 199.99,
  "precioAnual": 1999.99,
  "maxUsuarios": 20,
  "maxDoctores": 15,
  "maxMascotas": 5000,
  "maxAlmacenamientoMb": 50000,
  "tieneReportesAvanzados": true,
  "tieneApiAcceso": true,
  "tieneSoportePrioritario": true,
  "estado": "ACTIVO",
  "ordenVisualizacion": 5,
  "diasTrial": 30
}
```

4. Click en "Execute"

**Resultado esperado**:
- **Status**: `200 OK`
- **Response**: Plan creado con `idPlan: 5`

---

### ✅ 3.5 Suspender una Veterinaria

**Endpoint**: `PATCH /api/super-admin/tenants/{id}/suspender`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. En el campo `id`, ingresar: `2`
4. Click en "Execute"

**Resultado esperado**:
- **Status**: `200 OK`

**Verificación**:
1. Volver a ejecutar `GET /api/super-admin/tenants/2`
2. Verificar que `estadoSuscripcion` cambió a `"SUSPENDIDO"`

---

### ✅ 3.6 Reactivar una Veterinaria

**Endpoint**: `PATCH /api/super-admin/tenants/{id}/reactivar`

**Acción**:
1. Click en el endpoint
2. Click en "Try it out"
3. En el campo `id`, ingresar: `2`
4. Click en "Execute"

**Resultado esperado**:
- **Status**: `200 OK`
- `estadoSuscripcion` vuelve a `"TRIAL"`

---

## 👤 Fase 4: Autenticación Tenant (Usuario de Veterinaria)

### ✅ 4.1 Login como Usuario Tenant

**Endpoint**: `POST /api/auth/tenant/login`

**Acción**:
1. **Primero, cerrar la sesión del super admin**:
   - Click en el botón "Authorize" 🔓
   - Click en "Logout"
   - Click en "Close"

2. Ir al endpoint `POST /api/auth/tenant/login`
3. Click en "Try it out"
4. Pegar el siguiente JSON:

```json
{
  "username": "admin.maria",
  "password": "Maria2025!"
}
```

5. Click en "Execute"

**Resultado esperado**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ0ZW5hbnRJZCI6MiwidXNlclR5cGUiOiJVU1VBUklPIiwic3ViIjoiYWRtaW4ubWFyaWEiLCJpYXQiOjE3MDM2NzI0MDAsImV4cCI6MTcwMzY3NjAwMH0...",
  "type": "Bearer",
  "username": "admin.maria",
  "refreshToken": "..."
}
```

**Validación JWT**:
1. Copiar el token
2. Ir a https://jwt.io
3. Pegar el token en el campo "Encoded"
4. En "Decoded" verificar que tenga:
   ```json
   {
     "tenantId": 2,
     "userType": "USUARIO",
     "sub": "admin.maria",
     "iat": ...,
     "exp": ...
   }
   ```

**Acción importante**:
1. Autorizar con este nuevo token en Swagger (mismo proceso que con super admin)
2. Pegar: `Bearer {token-del-tenant}`

---

### ✅ 4.2 Verificar Aislamiento de Datos

**Prueba de Seguridad - Intentar Acceder a Endpoint Super Admin**:

**Endpoint**: `GET /api/super-admin/tenants`

**Acción**:
1. Con el token del tenant autorizado
2. Click en el endpoint
3. Click en "Try it out"
4. Click en "Execute"

**Resultado esperado**:
- **Status**: `403 Forbidden`
- **Mensaje**: "Access Denied" o similar

**Validación**: ✅ Un usuario tenant NO puede acceder a endpoints de super admin

---

## 📊 Resumen de Pruebas

| # | Endpoint | Método | Autenticación | Estado Esperado |
|---|----------|--------|---------------|-----------------|
| 1 | `/api/public/planes` | GET | ❌ No | 200 OK |
| 2 | `/api/public/tenants/register` | POST | ❌ No | 201 Created |
| 3 | `/api/auth/super-admin/login` | POST | ❌ No | 200 OK |
| 4 | `/api/super-admin/planes` | GET | ✅ Super Admin | 200 OK |
| 5 | `/api/super-admin/tenants` | GET | ✅ Super Admin | 200 OK |
| 6 | `/api/super-admin/tenants/{id}` | GET | ✅ Super Admin | 200 OK |
| 7 | `/api/super-admin/planes` | POST | ✅ Super Admin | 200 OK |
| 8 | `/api/super-admin/tenants/{id}/suspender` | PATCH | ✅ Super Admin | 200 OK |
| 9 | `/api/super-admin/tenants/{id}/reactivar` | PATCH | ✅ Super Admin | 200 OK |
| 10 | `/api/auth/tenant/login` | POST | ❌ No | 200 OK |
| 11 | `/api/super-admin/tenants` | GET | ❌ Tenant (debe fallar) | 403 Forbidden |

---

## 🐛 Solución de Problemas

### Error 401 Unauthorized en endpoints protegidos
- ✅ Verificar que el token esté autorizado (candado cerrado 🔒)
- ✅ Verificar que el formato sea: `Bearer {token}`
- ✅ Verificar que el token no haya expirado (duración: 1 hora)

### Error 403 Forbidden
- ✅ Verificar que estés usando el token correcto (super admin vs tenant)
- ✅ Verificar que el usuario tenga el rol adecuado

### Error 500 Internal Server Error
- ✅ Revisar la consola del servidor para ver el stack trace
- ✅ Verificar que la base de datos esté corriendo
- ✅ Verificar que los datos de entrada sean válidos

### Swagger no carga
- ✅ Verificar que el servidor esté corriendo en http://localhost:8080
- ✅ Probar con: http://localhost:8080/swagger-ui/index.html
- ✅ Verificar que no haya errores de compilación

---

## 📝 Notas Finales

- **Tokens JWT expiran en 1 hora** - Si un endpoint retorna 401, re-autenticarse
- **El tenant "vet-demo" ya existe** en la BD con id_tenant=1
- **Cada tenant está aislado** - Solo puede ver sus propios datos
- **Super Admin ve todo** - Puede gestionar todos los tenants

---

## ✅ Checklist de Pruebas Completas

- [ ] Endpoints públicos funcionan sin autenticación
- [ ] Registro de tenant crea: tenant + suscripción + usuario admin
- [ ] Login super admin retorna token con `userType: "SUPER_ADMIN"`
- [ ] Login tenant retorna token con `tenantId` y `userType: "USUARIO"`
- [ ] Token super admin puede acceder a `/api/super-admin/**`
- [ ] Token tenant NO puede acceder a `/api/super-admin/**` (403 Forbidden)
- [ ] Suspender tenant cambia estado a "SUSPENDIDO"
- [ ] Reactivar tenant restaura estado anterior
- [ ] Crear plan funciona correctamente
- [ ] JWT decodificado muestra claims correctos (tenantId, userType)

---

**¡Listo para comenzar las pruebas! 🚀**

Una vez completadas todas las pruebas, procederemos a:
1. **Frontend**: Landing page con pricing
2. **Dashboard Super Admin**: Gestión de veterinarias
3. **Dashboard Tenant**: Espacio de cada veterinaria
