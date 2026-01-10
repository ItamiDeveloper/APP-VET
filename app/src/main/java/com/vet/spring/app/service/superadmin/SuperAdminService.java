package com.vet.spring.app.service.superadmin;

import com.vet.spring.app.dto.superadminDto.*;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.usuario.Usuario;
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
public class SuperAdminService {

    private final TenantRepository tenantRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public SuperAdminStatsDTO getStats() {
        Long totalVeterinarias = tenantRepository.count();
        Long totalUsuarios = usuarioRepository.count();
        
        // Calcular ingresos del mes actual
        Double ingresosmes = tenantRepository.findAll().stream()
                .filter(t -> Tenant.EstadoTenant.ACTIVO.equals(t.getEstado()))
                .filter(t -> t.getPlanActual() != null)
                .mapToDouble(t -> t.getPlanActual().getPrecioMensual() != null ? t.getPlanActual().getPrecioMensual().doubleValue() : 0.0)
                .sum();
        
        Long suscripcionesActivas = tenantRepository.findAll().stream()
                .filter(t -> Tenant.EstadoTenant.ACTIVO.equals(t.getEstado()))
                .count();
        
        // Calcular tendencia mensual (simplificado - comparar con mes anterior)
        Double tendenciaMensual = 5.0; // Placeholder - implementar lógica real
        
        return new SuperAdminStatsDTO(
                totalVeterinarias,
                totalUsuarios,
                ingresosmes,
                suscripcionesActivas,
                tendenciaMensual
        );
    }

    @Transactional(readOnly = true)
    public List<VeterinariaAdminDTO> getAllVeterinarias() {
        return tenantRepository.findAll().stream()
                .map(this::toVeterinariaAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VeterinariaAdminDTO> getVeterinariasRecientes() {
        return tenantRepository.findAll().stream()
                .sorted((t1, t2) -> {
                    LocalDateTime f1 = t1.getFechaRegistro() != null ? t1.getFechaRegistro() : LocalDateTime.MIN;
                    LocalDateTime f2 = t2.getFechaRegistro() != null ? t2.getFechaRegistro() : LocalDateTime.MIN;
                    return f2.compareTo(f1);
                })
                .limit(10)
                .map(this::toVeterinariaAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VeterinariaAdminDTO getVeterinariaById(Integer id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
        return toVeterinariaAdminDTO(tenant);
    }

    @Transactional
    public VeterinariaAdminDTO createVeterinaria(VeterinariaAdminDTO dto) {
        Tenant tenant = new Tenant();
        tenant.setCodigoTenant("VET-" + System.currentTimeMillis());
        tenant.setNombreComercial(dto.getNombre());
        tenant.setEmailContacto(dto.getEmail());
        tenant.setTelefono(dto.getTelefono());
        tenant.setDireccion(dto.getDireccion());
        tenant.setFechaRegistro(LocalDateTime.now());
        tenant.setEstado("ACTIVO".equals(dto.getEstado()) ? Tenant.EstadoTenant.ACTIVO : Tenant.EstadoTenant.INACTIVO);
        
        // Nota: Se necesitaría asignar un plan real aquí
        // Por ahora, dejar que el sistema asigne un plan por defecto
        
        Tenant saved = tenantRepository.save(tenant);
        return toVeterinariaAdminDTO(saved);
    }

    @Transactional
    public VeterinariaAdminDTO updateVeterinaria(Integer id, VeterinariaAdminDTO dto) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
        
        tenant.setNombreComercial(dto.getNombre());
        tenant.setEmailContacto(dto.getEmail());
        tenant.setTelefono(dto.getTelefono());
        tenant.setDireccion(dto.getDireccion());
        tenant.setEstado("ACTIVO".equals(dto.getEstado()) ? Tenant.EstadoTenant.ACTIVO : Tenant.EstadoTenant.INACTIVO);
        tenant.setFechaActualizacion(LocalDateTime.now());
        
        Tenant updated = tenantRepository.save(tenant);
        return toVeterinariaAdminDTO(updated);
    }

    @Transactional
    public void deleteVeterinaria(Integer id) {
        if (!tenantRepository.existsById(id)) {
            throw new RuntimeException("Veterinaria no encontrada");
        }
        tenantRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioAdminDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toUsuarioAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioAdminDTO> getUsuariosByVeterinaria(Integer veterinariaId) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getTenant() != null && u.getTenant().getIdTenant().equals(veterinariaId))
                .map(this::toUsuarioAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioAdminDTO updateUsuarioEstado(Integer usuarioId, String estado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(Usuario.EstadoUsuario.valueOf(estado));
        Usuario updated = usuarioRepository.save(usuario);
        return toUsuarioAdminDTO(updated);
    }

    @Transactional
    public void deleteUsuario(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(usuarioId);
    }

    private VeterinariaAdminDTO toVeterinariaAdminDTO(Tenant tenant) {
        VeterinariaAdminDTO dto = new VeterinariaAdminDTO();
        dto.setIdVeterinaria(tenant.getIdTenant());
        dto.setNombre(tenant.getNombreComercial());
        dto.setEmail(tenant.getEmailContacto());
        dto.setTelefono(tenant.getTelefono());
        dto.setDireccion(tenant.getDireccion());
        dto.setPlan(tenant.getPlanActual() != null ? tenant.getPlanActual().getNombre() : "SIN PLAN");
        dto.setEstado(tenant.getEstado().toString());
        dto.setFechaRegistro(tenant.getFechaRegistro());
        dto.setFechaExpiracion(null); // Implementar lógica de expiración si es necesario
        dto.setUltimoPago(tenant.getFechaActivacion()); // Placeholder - implementar lógica real
        dto.setMontoMensual(tenant.getPlanActual() != null && tenant.getPlanActual().getPrecioMensual() != null ? tenant.getPlanActual().getPrecioMensual().doubleValue() : 0.0);
        return dto;
    }

    private UsuarioAdminDTO toUsuarioAdminDTO(Usuario usuario) {
        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        
        if (usuario.getTenant() != null) {
            dto.setIdVeterinaria(usuario.getTenant().getIdTenant());
            dto.setVeterinariaNombre(usuario.getTenant().getNombreComercial());
        }
        
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setNombres(usuario.getNombres());
        dto.setApellidos(usuario.getApellidos());
        dto.setTelefono(usuario.getTelefono());
        dto.setEstado(usuario.getEstado().toString());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        
        // Obtener rol desde la relación
        if (usuario.getRol() != null) {
            dto.setRol(usuario.getRol().getNombre());
        } else {
            dto.setRol("SIN ROL");
        }
        
        return dto;
    }
}
