package com.vet.spring.app.mapper.inventarioMapper;

import com.vet.spring.app.dto.inventarioDto.InventarioDTO;
import com.vet.spring.app.entity.inventario.Inventario;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.inventario.Producto;

public class InventarioMapper {

    public static InventarioDTO toDTO(Inventario e) {
        if (e == null) return null;
        InventarioDTO d = new InventarioDTO();
        d.setIdInventario(e.getIdInventario());
        d.setIdVeterinaria(e.getVeterinaria() == null ? null : e.getVeterinaria().getIdVeterinaria());
        d.setIdProducto(e.getProducto() == null ? null : e.getProducto().getIdProducto());
        d.setStockActual(e.getStockActual());
        d.setStockMinimo(e.getStockMinimo());
        d.setStockMaximo(e.getStockMaximo());
        return d;
    }

    public static Inventario toEntity(InventarioDTO d) {
        if (d == null) return null;
        Inventario e = new Inventario();
        e.setIdInventario(d.getIdInventario());
        if (d.getIdVeterinaria() != null) { Veterinaria v = new Veterinaria(); v.setIdVeterinaria(d.getIdVeterinaria()); e.setVeterinaria(v); }
        if (d.getIdProducto() != null) { Producto p = new Producto(); p.setIdProducto(d.getIdProducto()); e.setProducto(p); }
        e.setStockActual(d.getStockActual());
        e.setStockMinimo(d.getStockMinimo());
        e.setStockMaximo(d.getStockMaximo());
        return e;
    }
}
