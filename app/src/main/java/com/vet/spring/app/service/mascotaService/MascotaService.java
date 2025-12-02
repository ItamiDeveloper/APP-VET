package com.vet.spring.app.service.mascotaService;

import java.util.List;
import com.vet.spring.app.dto.mascotaDto.MascotaDTO;

public interface MascotaService {
    List<MascotaDTO> findAll();
    MascotaDTO findById(Integer id);
    MascotaDTO create(MascotaDTO dto);
    MascotaDTO update(Integer id, MascotaDTO dto);
    void delete(Integer id);
}
