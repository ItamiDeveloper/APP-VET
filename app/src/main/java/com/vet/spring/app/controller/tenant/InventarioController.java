package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.inventarioDto.InventarioDTO;
import com.vet.spring.app.dto.inventarioDto.ProductoDTO;
import com.vet.spring.app.service.tenantService.InventarioService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Gestión de inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Listar todo el inventario")
    public ResponseEntity<List<InventarioDTO>> getAllInventario() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(inventarioService.getAllInventarioByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un inventario por ID")
    public ResponseEntity<InventarioDTO> getInventarioById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(inventarioService.getInventarioById(id, tenantId));
    }

    @GetMapping("/productos")
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(inventarioService.getAllProductosByTenant(tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo inventario")
    public ResponseEntity<InventarioDTO> createInventario(@RequestBody InventarioDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        InventarioDTO created = inventarioService.createInventario(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un inventario existente")
    public ResponseEntity<InventarioDTO> updateInventario(@PathVariable Integer id, @RequestBody InventarioDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(inventarioService.updateInventario(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un inventario")
    public ResponseEntity<Void> deleteInventario(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        inventarioService.deleteInventario(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
