package com.vet.spring.app.dto.inventarioDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InventarioDTO {
    private Integer idInventario;
    private Integer idTenant;
    private Integer idProducto;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
}
