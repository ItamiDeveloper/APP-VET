package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.ventaDto.DetalleVentaDTO;
import com.vet.spring.app.dto.ventaDto.VentaDTO;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.inventario.Inventario;
import com.vet.spring.app.entity.inventario.Producto;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.venta.DetalleVenta;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.repository.clienteRepository.ClienteRepository;
import com.vet.spring.app.repository.inventarioRepository.InventarioRepository;
import com.vet.spring.app.repository.inventarioRepository.ProductoRepository;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import com.vet.spring.app.repository.ventaRepository.VentaRepository;
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
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final TenantRepository tenantRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    @Transactional(readOnly = true)
    public List<VentaDTO> getAllVentasByTenant(Integer tenantId) {
        return ventaRepository.findByTenantId(tenantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaDTO getVentaById(Integer id, Integer tenantId) {
        Venta venta = ventaRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        return toDTO(venta);
    }

    @Transactional
    public VentaDTO createVenta(VentaDTO dto, Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));
        
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Venta venta = new Venta();
        venta.setTenant(tenant);
        venta.setCliente(cliente);
        // La fecha se setea automáticamente con @CreationTimestamp
        venta.setTotal(dto.getTotal());
        venta.setMetodoPago(dto.getMetodoPago());
        venta.setEstado(dto.getEstado() != null ? dto.getEstado() : "COMPLETADA");
        
        // Guardar detalles si existen
        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            List<DetalleVenta> detalles = new ArrayList<>();
            for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
                Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getIdProducto()));
                
                // ✅ VALIDAR STOCK ANTES DE VENDER
                Inventario inventario = inventarioRepository.findByTenantIdAndProductoId(tenantId, producto.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto " + producto.getNombre() + " no está en inventario"));
                
                if (inventario.getStockActual() < detalleDTO.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para " + producto.getNombre() + 
                            ". Disponible: " + inventario.getStockActual() + 
                            ", Solicitado: " + detalleDTO.getCantidad());
                }
                
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));
                
                detalles.add(detalle);
                
                // ✅ ACTUALIZAR INVENTARIO: Restar stock al vender
                inventario.setStockActual(inventario.getStockActual() - detalleDTO.getCantidad());
                inventario.setFechaUltimaSalida(LocalDateTime.now());
                inventarioRepository.save(inventario);
            }
            venta.setDetalles(detalles);
        }
        
        Venta saved = ventaRepository.save(venta);
        return toDTO(saved);
    }

    @Transactional
    public VentaDTO updateVenta(Integer id, VentaDTO dto, Integer tenantId) {
        Venta venta = ventaRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        // Update Cliente if provided
        if (dto.getIdCliente() != null && !dto.getIdCliente().equals(venta.getCliente().getIdCliente())) {
            Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            venta.setCliente(cliente);
        }
        
        // La fecha no se puede actualizar (updatable=false)
        venta.setTotal(dto.getTotal());
        venta.setMetodoPago(dto.getMetodoPago());
        if (dto.getEstado() != null) {
            venta.setEstado(dto.getEstado());
        }
        
        // Actualizar detalles si se proporcionan
        if (dto.getDetalles() != null) {
            venta.getDetalles().clear();
            for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
                Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getIdProducto()));
                
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));
                
                venta.getDetalles().add(detalle);
            }
        }
        
        Venta updated = ventaRepository.save(venta);
        return toDTO(updated);
    }

    @Transactional
    public void deleteVenta(Integer id, Integer tenantId) {
        Venta venta = ventaRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        ventaRepository.deleteById(id);
    }

    private VentaDTO toDTO(Venta entity) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(entity.getIdVenta());
        dto.setIdTenant(entity.getTenant().getIdTenant());
        if (entity.getCliente() != null) {
            dto.setIdCliente(entity.getCliente().getIdCliente());
            dto.setClienteNombre(entity.getCliente().getNombres() + " " + entity.getCliente().getApellidos());
        }
        dto.setFecha(entity.getFecha());
        dto.setTotal(entity.getTotal());
        dto.setMetodoPago(entity.getMetodoPago());
        dto.setEstado(entity.getEstado());
        
        // Convertir detalles
        if (entity.getDetalles() != null && !entity.getDetalles().isEmpty()) {
            List<DetalleVentaDTO> detallesDTO = entity.getDetalles().stream()
                    .map(this::detalleToDTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }
        
        return dto;
    }
    
    private DetalleVentaDTO detalleToDTO(DetalleVenta detalle) {
        DetalleVentaDTO dto = new DetalleVentaDTO();
        dto.setIdDetalleVenta(detalle.getIdDetalleVenta());
        dto.setIdVenta(detalle.getVenta().getIdVenta());
        dto.setIdProducto(detalle.getProducto().getIdProducto());
        dto.setProductoNombre(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
