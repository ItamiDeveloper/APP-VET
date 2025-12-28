package com.vet.spring.app.mapper.inventarioMapper;

import com.vet.spring.app.dto.inventarioDto.InventarioDTO;
import com.vet.spring.app.entity.inventario.Inventario;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.inventario.Producto;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public InventarioDTO toDTO(Inventario e) {
        if (e == null) return null;
        InventarioDTO d = new InventarioDTO();
        d.setIdInventario(e.getIdInventario());
        d.setIdTenant(e.getTenant() == null ? null : e.getTenant().getIdTenant());
        d.setIdProducto(e.getProducto() == null ? null : e.getProducto().getIdProducto());
        d.setStockActual(e.getStockActual());
        d.setStockMinimo(e.getStockMinimo());
        d.setStockMaximo(e.getStockMaximo());
        return d;
    }

    public Inventario toEntity(InventarioDTO d) {
        if (d == null) return null;
        Inventario e = new Inventario();
        e.setIdInventario(d.getIdInventario());
        if (d.getIdTenant() != null) { Tenant t = new Tenant(); t.setIdTenant(d.getIdTenant()); e.setTenant(t); }
        if (d.getIdProducto() != null) { Producto p = new Producto(); p.setIdProducto(d.getIdProducto()); e.setProducto(p); }
        e.setStockActual(d.getStockActual());
        e.setStockMinimo(d.getStockMinimo());
        e.setStockMaximo(d.getStockMaximo());
        return e;
    }
}
