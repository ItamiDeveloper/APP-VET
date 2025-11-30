package com.vet.spring.app.dto.veterinariaDto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuditoriaDTO {
    private Integer idAuditoria;
    private Integer idUsuario;
    private String tablaAfectada;
    private Integer idRegistro;
    private String accion;
    private LocalDateTime fechaHora;
    private String detalle;
}
