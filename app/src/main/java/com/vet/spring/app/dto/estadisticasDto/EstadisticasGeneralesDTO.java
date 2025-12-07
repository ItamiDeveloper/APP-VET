package com.vet.spring.app.dto.estadisticasDto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasGeneralesDTO {
    private Long totalClientes;
    private Long totalMascotas;
    private Long totalCitas;
    private BigDecimal totalIngresos;
}
