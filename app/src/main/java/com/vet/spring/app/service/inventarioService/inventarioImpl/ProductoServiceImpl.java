package com.vet.spring.app.service.inventarioService.inventarioImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.inventarioDto.ProductoDTO;
import com.vet.spring.app.entity.inventario.Producto;
import com.vet.spring.app.mapper.inventarioMapper.ProductoMapper;
import com.vet.spring.app.repository.inventarioRepository.ProductoRepository;
import com.vet.spring.app.service.inventarioService.ProductoService;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream().map(ProductoMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductoDTO findById(Integer id) {
        return productoRepository.findById(id).map(ProductoMapper::toDTO).orElse(null);
    }

    @Override
    public ProductoDTO create(ProductoDTO dto) {
        Producto e = ProductoMapper.toEntity(dto);
        Producto saved = productoRepository.save(e);
        return ProductoMapper.toDTO(saved);
    }

    @Override
    public ProductoDTO update(Integer id, ProductoDTO dto) {
        return productoRepository.findById(id).map(existing -> {
            existing.setNombre(dto.getNombre());
            existing.setDescripcion(dto.getDescripcion());
            existing.setEsMedicamento(dto.getEsMedicamento());
            existing.setPrecioUnitario(dto.getPrecioUnitario());
            return ProductoMapper.toDTO(productoRepository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }
}
