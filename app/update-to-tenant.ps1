# Script para actualizar DTOs y Mappers de Veterinaria a Tenant

$projectPath = "C:\Users\Eduardo\APP-VET\app\src\main\java\com\vet\spring\app"

# Actualizar DTOs: cambiar idVeterinaria por idTenant
$dtoFiles = @(
    "$projectPath\dto\usuarioDto\UsuarioDTO.java",
    "$projectPath\dto\mascotaDto\MascotaDTO.java",
    "$projectPath\dto\historiaDto\HistoriaClinicaDTO.java",
    "$projectPath\dto\inventarioDto\InventarioDTO.java"
)

foreach ($file in $dtoFiles) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw
        $content = $content -replace 'idVeterinaria', 'idTenant'
        Set-Content $file -Value $content -NoNewline
        Write-Host "Actualizado: $file"
    }
}

# Actualizar Mappers: cambiar getVeterinaria/setVeterinaria por getTenant/setTenant
$mapperFiles = @(
    "$projectPath\mapper\usuarioMapper\UsuarioMapper.java",
    "$projectPath\mapper\mascotaMapper\MascotaMapper.java",
    "$projectPath\mapper\historiaMapper\HistoriaClinicaMapper.java",
    "$projectPath\mapper\inventarioMapper\InventarioMapper.java"
)

foreach ($file in $mapperFiles) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw
        # Cambiar imports
        $content = $content -replace 'import com\.vet\.spring\.app\.entity\.veterinaria\.Veterinaria;', 'import com.vet.spring.app.entity.tenant.Tenant;'
        $content = $content -replace 'import com\.vet\.spring\.app\.entity\.veterinaria\.Estado;', ''
        # Cambiar getters/setters
        $content = $content -replace '\.getVeterinaria\(\)', '.getTenant()'
        $content = $content -replace '\.setVeterinaria\(', '.setTenant('
        $content = $content -replace '\.getIdVeterinaria\(\)', '.getIdTenant()'
        $content = $content -replace '\.setIdVeterinaria\(', '.setIdTenant('
        $content = $content -replace 'idVeterinaria', 'idTenant'
        $content = $content -replace 'Veterinaria v = new Veterinaria\(\); v\.setIdTenant', 'Tenant t = new Tenant(); t.setIdTenant'
        $content = $content -replace 'e\.setTenant\(v\)', 'e.setTenant(t)'
        $content = $content -replace 'Veterinaria', 'Tenant'
        Set-Content $file -Value $content -NoNewline
        Write-Host "Actualizado: $file"
    }
}

Write-Host "`nActualizacion completada!"
