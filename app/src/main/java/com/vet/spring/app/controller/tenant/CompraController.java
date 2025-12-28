package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.compraDto.CompraDTO;
import com.vet.spring.app.service.tenantService.CompraService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/compras")
@RequiredArgsConstructor
@Tag(name = "Compras", description = "Gestión de compras")
public class CompraController {

    private final CompraService compraService;

    @GetMapping
    @Operation(summary = "Listar todas las compras")
    public ResponseEntity<List<CompraDTO>> getAllCompras() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(compraService.getAllComprasByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una compra por ID")
    public ResponseEntity<CompraDTO> getCompraById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(compraService.getCompraById(id, tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva compra")
    public ResponseEntity<CompraDTO> createCompra(@RequestBody CompraDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        CompraDTO created = compraService.createCompra(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una compra existente")
    public ResponseEntity<CompraDTO> updateCompra(@PathVariable Integer id, @RequestBody CompraDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(compraService.updateCompra(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una compra")
    public ResponseEntity<Void> deleteCompra(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        compraService.deleteCompra(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
