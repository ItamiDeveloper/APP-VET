package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.compraDto.ProveedorDTO;
import com.vet.spring.app.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "Gestión de proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @Operation(summary = "Listar todos los proveedores")
    public ResponseEntity<List<ProveedorDTO>> getAllProveedores() {
        return ResponseEntity.ok(proveedorService.getAllProveedores());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar proveedores activos")
    public ResponseEntity<List<ProveedorDTO>> getProveedoresActivos() {
        return ResponseEntity.ok(proveedorService.getProveedoresActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un proveedor por ID")
    public ResponseEntity<ProveedorDTO> getProveedorById(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.getProveedorById(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo proveedor")
    public ResponseEntity<ProveedorDTO> createProveedor(@RequestBody ProveedorDTO dto) {
        ProveedorDTO created = proveedorService.createProveedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un proveedor existente")
    public ResponseEntity<ProveedorDTO> updateProveedor(@PathVariable Integer id, @RequestBody ProveedorDTO dto) {
        return ResponseEntity.ok(proveedorService.updateProveedor(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un proveedor")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Integer id) {
        proveedorService.deleteProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
