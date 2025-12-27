package com.vet.spring.app.dto.citaDto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CitaDTO {
    private Integer idCita;
    private Integer idTenant;
    private Integer idMascota;
    private Integer idCliente;
    private Integer idDoctor;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private String motivo;
    private String observaciones;
    private String estado;
}
