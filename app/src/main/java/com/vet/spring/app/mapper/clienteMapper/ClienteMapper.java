package com.vet.spring.app.mapper.clienteMapper;

import com.vet.spring.app.dto.clienteDto.ClienteDTO;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.veterinaria.Veterinaria;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente e) {
        if (e == null) return null;
        ClienteDTO d = new ClienteDTO();
        d.setIdCliente(e.getIdCliente());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setNombres(e.getNombres());
        d.setApellidos(e.getApellidos());
        d.setTipoDocumento(e.getTipoDocumento());
        d.setDocumento(e.getDocumento());
        d.setTelefono(e.getTelefono());
        d.setEmail(e.getEmail());
        d.setDireccion(e.getDireccion());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static Cliente toEntity(ClienteDTO d) {
        if (d == null) return null;
        Cliente e = new Cliente();
        e.setIdCliente(d.getIdCliente());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        e.setNombres(d.getNombres());
        e.setApellidos(d.getApellidos());
        e.setTipoDocumento(d.getTipoDocumento());
        e.setDocumento(d.getDocumento());
        e.setTelefono(d.getTelefono());
        e.setEmail(d.getEmail());
        e.setDireccion(d.getDireccion());
        return e;
    }
}
