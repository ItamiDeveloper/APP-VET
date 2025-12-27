package com.vet.spring.app.dto.ventaDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VentaDTO {
    private Integer idVenta;
    private Integer idTenant;
    private Integer idCliente;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
}
