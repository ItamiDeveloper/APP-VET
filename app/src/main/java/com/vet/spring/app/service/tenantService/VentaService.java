package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.ventaDto.VentaDTO;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.repository.ventaRepository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;

    @Transactional(readOnly = true)
    public List<VentaDTO> getAllVentasByTenant(Integer tenantId) {
        return ventaRepository.findAll().stream()
                .filter(v -> v.getTenant().getIdTenant().equals(tenantId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaDTO getVentaById(Integer id, Integer tenantId) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        if (!venta.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        return toDTO(venta);
    }

    @Transactional
    public VentaDTO createVenta(VentaDTO dto, Integer tenantId) {
        Venta venta = new Venta();
        
        // Set Tenant
        com.vet.spring.app.entity.tenant.Tenant tenant = new com.vet.spring.app.entity.tenant.Tenant();
        tenant.setIdTenant(tenantId);
        venta.setTenant(tenant);
        
        // Set Cliente
        com.vet.spring.app.entity.cliente.Cliente cliente = new com.vet.spring.app.entity.cliente.Cliente();
        cliente.setIdCliente(dto.getIdCliente());
        venta.setCliente(cliente);
        
        venta.setFecha(dto.getFecha());
        venta.setTotal(dto.getTotal());
        venta.setMetodoPago(dto.getMetodoPago());
        venta.setEstado(dto.getEstado() != null ? dto.getEstado() : "COMPLETADA");
        
        Venta saved = ventaRepository.save(venta);
        return toDTO(saved);
    }

    @Transactional
    public VentaDTO updateVenta(Integer id, VentaDTO dto, Integer tenantId) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        if (!venta.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        // Update Cliente if provided
        if (dto.getIdCliente() != null) {
            com.vet.spring.app.entity.cliente.Cliente cliente = new com.vet.spring.app.entity.cliente.Cliente();
            cliente.setIdCliente(dto.getIdCliente());
            venta.setCliente(cliente);
        }
        
        venta.setFecha(dto.getFecha());
        venta.setTotal(dto.getTotal());
        venta.setMetodoPago(dto.getMetodoPago());
        if (dto.getEstado() != null) {
            venta.setEstado(dto.getEstado());
        }
        
        Venta updated = ventaRepository.save(venta);
        return toDTO(updated);
    }

    @Transactional
    public void deleteVenta(Integer id, Integer tenantId) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        if (!venta.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        ventaRepository.deleteById(id);
    }

    private VentaDTO toDTO(Venta entity) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(entity.getIdVenta());
        dto.setIdTenant(entity.getTenant().getIdTenant());
        if (entity.getCliente() != null) {
            dto.setIdCliente(entity.getCliente().getIdCliente());
        }
        dto.setFecha(entity.getFecha());
        dto.setTotal(entity.getTotal());
        dto.setMetodoPago(entity.getMetodoPago());
        dto.setEstado(entity.getEstado());
        return dto;
    }
}
