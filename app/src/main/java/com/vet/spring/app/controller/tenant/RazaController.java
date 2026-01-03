package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.mascotaDto.RazaDTO;
import com.vet.spring.app.service.tenantService.RazaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/razas")
@RequiredArgsConstructor
@Tag(name = "Razas", description = "API para gestión de razas (catálogo)")
public class RazaController {

    private final RazaService razaService;

    @GetMapping
    @Operation(summary = "Listar todas las razas")
    public ResponseEntity<List<RazaDTO>> getAllRazas() {
        return ResponseEntity.ok(razaService.getAllRazas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una raza por ID")
    public ResponseEntity<RazaDTO> getRazaById(@PathVariable Integer id) {
        return ResponseEntity.ok(razaService.getRazaById(id));
    }

    @GetMapping("/especie/{idEspecie}")
    @Operation(summary = "Listar razas por especie")
    public ResponseEntity<List<RazaDTO>> getRazasByEspecie(@PathVariable Integer idEspecie) {
        return ResponseEntity.ok(razaService.getRazasByEspecie(idEspecie));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva raza")
    public ResponseEntity<RazaDTO> createRaza(@RequestBody RazaDTO dto) {
        RazaDTO created = razaService.createRaza(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una raza existente")
    public ResponseEntity<RazaDTO> updateRaza(@PathVariable Integer id, @RequestBody RazaDTO dto) {
        return ResponseEntity.ok(razaService.updateRaza(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una raza")
    public ResponseEntity<Void> deleteRaza(@PathVariable Integer id) {
        razaService.deleteRaza(id);
        return ResponseEntity.noContent().build();
    }
}
