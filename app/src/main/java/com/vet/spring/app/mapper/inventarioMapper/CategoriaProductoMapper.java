package com.vet.spring.app.mapper.inventarioMapper;

import com.vet.spring.app.dto.inventarioDto.CategoriaProductoDTO;
import com.vet.spring.app.entity.inventario.CategoriaProducto;

public class CategoriaProductoMapper {

    public static CategoriaProductoDTO toDTO(CategoriaProducto e) {
        if (e == null) return null;
        CategoriaProductoDTO d = new CategoriaProductoDTO();
        d.setIdCategoria(e.getIdCategoria());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public static CategoriaProducto toEntity(CategoriaProductoDTO d) {
        if (d == null) return null;
        CategoriaProducto e = new CategoriaProducto();
        e.setIdCategoria(d.getIdCategoria());
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        return e;
    }
}
