package com.vet.spring.app.service.clienteService;

import java.util.List;
import com.vet.spring.app.dto.clienteDto.ClienteDTO;

public interface ClienteService {
    List<ClienteDTO> findAll();
    ClienteDTO findById(Integer id);
    ClienteDTO create(ClienteDTO dto);
    ClienteDTO update(Integer id, ClienteDTO dto);
    void delete(Integer id);
}
