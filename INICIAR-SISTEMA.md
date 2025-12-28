# 🚀 GUÍA RÁPIDA - INICIAR SISTEMA

## ✅ ESTADO ACTUAL

- **Frontend:** ✅ CORRIENDO en http://localhost:3000
- **Backend:** ⏳ INICIANDO (toma 30-40 segundos la primera vez)

---

## 📝 INSTRUCCIONES MANUALES

### 1. Iniciar Backend (Si no está corriendo)

Abre **PowerShell** o **CMD** y ejecuta:

```powershell
cd c:\Users\Eduardo\APP-VET\app
.\mvnw.cmd spring-boot:run
```

**Espera hasta ver:** `Started AppApplication in X seconds`

---

### 2. Iniciar Frontend (Ya está corriendo ✅)

```powershell
cd c:\Users\Eduardo\APP-VET\nx-vet
npm run dev
```

---

## 🧪 VERIFICAR QUE TODO FUNCIONA

### Backend:
```powershell
curl http://localhost:8080/actuator/health
```
Debe responder: `{"status":"UP"}`

### Frontend:
Abre navegador: http://localhost:3000

---

## 📋 PLAN DE PRUEBAS

### ✅ Ya completado:
- Frontend levantado
- Backend iniciándose

### 🔄 Siguiente:

#### **1. DASHBOARD** ⭐ (PRIORIDAD)
- [ ] Login con usuario de prueba
- [ ] Verificar estadísticas:
  - Total clientes
  - Total mascotas
  - Total citas
  - Total ingresos
  - Gráfico ingresos mensuales
  - Gráfico citas por estado
  - Distribución mascotas
  - Actividad reciente

#### **2. MÓDULO CLIENTES** 📋
- [ ] Ver lista de clientes
- [ ] Crear nuevo cliente
- [ ] Editar cliente
- [ ] Ver detalle cliente
- [ ] Buscar cliente
- [ ] Validar filtro por tenant

#### **3. MÓDULO MASCOTAS** 🐕
- [ ] Ver lista de mascotas
- [ ] Crear nueva mascota
- [ ] Asignar a cliente
- [ ] Editar mascota
- [ ] Ver detalle mascota
- [ ] Filtrar por especie/raza

#### **4. MÓDULO CITAS** 📅
- [ ] Ver calendario de citas
- [ ] Crear nueva cita
- [ ] Asignar doctor y mascota
- [ ] Cambiar estado (PENDIENTE → CONFIRMADA → ATENDIDA)
- [ ] Cancelar cita
- [ ] Ver historial

#### **5. MÓDULO HISTORIAS CLÍNICAS** 🏥
- [ ] Ver historias de una mascota
- [ ] Crear nueva historia
- [ ] Agregar diagnóstico
- [ ] Agregar tratamiento
- [ ] Agregar medicamentos
- [ ] Ver historial completo

#### **6. MÓDULO INVENTARIO** 📦
- [ ] Ver productos
- [ ] Crear producto
- [ ] Registrar entrada
- [ ] Registrar salida
- [ ] Ver kardex
- [ ] Alertas stock mínimo

#### **7. MÓDULO VENTAS** 💰
- [ ] Ver ventas
- [ ] Crear nueva venta
- [ ] Agregar productos
- [ ] Calcular total
- [ ] Generar comprobante
- [ ] Ver reportes

#### **8. MÓDULO USUARIOS** 👥
- [ ] Ver usuarios
- [ ] Crear usuario
- [ ] Asignar rol
- [ ] Cambiar password
- [ ] Desactivar usuario
- [ ] Permisos por rol

---

## 🎯 ORDEN DE PRUEBA RECOMENDADO

1. **Dashboard** - Ver que todo carga
2. **Clientes** - Base de datos
3. **Mascotas** - Relacionado con clientes
4. **Doctores** - Necesario para citas
5. **Citas** - Usa doctores y mascotas
6. **Historias** - Resultado de citas
7. **Inventario** - Gestión de productos
8. **Ventas** - Usa inventario
9. **Usuarios** - Gestión de accesos

---

## ⚠️ PROBLEMAS COMUNES

### Backend no inicia:
```powershell
# Detener procesos Java anteriores
Stop-Process -Name "java" -Force

# Reiniciar
cd c:\Users\Eduardo\APP-VET\app
.\mvnw.cmd clean spring-boot:run
```

### Frontend no inicia:
```powershell
# Limpiar cache
cd c:\Users\Eduardo\APP-VET\nx-vet
rm -r .next
npm run dev
```

### Puerto ocupado:
```powershell
# Ver qué usa el puerto 8080
netstat -ano | findstr :8080

# Matar proceso
taskkill /PID <numero_pid> /F
```

---

## 📞 SIGUIENTE PASO

**En cuanto veas en la ventana del backend:**
```
Started AppApplication in XX.XXX seconds
```

**Entonces:**
1. Abre navegador
2. Ve a http://localhost:3000
3. Haz login
4. Prueba el Dashboard

---

**¿Listo para empezar las pruebas? 🚀**
