# ========================================
# SCRIPT DE DIAGNÓSTICO RÁPIDO
# ========================================
# Verifica el estado del sistema completo

Write-Host "`n=== DIAGNÓSTICO DEL SISTEMA VET ===" -ForegroundColor Cyan
Write-Host "Fecha: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')`n" -ForegroundColor Gray

# ========================================
# 1. VERIFICAR PUERTOS
# ========================================
Write-Host "[1/5] Verificando puertos..." -ForegroundColor Yellow

$backend = netstat -ano | findstr ":8080"
$frontend = netstat -ano | findstr ":3000"
$mysql = netstat -ano | findstr ":3306"

if ($backend) {
    Write-Host "  ✅ Backend (puerto 8080): ACTIVO" -ForegroundColor Green
    Write-Host "     $backend" -ForegroundColor Gray
} else {
    Write-Host "  ❌ Backend (puerto 8080): INACTIVO" -ForegroundColor Red
    Write-Host "     Ejecutar: cd app && mvn spring-boot:run" -ForegroundColor Yellow
}

if ($frontend) {
    Write-Host "  ✅ Frontend (puerto 3000): ACTIVO" -ForegroundColor Green
    Write-Host "     $frontend" -ForegroundColor Gray
} else {
    Write-Host "  ❌ Frontend (puerto 3000): INACTIVO" -ForegroundColor Red
    Write-Host "     Ejecutar: cd nx-vet && npm run dev" -ForegroundColor Yellow
}

if ($mysql) {
    Write-Host "  ✅ MySQL (puerto 3306): ACTIVO" -ForegroundColor Green
    Write-Host "     $mysql" -ForegroundColor Gray
} else {
    Write-Host "  ❌ MySQL (puerto 3306): INACTIVO" -ForegroundColor Red
    Write-Host "     Iniciar MySQL desde XAMPP o servicios" -ForegroundColor Yellow
}

# ========================================
# 2. VERIFICAR ARCHIVOS DE CONFIGURACIÓN
# ========================================
Write-Host "`n[2/5] Verificando configuración..." -ForegroundColor Yellow

$appProperties = "C:\Users\Itami\APP-VET\app\src\main\resources\application.properties"
if (Test-Path $appProperties) {
    Write-Host "  ✅ application.properties existe" -ForegroundColor Green
    
    $dbUrl = Get-Content $appProperties | Select-String "spring.datasource.url"
    $dbUser = Get-Content $appProperties | Select-String "spring.datasource.username"
    $serverPort = Get-Content $appProperties | Select-String "server.port"
    
    Write-Host "     Base de datos: $($dbUrl -replace 'spring.datasource.url=', '')" -ForegroundColor Gray
    Write-Host "     Usuario DB: $($dbUser -replace 'spring.datasource.username=', '')" -ForegroundColor Gray
    Write-Host "     Puerto: $($serverPort -replace 'server.port=', '')" -ForegroundColor Gray
} else {
    Write-Host "  ❌ application.properties NO existe" -ForegroundColor Red
}

$packageJson = "C:\Users\Itami\APP-VET\nx-vet\package.json"
if (Test-Path $packageJson) {
    Write-Host "  ✅ package.json existe" -ForegroundColor Green
} else {
    Write-Host "  ❌ package.json NO existe" -ForegroundColor Red
}

# ========================================
# 3. VERIFICAR COMPILACIÓN
# ========================================
Write-Host "`n[3/5] Verificando compilación..." -ForegroundColor Yellow

$targetClasses = "C:\Users\Itami\APP-VET\app\target\classes"
if (Test-Path $targetClasses) {
    Write-Host "  ✅ Backend compilado (target/classes existe)" -ForegroundColor Green
    
    $historiaDto = "$targetClasses\com\vet\spring\app\dto\historiaDto\HistoriaClinicaDTO.class"
    if (Test-Path $historiaDto) {
        Write-Host "     ✅ HistoriaClinicaDTO.class compilado" -ForegroundColor Green
    } else {
        Write-Host "     ⚠️  HistoriaClinicaDTO.class NO encontrado" -ForegroundColor Yellow
    }
} else {
    Write-Host "  ❌ Backend NO compilado" -ForegroundColor Red
    Write-Host "     Ejecutar: cd app && mvn clean compile" -ForegroundColor Yellow
}

$nextBuild = "C:\Users\Itami\APP-VET\nx-vet\.next"
if (Test-Path $nextBuild) {
    Write-Host "  ✅ Frontend compilado (.next existe)" -ForegroundColor Green
} else {
    Write-Host "  ⚠️  Frontend sin build (primera vez ejecutar npm run dev)" -ForegroundColor Yellow
}

# ========================================
# 4. VERIFICAR LOGS (si existen)
# ========================================
Write-Host "`n[4/5] Verificando logs recientes..." -ForegroundColor Yellow

$backendLog = "C:\Users\Itami\APP-VET\app\logs\app.log"
if (Test-Path $backendLog) {
    Write-Host "  ✅ Log del backend encontrado" -ForegroundColor Green
    $lastErrors = Get-Content $backendLog -Tail 20 | Select-String "ERROR|Exception"
    if ($lastErrors) {
        Write-Host "     ⚠️  ERRORES RECIENTES:" -ForegroundColor Yellow
        $lastErrors | ForEach-Object { Write-Host "        $_" -ForegroundColor Red }
    } else {
        Write-Host "     ✅ Sin errores recientes" -ForegroundColor Green
    }
} else {
    Write-Host "  ℹ️  No hay log del backend (o no está configurado)" -ForegroundColor Gray
}

# ========================================
# 5. VERIFICAR ARCHIVOS CORREGIDOS
# ========================================
Write-Host "`n[5/5] Verificando correcciones aplicadas..." -ForegroundColor Yellow

$historiaPage = "C:\Users\Itami\APP-VET\nx-vet\src\app\historias\page.tsx"
$mascotasPage = "C:\Users\Itami\APP-VET\nx-vet\src\app\mascotas\page.tsx"
$citasPage = "C:\Users\Itami\APP-VET\nx-vet\src\app\citas\page.tsx"

# Verificar que NO tenga idVeterinaria hardcodeado
$historiaHardcoded = Get-Content $historiaPage | Select-String "idVeterinaria: 1"
$mascotasHardcoded = Get-Content $mascotasPage | Select-String "idVeterinaria: 1"
$citasHardcoded = Get-Content $citasPage | Select-String "idVeterinaria: 1"

if ($historiaHardcoded) {
    Write-Host "  ❌ historias/page.tsx TODAVÍA tiene idVeterinaria: 1" -ForegroundColor Red
} else {
    Write-Host "  ✅ historias/page.tsx corregido (sin hardcoding)" -ForegroundColor Green
}

if ($mascotasHardcoded) {
    Write-Host "  ❌ mascotas/page.tsx TODAVÍA tiene idVeterinaria: 1" -ForegroundColor Red
} else {
    Write-Host "  ✅ mascotas/page.tsx corregido (sin hardcoding)" -ForegroundColor Green
}

if ($citasHardcoded) {
    Write-Host "  ❌ citas/page.tsx TODAVÍA tiene idVeterinaria: 1" -ForegroundColor Red
} else {
    Write-Host "  ✅ citas/page.tsx corregido (sin hardcoding)" -ForegroundColor Green
}

# ========================================
# RESUMEN
# ========================================
Write-Host "`n=== RESUMEN ===" -ForegroundColor Cyan

$issues = @()

if (-not $backend) { $issues += "Backend no está corriendo" }
if (-not $frontend) { $issues += "Frontend no está corriendo" }
if (-not $mysql) { $issues += "MySQL no está corriendo" }
if (-not (Test-Path $targetClasses)) { $issues += "Backend no está compilado" }

if ($issues.Count -eq 0) {
    Write-Host "✅ TODO ESTÁ LISTO - El sistema debería funcionar" -ForegroundColor Green
    Write-Host "`nAcceder a: http://localhost:3000" -ForegroundColor Cyan
    Write-Host "API Backend: http://localhost:8080/api" -ForegroundColor Cyan
} else {
    Write-Host "⚠️  Hay $($issues.Count) problema(s):" -ForegroundColor Yellow
    $issues | ForEach-Object { Write-Host "   - $_" -ForegroundColor Red }
}

Write-Host "`n=== PRÓXIMOS PASOS ===" -ForegroundColor Cyan
Write-Host "1. Si algo está inactivo, iniciarlo según las instrucciones arriba" -ForegroundColor White
Write-Host "2. Verificar datos en MySQL ejecutando: mysql -u root -p" -ForegroundColor White
Write-Host "3. Probar crear historia clínica en: http://localhost:3000/historias" -ForegroundColor White
Write-Host "4. Ver detalles completos en: SOLUCIONES-PROBLEMAS-VISUALIZACION.md`n" -ForegroundColor White
