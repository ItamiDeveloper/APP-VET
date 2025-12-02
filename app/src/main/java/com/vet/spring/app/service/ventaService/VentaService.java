package com.vet.spring.app.service.ventaService;

import java.util.List;
import com.vet.spring.app.dto.ventaDto.VentaDTO;

public interface VentaService {
    List<VentaDTO> findAll();
    VentaDTO findById(Integer id);
    VentaDTO create(VentaDTO dto);
    VentaDTO update(Integer id, VentaDTO dto);
    void delete(Integer id);
}
