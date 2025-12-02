package com.vet.spring.app.service.citaService.citaImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.citaDto.CitaDTO;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.mapper.citaMapper.CitaMapper;
import com.vet.spring.app.mapper.clienteMapper.ClienteMapper;
import com.vet.spring.app.repository.citaRepository.CitaRepository;
import com.vet.spring.app.service.citaService.CitaService;

@Service
@Transactional(readOnly = true)
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;

    public CitaServiceImpl(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Override
    public List<CitaDTO> findAll() {
        return citaRepository.findAll().stream()
                .map(CitaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CitaDTO findById(Integer id) {
        return citaRepository.findById(id).map(CitaMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public CitaDTO create(CitaDTO dto) {
        Cita entity = CitaMapper.toEntity(dto);
        Cita saved = citaRepository.save(entity);
        return CitaMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CitaDTO update(Integer id, CitaDTO dto) {
        return citaRepository.findById(id).map(existing -> {
            Cita updated = CitaMapper.toEntity(dto);
            updated.setIdCita(existing.getIdCita());
            Cita saved = citaRepository.save(updated);
            return CitaMapper.toDTO(saved);
        }).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        citaRepository.deleteById(id);
    }
}
