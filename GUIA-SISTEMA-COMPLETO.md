# 🚀 GUÍA COMPLETA DEL SISTEMA NX VET

## 📋 TABLA DE CONTENIDOS
1. [Sistema de Registro](#sistema-de-registro)
2. [Acceso al Super Admin](#acceso-al-super-admin)
3. [Gestión de Planes](#gestión-de-planes)
4. [Flujo de Usuario](#flujo-de-usuario)
5. [Endpoints Importantes](#endpoints-importantes)

---

## 🎯 SISTEMA DE REGISTRO

### ¿Qué hace el registro?

Cuando una veterinaria se registra desde el landing page, el sistema automáticamente:

1. **Crea el Tenant (Veterinaria)**
   - Asigna un código único
   - Configura el plan seleccionado
   - Establece período de prueba de 30 días
   - Estado: ACTIVO
   - Suscripción: TRIAL

2. **Crea la Suscripción**
   - Fecha inicio: hoy
   - Fecha fin: +30 días
   - Estado: ACTIVA

3. **Crea el Usuario Administrador**
   - Rol: ADMIN
   - Vinculado al tenant
   - Credenciales de acceso

### Datos Requeridos para Registro

```typescript
{
  // Plan
  idPlan: 1,                              // ID del plan seleccionado
  
  // Veterinaria
  codigoTenant: "veterinaria-sanjuan",   // Generado automáticamente
  nombreComercial: "Veterinaria San Juan",
  razonSocial: "Veterinaria San Juan S.A.C.",
  ruc: "20123456789",
  telefono: "987654321",
  emailContacto: "contacto@vetsanjuan.com",
  direccion: "Av. Principal 123",
  ciudad: "Lima",
  pais: "Perú",
  
  // Administrador
  nombrePropietario: "Juan",
  apellidoPropietario: "Pérez",
  emailPropietario: "juan.perez@vetsanjuan.com",
  telefonoPropietario: "987654321",
  usernamePropietario: "admin.juan",
  passwordPropietario: "contraseña123"
}
```

### Endpoint de Registro

```
POST /api/public/tenants/register
```

**No requiere autenticación** ✅

---

## 👑 ACCESO AL SUPER ADMIN

### ¿Qué es el Super Admin?

El Super Admin es un usuario especial que tiene acceso completo al sistema y puede:
- Gestionar todas las veterinarias (tenants)
- Crear y modificar planes
- Ver estadísticas globales
- Suspender o reactivar veterinarias
- Cambiar planes de suscripción

### Credenciales de Acceso

```
URL: http://localhost:3000/auth/login
Username: superadmin
Password: [Configurar en la base de datos]
```

### Cómo Crear un Super Admin

Ejecuta este SQL en tu base de datos:

```sql
-- 1. Crear el usuario super admin
INSERT INTO super_admin (
    username,
    password_hash,
    email,
    nombres,
    apellidos,
    estado,
    fecha_creacion
) VALUES (
    'superadmin',
    '$2a$10$HASH_AQUI',  -- Usar BCrypt para hashear la password
    'admin@nxvet.com',
    'Super',
    'Administrador',
    'ACTIVO',
    NOW()
);
```

### Generar Hash de Password

Puedes usar este endpoint para generar el hash:

```
POST /api/auth/generate-hash
Body: {
  "username": "superadmin",
  "password": "tu_password_segura"
}
```

Copia el hash generado y úsalo en el SQL de arriba.

### Acceder al Dashboard de Super Admin

Una vez autenticado como super admin:

```
URL: http://localhost:3000/superadmin/dashboard
```

Verás:
- **Dashboard**: Estadísticas de todas las veterinarias
- **Veterinarias**: Lista y gestión de tenants
- **Usuarios**: Usuarios del sistema
- **Reportes**: Reportes globales
- **Configuración**: Gestión de planes y configuraciones

---

## 💰 GESTIÓN DE PLANES

### Ver Planes Actuales

El sistema carga los planes dinámicamente desde el backend:

```
GET /api/public/planes
```

### Crear Nuevo Plan (Solo Super Admin)

```
POST /api/super-admin/planes
Authorization: Bearer <token>

Body:
{
  "nombre": "Básico",
  "descripcion": "Plan básico para veterinarias pequeñas",
  "precioMensual": 29.99,
  "precioAnual": 299.99,
  "maxUsuarios": 5,
  "maxDoctores": 3,
  "maxMascotas": 100,
  "maxAlmacenamientoMb": 1024,
  "tieneReportesAvanzados": false,
  "tieneApiAcceso": false,
  "tieneSoportePrioritario": false,
  "ordenVisualizacion": 1,
  "estado": "ACTIVO"
}
```

### Actualizar Plan

```
PUT /api/super-admin/planes/{id}
Authorization: Bearer <token>
```

### Cambiar Estado de Plan

```
PATCH /api/super-admin/planes/{id}/estado
Authorization: Bearer <token>

Body: { "estado": "INACTIVO" }
```

---

## 👤 FLUJO DE USUARIO

### 1. Usuario Visita Landing Page
- URL: `http://localhost:3000`
- Ve características y precios (cargados dinámicamente)
- Puede ver los 3 tipos de planes

### 2. Usuario Se Registra
- URL: `http://localhost:3000/registro`
- Selecciona un plan
- Completa formulario
- Sistema crea: Tenant + Suscripción + Usuario Admin

### 3. Usuario Inicia Sesión
- URL: `http://localhost:3000/auth/login`
- Ingresa username y password
- Sistema detecta automáticamente si es tenant o super admin
- Redirige al dashboard correspondiente

### 4. Dashboard del Tenant
- URL: `http://localhost:3000/dashboard`
- Puede gestionar:
  - Clientes
  - Mascotas
  - Citas
  - Historias clínicas
  - Inventario
  - Ventas/Compras
  - Usuarios de su veterinaria

### 5. Ver Mi Suscripción
- URL: `http://localhost:3000/suscripcion`
- Puede ver:
  - Plan actual
  - Límites y uso actual
  - Fecha de vencimiento
  - Opción de upgrade

---

## 🔐 ENDPOINTS IMPORTANTES

### Públicos (Sin Autenticación)

```
GET  /api/public/planes              # Ver planes disponibles
POST /api/public/tenants/register    # Registrar nueva veterinaria
POST /api/auth/login                 # Login universal
```

### Tenant (Requiere Autenticación)

```
GET  /api/tenant/mi-veterinaria      # Ver datos de mi veterinaria
PUT  /api/tenant/mi-veterinaria      # Actualizar mi veterinaria
GET  /api/tenant/mi-suscripcion      # Ver mi suscripción y límites

# Gestión dentro del tenant
GET  /api/clientes                   # Listar clientes
POST /api/clientes                   # Crear cliente
GET  /api/mascotas                   # Listar mascotas
GET  /api/citas                      # Listar citas
# ... más endpoints
```

### Super Admin (Requiere SUPER_ADMIN Role)

```
# Gestión de Tenants
GET    /api/super-admin/tenants              # Listar todas las veterinarias
GET    /api/super-admin/tenants/{id}         # Ver veterinaria específica
PUT    /api/super-admin/tenants/{id}         # Actualizar veterinaria
PATCH  /api/super-admin/tenants/{id}/plan    # Cambiar plan
PATCH  /api/super-admin/tenants/{id}/suspender   # Suspender
PATCH  /api/super-admin/tenants/{id}/reactivar  # Reactivar

# Gestión de Planes
GET    /api/super-admin/planes               # Listar todos los planes
POST   /api/super-admin/planes               # Crear nuevo plan
PUT    /api/super-admin/planes/{id}          # Actualizar plan
PATCH  /api/super-admin/planes/{id}/estado   # Cambiar estado
```

---

## 🧪 TESTING

### Probar el Registro

1. Abre: `http://localhost:3000`
2. Click en "Comenzar Gratis"
3. Selecciona un plan
4. Completa el formulario
5. Click "Registrar Veterinaria"
6. Guarda las credenciales mostradas
7. Click "Ir al Login"
8. Inicia sesión con las credenciales

### Probar Super Admin

1. Crea el usuario super admin en la BD
2. Abre: `http://localhost:3000/auth/login`
3. Ingresa credenciales de super admin
4. Verás el dashboard de super admin
5. Navega a "Veterinarias" para ver todos los tenants
6. Navega a "Planes" para gestionar los planes

---

## 📊 SINCRONIZACIÓN DE PLANES

### Landing Page
- Carga planes dinámicamente desde `/api/public/planes`
- Muestra nombre, precio, descripción y características
- Resalta el plan más popular (segundo plan o "Profesional")

### Página de Registro
- Carga los mismos planes
- Usuario selecciona uno al registrarse
- Plan se asigna automáticamente al tenant

### Super Admin
- Puede crear/editar planes
- Cambios se reflejan inmediatamente en:
  - Landing page
  - Página de registro
  - Asignación de nuevos tenants

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

- [✅] Landing page carga planes dinámicamente
- [✅] Registro usa endpoint correcto del backend
- [✅] Registro crea tenant + suscripción + usuario
- [✅] Login detecta automáticamente tipo de usuario
- [✅] Super admin puede gestionar planes
- [✅] Super admin puede gestionar veterinarias
- [✅] Precios sincronizados entre frontend y backend
- [✅] Sistema multi-tenant funcional

---

## 🆘 TROUBLESHOOTING

### No aparecen los planes en el landing page
- Verifica que el backend esté corriendo
- Revisa la consola del navegador para errores
- Verifica que la URL de la API sea correcta en `.env.local`

### Error al registrar
- Verifica que todos los campos requeridos estén completos
- Revisa que el backend esté corriendo
- Verifica los logs del backend para ver el error exacto

### No puedo acceder como super admin
- Verifica que el usuario exista en la tabla `super_admin`
- Verifica que el hash de la password sea correcto
- Usa el endpoint `/api/auth/generate-hash` para generar el hash

### Los planes no se actualizan
- Recarga la página después de hacer cambios
- Verifica que el estado del plan sea "ACTIVO"
- Revisa el `ordenVisualizacion` para el orden de presentación

---

## 📝 NOTAS IMPORTANTES

1. **Códigos de Tenant**: Son únicos y se generan automáticamente del nombre comercial
2. **Período de Prueba**: Todos los tenants nuevos tienen 30 días gratis
3. **Passwords**: Siempre se hashean con BCrypt antes de guardar
4. **Multi-tenant**: Cada veterinaria tiene sus propios datos aislados
5. **Límites**: Se validan según el plan (usuarios, mascotas, etc.)

---

## 🔄 PRÓXIMOS PASOS

1. Configura los planes iniciales en el backend
2. Crea el usuario super admin
3. Prueba el flujo completo de registro
4. Personaliza los mensajes y textos según tu región
5. Configura los métodos de pago para suscripciones

---

**¡Sistema listo para producción!** 🎉
