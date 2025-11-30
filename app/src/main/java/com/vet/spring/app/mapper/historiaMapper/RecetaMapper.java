package com.vet.spring.app.mapper.historiaMapper;

import com.vet.spring.app.dto.historiaDto.RecetaDTO;
import com.vet.spring.app.entity.historia.Receta;
import com.vet.spring.app.entity.historia.RecetaEstado;
import com.vet.spring.app.entity.historia.HistoriaClinica;
import com.vet.spring.app.entity.doctor.Doctor;
import com.vet.spring.app.entity.mascota.Mascota;

public class RecetaMapper {

    public static RecetaDTO toDTO(Receta e) {
        if (e == null) return null;
        RecetaDTO d = new RecetaDTO();
        d.setIdReceta(e.getIdReceta());
        d.setIdHistoria(e.getHistoria() == null ? null : e.getHistoria().getIdHistoria());
        d.setIdDoctor(e.getDoctor() == null ? null : e.getDoctor().getIdDoctor());
        d.setIdMascota(e.getMascota() == null ? null : e.getMascota().getIdMascota());
        d.setFechaEmision(e.getFechaEmision());
        d.setObservaciones(e.getObservaciones());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Receta toEntity(RecetaDTO d) {
        if (d == null) return null;
        Receta e = new Receta();
        e.setIdReceta(d.getIdReceta());
        if (d.getIdHistoria() != null) { HistoriaClinica h = new HistoriaClinica(); h.setIdHistoria(d.getIdHistoria()); e.setHistoria(h); }
        if (d.getIdDoctor() != null) { Doctor dr = new Doctor(); dr.setIdDoctor(d.getIdDoctor()); e.setDoctor(dr); }
        if (d.getIdMascota() != null) { Mascota m = new Mascota(); m.setIdMascota(d.getIdMascota()); e.setMascota(m); }
        e.setFechaEmision(d.getFechaEmision());
        e.setObservaciones(d.getObservaciones());
        if (d.getEstado() != null) e.setEstado(RecetaEstado.valueOf(d.getEstado()));
        return e;
    }
}
