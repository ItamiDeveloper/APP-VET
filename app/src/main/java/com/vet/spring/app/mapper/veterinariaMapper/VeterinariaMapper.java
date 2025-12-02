package com.vet.spring.app.mapper.veterinariaMapper;

import com.vet.spring.app.dto.veterinariaDto.VeterinariaDTO;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.plan.Plan;

public class VeterinariaMapper {

    public static VeterinariaDTO toDTO(Veterinaria e) {
        if (e == null) return null;
        VeterinariaDTO d = new VeterinariaDTO();
        d.setIdVeterinaria(e.getIdVeterinaria());
        d.setIdPlan(e.getPlan() == null ? null : e.getPlan().getIdPlan());
        d.setNombre(e.getNombre());
        d.setRuc(e.getRuc());
        d.setTelefono(e.getTelefono());
        d.setDireccion(e.getDireccion());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Veterinaria toEntity(VeterinariaDTO d) {
        if (d == null) return null;
        Veterinaria e = new Veterinaria();
        e.setIdVeterinaria(d.getIdVeterinaria());
        if (d.getIdPlan() != null) {
            Plan p = new Plan();
            p.setIdPlan(d.getIdPlan());
            e.setPlan(p);
        }
        e.setNombre(d.getNombre());
        e.setRuc(d.getRuc());
        e.setTelefono(d.getTelefono());
        e.setDireccion(d.getDireccion());
        return e;
    }
}
