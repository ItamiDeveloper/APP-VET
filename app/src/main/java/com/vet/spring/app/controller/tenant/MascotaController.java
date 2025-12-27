package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.mascotaDto.MascotaDTO;
import com.vet.spring.app.service.tenantService.MascotaService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/mascotas")
@RequiredArgsConstructor
@Tag(name = "Mascotas", description = "Gestión de mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    @GetMapping
    @Operation(summary = "Listar todas las mascotas")
    public ResponseEntity<List<MascotaDTO>> getAllMascotas() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(mascotaService.getAllMascotasByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una mascota por ID")
    public ResponseEntity<MascotaDTO> getMascotaById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(mascotaService.getMascotaById(id, tenantId));
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener mascotas de un cliente")
    public ResponseEntity<List<MascotaDTO>> getMascotasByCliente(@PathVariable Integer idCliente) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(mascotaService.getMascotasByCliente(idCliente, tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva mascota")
    public ResponseEntity<MascotaDTO> createMascota(@RequestBody MascotaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        MascotaDTO created = mascotaService.createMascota(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una mascota existente")
    public ResponseEntity<MascotaDTO> updateMascota(@PathVariable Integer id, @RequestBody MascotaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(mascotaService.updateMascota(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una mascota (soft delete)")
    public ResponseEntity<Void> deleteMascota(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        mascotaService.deleteMascota(id, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar mascotas activas")
    public ResponseEntity<List<MascotaDTO>> getMascotasActivas() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(mascotaService.getMascotasActivas(tenantId));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar mascotas por nombre")
    public ResponseEntity<List<MascotaDTO>> buscarMascotas(@RequestParam String termino) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(mascotaService.buscarMascotas(termino, tenantId));
    }
}
