# 📋 Instrucciones para Configurar la Base de Datos Completa

## ⚠️ IMPORTANTE: ARCHIVO ÚNICO
Todo está integrado en un solo archivo: **SETUP-DATABASE.sql**

Ya no necesitas ejecutar scripts separados. El módulo de proveedores está incluido en la instalación principal.

## Ubicación del Archivo
```
APP-VET/SETUP-DATABASE.sql
```

## Método 1: MySQL Workbench (Recomendado para Windows)

1. Abre **MySQL Workbench**
2. Conéctate a tu servidor MySQL local
3. Abre el archivo:
   - Menú: `File` → `Open SQL Script`
   - Navega a: `C:\Users\Itami\APP-VET\SETUP-DATABASE.sql`
4. Haz clic en el botón **Execute** ⚡ (o presiona `Ctrl+Shift+Enter`)
5. Espera ~15 segundos
6. Verifica en los mensajes que todo se creó correctamente

## Método 2: Línea de Comandos (MySQL CLI)

### Opción A: Desde PowerShell
```powershell
cd C:\Users\Itami\APP-VET
mysql -u root -p < SETUP-DATABASE.sql
```

### Opción B: Desde MySQL CLI
```powershell
mysql -u root -p
```
Luego dentro de MySQL:
```sql
SOURCE C:/Users/Itami/APP-VET/SETUP-DATABASE.sql;
```

## Método 3: HeidiSQL (Alternativa)

1. Abre **HeidiSQL**
2. Conéctate a tu base de datos
3. Menú: `File` → `Load SQL file`
4. Selecciona `C:\Users\Itami\APP-VET\SETUP-DATABASE.sql`
5. Presiona `F9` o clic en **Execute**

## Verificación Post-Ejecución

### 1. Verifica que la base de datos existe
```sql
SHOW DATABASES LIKE 'veterinaria_saas';
USE veterinaria_saas;
```

### 2. Verifica que la tabla proveedor existe
```sql
SHOW CREATE TABLE proveedor;
```

**Resultado esperado:**
```
Table: proveedor
Create Table: CREATE TABLE `proveedor` (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) NOT NULL,
  `ruc` varchar(20) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` text,
  `contacto` varchar(100) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_proveedor`),
  KEY `idx_proveedor_estado` (`estado`),
  KEY `idx_proveedor_nombre` (`nombre`)
) ENGINE=InnoDB
```

### 3. Verifica los datos insertados
```sql
SELECT * FROM proveedor;
```

**Resultado esperado: 5 proveedores**
| id_proveedor | nombre | ruc | telefono | email | contacto | estado |
|--------------|--------|-----|----------|-------|----------|--------|
| 1 | Laboratorios Bayer | 20123456789 | 01-234-5678 | ventas@bayer.com | Juan Pérez | ACTIVO |
| 2 | MSD Animal Health | 20987654321 | 01-876-5432 | info@msd.com | María García | ACTIVO |
| 3 | Zoetis Perú | 20456789123 | 01-456-7890 | contacto@zoetis.pe | Carlos Rodríguez | ACTIVO |
| 4 | Alimentos Pet Supply | 20789123456 | 01-789-1234 | ventas@petsupply.com | Ana Torres | ACTIVO |
| 5 | Distribuidora Veterinaria S.A. | 20321654987 | 01-321-6549 | info@distveterinaria.com | Luis Martínez | ACTIVO |

### 4. Verifica todas las tablas creadas
```sql
SHOW TABLES;
```

**Deberías ver ~20 tablas incluyendo:**
- plan, super_admin, tenant, suscripcion
- rol, especie, raza, categoria_producto, producto, **proveedor**
- usuario, doctor, cliente, mascota, cita, historia_clinica
- inventario, venta, detalle_venta, compra, detalle_compra

### 5. Prueba el endpoint en el backend

**Reinicia el servidor Spring Boot:**
```powershell
cd C:\Users\Itami\APP-VET\app
mvn spring-boot:run
# O si ya lo tienes compilado:
java -jar target/app-0.0.1-SNAPSHOT.jar
```

**Prueba la API:**
```powershell
# Obtener todos los proveedores
curl http://localhost:8080/api/tenant/proveedores

# Obtener solo proveedores activos
curl http://localhost:8080/api/tenant/proveedores/activos
```

**Respuesta esperada (JSON):**
```json
[
  {
    "idProveedor": 1,
    "nombre": "Laboratorios Bayer",
    "ruc": "20123456789",
    "telefono": "01-234-5678",
    "email": "ventas@bayer.com",
    "direccion": null,
    "contacto": "Juan Pérez",
    "estado": "ACTIVO"
  },
  ...
]
```

### 6. Verifica en el frontend

**Inicia el servidor Next.js:**
```powershell
cd C:\Users\Itami\APP-VET\nx-vet
npm run dev
```

**Accede a:**
- Página de Proveedores: http://localhost:3000/proveedores
- Página de Compras: http://localhost:3000/compras

**Verifica que:**
- ✅ En `/proveedores` aparezcan los 5 proveedores
- ✅ En `/compras` el selector de "Proveedor" muestre los 5 nombres
- ✅ Al crear una compra, se pueda seleccionar un proveedor

## Solución de Problemas

### Error: "Database 'veterinaria_saas' already exists"
Es normal si ya habías ejecutado el script antes. El script incluye:
```sql
DROP DATABASE IF EXISTS veterinaria_saas;
CREATE DATABASE veterinaria_saas;
```
Esto **eliminará y recreará** toda la base de datos. Si quieres mantener datos existentes, **NO ejecutes el script completo**.

### Error: "Access denied for user"
```powershell
# Verifica tu usuario y contraseña
mysql -u root -p
# Ingresa la contraseña cuando se solicite
```

### Verificar conexión MySQL
```powershell
# Verifica que MySQL esté corriendo
Get-Service MySQL*

# Si no está corriendo, inícialo
Start-Service MySQL80  # o el nombre de tu servicio
```

### Problemas con application.properties
Verifica que tengas estas configuraciones en `app/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria_saas
spring.datasource.username=root
spring.datasource.password=Sasuke_77920!!
```

## 🎯 Checklist de Verificación

- [ ] SETUP-DATABASE.sql ejecutado sin errores
- [ ] Base de datos `veterinaria_saas` creada
- [ ] ~20 tablas creadas (incluyendo `proveedor`)
- [ ] 5 proveedores insertados
- [ ] 2 tenants creados (VET001, VET002)
- [ ] 4 usuarios creados
- [ ] Backend responde en `/api/tenant/proveedores`
- [ ] Frontend muestra proveedores en `/proveedores`
- [ ] Selector de proveedores funciona en `/compras`

## 📞 Datos Insertados Automáticamente

### 🔐 Credenciales de Acceso
- **Super Admin**: superadmin / admin123
- **Vet 1 Admin**: admin_vet1 / admin123
- **Vet 1 Doctor**: drjuan / admin123
- **Vet 2 Admin**: admin_vet2 / admin123
- **Vet 2 Doctor**: drana / admin123

### 📦 Datos de Ejemplo
- ✅ 3 Planes de suscripción
- ✅ 2 Veterinarias (Tenants)
- ✅ 4 Usuarios del sistema
- ✅ 2 Doctores registrados
- ✅ 5 Clientes registrados
- ✅ 15 Productos en catálogo
- ✅ **5 Proveedores** (Bayer, MSD, Zoetis, Pet Supply, Distribuidora)
- ✅ 5 Especies disponibles
- ✅ 18 Razas disponibles

---

**¡Listo! Ahora tu sistema tiene TODO configurado con una sola ejecución.**
