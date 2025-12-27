package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.citaDto.CitaDTO;
import com.vet.spring.app.service.tenantService.CitaService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tenant/citas")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "Gestión de citas veterinarias")
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    @Operation(summary = "Listar todas las citas")
    public ResponseEntity<List<CitaDTO>> getAllCitas() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.getAllCitasByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una cita por ID")
    public ResponseEntity<CitaDTO> getCitaById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.getCitaById(id, tenantId));
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Obtener citas de una mascota")
    public ResponseEntity<List<CitaDTO>> getCitasByMascota(@PathVariable Integer idMascota) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.getCitasByMascota(idMascota, tenantId));
    }

    @GetMapping("/doctor/{idDoctor}")
    @Operation(summary = "Obtener citas de un doctor")
    public ResponseEntity<List<CitaDTO>> getCitasByDoctor(@PathVariable Integer idDoctor) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.getCitasByDoctor(idDoctor, tenantId));
    }

    @GetMapping("/por-fecha")
    @Operation(summary = "Obtener citas por rango de fechas")
    public ResponseEntity<List<CitaDTO>> getCitasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.getCitasByFecha(inicio, fin, tenantId));
    }

    @GetMapping("/del-dia")
    @Operation(summary = "Obtener citas del día")
    public ResponseEntity<List<CitaDTO>> getCitasDelDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.getCitasDelDia(fecha, tenantId));
    }

    @GetMapping("/programadas")
    @Operation(summary = "Listar citas programadas")
    public ResponseEntity<List<CitaDTO>> getCitasProgramadas() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.getCitasProgramadas(tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva cita")
    public ResponseEntity<CitaDTO> createCita(@RequestBody CitaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        CitaDTO created = citaService.createCita(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cita existente")
    public ResponseEntity<CitaDTO> updateCita(@PathVariable Integer id, @RequestBody CitaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.updateCita(id, dto, tenantId));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de una cita")
    public ResponseEntity<CitaDTO> cambiarEstado(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(citaService.cambiarEstado(id, nuevoEstado, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar una cita")
    public ResponseEntity<Void> cancelarCita(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        citaService.cancelarCita(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
