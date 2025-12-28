package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.inventarioDto.InventarioDTO;
import com.vet.spring.app.dto.inventarioDto.ProductoDTO;
import com.vet.spring.app.entity.inventario.Inventario;
import com.vet.spring.app.entity.inventario.Producto;
import com.vet.spring.app.repository.inventarioRepository.InventarioRepository;
import com.vet.spring.app.repository.inventarioRepository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

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

    @Transactional(readOnly = true)
    public List<ProductoDTO> getAllProductosByTenant(Integer tenantId) {
        // Productos son globales, no tienen tenant
        return productoRepository.findAll().stream()
                .map(this::toProductoDTO)
                .collect(Collectors.toList());
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

    private InventarioDTO toDTO(Inventario entity) {
        InventarioDTO dto = new InventarioDTO();
        dto.setIdInventario(entity.getIdInventario());
        dto.setIdTenant(entity.getTenant().getIdTenant());
        dto.setIdProducto(entity.getProducto().getIdProducto());
        dto.setStockActual(entity.getStockActual());
        dto.setStockMinimo(entity.getStockMinimo());
        dto.setStockMaximo(entity.getStockMaximo());
        return dto;
    }

    private ProductoDTO toProductoDTO(Producto entity) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(entity.getIdProducto());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : "ACTIVO");
        return dto;
    }
}
