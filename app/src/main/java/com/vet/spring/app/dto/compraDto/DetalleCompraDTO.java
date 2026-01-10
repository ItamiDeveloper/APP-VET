package com.vet.spring.app.dto.compraDto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalleCompraDTO {
    private Integer idDetalleCompra;
    private Integer idCompra;
    private Integer idProducto;
    private String productoNombre; // Nombre del producto
    private Integer cantidad;
    private BigDecimal precioUnitario; // Renamed from costoUnitario for consistency
    private BigDecimal subtotal;
}
