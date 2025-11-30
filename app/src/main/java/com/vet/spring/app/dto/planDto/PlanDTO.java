package com.vet.spring.app.dto.planDto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlanDTO {
    private Integer idPlan;
    private String nombre;
    private BigDecimal precioMensual;
    private Integer maxDoctores;
    private Integer maxMascotas;
    private Integer maxAlmacenamientoMb;
    private String estado;
}
