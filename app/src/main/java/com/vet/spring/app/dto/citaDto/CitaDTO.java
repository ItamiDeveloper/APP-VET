package com.vet.spring.app.dto.citaDto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CitaDTO {
    private Integer idCita;
    private Integer idVeterinaria;
    private Integer idMascota;
    private Integer idDoctor;
    private LocalDateTime fechaHora;
    private String motivo;
    private String estado;
}
