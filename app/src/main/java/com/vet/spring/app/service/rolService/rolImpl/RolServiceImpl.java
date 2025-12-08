package com.vet.spring.app.service.rolService.rolImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vet.spring.app.dto.usuarioDto.RolDTO;
import com.vet.spring.app.repository.usuarioRepository.RolRepository;
import com.vet.spring.app.service.rolService.RolService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public List<RolDTO> findAll() {
        return rolRepository.findAll().stream()
            .map(rol -> {
                RolDTO dto = new RolDTO();
                dto.setIdRol(rol.getIdRol());
                dto.setNombre(rol.getNombre());
                return dto;
            })
            .collect(Collectors.toList());
    }
}
