package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.doctorDto.DoctorDTO;
import com.vet.spring.app.service.tenantService.DoctorService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/doctores")
@RequiredArgsConstructor
@Tag(name = "Doctores", description = "Gestión de doctores/veterinarios")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @Operation(summary = "Listar todos los doctores")
    public ResponseEntity<List<DoctorDTO>> getAllDoctores() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(doctorService.getAllDoctoresByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un doctor por ID")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(doctorService.getDoctorById(id, tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo doctor")
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        DoctorDTO created = doctorService.createDoctor(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un doctor existente")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Integer id, @RequestBody DoctorDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(doctorService.updateDoctor(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un doctor (soft delete)")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        doctorService.deleteDoctor(id, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar doctores activos")
    public ResponseEntity<List<DoctorDTO>> getDoctoresActivos() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(doctorService.getDoctoresActivos(tenantId));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar doctores por nombre o especialidad")
    public ResponseEntity<List<DoctorDTO>> buscarDoctores(@RequestParam String termino) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(doctorService.buscarDoctores(termino, tenantId));
    }
}
