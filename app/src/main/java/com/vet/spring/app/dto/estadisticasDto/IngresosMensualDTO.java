package com.vet.spring.app.dto.estadisticasDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngresosMensualDTO {
    private String mes;
    private Double ingresos;
}
