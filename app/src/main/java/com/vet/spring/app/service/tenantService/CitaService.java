package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.citaDto.CitaDTO;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.doctor.Doctor;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.exception.ResourceNotFoundException;
import com.vet.spring.app.mapper.citaMapper.CitaMapper;
import com.vet.spring.app.repository.citaRepository.CitaRepository;
import com.vet.spring.app.repository.clienteRepository.ClienteRepository;
import com.vet.spring.app.repository.doctorRepository.DoctorRepository;
import com.vet.spring.app.repository.mascotaRepository.MascotaRepository;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final TenantRepository tenantRepository;
    private final MascotaRepository mascotaRepository;
    private final ClienteRepository clienteRepository;
    private final DoctorRepository doctorRepository;
    private final CitaMapper citaMapper;

    /**
     * Obtener todas las citas de un tenant
     */
    public List<CitaDTO> getAllCitasByTenant(Integer tenantId) {
        List<Cita> citas = citaRepository.findAll()
            .stream()
            .filter(c -> c.getTenant().getIdTenant().equals(tenantId))
            .collect(Collectors.toList());
        return citas.stream()
            .map(citaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener una cita por ID
     */
    public CitaDTO getCitaById(Integer id, Integer tenantId) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
        
        if (!cita.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cita no encontrada en este tenant");
        }
        
        return citaMapper.toDTO(cita);
    }

    /**
     * Obtener citas de una mascota
     */
    public List<CitaDTO> getCitasByMascota(Integer idMascota, Integer tenantId) {
        Mascota mascota = mascotaRepository.findById(idMascota)
            .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con ID: " + idMascota));
        
        if (!mascota.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Mascota no encontrada en este tenant");
        }

        List<Cita> citas = citaRepository.findAll()
            .stream()
            .filter(c -> c.getMascota().getIdMascota().equals(idMascota))
            .collect(Collectors.toList());
        
        return citas.stream()
            .map(citaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener citas de un doctor
     */
    public List<CitaDTO> getCitasByDoctor(Integer idDoctor, Integer tenantId) {
        Doctor doctor = doctorRepository.findById(idDoctor)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + idDoctor));
        
        if (!doctor.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Doctor no encontrado en este tenant");
        }

        List<Cita> citas = citaRepository.findAll()
            .stream()
            .filter(c -> c.getDoctor().getIdDoctor().equals(idDoctor))
            .collect(Collectors.toList());
        
        return citas.stream()
            .map(citaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener citas por fecha
     */
    public List<CitaDTO> getCitasByFecha(LocalDateTime inicio, LocalDateTime fin, Integer tenantId) {
        List<Cita> citas = citaRepository.findAll()
            .stream()
            .filter(c -> c.getTenant().getIdTenant().equals(tenantId)
                && c.getFechaHora().isAfter(inicio)
                && c.getFechaHora().isBefore(fin))
            .collect(Collectors.toList());
        
        return citas.stream()
            .map(citaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Crear una nueva cita
     */
    @Transactional
    public CitaDTO createCita(CitaDTO dto, Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con ID: " + tenantId));

        Mascota mascota = mascotaRepository.findById(dto.getIdMascota())
            .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con ID: " + dto.getIdMascota()));

        if (!mascota.getTenant().getIdTenant().equals(tenantId)) {
            throw new IllegalArgumentException("La mascota no pertenece a este tenant");
        }

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + dto.getIdCliente()));

        if (!cliente.getTenant().getIdTenant().equals(tenantId)) {
            throw new IllegalArgumentException("El cliente no pertenece a este tenant");
        }

        Doctor doctor = doctorRepository.findById(dto.getIdDoctor())
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + dto.getIdDoctor()));

        if (!doctor.getTenant().getIdTenant().equals(tenantId)) {
            throw new IllegalArgumentException("El doctor no pertenece a este tenant");
        }

        // Verificar disponibilidad del doctor en esa fecha/hora
        boolean doctorOcupado = citaRepository.findAll()
            .stream()
            .anyMatch(c -> c.getDoctor().getIdDoctor().equals(dto.getIdDoctor())
                && c.getEstado() != Cita.CitaEstado.CANCELADA
                && c.getFechaHora().equals(dto.getFechaHora()));

        if (doctorOcupado) {
            throw new IllegalArgumentException("El doctor ya tiene una cita PENDIENTE en ese horario");
        }

        Cita cita = new Cita();
        cita.setTenant(tenant);
        cita.setMascota(mascota);
        cita.setCliente(cliente);
        cita.setDoctor(doctor);
        cita.setFechaHora(dto.getFechaHora());
        cita.setDuracionMinutos(dto.getDuracionMinutos() != null ? dto.getDuracionMinutos() : 30);
        cita.setMotivo(dto.getMotivo());
        cita.setObservaciones(dto.getObservaciones());
        cita.setEstado(Cita.CitaEstado.PENDIENTE);
        cita.setFechaCreacion(LocalDateTime.now());

        Cita savedCita = citaRepository.save(cita);
        return citaMapper.toDTO(savedCita);
    }

    /**
     * Actualizar una cita existente
     */
    @Transactional
    public CitaDTO updateCita(Integer id, CitaDTO dto, Integer tenantId) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        if (!cita.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cita no encontrada en este tenant");
        }

        // Actualizar doctor si cambió
        if (dto.getIdDoctor() != null && !dto.getIdDoctor().equals(cita.getDoctor().getIdDoctor())) {
            Doctor nuevoDoctor = doctorRepository.findById(dto.getIdDoctor())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + dto.getIdDoctor()));
            
            if (!nuevoDoctor.getTenant().getIdTenant().equals(tenantId)) {
                throw new IllegalArgumentException("El doctor no pertenece a este tenant");
            }
            cita.setDoctor(nuevoDoctor);
        }

        if (dto.getFechaHora() != null) cita.setFechaHora(dto.getFechaHora());
        if (dto.getDuracionMinutos() != null) cita.setDuracionMinutos(dto.getDuracionMinutos());
        if (dto.getMotivo() != null) cita.setMotivo(dto.getMotivo());
        if (dto.getObservaciones() != null) cita.setObservaciones(dto.getObservaciones());

        Cita updatedCita = citaRepository.save(cita);
        return citaMapper.toDTO(updatedCita);
    }

    /**
     * Cambiar estado de una cita
     */
    @Transactional
    public CitaDTO cambiarEstado(Integer id, String nuevoEstado, Integer tenantId) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        if (!cita.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cita no encontrada en este tenant");
        }

        try {
            cita.setEstado(Cita.CitaEstado.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        Cita updatedCita = citaRepository.save(cita);
        return citaMapper.toDTO(updatedCita);
    }

    /**
     * Cancelar una cita
     */
    @Transactional
    public void cancelarCita(Integer id, Integer tenantId) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        if (!cita.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Cita no encontrada en este tenant");
        }

        cita.setEstado(Cita.CitaEstado.CANCELADA);
        citaRepository.save(cita);
    }

    /**
     * Obtener citas programadas (pendientes)
     */
    public List<CitaDTO> getCitasProgramadas(Integer tenantId) {
        List<Cita> citas = citaRepository.findAll()
            .stream()
            .filter(c -> c.getTenant().getIdTenant().equals(tenantId) 
                && c.getEstado() == Cita.CitaEstado.PENDIENTE)
            .collect(Collectors.toList());
        
        return citas.stream()
            .map(citaMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener citas del día
     */
    public List<CitaDTO> getCitasDelDia(LocalDateTime fecha, Integer tenantId) {
        LocalDateTime inicioDia = fecha.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        
        List<Cita> citas = citaRepository.findAll()
            .stream()
            .filter(c -> c.getTenant().getIdTenant().equals(tenantId)
                && c.getFechaHora().isAfter(inicioDia)
                && c.getFechaHora().isBefore(finDia))
            .collect(Collectors.toList());
        
        return citas.stream()
            .map(citaMapper::toDTO)
            .collect(Collectors.toList());
    }
}
