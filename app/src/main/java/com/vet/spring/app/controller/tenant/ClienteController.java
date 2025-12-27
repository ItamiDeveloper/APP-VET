package com.vet.spring.app.controller.tenant;

import com.vet.spring.app.dto.clienteDto.ClienteDTO;
import com.vet.spring.app.service.tenantService.ClienteService;
import com.vet.spring.app.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(clienteService.getAllClientesByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(clienteService.getClienteById(id, tenantId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente")
    public ResponseEntity<ClienteDTO> createCliente(@RequestBody ClienteDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        ClienteDTO created = clienteService.createCliente(dto, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente existente")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Integer id, @RequestBody ClienteDTO dto) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(clienteService.updateCliente(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente (soft delete)")
    public ResponseEntity<Void> deleteCliente(@PathVariable Integer id) {
        Integer tenantId = TenantContext.getTenantId();
        clienteService.deleteCliente(id, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar clientes activos")
    public ResponseEntity<List<ClienteDTO>> getClientesActivos() {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(clienteService.getClientesActivos(tenantId));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nombre o documento")
    public ResponseEntity<List<ClienteDTO>> buscarClientes(@RequestParam String termino) {
        Integer tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(clienteService.buscarClientes(termino, tenantId));
    }
}
