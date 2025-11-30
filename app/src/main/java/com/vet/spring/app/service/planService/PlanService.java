package com.vet.spring.app.service.planService.PlanService;

import java.util.List;
import com.vet.spring.app.dto.plan.PlanDTO;

public interface PlanService {
    List<PlanDTO> findAll();
    PlanDTO findById(Integer id);
    PlanDTO create(PlanDTO dto);
    PlanDTO update(Integer id, PlanDTO dto);
    void delete(Integer id);
}
