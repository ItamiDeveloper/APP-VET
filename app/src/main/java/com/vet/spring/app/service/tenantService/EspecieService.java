package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.mascotaDto.EspecieDTO;
import com.vet.spring.app.entity.mascota.Especie;
import com.vet.spring.app.mapper.mascotaMapper.EspecieMapper;
import com.vet.spring.app.repository.mascotaRepository.EspecieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecieService {

    private final EspecieRepository especieRepository;
    private final EspecieMapper especieMapper;

    /**
     * Obtener todas las especies (catálogo global)
     */
    @Transactional(readOnly = true)
    public List<EspecieDTO> getAllEspecies() {
        return especieRepository.findAll()
            .stream()
            .map(especieMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener especie por ID
     */
    @Transactional(readOnly = true)
    public EspecieDTO getEspecieById(Integer id) {
        Especie especie = especieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
        return especieMapper.toDTO(especie);
    }

    /**
     * Crear nueva especie
     */
    @Transactional
    public EspecieDTO createEspecie(EspecieDTO dto) {
        Especie especie = especieMapper.toEntity(dto);
        Especie saved = especieRepository.save(especie);
        return especieMapper.toDTO(saved);
    }

    /**
     * Actualizar especie existente
     */
    @Transactional
    public EspecieDTO updateEspecie(Integer id, EspecieDTO dto) {
        Especie especie = especieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
        
        especie.setNombre(dto.getNombre());
        especie.setDescripcion(dto.getDescripcion());
        
        Especie updated = especieRepository.save(especie);
        return especieMapper.toDTO(updated);
    }

    /**
     * Eliminar especie
     */
    @Transactional
    public void deleteEspecie(Integer id) {
        if (!especieRepository.existsById(id)) {
            throw new RuntimeException("Especie no encontrada");
        }
        especieRepository.deleteById(id);
    }
}
