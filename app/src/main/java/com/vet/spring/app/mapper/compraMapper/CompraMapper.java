package com.vet.spring.app.mapper.compraMapper;

import com.vet.spring.app.dto.compraDto.CompraDTO;
import com.vet.spring.app.entity.compra.Compra;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.inventario.Proveedor;

public class CompraMapper {

    public static CompraDTO toDTO(Compra e) {
        if (e == null) return null;
        CompraDTO d = new CompraDTO();
        d.setIdCompra(e.getIdCompra());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdProveedor(e.getProveedor() == null ? null : e.getProveedor().getIdProveedor());
        d.setFecha(e.getFecha());
        d.setTotal(e.getTotal());
        d.setEstado(e.getEstado());
        return d;
    }

    public static Compra toEntity(CompraDTO d) {
        if (d == null) return null;
        Compra e = new Compra();
        e.setIdCompra(d.getIdCompra());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdProveedor() != null) { Proveedor p = new Proveedor(); p.setIdProveedor(d.getIdProveedor()); e.setProveedor(p); }
        e.setFecha(d.getFecha());
        e.setTotal(d.getTotal());
        e.setEstado(d.getEstado());
        return e;
    }
}
