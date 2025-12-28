package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.mascotaDto.RazaDTO;
import com.vet.spring.app.mapper.mascotaMapper.RazaMapper;
import com.vet.spring.app.repository.mascotaRepository.RazaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RazaService {

    private final RazaRepository razaRepository;
    private final RazaMapper razaMapper;

    /**
     * Obtener todas las razas (catálogo global)
     */
    public List<RazaDTO> getAllRazas() {
        return razaRepository.findAll()
            .stream()
            .map(razaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener razas por especie
     */
    public List<RazaDTO> getRazasByEspecie(Integer idEspecie) {
        return razaRepository.findAll()
            .stream()
            .filter(r -> r.getEspecie().getIdEspecie().equals(idEspecie))
            .map(razaMapper::toDTO)
            .collect(Collectors.toList());
    }
}
