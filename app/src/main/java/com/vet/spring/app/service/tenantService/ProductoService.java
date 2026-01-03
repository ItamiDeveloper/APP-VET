package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.inventarioDto.ProductoDTO;
import com.vet.spring.app.entity.inventario.CategoriaProducto;
import com.vet.spring.app.entity.inventario.Producto;
import com.vet.spring.app.repository.inventarioRepository.CategoriaProductoRepository;
import com.vet.spring.app.repository.inventarioRepository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaProductoRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDTO getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toDTO(producto);
    }

    @Transactional
    public ProductoDTO createProducto(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        producto.setEsMedicamento(dto.getEsMedicamento());
        producto.setEstado(Producto.EstadoProducto.valueOf(dto.getEstado()));

        if (dto.getIdCategoria() != null) {
            CategoriaProducto categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        Producto saved = productoRepository.save(producto);
        return toDTO(saved);
    }

    @Transactional
    public ProductoDTO updateProducto(Integer id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        producto.setEsMedicamento(dto.getEsMedicamento());
        producto.setEstado(Producto.EstadoProducto.valueOf(dto.getEstado()));

        if (dto.getIdCategoria() != null) {
            CategoriaProducto categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        Producto updated = productoRepository.save(producto);
        return toDTO(updated);
    }

    @Transactional
    public void deleteProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    private ProductoDTO toDTO(Producto entity) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(entity.getIdProducto());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setEsMedicamento(entity.getEsMedicamento());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : "ACTIVO");
        
        if (entity.getCategoria() != null) {
            dto.setIdCategoria(entity.getCategoria().getIdCategoria());
            dto.setNombreCategoria(entity.getCategoria().getNombre());
        }
        
        return dto;
    }
}
