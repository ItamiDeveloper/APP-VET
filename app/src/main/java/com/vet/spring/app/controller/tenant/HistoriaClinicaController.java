package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.historiaDto.HistoriaClinicaDTO;
import com.vet.spring.app.service.tenantService.HistoriaClinicaService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/historias")
@RequiredArgsConstructor
@Tag(name = "Historias Clínicas", description = "Gestión de historias clínicas")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping
    @Operation(summary = "Listar todas las historias clínicas")
    public ResponseEntity<List<HistoriaClinicaDTO>> getAllHistorias() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(historiaClinicaService.getAllHistoriasByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una historia clínica por ID")
    public ResponseEntity<HistoriaClinicaDTO> getHistoriaById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(historiaClinicaService.getHistoriaById(id, tenantId));
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Obtener historias clínicas de una mascota")
    public ResponseEntity<List<HistoriaClinicaDTO>> getHistoriasByMascota(@PathVariable Integer idMascota) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(historiaClinicaService.getHistoriasByMascota(idMascota, tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva historia clínica")
    public ResponseEntity<HistoriaClinicaDTO> createHistoria(@RequestBody HistoriaClinicaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        HistoriaClinicaDTO created = historiaClinicaService.createHistoria(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una historia clínica existente")
    public ResponseEntity<HistoriaClinicaDTO> updateHistoria(@PathVariable Integer id, @RequestBody HistoriaClinicaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(historiaClinicaService.updateHistoria(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una historia clínica")
    public ResponseEntity<Void> deleteHistoria(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        historiaClinicaService.deleteHistoria(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
