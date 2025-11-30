package com.vet.spring.app.mapper.historiaMapper;

import com.vet.spring.app.dto.historiaDto.HistoriaClinicaDTO;
import com.vet.spring.app.entity.historia.HistoriaClinica;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.doctor.Doctor;

public class HistoriaClinicaMapper {

    public static HistoriaClinicaDTO toDTO(HistoriaClinica e) {
        if (e == null) return null;
        HistoriaClinicaDTO d = new HistoriaClinicaDTO();
        d.setIdHistoria(e.getIdHistoria());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdMascota(e.getMascota() == null ? null : e.getMascota().getIdMascota());
        d.setIdDoctor(e.getDoctor() == null ? null : e.getDoctor().getIdDoctor());
        d.setFechaAtencion(e.getFechaAtencion());
        d.setDiagnostico(e.getDiagnostico());
        d.setTratamiento(e.getTratamiento());
        d.setObservaciones(e.getObservaciones());
        return d;
    }

    public static HistoriaClinica toEntity(HistoriaClinicaDTO d) {
        if (d == null) return null;
        HistoriaClinica e = new HistoriaClinica();
        e.setIdHistoria(d.getIdHistoria());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdMascota() != null) { Mascota m = new Mascota(); m.setIdMascota(d.getIdMascota()); e.setMascota(m); }
        if (d.getIdDoctor() != null) { Doctor dr = new Doctor(); dr.setIdDoctor(d.getIdDoctor()); e.setDoctor(dr); }
        e.setFechaAtencion(d.getFechaAtencion());
        e.setDiagnostico(d.getDiagnostico());
        e.setTratamiento(d.getTratamiento());
        e.setObservaciones(d.getObservaciones());
        return e;
    }
}
