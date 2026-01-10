package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.doctorDto.DoctorDTO;
import com.vet.spring.app.entity.doctor.Doctor;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.exception.ResourceNotFoundException;
import com.vet.spring.app.mapper.doctorMapper.DoctorMapper;
import com.vet.spring.app.repository.doctorRepository.DoctorRepository;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import com.vet.spring.app.repository.usuarioRepository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final TenantRepository tenantRepository;
    private final UsuarioRepository usuarioRepository;
    private final DoctorMapper doctorMapper;

    /**
     * Obtener todos los doctores de un tenant
     */
    public List<DoctorDTO> getAllDoctoresByTenant(Integer tenantId) {
        List<Doctor> doctores = doctorRepository.findByTenantId(tenantId);
        return doctores.stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener un doctor por ID
     */
    public DoctorDTO getDoctorById(Integer id, Integer tenantId) {
        Doctor doctor = doctorRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + id));
        
        return doctorMapper.toDTO(doctor);
    }

    /**
     * Crear un nuevo doctor
     */
    @Transactional
    public DoctorDTO createDoctor(DoctorDTO dto, Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con ID: " + tenantId));

        // Verificar que el número de colegiatura es único en el tenant
        if (dto.getColegiatura() != null) {
            boolean existeColegiatura = doctorRepository.findByTenantId(tenantId)
                .stream()
                .anyMatch(d -> d.getColegiatura() != null
                    && d.getColegiatura().equals(dto.getColegiatura()));
            
            if (existeColegiatura) {
                throw new IllegalArgumentException("Ya existe un doctor con ese número de colegiatura");
            }
        }

        // Si se proporciona un idUsuario, verificar que existe y pertenece al tenant
        Usuario usuario = null;
        if (dto.getIdUsuario() != null) {
            usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
            
            if (!usuario.getTenant().getIdTenant().equals(tenantId)) {
                throw new IllegalArgumentException("El usuario no pertenece a este tenant");
            }
        }

        Doctor doctor = new Doctor();
        doctor.setTenant(tenant);
        doctor.setUsuario(usuario);
        doctor.setNombres(dto.getNombres());
        doctor.setApellidos(dto.getApellidos());
        doctor.setEspecialidad(dto.getEspecialidad());
        doctor.setColegiatura(dto.getColegiatura());
        doctor.setTelefono(dto.getTelefono());
        doctor.setEmail(dto.getEmail());
        doctor.setEstado(Doctor.EstadoDoctor.ACTIVO);

        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDTO(savedDoctor);
    }

    /**
     * Actualizar un doctor existente
     */
    @Transactional
    public DoctorDTO updateDoctor(Integer id, DoctorDTO dto, Integer tenantId) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + id));

        if (!doctor.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Doctor no encontrado en este tenant");
        }

        // Verificar número de colegiatura si cambió
        if (dto.getColegiatura() != null && 
            !dto.getColegiatura().equals(doctor.getColegiatura())) {
            boolean existeColegiatura = doctorRepository.findByTenantId(tenantId)
                .stream()
                .anyMatch(d -> d.getColegiatura() != null
                    && d.getColegiatura().equals(dto.getColegiatura())
                    && !d.getIdDoctor().equals(id));
            
            if (existeColegiatura) {
                throw new IllegalArgumentException("Ya existe otro doctor con ese número de colegiatura");
            }
            doctor.setColegiatura(dto.getColegiatura());
        }

        if (dto.getNombres() != null) doctor.setNombres(dto.getNombres());
        if (dto.getApellidos() != null) doctor.setApellidos(dto.getApellidos());
        if (dto.getEspecialidad() != null) doctor.setEspecialidad(dto.getEspecialidad());
        if (dto.getTelefono() != null) doctor.setTelefono(dto.getTelefono());
        if (dto.getEmail() != null) doctor.setEmail(dto.getEmail());

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDTO(updatedDoctor);
    }

    /**
     * Eliminar un doctor (soft delete)
     */
    @Transactional
    public void deleteDoctor(Integer id, Integer tenantId) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + id));

        if (!doctor.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Doctor no encontrado en este tenant");
        }

        doctor.setEstado(Doctor.EstadoDoctor.INACTIVO);
        doctorRepository.save(doctor);
    }

    /**
     * Obtener doctores activos
     */
    public List<DoctorDTO> getDoctoresActivos(Integer tenantId) {
        List<Doctor> doctores = doctorRepository.findByTenantId(tenantId)
            .stream()
            .filter(d -> d.getEstado() == Doctor.EstadoDoctor.ACTIVO)
            .collect(Collectors.toList());
        
        return doctores.stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Buscar doctores por nombre o especialidad
     */
    public List<DoctorDTO> buscarDoctores(String termino, Integer tenantId) {
        String terminoLower = termino.toLowerCase();
        List<Doctor> doctores = doctorRepository.findByTenantId(tenantId)
            .stream()
            .filter(d -> d.getNombres().toLowerCase().contains(terminoLower)
                    || d.getApellidos().toLowerCase().contains(terminoLower)
                    || (d.getEspecialidad() != null && d.getEspecialidad().toLowerCase().contains(terminoLower)))
            .collect(Collectors.toList());
        
        return doctores.stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }
}
