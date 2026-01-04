package com.vet.spring.app.service;

import com.vet.spring.app.dto.compraDto.ProveedorDTO;
import com.vet.spring.app.entity.common.Proveedor;
import com.vet.spring.app.repository.proveedorRepository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Transactional(readOnly = true)
    public List<ProveedorDTO> getAllProveedores() {
        return proveedorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> getProveedoresActivos() {
        return proveedorRepository.findByEstado("ACTIVO").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProveedorDTO getProveedorById(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        return toDTO(proveedor);
    }

    @Transactional
    public ProveedorDTO createProveedor(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setContacto(dto.getContacto());
        proveedor.setEstado(dto.getEstado() != null ? dto.getEstado() : "ACTIVO");
        
        Proveedor saved = proveedorRepository.save(proveedor);
        return toDTO(saved);
    }

    @Transactional
    public ProveedorDTO updateProveedor(Integer id, ProveedorDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        
        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setContacto(dto.getContacto());
        if (dto.getEstado() != null) {
            proveedor.setEstado(dto.getEstado());
        }
        
        Proveedor updated = proveedorRepository.save(proveedor);
        return toDTO(updated);
    }

    @Transactional
    public void deleteProveedor(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedorRepository.deleteById(id);
    }

    private ProveedorDTO toDTO(Proveedor entity) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setIdProveedor(entity.getIdProveedor());
        dto.setNombre(entity.getNombre());
        dto.setRuc(entity.getRuc());
        dto.setTelefono(entity.getTelefono());
        dto.setEmail(entity.getEmail());
        dto.setDireccion(entity.getDireccion());
        dto.setContacto(entity.getContacto());
        dto.setEstado(entity.getEstado());
        return dto;
    }
}
