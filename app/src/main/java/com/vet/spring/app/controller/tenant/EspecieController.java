package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.mascotaDto.EspecieDTO;
import com.vet.spring.app.service.tenantService.EspecieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/especies")
@RequiredArgsConstructor
@Tag(name = "Especies", description = "API para gestión de especies (catálogo)")
public class EspecieController {

    private final EspecieService especieService;

    @GetMapping
    @Operation(summary = "Listar todas las especies")
    public ResponseEntity<List<EspecieDTO>> getAllEspecies() {
        return ResponseEntity.ok(especieService.getAllEspecies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una especie por ID")
    public ResponseEntity<EspecieDTO> getEspecieById(@PathVariable Integer id) {
        return ResponseEntity.ok(especieService.getEspecieById(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva especie")
    public ResponseEntity<EspecieDTO> createEspecie(@RequestBody EspecieDTO dto) {
        EspecieDTO created = especieService.createEspecie(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una especie existente")
    public ResponseEntity<EspecieDTO> updateEspecie(@PathVariable Integer id, @RequestBody EspecieDTO dto) {
        return ResponseEntity.ok(especieService.updateEspecie(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una especie")
    public ResponseEntity<Void> deleteEspecie(@PathVariable Integer id) {
        especieService.deleteEspecie(id);
        return ResponseEntity.noContent().build();
    }
}
