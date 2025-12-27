package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.mascotaDto.EspecieDTO;
import com.vet.spring.app.service.tenantService.EspecieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
}
