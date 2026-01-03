# 🏥 ARQUITECTURA SISTEMA VETERINARIA SAAS

## 📊 ESTRUCTURA DE 3 VISTAS

### 1️⃣ SUPERADMIN (Vista Global)
**URL:** `/superadmin/*`
**Autenticación:** super_admin table
**Funcionalidades:**
- ✅ Gestión de TODOS los tenants/veterinarias
- ✅ Control de planes (crear, editar, activar/desactivar)
- ✅ Monitoreo de suscripciones
- ✅ Control de expiración de trials
- ✅ Estadísticas globales del sistema
- ✅ Facturación y pagos
- ✅ Suspender/Reactivar veterinarias

**Tabla Principal:** `super_admin`
**Acceso:** NO tiene `id_tenant`, puede ver TODAS las veterinarias

---

### 2️⃣ TENANT/VETERINARIA (Vista Admin de Clínica)
**URL:** `/dashboard/*`
**Autenticación:** usuario table con rol ADMIN_VET
**Funcionalidades:**
- ✅ Dashboard con estadísticas de SU clínica
- ✅ Gestión de Clientes
- ✅ Gestión de Mascotas
- ✅ Gestión de Citas
- ✅ Historias Clínicas
- ✅ Inventario (productos/medicamentos)
- ✅ Compras y Ventas
- ✅ Gestión de Doctores
- ✅ Usuarios internos
- ✅ Reportes de SU clínica
- ✅ Configuración de SU veterinaria
- ✅ Ver estado de su suscripción
- ✅ Mi Plan actual

**Tabla Principal:** `usuario` con `id_tenant` específico
**Aislamiento:** SOLO puede ver datos de su `id_tenant`
**Multi-Tenant:** Todas las tablas tienen `id_tenant` + Foreign Key

---

### 3️⃣ LANDING PAGE (Vista Pública)
**URL:** `/` y `/registro`
**Autenticación:** No requiere (público)
**Funcionalidades:**
- ✅ Landing page con información de planes
- ✅ Mostrar planes disponibles (desde tabla `plan`)
- ✅ Formulario de registro de nueva veterinaria
- ✅ Selección de plan (Básico, Pro, Enterprise)
- ✅ Crear cuenta nueva (inserta en `tenant` y primer `usuario`)
- ✅ Proceso de pago (integración futura)
- ✅ Email de confirmación

**Flujo de Registro:**
1. Usuario rellena formulario en `/registro`
2. Selecciona un plan
3. Sistema crea:
   - Nuevo registro en `tenant` (estado=TRIAL)
   - Nuevo `codigo_tenant` único
   - Primer usuario ADMIN_VET
   - Asigna `dias_trial = 14`
4. Redirige a `/auth/login`
5. Usuario inicia sesión con credenciales nuevas

---

## 🗄️ ESTRUCTURA DE BASE DE DATOS

### TABLAS GLOBALES (Sin id_tenant)
```
✅ plan - Planes disponibles para contratar
✅ super_admin - Administradores del sistema
✅ rol - Roles de usuarios
✅ especie - Especies de mascotas (Perro, Gato, etc)
✅ raza - Razas por especie
✅ categoria_producto - Categorías de productos
✅ producto - Catálogo global de productos
✅ proveedor - Proveedores de productos
```

### TABLAS MULTI-TENANT (Con id_tenant)
```
✅ tenant - Veterinarias registradas
✅ suscripcion - Historial de suscripciones
✅ pago - Pagos realizados
✅ usuario - Usuarios por veterinaria
✅ doctor - Doctores por veterinaria
✅ cliente - Clientes por veterinaria
✅ mascota - Mascotas por veterinaria
✅ cita - Citas por veterinaria
✅ historia_clinica - Historias clínicas
✅ inventario - Stock por veterinaria
✅ compra - Compras por veterinaria
✅ venta - Ventas por veterinaria
✅ detalle_compra
✅ detalle_venta
✅ refresh_token
```

---

## 🔐 SISTEMA DE AUTENTICACIÓN

### SuperAdmin
```java
- Tabla: super_admin
- No tiene id_tenant
- Puede acceder a TODOS los datos
- Username único global
- Roles: Solo SUPER_ADMIN
```

### Usuario Tenant
```java
- Tabla: usuario
- TIENE id_tenant obligatorio
- Solo accede a datos de SU tenant
- Username único POR TENANT (uk_tenant_username)
- Roles: ADMIN_VET, DOCTOR, RECEPCIONISTA, etc
```

### Flujo Login
```
1. POST /api/auth/login { username, password }
2. Backend busca en super_admin PRIMERO
   - Si existe → Token con role SUPER_ADMIN, sin tenantId
3. Si no existe, busca en usuario
   - Si existe → Token con role del usuario, CON tenantId
4. Frontend recibe token JWT con:
   - userId
   - username  
   - role
   - tenantId (null para superadmin)
5. Frontend redirige según role:
   - SUPER_ADMIN → /superadmin/dashboard
   - ADMIN_VET/DOCTOR → /dashboard
```

---

## 🛡️ FILTRADO MULTI-TENANT

### En el Backend (Spring Security)
```java
// SecurityUtils.java
public static Integer getTenantId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getPrincipal() instanceof UserDetails userDetails) {
        return userDetails.getTenantId(); // null para superadmin
    }
    return null;
}

// En cada servicio
@Service
public class ClienteService {
    public List<Cliente> findAll() {
        Integer tenantId = SecurityUtils.getTenantId();
        if (tenantId == null) {
            // SuperAdmin - ver todos
            return clienteRepo.findAll();
        }
        // Usuario normal - solo su tenant
        return clienteRepo.findByTenantIdTenant(tenantId);
    }
}
```

### En los Repositorios
```java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // Para usuarios normales
    List<Cliente> findByTenantIdTenant(Integer tenantId);
    
    // Para superadmin
    @Query("SELECT c FROM Cliente c")
    List<Cliente> findAll();
}
```

---

## 📦 ENTIDADES CRÍTICAS

### Tenant.java
```java
@Entity
@Table(name = "tenant")
public class Tenant {
    @Id
    @Column(name = "id_tenant")
    private Integer idTenant;
    
    @Column(name = "codigo_tenant") // ⚠️ NO tenant_code
    private String codigoTenant;
    
    @Column(name = "email_contacto") // ⚠️ NO email
    private String emailContacto;
    
    @Column(name = "dias_trial") // ⚠️ NO dias_prueba_restantes
    private Integer diasTrial;
    
    @Column(name = "estado_suscripcion") // ⚠️ Obligatorio
    private String estadoSuscripcion;
    
    // Nuevos campos agregados
    @Column(name = "nombre_propietario")
    @Column(name = "email_propietario")
    @Column(name = "telefono_propietario")
    @Column(name = "usuarios_activos")
    @Column(name = "doctores_activos")
    @Column(name = "mascotas_registradas")
    @Column(name = "almacenamiento_usado_mb")
    @Column(name = "fecha_activacion")
    @Column(name = "fecha_suspension")
}
```

### Cita.java
```java
@Column(name = "id_cliente") // ⚠️ Falta en SQL actual
@Column(name = "duracion_minutos") // ⚠️ Falta en SQL
```

### Cliente.java  
```java
@Column(name = "tipo_documento") // ⚠️ NO tipo_doc
@Column(name = "numero_documento") // ⚠️ NO num_documento
```

### Mascota.java
```java
@Column(name = "id_raza") // OK
@Column(name = "peso_kg") // OK
@Column(name = "foto_url") // ⚠️ NO fotoUrl
```

### HistoriaClinica.java
```java
@Column(name = "fecha_atencion") // ⚠️ NO fecha
@Column(name = "motivo_consulta") // OK
@Column(name = "examen_fisico") // ⚠️ Falta en SQL
@Column(name = "examenes_solicitados") // ⚠️ NO examenes_realizados
@Column(name = "proxima_cita") // ⚠️ Falta en SQL
```

---

## ❌ PROBLEMAS ENCONTRADOS EN SQL

### 1. Tabla CITA
```sql
-- ❌ FALTA: id_cliente
-- ❌ FALTA: duracion_minutos
-- ❌ ESTADO INCORRECTO: 'PROGRAMADA' debe ser 'PENDIENTE'
-- ❌ ESTADO INCORRECTO: 'COMPLETADA' debe ser 'ATENDIDA'
```

### 2. Tabla CLIENTE
```sql
-- ❌ SQL tiene: tipo_documento ENUM
-- ✅ Java tiene: tipo_documento String (default "DNI")
-- ❌ SQL tiene: num_documento
-- ✅ Java tiene: numero_documento
```

### 3. Tabla MASCOTA
```sql
-- ❌ SQL tiene: id_especie (separado de raza)
-- ✅ Java solo tiene: id_raza (Raza ya tiene especie)
-- ❌ SQL tiene: altura_cm, esterilizado
-- ✅ Java NO tiene esos campos
```

### 4. Tabla HISTORIA_CLINICA
```sql
-- ❌ SQL tiene: fecha
-- ✅ Java tiene: fecha_atencion
-- ❌ SQL tiene: sintomas
-- ✅ Java tiene: anamnesis
-- ❌ SQL tiene: examenes_realizados
-- ✅ Java tiene: examenes_solicitados
-- ❌ SQL FALTA: examen_fisico
-- ❌ SQL FALTA: proxima_cita
-- ❌ SQL tiene: peso_kg, temperatura_c
-- ✅ Java NO tiene esos campos ahí
```

### 5. Tabla ESPECIE
```sql
-- ❌ SQL tiene: estado ENUM
-- ✅ Java NO tiene campo estado
```

### 6. Tabla RAZA
```sql
-- ❌ SQL tiene: tamano_promedio, peso_promedio_kg, estado
-- ✅ Java solo tiene: nombre, descripcion
```

### 7. Tabla PRODUCTO
```sql
-- ❌ SQL tiene: codigo, unidad_medida, precio_referencia
-- ✅ Java tiene: nombre, descripcion, esMedicamento, precioUnitario, estado
```

### 8. Tabla PROVEEDOR
```sql
-- ✅ Java tiene: Estado estado (enum)
-- ❌ Debe ser VARCHAR o ENUM en SQL
```

### 9. Tabla COMPRA
```sql
-- ❌ SQL FALTA toda la tabla
```

### 10. Tabla VENTA
```sql
-- ❌ SQL FALTA: id_cliente
-- ❌ SQL FALTA: metodo_pago
```

---

## ✅ PLAN DE CORRECCIÓN

1. ✅ Corregir tabla `tenant` - HECHO
2. ⏳ Corregir tabla `cita` - Agregar id_cliente, duracion_minutos, fix estados
3. ⏳ Corregir tabla `cliente` - numero_documento
4. ⏳ Corregir tabla `mascota` - Quitar id_especie, altura_cm, esterilizado
5. ⏳ Corregir tabla `historia_clinica` - fecha_atencion, anamnesis, examen_fisico, etc
6. ⏳ Corregir tabla `especie` - Quitar estado
7. ⏳ Corregir tabla `raza` - Quitar campos extra
8. ⏳ Corregir tabla `producto` - Ajustar campos
9. ⏳ Crear tabla `compra` correctamente
10. ⏳ Corregir tabla `venta` - Agregar id_cliente, metodo_pago

---

## 🚀 SIGUIENTE PASO

Aplicar TODAS las correcciones al archivo `SETUP-DATABASE.sql` para que coincida **EXACTAMENTE** con las entidades Java.
