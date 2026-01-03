package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.inventarioDto.CategoriaProductoDTO;
import com.vet.spring.app.entity.inventario.CategoriaProducto;
import com.vet.spring.app.repository.inventarioRepository.CategoriaProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaProductoService {

    private final CategoriaProductoRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaProductoDTO> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaProductoDTO getCategoriaById(Integer id) {
        CategoriaProducto categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return toDTO(categoria);
    }

    @Transactional
    public CategoriaProductoDTO createCategoria(CategoriaProductoDTO dto) {
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(CategoriaProducto.EstadoCategoria.valueOf(dto.getEstado()));

        CategoriaProducto saved = categoriaRepository.save(categoria);
        return toDTO(saved);
    }

    @Transactional
    public CategoriaProductoDTO updateCategoria(Integer id, CategoriaProductoDTO dto) {
        CategoriaProducto categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(CategoriaProducto.EstadoCategoria.valueOf(dto.getEstado()));

        CategoriaProducto updated = categoriaRepository.save(categoria);
        return toDTO(updated);
    }

    @Transactional
    public void deleteCategoria(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaProductoDTO toDTO(CategoriaProducto entity) {
        CategoriaProductoDTO dto = new CategoriaProductoDTO();
        dto.setIdCategoria(entity.getIdCategoria());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : "ACTIVO");
        return dto;
    }
}
