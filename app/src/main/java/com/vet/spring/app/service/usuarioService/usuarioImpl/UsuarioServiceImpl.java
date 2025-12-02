package com.vet.spring.app.service.usuarioService.usuarioImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.usuarioDto.UsuarioDTO;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.mapper.usuarioMapper.UsuarioMapper;
import com.vet.spring.app.repository.usuarioRepository.UsuarioRepository;
import com.vet.spring.app.service.usuarioService.UsuarioService;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream().map(UsuarioMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO findById(Integer id) {
        return usuarioRepository.findById(id).map(UsuarioMapper::toDTO).orElse(null);
    }

    @Override
    public UsuarioDTO create(UsuarioDTO dto) {
        Usuario e = UsuarioMapper.toEntity(dto);
        Usuario saved = usuarioRepository.save(e);
        return UsuarioMapper.toDTO(saved);
    }

    @Override
    public UsuarioDTO update(Integer id, UsuarioDTO dto) {
        return usuarioRepository.findById(id).map(existing -> {
            existing.setUsername(dto.getUsername());
            existing.setEmail(dto.getEmail());
            return UsuarioMapper.toDTO(usuarioRepository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
