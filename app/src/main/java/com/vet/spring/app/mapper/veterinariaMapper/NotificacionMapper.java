package com.vet.spring.app.mapper.veterinariaMapper;

import com.vet.spring.app.dto.veterinariaDto.NotificacionDTO;
import com.vet.spring.app.entity.veterinaria.Notificacion;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.entity.veterinaria.Veterinaria;

public class NotificacionMapper {

    public static NotificacionDTO toDTO(Notificacion e) {
        if (e == null) return null;
        NotificacionDTO d = new NotificacionDTO();
        d.setIdNotificacion(e.getIdNotificacion());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdUsuario(e.getUsuario() == null ? null : e.getUsuario().getIdUsuario());
        d.setTitulo(e.getTitulo());
        d.setMensaje(e.getMensaje());
        d.setTipo(e.getTipo());
        d.setFecha(e.getFecha());
        d.setLeido(e.getLeido());
        return d;
    }

    public static Notificacion toEntity(NotificacionDTO d) {
        if (d == null) return null;
        Notificacion e = new Notificacion();
        e.setIdNotificacion(d.getIdNotificacion());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdUsuario() != null) { Usuario u = new Usuario(); u.setIdUsuario(d.getIdUsuario()); e.setUsuario(u); }
        e.setTitulo(d.getTitulo());
        e.setMensaje(d.getMensaje());
        e.setTipo(d.getTipo());
        e.setFecha(d.getFecha());
        e.setLeido(d.getLeido());
        return e;
    }
}
