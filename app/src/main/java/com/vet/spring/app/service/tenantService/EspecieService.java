package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.mascotaDto.EspecieDTO;
import com.vet.spring.app.mapper.mascotaMapper.EspecieMapper;
import com.vet.spring.app.repository.mascotaRepository.EspecieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<EspecieDTO> getAllEspecies() {
        return especieRepository.findAll()
            .stream()
            .map(especieMapper::toDTO)
            .collect(Collectors.toList());
    }
}
