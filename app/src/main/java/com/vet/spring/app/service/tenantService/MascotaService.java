package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.mascotaDto.MascotaDTO;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.mascota.Raza;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.exception.ResourceNotFoundException;
import com.vet.spring.app.mapper.mascotaMapper.MascotaMapper;
import com.vet.spring.app.repository.clienteRepository.ClienteRepository;
import com.vet.spring.app.repository.mascotaRepository.MascotaRepository;
import com.vet.spring.app.repository.mascotaRepository.RazaRepository;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final TenantRepository tenantRepository;
    private final ClienteRepository clienteRepository;
    private final RazaRepository razaRepository;
    private final MascotaMapper mascotaMapper;

    /**
     * Obtener todas las mascotas de un tenant
     */
    public List<MascotaDTO> getAllMascotasByTenant(Integer tenantId) {
        List<Mascota> mascotas = mascotaRepository.findAll()
            .stream()
            .filter(m -> m.getTenant().getIdTenant().equals(tenantId))
            .collect(Collectors.toList());
        return mascotas.stream()
            .map(MascotaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener una mascota por ID
     */
    public MascotaDTO getMascotaById(Integer id, Integer tenantId) {
        Mascota mascota = mascotaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con ID: " + id));
        
        if (!mascota.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Mascota no encontrada en este tenant");
        }
        
        return MascotaMapper.toDTO(mascota);
    }

    /**
     * Obtener mascotas de un cliente
     */
    public List<MascotaDTO> getMascotasByCliente(Integer idCliente, Integer tenantId) {
        // Verificar que el cliente existe y pertenece al tenant
        Cliente cliente = clienteRepository.findById(idCliente)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + idCliente));
        
        if (!cliente.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cliente no encontrado en este tenant");
        }

        List<Mascota> mascotas = mascotaRepository.findAll()
            .stream()
            .filter(m -> m.getCliente().getIdCliente().equals(idCliente))
            .collect(Collectors.toList());
        
        return mascotas.stream()
            .map(MascotaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Crear una nueva mascota
     */
    @Transactional
    public MascotaDTO createMascota(MascotaDTO dto, Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con ID: " + tenantId));

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + dto.getIdCliente()));

        if (!cliente.getTenant().getIdTenant().equals(tenantId)) {
            throw new IllegalArgumentException("El cliente no pertenece a este tenant");
        }

        Raza raza = razaRepository.findById(dto.getIdRaza())
            .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada con ID: " + dto.getIdRaza()));

        Mascota mascota = new Mascota();
        mascota.setTenant(tenant);
        mascota.setCliente(cliente);
        mascota.setRaza(raza);
        mascota.setNombre(dto.getNombre());
        mascota.setSexo(dto.getSexo() != null ? dto.getSexo() : "MACHO");
        mascota.setFechaNacimiento(dto.getFechaNacimiento());
        mascota.setColor(dto.getColor());
        mascota.setPesoKg(dto.getPesoKg());
        mascota.setMicrochip(dto.getMicrochip());
        mascota.setObservaciones(dto.getObservaciones());
        mascota.setEstado(Mascota.EstadoMascota.ACTIVO);
        mascota.setFechaRegistro(LocalDateTime.now());

        Mascota savedMascota = mascotaRepository.save(mascota);
        return MascotaMapper.toDTO(savedMascota);
    }

    /**
     * Actualizar una mascota existente
     */
    @Transactional
    public MascotaDTO updateMascota(Integer id, MascotaDTO dto, Integer tenantId) {
        Mascota mascota = mascotaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con ID: " + id));

        if (!mascota.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Mascota no encontrada en este tenant");
        }

        // Actualizar raza si cambió
        if (dto.getIdRaza() != null && !dto.getIdRaza().equals(mascota.getRaza().getIdRaza())) {
            Raza nuevaRaza = razaRepository.findById(dto.getIdRaza())
                .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada con ID: " + dto.getIdRaza()));
            mascota.setRaza(nuevaRaza);
        }

        if (dto.getNombre() != null) mascota.setNombre(dto.getNombre());
        if (dto.getSexo() != null) mascota.setSexo(dto.getSexo());
        if (dto.getFechaNacimiento() != null) mascota.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getColor() != null) mascota.setColor(dto.getColor());
        if (dto.getPesoKg() != null) mascota.setPesoKg(dto.getPesoKg());
        if (dto.getMicrochip() != null) mascota.setMicrochip(dto.getMicrochip());
        if (dto.getObservaciones() != null) mascota.setObservaciones(dto.getObservaciones());

        Mascota updatedMascota = mascotaRepository.save(mascota);
        return MascotaMapper.toDTO(updatedMascota);
    }

    /**
     * Eliminar una mascota (soft delete)
     */
    @Transactional
    public void deleteMascota(Integer id, Integer tenantId) {
        Mascota mascota = mascotaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con ID: " + id));

        if (!mascota.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Mascota no encontrada en este tenant");
        }

        mascota.setEstado(Mascota.EstadoMascota.FALLECIDO);
        mascotaRepository.save(mascota);
    }

    /**
     * Obtener mascotas activas
     */
    public List<MascotaDTO> getMascotasActivas(Integer tenantId) {
        List<Mascota> mascotas = mascotaRepository.findAll()
            .stream()
            .filter(m -> m.getTenant().getIdTenant().equals(tenantId) 
                && m.getEstado() == Mascota.EstadoMascota.ACTIVO)
            .collect(Collectors.toList());
        
        return mascotas.stream()
            .map(MascotaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Buscar mascotas por nombre
     */
    public List<MascotaDTO> buscarMascotas(String termino, Integer tenantId) {
        String terminoLower = termino.toLowerCase();
        List<Mascota> mascotas = mascotaRepository.findAll()
            .stream()
            .filter(m -> m.getTenant().getIdTenant().equals(tenantId) 
                && m.getNombre().toLowerCase().contains(terminoLower))
            .collect(Collectors.toList());
        
        return mascotas.stream()
            .map(MascotaMapper::toDTO)
            .collect(Collectors.toList());
    }
}
