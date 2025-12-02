package com.vet.spring.app.service.usuarioService;

import java.util.List;
import com.vet.spring.app.dto.usuarioDto.UsuarioDTO;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(Integer id);
    UsuarioDTO create(UsuarioDTO dto);
    UsuarioDTO update(Integer id, UsuarioDTO dto);
    void delete(Integer id);
}
