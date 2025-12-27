# 🚀 PLAN DE TRANSFORMACIÓN A SAAS MULTI-TENANT
## Sistema de Gestión Veterinaria - Arquitectura Multi-Tenant

---

## 📋 **ÍNDICE**
1. [Visión General](#visión-general)
2. [Arquitectura Multi-Tenant](#arquitectura-multi-tenant)
3. [Cambios en la Base de Datos](#cambios-en-la-base-de-datos)
4. [Flujo de Usuario](#flujo-de-usuario)
5. [Niveles de Acceso](#niveles-de-acceso)
6. [Módulos del Sistema](#módulos-del-sistema)
7. [Plan de Implementación](#plan-de-implementación)

---

## 🎯 **VISIÓN GENERAL**

### **Objetivo**
Transformar el sistema de gestión veterinaria en un verdadero **SaaS Multi-Tenant** donde:
- Múltiples veterinarias pueden registrarse y usar el sistema
- Cada veterinaria tiene su espacio aislado y seguro
- Existe un **Super Admin** que gestiona todo el sistema
- Las veterinarias pagan por planes mensuales/anuales
- Landing page pública para captación de clientes

### **Actores del Sistema**

#### 1. **Super Administrador** 🔴
- Administra todo el sistema SaaS
- Gestiona planes y precios
- Aprueba/rechaza nuevos tenants
- Monitorea uso y facturación
- Soporte técnico nivel 1

#### 2. **Propietario de Veterinaria** 🟡
- Se registra desde la landing page
- Selecciona un plan
- Administra su veterinaria
- Gestiona usuarios de su veterinaria
- Ve reportes y estadísticas

#### 3. **Usuarios de Veterinaria** 🟢
- **Administrador**: Gestión completa de la veterinaria
- **Veterinario**: Atención clínica, recetas, historias
- **Recepcionista**: Citas, clientes, ventas
- **Asistente**: Apoyo en consultas

---

## 🏗️ **ARQUITECTURA MULTI-TENANT**

### **Modelo: Row-Level Multitenancy**
Todas las tablas de datos incluyen `id_tenant` para aislamiento lógico.

```
┌─────────────────────────────────────────────┐
│         NIVEL SISTEMA SAAS                  │
│  ┌──────────────┐  ┌───────────────────┐   │
│  │ Super Admin  │  │  Planes & Precios │   │
│  └──────────────┘  └───────────────────┘   │
└─────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────┐
│            TENANT (Veterinaria)              │
│  ┌────────────┐  ┌────────────┐             │
│  │  Usuarios  │  │  Doctores  │             │
│  └────────────┘  └────────────┘             │
│  ┌────────────┐  ┌────────────┐             │
│  │  Clientes  │  │  Mascotas  │             │
│  └────────────┘  └────────────┘             │
│  ┌────────────┐  ┌────────────┐             │
│  │   Citas    │  │  Historias │             │
│  └────────────┘  └────────────┘             │
│  ┌────────────┐  ┌────────────┐             │
│  │ Inventario │  │   Ventas   │             │
│  └────────────┘  └────────────┘             │
└─────────────────────────────────────────────┘
```

---

## 🗄️ **CAMBIOS EN LA BASE DE DATOS**

### **Nuevas Tablas Principales**

#### 1. **`super_admin`**
```sql
- Administradores del sistema SaaS
- NO pertenecen a ningún tenant
- Acceso total al sistema
```

#### 2. **`tenant`** (reemplaza parte de `veterinaria`)
```sql
- Cada veterinaria es un tenant
- Código único (subdomain: vet-demo.appvet.com)
- Configuración de suscripción
- Límites de uso por plan
- Datos del propietario
- Personalización (logo, colores)
```

#### 3. **`suscripcion`**
```sql
- Historial de planes contratados
- Fechas de inicio/fin
- Estado (ACTIVO, VENCIDO, CANCELADO)
- Método de pago
```

#### 4. **`pago`**
```sql
- Registro de pagos realizados
- Referencia de transacción
- Estado (COMPLETADO, PENDIENTE, FALLIDO)
```

### **Tablas Modificadas**

#### **Antes:**
```sql
CREATE TABLE usuario (
  id_usuario INT PRIMARY KEY,
  id_veterinaria INT,  -- FK a VETERINARIA
  ...
)
```

#### **Después:**
```sql
CREATE TABLE usuario (
  id_usuario INT PRIMARY KEY,
  id_tenant INT NOT NULL,  -- FK a TENANT
  ...
  CONSTRAINT fk_usuario_tenant
    FOREIGN KEY (id_tenant) REFERENCES tenant(id_tenant)
    ON DELETE CASCADE  -- Si se elimina tenant, se eliminan sus usuarios
)
```

**Todas las tablas de datos ahora incluyen `id_tenant`:**
- ✅ usuario
- ✅ doctor
- ✅ cliente
- ✅ mascota
- ✅ cita
- ✅ historia_clinica
- ✅ inventario
- ✅ compra
- ✅ venta
- ✅ notificacion
- ✅ auditoria

### **Tablas Compartidas (Sin `id_tenant`)**
Estas son catálogos globales:
- `plan` - Planes del SaaS
- `rol` - Roles del sistema
- `especie` - Especies de animales
- `raza` - Razas por especie
- `categoria_producto` - Categorías globales

---

## 👥 **FLUJO DE USUARIO**

### **A. Flujo de Registro (Nuevo Cliente)**

```
┌──────────────────────────────────────────────────┐
│  1. Landing Page (appvet.com)                    │
│     - Ver planes y precios                       │
│     - Comparar características                   │
│     - Testimonios y demos                        │
└──────────────────────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────┐
│  2. Formulario de Registro                       │
│     - Datos de la veterinaria                    │
│     - Datos del propietario                      │
│     - Selección de plan                          │
│     - Crear cuenta (email + password)            │
└──────────────────────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────┐
│  3. Confirmación por Email                       │
│     - Verificar email                            │
│     - Activar cuenta trial (15 días)             │
└──────────────────────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────┐
│  4. Dashboard de Veterinaria                     │
│     - Configuración inicial                      │
│     - Crear usuarios                             │
│     - Comenzar a usar                            │
└──────────────────────────────────────────────────┘
                     ↓
┌──────────────────────────────────────────────────┐
│  5. Fin del Trial                                │
│     - Notificación 3 días antes                  │
│     - Proceso de pago                            │
│     - Activación de suscripción                  │
└──────────────────────────────────────────────────┘
```

### **B. Flujo de Login**

```
┌─────────────────────────────────────┐
│  Login (/auth/login)                │
│  - Username: admin                  │
│  - Password: ********               │
└─────────────────────────────────────┘
            ↓
      ¿Es Super Admin?
       /            \
     SI              NO
      ↓               ↓
┌─────────────┐  ┌──────────────────┐
│ Panel Super │  │ ¿Tenant activo?  │
│    Admin    │  │                  │
└─────────────┘  └──────────────────┘
                        ↓
                  ┌──────────┬───────────┐
                  │          │           │
              ACTIVO     SUSPENDIDO  CANCELADO
                  ↓          ↓           ↓
          ┌──────────┐  ┌────────┐  ┌────────┐
          │Dashboard │  │Mensaje │  │Mensaje │
          │Veterinaria│  │Pago    │  │Contacto│
          └──────────┘  └────────┘  └────────┘
```

---

## 🔐 **NIVELES DE ACCESO**

### **1. Super Admin** (Fuera de tenants)
**Rutas:**
- `/super-admin/dashboard` - Panel principal
- `/super-admin/tenants` - Gestión de veterinarias
- `/super-admin/planes` - Gestión de planes
- `/super-admin/pagos` - Registro de pagos
- `/super-admin/reportes` - Estadísticas globales
- `/super-admin/soporte` - Tickets de soporte

**Permisos:**
- Ver todos los tenants
- Suspender/activar tenants
- Modificar planes
- Ver pagos y facturación
- Acceder a cualquier tenant (modo soporte)

### **2. Admin de Veterinaria** (Dentro de tenant)
**Rutas:**
- `/dashboard` - Dashboard propio
- `/veterinarias/configuracion` - Config de su veterinaria
- `/usuarios` - Gestión de usuarios
- `/planes/suscripcion` - Ver/cambiar plan
- Acceso completo a todos los módulos

**Permisos:**
- Ver solo datos de su tenant
- CRUD completo en su tenant
- Gestionar usuarios y roles
- Ver reportes de su veterinaria

### **3. Veterinario** (Dentro de tenant)
**Rutas:**
- `/dashboard` - Dashboard veterinario
- `/citas` - Gestión de citas
- `/historias` - Historias clínicas
- `/clientes` - Ver clientes
- `/mascotas` - Ver mascotas

**Permisos:**
- Ver/editar citas asignadas
- Crear historias clínicas
- Emitir recetas
- Ver clientes y mascotas

### **4. Recepcionista** (Dentro de tenant)
**Rutas:**
- `/dashboard` - Dashboard recepción
- `/citas` - Agendar citas
- `/clientes` - Gestión de clientes
- `/ventas` - Registrar ventas

**Permisos:**
- Gestión de citas
- Gestión de clientes
- Registro de ventas
- Sin acceso a historias clínicas

---

## 🧩 **MÓDULOS DEL SISTEMA**

### **A. Landing Page (Público)**
```
📍 URL: appvet.com
Componentes:
- Hero section con CTA
- Sección de planes (3-4 planes)
- Características principales
- Testimonios de clientes
- FAQ
- Footer con links legales
- Botón "Comenzar Gratis"
```

### **B. Panel Super Admin**
```
📍 URL: appvet.com/super-admin
Módulos:
1. Dashboard
   - Tenants activos/suspendidos
   - Ingresos mensuales
   - Nuevos registros
   - Tickets abiertos

2. Gestión de Tenants
   - Lista de todas las veterinarias
   - Ver detalles de cada tenant
   - Suspender/activar
   - Cambiar plan
   - Ver uso (usuarios, mascotas, storage)

3. Gestión de Planes
   - Crear/editar planes
   - Definir precios y límites
   - Activar/desactivar planes

4. Pagos y Facturación
   - Lista de pagos
   - Generar facturas
   - Reportes financieros

5. Soporte
   - Tickets de usuarios
   - Chat directo con tenants
   - Historial de interacciones
```

### **C. Panel de Veterinaria**
```
📍 URL: appvet.com/{codigo_tenant}/dashboard
Módulos:
1. Dashboard
   - Citas del día
   - Clientes nuevos
   - Ventas del día
   - Alertas de inventario

2. Citas
   - Calendario visual
   - Agendar nueva cita
   - Confirmar/cancelar
   - Estados de citas

3. Clientes y Mascotas
   - Ficha de cliente
   - Mascotas del cliente
   - Historial de consultas

4. Historias Clínicas
   - Crear historia
   - Adjuntar archivos
   - Recetar medicamentos
   - Seguimiento

5. Inventario
   - Stock actual
   - Alertas de stock mínimo
   - Compras
   - Kardex

6. Ventas
   - Registrar venta
   - Comprobantes
   - Reportes de ventas

7. Reportes
   - Dashboard analítico
   - Reportes predefinidos
   - Exportar PDF/Excel

8. Configuración
   - Datos de la veterinaria
   - Usuarios y permisos
   - Personalización
   - Suscripción y planes
```

---

## 📅 **PLAN DE IMPLEMENTACIÓN**

### **FASE 1: Base de Datos (2-3 días)** ✅
- [x] Crear nuevo schema multi-tenant
- [ ] Migrar datos existentes
- [ ] Probar integridad referencial
- [ ] Crear índices optimizados

### **FASE 2: Backend - Autenticación (3-4 días)**
- [ ] Crear entidad `SuperAdmin`
- [ ] Crear entidad `Tenant`
- [ ] Modificar `Usuario` para multi-tenant
- [ ] Implementar login diferenciado (SuperAdmin vs Usuario)
- [ ] Middleware de tenant detection
- [ ] Filtros automáticos por tenant

### **FASE 3: Backend - APIs Multi-Tenant (5-7 días)**
- [ ] Modificar todos los controllers para incluir tenant
- [ ] Validar que cada query filtre por `id_tenant`
- [ ] APIs de Super Admin
  - CRUD de tenants
  - CRUD de planes
  - Gestión de pagos
- [ ] APIs de registro público
  - Registro de nuevo tenant
  - Validación de disponibilidad
  - Activación de trial

### **FASE 4: Frontend - Landing Page (3-4 días)**
- [ ] Diseño responsive
- [ ] Hero section
- [ ] Sección de planes (pricing cards)
- [ ] Formulario de registro
- [ ] Integración con backend

### **FASE 5: Frontend - Panel Super Admin (5-6 días)**
- [ ] Layout de super admin
- [ ] Dashboard con métricas
- [ ] Gestión de tenants
- [ ] Gestión de planes
- [ ] Reportes y analytics

### **FASE 6: Frontend - Panel Veterinaria (4-5 días)**
- [ ] Modificar routing multi-tenant
- [ ] Dashboard por tenant
- [ ] Selección de plan/upgrade
- [ ] Límites visuales por plan
- [ ] Notificaciones de trial/vencimiento

### **FASE 7: Testing & Deploy (3-4 días)**
- [ ] Testing de aislamiento de datos
- [ ] Testing de roles y permisos
- [ ] Testing de facturación
- [ ] Deploy a producción
- [ ] Monitoreo inicial

---

## 🚨 **PUNTOS CRÍTICOS DE SEGURIDAD**

### **1. Aislamiento de Datos**
```java
// TODO: En cada query filtrar por tenant automáticamente
@Where(clause = "id_tenant = :tenantId")
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // Automáticamente filtra por tenant del usuario logueado
}
```

### **2. Validación de Tenant**
```java
// TODO: Middleware que valida tenant en cada request
@Component
public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ...) {
        // Obtener tenant del usuario logueado
        // Setear en ThreadLocal para queries
        // Validar que el recurso pertenece al tenant
    }
}
```

### **3. Prevención de Cross-Tenant Access**
```java
// TODO: Validar que ID de recursos pertenecen al tenant
public Cliente getCliente(Integer idCliente) {
    Cliente cliente = repo.findById(idCliente).orElseThrow();
    if (!cliente.getIdTenant().equals(getCurrentTenant())) {
        throw new UnauthorizedException();
    }
    return cliente;
}
```

---

## 📊 **MÉTRICAS A MONITOREAR**

### **Super Admin Dashboard**
- Total de tenants activos
- Ingresos mensuales recurrentes (MRR)
- Tasa de conversión trial → pago
- Churn rate (cancelaciones)
- Tickets de soporte abiertos
- Uso promedio por tenant

### **Dashboard de Veterinaria**
- Citas programadas hoy
- Clientes nuevos este mes
- Ventas del día/mes
- Mascotas activas
- % de ocupación de límites del plan
- Días restantes de suscripción

---

## 💰 **MODELO DE NEGOCIO**

### **Planes Propuestos**

| Característica | Básico | Profesional | Empresarial | Enterprise |
|----------------|--------|-------------|-------------|------------|
| **Precio/mes** | $49.99 | $99.99 | $199.99 | $399.99 |
| **Usuarios** | 3 | 10 | 30 | 100 |
| **Doctores** | 2 | 5 | 15 | 50 |
| **Mascotas** | 50 | 200 | 1000 | 5000 |
| **Storage** | 512 MB | 2 GB | 10 GB | 50 GB |
| **Reportes** | Básicos | ✅ Avanzados | ✅ Avanzados | ✅ Avanzados |
| **API Access** | ❌ | ❌ | ✅ | ✅ |
| **Soporte** | Email | Email | Prioritario | Dedicado |
| **Trial** | 15 días | 15 días | 30 días | Demo |

---

## 🎨 **PRÓXIMOS PASOS INMEDIATOS**

1. ✅ **Crear nuevo schema SQL** - COMPLETADO
2. 📝 **Ejecutar script en MySQL**
3. 🔧 **Modificar entidades del backend**
4. 🎯 **Implementar autenticación multi-tenant**
5. 🌐 **Crear landing page básica**
6. 👨‍💼 **Implementar panel super admin**

---

**¿Listo para comenzar la implementación?** 🚀
