package com.vet.spring.app.dto.inventarioDto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class InventarioDTO {
    private Integer idInventario;
    private Integer idTenant;
    private Integer idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private BigDecimal precioUnitario;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
}
