package com.vet.spring.app.dto.historiaDto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecetaDTO {
    private Integer idReceta;
    private Integer idHistoria;
    private Integer idDoctor;
    private Integer idMascota;
    private LocalDateTime fechaEmision;
    private String observaciones;
    private String estado;
}
