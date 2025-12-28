# 📘 DOCUMENTACIÓN COMPLETA - APP-VET SAAS

**Sistema Multi-Tenant para Gestión Veterinaria**  
**Versión:** 3.0 Final  
**Fecha:** 28 de Diciembre, 2025  
**Autor:** Eduardo

---

## 📋 TABLA DE CONTENIDOS

1. [Resumen del Sistema](#resumen-del-sistema)
2. [Arquitectura](#arquitectura)
3. [Instalación Rápida](#instalación-rápida)
4. [Backend - Spring Boot](#backend---spring-boot)
5. [Frontend - Next.js](#frontend---nextjs)
6. [Base de Datos](#base-de-datos)
7. [Endpoints API](#endpoints-api)
8. [Credenciales de Prueba](#credenciales-de-prueba)
9. [Troubleshooting](#troubleshooting)

---

## 🎯 RESUMEN DEL SISTEMA

**APP-VET** es un sistema SaaS multi-tenant completo para la gestión de veterinarias, desarrollado con:

- **Backend:** Spring Boot 3.5.8 + Java 17
- **Frontend:** Next.js 14.2.33 + React + TypeScript
- **Base de Datos:** MySQL 8.0+
- **Arquitectura:** Multi-Tenant (un sistema, múltiples veterinarias)

### Funcionalidades Principales

✅ **Gestión de Clientes y Mascotas**
- Registro completo de propietarios
- Fichas de mascotas con historial
- Relación cliente-mascotas

✅ **Agenda y Citas**
- Programación de citas
- Asignación de doctores
- Estados: PROGRAMADA, COMPLETADA, CANCELADA

✅ **Historia Clínica**
- Registro de consultas
- Diagnósticos y tratamientos
- Historial completo por mascota

✅ **Inventario**
- Control de stock de productos
- Alertas de stock mínimo
- Catálogo global de productos

✅ **Ventas y Compras**
- Registro de ventas con detalle
- Control de compras a proveedores
- Integración con inventario

✅ **Reportes y Estadísticas**
- Dashboard con métricas
- Reportes de ventas
- Estadísticas de citas

✅ **Multi-Tenant**
- Aislamiento de datos por veterinaria
- Planes de suscripción
- Gestión de usuarios por tenant

---

## 🏗️ ARQUITECTURA

### Stack Tecnológico

```
┌─────────────────────────────────────────────┐
│           FRONTEND (Next.js 14)             │
│  - React 18 + TypeScript                    │
│  - Tailwind CSS                             │
│  - React Query (TanStack Query)             │
│  - Axios para HTTP                          │
│  Puerto: 3000                               │
└─────────────┬───────────────────────────────┘
              │ HTTP/REST
              │
┌─────────────▼───────────────────────────────┐
│        BACKEND (Spring Boot 3.5.8)          │
│  - Java 17                                  │
│  - Spring Security + JWT                    │
│  - Spring Data JPA                          │
│  - Hibernate 6.6.36                         │
│  - Maven                                    │
│  Puerto: 8080                               │
└─────────────┬───────────────────────────────┘
              │ JDBC
              │
┌─────────────▼───────────────────────────────┐
│         BASE DE DATOS (MySQL 8)             │
│  - 28 tablas                                │
│  - Multi-tenant por id_tenant               │
│  - Datos de prueba incluidos                │
│  Puerto: 3306                               │
└─────────────────────────────────────────────┘
```

### Estructura Multi-Tenant

```
┌─────────────────────────────────────────┐
│     TABLAS GLOBALES (Sin tenant)        │
├─────────────────────────────────────────┤
│  - plan                                 │
│  - super_admin                          │
│  - tenant                               │
│  - rol                                  │
│  - especie                              │
│  - raza                                 │
│  - producto                             │
│  - categoria_producto                   │
│  - proveedor                            │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│   TABLAS POR TENANT (Con id_tenant)     │
├─────────────────────────────────────────┤
│  - usuario                              │
│  - doctor                               │
│  - cliente                              │
│  - mascota                              │
│  - cita                                 │
│  - historia_clinica                     │
│  - inventario                           │
│  - venta / detalle_venta                │
│  - compra / detalle_compra              │
└─────────────────────────────────────────┘
```

---

## ⚡ INSTALACIÓN RÁPIDA

### Prerequisitos

- ✅ Java 17 o superior
- ✅ Node.js 18 o superior
- ✅ MySQL 8.0 o superior
- ✅ Git

### Paso 1: Clonar Repositorio

```bash
git clone <url-repo>
cd APP-VET
```

### Paso 2: Configurar Base de Datos

1. Inicia MySQL (XAMPP, MySQL Workbench, etc.)
2. Ejecuta el script completo:

```bash
# Abrir MySQL Workbench o cliente MySQL
# Ejecutar: SETUP-DATABASE-COMPLETO.sql
```

Este script crea:
- Base de datos `veterinaria_saas`
- 28 tablas
- Datos iniciales (planes, especies, razas, etc.)
- 2 tenants de prueba (VET001, VET002)
- Usuarios y contraseñas

### Paso 3: Configurar Backend

```bash
cd app

# Verificar application.properties
# Las credenciales por defecto son:
# spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria_saas
# spring.datasource.username=root
# spring.datasource.password=

# Iniciar servidor
.\mvnw.cmd spring-boot:run
```

Backend estará en: `http://localhost:8080`

### Paso 4: Configurar Frontend

```bash
cd nx-vet

# Instalar dependencias (solo primera vez)
npm install

# Verificar .env.local
# NEXT_PUBLIC_API_URL=http://localhost:8080

# Iniciar servidor de desarrollo
npm run dev
```

Frontend estará en: `http://localhost:3000`

---

## 🔧 BACKEND - SPRING BOOT

### Estructura de Carpetas

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/vet/spring/app/
│   │   │   ├── config/          # Configuraciones
│   │   │   ├── controller/      # Controladores REST
│   │   │   │   ├── auth/        # Autenticación
│   │   │   │   └── tenant/      # Endpoints por tenant
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── repository/      # Repositorios JPA
│   │   │   ├── service/         # Lógica de negocio
│   │   │   ├── security/        # JWT y seguridad
│   │   │   └── tenant/          # Context multi-tenant
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Tests
├── pom.xml                      # Dependencias Maven
└── mvnw.cmd                     # Maven Wrapper
```

### Controladores Implementados

**Autenticación:**
- `POST /api/auth/login` - Login con JWT
- `POST /api/auth/register` - Registro (solo super admin)

**Endpoints Tenant (requieren autenticación):**

1. **CitaController** - `/api/tenant/citas`
2. **ClienteController** - `/api/tenant/clientes`
3. **CompraController** - `/api/tenant/compras`
4. **DoctorController** - `/api/tenant/doctores`
5. **EspecieController** - `/api/tenant/especies`
6. **EstadisticasController** - `/api/tenant/estadisticas`
7. **HistoriaClinicaController** - `/api/tenant/historias`
8. **InventarioController** - `/api/tenant/inventario`
9. **MascotaController** - `/api/tenant/mascotas`
10. **PlanController** - `/api/tenant/planes`
11. **RazaController** - `/api/tenant/razas`
12. **TenantController** - `/api/tenant/mi-suscripcion`
13. **UsuarioController** - `/api/tenant/usuarios`
14. **VentaController** - `/api/tenant/ventas`

### Seguridad

- **JWT Token:** Válido por 24 horas
- **BCrypt:** Para hash de contraseñas
- **TenantContext:** ThreadLocal para aislamiento de datos
- **CORS:** Configurado para localhost:3000

### Configuración (application.properties)

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria_saas
spring.datasource.username=root
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

# Puerto
server.port=8080

# JWT
jwt.secret=tu_clave_secreta_muy_larga_y_segura
jwt.expiration=86400000
```

---

## 💻 FRONTEND - NEXT.JS

### Estructura de Carpetas

```
nx-vet/
├── src/
│   ├── app/                     # App Router de Next.js
│   │   ├── auth/login/          # Página de login
│   │   ├── dashboard/           # Dashboard principal
│   │   ├── citas/               # Módulo de citas
│   │   ├── clientes/            # Módulo de clientes
│   │   ├── mascotas/            # Módulo de mascotas
│   │   ├── historias/           # Historia clínica
│   │   ├── inventario/          # Inventario
│   │   ├── ventas/              # Ventas
│   │   ├── compras/             # Compras (pendiente)
│   │   ├── usuarios/            # Usuarios
│   │   ├── reportes/            # Reportes (pendiente)
│   │   ├── components/          # Componentes compartidos
│   │   └── providers/           # Providers (Auth, Query)
│   ├── features/                # Hooks por módulo
│   ├── services/                # API services
│   ├── hooks/                   # Hooks globales
│   └── shared/                  # Utilidades compartidas
├── .env.local                   # Variables de entorno
├── package.json
└── next.config.js
```

### Servicios API Implementados

Todos los servicios en `src/services/`:

```typescript
api.ts              // Cliente Axios configurado
citas.ts            // Citas
clientes.ts         // Clientes
compras.ts          // Compras
doctores.ts         // Doctores
especies.ts         // Especies
estadisticas.ts     // Estadísticas
historias.ts        // Historia clínica
inventarios.ts      // Inventario
mascotas.ts         // Mascotas
razas.ts            // Razas
reportes.ts         // Reportes (pendiente)
roles.ts            // Roles
tenant.ts           // Suscripción
usuarios.ts         // Usuarios
ventas.ts           // Ventas
veterinarias.ts     // Veterinarias (super admin)
```

### Autenticación

El sistema usa JWT almacenado en localStorage:

```typescript
// Login
POST /api/auth/login
Body: { username, password }
Response: { token, user }

// El token se almacena y se envía en todos los requests:
Authorization: Bearer <token>
```

### Configuración (.env.local)

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## 💾 BASE DE DATOS

### Esquema Principal

**28 Tablas creadas:**

**Globales (9):**
- `plan` - Planes de suscripción
- `super_admin` - Administradores del SaaS
- `tenant` - Veterinarias clientes
- `suscripcion` - Suscripciones activas
- `rol` - Roles de usuario
- `especie` - Especies de animales
- `raza` - Razas por especie
- `producto` - Catálogo global de productos
- `categoria_producto` - Categorías de productos
- `proveedor` - Proveedores

**Por Tenant (18):**
- `usuario` - Usuarios del tenant
- `doctor` - Doctores veterinarios
- `cliente` - Clientes (dueños de mascotas)
- `mascota` - Mascotas registradas
- `cita` - Citas médicas
- `historia_clinica` - Historias clínicas
- `inventario` - Inventario de productos
- `venta` - Ventas
- `detalle_venta` - Detalle de ventas
- `compra` - Compras
- `detalle_compra` - Detalle de compras

### Datos Iniciales

El script `SETUP-DATABASE-COMPLETO.sql` incluye:

- ✅ 3 Planes (Básico, Profesional, Enterprise)
- ✅ 1 Super Admin
- ✅ 4 Roles (ADMIN, VETERINARIO, RECEPCIONISTA, ASISTENTE)
- ✅ 5 Especies (Perro, Gato, Ave, Roedor, Reptil)
- ✅ 18 Razas
- ✅ 6 Categorías de productos
- ✅ 16 Productos de ejemplo
- ✅ 3 Proveedores
- ✅ 2 Tenants (VET001, VET002)
- ✅ Usuarios, doctores, clientes y mascotas de prueba
- ✅ Citas, historias, inventario, ventas y compras de ejemplo

---

## 🌐 ENDPOINTS API

### Autenticación

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR...",
  "user": {
    "idUsuario": 1,
    "username": "admin",
    "nombres": "Carlos",
    "apellidos": "Rodríguez",
    "rol": "ADMIN",
    "tenant": "VET001"
  }
}
```

### Citas

```http
GET    /api/tenant/citas              # Listar todas
GET    /api/tenant/citas/{id}         # Por ID
GET    /api/tenant/citas/cliente/{id} # Por cliente
POST   /api/tenant/citas              # Crear
PUT    /api/tenant/citas/{id}         # Actualizar
DELETE /api/tenant/citas/{id}         # Eliminar
```

### Clientes

```http
GET    /api/tenant/clientes           # Listar todos
GET    /api/tenant/clientes/{id}      # Por ID
POST   /api/tenant/clientes           # Crear
PUT    /api/tenant/clientes/{id}      # Actualizar
DELETE /api/tenant/clientes/{id}      # Eliminar
```

### Mascotas

```http
GET    /api/tenant/mascotas           # Listar todas
GET    /api/tenant/mascotas/{id}      # Por ID
GET    /api/tenant/mascotas/cliente/{id} # Por cliente
POST   /api/tenant/mascotas           # Crear
PUT    /api/tenant/mascotas/{id}      # Actualizar
DELETE /api/tenant/mascotas/{id}      # Eliminar
```

### Historia Clínica

```http
GET    /api/tenant/historias          # Listar todas
GET    /api/tenant/historias/{id}     # Por ID
GET    /api/tenant/historias/mascota/{id} # Por mascota
POST   /api/tenant/historias          # Crear
PUT    /api/tenant/historias/{id}     # Actualizar
DELETE /api/tenant/historias/{id}     # Eliminar
```

### Inventario

```http
GET    /api/tenant/inventario         # Listar inventario
GET    /api/tenant/inventario/{id}    # Por ID
GET    /api/tenant/inventario/productos # Productos disponibles
POST   /api/tenant/inventario         # Crear
PUT    /api/tenant/inventario/{id}    # Actualizar
DELETE /api/tenant/inventario/{id}    # Eliminar
```

### Ventas

```http
GET    /api/tenant/ventas             # Listar todas
GET    /api/tenant/ventas/{id}        # Por ID
POST   /api/tenant/ventas             # Crear
PUT    /api/tenant/ventas/{id}        # Actualizar
DELETE /api/tenant/ventas/{id}        # Eliminar
```

### Compras

```http
GET    /api/tenant/compras            # Listar todas
GET    /api/tenant/compras/{id}       # Por ID
POST   /api/tenant/compras            # Crear
PUT    /api/tenant/compras/{id}       # Actualizar
DELETE /api/tenant/compras/{id}       # Eliminar
```

### Estadísticas

```http
GET    /api/tenant/estadisticas       # Dashboard completo
```

### Suscripción

```http
GET    /api/tenant/mi-suscripcion     # Info de suscripción actual
```

---

## 🔑 CREDENCIALES DE PRUEBA

### Super Administrador

```
Usuario: superadmin
Password: admin123
Email: superadmin@appvet.com
```

### Tenant VET001 (Veterinaria Patitas Felices)

```
Usuario: admin
Password: admin123
Email: admin@patitasfelices.com
Tenant: VET001
```

### Tenant VET002 (Clínica Veterinaria Amigos Peludos)

```
Usuario: admin
Password: admin123
Email: admin@amigospeludos.com
Tenant: VET002
```

---

## 🔍 TROUBLESHOOTING

### Backend no inicia

**Problema:** Error al iniciar Spring Boot

**Solución:**
```bash
# 1. Verificar que MySQL esté corriendo
# 2. Verificar credenciales en application.properties
# 3. Limpiar y recompilar
cd app
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

### Frontend no conecta con backend

**Problema:** Errores de CORS o conexión rechazada

**Solución:**
```bash
# 1. Verificar que backend esté corriendo en puerto 8080
# 2. Verificar .env.local
echo $env:NEXT_PUBLIC_API_URL

# 3. Reiniciar frontend
npm run dev
```

### Errores de autenticación

**Problema:** Token inválido o expirado

**Solución:**
1. Cerrar sesión en el frontend
2. Limpiar localStorage
3. Volver a hacer login

### Base de datos vacía

**Problema:** No hay datos después de ejecutar script

**Solución:**
```sql
-- Verificar que la base de datos existe
USE veterinaria_saas;

-- Verificar tablas
SHOW TABLES;

-- Verificar tenants
SELECT * FROM tenant;

-- Re-ejecutar script completo si es necesario
-- SETUP-DATABASE-COMPLETO.sql
```

### Puertos ocupados

**Problema:** Puerto 8080 o 3000 en uso

**Solución:**
```powershell
# Matar procesos en puerto 8080
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process -Force

# Matar procesos en puerto 3000
Get-Process -Id (Get-NetTCPConnection -LocalPort 3000).OwningProcess | Stop-Process -Force
```

---

## 📊 ESTADO ACTUAL DEL PROYECTO

### ✅ Completado

- [x] Backend Spring Boot completo con 14 controladores
- [x] Frontend Next.js con todas las rutas
- [x] Autenticación JWT funcional
- [x] Multi-tenant implementado
- [x] Base de datos con datos de prueba
- [x] CRUD completo para todas las entidades
- [x] Servicios frontend para todos los módulos
- [x] Historia Clínica
- [x] Inventario
- [x] Ventas
- [x] Compras
- [x] Estadísticas y Dashboard

### ⚠️ Pendiente de Optimización

- [ ] Resolver N+1 queries de Hibernate (agregar @EntityGraph)
- [ ] Implementar reportes avanzados
- [ ] Landing page pública
- [ ] Portal de super administrador
- [ ] Mejoras en UI/UX

### 🐛 Problemas Conocidos

1. **Backend startup intermitente** - Requiere depuración adicional
2. **N+1 queries** - Múltiples SELECT por relaciones lazy
3. **Reportes** - Implementación parcial

---

## 📚 RECURSOS ADICIONALES

### Documentación Técnica

- **Spring Boot:** https://spring.io/projects/spring-boot
- **Next.js:** https://nextjs.org/docs
- **React Query:** https://tanstack.com/query/latest
- **JWT:** https://jwt.io/

### Comandos Útiles

```bash
# Backend
cd app
.\mvnw.cmd clean install      # Compilar
.\mvnw.cmd spring-boot:run    # Iniciar
.\mvnw.cmd test               # Tests

# Frontend
cd nx-vet
npm install                   # Instalar dependencias
npm run dev                   # Desarrollo
npm run build                 # Producción
npm start                     # Iniciar producción

# Base de datos
mysql -u root -p < SETUP-DATABASE-COMPLETO.sql
```

---

## 👨‍💻 CONTACTO Y SOPORTE

**Desarrollador:** Eduardo  
**Proyecto:** APP-VET SaaS  
**Versión:** 3.0 Final  
**Fecha:** 28 de Diciembre, 2025

---

## 📝 CHANGELOG

### v3.0 (28 Dic 2025)
- ✅ Consolidación de archivos SQL en uno solo
- ✅ Consolidación de documentación
- ✅ Implementación completa de Historia Clínica
- ✅ Implementación completa de Inventario
- ✅ Implementación completa de Ventas
- ✅ Implementación completa de Compras
- ✅ Corrección de errores de compilación
- ✅ 14 controladores backend funcionando

### v2.0 (27 Dic 2025)
- ✅ Migración completa a arquitectura multi-tenant
- ✅ Corrección de endpoints y rutas
- ✅ Implementación de JWT
- ✅ 10 controladores iniciales

### v1.0 (Dic 2025)
- ✅ Versión inicial del proyecto
- ✅ Estructura básica backend y frontend

---

**¡Sistema listo para usar! 🚀**
