package com.vet.spring.app.mapper.citaMapper;

import com.vet.spring.app.dto.citaDto.CitaDTO;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.doctor.Doctor;
import com.vet.spring.app.entity.cita.CitaEstado;

public class CitaMapper {

    public static CitaDTO toDTO(Cita e) {
        if (e == null) return null;
        CitaDTO d = new CitaDTO();
        d.setIdCita(e.getIdCita());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdMascota(e.getMascota() == null ? null : e.getMascota().getIdMascota());
        d.setIdDoctor(e.getDoctor() == null ? null : e.getDoctor().getIdDoctor());
        d.setFechaHora(e.getFechaHora());
        d.setMotivo(e.getMotivo());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Cita toEntity(CitaDTO d) {
        if (d == null) return null;
        Cita e = new Cita();
        e.setIdCita(d.getIdCita());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdMascota() != null) { Mascota m = new Mascota(); m.setIdMascota(d.getIdMascota()); e.setMascota(m); }
        if (d.getIdDoctor() != null) { Doctor dr = new Doctor(); dr.setIdDoctor(d.getIdDoctor()); e.setDoctor(dr); }
        e.setFechaHora(d.getFechaHora());
        e.setMotivo(d.getMotivo());
        if (d.getEstado() != null) e.setEstado(CitaEstado.valueOf(d.getEstado()));
        return e;
    }
}
