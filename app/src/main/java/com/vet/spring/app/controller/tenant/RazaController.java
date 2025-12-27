package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.mascotaDto.RazaDTO;
import com.vet.spring.app.service.tenantService.RazaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/especie/{idEspecie}")
    @Operation(summary = "Listar razas por especie")
    public ResponseEntity<List<RazaDTO>> getRazasByEspecie(@PathVariable Integer idEspecie) {
        return ResponseEntity.ok(razaService.getRazasByEspecie(idEspecie));
    }
}
