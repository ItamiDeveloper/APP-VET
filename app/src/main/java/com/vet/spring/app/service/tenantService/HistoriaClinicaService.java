package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.historiaDto.HistoriaClinicaDTO;
import com.vet.spring.app.entity.historia.HistoriaClinica;
import com.vet.spring.app.repository.historiaRepository.HistoriaClinicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;

    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> getAllHistoriasByTenant(Integer tenantId) {
        return historiaClinicaRepository.findAll().stream()
                .filter(h -> h.getTenant().getIdTenant().equals(tenantId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaDTO getHistoriaById(Integer id, Integer tenantId) {
        HistoriaClinica historia = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));
        
        if (!historia.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        return toDTO(historia);
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> getHistoriasByMascota(Integer idMascota, Integer tenantId) {
        return historiaClinicaRepository.findAll().stream()
                .filter(h -> h.getTenant().getIdTenant().equals(tenantId))
                .filter(h -> h.getMascota().getIdMascota().equals(idMascota))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public HistoriaClinicaDTO createHistoria(HistoriaClinicaDTO dto, Integer tenantId) {
        HistoriaClinica historia = new HistoriaClinica();
        historia.setFechaAtencion(dto.getFechaAtencion());
        historia.setDiagnostico(dto.getDiagnostico());
        historia.setTratamiento(dto.getTratamiento());
        historia.setObservaciones(dto.getObservaciones());
        
        HistoriaClinica saved = historiaClinicaRepository.save(historia);
        return toDTO(saved);
    }

    @Transactional
    public HistoriaClinicaDTO updateHistoria(Integer id, HistoriaClinicaDTO dto, Integer tenantId) {
        HistoriaClinica historia = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));
        
        if (!historia.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        historia.setDiagnostico(dto.getDiagnostico());
        historia.setTratamiento(dto.getTratamiento());
        historia.setObservaciones(dto.getObservaciones());
        
        HistoriaClinica updated = historiaClinicaRepository.save(historia);
        return toDTO(updated);
    }

    @Transactional
    public void deleteHistoria(Integer id, Integer tenantId) {
        HistoriaClinica historia = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));
        
        if (!historia.getTenant().getIdTenant().equals(tenantId)) {
            throw new RuntimeException("Acceso denegado");
        }
        
        historiaClinicaRepository.deleteById(id);
    }

    private HistoriaClinicaDTO toDTO(HistoriaClinica entity) {
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        dto.setIdHistoria(entity.getIdHistoria());
        dto.setIdTenant(entity.getTenant().getIdTenant());
        dto.setIdMascota(entity.getMascota().getIdMascota());
        dto.setIdDoctor(entity.getDoctor().getIdDoctor());
        dto.setFechaAtencion(entity.getFechaAtencion());
        dto.setDiagnostico(entity.getDiagnostico());
        dto.setTratamiento(entity.getTratamiento());
        dto.setObservaciones(entity.getObservaciones());
        return dto;
    }
}
