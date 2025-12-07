package com.vet.spring.app.service.razaService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.razaDto.RazaDTO;
import com.vet.spring.app.entity.mascota.Raza;
import com.vet.spring.app.repository.mascotaRepository.RazaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazaService {

    private final RazaRepository razaRepository;

    @Transactional(readOnly = true)
    public List<RazaDTO> getAllRazas() {
        return razaRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RazaDTO getRazaById(Integer id) {
        Raza raza = razaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
        return toDTO(raza);
    }

    @Transactional(readOnly = true)
    public List<RazaDTO> getRazasByEspecie(Integer idEspecie) {
        return razaRepository.findAll().stream()
            .filter(r -> r.getEspecie().getIdEspecie().equals(idEspecie))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    private RazaDTO toDTO(Raza raza) {
        RazaDTO dto = new RazaDTO();
        dto.setIdRaza(raza.getIdRaza());
        dto.setIdEspecie(raza.getEspecie().getIdEspecie());
        dto.setNombre(raza.getNombre());
        dto.setDescripcion(raza.getDescripcion());
        dto.setEspecieNombre(raza.getEspecie().getNombre());
        return dto;
    }
}
