package com.vet.spring.app.service.rolService;

import java.util.List;
import com.vet.spring.app.dto.usuarioDto.RolDTO;

public interface RolService {
    List<RolDTO> findAll();
}
