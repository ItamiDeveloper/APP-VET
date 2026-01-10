package com.vet.spring.app.mapper.compraMapper;

import com.vet.spring.app.dto.compraDto.DetalleCompraDTO;
import com.vet.spring.app.entity.compra.Compra;
import com.vet.spring.app.entity.compra.DetalleCompra;
import com.vet.spring.app.entity.inventario.Producto;
import org.springframework.stereotype.Component;

@Component
public class DetalleCompraMapper {

    public DetalleCompraDTO toDTO(DetalleCompra e) {
        if (e == null) return null;
        DetalleCompraDTO d = new DetalleCompraDTO();
        d.setIdDetalleCompra(e.getIdDetalleCompra());
        d.setIdCompra(e.getCompra() == null ? null : e.getCompra().getIdCompra());
        d.setIdProducto(e.getProducto() == null ? null : e.getProducto().getIdProducto());
        d.setCantidad(e.getCantidad());
        d.setPrecioUnitario(e.getPrecioUnitario());
        d.setSubtotal(e.getSubtotal());
        return d;
    }

    public DetalleCompra toEntity(DetalleCompraDTO d) {
        if (d == null) return null;
        DetalleCompra e = new DetalleCompra();
        e.setIdDetalleCompra(d.getIdDetalleCompra());
        if (d.getIdCompra() != null) { Compra c = new Compra(); c.setIdCompra(d.getIdCompra()); e.setCompra(c); }
        if (d.getIdProducto() != null) { Producto p = new Producto(); p.setIdProducto(d.getIdProducto()); e.setProducto(p); }
        e.setCantidad(d.getCantidad());
        e.setPrecioUnitario(d.getPrecioUnitario());
        e.setSubtotal(d.getSubtotal());
        return e;
    }
}
