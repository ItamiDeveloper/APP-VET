package com.vet.spring.app.mapper.mascotaMapper;

import com.vet.spring.app.dto.mascotaDto.RazaDTO;
import com.vet.spring.app.entity.mascota.Raza;
import com.vet.spring.app.entity.mascota.Especie;

public class RazaMapper {

    public static RazaDTO toDTO(Raza e) {
        if (e == null) return null;
        RazaDTO d = new RazaDTO();
        d.setIdRaza(e.getIdRaza());
        d.setIdEspecie(e.getEspecie() == null ? null : e.getEspecie().getIdEspecie());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        return d;
    }

    public static Raza toEntity(RazaDTO d) {
        if (d == null) return null;
        Raza e = new Raza();
        e.setIdRaza(d.getIdRaza());
        if (d.getIdEspecie() != null) { Especie s = new Especie(); s.setIdEspecie(d.getIdEspecie()); e.setEspecie(s); }
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        return e;
    }
}
