package com.vet.spring.app.controller.superadmin;

import com.vet.spring.app.dto.superadminDto.*;
import com.vet.spring.app.service.superadmin.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/superadmin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @GetMapping("/stats")
    public ResponseEntity<SuperAdminStatsDTO> getStats() {
        return ResponseEntity.ok(superAdminService.getStats());
    }

    @GetMapping("/veterinarias")
    public ResponseEntity<List<VeterinariaAdminDTO>> getAllVeterinarias() {
        return ResponseEntity.ok(superAdminService.getAllVeterinarias());
    }

    @GetMapping("/veterinarias/recientes")
    public ResponseEntity<List<VeterinariaAdminDTO>> getVeterinariasRecientes() {
        return ResponseEntity.ok(superAdminService.getVeterinariasRecientes());
    }

    @GetMapping("/veterinarias/{id}")
    public ResponseEntity<VeterinariaAdminDTO> getVeterinariaById(@PathVariable Integer id) {
        return ResponseEntity.ok(superAdminService.getVeterinariaById(id));
    }

    @PostMapping("/veterinarias")
    public ResponseEntity<VeterinariaAdminDTO> createVeterinaria(
            @RequestBody VeterinariaAdminDTO dto
    ) {
        return ResponseEntity.ok(superAdminService.createVeterinaria(dto));
    }

    @PutMapping("/veterinarias/{id}")
    public ResponseEntity<VeterinariaAdminDTO> updateVeterinaria(
            @PathVariable Integer id,
            @RequestBody VeterinariaAdminDTO dto
    ) {
        return ResponseEntity.ok(superAdminService.updateVeterinaria(id, dto));
    }

    @DeleteMapping("/veterinarias/{id}")
    public ResponseEntity<Void> deleteVeterinaria(@PathVariable Integer id) {
        superAdminService.deleteVeterinaria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioAdminDTO>> getAllUsuarios() {
        return ResponseEntity.ok(superAdminService.getAllUsuarios());
    }

    @GetMapping("/veterinarias/{veterinariaId}/usuarios")
    public ResponseEntity<List<UsuarioAdminDTO>> getUsuariosByVeterinaria(
            @PathVariable Integer veterinariaId
    ) {
        return ResponseEntity.ok(superAdminService.getUsuariosByVeterinaria(veterinariaId));
    }

    @PatchMapping("/usuarios/{usuarioId}/estado")
    public ResponseEntity<UsuarioAdminDTO> updateUsuarioEstado(
            @PathVariable Integer usuarioId,
            @RequestBody Map<String, String> body
    ) {
        String estado = body.get("estado");
        return ResponseEntity.ok(superAdminService.updateUsuarioEstado(usuarioId, estado));
    }

    @DeleteMapping("/usuarios/{usuarioId}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer usuarioId) {
        superAdminService.deleteUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
