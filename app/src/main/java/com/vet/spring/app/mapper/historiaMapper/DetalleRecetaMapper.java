package com.vet.spring.app.mapper.historiaMapper;

import com.vet.spring.app.dto.historiaDto.DetalleRecetaDTO;
import com.vet.spring.app.entity.historia.DetalleReceta;
import com.vet.spring.app.entity.historia.Receta;
import com.vet.spring.app.entity.inventario.Producto;

public class DetalleRecetaMapper {

    public static DetalleRecetaDTO toDTO(DetalleReceta e) {
        if (e == null) return null;
        DetalleRecetaDTO d = new DetalleRecetaDTO();
        d.setIdDetalleReceta(e.getIdDetalleReceta());
        d.setIdReceta(e.getReceta() == null ? null : e.getReceta().getIdReceta());
        d.setIdProducto(e.getProducto() == null ? null : e.getProducto().getIdProducto());
        d.setDosis(e.getDosis());
        d.setDuracion(e.getDuracion());
        d.setIndicaciones(e.getIndicaciones());
        return d;
    }

    public static DetalleReceta toEntity(DetalleRecetaDTO d) {
        if (d == null) return null;
        DetalleReceta e = new DetalleReceta();
        e.setIdDetalleReceta(d.getIdDetalleReceta());
        if (d.getIdReceta() != null) { Receta r = new Receta(); r.setIdReceta(d.getIdReceta()); e.setReceta(r); }
        if (d.getIdProducto() != null) { Producto p = new Producto(); p.setIdProducto(d.getIdProducto()); e.setProducto(p); }
        e.setDosis(d.getDosis());
        e.setDuracion(d.getDuracion());
        e.setIndicaciones(d.getIndicaciones());
        return e;
    }
}
