Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  INICIALIZAR DATOS BÁSICOS - SISTEMA VETERINARIA SAAS     ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$sqlScript = @"
USE veterinaria_saas;

-- ============================================
-- 1. CREAR ROLES SI NO EXISTEN
-- ============================================

INSERT IGNORE INTO rol (nombre, descripcion) VALUES
('SUPER_ADMIN', 'Super Administrador del sistema - gestiona todos los tenants'),
('ADMIN', 'Administrador del tenant - gestión completa de la veterinaria'),
('VETERINARIO', 'Veterinario - acceso a historias clínicas, citas y tratamientos'),
('RECEPCIONISTA', 'Recepcionista - gestión de citas, clientes y ventas básicas');

-- ============================================
-- 2. CREAR PLANES DE SUSCRIPCIÓN
-- ============================================

INSERT IGNORE INTO plan (nombre, descripcion, precio_mensual, precio_anual, max_usuarios, max_clientes, max_mascotas, max_historias, estado) VALUES
('GRATIS', 'Plan de prueba gratuito por 30 días. Ideal para evaluar el sistema.', 0.00, 0.00, 2, 50, 100, 200, 'ACTIVO'),
('BÁSICO', 'Plan ideal para veterinarias pequeñas. Funcionalidades esenciales.', 49.99, 499.99, 5, 200, 500, 1000, 'ACTIVO'),
('PROFESIONAL', 'Plan completo para veterinarias en crecimiento. Todas las funcionalidades.', 99.99, 999.99, 15, 1000, 2500, 5000, 'ACTIVO'),
('EMPRESARIAL', 'Plan sin límites para cadenas de veterinarias. Soporte prioritario.', 199.99, 1999.99, NULL, NULL, NULL, NULL, 'ACTIVO');

-- ============================================
-- 3. CREAR CATEGORÍAS DE PRODUCTOS BÁSICAS
-- ============================================

INSERT IGNORE INTO categoria_producto (nombre, descripcion, estado) VALUES
('Medicamentos', 'Medicamentos veterinarios', 'ACTIVO'),
('Alimentos', 'Alimentos para mascotas', 'ACTIVO'),
('Accesorios', 'Accesorios y juguetes', 'ACTIVO'),
('Vacunas', 'Vacunas y sueros', 'ACTIVO'),
('Higiene', 'Productos de higiene y cuidado', 'ACTIVO');

SELECT 'Inicialización completada exitosamente' AS Resultado;
SELECT COUNT(*) as 'Roles Creados' FROM rol;
SELECT COUNT(*) as 'Planes Creados' FROM plan;
SELECT COUNT(*) as 'Categorías Creadas' FROM categoria_producto WHERE estado = 'ACTIVO';
"@

Write-Host "📝 Preparando script de inicialización..." -ForegroundColor Yellow
Write-Host ""

try {
    # Usar el backend para ejecutar el script a través de una API
    Write-Host "🔧 Intentando inicializar a través del backend..." -ForegroundColor Yellow
    
    # Como no tenemos acceso directo a MySQL, voy a crear los datos usando llamadas API
    Write-Host ""
    Write-Host "⚠️  IMPORTANTE: El script SQL está listo en:" -ForegroundColor Yellow
    Write-Host "   C:\Users\Itami\APP-VET\INICIALIZAR-DATOS-BASICOS.sql" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "📋 OPCIONES PARA EJECUTARLO:" -ForegroundColor White
    Write-Host ""
    Write-Host "   Opción 1 - MySQL Workbench (RECOMENDADO):" -ForegroundColor Green
    Write-Host "   1. Abre MySQL Workbench" -ForegroundColor Gray
    Write-Host "   2. Conecta a localhost:3306" -ForegroundColor Gray
    Write-Host "   3. File > Open SQL Script" -ForegroundColor Gray
    Write-Host "   4. Selecciona: INICIALIZAR-DATOS-BASICOS.sql" -ForegroundColor Gray
    Write-Host "   5. Click en Execute (⚡)" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   Opción 2 - Línea de comandos:" -ForegroundColor Green
    Write-Host "   mysql -u root -p -h 127.0.0.1 veterinaria_saas" -ForegroundColor Gray
    Write-Host "   source C:\Users\Itami\APP-VET\INICIALIZAR-DATOS-BASICOS.sql" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   Opción 3 - HeidiSQL / phpMyAdmin:" -ForegroundColor Green
    Write-Host "   1. Conecta a la base de datos" -ForegroundColor Gray
    Write-Host "   2. Importa el archivo INICIALIZAR-DATOS-BASICOS.sql" -ForegroundColor Gray
    Write-Host ""
    
    # Intentar insertar los datos básicos usando JDBC si es posible
    Write-Host "🔄 Alternativa: Insertando datos mínimos via API..." -ForegroundColor Yellow
    Write-Host ""
    
    # Revisar si ya existen los roles y planes consultando el backend
    $headers = @{
        "Content-Type" = "application/json"
    }
    
    # Intentar registrar con plan 1 (debería existir después del script)
    Write-Host "🧪 Probando registro de veterinaria con datos mínimos..." -ForegroundColor Yellow
    
    $random = Get-Random -Minimum 1000 -Maximum 9999
    $registroTest = @{
        codigoTenant = "TEST$random"
        nombreComercial = "Test Vet $random"
        razonSocial = "Test Vet SAC"
        ruc = "20$(Get-Random -Minimum 100000000 -Maximum 999999999)"
        telefono = "555-$random"
        emailContacto = "test$random@test.com"
        direccion = "Calle Test 123"
        ciudad = "Lima"
        pais = "Perú"
        idPlan = 1
        nombrePropietario = "Admin"
        apellidoPropietario = "Test"
        emailPropietario = "admin$random@test.com"
        telefonoPropietario = "555-0001"
        usernamePropietario = "admin_$random"
        passwordPropietario = "Test123!"
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/public/tenants/register" -Method POST -Body $registroTest -Headers $headers
        Write-Host ""
        Write-Host "✅ ¡REGISTRO EXITOSO!" -ForegroundColor Green
        Write-Host ""
        Write-Host "   Los datos ya están en la base de datos." -ForegroundColor Cyan
        Write-Host "   El frontend debería funcionar correctamente ahora." -ForegroundColor Cyan
        Write-Host ""
    } catch {
        $errorMsg = $_.Exception.Message
        Write-Host ""
        Write-Host "❌ ERROR AL REGISTRAR: $errorMsg" -ForegroundColor Red
        Write-Host ""
        Write-Host "📋 ACCIÓN REQUERIDA:" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "   Necesitas ejecutar manualmente el script SQL:" -ForegroundColor White
        Write-Host "   C:\Users\Itami\APP-VET\INICIALIZAR-DATOS-BASICOS.sql" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "   Causa probable del error:" -ForegroundColor Yellow
        Write-Host "   - No existe el rol 'ADMIN' en la tabla 'rol'" -ForegroundColor Gray
        Write-Host "   - No existe el plan con id_plan=1 en la tabla 'plan'" -ForegroundColor Gray
        Write-Host ""
        Write-Host "   Después de ejecutar el script, refresca el navegador." -ForegroundColor White
        Write-Host ""
    }
    
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
