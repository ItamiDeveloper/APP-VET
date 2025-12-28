# Script para corregir todas las rutas de API en los servicios del frontend
$servicios = @(
    "citas.ts",
    "compras.ts",
    "doctores.ts",
    "especies.ts",
    "estadisticas.ts",
    "historias.ts",
    "inventarios.ts",
    "mascotas.ts",
    "razas.ts",
    "reportes.ts",
    "roles.ts",
    "usuarios.ts",
    "ventas.ts",
    "veterinarias.ts"
)

$ruta = "c:\Users\Eduardo\APP-VET\nx-vet\src\services"

foreach ($servicio in $servicios) {
    $archivo = Join-Path $ruta $servicio
    if (Test-Path $archivo) {
        $contenido = Get-Content $archivo -Raw
        
        # Reemplazar rutas de API
        $contenido = $contenido -replace "api\.get\('/api/([a-z\-]+)", "api.get('/api/tenant/`$1"
        $contenido = $contenido -replace "api\.post\('/api/([a-z\-]+)", "api.post('/api/tenant/`$1"
        $contenido = $contenido -replace "api\.put\('/api/([a-z\-]+)", "api.put('/api/tenant/`$1"
        $contenido = $contenido -replace "api\.delete\('/api/([a-z\-]+)", "api.delete('/api/tenant/`$1"
        $contenido = $contenido -replace "api\.patch\('/api/([a-z\-]+)", "api.patch('/api/tenant/`$1"
        
        # Guardar el archivo
        $contenido | Set-Content $archivo -NoNewline
        Write-Host "✓ Actualizado: $servicio"
    }
}

Write-Host "`n✅ Todos los servicios han sido actualizados"
