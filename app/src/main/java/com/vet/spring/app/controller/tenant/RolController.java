package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.usuarioDto.RolDTO;
import com.vet.spring.app.entity.usuario.Rol;
import com.vet.spring.app.repository.usuarioRepository.RolRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tenant/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Gestión de roles del sistema")
public class RolController {

    private final RolRepository rolRepository;

    @GetMapping
    @Operation(summary = "Listar todos los roles")
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        List<Rol> roles = rolRepository.findAll();
        List<RolDTO> dtos = roles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un rol por ID")
    public ResponseEntity<RolDTO> getRolById(@PathVariable Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return ResponseEntity.ok(toDTO(rol));
    }

    private RolDTO toDTO(Rol entity) {
        RolDTO dto = new RolDTO();
        dto.setIdRol(entity.getIdRol());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }
}
