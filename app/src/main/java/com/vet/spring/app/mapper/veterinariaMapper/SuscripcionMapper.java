package com.vet.spring.app.mapper.veterinariaMapper;

import com.vet.spring.app.dto.veterinariaDto.SuscripcionDTO;
import com.vet.spring.app.entity.veterinaria.Suscripcion;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.plan.Plan;
import com.vet.spring.app.entity.veterinaria.Estado;

public class SuscripcionMapper {

    public static SuscripcionDTO toDTO(Suscripcion e) {
        if (e == null) return null;
        SuscripcionDTO d = new SuscripcionDTO();
        d.setIdSuscripcion(e.getIdSuscripcion());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdPlan(e.getPlan() == null ? null : e.getPlan().getIdPlan());
        d.setFechaInicio(e.getFechaInicio());
        d.setFechaFin(e.getFechaFin());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Suscripcion toEntity(SuscripcionDTO d) {
        if (d == null) return null;
        Suscripcion e = new Suscripcion();
        e.setIdSuscripcion(d.getIdSuscripcion());
        if (d.getIdVeterinaria() != null) {
            Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v);
        }
        if (d.getIdPlan() != null) {
            Plan p = new Plan(); p.setIdPlan(d.getIdPlan()); e.setPlan(p);
        }
        e.setFechaInicio(d.getFechaInicio());
        e.setFechaFin(d.getFechaFin());
        if (d.getEstado() != null) e.setEstado(Estado.valueOf(d.getEstado()));
        return e;
    }
}
