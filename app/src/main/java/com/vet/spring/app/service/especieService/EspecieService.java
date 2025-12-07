package com.vet.spring.app.service.especieService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.especieDto.EspecieDTO;
import com.vet.spring.app.entity.mascota.Especie;
import com.vet.spring.app.repository.mascotaRepository.EspecieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EspecieService {

    private final EspecieRepository especieRepository;

    @Transactional(readOnly = true)
    public List<EspecieDTO> getAllEspecies() {
        return especieRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EspecieDTO getEspecieById(Integer id) {
        Especie especie = especieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
        return toDTO(especie);
    }

    private EspecieDTO toDTO(Especie especie) {
        EspecieDTO dto = new EspecieDTO();
        dto.setIdEspecie(especie.getIdEspecie());
        dto.setNombre(especie.getNombre());
        dto.setDescripcion(especie.getDescripcion());
        return dto;
    }
}
