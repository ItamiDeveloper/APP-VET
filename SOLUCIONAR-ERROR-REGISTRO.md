# 🚨 SOLUCIÓN AL ERROR DE REGISTRO DE VETERINARIAS

## ❌ Error Actual
```
localhost:3000 dice: Error al registrar veterinaria. Intenta nuevamente.
Backend retorna: 500 Internal Server Error
```

## 🔍 Causa del Problema
La base de datos **NO tiene** los datos iniciales necesarios:
- ❌ No existen ROLES (ADMIN, VETERINARIO, etc.)
- ❌ No existen PLANES (GRATIS, BÁSICO, etc.)
- ❌ No existen CATEGORÍAS de productos

## ✅ SOLUCIÓN (3 pasos - 2 minutos)

### PASO 1: Abre MySQL Workbench
1. Abre **MySQL Workbench** (la aplicación naranja/azul de MySQL)
2. Haz clic en tu conexión a **localhost:3306**
3. Ingresa tu contraseña de root

### PASO 2: Ejecuta el Script
1. En MySQL Workbench, haz clic en: **File > Open SQL Script...**
2. Navega a: `C:\Users\Itami\APP-VET\`
3. Selecciona el archivo: **`INIT-RAPIDO.sql`** ⭐ (USAR ESTE)
4. Haz clic en **Abrir**
5. Haz clic en el botón **⚡ Execute** (o presiona Ctrl+Shift+Enter)
6. Verás el mensaje: **"Script ejecutado exitosamente"** ✅

### PASO 3: Refresca el Navegador
1. Ve a tu navegador (Chrome/Edge)
2. En la página `localhost:3000/registro?plan=1`
3. Presiona **F5** o **Ctrl+R** para recargar
4. **¡Completa el formulario y registra tu veterinaria!** 🎉

---

## 📋 Alternativa: Copiar y Pegar Directo

Si prefieres copiar/pegar en vez de abrir el archivo:

1. En MySQL Workbench, abre una nueva pestaña SQL
2. **Copia TODO el contenido de abajo:**

\`\`\`sql
USE veterinaria_saas;

INSERT IGNORE INTO rol (nombre, descripcion) VALUES
('SUPER_ADMIN', 'Super Administrador del sistema - gestiona todos los tenants'),
('ADMIN', 'Administrador del tenant - gestión completa de la veterinaria'),
('VETERINARIO', 'Veterinario - acceso a historias clínicas, citas y tratamientos'),
('RECEPCIONISTA', 'Recepcionista - gestión de citas, clientes y ventas básicas');

INSERT IGNORE INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_clientes, max_mascotas, max_historias, estado) VALUES
('GRATIS', 'Plan de prueba gratuito por 30 días', 0.00, 0.00, 2, 50, 100, 200, 'ACTIVO'),
('BÁSICO', 'Plan ideal para veterinarias pequeñas', 49.99, 499.99, 5, 200, 500, 1000, 'ACTIVO'),
('PROFESIONAL', 'Plan completo para veterinarias en crecimiento', 99.99, 999.99, 15, 1000, 2500, 5000, 'ACTIVO'),
('EMPRESARIAL', 'Plan sin límites para cadenas de veterinarias', 199.99, 1999.99, NULL, NULL, NULL, NULL, 'ACTIVO');

INSERT IGNORE INTO categoria_producto (nombre, descripcion, estado) VALUES
('Medicamentos', 'Medicamentos veterinarios', 'ACTIVO'),
('Alimentos', 'Alimentos para mascotas', 'ACTIVO'),
('Accesorios', 'Accesorios y juguetes', 'ACTIVO'),
('Vacunas', 'Vacunas y sueros', 'ACTIVO'),
('Higiene', 'Productos de higiene y cuidado', 'ACTIVO');

SELECT 'Script ejecutado exitosamente' AS Resultado;
\`\`\`

3. **Pega** en MySQL Workbench
4. Haz clic en **⚡ Execute**

---

## ✅ Verificación

Después de ejecutar el script, verifica que se crearon los datos:

\`\`\`sql
SELECT * FROM rol;           -- Debe mostrar 4 roles
SELECT * FROM plan;          -- Debe mostrar 4 planes
SELECT * FROM categoria_producto; -- Debe mostrar 5 categorías
\`\`\`

---

## 🎯 Resultado Esperado

Después de ejecutar el script:

✅ **Backend**: Ya NO dará error 500  
✅ **Frontend**: El formulario de registro funcionará  
✅ **Podrás crear**: Tu veterinaria "Veterinaria Comas" con todos los datos  

---

## 🆘 Si Sigue sin Funcionar

Si después de ejecutar el script aún tienes problemas:

1. Verifica que el backend esté corriendo: `http://localhost:8080/actuator/health`
2. Revisa la consola del navegador (F12) para ver errores específicos
3. Verifica que los datos se insertaron correctamente (ejecuta los SELECT de arriba)

---

## 📄 Archivos Disponibles

- ✅ **INIT-RAPIDO.sql** - Script corto para copiar/pegar (USAR ESTE)
- 📋 **INICIALIZAR-DATOS-BASICOS.sql** - Script completo con comentarios
- 🧪 **TEST-RAPIDO-E2E.ps1** - Script de pruebas del sistema
- 📊 **REPORTE-FINAL-VALIDACION.md** - Documentación completa

---

**¡Con esto quedará listo para registrar veterinarias! 🚀**
