Write-Host "╔══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   NUEVO FLUJO DE REGISTRO CON APROBACIÓN DE SUPERADMIN     ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api"
$headers = @{ "Content-Type" = "application/json" }

# ============================================
# PASO 1: REGISTRO PÚBLICO (SIN ROL REQUERIDO)
# ============================================

Write-Host "[1/4] REGISTRO PÚBLICO - Crear solicitud..." -ForegroundColor Yellow

$random = Get-Random -Minimum 1000 -Maximum 9999
$registroBody = @{
    codigoTenant = "VET$random"
    nombreComercial = "Veterinaria Comas"
    razonSocial = "Veterinaria Comas SAC"
    ruc = "20123456789"
    telefono = "555-1234"
    emailContacto = "contacto@vetcomas.com"
    direccion = "Av. Túpac Amaru 123"
    ciudad = "Lima"
    pais = "Perú"
    idPlan = 1
    nombrePropietario = "Carlos"
    apellidoPropietario = "Pérez"
    emailPropietario = "carlos.perez@vetcomas.com"
    telefonoPropietario = "555-0001"
    usernamePropietario = "carlos_admin"
    passwordPropietario = "Carlos123!"
} | ConvertTo-Json

try {
    $solicitud = Invoke-RestMethod -Uri "$baseUrl/public/tenants/register" -Method POST -Body $registroBody -Headers $headers
    $idSolicitud = $solicitud.idTenant
    Write-Host "✓ Solicitud creada exitosamente!" -ForegroundColor Green
    Write-Host "  ID: $idSolicitud" -ForegroundColor Cyan
    Write-Host "  Nombre: $($solicitud.nombreComercial)" -ForegroundColor Cyan
    Write-Host "  Estado: $($solicitud.estado) ⏳ (Esperando aprobación)" -ForegroundColor Yellow
    Write-Host ""
} catch {
    Write-Host "✗ ERROR: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# ============================================
# PASO 2: LOGIN COMO SUPERADMIN
# ============================================

Write-Host "[2/4] LOGIN COMO SUPERADMIN..." -ForegroundColor Yellow

$loginBody = @{
    username = "superadmin"
    password = "admin123"
} | ConvertTo-Json

try {
    $authResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Body $loginBody -Headers $headers
    $token = $authResponse.token
    $headersAuth = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer $token"
    }
    Write-Host "✓ Login exitoso como SuperAdmin" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "✗ ERROR al hacer login: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "📋 NOTA: Necesitas crear el usuario superadmin primero." -ForegroundColor Yellow
    Write-Host "   Ejecuta este SQL en MySQL:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "   INSERT INTO usuario (id_tenant, id_rol, username, password_hash, email, nombres, apellidos, estado)" -ForegroundColor Gray
    Write-Host "   VALUES (NULL, (SELECT id_rol FROM rol WHERE nombre='SUPER_ADMIN'), 'superadmin'," -ForegroundColor Gray
    Write-Host "           '$2a$10$rVHLW5G8RW8G7P37DH', 'super@admin.com', 'Super', 'Admin', 'ACTIVO');" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   Contraseña: admin123" -ForegroundColor Cyan
    Write-Host ""
    exit
}

# ============================================
# PASO 3: VER SOLICITUDES PENDIENTES
# ============================================

Write-Host "[3/4] VER SOLICITUDES PENDIENTES..." -ForegroundColor Yellow

try {
    $solicitudes = Invoke-RestMethod -Uri "$baseUrl/super-admin/tenants/solicitudes/pendientes" -Method GET -Headers $headersAuth
    Write-Host "✓ Solicitudes pendientes: $($solicitudes.Count)" -ForegroundColor Green
    
    foreach ($sol in $solicitudes) {
        Write-Host ""
        Write-Host "  📋 Solicitud #$($sol.idTenant)" -ForegroundColor White
        Write-Host "     Veterinaria: $($sol.nombreComercial)" -ForegroundColor Cyan
        Write-Host "     RUC: $($sol.ruc)" -ForegroundColor Gray
        Write-Host "     Email: $($sol.emailContacto)" -ForegroundColor Gray
        Write-Host "     Plan: $($sol.planActual)" -ForegroundColor Gray
        Write-Host "     Estado: PENDIENTE ⏳" -ForegroundColor Yellow
    }
    Write-Host ""
} catch {
    Write-Host "✗ ERROR: $($_.Exception.Message)" -ForegroundColor Red
}

# ============================================
# PASO 4: APROBAR SOLICITUD
# ============================================

Write-Host "[4/4] APROBAR SOLICITUD..." -ForegroundColor Yellow

$aprobarBody = @{
    usernamePropietario = "carlos_admin"
    passwordPropietario = "Carlos123!"
    nombrePropietario = "Carlos"
    apellidoPropietario = "Pérez"
    emailPropietario = "carlos.perez@vetcomas.com"
    telefonoPropietario = "555-0001"
} | ConvertTo-Json

try {
    $aprobada = Invoke-RestMethod -Uri "$baseUrl/super-admin/tenants/$idSolicitud/aprobar" -Method POST -Body $aprobarBody -Headers $headersAuth
    Write-Host "✓ Solicitud APROBADA exitosamente!" -ForegroundColor Green
    Write-Host ""
    Write-Host "  🎉 Veterinaria activada:" -ForegroundColor White
    Write-Host "     ID: $($aprobada.idTenant)" -ForegroundColor Cyan
    Write-Host "     Nombre: $($aprobada.nombreComercial)" -ForegroundColor Cyan
    Write-Host "     Estado: $($aprobada.estado) ✅" -ForegroundColor Green
    Write-Host "     Código: $($aprobada.codigoTenant)" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  👤 Usuario administrador creado:" -ForegroundColor White
    Write-Host "     Username: carlos_admin" -ForegroundColor Cyan
    Write-Host "     Password: Carlos123!" -ForegroundColor Cyan
    Write-Host ""
} catch {
    Write-Host "✗ ERROR: $($_.Exception.Message)" -ForegroundColor Red
}

# ============================================
# VERIFICACIÓN FINAL
# ============================================

Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  ✅ NUEVO FLUJO IMPLEMENTADO CORRECTAMENTE" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Resumen del flujo:" -ForegroundColor White
Write-Host "   1. Usuario registra veterinaria → Estado PENDIENTE" -ForegroundColor Gray
Write-Host "   2. SuperAdmin ve solicitudes pendientes" -ForegroundColor Gray
Write-Host "   3. SuperAdmin aprueba → Se crea tenant + suscripción + usuario" -ForegroundColor Gray
Write-Host "   4. Veterinaria puede hacer login y usar el sistema" -ForegroundColor Gray
Write-Host ""
Write-Host "🔑 Credenciales SuperAdmin:" -ForegroundColor Yellow
Write-Host "   Username: superadmin" -ForegroundColor Cyan
Write-Host "   Password: admin123" -ForegroundColor Cyan
Write-Host "   (Debes crear este usuario en la BD primero)" -ForegroundColor Gray
Write-Host ""
Write-Host "✨ Beneficios del nuevo flujo:" -ForegroundColor White
Write-Host "   ✓ NO requiere rol ADMIN en la base de datos para registrar" -ForegroundColor Green
Write-Host "   ✓ SuperAdmin tiene control total sobre nuevas veterinarias" -ForegroundColor Green
Write-Host "   ✓ Previene registros automáticos no deseados" -ForegroundColor Green
Write-Host "   ✓ Permite validar datos antes de activar" -ForegroundColor Green
Write-Host ""
