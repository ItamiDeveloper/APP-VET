package com.vet.spring.app.service.historiaService;

import java.util.List;
import com.vet.spring.app.dto.historiaDto.HistoriaClinicaDTO;

public interface HistoriaService {
    List<HistoriaClinicaDTO> findAll();
    HistoriaClinicaDTO findById(Integer id);
    HistoriaClinicaDTO create(HistoriaClinicaDTO dto);
    HistoriaClinicaDTO update(Integer id, HistoriaClinicaDTO dto);
    void delete(Integer id);
}
