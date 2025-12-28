package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.ventaDto.VentaDTO;
import com.vet.spring.app.service.tenantService.VentaService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    @Operation(summary = "Listar todas las ventas")
    public ResponseEntity<List<VentaDTO>> getAllVentas() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(ventaService.getAllVentasByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una venta por ID")
    public ResponseEntity<VentaDTO> getVentaById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(ventaService.getVentaById(id, tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva venta")
    public ResponseEntity<VentaDTO> createVenta(@RequestBody VentaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        VentaDTO created = ventaService.createVenta(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una venta existente")
    public ResponseEntity<VentaDTO> updateVenta(@PathVariable Integer id, @RequestBody VentaDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(ventaService.updateVenta(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una venta")
    public ResponseEntity<Void> deleteVenta(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        ventaService.deleteVenta(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
