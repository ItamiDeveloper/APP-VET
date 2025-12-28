# 🎯 PLAN DE TRABAJO - SISTEMA VETERINARIO SAAS
**Fecha:** 27 de Diciembre, 2025  
**Estado del Sistema:** Backend funcionando ✅ | Frontend funcionando ✅

---

## 📊 ESTADO ACTUAL

### ✅ COMPLETADO
1. **Backend Spring Boot 3.5.8**
   - ✅ 26 Repositorios JPA funcionando
   - ✅ Autenticación JWT implementada
   - ✅ Multi-tenant con TenantContext
   - ✅ 19 Mappers corregidos (@Component)
   - ✅ Login universal (/api/auth/login)
   - ✅ Módulo de Estadísticas completo
   - ✅ Backend corriendo en puerto 8080

2. **Frontend Next.js**
   - ✅ Estructura base de la aplicación
   - ✅ Autenticación funcionando
   - ✅ Dashboard con estadísticas (recién corregido)
   - ✅ Módulos básicos creados (Citas, Clientes, Mascotas, etc.)

3. **Base de Datos**
   - ✅ Schema multi-tenant diseñado
   - ✅ Tablas de tenant, plan, usuario, cliente, mascota, cita, etc.
   - ✅ Relaciones y constraints definidos

### ⚠️ WARNINGS (No críticos)
- Warnings de null safety en varios servicios (no impiden funcionamiento)
- Deprecación de DaoAuthenticationProvider (actualizar en futuro)

---

## 🚀 PLAN DE TRABAJO PRIORITARIO

### **FASE 1: VALIDACIÓN DEL SISTEMA BASE** (1-2 días)
**Objetivo:** Asegurar que todos los módulos del tenant funcionen correctamente

#### ✅ **1.1 Dashboard y Estadísticas**
- [ ] Iniciar frontend (`npm run dev` en nx-vet)
- [ ] Hacer login en http://localhost:3000
- [ ] Verificar que dashboard carga sin errores 500
- [ ] Verificar que se muestran:
  - Total de clientes, mascotas, citas, ingresos
  - Gráfico de ingresos mensuales
  - Gráfico de citas por estado
  - Distribución de mascotas por cliente
  - Actividad reciente

#### 📋 **1.2 Módulo de Clientes**
- [ ] Listar todos los clientes del tenant
- [ ] Crear nuevo cliente
- [ ] Editar cliente existente
- [ ] Ver detalles de cliente
- [ ] Buscar/filtrar clientes
- [ ] Validar que solo ve clientes de su tenant

#### 🐕 **1.3 Módulo de Mascotas**
- [ ] Listar todas las mascotas del tenant
- [ ] Crear nueva mascota (asociada a cliente)
- [ ] Editar mascota existente
- [ ] Ver detalles de mascota
- [ ] Buscar/filtrar mascotas
- [ ] Validar relación con cliente correcto

#### 📅 **1.4 Módulo de Citas**
- [ ] Listar todas las citas del tenant
- [ ] Crear nueva cita (seleccionar mascota, doctor, fecha)
- [ ] Editar cita existente
- [ ] Cambiar estado de cita (PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA)
- [ ] Ver calendario de citas
- [ ] Validar que doctores sean del mismo tenant

#### 🏥 **1.5 Módulo de Historias Clínicas**
- [ ] Listar historias clínicas
- [ ] Crear nueva historia (asociada a cita/mascota)
- [ ] Ver detalles de historia clínica
- [ ] Agregar diagnóstico, tratamiento, recetas
- [ ] Adjuntar archivos (si aplica)

#### 📦 **1.6 Módulo de Inventario**
- [ ] Listar productos del inventario
- [ ] Crear nuevo producto
- [ ] Editar producto existente
- [ ] Registrar entrada de stock
- [ ] Registrar salida de stock
- [ ] Ver alertas de stock mínimo
- [ ] Validar kardex

#### 💰 **1.7 Módulo de Ventas**
- [ ] Listar ventas del tenant
- [ ] Crear nueva venta
- [ ] Ver detalle de venta
- [ ] Generar comprobante
- [ ] Validar actualización de inventario
- [ ] Ver reportes de ventas

#### 👥 **1.8 Módulo de Usuarios**
- [ ] Listar usuarios del tenant
- [ ] Crear nuevo usuario
- [ ] Editar usuario existente
- [ ] Asignar roles
- [ ] Desactivar usuario
- [ ] Validar permisos por rol

---

### **FASE 2: ARQUITECTURA SAAS MULTI-TENANT** (3-5 días)
**Objetivo:** Implementar las 3 aplicaciones del sistema SaaS

#### 🌐 **2.1 Landing Page Pública** (1-2 días)
Crear aplicación Next.js separada: `landing-page`

**Funcionalidades:**
- [ ] Diseño de página principal (Hero, Features, Pricing)
- [ ] Sección de planes con precios
  - Básico: $49.99/mes
  - Profesional: $99.99/mes
  - Empresarial: $199.99/mes
  - Enterprise: $399.99/mes
- [ ] Comparación de características
- [ ] Testimonios (puede ser mock inicial)
- [ ] FAQ
- [ ] Footer con links legales
- [ ] Botón "Comenzar Gratis" / "Registrar mi Veterinaria"
- [ ] Formulario de registro de nuevo tenant

**Tecnologías:**
- Next.js 14 (App Router)
- Tailwind CSS
- Shadcn/ui o Chakra UI
- React Hook Form

#### 👨‍💼 **2.2 Panel Super Admin** (2-3 días)
Crear aplicación Next.js separada: `admin-portal`

**Funcionalidades:**
- [ ] Login exclusivo para super admin
- [ ] Dashboard con métricas globales:
  - Total de tenants activos/suspendidos/cancelados
  - Ingresos mensuales recurrentes (MRR)
  - Nuevos registros del mes
  - Gráficos de crecimiento
- [ ] **Gestión de Tenants:**
  - [ ] Listar todos los tenants
  - [ ] Ver detalles de cada tenant
  - [ ] Suspender/activar tenant
  - [ ] Cambiar plan de tenant
  - [ ] Ver uso de recursos (usuarios, mascotas, storage)
  - [ ] Acceder a tenant (modo soporte)
- [ ] **Gestión de Planes:**
  - [ ] CRUD de planes
  - [ ] Definir características por plan
  - [ ] Establecer precios
  - [ ] Activar/desactivar planes
- [ ] **Gestión de Pagos:**
  - [ ] Listar todos los pagos
  - [ ] Ver detalles de pago
  - [ ] Estados de pago
  - [ ] Reportes financieros
- [ ] **Soporte:**
  - [ ] Sistema de tickets (básico)
  - [ ] Historial de interacciones con tenants

**Puerto sugerido:** 3001

#### 🏥 **2.3 App de Tenant (Actual)** (Mejoras)
Mejorar aplicación actual: `nx-vet`

**Funcionalidades adicionales:**
- [ ] Módulo de Perfil de Veterinaria
  - [ ] Editar datos de la veterinaria
  - [ ] Personalización (logo, colores)
  - [ ] Ver información de suscripción
- [ ] Módulo de Suscripción
  - [ ] Ver plan actual
  - [ ] Ver días restantes
  - [ ] Upgrade/downgrade de plan
  - [ ] Historial de pagos
  - [ ] Facturación
- [ ] Notificaciones
  - [ ] Alerta de vencimiento de trial
  - [ ] Alerta de pago pendiente
  - [ ] Notificación de límites del plan
- [ ] Límites visuales por plan
  - [ ] Mostrar uso actual vs límites
  - [ ] Bloquear acciones si excede límite
  - [ ] Sugerencia de upgrade

**Puerto actual:** 3000

---

### **FASE 3: BACKEND - APIs MULTI-TENANT** (2-3 días)

#### 🔐 **3.1 Super Admin Backend**
- [ ] Crear entidad SuperAdmin
- [ ] Crear SuperAdminRepository
- [ ] Crear SuperAdminService
- [ ] Crear SuperAdminController
- [ ] Login diferenciado para super admin
- [ ] JWT con claim de tipo de usuario (SUPER_ADMIN vs TENANT_USER)

#### 🏢 **3.2 Gestión de Tenants (Backend)**
- [ ] Endpoint: GET /super-admin/tenants (listar todos)
- [ ] Endpoint: GET /super-admin/tenants/{id} (detalle)
- [ ] Endpoint: POST /super-admin/tenants (crear - registro)
- [ ] Endpoint: PUT /super-admin/tenants/{id} (actualizar)
- [ ] Endpoint: PUT /super-admin/tenants/{id}/suspend (suspender)
- [ ] Endpoint: PUT /super-admin/tenants/{id}/activate (activar)
- [ ] Endpoint: DELETE /super-admin/tenants/{id} (eliminar)
- [ ] Endpoint: GET /super-admin/tenants/{id}/stats (uso de recursos)

#### 💳 **3.3 Gestión de Suscripciones**
- [ ] Crear entidad Suscripcion
- [ ] Crear SuscripcionRepository
- [ ] Crear SuscripcionService
- [ ] Endpoint: GET /tenant/suscripcion (ver suscripción actual)
- [ ] Endpoint: POST /tenant/suscripcion/upgrade (cambiar plan)
- [ ] Endpoint: GET /tenant/suscripcion/historial (historial de pagos)

#### 💰 **3.4 Gestión de Pagos**
- [ ] Crear entidad Pago
- [ ] Crear PagoRepository
- [ ] Crear PagoService
- [ ] Endpoint: POST /pagos (registrar pago)
- [ ] Endpoint: GET /pagos (listar pagos del tenant)
- [ ] Endpoint: GET /super-admin/pagos (todos los pagos - super admin)

#### 📝 **3.5 Registro Público**
- [ ] Endpoint: POST /public/registro (registro de nuevo tenant)
  - Validar datos
  - Crear tenant
  - Crear usuario propietario
  - Asignar plan trial
  - Enviar email de confirmación (mock)
- [ ] Endpoint: GET /public/planes (listar planes disponibles)
- [ ] Endpoint: GET /public/check-disponibilidad (validar código de tenant)

#### 🔒 **3.6 Middleware y Seguridad**
- [ ] TenantFilter: Validar tenant en cada request
- [ ] SuperAdminFilter: Validar acceso de super admin
- [ ] Validar límites de plan antes de operaciones
- [ ] Logging de operaciones críticas
- [ ] Auditoría de accesos

---

### **FASE 4: TESTING Y VALIDACIÓN** (2 días)

#### 🧪 **4.1 Testing de Aislamiento**
- [ ] Crear 3 tenants de prueba
- [ ] Verificar que cada tenant solo ve sus datos
- [ ] Intentar acceso cross-tenant (debe fallar)
- [ ] Validar filtros automáticos por tenant

#### 🔐 **4.2 Testing de Roles y Permisos**
- [ ] Crear usuarios con diferentes roles
- [ ] Validar permisos de cada rol
- [ ] Verificar que super admin puede acceder a todo
- [ ] Verificar restricciones por plan

#### 💳 **4.3 Testing de Facturación**
- [ ] Simular registro de nuevo tenant
- [ ] Simular trial expirando
- [ ] Simular upgrade de plan
- [ ] Simular suspensión por falta de pago
- [ ] Validar límites de plan

#### 🚀 **4.4 Testing de Performance**
- [ ] Cargar 100+ registros por entidad
- [ ] Medir tiempo de respuesta
- [ ] Verificar índices de base de datos
- [ ] Optimizar queries lentas

---

## 📝 DECISIONES TÉCNICAS A TOMAR

### 1. **Estructura de Proyectos**
**Opción A (Recomendada):** 3 proyectos separados
```
APP-VET/
  ├── backend/           (Spring Boot - Puerto 8080)
  ├── landing-page/      (Next.js - Puerto 3002)
  ├── admin-portal/      (Next.js - Puerto 3001)
  └── tenant-app/        (Next.js - Puerto 3000 - actual nx-vet)
```

**Opción B:** Monorepo con Nx o Turborepo
```
APP-VET/
  ├── apps/
  │   ├── backend/
  │   ├── landing/
  │   ├── admin/
  │   └── tenant/
  └── packages/
      ├── ui/
      └── shared/
```

### 2. **Autenticación Multi-Dominio**
- Landing page: Sin autenticación
- Admin portal: SuperAdmin (JWT diferente)
- Tenant app: Usuarios de tenant (JWT con tenantId)

### 3. **Base de Datos**
- ¿Mantener schema actual o crear nuevo?
- ¿Migrar datos existentes o empezar limpio?

### 4. **Pasarela de Pagos**
- Stripe (internacional)
- Mercado Pago (LATAM)
- Niubiz (Perú)
- **Decisión:** Empezar con mock, integrar después

### 5. **Deploy**
- Backend: Railway, Render, AWS
- Frontend: Vercel, Netlify
- Base de Datos: Railway, AWS RDS

---

## 📅 CRONOGRAMA ESTIMADO

| Fase | Tiempo | Prioridad |
|------|--------|-----------|
| **Fase 1:** Validación Sistema Base | 1-2 días | 🔴 ALTA |
| **Fase 2:** Arquitectura SaaS | 3-5 días | 🔴 ALTA |
| **Fase 3:** Backend Multi-Tenant | 2-3 días | 🟡 MEDIA |
| **Fase 4:** Testing y Validación | 2 días | 🟡 MEDIA |
| **TOTAL** | **8-12 días** | |

---

## 🎯 PRÓXIMOS PASOS INMEDIATOS

### **HOY (27 Dic):**
1. ✅ Revisar estado actual del sistema
2. ✅ Crear plan de trabajo
3. ▶️ **INICIAR FASE 1:** Validar Dashboard
   - Iniciar frontend: `cd nx-vet && npm run dev`
   - Abrir http://localhost:3000
   - Hacer login
   - Probar módulo de estadísticas
   - Reportar cualquier error

### **Mañana (28 Dic):**
1. Continuar validación de módulos (Clientes, Mascotas, Citas)
2. Documentar problemas encontrados
3. Corregir errores críticos

### **29-30 Dic:**
1. Completar validación de todos los módulos
2. Decidir estructura de proyectos
3. Iniciar creación de Landing Page

---

## 📞 SOPORTE Y COMUNICACIÓN

**Preguntas clave para avanzar:**
1. ¿Ya tienes datos de prueba en la base de datos?
2. ¿Prefieres 3 proyectos separados o monorepo?
3. ¿Qué módulo quieres priorizar primero?
4. ¿Ya definiste la pasarela de pagos?
5. ¿Necesitas ayuda con diseño de la landing page?

---

**¿Listo para comenzar con la Fase 1? 🚀**

Podemos empezar probando el dashboard ahora mismo.
