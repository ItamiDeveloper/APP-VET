package com.vet.spring.app.mapper.historiaMapper;

import com.vet.spring.app.dto.historiaDto.ArchivoAdjuntoDTO;
import com.vet.spring.app.entity.historia.ArchivoAdjunto;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.historia.HistoriaClinica;
import com.vet.spring.app.entity.mascota.Mascota;

public class ArchivoAdjuntoMapper {

    public static ArchivoAdjuntoDTO toDTO(ArchivoAdjunto e) {
        if (e == null) return null;
        ArchivoAdjuntoDTO d = new ArchivoAdjuntoDTO();
        d.setIdArchivo(e.getIdArchivo());
        d.setIdTenant(e.getTenant() == null ? null : e.getTenant().getIdTenant());
        d.setIdHistoria(e.getHistoria() == null ? null : e.getHistoria().getIdHistoria());
        d.setIdMascota(e.getMascota() == null ? null : e.getMascota().getIdMascota());
        d.setRutaArchivo(e.getRutaArchivo());
        d.setTipo(e.getTipo());
        d.setDescripcion(e.getDescripcion());
        d.setFechaSubida(e.getFechaSubida());
        return d;
    }

    public static ArchivoAdjunto toEntity(ArchivoAdjuntoDTO d) {
        if (d == null) return null;
        ArchivoAdjunto e = new ArchivoAdjunto();
        e.setIdArchivo(d.getIdArchivo());
        if (d.getIdTenant() != null) { Tenant t = new Tenant(); t.setIdTenant(d.getIdTenant()); e.setTenant(t); }
        if (d.getIdHistoria() != null) { HistoriaClinica h = new HistoriaClinica(); h.setIdHistoria(d.getIdHistoria()); e.setHistoria(h); }
        if (d.getIdMascota() != null) { Mascota m = new Mascota(); m.setIdMascota(d.getIdMascota()); e.setMascota(m); }
        e.setRutaArchivo(d.getRutaArchivo());
        e.setTipo(d.getTipo());
        e.setDescripcion(d.getDescripcion());
        e.setFechaSubida(d.getFechaSubida());
        return e;
    }
}
