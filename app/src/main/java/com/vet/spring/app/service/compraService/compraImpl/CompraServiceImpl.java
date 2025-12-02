package com.vet.spring.app.service.compraService.compraImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.compraDto.CompraDTO;
import com.vet.spring.app.entity.compra.Compra;
import com.vet.spring.app.mapper.compraMapper.CompraMapper;
import com.vet.spring.app.repository.compraRepository.CompraRepository;
import com.vet.spring.app.service.compraService.CompraService;

@Service
@Transactional(readOnly = true)
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;

    public CompraServiceImpl(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Override
    public List<CompraDTO> findAll() {
        return compraRepository.findAll().stream()
                .map(CompraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompraDTO findById(Integer id) {
        return compraRepository.findById(id).map(CompraMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public CompraDTO create(CompraDTO dto) {
        Compra entity = CompraMapper.toEntity(dto);
        Compra saved = compraRepository.save(entity);
        return CompraMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CompraDTO update(Integer id, CompraDTO dto) {
        return compraRepository.findById(id).map(existing -> {
            Compra updated = CompraMapper.toEntity(dto);
            updated.setIdCompra(existing.getIdCompra());
            Compra saved = compraRepository.save(updated);
            return CompraMapper.toDTO(saved);
        }).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        compraRepository.deleteById(id);
    }
}
