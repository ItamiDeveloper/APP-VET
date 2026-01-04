# 🚀 Instalación Rápida - APP-VET

## 📋 Paso 1: Instalar Base de Datos (15 segundos)

### Opción A: MySQL Workbench (Recomendado)
1. Abre **MySQL Workbench**
2. `File` → `Open SQL Script`
3. Selecciona: `SETUP-DATABASE.sql`
4. Clic en **Execute** ⚡
5. ¡Listo!

### Opción B: Terminal
```powershell
mysql -u root -p < SETUP-DATABASE.sql
```

**✅ Esto crea:**
- Base de datos `veterinaria_saas`
- 20+ tablas
- 2 veterinarias de prueba
- 5 proveedores
- Usuarios, doctores, clientes, mascotas, productos de ejemplo

## 📋 Paso 2: Configurar Backend

### Verificar `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria_saas
spring.datasource.username=root
spring.datasource.password=Sasuke_77920!!
```

### Iniciar Backend
```powershell
cd app
mvn spring-boot:run
```

**Backend corriendo en:** http://localhost:8080

## 📋 Paso 3: Iniciar Frontend

```powershell
cd nx-vet
npm install  # Solo la primera vez
npm run dev
```

**Frontend corriendo en:** http://localhost:3000

## 🔐 Credenciales de Prueba

### Super Administrador
- Usuario: `superadmin`
- Password: `admin123`

### Veterinaria 1 (VET001 - Patitas Felices)
- Admin: `admin_vet1` / `admin123`
- Doctor: `drjuan` / `admin123`

### Veterinaria 2 (VET002 - Amigos Peludos)
- Admin: `admin_vet2` / `admin123`
- Doctor: `drana` / `admin123`

## 🧪 Verificar que Funciona

1. Ir a: http://localhost:3000/auth/login
2. Login con `admin_vet1` / `admin123`
3. Ir a **Proveedores**: http://localhost:3000/proveedores
   - Deberías ver 5 proveedores (Bayer, MSD, Zoetis, etc.)
4. Ir a **Compras**: http://localhost:3000/compras
   - El selector de proveedor debe mostrar los 5 nombres
5. Ir a **Ventas**: http://localhost:3000/ventas
   - Crear una venta seleccionando productos (precio se autocompleta)

## 📚 Documentación Completa

- **Frontend-Backend Sync**: [ACTUALIZACION-FRONTEND-BACKEND.md](ACTUALIZACION-FRONTEND-BACKEND.md)
- **SQL Detallado**: [INSTRUCCIONES-SQL-COMPLETO.md](INSTRUCCIONES-SQL-COMPLETO.md)

## ⚠️ Solución de Problemas

### Backend no inicia
```bash
# Verificar Java
java -version  # Debe ser Java 21

# Ver logs
cd app
mvn clean compile
```

### Frontend no inicia
```bash
cd nx-vet
# Limpiar y reinstalar
rm -rf node_modules .next
npm install
npm run dev
```

### MySQL no conecta
```bash
# Verificar que MySQL está corriendo
Get-Service MySQL*

# Iniciar si está detenido
Start-Service MySQL80
```

---

**¿Listo?** ¡Ahora tienes un sistema veterinario multi-tenant completo funcionando! 🎉
