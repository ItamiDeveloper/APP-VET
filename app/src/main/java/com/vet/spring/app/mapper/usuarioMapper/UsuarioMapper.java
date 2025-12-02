package com.vet.spring.app.mapper.usuarioMapper;

import com.vet.spring.app.dto.usuarioDto.UsuarioDTO;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.entity.usuario.Rol;
import com.vet.spring.app.entity.veterinaria.Veterinaria;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario e) {
        if (e == null) return null;
        UsuarioDTO d = new UsuarioDTO();
        d.setIdUsuario(e.getIdUsuario());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdRol(e.getRol() == null ? null : e.getRol().getIdRol());
        d.setUsername(e.getUsername());
        d.setEmail(e.getEmail());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Usuario toEntity(UsuarioDTO d) {
        if (d == null) return null;
        Usuario e = new Usuario();
        e.setIdUsuario(d.getIdUsuario());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdRol() != null) { Rol r = new Rol(); r.setIdRol(d.getIdRol()); e.setRol(r); }
        e.setUsername(d.getUsername());
        e.setEmail(d.getEmail());
        return e;
    }
}
