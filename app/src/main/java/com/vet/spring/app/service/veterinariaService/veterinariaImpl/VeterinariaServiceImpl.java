package com.vet.spring.app.service.veterinaria.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.veterinaria.VeterinariaDTO;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.mapper.VeterinariaMapper;
import com.vet.spring.app.repository.VeterinariaRepository;
import com.vet.spring.app.service.veterinaria.VeterinariaService;

@Service
@Transactional
public class VeterinariaServiceImpl implements VeterinariaService {

    private final VeterinariaRepository veterinariaRepository;

    public VeterinariaServiceImpl(VeterinariaRepository veterinariaRepository) {
        this.veterinariaRepository = veterinariaRepository;
    }

    @Override
    public List<VeterinariaDTO> findAll() {
        return veterinariaRepository.findAll().stream().map(VeterinariaMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public VeterinariaDTO findById(Integer id) {
        return veterinariaRepository.findById(id).map(VeterinariaMapper::toDTO).orElse(null);
    }

    @Override
    public VeterinariaDTO create(VeterinariaDTO dto) {
        Veterinaria e = VeterinariaMapper.toEntity(dto);
        Veterinaria saved = veterinariaRepository.save(e);
        return VeterinariaMapper.toDTO(saved);
    }

    @Override
    public VeterinariaDTO update(Integer id, VeterinariaDTO dto) {
        return veterinariaRepository.findById(id).map(existing -> {
            existing.setNombre(dto.getNombre());
            existing.setRuc(dto.getRuc());
            existing.setTelefono(dto.getTelefono());
            existing.setDireccion(dto.getDireccion());
            return VeterinariaMapper.toDTO(veterinariaRepository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        veterinariaRepository.deleteById(id);
    }
}
