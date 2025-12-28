package com.vet.spring.app.mapper.inventarioMapper;

import com.vet.spring.app.dto.inventarioDto.ProductoDTO;
import com.vet.spring.app.entity.inventario.Producto;
import com.vet.spring.app.entity.inventario.CategoriaProducto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDTO toDTO(Producto e) {
        if (e == null) return null;
        ProductoDTO d = new ProductoDTO();
        d.setIdProducto(e.getIdProducto());
        d.setIdCategoria(e.getCategoria() == null ? null : e.getCategoria().getIdCategoria());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        d.setEsMedicamento(e.getEsMedicamento());
        d.setPrecioUnitario(e.getPrecioUnitario());
        d.setEstado(e.getEstado() == null ? null : e.getEstado().name());
        return d;
    }

    public Producto toEntity(ProductoDTO d) {
        if (d == null) return null;
        Producto e = new Producto();
        e.setIdProducto(d.getIdProducto());
        if (d.getIdCategoria() != null) { CategoriaProducto c = new CategoriaProducto(); c.setIdCategoria(d.getIdCategoria()); e.setCategoria(c); }
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        e.setEsMedicamento(d.getEsMedicamento());
        e.setPrecioUnitario(d.getPrecioUnitario());
        return e;
    }
}
