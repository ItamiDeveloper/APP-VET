package com.vet.spring.app.service.citaService;

import java.util.List;
import com.vet.spring.app.dto.citaDto.CitaDTO;

public interface CitaService {
    List<CitaDTO> findAll();
    CitaDTO findById(Integer id);
    CitaDTO create(CitaDTO dto);
    CitaDTO update(Integer id, CitaDTO dto);
    void delete(Integer id);
}
