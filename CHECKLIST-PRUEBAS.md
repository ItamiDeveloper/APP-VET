# 📋 CHECKLIST DE PRUEBAS - SISTEMA VETERINARIO

**Fecha:** 27 Diciembre 2025  
**Estado:** Backend iniciando | Frontend ✅ Listo

---

## ⏳ **ESPERANDO BACKEND...**

El backend toma 30-40 segundos en iniciar la primera vez. 
Cuando veas este mensaje en la ventana del backend:
```
Started AppApplication in XX.XXX seconds (JVM running for XX.XXX)
```

**Entonces podemos comenzar! 🚀**

---

## 🎯 **PLAN DE PRUEBAS SISTEMÁTICO**

### **FASE 1: ACCESO Y DASHBOARD** 

#### ✅ 1.1 Login
- [ ] Abrir http://localhost:3000
- [ ] Ingresar credenciales
- [ ] Verificar que redirige a /dashboard
- [ ] Verificar que muestra el nombre de usuario
- [ ] Verificar que se guardó el token JWT

**Datos de prueba:**
```
Usuario: admin (o el que tengas en BD)
Password: (el que configuraste)
```

#### ✅ 1.2 Dashboard - Tarjetas de Resumen
- [ ] Ver **Total Clientes** (número)
- [ ] Ver **Total Mascotas** (número)
- [ ] Ver **Total Citas** (número)  
- [ ] Ver **Total Ingresos** (monto en $)
- [ ] Verificar que no hay errores 500 en consola

#### ✅ 1.3 Dashboard - Gráficos
- [ ] **Gráfico de Ingresos Mensuales**
  - Ver línea o barras con últimos 6 meses
  - Verificar nombres de meses
  - Verificar montos

- [ ] **Gráfico de Citas por Estado**
  - Ver distribución: PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA
  - Verificar conteos

- [ ] **Distribución de Mascotas por Cliente**
  - Ver top 10 clientes con más mascotas
  - Verificar nombres y cantidades

- [ ] **Actividad Reciente**
  - Ver últimas 10 acciones
  - Verificar descripción y fechas

---

### **FASE 2: MÓDULO CLIENTES** 📋

#### 2.1 Listar Clientes
- [ ] Ir a /clientes
- [ ] Ver tabla con clientes
- [ ] Verificar columnas:
  - Nombres
  - Apellidos
  - Documento
  - Teléfono
  - Email
  - Estado
  - Acciones

#### 2.2 Buscar/Filtrar
- [ ] Usar barra de búsqueda
- [ ] Buscar por nombre
- [ ] Buscar por documento
- [ ] Filtrar por estado (ACTIVO/INACTIVO)

#### 2.3 Ver Detalle
- [ ] Click en un cliente
- [ ] Ver modal o página de detalle
- [ ] Verificar todos los datos
- [ ] Ver mascotas asociadas
- [ ] Ver historial de citas

#### 2.4 Crear Cliente
- [ ] Click en "Nuevo Cliente"
- [ ] Llenar formulario:
  - Nombres *
  - Apellidos *
  - Tipo Documento
  - Número Documento
  - Teléfono
  - Email
  - Dirección
- [ ] Guardar
- [ ] Verificar que aparece en la lista
- [ ] Verificar mensaje de éxito

#### 2.5 Editar Cliente
- [ ] Click en "Editar"
- [ ] Modificar datos
- [ ] Guardar
- [ ] Verificar cambios en la lista

#### 2.6 Desactivar Cliente
- [ ] Click en "Desactivar"
- [ ] Confirmar acción
- [ ] Verificar cambio de estado

---

### **FASE 3: MÓDULO MASCOTAS** 🐕

#### 3.1 Listar Mascotas
- [ ] Ir a /mascotas
- [ ] Ver tabla con mascotas
- [ ] Verificar columnas:
  - Nombre
  - Cliente (dueño)
  - Especie
  - Raza
  - Edad
  - Peso
  - Estado
  - Acciones

#### 3.2 Buscar/Filtrar
- [ ] Buscar por nombre de mascota
- [ ] Buscar por nombre de cliente
- [ ] Filtrar por especie
- [ ] Filtrar por raza
- [ ] Filtrar por estado

#### 3.3 Ver Detalle
- [ ] Click en una mascota
- [ ] Ver información completa:
  - Datos básicos
  - Foto (si tiene)
  - Cliente dueño
  - Especie y raza
  - Características
  - Historial médico
  - Próximas citas

#### 3.4 Crear Mascota
- [ ] Click en "Nueva Mascota"
- [ ] Seleccionar cliente *
- [ ] Ingresar datos:
  - Nombre *
  - Especie * (perro, gato, etc.)
  - Raza *
  - Fecha nacimiento
  - Sexo
  - Color
  - Peso
  - Observaciones
- [ ] Guardar
- [ ] Verificar en lista
- [ ] Verificar que está asociada al cliente

#### 3.5 Editar Mascota
- [ ] Click en "Editar"
- [ ] Modificar datos (peso, observaciones, etc.)
- [ ] Guardar
- [ ] Verificar cambios

#### 3.6 Validaciones
- [ ] Intentar crear sin cliente (debe fallar)
- [ ] Intentar crear sin nombre (debe fallar)
- [ ] Verificar que solo se ven mascotas del tenant

---

### **FASE 4: MÓDULO DOCTORES** 👨‍⚕️

#### 4.1 Listar Doctores
- [ ] Ir a /doctores (o usuarios con rol veterinario)
- [ ] Ver lista de doctores
- [ ] Verificar columnas:
  - Nombres
  - Apellidos
  - Colegiatura
  - Especialidad
  - Teléfono
  - Email
  - Estado

#### 4.2 Crear Doctor
- [ ] Agregar nuevo doctor
- [ ] Ingresar datos completos
- [ ] Asociar a usuario (opcional)
- [ ] Guardar

#### 4.3 Ver Horarios
- [ ] Ver disponibilidad del doctor
- [ ] Ver citas asignadas

---

### **FASE 5: MÓDULO CITAS** 📅

#### 5.1 Listar Citas
- [ ] Ir a /citas
- [ ] Ver lista de citas
- [ ] Verificar columnas:
  - Fecha y hora
  - Mascota
  - Cliente
  - Doctor
  - Motivo
  - Estado
  - Acciones

#### 5.2 Vista de Calendario
- [ ] Cambiar a vista calendario
- [ ] Ver citas por día/semana/mes
- [ ] Ver citas de hoy destacadas
- [ ] Click en una cita para ver detalle

#### 5.3 Crear Cita
- [ ] Click en "Nueva Cita"
- [ ] Seleccionar mascota *
- [ ] Auto-completar cliente
- [ ] Seleccionar doctor *
- [ ] Seleccionar fecha y hora *
- [ ] Duración (minutos)
- [ ] Motivo
- [ ] Observaciones
- [ ] Estado inicial: PENDIENTE
- [ ] Guardar
- [ ] Verificar en calendario

#### 5.4 Cambiar Estado de Cita
- [ ] Cita PENDIENTE → CONFIRMADA
  - [ ] Click en "Confirmar"
  - [ ] Verificar cambio de color/estado
  
- [ ] Cita CONFIRMADA → ATENDIDA
  - [ ] Click en "Marcar como Atendida"
  - [ ] Verificar estado actualizado
  
- [ ] Cita → CANCELADA
  - [ ] Click en "Cancelar"
  - [ ] Ingresar motivo
  - [ ] Confirmar

#### 5.5 Editar Cita
- [ ] Click en "Editar"
- [ ] Cambiar fecha/hora
- [ ] Cambiar doctor
- [ ] Modificar observaciones
- [ ] Guardar

#### 5.6 Validaciones
- [ ] No permitir citas en el pasado
- [ ] No permitir solapamiento de citas del mismo doctor
- [ ] Verificar que solo doctores del tenant

---

### **FASE 6: MÓDULO HISTORIAS CLÍNICAS** 🏥

#### 6.1 Ver Historias
- [ ] Ir a /historias
- [ ] Ver lista de historias
- [ ] Filtrar por mascota
- [ ] Filtrar por fecha
- [ ] Filtrar por doctor

#### 6.2 Crear Historia Clínica
- [ ] Click en "Nueva Historia" desde cita
- [ ] O click en "Nueva Historia" directo
- [ ] Seleccionar cita/mascota *
- [ ] Ingresar:
  - Motivo consulta *
  - Síntomas
  - Diagnóstico *
  - Tratamiento *
  - Medicamentos (nombre, dosis, frecuencia)
  - Observaciones
  - Próxima cita (fecha)
- [ ] Guardar
- [ ] Verificar que se creó

#### 6.3 Ver Historia Completa
- [ ] Click en una historia
- [ ] Ver todos los datos
- [ ] Ver medicamentos prescritos
- [ ] Ver tratamiento completo
- [ ] Descargar PDF (si existe)

#### 6.4 Historial de Mascota
- [ ] Desde detalle de mascota
- [ ] Ver todas las historias
- [ ] Ordenadas por fecha desc
- [ ] Ver evolución

---

### **FASE 7: MÓDULO INVENTARIO** 📦

#### 7.1 Listar Productos
- [ ] Ir a /inventario
- [ ] Ver lista de productos
- [ ] Verificar columnas:
  - Código
  - Nombre
  - Categoría
  - Stock actual
  - Stock mínimo
  - Precio venta
  - Estado

#### 7.2 Ver Alertas
- [ ] Ver productos con stock bajo
- [ ] Ver productos sin stock
- [ ] Filtrar por alerta

#### 7.3 Crear Producto
- [ ] Click en "Nuevo Producto"
- [ ] Ingresar:
  - Código/SKU
  - Nombre *
  - Categoría *
  - Descripción
  - Stock actual *
  - Stock mínimo *
  - Precio compra
  - Precio venta *
  - Proveedor
  - Ubicación
- [ ] Guardar
- [ ] Verificar en lista

#### 7.4 Registrar Entrada (Compra)
- [ ] Click en producto
- [ ] "Registrar Entrada"
- [ ] Ingresar cantidad
- [ ] Precio compra
- [ ] Proveedor
- [ ] Número factura
- [ ] Fecha
- [ ] Guardar
- [ ] Verificar que stock aumentó

#### 7.5 Registrar Salida (Uso/Venta)
- [ ] Click en producto
- [ ] "Registrar Salida"
- [ ] Seleccionar motivo (venta, uso interno, ajuste)
- [ ] Ingresar cantidad
- [ ] Observaciones
- [ ] Guardar
- [ ] Verificar que stock disminuyó

#### 7.6 Ver Kardex
- [ ] Click en "Ver Kardex"
- [ ] Ver movimientos:
  - Fecha
  - Tipo (entrada/salida)
  - Cantidad
  - Stock resultante
  - Usuario
  - Observaciones
- [ ] Filtrar por fechas

---

### **FASE 8: MÓDULO VENTAS** 💰

#### 8.1 Listar Ventas
- [ ] Ir a /ventas
- [ ] Ver lista de ventas
- [ ] Verificar columnas:
  - Número
  - Fecha
  - Cliente
  - Total
  - Método pago
  - Estado
  - Acciones

#### 8.2 Ver Detalle de Venta
- [ ] Click en una venta
- [ ] Ver:
  - Cliente
  - Fecha
  - Productos vendidos
  - Cantidades
  - Precios unitarios
  - Subtotal
  - Descuento (si aplica)
  - Total
  - Método de pago
  - Estado

#### 8.3 Crear Nueva Venta
- [ ] Click en "Nueva Venta"
- [ ] Seleccionar cliente *
- [ ] Agregar productos:
  - [ ] Buscar producto
  - [ ] Seleccionar
  - [ ] Ingresar cantidad
  - [ ] Ver precio unitario
  - [ ] Ver subtotal
  - [ ] Agregar más productos
- [ ] Ver resumen:
  - Subtotal
  - Descuento %
  - Total
- [ ] Seleccionar método pago *
- [ ] Guardar
- [ ] Verificar que se creó

#### 8.4 Validaciones
- [ ] No permitir venta sin cliente
- [ ] No permitir venta sin productos
- [ ] No permitir cantidad > stock
- [ ] Verificar actualización de inventario

#### 8.5 Comprobantes
- [ ] Generar boleta
- [ ] Generar factura (si aplica)
- [ ] Descargar PDF
- [ ] Enviar por email (si aplica)

---

### **FASE 9: MÓDULO REPORTES** 📊

#### 9.1 Reportes Disponibles
- [ ] Ir a /reportes
- [ ] Ver lista de reportes:
  - Ventas por período
  - Clientes activos
  - Mascotas atendidas
  - Citas por doctor
  - Ingresos por servicio
  - Inventario valorizado
  - Productos más vendidos

#### 9.2 Generar Reporte
- [ ] Seleccionar reporte
- [ ] Configurar filtros:
  - Fecha inicio
  - Fecha fin
  - Doctor (si aplica)
  - Categoría (si aplica)
- [ ] Generar
- [ ] Ver resultados
- [ ] Ver gráficos

#### 9.3 Exportar
- [ ] Exportar a PDF
- [ ] Exportar a Excel
- [ ] Imprimir

---

### **FASE 10: MÓDULO USUARIOS** 👥

#### 10.1 Listar Usuarios
- [ ] Ir a /usuarios
- [ ] Ver lista de usuarios del tenant
- [ ] Verificar columnas:
  - Username
  - Nombres
  - Apellidos
  - Email
  - Rol
  - Estado
  - Acciones

#### 10.2 Crear Usuario
- [ ] Click en "Nuevo Usuario"
- [ ] Ingresar:
  - Username *
  - Password *
  - Confirmar password *
  - Nombres *
  - Apellidos *
  - Email *
  - Teléfono
  - Rol * (Admin, Veterinario, Recepcionista)
  - Estado (Activo)
- [ ] Guardar
- [ ] Verificar en lista

#### 10.3 Editar Usuario
- [ ] Click en "Editar"
- [ ] Modificar datos
- [ ] Cambiar rol
- [ ] Guardar

#### 10.4 Cambiar Password
- [ ] Click en "Cambiar Contraseña"
- [ ] Ingresar nueva password
- [ ] Confirmar
- [ ] Guardar

#### 10.5 Desactivar Usuario
- [ ] Click en "Desactivar"
- [ ] Confirmar
- [ ] Verificar que no puede hacer login

#### 10.6 Permisos por Rol
**ADMINISTRADOR:**
- [ ] Acceso a todos los módulos
- [ ] Puede crear/editar/eliminar todo
- [ ] Puede gestionar usuarios

**VETERINARIO:**
- [ ] Ver/editar citas asignadas
- [ ] Crear historias clínicas
- [ ] Ver clientes y mascotas
- [ ] NO puede gestionar usuarios
- [ ] NO puede ver reportes financieros

**RECEPCIONISTA:**
- [ ] Gestionar citas
- [ ] Gestionar clientes
- [ ] Registrar ventas
- [ ] NO puede ver historias clínicas completas
- [ ] NO puede gestionar usuarios

---

## 🔒 **PRUEBAS DE SEGURIDAD**

### Multi-Tenant
- [ ] Login con usuario Tenant A
- [ ] Verificar que solo ve datos de Tenant A
- [ ] Intentar acceder a datos de Tenant B vía URL/API
- [ ] Verificar que falla (403 Forbidden)

### JWT
- [ ] Login
- [ ] Copiar token del localStorage
- [ ] Decodificar en jwt.io
- [ ] Verificar claims (tenantId, roles, etc.)
- [ ] Borrar token y refrescar
- [ ] Verificar que redirige a login

### Roles
- [ ] Login como cada rol
- [ ] Verificar menú visible según permisos
- [ ] Intentar acceder a ruta no permitida
- [ ] Verificar redirección o error

---

## 🐛 **REPORTE DE ERRORES**

Usa este formato para reportar problemas:

```
### Error #X: [Título corto]

**Módulo:** [Clientes/Citas/etc.]
**Acción:** [Crear cliente / Editar cita / etc.]
**Esperado:** [Lo que debería pasar]
**Actual:** [Lo que pasó]
**Pasos para reproducir:**
1. Paso 1
2. Paso 2
3. Paso 3

**Error en consola:**
```
[Pegar error aquí]
```

**Screenshot:** [Si es posible]
```

---

## ✅ **CRITERIOS DE ÉXITO**

### Para considerar un módulo completo:
- ✅ CRUD completo funciona
- ✅ Validaciones funcionan
- ✅ Mensajes de error claros
- ✅ Filtros/búsqueda funcionan
- ✅ Aislamiento por tenant
- ✅ Permisos por rol
- ✅ Sin errores 500
- ✅ UX fluida

---

## 📊 **PROGRESO GENERAL**

```
[░░░░░░░░░░] 0% - Dashboard
[░░░░░░░░░░] 0% - Clientes
[░░░░░░░░░░] 0% - Mascotas
[░░░░░░░░░░] 0% - Doctores
[░░░░░░░░░░] 0% - Citas
[░░░░░░░░░░] 0% - Historias
[░░░░░░░░░░] 0% - Inventario
[░░░░░░░░░░] 0% - Ventas
[░░░░░░░░░░] 0% - Reportes
[░░░░░░░░░░] 0% - Usuarios
```

---

**🎯 Vamos a completar cada módulo uno por uno hasta tener el sistema 100% funcional!**
