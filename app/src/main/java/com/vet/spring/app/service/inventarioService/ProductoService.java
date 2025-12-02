package com.vet.spring.app.service.inventarioService;

import java.util.List;
import com.vet.spring.app.dto.inventarioDto.ProductoDTO;

public interface ProductoService {
    List<ProductoDTO> findAll();
    ProductoDTO findById(Integer id);
    ProductoDTO create(ProductoDTO dto);
    ProductoDTO update(Integer id, ProductoDTO dto);
    void delete(Integer id);
}
