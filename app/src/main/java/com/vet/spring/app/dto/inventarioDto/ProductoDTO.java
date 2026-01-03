package com.vet.spring.app.dto.inventarioDto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductoDTO {
    private Integer idProducto;
    private Integer idCategoria;
    private String nombreCategoria;
    private String nombre;
    private String descripcion;
    private Boolean esMedicamento;
    private BigDecimal precioUnitario;
    private String estado;
}
