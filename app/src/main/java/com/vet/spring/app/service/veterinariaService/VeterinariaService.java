package com.vet.spring.app.service.veterinaria;

import java.util.List;
import com.vet.spring.app.dto.veterinaria.VeterinariaDTO;

public interface VeterinariaService {
    List<VeterinariaDTO> findAll();
    VeterinariaDTO findById(Integer id);
    VeterinariaDTO create(VeterinariaDTO dto);
    VeterinariaDTO update(Integer id, VeterinariaDTO dto);
    void delete(Integer id);
}
