package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.mascotaDto.RazaDTO;
import com.vet.spring.app.entity.mascota.Especie;
import com.vet.spring.app.entity.mascota.Raza;
import com.vet.spring.app.mapper.mascotaMapper.RazaMapper;
import com.vet.spring.app.repository.mascotaRepository.EspecieRepository;
import com.vet.spring.app.repository.mascotaRepository.RazaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RazaService {

    private final RazaRepository razaRepository;
    private final EspecieRepository especieRepository;
    private final RazaMapper razaMapper;

    /**
     * Obtener todas las razas (catálogo global)
     */
    @Transactional(readOnly = true)
    public List<RazaDTO> getAllRazas() {
        return razaRepository.findAll()
            .stream()
            .map(razaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener raza por ID
     */
    @Transactional(readOnly = true)
    public RazaDTO getRazaById(Integer id) {
        Raza raza = razaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
        return razaMapper.toDTO(raza);
    }

    /**
     * Obtener razas por especie
     */
    @Transactional(readOnly = true)
    public List<RazaDTO> getRazasByEspecie(Integer idEspecie) {
        return razaRepository.findAll()
            .stream()
            .filter(r -> r.getEspecie().getIdEspecie().equals(idEspecie))
            .map(razaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Crear nueva raza
     */
    @Transactional
    public RazaDTO createRaza(RazaDTO dto) {
        Especie especie = especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
        
        Raza raza = new Raza();
        raza.setEspecie(especie);
        raza.setNombre(dto.getNombre());
        raza.setDescripcion(dto.getDescripcion());
        
        Raza saved = razaRepository.save(raza);
        return razaMapper.toDTO(saved);
    }

    /**
     * Actualizar raza existente
     */
    @Transactional
    public RazaDTO updateRaza(Integer id, RazaDTO dto) {
        Raza raza = razaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
        
        if (dto.getIdEspecie() != null && !dto.getIdEspecie().equals(raza.getEspecie().getIdEspecie())) {
            Especie especie = especieRepository.findById(dto.getIdEspecie())
                    .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
            raza.setEspecie(especie);
        }
        
        raza.setNombre(dto.getNombre());
        raza.setDescripcion(dto.getDescripcion());
        
        Raza updated = razaRepository.save(raza);
        return razaMapper.toDTO(updated);
    }

    /**
     * Eliminar raza
     */
    @Transactional
    public void deleteRaza(Integer id) {
        if (!razaRepository.existsById(id)) {
            throw new RuntimeException("Raza no encontrada");
        }
        razaRepository.deleteById(id);
    }
}
