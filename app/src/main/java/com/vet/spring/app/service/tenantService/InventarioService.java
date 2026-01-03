package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.inventarioDto.InventarioDTO;
import com.vet.spring.app.entity.inventario.Inventario;
import com.vet.spring.app.repository.inventarioRepository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    @Transactional(readOnly = true)
    public List<InventarioDTO> getAllInventarioByTenant(Integer tenantId) {
        return inventarioRepository.findAll().stream()
                .filter(i -> i.getTenant().getIdTenant().equals(tenantId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventarioDTO getInventarioById(Integer id, Integer tenantId) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        if (!inventario.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        return toDTO(inventario);
    }

    @Transactional
    public InventarioDTO createInventario(InventarioDTO dto, Integer tenantId) {
        Inventario inventario = new Inventario();
        inventario.setStockActual(dto.getStockActual());
        inventario.setStockMinimo(dto.getStockMinimo());
        inventario.setStockMaximo(dto.getStockMaximo());
        
        Inventario saved = inventarioRepository.save(inventario);
        return toDTO(saved);
    }

    @Transactional
    public InventarioDTO updateInventario(Integer id, InventarioDTO dto, Integer tenantId) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        if (!inventario.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        inventario.setStockActual(dto.getStockActual());
        inventario.setStockMinimo(dto.getStockMinimo());
        inventario.setStockMaximo(dto.getStockMaximo());
        
        Inventario updated = inventarioRepository.save(inventario);
        return toDTO(updated);
    }

    @Transactional
    public void deleteInventario(Integer id, Integer tenantId) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        if (!inventario.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        inventarioRepository.deleteById(id);
    }
    
    /**
     * Incrementar stock de un inventario
     */
    @Transactional
    public InventarioDTO incrementarStock(Integer id, Integer cantidad, Integer tenantId) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        if (!inventario.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
        
        inventario.setStockActual(inventario.getStockActual() + cantidad);
        inventario.setFechaUltimoIngreso(java.time.LocalDateTime.now());
        
        Inventario updated = inventarioRepository.save(inventario);
        return toDTO(updated);
    }
    
    /**
     * Decrementar stock de un inventario
     */
    @Transactional
    public InventarioDTO decrementarStock(Integer id, Integer cantidad, Integer tenantId) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        if (!inventario.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
        
        if (inventario.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + inventario.getStockActual());
        }
        
        inventario.setStockActual(inventario.getStockActual() - cantidad);
        inventario.setFechaUltimaSalida(java.time.LocalDateTime.now());
        
        Inventario updated = inventarioRepository.save(inventario);
        return toDTO(updated);
    }

    private InventarioDTO toDTO(Inventario entity) {
        InventarioDTO dto = new InventarioDTO();
        dto.setIdInventario(entity.getIdInventario());
        dto.setIdTenant(entity.getTenant().getIdTenant());
        dto.setIdProducto(entity.getProducto().getIdProducto());
        dto.setNombreProducto(entity.getProducto().getNombre());
        dto.setDescripcionProducto(entity.getProducto().getDescripcion());
        dto.setPrecioUnitario(entity.getProducto().getPrecioUnitario());
        dto.setStockActual(entity.getStockActual());
        dto.setStockMinimo(entity.getStockMinimo());
        dto.setStockMaximo(entity.getStockMaximo());
        return dto;
    }
}
