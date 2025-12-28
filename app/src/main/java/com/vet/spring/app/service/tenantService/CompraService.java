package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.compraDto.CompraDTO;
import com.vet.spring.app.entity.compra.Compra;
import com.vet.spring.app.repository.compraRepository.CompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;

    @Transactional(readOnly = true)
    public List<CompraDTO> getAllComprasByTenant(Integer tenantId) {
        return compraRepository.findAll().stream()
                .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompraDTO getCompraById(Integer id, Integer tenantId) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        if (!compra.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        return toDTO(compra);
    }

    @Transactional
    public CompraDTO createCompra(CompraDTO dto, Integer tenantId) {
        Compra compra = new Compra();
        compra.setFecha(dto.getFecha());
        compra.setTotal(dto.getTotal());
        
        Compra saved = compraRepository.save(compra);
        return toDTO(saved);
    }

    @Transactional
    public CompraDTO updateCompra(Integer id, CompraDTO dto, Integer tenantId) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        if (!compra.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        compra.setFecha(dto.getFecha());
        compra.setTotal(dto.getTotal());
        
        Compra updated = compraRepository.save(compra);
        return toDTO(updated);
    }

    @Transactional
    public void deleteCompra(Integer id, Integer tenantId) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        if (!compra.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        compraRepository.deleteById(id);
    }

    private CompraDTO toDTO(Compra entity) {
        CompraDTO dto = new CompraDTO();
        dto.setIdCompra(entity.getIdCompra());
        dto.setIdTenant(entity.getTenant().getIdTenant());
        dto.setIdProveedor(entity.getProveedor().getIdProveedor());
        dto.setFecha(entity.getFecha());
        dto.setTotal(entity.getTotal());
        dto.setEstado(entity.getEstado());
        return dto;
    }
}
