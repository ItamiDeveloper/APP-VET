package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.tenantDto.PlanDTO;
import com.vet.spring.app.service.tenantService.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Planes", description = "Gestión de planes de suscripción para veterinarias")
public class PlanController {

    private final PlanService planService;

    @Operation(
            summary = "Obtener planes activos (Público)",
            description = "Retorna todos los planes de suscripción activos ordenados para mostrar en el landing page. No requiere autenticación."
    )
    @GetMapping("/public/planes")
    public ResponseEntity<List<PlanDTO>> getPlanesPublicos() {
        return ResponseEntity.ok(planService.getPlanesActivos());
    }

    @Operation(
            summary = "Listar todos los planes",
            description = "Retorna todos los planes incluyendo inactivos. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/super-admin/planes")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<PlanDTO>> getAllPlanes() {
        return ResponseEntity.ok(planService.getAllPlanes());
    }

    @Operation(
            summary = "Obtener plan por ID",
            description = "Retorna los detalles de un plan específico. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/super-admin/planes/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<PlanDTO> getPlanById(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @Operation(
            summary = "Crear nuevo plan",
            description = "Crea un nuevo plan de suscripción. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping("/super-admin/planes")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<PlanDTO> crearPlan(@RequestBody PlanDTO planDTO) {
        return ResponseEntity.ok(planService.crearPlan(planDTO));
    }

    @Operation(
            summary = "Actualizar plan existente",
            description = "Actualiza los detalles de un plan de suscripción. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PutMapping("/super-admin/planes/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<PlanDTO> actualizarPlan(@PathVariable Integer id, @RequestBody PlanDTO planDTO) {
        return ResponseEntity.ok(planService.actualizarPlan(id, planDTO));
    }

    @Operation(
            summary = "Cambiar estado del plan",
            description = "Activa o desactiva un plan de suscripción. Solo para super administradores.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PatchMapping("/super-admin/planes/{id}/estado")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        planService.cambiarEstadoPlan(id, body.get("estado"));
        return ResponseEntity.ok().build();
    }
}
