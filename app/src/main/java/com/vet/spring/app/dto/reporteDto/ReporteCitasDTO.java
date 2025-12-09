package com.vet.spring.app.dto.reporteDto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteCitasDTO {
    private Integer idCita;
    private String mascota;
    private String cliente;
    private String doctor;
    private LocalDateTime fechaHora;
    private String motivo;
    private String estado;
}
