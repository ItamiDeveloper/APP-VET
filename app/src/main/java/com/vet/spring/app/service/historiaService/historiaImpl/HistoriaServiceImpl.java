package com.vet.spring.app.service.historiaService.historiaImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.historiaDto.HistoriaClinicaDTO;
import com.vet.spring.app.entity.historia.HistoriaClinica;
import com.vet.spring.app.mapper.historiaMapper.HistoriaClinicaMapper;
import com.vet.spring.app.repository.historiaRepository.HistoriaClinicaRepository;
import com.vet.spring.app.service.historiaService.HistoriaService;

@Service
@Transactional(readOnly = true)
public class HistoriaServiceImpl implements HistoriaService {

    private final HistoriaClinicaRepository historiaRepository;

    public HistoriaServiceImpl(HistoriaClinicaRepository historiaRepository) {
        this.historiaRepository = historiaRepository;
    }

    @Override
    public List<HistoriaClinicaDTO> findAll() {
        return historiaRepository.findAll().stream()
                .map(HistoriaClinicaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HistoriaClinicaDTO findById(Integer id) {
        return historiaRepository.findById(id).map(HistoriaClinicaMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public HistoriaClinicaDTO create(HistoriaClinicaDTO dto) {
        HistoriaClinica entity = HistoriaClinicaMapper.toEntity(dto);
        HistoriaClinica saved = historiaRepository.save(entity);
        return HistoriaClinicaMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public HistoriaClinicaDTO update(Integer id, HistoriaClinicaDTO dto) {
        return historiaRepository.findById(id).map(existing -> {
            HistoriaClinica updated = HistoriaClinicaMapper.toEntity(dto);
            updated.setIdHistoria(existing.getIdHistoria());
            HistoriaClinica saved = historiaRepository.save(updated);
            return HistoriaClinicaMapper.toDTO(saved);
        }).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        historiaRepository.deleteById(id);
    }
}
