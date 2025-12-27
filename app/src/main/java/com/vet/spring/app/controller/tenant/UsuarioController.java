package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.usuarioDto.UsuarioDTO;
import com.vet.spring.app.service.tenantService.UsuarioService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del tenant")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios del tenant")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(usuarioService.getAllUsuariosByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(usuarioService.getUsuarioById(id, tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario")
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        UsuarioDTO created = usuarioService.createUsuario(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(usuarioService.updateUsuario(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario (soft delete)")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        usuarioService.deleteUsuario(id, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar usuarios activos")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosActivos() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(usuarioService.getUsuariosActivos(tenantId));
    }

    @GetMapping("/por-rol/{idRol}")
    @Operation(summary = "Listar usuarios por rol")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByRol(@PathVariable Integer idRol) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(usuarioService.getUsuariosByRol(idRol, tenantId));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de un usuario")
    public ResponseEntity<UsuarioDTO> cambiarEstado(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(usuarioService.cambiarEstado(id, nuevoEstado, tenantId));
    }
}
