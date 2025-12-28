# 🧪 GUÍA DE PRUEBAS - VETERINARIA SAAS

## 📋 Resumen
Este documento te guía para probar **TODOS** los endpoints del backend automáticamente.

---

## 🚀 INICIO RÁPIDO

### 1️⃣ **Iniciar el Backend** (Git Bash o PowerShell)

```bash
# Opción 1: Git Bash
cd app
./mvnw spring-boot:run

# Opción 2: PowerShell
cd app
.\mvnw.cmd spring-boot:run
```

Espera hasta ver: **`Started AppApplication`** (30-45 segundos)

### 2️⃣ **Ejecutar Pruebas Automáticas**

Abre **otra terminal** (PowerShell, Git Bash o CMD):

```powershell
# PowerShell
cd C:\Users\Eduardo\APP-VET
.\test-all-endpoints.ps1

# Si da error de permisos:
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\test-all-endpoints.ps1
```

```bash
# Git Bash
cd /c/Users/Eduardo/APP-VET
pwsh ./test-all-endpoints.ps1
```

---

## 🔐 CREDENCIALES

### Usuario Tenant (VET001)
- **Username:** `admin`
- **Password:** `admin123`
- **TenantId:** `1` (VET001)

### Usuario Tenant (VET002)  
- **Username:** `admin`
- **Password:** `admin123`
- **TenantId:** `2` (VET002)

### Super Admin
- **Username:** `superadmin`
- **Password:** `admin123`

---

## 📡 ENDPOINTS QUE SE PRUEBAN

### ✅ 1. Health Check
- `GET /actuator/health`

### 🔐 2. Autenticación
- `POST /api/auth/login`

### 📚 3. Catálogos (12 endpoints)
- `GET /api/tenant/especies`
- `GET /api/tenant/razas`
- `GET /api/tenant/razas/especie/{id}`
- `GET /api/tenant/doctores`
- `GET /api/tenant/roles`

### 👥 4. Clientes (2 endpoints)
- `GET /api/tenant/clientes`
- `GET /api/tenant/clientes/search?query={texto}`

### 🐾 5. Mascotas (2 endpoints)
- `GET /api/tenant/mascotas`
- `GET /api/tenant/mascotas/cliente/{id}`

### 📅 6. Citas (3 endpoints)
- `GET /api/tenant/citas`
- `GET /api/tenant/citas/fecha?fecha={YYYY-MM-DD}`
- `GET /api/tenant/citas/estado/{ESTADO}`

### 📋 7. Historia Clínica (2 endpoints)
- `GET /api/tenant/historias`
- `GET /api/tenant/historias/mascota/{id}`

### 📦 8. Inventario (3 endpoints)
- `GET /api/tenant/inventario`
- `GET /api/tenant/inventario/producto/{id}`
- `GET /api/tenant/inventario/stock-bajo`

### 💰 9. Ventas (3 endpoints)
- `GET /api/tenant/ventas`
- `GET /api/tenant/ventas/cliente/{id}`
- `GET /api/tenant/ventas/rango?fechaInicio={date}&fechaFin={date}`

### 🛒 10. Compras (2 endpoints)
- `GET /api/tenant/compras`
- `GET /api/tenant/compras/proveedor/{id}`

### 📊 11. Dashboard/Estadísticas (4 endpoints)
- `GET /api/tenant/estadisticas/citas-por-estado`
- `GET /api/tenant/estadisticas/ingresos-mensuales`
- `GET /api/tenant/estadisticas/top-productos`
- `GET /api/tenant/estadisticas/mascotas-por-especie`

### 👤 12. Usuarios (2 endpoints)
- `GET /api/tenant/usuarios`
- `GET /api/tenant/usuarios/search?query={texto}`

---

## 🎯 RESULTADO ESPERADO

Si todo funciona correctamente, verás:

```
═══════════════════════════════════════════════
📊 RESUMEN DE PRUEBAS
═══════════════════════════════════════════════

  Total de pruebas:  36
✅ Pruebas exitosas:   36
❌ Pruebas fallidas:   0

  Tasa de éxito:     100%

╔═══════════════════════════════════════════════╗
║                                               ║
║      ✅ ¡TODAS LAS PRUEBAS PASARON! ✅      ║
║                                               ║
╚═══════════════════════════════════════════════╝

✅ El sistema está funcionando correctamente
```

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### ❌ Error: "Backend no está respondiendo"
**Solución:** Asegúrate de que el backend esté corriendo:
```bash
cd app
./mvnw spring-boot:run
```
Espera hasta ver `Started AppApplication`

### ❌ Error: "Login falló - 401"
**Solución:** Verifica el hash de password en la BD:
```sql
USE veterinaria_saas;
SELECT username, password_hash FROM usuario WHERE username='admin';
```
Debe ser: `$2a$10$dXJ3SW6G7P370PKenZP82u3.LwdFP7D.CnK0MkLfJLBB3KPLfITLe`

### ❌ Error: "Login falló - 500"
**Solución:** Revisa los logs del backend para ver el error específico.

### ❌ Endpoints devuelven arrays vacíos `[]`
**Solución:** Es normal si no hay datos. Ejecuta el script de datos de prueba:
```sql
-- Verifica que los datos existan
SELECT COUNT(*) FROM cliente WHERE id_tenant = 1;
SELECT COUNT(*) FROM mascota WHERE id_tenant = 1;
```

---

## 📝 NOTAS IMPORTANTES

1. **Puerto del Backend:** `8080` (http://localhost:8080)
2. **Puerto del Frontend:** `3000` (http://localhost:3000)
3. **Base de Datos:** `veterinaria_saas` (MySQL)
4. **Tenant Multi-Tenant:** El sistema filtra datos por `id_tenant` automáticamente
5. **JWT Token:** Se genera en el login y tiene validez de 24 horas

---

## 🔄 REINICIAR TODO

Si necesitas empezar de cero:

```powershell
# 1. Detener procesos
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Get-Process node -ErrorAction SilentlyContinue | Stop-Process -Force

# 2. Recrear base de datos (en MySQL)
# Ejecuta: SETUP-DATABASE-COMPLETO.sql

# 3. Reiniciar backend
cd app
.\mvnw.cmd spring-boot:run

# 4. Ejecutar pruebas
.\test-all-endpoints.ps1
```

---

## 📞 SOPORTE

Si tienes problemas:
1. Verifica que MySQL esté corriendo
2. Verifica que el puerto 8080 esté libre
3. Revisa los logs del backend en la consola
4. Verifica que la base de datos `veterinaria_saas` exista

---

**Última actualización:** 28 de Diciembre, 2025
