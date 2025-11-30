package com.vet.spring.app.mapper.ventaMapper;

import com.vet.spring.app.dto.ventaDto.DetalleVentaDTO;
import com.vet.spring.app.entity.venta.DetalleVenta;
import com.vet.spring.app.entity.venta.Venta;
import com.vet.spring.app.entity.inventario.Producto;

public class DetalleVentaMapper {

    public static DetalleVentaDTO toDTO(DetalleVenta e) {
        if (e == null) return null;
        DetalleVentaDTO d = new DetalleVentaDTO();
        d.setIdDetalleVenta(e.getIdDetalleVenta());
        d.setIdVenta(e.getVenta() == null ? null : e.getVenta().getIdVenta());
        d.setIdProducto(e.getProducto() == null ? null : e.getProducto().getIdProducto());
        d.setCantidad(e.getCantidad());
        d.setPrecioUnitario(e.getPrecioUnitario());
        d.setSubtotal(e.getSubtotal());
        return d;
    }

    public static DetalleVenta toEntity(DetalleVentaDTO d) {
        if (d == null) return null;
        DetalleVenta e = new DetalleVenta();
        e.setIdDetalleVenta(d.getIdDetalleVenta());
        if (d.getIdVenta() != null) { Venta v = new Venta(); v.setIdVenta(d.getIdVenta()); e.setVenta(v); }
        if (d.getIdProducto() != null) { Producto p = new Producto(); p.setIdProducto(d.getIdProducto()); e.setProducto(p); }
        e.setCantidad(d.getCantidad());
        e.setPrecioUnitario(d.getPrecioUnitario());
        e.setSubtotal(d.getSubtotal());
        return e;
    }
}
