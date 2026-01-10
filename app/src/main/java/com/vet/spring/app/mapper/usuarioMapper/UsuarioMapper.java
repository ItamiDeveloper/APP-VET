package com.vet.spring.app.mapper.usuarioMapper;

import com.vet.spring.app.dto.usuarioDto.UsuarioDTO;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.entity.usuario.Rol;
import com.vet.spring.app.entity.tenant.Tenant;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario e) {
        if (e == null) return null;
        UsuarioDTO d = new UsuarioDTO();
        d.setIdUsuario(e.getIdUsuario());
        d.setIdTenant(e.getTenant() == null ? null : e.getTenant().getIdTenant());
        d.setIdRol(e.getRol() == null ? null : e.getRol().getIdRol());
        
        // Incluir nombre del rol
        if (e.getRol() != null) {
            d.setRolNombre(e.getRol().getNombre());
        }
        
        // Incluir nombre de la veterinaria (tenant)
        if (e.getTenant() != null) {
            d.setVeterinariaNombre(e.getTenant().getNombreComercial());
        }
        
        d.setUsername(e.getUsername());
        d.setEmail(e.getEmail());
        d.setNombres(e.getNombres());
        d.setApellidos(e.getApellidos());
        d.setTelefono(e.getTelefono());
        d.setAvatarUrl(e.getAvatarUrl());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public Usuario toEntity(UsuarioDTO d) {
        if (d == null) return null;
        Usuario e = new Usuario();
        e.setIdUsuario(d.getIdUsuario());
        if (d.getIdTenant() != null) { Tenant t = new Tenant(); t.setIdTenant(d.getIdTenant()); e.setTenant(t); }
        if (d.getIdRol() != null) { Rol r = new Rol(); r.setIdRol(d.getIdRol()); e.setRol(r); }
        e.setUsername(d.getUsername());
        e.setEmail(d.getEmail());
        return e;
    }
}
