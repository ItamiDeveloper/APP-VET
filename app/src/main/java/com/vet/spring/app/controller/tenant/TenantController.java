package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.tenantDto.TenantDTO;
import com.vet.spring.app.dto.tenantDto.TenantRegistroDTO;
import com.vet.spring.app.service.tenantService.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Tenants (Veterinarias)", description = "Registro y gestión de veterinarias en el sistema multi-tenant")
public class TenantController {

    private final TenantService tenantService;

    @Operation(
            summary = "Registrar nueva veterinaria (Público)",
            description = """
                    Endpoint público para el registro de nuevas veterinarias desde el landing page.
                    
                    Crea:
                    - Tenant (veterinaria)
                    - Suscripción con período de prueba
                    - Usuario administrador
                    
                    No requiere autenticación.
                    """
    )
    @PostMapping("/public/tenants/register")
    public ResponseEntity<TenantDTO> registrarTenant(@RequestBody TenantRegistroDTO dto) {
        try {
            TenantDTO tenant = tenantService.registrarTenant(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(tenant);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar tenant: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Listar todas las veterinarias",
            description = "Retorna todas las veterinarias registradas en el sistema. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/super-admin/tenants")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @Operation(
            summary = "Obtener veterinaria por ID",
            description = "Retorna los detalles de una veterinaria específica. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/super-admin/tenants/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable Integer id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @Operation(
            summary = "Obtener veterinaria por código",
            description = "Busca una veterinaria por su código único. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/super-admin/tenants/codigo/{codigo}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDTO> getTenantByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(tenantService.getTenantByCodigo(codigo));
    }

    @Operation(
            summary = "Actualizar veterinaria",
            description = "Actualiza la información de una veterinaria. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PutMapping("/super-admin/tenants/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDTO> actualizarTenant(@PathVariable Integer id, @RequestBody TenantDTO dto) {
        return ResponseEntity.ok(tenantService.actualizarTenant(id, dto));
    }

    @Operation(
            summary = "Cambiar plan de suscripción",
            description = "Cambia el plan de suscripción de una veterinaria. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PatchMapping("/super-admin/tenants/{id}/plan")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> cambiarPlan(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        tenantService.cambiarPlan(id, body.get("idPlan"));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Suspender veterinaria",
            description = "Suspende una veterinaria por falta de pago o violación de términos. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PatchMapping("/super-admin/tenants/{id}/suspender")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> suspenderTenant(@PathVariable Integer id) {
        tenantService.suspenderTenant(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Reactivar veterinaria",
            description = "Reactiva una veterinaria previamente suspendida. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PatchMapping("/super-admin/tenants/{id}/reactivar")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> reactivarTenant(@PathVariable Integer id) {
        tenantService.reactivarTenant(id);
        return ResponseEntity.ok().build();
    }
}
