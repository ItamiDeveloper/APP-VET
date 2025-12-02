package com.vet.spring.app.service.clienteService.clienteImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.clienteDto.ClienteDTO;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.mapper.clienteMapper.ClienteMapper;
import com.vet.spring.app.repository.clienteRepository.ClienteRepository;
import com.vet.spring.app.service.clienteService.ClienteService;

@Service
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO findById(Integer id) {
        return clienteRepository.findById(id)
                .map(ClienteMapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public ClienteDTO create(ClienteDTO dto) {
        Cliente entity = ClienteMapper.toEntity(dto);
        Cliente saved = clienteRepository.save(entity);
        return ClienteMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public ClienteDTO update(Integer id, ClienteDTO dto) {
        return clienteRepository.findById(id).map(existing -> {
            Cliente updated = ClienteMapper.toEntity(dto);
            updated.setIdCliente(existing.getIdCliente());
            Cliente saved = clienteRepository.save(updated);
            return ClienteMapper.toDTO(saved);
        }).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        clienteRepository.deleteById(id);
    }
}
