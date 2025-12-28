package com.vet.spring.app.mapper.historiaMapper;

import com.vet.spring.app.dto.historiaDto.HistoriaClinicaDTO;
import com.vet.spring.app.entity.historia.HistoriaClinica;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.doctor.Doctor;
import org.springframework.stereotype.Component;

@Component
public class HistoriaClinicaMapper {

    public HistoriaClinicaDTO toDTO(HistoriaClinica e) {
        if (e == null) return null;
        HistoriaClinicaDTO d = new HistoriaClinicaDTO();
        d.setIdHistoria(e.getIdHistoria());
        d.setIdTenant(e.getTenant() == null ? null : e.getTenant().getIdTenant());
        d.setIdMascota(e.getMascota() == null ? null : e.getMascota().getIdMascota());
        d.setIdDoctor(e.getDoctor() == null ? null : e.getDoctor().getIdDoctor());
        d.setFechaAtencion(e.getFechaAtencion());
        d.setDiagnostico(e.getDiagnostico());
        d.setTratamiento(e.getTratamiento());
        d.setObservaciones(e.getObservaciones());
        return d;
    }

    public HistoriaClinica toEntity(HistoriaClinicaDTO d) {
        if (d == null) return null;
        HistoriaClinica e = new HistoriaClinica();
        e.setIdHistoria(d.getIdHistoria());
        if (d.getIdTenant() != null) { Tenant t = new Tenant(); t.setIdTenant(d.getIdTenant()); e.setTenant(t); }
        if (d.getIdMascota() != null) { Mascota m = new Mascota(); m.setIdMascota(d.getIdMascota()); e.setMascota(m); }
        if (d.getIdDoctor() != null) { Doctor dr = new Doctor(); dr.setIdDoctor(d.getIdDoctor()); e.setDoctor(dr); }
        e.setFechaAtencion(d.getFechaAtencion());
        e.setDiagnostico(d.getDiagnostico());
        e.setTratamiento(d.getTratamiento());
        e.setObservaciones(d.getObservaciones());
        return e;
    }
}
