package com.vet.spring.app.mapper.citaMapper;

import com.vet.spring.app.dto.citaDto.CitaDTO;
import com.vet.spring.app.entity.cita.Cita;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.doctor.Doctor;

public class CitaMapper {

    public static CitaDTO toDTO(Cita e) {
        if (e == null) return null;
        CitaDTO d = new CitaDTO();
        d.setIdCita(e.getIdCita());
        d.setIdTenant(e.getTenant() == null ? null : e.getTenant().getIdTenant());
        d.setIdMascota(e.getMascota() == null ? null : e.getMascota().getIdMascota());
        d.setIdCliente(e.getCliente() == null ? null : e.getCliente().getIdCliente());
        d.setIdDoctor(e.getDoctor() == null ? null : e.getDoctor().getIdDoctor());
        d.setFechaHora(e.getFechaHora());
        d.setDuracionMinutos(e.getDuracionMinutos());
        d.setMotivo(e.getMotivo());
        d.setObservaciones(e.getObservaciones());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Cita toEntity(CitaDTO d) {
        if (d == null) return null;
        Cita e = new Cita();
        e.setIdCita(d.getIdCita());
        if (d.getIdTenant() != null) { Tenant t = new Tenant(); t.setIdTenant(d.getIdTenant()); e.setTenant(t); }
        if (d.getIdMascota() != null) { Mascota m = new Mascota(); m.setIdMascota(d.getIdMascota()); e.setMascota(m); }
        if (d.getIdCliente() != null) { Cliente c = new Cliente(); c.setIdCliente(d.getIdCliente()); e.setCliente(c); }
        if (d.getIdDoctor() != null) { Doctor dr = new Doctor(); dr.setIdDoctor(d.getIdDoctor()); e.setDoctor(dr); }
        e.setFechaHora(d.getFechaHora());
        e.setDuracionMinutos(d.getDuracionMinutos());
        e.setMotivo(d.getMotivo());
        e.setObservaciones(d.getObservaciones());
        if (d.getEstado() != null) e.setEstado(Enum.valueOf(Cita.CitaEstado.class, d.getEstado()));
        return e;
    }
}
