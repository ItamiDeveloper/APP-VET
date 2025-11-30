package com.vet.spring.app.dto.veterinariaDto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificacionDTO {
    private Integer idNotificacion;
    private Integer idVeterinaria;
    private Integer idUsuario;
    private String titulo;
    private String mensaje;
    private String tipo;
    private LocalDateTime fecha;
    private Boolean leido;
}
