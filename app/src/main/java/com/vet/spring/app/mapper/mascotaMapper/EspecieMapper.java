package com.vet.spring.app.mapper.mascotaMapper;

import com.vet.spring.app.dto.mascotaDto.EspecieDTO;
import com.vet.spring.app.entity.mascota.Especie;
import org.springframework.stereotype.Component;

@Component
public class EspecieMapper {

    public EspecieDTO toDTO(Especie e) {
        if (e == null) return null;
        EspecieDTO d = new EspecieDTO();
        d.setIdEspecie(e.getIdEspecie());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        return d;
    }

    public Especie toEntity(EspecieDTO d) {
        if (d == null) return null;
        Especie e = new Especie();
        e.setIdEspecie(d.getIdEspecie());
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        return e;
    }
}
