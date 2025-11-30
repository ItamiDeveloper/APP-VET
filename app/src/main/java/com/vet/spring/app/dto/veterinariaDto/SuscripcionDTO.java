package com.vet.spring.app.dto.veterinariaDto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SuscripcionDTO {
    private Integer idSuscripcion;
    private Integer idVeterinaria;
    private Integer idPlan;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}
