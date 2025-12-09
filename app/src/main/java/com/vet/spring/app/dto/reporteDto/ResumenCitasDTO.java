package com.vet.spring.app.dto.reporteDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumenCitasDTO {
    private Long totalCitas;
    private Long citasProgramadas;
    private Long citasCompletadas;
    private Long citasCanceladas;
    private Long citasHoy;
}
