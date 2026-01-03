# ❌ PROBLEMA ENCONTRADO

## 🔍 Diagnóstico:
La base de datos actual NO coincide con las entidades Java del backend. Los nombres de columnas son diferentes:

**Base de Datos actual:**
- `tenant_code`
- `email` (en tenant)
- `dias_prueba_restantes`
- `ultimo_login`

**Backend Java espera:**
- `codigo_tenant`
- `email_contacto`
- `dias_trial`
- `ultimo_acceso`

## ✅ SOLUCIÓN:

### Opción 1: Ejecutar SQL manualmente (RECOMENDADO)

1. **Abre MySQL Workbench**

2. **Copia y pega el contenido completo de: `SETUP-DATABASE.sql`**

3. **Ejecuta todo el script** (Ctrl + Shift + Enter)

4. **Espera 10-15 segundos**

### Opción 2: Desde PowerShell

```powershell
cd C:\Users\Itami\APP-VET
Get-Content SETUP-DATABASE.sql | & "C:\Program Files\MySQL\MySQL Server 9.5\bin\mysql.exe" -u root -p"Sasuke_77920!!"
```

## 🔑 Credenciales después de ejecutar:
```
Usuario: admin_vet1
Contraseña: admin123
```

## ⚠️ IMPORTANTE:
El archivo `SETUP-DATABASE.sql` tiene el hash BCrypt correcto que fue **generado y verificado** con el backend:
```
$2a$10$oLJHyLYLScCRvLm/vZSfMuazNV9vq3Ry/VmKLIHpkk1pYeQEksHwy
```

## 📝 Próximos pasos después de ejecutar el SQL:
1. Backend ya está corriendo ✅
2. Ejecuta el SQL en MySQL Workbench
3. Refresca el login en el navegador
4. Intenta login con: `admin_vet1` / `admin123`
