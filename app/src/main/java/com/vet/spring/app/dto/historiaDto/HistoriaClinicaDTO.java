package com.vet.spring.app.dto.historiaDto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HistoriaClinicaDTO {
    private Integer idHistoria;
    private Integer idVeterinaria;
    private Integer idMascota;
    private Integer idDoctor;
    private LocalDateTime fechaAtencion;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
}
