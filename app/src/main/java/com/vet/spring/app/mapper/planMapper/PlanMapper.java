package com.vet.spring.app.mapper.planMapper;

import com.vet.spring.app.dto.planDto.PlanDTO;
import com.vet.spring.app.entity.plan.Plan;
import com.vet.spring.app.entity.veterinaria.Estado;

public class PlanMapper {

    public static PlanDTO toDTO(Plan e) {
        if (e == null) return null;
        PlanDTO d = new PlanDTO();
        d.setIdPlan(e.getIdPlan());
        d.setNombre(e.getNombre());
        d.setPrecioMensual(e.getPrecioMensual());
        d.setMaxDoctores(e.getMaxDoctores());
        d.setMaxMascotas(e.getMaxMascotas());
        d.setMaxAlmacenamientoMb(e.getMaxAlmacenamientoMb());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Plan toEntity(PlanDTO d) {
        if (d == null) return null;
        Plan e = new Plan();
        e.setIdPlan(d.getIdPlan());
        e.setNombre(d.getNombre());
        e.setPrecioMensual(d.getPrecioMensual());
        e.setMaxDoctores(d.getMaxDoctores());
        e.setMaxMascotas(d.getMaxMascotas());
        e.setMaxAlmacenamientoMb(d.getMaxAlmacenamientoMb());
        if (d.getEstado() != null) e.setEstado(Estado.valueOf(d.getEstado()));
        return e;
    }
}
