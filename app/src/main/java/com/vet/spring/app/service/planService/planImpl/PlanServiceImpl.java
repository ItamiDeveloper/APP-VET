package com.vet.spring.app.service.planService.planImpl.PlanServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.plan.PlanDTO;
import com.vet.spring.app.entity.plan.Plan;
import com.vet.spring.app.mapper.PlanMapper;
import com.vet.spring.app.repository.PlanRepository;
import com.vet.spring.app.service.plan.PlanService;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<PlanDTO> findAll() {
        return planRepository.findAll().stream().map(PlanMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public PlanDTO findById(Integer id) {
        return planRepository.findById(id).map(PlanMapper::toDTO).orElse(null);
    }

    @Override
    public PlanDTO create(PlanDTO dto) {
        Plan p = PlanMapper.toEntity(dto);
        Plan saved = planRepository.save(p);
        return PlanMapper.toDTO(saved);
    }

    @Override
    public PlanDTO update(Integer id, PlanDTO dto) {
        return planRepository.findById(id).map(existing -> {
            existing.setNombre(dto.getNombre());
            existing.setPrecioMensual(dto.getPrecioMensual());
            existing.setMaxDoctores(dto.getMaxDoctores());
            existing.setMaxMascotas(dto.getMaxMascotas());
            existing.setMaxAlmacenamientoMb(dto.getMaxAlmacenamientoMb());
            if (dto.getEstado() != null) existing.setEstado(java.util.Enum.valueOf(com.vet.spring.app.entity.veterinaria.Estado.class, dto.getEstado()));
            return PlanMapper.toDTO(planRepository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        planRepository.deleteById(id);
    }
}
