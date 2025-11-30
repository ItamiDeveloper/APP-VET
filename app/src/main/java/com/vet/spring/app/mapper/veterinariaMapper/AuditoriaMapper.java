package com.vet.spring.app.mapper.veterinariaMapper;

import com.vet.spring.app.dto.veterinariaDto.AuditoriaDTO;
import com.vet.spring.app.entity.veterinaria.Auditoria;
import com.vet.spring.app.entity.usuario.Usuario;

public class AuditoriaMapper {

    public static AuditoriaDTO toDTO(Auditoria e) {
        if (e == null) return null;
        AuditoriaDTO d = new AuditoriaDTO();
        d.setIdAuditoria(e.getIdAuditoria());
        d.setIdUsuario(e.getUsuario() == null ? null : e.getUsuario().getIdUsuario());
        d.setTablaAfectada(e.getTablaAfectada());
        d.setIdRegistro(e.getIdRegistro());
        d.setAccion(e.getAccion());
        d.setFechaHora(e.getFechaHora());
        d.setDetalle(e.getDetalle());
        return d;
    }

    public static Auditoria toEntity(AuditoriaDTO d) {
        if (d == null) return null;
        Auditoria e = new Auditoria();
        e.setIdAuditoria(d.getIdAuditoria());
        if (d.getIdUsuario() != null) { Usuario u = new Usuario(); u.setIdUsuario(d.getIdUsuario()); e.setUsuario(u); }
        e.setTablaAfectada(d.getTablaAfectada());
        e.setIdRegistro(d.getIdRegistro());
        e.setAccion(d.getAccion());
        e.setFechaHora(d.getFechaHora());
        e.setDetalle(d.getDetalle());
        return e;
    }
}
