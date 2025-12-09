package com.vet.spring.app.dto.reporteDto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumenVentasDTO {
    private Long totalVentas;
    private BigDecimal totalIngresos;
    private BigDecimal promedioVenta;
    private Long ventasPagadas;
    private Long ventasPendientes;
    private Long ventasCanceladas;
}
