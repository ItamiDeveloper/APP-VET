package com.vet.spring.app.service.planService;

import java.util.List;
import com.vet.spring.app.dto.planDto.PlanDTO;

public interface PlanService {
    List<PlanDTO> findAll();
    PlanDTO findById(Integer id);
    PlanDTO create(PlanDTO dto);
    PlanDTO update(Integer id, PlanDTO dto);
    void delete(Integer id);
}
