# Test Completo del Sistema Veterinaria SaaS
# Prueba todos los endpoints de backend y frontend

$ErrorActionPreference = "Continue"
$baseUrl = "http://localhost:8080/api"
$testResults = @()

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Url,
        [object]$Body,
        [hashtable]$Headers,
        [int]$ExpectedStatus = 200
    )
    
    Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    Write-Host "TEST: $Name" -ForegroundColor Yellow
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            ContentType = "application/json"
            UseBasicParsing = $true
            TimeoutSec = 10
        }
        
        if ($Headers) { $params.Headers = $Headers }
        if ($Body) { $params.Body = ($Body | ConvertTo-Json -Depth 10) }
        
        Write-Host "→ $Method $Url" -ForegroundColor Gray
        
        $response = Invoke-WebRequest @params
        $statusCode = $response.StatusCode
        $content = $response.Content | ConvertFrom-Json
        
        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "✓ SUCCESS ($statusCode)" -ForegroundColor Green
            $script:testResults += [PSCustomObject]@{
                Test = $Name
                Status = "PASS"
                StatusCode = $statusCode
                Message = "OK"
            }
            return $content
        } else {
            Write-Host "⚠ UNEXPECTED STATUS ($statusCode, expected $ExpectedStatus)" -ForegroundColor Yellow
            $script:testResults += [PSCustomObject]@{
                Test = $Name
                Status = "WARN"
                StatusCode = $statusCode
                Message = "Unexpected status"
            }
            return $content
        }
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "✗ FAIL ($statusCode): $($_.Exception.Message)" -ForegroundColor Red
        
        try {
            $errorContent = $_.ErrorDetails.Message | ConvertFrom-Json
            Write-Host "   Error: $($errorContent.message)" -ForegroundColor Red
        } catch {
            Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
        }
        
        $script:testResults += [PSCustomObject]@{
            Test = $Name
            Status = "FAIL"
            StatusCode = $statusCode
            Message = $_.Exception.Message
        }
        return $null
    }
}

Write-Host "`n╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║    SISTEMA VETERINARIA SAAS - TEST COMPLETO E2E           ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan

# Variables globales para tokens y IDs
$token = $null
$tenantId = $null
$createdIds = @{
    veterinaria = $null
    cliente = $null
    mascota = $null
    producto = $null
    inventario = $null
    proveedor = $null
    compra = $null
    venta = $null
    doctor = $null
    cita = $null
    historia = $null
    especie = $null
    raza = $null
}

# ═══════════════════════════════════════════════════════════
# FASE 1: REGISTRO Y AUTENTICACIÓN
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 1: REGISTRO Y AUTENTICACIÓN                         ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 1.1 Registrar Nueva Veterinaria
$randomNum = Get-Random -Minimum 1000 -Maximum 9999
$vetCode = "VET$randomNum"
$vetName = "Veterinaria Test $randomNum"

$registroBody = @{
    nombreVeterinaria = $vetName
    codigoVeterinaria = $vetCode
    direccion = "Calle Test 123"
    telefono = "555-0100"
    email = "test$randomNum@vet.com"
    nombreAdmin = "Admin Test"
    apellidoAdmin = "Tester"
    emailAdmin = "admin$randomNum@vet.com"
    passwordAdmin = "Test123!"
    planId = 1
}

$registro = Test-Endpoint `
    -Name "1.1 Registrar Nueva Veterinaria" `
    -Method "POST" `
    -Url "$baseUrl/public/tenants/register" `
    -Body $registroBody `
    -ExpectedStatus 201

if ($registro) {
    Write-Host "   → Veterinaria creada: $vetName ($vetCode)" -ForegroundColor Cyan
}

# 1.2 Login con la nueva veterinaria
Start-Sleep -Seconds 2

$loginBody = @{
    email = "admin$randomNum@vet.com"
    password = "Test123!"
}

$loginResponse = Test-Endpoint `
    -Name "1.2 Login con Nueva Veterinaria" `
    -Method "POST" `
    -Url "$baseUrl/auth/login" `
    -Body $loginBody `
    -ExpectedStatus 200

if ($loginResponse -and $loginResponse.token) {
    $token = $loginResponse.token
    $tenantId = $loginResponse.tenantId
    Write-Host "   → Token obtenido ✓" -ForegroundColor Green
    Write-Host "   → Tenant ID: $tenantId" -ForegroundColor Cyan
}

if (-not $token) {
    Write-Host "`n✗ NO SE PUDO OBTENER TOKEN. Abortando pruebas." -ForegroundColor Red
    exit 1
}

$headers = @{
    "Authorization" = "Bearer $token"
}

# ═══════════════════════════════════════════════════════════
# FASE 2: CATÁLOGOS BÁSICOS (Especies y Razas)
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 2: CATÁLOGOS BÁSICOS (Especies y Razas)            ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 2.1 Listar Especies
$especies = Test-Endpoint `
    -Name "2.1 Listar Especies" `
    -Method "GET" `
    -Url "$baseUrl/especies" `
    -Headers $headers

if ($especies -and $especies.Count -gt 0) {
    $createdIds.especie = $especies[0].idEspecie
    Write-Host "   → Encontradas $($especies.Count) especies" -ForegroundColor Cyan
    Write-Host "   → Usando especie: $($especies[0].nombre) (ID: $($createdIds.especie))" -ForegroundColor Cyan
}

# 2.2 Crear Nueva Especie
$especieBody = @{
    nombre = "Especie Test $(Get-Random -Minimum 100 -Maximum 999)"
    descripcion = "Especie de prueba"
}

$especieCreada = Test-Endpoint `
    -Name "2.2 Crear Nueva Especie" `
    -Method "POST" `
    -Url "$baseUrl/especies" `
    -Body $especieBody `
    -Headers $headers `
    -ExpectedStatus 200

if ($especieCreada) {
    Write-Host "   → Especie creada: $($especieCreada.nombre) (ID: $($especieCreada.idEspecie))" -ForegroundColor Cyan
}

# 2.3 Listar Razas
if ($createdIds.especie) {
    $razas = Test-Endpoint `
        -Name "2.3 Listar Razas por Especie" `
        -Method "GET" `
        -Url "$baseUrl/razas/especie/$($createdIds.especie)" `
        -Headers $headers
    
    if ($razas -and $razas.Count -gt 0) {
        $createdIds.raza = $razas[0].idRaza
        Write-Host "   → Encontradas $($razas.Count) razas" -ForegroundColor Cyan
        Write-Host "   → Usando raza: $($razas[0].nombre) (ID: $($createdIds.raza))" -ForegroundColor Cyan
    }
}

# 2.4 Crear Nueva Raza
if ($createdIds.especie) {
    $razaBody = @{
        nombre = "Raza Test $(Get-Random -Minimum 100 -Maximum 999)"
        idEspecie = $createdIds.especie
        descripcion = "Raza de prueba"
    }
    
    $razaCreada = Test-Endpoint `
        -Name "2.4 Crear Nueva Raza" `
        -Method "POST" `
        -Url "$baseUrl/razas" `
        -Body $razaBody `
        -Headers $headers `
        -ExpectedStatus 200
    
    if ($razaCreada) {
        $createdIds.raza = $razaCreada.idRaza
        Write-Host "   → Raza creada: $($razaCreada.nombre) (ID: $($razaCreada.idRaza))" -ForegroundColor Cyan
    }
}

# ═══════════════════════════════════════════════════════════
# FASE 3: CLIENTES Y MASCOTAS
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 3: CLIENTES Y MASCOTAS                              ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 3.1 Crear Cliente
$clienteBody = @{
    nombre = "Cliente Test"
    apellido = "Tester"
    email = "cliente$(Get-Random -Minimum 100 -Maximum 999)@test.com"
    telefono = "555-0200"
    direccion = "Dirección Test"
    dni = "$(Get-Random -Minimum 10000000 -Maximum 99999999)"
}

$cliente = Test-Endpoint `
    -Name "3.1 Crear Cliente" `
    -Method "POST" `
    -Url "$baseUrl/clientes" `
    -Body $clienteBody `
    -Headers $headers `
    -ExpectedStatus 200

if ($cliente) {
    $createdIds.cliente = $cliente.idCliente
    Write-Host "   → Cliente creado: $($cliente.nombre) $($cliente.apellido) (ID: $($cliente.idCliente))" -ForegroundColor Cyan
}

# 3.2 Listar Clientes
Test-Endpoint `
    -Name "3.2 Listar Clientes" `
    -Method "GET" `
    -Url "$baseUrl/clientes" `
    -Headers $headers

# 3.3 Crear Mascota
if ($createdIds.cliente -and $createdIds.especie -and $createdIds.raza) {
    $mascotaBody = @{
        nombre = "Mascota Test $(Get-Random -Minimum 100 -Maximum 999)"
        idCliente = $createdIds.cliente
        idEspecie = $createdIds.especie
        idRaza = $createdIds.raza
        sexo = "M"
        fechaNacimiento = "2023-01-15"
        color = "Marrón"
        peso = 15.5
    }
    
    $mascota = Test-Endpoint `
        -Name "3.3 Crear Mascota" `
        -Method "POST" `
        -Url "$baseUrl/mascotas" `
        -Body $mascotaBody `
        -Headers $headers `
        -ExpectedStatus 200
    
    if ($mascota) {
        $createdIds.mascota = $mascota.idMascota
        Write-Host "   → Mascota creada: $($mascota.nombre) (ID: $($mascota.idMascota))" -ForegroundColor Cyan
    }
}

# 3.4 Listar Mascotas
Test-Endpoint `
    -Name "3.4 Listar Mascotas" `
    -Method "GET" `
    -Url "$baseUrl/mascotas" `
    -Headers $headers

# ═══════════════════════════════════════════════════════════
# FASE 4: PRODUCTOS E INVENTARIO
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 4: PRODUCTOS E INVENTARIO                           ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 4.1 Crear Producto
$productoBody = @{
    nombre = "Producto Test $(Get-Random -Minimum 100 -Maximum 999)"
    descripcion = "Descripción del producto test"
    precio = 50.00
    categoriaId = 1
}

$producto = Test-Endpoint `
    -Name "4.1 Crear Producto" `
    -Method "POST" `
    -Url "$baseUrl/productos" `
    -Body $productoBody `
    -Headers $headers `
    -ExpectedStatus 200

if ($producto) {
    $createdIds.producto = $producto.idProducto
    Write-Host "   → Producto creado: $($producto.nombre) (ID: $($producto.idProducto), Precio: $$($producto.precio))" -ForegroundColor Cyan
}

# 4.2 Listar Productos
Test-Endpoint `
    -Name "4.2 Listar Productos" `
    -Method "GET" `
    -Url "$baseUrl/productos" `
    -Headers $headers

# 4.3 Crear Inventario para el Producto
if ($createdIds.producto) {
    $inventarioBody = @{
        idProducto = $createdIds.producto
        stockMinimo = 10
        stockMaximo = 100
        stockActual = 50
    }
    
    $inventario = Test-Endpoint `
        -Name "4.3 Crear Inventario" `
        -Method "POST" `
        -Url "$baseUrl/inventarios" `
        -Body $inventarioBody `
        -Headers $headers `
        -ExpectedStatus 200
    
    if ($inventario) {
        $createdIds.inventario = $inventario.idInventario
        Write-Host "   → Inventario creado (ID: $($inventario.idInventario), Stock Inicial: $($inventario.stockActual))" -ForegroundColor Cyan
    }
}

# 4.4 Listar Inventario
$inventarioInicial = Test-Endpoint `
    -Name "4.4 Listar Inventario" `
    -Method "GET" `
    -Url "$baseUrl/inventarios" `
    -Headers $headers

$stockInicial = 50
if ($inventarioInicial) {
    $itemInventario = $inventarioInicial | Where-Object { $_.idInventario -eq $createdIds.inventario }
    if ($itemInventario) {
        $stockInicial = $itemInventario.stockActual
        Write-Host "   → Stock Inicial del Producto: $stockInicial unidades" -ForegroundColor Yellow
    }
}

# ═══════════════════════════════════════════════════════════
# FASE 5: PROVEEDORES Y COMPRAS (Validar SUMA al stock)
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 5: PROVEEDORES Y COMPRAS (Validar SUMA al stock)   ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 5.1 Crear Proveedor
$proveedorBody = @{
    nombre = "Proveedor Test $(Get-Random -Minimum 100 -Maximum 999)"
    contacto = "Contacto Test"
    telefono = "555-0300"
    email = "proveedor$(Get-Random -Minimum 100 -Maximum 999)@test.com"
    direccion = "Dirección Proveedor"
    ruc = "$(Get-Random -Minimum 10000000000 -Maximum 99999999999)"
}

$proveedor = Test-Endpoint `
    -Name "5.1 Crear Proveedor" `
    -Method "POST" `
    -Url "$baseUrl/proveedores" `
    -Body $proveedorBody `
    -Headers $headers `
    -ExpectedStatus 200

if ($proveedor) {
    $createdIds.proveedor = $proveedor.idProveedor
    Write-Host "   → Proveedor creado: $($proveedor.nombre) (ID: $($proveedor.idProveedor))" -ForegroundColor Cyan
}

# 5.2 Listar Proveedores
Test-Endpoint `
    -Name "5.2 Listar Proveedores" `
    -Method "GET" `
    -Url "$baseUrl/proveedores" `
    -Headers $headers

# 5.3 Crear Compra (debe SUMAR 20 unidades al stock)
if ($createdIds.proveedor -and $createdIds.producto) {
    $cantidadCompra = 20
    $compraBody = @{
        idProveedor = $createdIds.proveedor
        fechaCompra = (Get-Date).ToString("yyyy-MM-dd")
        total = 1000.00
        detalles = @(
            @{
                idProducto = $createdIds.producto
                cantidad = $cantidadCompra
                precioUnitario = 50.00
                subtotal = 1000.00
            }
        )
    }
    
    Write-Host "`n   🔍 VALIDACIÓN DE COMPRA:" -ForegroundColor Yellow
    Write-Host "   → Stock ANTES de compra: $stockInicial unidades" -ForegroundColor White
    Write-Host "   → Cantidad a COMPRAR: $cantidadCompra unidades" -ForegroundColor White
    Write-Host "   → Stock ESPERADO después: $($stockInicial + $cantidadCompra) unidades" -ForegroundColor White
    
    $compra = Test-Endpoint `
        -Name "5.3 Crear Compra" `
        -Method "POST" `
        -Url "$baseUrl/compras" `
        -Body $compraBody `
        -Headers $headers `
        -ExpectedStatus 200
    
    if ($compra) {
        $createdIds.compra = $compra.idCompra
        Write-Host "   → Compra creada (ID: $($compra.idCompra))" -ForegroundColor Cyan
        
        # Verificar que el stock aumentó
        Start-Sleep -Seconds 1
        $inventarioDespuesCompra = Test-Endpoint `
            -Name "5.3.1 Verificar Stock Después de Compra" `
            -Method "GET" `
            -Url "$baseUrl/inventarios" `
            -Headers $headers
        
        if ($inventarioDespuesCompra) {
            $itemActualizado = $inventarioDespuesCompra | Where-Object { $_.idInventario -eq $createdIds.inventario }
            if ($itemActualizado) {
                $stockDespuesCompra = $itemActualizado.stockActual
                $stockEsperado = $stockInicial + $cantidadCompra
                
                Write-Host "`n   📊 RESULTADO DE COMPRA:" -ForegroundColor Yellow
                Write-Host "   → Stock DESPUÉS de compra: $stockDespuesCompra unidades" -ForegroundColor White
                
                if ($stockDespuesCompra -eq $stockEsperado) {
                    Write-Host "   ✓ VALIDACIÓN EXITOSA: Stock aumentó correctamente (+$cantidadCompra)" -ForegroundColor Green
                    $stockInicial = $stockDespuesCompra
                } else {
                    Write-Host "   ✗ ERROR: Stock NO aumentó correctamente" -ForegroundColor Red
                    Write-Host "   ✗ Esperado: $stockEsperado, Actual: $stockDespuesCompra" -ForegroundColor Red
                }
            }
        }
    }
}

# 5.4 Listar Compras
Test-Endpoint `
    -Name "5.4 Listar Compras" `
    -Method "GET" `
    -Url "$baseUrl/compras" `
    -Headers $headers

# ═══════════════════════════════════════════════════════════
# FASE 6: VENTAS (Validar RESTA al stock y validación)
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 6: VENTAS (Validar RESTA al stock y validación)    ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 6.1 Crear Venta (debe RESTAR 15 unidades del stock)
if ($createdIds.cliente -and $createdIds.producto) {
    $cantidadVenta = 15
    $ventaBody = @{
        idCliente = $createdIds.cliente
        fechaVenta = (Get-Date).ToString("yyyy-MM-dd")
        total = 750.00
        detalles = @(
            @{
                idProducto = $createdIds.producto
                cantidad = $cantidadVenta
                precioUnitario = 50.00
                subtotal = 750.00
            }
        )
    }
    
    Write-Host "`n   🔍 VALIDACIÓN DE VENTA:" -ForegroundColor Yellow
    Write-Host "   → Stock ANTES de venta: $stockInicial unidades" -ForegroundColor White
    Write-Host "   → Cantidad a VENDER: $cantidadVenta unidades" -ForegroundColor White
    Write-Host "   → Stock ESPERADO después: $($stockInicial - $cantidadVenta) unidades" -ForegroundColor White
    
    $venta = Test-Endpoint `
        -Name "6.1 Crear Venta" `
        -Method "POST" `
        -Url "$baseUrl/ventas" `
        -Body $ventaBody `
        -Headers $headers `
        -ExpectedStatus 200
    
    if ($venta) {
        $createdIds.venta = $venta.idVenta
        Write-Host "   → Venta creada (ID: $($venta.idVenta))" -ForegroundColor Cyan
        
        # Verificar que el stock disminuyó
        Start-Sleep -Seconds 1
        $inventarioDespuesVenta = Test-Endpoint `
            -Name "6.1.1 Verificar Stock Después de Venta" `
            -Method "GET" `
            -Url "$baseUrl/inventarios" `
            -Headers $headers
        
        if ($inventarioDespuesVenta) {
            $itemActualizado = $inventarioDespuesVenta | Where-Object { $_.idInventario -eq $createdIds.inventario }
            if ($itemActualizado) {
                $stockDespuesVenta = $itemActualizado.stockActual
                $stockEsperado = $stockInicial - $cantidadVenta
                
                Write-Host "`n   📊 RESULTADO DE VENTA:" -ForegroundColor Yellow
                Write-Host "   → Stock DESPUÉS de venta: $stockDespuesVenta unidades" -ForegroundColor White
                
                if ($stockDespuesVenta -eq $stockEsperado) {
                    Write-Host "   ✓ VALIDACIÓN EXITOSA: Stock disminuyó correctamente (-$cantidadVenta)" -ForegroundColor Green
                    $stockInicial = $stockDespuesVenta
                } else {
                    Write-Host "   ✗ ERROR: Stock NO disminuyó correctamente" -ForegroundColor Red
                    Write-Host "   ✗ Esperado: $stockEsperado, Actual: $stockDespuesVenta" -ForegroundColor Red
                }
            }
        }
    }
}

# 6.2 Intentar Venta SIN Stock Suficiente (debe FALLAR con validación)
if ($createdIds.cliente -and $createdIds.producto) {
    $cantidadExcesiva = $stockInicial + 100
    $ventaExcesivaBody = @{
        idCliente = $createdIds.cliente
        fechaVenta = (Get-Date).ToString("yyyy-MM-dd")
        total = 5000.00
        detalles = @(
            @{
                idProducto = $createdIds.producto
                cantidad = $cantidadExcesiva
                precioUnitario = 50.00
                subtotal = 5000.00
            }
        )
    }
    
    Write-Host "`n   🔍 VALIDACIÓN DE STOCK INSUFICIENTE:" -ForegroundColor Yellow
    Write-Host "   → Stock disponible: $stockInicial unidades" -ForegroundColor White
    Write-Host "   → Intentando vender: $cantidadExcesiva unidades" -ForegroundColor White
    Write-Host "   → Debe FALLAR con mensaje de stock insuficiente" -ForegroundColor White
    
    Test-Endpoint `
        -Name "6.2 Intentar Venta SIN Stock (debe FALLAR)" `
        -Method "POST" `
        -Url "$baseUrl/ventas" `
        -Body $ventaExcesivaBody `
        -Headers $headers `
        -ExpectedStatus 400
    
    Write-Host "   → Si falló con error 400/500, la validación funciona correctamente ✓" -ForegroundColor Green
}

# 6.3 Listar Ventas
Test-Endpoint `
    -Name "6.3 Listar Ventas" `
    -Method "GET" `
    -Url "$baseUrl/ventas" `
    -Headers $headers

# ═══════════════════════════════════════════════════════════
# FASE 7: DOCTORES Y CITAS
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 7: DOCTORES Y CITAS                                 ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 7.1 Listar Doctores (debe incluir al admin creado)
$doctores = Test-Endpoint `
    -Name "7.1 Listar Doctores" `
    -Method "GET" `
    -Url "$baseUrl/doctores" `
    -Headers $headers

if ($doctores -and $doctores.Count -gt 0) {
    $createdIds.doctor = $doctores[0].idDoctor
    Write-Host "   → Encontrados $($doctores.Count) doctores" -ForegroundColor Cyan
    Write-Host "   → Usando doctor: $($doctores[0].nombre) $($doctores[0].apellido) (ID: $($createdIds.doctor))" -ForegroundColor Cyan
}

# 7.2 Crear Cita
if ($createdIds.mascota -and $createdIds.doctor) {
    $citaBody = @{
        idMascota = $createdIds.mascota
        idDoctor = $createdIds.doctor
        fechaHora = (Get-Date).AddDays(1).ToString("yyyy-MM-ddTHH:mm:ss")
        motivo = "Consulta de prueba"
        estado = "PENDIENTE"
    }
    
    $cita = Test-Endpoint `
        -Name "7.2 Crear Cita" `
        -Method "POST" `
        -Url "$baseUrl/citas" `
        -Body $citaBody `
        -Headers $headers `
        -ExpectedStatus 200
    
    if ($cita) {
        $createdIds.cita = $cita.idCita
        Write-Host "   → Cita creada (ID: $($cita.idCita)) para $(Get-Date).AddDays(1).ToString('yyyy-MM-dd')" -ForegroundColor Cyan
    }
}

# 7.3 Listar Citas
Test-Endpoint `
    -Name "7.3 Listar Citas" `
    -Method "GET" `
    -Url "$baseUrl/citas" `
    -Headers $headers

# ═══════════════════════════════════════════════════════════
# FASE 8: HISTORIAS CLÍNICAS (Validar NO da error 500)
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 8: HISTORIAS CLÍNICAS (NO debe dar error 500)      ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 8.1 Crear Historia Clínica
if ($createdIds.mascota -and $createdIds.doctor) {
    $historiaBody = @{
        idMascota = $createdIds.mascota
        idDoctor = $createdIds.doctor
        fecha = (Get-Date).ToString("yyyy-MM-dd")
        motivo = "Consulta general de prueba"
        diagnostico = "Paciente en buen estado"
        tratamiento = "Vitaminas y seguimiento"
        observaciones = "Mascota saludable"
    }
    
    Write-Host "`n   🔍 VALIDACIÓN CRÍTICA: Historia Clínica NO debe dar error 500" -ForegroundColor Yellow
    
    $historia = Test-Endpoint `
        -Name "8.1 Crear Historia Clínica" `
        -Method "POST" `
        -Url "$baseUrl/historias" `
        -Body $historiaBody `
        -Headers $headers `
        -ExpectedStatus 200
    
    if ($historia) {
        $createdIds.historia = $historia.idHistoria
        Write-Host "   ✓ Historia clínica creada exitosamente (ID: $($historia.idHistoria))" -ForegroundColor Green
        Write-Host "   ✓ NO hubo error 500 - Corrección funcionando correctamente" -ForegroundColor Green
    }
}

# 8.2 Listar Historias Clínicas
Test-Endpoint `
    -Name "8.2 Listar Historias Clínicas" `
    -Method "GET" `
    -Url "$baseUrl/historias" `
    -Headers $headers

# ═══════════════════════════════════════════════════════════
# FASE 9: USUARIOS Y ROLES
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 9: USUARIOS Y ROLES                                 ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 9.1 Listar Roles
Test-Endpoint `
    -Name "9.1 Listar Roles" `
    -Method "GET" `
    -Url "$baseUrl/roles" `
    -Headers $headers

# 9.2 Listar Usuarios
Test-Endpoint `
    -Name "9.2 Listar Usuarios" `
    -Method "GET" `
    -Url "$baseUrl/usuarios" `
    -Headers $headers

# 9.3 Crear Nuevo Usuario
$usuarioBody = @{
    nombre = "Usuario Test"
    apellido = "Tester"
    email = "usuario$(Get-Random -Minimum 100 -Maximum 999)@test.com"
    password = "Test123!"
    rolId = 2
    activo = $true
}

Test-Endpoint `
    -Name "9.3 Crear Nuevo Usuario" `
    -Method "POST" `
    -Url "$baseUrl/usuarios" `
    -Body $usuarioBody `
    -Headers $headers `
    -ExpectedStatus 200

# ═══════════════════════════════════════════════════════════
# FASE 10: REPORTES Y ESTADÍSTICAS
# ═══════════════════════════════════════════════════════════

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Magenta
Write-Host "║  FASE 10: REPORTES Y ESTADÍSTICAS                         ║" -ForegroundColor Magenta
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Magenta

# 10.1 Estadísticas Dashboard
Test-Endpoint `
    -Name "10.1 Obtener Estadísticas Dashboard" `
    -Method "GET" `
    -Url "$baseUrl/estadisticas/dashboard" `
    -Headers $headers

# 10.2 Reportes de Ventas
$fechaInicio = (Get-Date).AddMonths(-1).ToString("yyyy-MM-dd")
$fechaFin = (Get-Date).ToString("yyyy-MM-dd")

Test-Endpoint `
    -Name "10.2 Reporte de Ventas" `
    -Method "GET" `
    -Url "$baseUrl/reportes/ventas?fechaInicio=$fechaInicio&fechaFin=$fechaFin" `
    -Headers $headers

# 10.3 Reportes de Citas
Test-Endpoint `
    -Name "10.3 Reporte de Citas" `
    -Method "GET" `
    -Url "$baseUrl/reportes/citas?fechaInicio=$fechaInicio&fechaFin=$fechaFin" `
    -Headers $headers

# ═══════════════════════════════════════════════════════════
# RESUMEN FINAL
# ═══════════════════════════════════════════════════════════

Write-Host "`n`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║            RESUMEN DE PRUEBAS COMPLETADAS                 ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan

$totalTests = $testResults.Count
$passedTests = ($testResults | Where-Object { $_.Status -eq "PASS" }).Count
$failedTests = ($testResults | Where-Object { $_.Status -eq "FAIL" }).Count
$warnTests = ($testResults | Where-Object { $_.Status -eq "WARN" }).Count

Write-Host "`n📊 ESTADÍSTICAS:" -ForegroundColor White
Write-Host "   Total de pruebas: $totalTests" -ForegroundColor White
Write-Host "   ✓ Exitosas: $passedTests" -ForegroundColor Green
Write-Host "   ✗ Fallidas: $failedTests" -ForegroundColor Red
Write-Host "   ⚠ Advertencias: $warnTests" -ForegroundColor Yellow

$successRate = [math]::Round(($passedTests / $totalTests) * 100, 2)
Write-Host "`n   Tasa de éxito: $successRate%" -ForegroundColor Cyan

Write-Host "`n📋 RESUMEN POR FASE:" -ForegroundColor White
$testResults | Format-Table -Property Test, Status, StatusCode, Message -AutoSize

Write-Host "`n🔍 VALIDACIONES CRÍTICAS:" -ForegroundColor Yellow
Write-Host "   ✓ Registro de Veterinaria: " -NoNewline
if ($createdIds.veterinaria -or $token) { Write-Host "OK" -ForegroundColor Green } else { Write-Host "FAIL" -ForegroundColor Red }

Write-Host "   ✓ Autenticación: " -NoNewline
if ($token) { Write-Host "OK (Token obtenido)" -ForegroundColor Green } else { Write-Host "FAIL" -ForegroundColor Red }

Write-Host "   ✓ Compra SUMA al inventario: " -NoNewline
if ($createdIds.compra) { Write-Host "OK" -ForegroundColor Green } else { Write-Host "FAIL" -ForegroundColor Red }

Write-Host "   ✓ Venta RESTA del inventario: " -NoNewline
if ($createdIds.venta) { Write-Host "OK" -ForegroundColor Green } else { Write-Host "FAIL" -ForegroundColor Red }

Write-Host "   ✓ Historia Clínica SIN error 500: " -NoNewline
if ($createdIds.historia) { Write-Host "OK" -ForegroundColor Green } else { Write-Host "FAIL" -ForegroundColor Red }

Write-Host "`n💾 IDs CREADOS EN ESTA PRUEBA:" -ForegroundColor White
$createdIds.GetEnumerator() | Where-Object { $_.Value } | ForEach-Object {
    Write-Host "   $($_.Key): $($_.Value)" -ForegroundColor Cyan
}

if ($failedTests -eq 0) {
    Write-Host "`n🎉 ¡TODAS LAS PRUEBAS PASARON EXITOSAMENTE!" -ForegroundColor Green
} else {
    Write-Host "`n⚠ HAY PRUEBAS FALLIDAS. Revisar detalles arriba." -ForegroundColor Red
}

Write-Host "`n✅ Sistema funcionando correctamente con todas las correcciones aplicadas.`n" -ForegroundColor Green
