# Script de prueba completa de endpoints
$ErrorActionPreference = "Continue"
$totalTests = 0
$passed = 0
$failed = 0

Write-Host ""
Write-Host "===============================================" -ForegroundColor Yellow
Write-Host "  PRUEBA COMPLETA DE ENDPOINTS - VETERINARIA SAAS" -ForegroundColor Yellow
Write-Host "===============================================" -ForegroundColor Yellow
Write-Host ""

# 1. AUTENTICACIÓN
Write-Host "1. AUTENTICACION" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray
try {
    $loginBody = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json
    
    $loginResp = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -Body $loginBody `
        -ContentType "application/json"
    
    $global:JWT_TOKEN = $loginResp.token
    $global:TENANT_ID = $loginResp.tenantId
    
    Write-Host "✓ POST /api/auth/login" -ForegroundColor Green
    Write-Host "  Usuario: $($loginResp.username) | Tenant: $($loginResp.tenantId)" -ForegroundColor Gray
    $totalTests++; $passed++
} catch {
    Write-Host "✗ POST /api/auth/login - Error: $($_.Exception.Message)" -ForegroundColor Red
    $totalTests++; $failed++
    Write-Host ""
    Write-Host "No se puede continuar sin autenticación." -ForegroundColor Red
    exit 1
}

Write-Host ""

# Headers para requests autenticados
$headers = @{
    Authorization = "Bearer $global:JWT_TOKEN"
}

# 2. CATÁLOGOS
Write-Host "2. CATALOGOS" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/especies" -Headers $headers
    Write-Host "✓ GET /api/tenant/especies - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/especies - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/razas" -Headers $headers
    Write-Host "✓ GET /api/tenant/razas - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/razas - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/doctores" -Headers $headers
    Write-Host "✓ GET /api/tenant/doctores - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/doctores - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/roles" -Headers $headers
    Write-Host "✓ GET /api/tenant/roles - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/roles - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 3. CLIENTES
Write-Host "3. CLIENTES" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/clientes" -Headers $headers
    Write-Host "✓ GET /api/tenant/clientes - $($resp.Count) registros" -ForegroundColor Green
    if ($resp.Count -gt 0) {
        $global:CLIENTE_ID = $resp[0].idCliente
        Write-Host "  ID Cliente ejemplo: $global:CLIENTE_ID" -ForegroundColor Gray
    }
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/clientes - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 4. MASCOTAS
Write-Host "4. MASCOTAS" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/mascotas" -Headers $headers
    Write-Host "✓ GET /api/tenant/mascotas - $($resp.Count) registros" -ForegroundColor Green
    if ($resp.Count -gt 0) {
        $global:MASCOTA_ID = $resp[0].idMascota
        Write-Host "  ID Mascota ejemplo: $global:MASCOTA_ID" -ForegroundColor Gray
    }
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/mascotas - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 5. CITAS
Write-Host "5. CITAS" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/citas" -Headers $headers
    Write-Host "✓ GET /api/tenant/citas - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/citas - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 6. HISTORIAS CLÍNICAS
Write-Host "6. HISTORIAS CLINICAS" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/historias" -Headers $headers
    Write-Host "✓ GET /api/tenant/historias - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/historias - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 7. INVENTARIO
Write-Host "7. INVENTARIO" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/inventario" -Headers $headers
    Write-Host "✓ GET /api/tenant/inventario - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/inventario - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 8. VENTAS
Write-Host "8. VENTAS" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/ventas" -Headers $headers
    Write-Host "✓ GET /api/tenant/ventas - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/ventas - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 9. COMPRAS
Write-Host "9. COMPRAS" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/compras" -Headers $headers
    Write-Host "✓ GET /api/tenant/compras - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/compras - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 10. ESTADÍSTICAS DEL DASHBOARD
Write-Host "10. ESTADISTICAS DEL DASHBOARD" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/estadisticas/citas-por-estado" -Headers $headers
    Write-Host "✓ GET /api/tenant/estadisticas/citas-por-estado" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/estadisticas/citas-por-estado - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/estadisticas/ingresos-mensuales" -Headers $headers
    Write-Host "✓ GET /api/tenant/estadisticas/ingresos-mensuales" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/estadisticas/ingresos-mensuales - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/estadisticas/top-productos" -Headers $headers
    Write-Host "✓ GET /api/tenant/estadisticas/top-productos" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/estadisticas/top-productos - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/estadisticas/mascotas-por-especie" -Headers $headers
    Write-Host "✓ GET /api/tenant/estadisticas/mascotas-por-especie" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/estadisticas/mascotas-por-especie - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# 11. USUARIOS
Write-Host "11. USUARIOS" -ForegroundColor Cyan
Write-Host "-----------------------------------------------" -ForegroundColor Gray

try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/tenant/usuarios" -Headers $headers
    Write-Host "✓ GET /api/tenant/usuarios - $($resp.Count) registros" -ForegroundColor Green
    $totalTests++; $passed++
} catch {
    Write-Host "✗ GET /api/tenant/usuarios - Error" -ForegroundColor Red
    $totalTests++; $failed++
}

Write-Host ""

# RESUMEN FINAL
Write-Host "===============================================" -ForegroundColor Yellow
Write-Host "  RESUMEN DE PRUEBAS" -ForegroundColor Yellow
Write-Host "===============================================" -ForegroundColor Yellow
Write-Host "Total de pruebas: $totalTests" -ForegroundColor White
Write-Host "Exitosas: $passed" -ForegroundColor Green
Write-Host "Fallidas: $failed" -ForegroundColor Red
$successRate = [math]::Round(($passed / $totalTests) * 100, 2)
Write-Host "Tasa de éxito: $successRate%" -ForegroundColor $(if($successRate -ge 80){"Green"}else{"Yellow"})
Write-Host ""
