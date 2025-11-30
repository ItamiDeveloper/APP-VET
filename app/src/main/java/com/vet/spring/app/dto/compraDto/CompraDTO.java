package com.vet.spring.app.dto.compraDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompraDTO {
    private Integer idCompra;
    private Integer idVeterinaria;
    private Integer idProveedor;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
}
