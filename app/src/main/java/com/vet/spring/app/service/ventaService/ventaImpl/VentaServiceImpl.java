package com.vet.spring.app.service.ventaService.ventaImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.dto.ventaDto.VentaDTO;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.mapper.ventaMapper.VentaMapper;
import com.vet.spring.app.repository.ventaRepository.VentaRepository;
import com.vet.spring.app.service.ventaService.VentaService;

@Service
@Transactional(readOnly = true)
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<VentaDTO> findAll() {
        return ventaRepository.findAll().stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VentaDTO findById(Integer id) {
        return ventaRepository.findById(id).map(VentaMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public VentaDTO create(VentaDTO dto) {
        Venta entity = VentaMapper.toEntity(dto);
        Venta saved = ventaRepository.save(entity);
        return VentaMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public VentaDTO update(Integer id, VentaDTO dto) {
        return ventaRepository.findById(id).map(existing -> {
            Venta updated = VentaMapper.toEntity(dto);
            updated.setIdVenta(existing.getIdVenta());
            Venta saved = ventaRepository.save(updated);
            return VentaMapper.toDTO(saved);
        }).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ventaRepository.deleteById(id);
    }
}
