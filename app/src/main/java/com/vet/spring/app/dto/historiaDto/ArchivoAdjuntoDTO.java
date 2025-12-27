package com.vet.spring.app.dto.historiaDto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArchivoAdjuntoDTO {
    private Integer idArchivo;
    private Integer idTenant;
    private Integer idHistoria;
    private Integer idMascota;
    private String rutaArchivo;
    private String tipo;
    private String descripcion;
    private LocalDateTime fechaSubida;
}
