package com.vet.spring.app.mapper.clienteMapper;

import com.vet.spring.app.dto.clienteDto.ClienteDTO;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.tenant.Tenant;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente e) {
        if (e == null) return null;
        ClienteDTO d = new ClienteDTO();
        d.setIdCliente(e.getIdCliente());
        d.setIdTenant(e.getTenant() == null ? null : e.getTenant().getIdTenant());
        d.setNombres(e.getNombres());
        d.setApellidos(e.getApellidos());
        d.setTipoDocumento(e.getTipoDocumento());
        d.setNumeroDocumento(e.getNumeroDocumento());
        d.setTelefono(e.getTelefono());
        d.setEmail(e.getEmail());
        d.setDireccion(e.getDireccion());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public Cliente toEntity(ClienteDTO d) {
        if (d == null) return null;
        Cliente e = new Cliente();
        e.setIdCliente(d.getIdCliente());
        if (d.getIdTenant() != null) { Tenant t = new Tenant(); t.setIdTenant(d.getIdTenant()); e.setTenant(t); }
        e.setNombres(d.getNombres());
        e.setApellidos(d.getApellidos());
        e.setTipoDocumento(d.getTipoDocumento());
        e.setNumeroDocumento(d.getNumeroDocumento());
        e.setTelefono(d.getTelefono());
        e.setEmail(d.getEmail());
        e.setDireccion(d.getDireccion());
        if (d.getEstado() != null) e.setEstado(Enum.valueOf(Cliente.EstadoCliente.class, d.getEstado()));
        return e;
    }
}
