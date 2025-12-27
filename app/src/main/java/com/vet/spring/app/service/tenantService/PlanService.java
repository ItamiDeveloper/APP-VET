package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.tenantDto.PlanDTO;
import com.vet.spring.app.entity.tenant.Plan;
import com.vet.spring.app.repository.tenantRepository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    /**
     * Obtiene todos los planes activos ordenados por visualización
     */
    public List<PlanDTO> getPlanesActivos() {
        return planRepository.findByEstadoOrderByOrdenVisualizacionAsc(Plan.EstadoPlan.ACTIVO)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los planes (incluyendo inactivos)
     */
    public List<PlanDTO> getAllPlanes() {
        return planRepository.findAllByOrderByOrdenVisualizacionAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un plan por ID
     */
    public PlanDTO getPlanById(Integer id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + id));
        return toDTO(plan);
    }

    /**
     * Crea un nuevo plan
     */
    @Transactional
    public PlanDTO crearPlan(PlanDTO dto) {
        Plan plan = toEntity(dto);
        plan.setEstado(Plan.EstadoPlan.ACTIVO);
        Plan saved = planRepository.save(plan);
        return toDTO(saved);
    }

    /**
     * Actualiza un plan existente
     */
    @Transactional
    public PlanDTO actualizarPlan(Integer id, PlanDTO dto) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + id));
        
        plan.setNombre(dto.getNombre());
        plan.setDescripcion(dto.getDescripcion());
        plan.setPrecioMensual(dto.getPrecioMensual());
        plan.setPrecioAnual(dto.getPrecioAnual());
        plan.setMaxUsuarios(dto.getMaxUsuarios());
        plan.setMaxDoctores(dto.getMaxDoctores());
        plan.setMaxMascotas(dto.getMaxMascotas());
        plan.setMaxAlmacenamientoMb(dto.getMaxAlmacenamientoMb());
        plan.setTieneReportesAvanzados(dto.getTieneReportesAvanzados());
        plan.setTieneApiAcceso(dto.getTieneApiAcceso());
        plan.setTieneSoportePrioritario(dto.getTieneSoportePrioritario());
        plan.setOrdenVisualizacion(dto.getOrdenVisualizacion());
        
        Plan updated = planRepository.save(plan);
        return toDTO(updated);
    }

    /**
     * Cambia el estado de un plan
     */
    @Transactional
    public void cambiarEstadoPlan(Integer id, String nuevoEstado) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + id));
        plan.setEstado(Plan.EstadoPlan.valueOf(nuevoEstado));
        planRepository.save(plan);
    }

    // Métodos de conversión
    private PlanDTO toDTO(Plan plan) {
        PlanDTO dto = new PlanDTO();
        dto.setIdPlan(plan.getIdPlan());
        dto.setNombre(plan.getNombre());
        dto.setDescripcion(plan.getDescripcion());
        dto.setPrecioMensual(plan.getPrecioMensual());
        dto.setPrecioAnual(plan.getPrecioAnual());
        dto.setMaxUsuarios(plan.getMaxUsuarios());
        dto.setMaxDoctores(plan.getMaxDoctores());
        dto.setMaxMascotas(plan.getMaxMascotas());
        dto.setMaxAlmacenamientoMb(plan.getMaxAlmacenamientoMb());
        dto.setTieneReportesAvanzados(plan.getTieneReportesAvanzados());
        dto.setTieneApiAcceso(plan.getTieneApiAcceso());
        dto.setTieneSoportePrioritario(plan.getTieneSoportePrioritario());
        dto.setOrdenVisualizacion(plan.getOrdenVisualizacion());
        dto.setEstado(plan.getEstado().name());
        return dto;
    }

    private Plan toEntity(PlanDTO dto) {
        Plan plan = new Plan();
        plan.setNombre(dto.getNombre());
        plan.setDescripcion(dto.getDescripcion());
        plan.setPrecioMensual(dto.getPrecioMensual());
        plan.setPrecioAnual(dto.getPrecioAnual());
        plan.setMaxUsuarios(dto.getMaxUsuarios());
        plan.setMaxDoctores(dto.getMaxDoctores());
        plan.setMaxMascotas(dto.getMaxMascotas());
        plan.setMaxAlmacenamientoMb(dto.getMaxAlmacenamientoMb());
        plan.setTieneReportesAvanzados(dto.getTieneReportesAvanzados());
        plan.setTieneApiAcceso(dto.getTieneApiAcceso());
        plan.setTieneSoportePrioritario(dto.getTieneSoportePrioritario());
        plan.setOrdenVisualizacion(dto.getOrdenVisualizacion());
        return plan;
    }
}
