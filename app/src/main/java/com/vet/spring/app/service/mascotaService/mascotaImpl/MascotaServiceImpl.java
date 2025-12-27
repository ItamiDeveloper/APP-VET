package com.vet.spring.app.service.mascotaService.mascotaImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.mascotaDto.MascotaDTO;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.mapper.mascotaMapper.MascotaMapper;
import com.vet.spring.app.repository.mascotaRepository.MascotaRepository;
import com.vet.spring.app.service.mascotaService.MascotaService;

@Service
@Transactional(readOnly = true)
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;

    public MascotaServiceImpl(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    public List<MascotaDTO> findAll() {
        return mascotaRepository.findAll().stream()
                .map(MascotaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MascotaDTO findById(Integer id) {
        if (id == null) return null;
        return mascotaRepository.findById(id).map(MascotaMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public MascotaDTO create(MascotaDTO dto) {
        Mascota entity = MascotaMapper.toEntity(dto);
        Mascota saved = mascotaRepository.save(entity);
        if (saved == null) return null;
        return MascotaMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public MascotaDTO update(Integer id, MascotaDTO dto) {
        if (id == null) return null;
        return mascotaRepository.findById(id).map(existing -> {
            Mascota updated = MascotaMapper.toEntity(dto);
            Mascota saved = mascotaRepository.save(updated);
            if (saved == null) return null;
            return MascotaMapper.toDTO(saved);
        }).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id == null) return;
        mascotaRepository.deleteById(id);
    }
}
