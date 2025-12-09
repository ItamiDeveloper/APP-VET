package com.vet.spring.app.dto.reporteDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentasDTO {
    private Integer idVenta;
    private String cliente;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
    private Integer cantidadProductos;
}
