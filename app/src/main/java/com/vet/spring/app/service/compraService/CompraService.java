package com.vet.spring.app.service.compraService;

import java.util.List;
import com.vet.spring.app.dto.compraDto.CompraDTO;

public interface CompraService {
    List<CompraDTO> findAll();
    CompraDTO findById(Integer id);
    CompraDTO create(CompraDTO dto);
    CompraDTO update(Integer id, CompraDTO dto);
    void delete(Integer id);
}
