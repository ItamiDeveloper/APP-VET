package com.vet.spring.app.mapper.ventaMapper;

import com.vet.spring.app.dto.ventaDto.VentaDTO;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.cliente.Cliente;

public class VentaMapper {

    public static VentaDTO toDTO(Venta e) {
        if (e == null) return null;
        VentaDTO d = new VentaDTO();
        d.setIdVenta(e.getIdVenta());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdCliente(e.getCliente() == null ? null : e.getCliente().getIdCliente());
        d.setFecha(e.getFecha());
        d.setTotal(e.getTotal());
        d.setMetodoPago(e.getMetodoPago());
        d.setEstado(e.getEstado());
        return d;
    }

    public static Venta toEntity(VentaDTO d) {
        if (d == null) return null;
        Venta e = new Venta();
        e.setIdVenta(d.getIdVenta());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdCliente() != null) { Cliente c = new Cliente(); c.setIdCliente(d.getIdCliente()); e.setCliente(c); }
        e.setFecha(d.getFecha());
        e.setTotal(d.getTotal());
        e.setMetodoPago(d.getMetodoPago());
        e.setEstado(d.getEstado());
        return e;
    }
}
