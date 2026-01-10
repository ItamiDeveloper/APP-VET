package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.compraDto.CompraDTO;
import com.vet.spring.app.dto.compraDto.DetalleCompraDTO;
import com.vet.spring.app.entity.compra.Compra;
import com.vet.spring.app.entity.compra.DetalleCompra;
import com.vet.spring.app.entity.common.Proveedor;
import com.vet.spring.app.entity.inventario.Inventario;
import com.vet.spring.app.entity.inventario.Producto;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.repository.compraRepository.CompraRepository;
import com.vet.spring.app.repository.inventarioRepository.InventarioRepository;
import com.vet.spring.app.repository.proveedorRepository.ProveedorRepository;
import com.vet.spring.app.repository.inventarioRepository.ProductoRepository;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final TenantRepository tenantRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    @Transactional(readOnly = true)
    public List<CompraDTO> getAllComprasByTenant(Integer tenantId) {
        return compraRepository.findByTenantId(tenantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompraDTO getCompraById(Integer id, Integer tenantId) {
        Compra compra = compraRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        return toDTO(compra);
    }

    @Transactional
    public CompraDTO createCompra(CompraDTO dto, Integer tenantId) {
        // Buscar Tenant
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        
        // Buscar Proveedor
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        
        Compra compra = new Compra();
        compra.setTenant(tenant);
        compra.setProveedor(proveedor);
        // La fecha se setea automáticamente con @CreationTimestamp
        compra.setTotal(dto.getTotal());
        compra.setEstado(dto.getEstado());
        
        // Crear y asociar detalles
        List<DetalleCompra> detalles = new ArrayList<>();
        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            for (DetalleCompraDTO detalleDTO : dto.getDetalles()) {
                Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getIdProducto()));
                
                DetalleCompra detalle = new DetalleCompra();
                detalle.setCompra(compra);
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getSubtotal());
                detalles.add(detalle);
                
                // ✅ ACTUALIZAR INVENTARIO: Sumar stock al comprar
                Inventario inventario = inventarioRepository.findByTenantIdAndProductoId(tenantId, producto.getIdProducto())
                        .orElse(null);
                
                if (inventario != null) {
                    // Si existe inventario, sumar la cantidad
                    inventario.setStockActual(inventario.getStockActual() + detalleDTO.getCantidad());
                    inventario.setFechaUltimoIngreso(LocalDateTime.now());
                    inventarioRepository.save(inventario);
                }
                // Si no existe inventario, no lo creamos automáticamente (debe hacerse desde módulo inventario)
            }
            compra.setDetalles(detalles);
        }
        
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
        
        // Actualizar Proveedor si cambió
        if (dto.getIdProveedor() != null && !dto.getIdProveedor().equals(compra.getProveedor().getIdProveedor())) {
            Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            compra.setProveedor(proveedor);
        }
        
        // La fecha no se puede actualizar (updatable=false)
        compra.setTotal(dto.getTotal());
        compra.setEstado(dto.getEstado());
        
        // Actualizar detalles: limpiar los existentes y agregar los nuevos
        compra.getDetalles().clear();
        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            for (DetalleCompraDTO detalleDTO : dto.getDetalles()) {
                Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getIdProducto()));
                
                DetalleCompra detalle = new DetalleCompra();
                detalle.setCompra(compra);
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getSubtotal());
                compra.getDetalles().add(detalle);
            }
        }
        
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
        
        // Populate proveedor information
        if (entity.getProveedor() != null) {
            dto.setIdProveedor(entity.getProveedor().getIdProveedor());
            dto.setProveedorNombre(entity.getProveedor().getNombre());
        }
        
        dto.setFecha(entity.getFecha());
        dto.setTotal(entity.getTotal());
        dto.setEstado(entity.getEstado());
        
        // Convert detalles to DTOs
        if (entity.getDetalles() != null) {
            List<DetalleCompraDTO> detallesDTO = entity.getDetalles().stream()
                    .map(this::detalleToDTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }
        
        return dto;
    }
    
    private DetalleCompraDTO detalleToDTO(DetalleCompra detalle) {
        DetalleCompraDTO dto = new DetalleCompraDTO();
        dto.setIdDetalleCompra(detalle.getIdDetalleCompra());
        dto.setIdCompra(detalle.getCompra().getIdCompra());
        dto.setIdProducto(detalle.getProducto().getIdProducto());
        dto.setProductoNombre(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
