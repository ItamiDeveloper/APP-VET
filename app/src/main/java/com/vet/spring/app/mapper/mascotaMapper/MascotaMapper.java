package com.vet.spring.app.mapper.mascotaMapper;

import com.vet.spring.app.dto.mascotaDto.MascotaDTO;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.mascota.Raza;

public class MascotaMapper {

    public static MascotaDTO toDTO(Mascota e) {
        if (e == null) return null;
        MascotaDTO d = new MascotaDTO();
        d.setIdMascota(e.getIdMascota());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdCliente(e.getCliente() == null ? null : e.getCliente().getIdCliente());
        d.setIdRaza(e.getRaza() == null ? null : e.getRaza().getIdRaza());
        d.setNombre(e.getNombre());
        d.setSexo(e.getSexo());
        d.setFechaNacimiento(e.getFechaNacimiento());
        d.setColor(e.getColor());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Mascota toEntity(MascotaDTO d) {
        if (d == null) return null;
        Mascota e = new Mascota();
        e.setIdMascota(d.getIdMascota());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdCliente() != null) { Cliente c = new Cliente(); c.setIdCliente(d.getIdCliente()); e.setCliente(c); }
        if (d.getIdRaza() != null) { Raza r = new Raza(); r.setIdRaza(d.getIdRaza()); e.setRaza(r); }
        e.setNombre(d.getNombre());
        e.setSexo(d.getSexo());
        e.setFechaNacimiento(d.getFechaNacimiento());
        e.setColor(d.getColor());
        return e;
    }
}
