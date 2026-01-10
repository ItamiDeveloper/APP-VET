package com.vet.spring.app.service.tenantService;

import com.vet.spring.app.dto.usuarioDto.UsuarioDTO;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.usuario.Rol;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.exception.ResourceNotFoundException;
import com.vet.spring.app.mapper.usuarioMapper.UsuarioMapper;
import com.vet.spring.app.repository.tenantRepository.TenantRepository;
import com.vet.spring.app.repository.usuarioRepository.RolRepository;
import com.vet.spring.app.repository.usuarioRepository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TenantRepository tenantRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Obtener todos los usuarios de un tenant
     */
    public List<UsuarioDTO> getAllUsuariosByTenant(Integer tenantId) {
        List<Usuario> usuarios = usuarioRepository.findByTenantId(tenantId);
        return usuarios.stream()
            .map(usuarioMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener un usuario por ID
     */
    public UsuarioDTO getUsuarioById(Integer id, Integer tenantId) {
        Usuario usuario = usuarioRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        return usuarioMapper.toDTO(usuario);
    }

    /**
     * Crear un nuevo usuario
     */
    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO dto, Integer tenantId) {
        // Verificar que el tenant existe
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con ID: " + tenantId));

        // Verificar que el rol existe
        Rol rol = rolRepository.findById(dto.getIdRol())
            .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + dto.getIdRol()));

        // Verificar que username es único en el tenant
        boolean existeUsername = usuarioRepository.findByTenantId(tenantId)
            .stream()
            .anyMatch(u -> u.getUsername().equalsIgnoreCase(dto.getUsername()));
        
        if (existeUsername) {
            throw new IllegalArgumentException("El username ya existe en este tenant");
        }

        // Verificar que email es único en el tenant
        boolean existeEmail = usuarioRepository.findByTenantId(tenantId)
            .stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()));
        
        if (existeEmail) {
            throw new IllegalArgumentException("El email ya existe en este tenant");
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setTenant(tenant);
        usuario.setRol(rol);
        usuario.setUsername(dto.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword())); // Encriptar password
        usuario.setEmail(dto.getEmail());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setUltimoAcceso(null);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(savedUsuario);
    }

    /**
     * Actualizar un usuario existente
     */
    @Transactional
    public UsuarioDTO updateUsuario(Integer id, UsuarioDTO dto, Integer tenantId) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar que pertenece al tenant
        if (!usuario.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Usuario no encontrado en este tenant");
        }

        // Actualizar rol si cambió
        if (dto.getIdRol() != null && !dto.getIdRol().equals(usuario.getRol().getIdRol())) {
            Rol nuevoRol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + dto.getIdRol()));
            usuario.setRol(nuevoRol);
        }

        // Actualizar campos
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            // Verificar que el nuevo email no exista
            boolean existeEmail = usuarioRepository.findByTenantId(tenantId)
                .stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail())
                    && !u.getIdUsuario().equals(id));
            
            if (existeEmail) {
                throw new IllegalArgumentException("El email ya existe en este tenant");
            }
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getNombres() != null) usuario.setNombres(dto.getNombres());
        if (dto.getApellidos() != null) usuario.setApellidos(dto.getApellidos());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());
        
        // Actualizar password solo si se proporciona uno nuevo
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(updatedUsuario);
    }

    /**
     * Eliminar un usuario (soft delete cambiando estado)
     */
    @Transactional
    public void deleteUsuario(Integer id, Integer tenantId) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar que pertenece al tenant
        if (!usuario.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Usuario no encontrado en este tenant");
        }

        // Soft delete: cambiar estado a INACTIVO
        usuario.setEstado(Usuario.EstadoUsuario.INACTIVO);
        usuarioRepository.save(usuario);
    }

    /**
     * Cambiar estado de un usuario
     */
    @Transactional
    public UsuarioDTO cambiarEstado(Integer id, String nuevoEstado, Integer tenantId) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar que pertenece al tenant
        if (!usuario.getTenant().getIdTenant().equals(tenantId)) {
            throw new ResourceNotFoundException("Usuario no encontrado en este tenant");
        }

        try {
            usuario.setEstado(Usuario.EstadoUsuario.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(updatedUsuario);
    }

    /**
     * Obtener usuarios por rol
     */
    public List<UsuarioDTO> getUsuariosByRol(Integer idRol, Integer tenantId) {
        List<Usuario> usuarios = usuarioRepository.findByTenantId(tenantId)
            .stream()
            .filter(u -> u.getRol().getIdRol().equals(idRol))
            .collect(Collectors.toList());
        
        return usuarios.stream()
            .map(usuarioMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener usuarios activos
     */
    public List<UsuarioDTO> getUsuariosActivos(Integer tenantId) {
        List<Usuario> usuarios = usuarioRepository.findByTenantId(tenantId)
            .stream()
            .filter(u -> u.getEstado() == Usuario.EstadoUsuario.ACTIVO)
            .collect(Collectors.toList());
        
        return usuarios.stream()
            .map(usuarioMapper::toDTO)
            .collect(Collectors.toList());
    }
}
