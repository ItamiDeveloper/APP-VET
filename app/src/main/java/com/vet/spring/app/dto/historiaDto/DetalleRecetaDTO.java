package com.vet.spring.app.dto.historiaDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalleRecetaDTO {
    private Integer idDetalleReceta;
    private Integer idReceta;
    private Integer idProducto;
    private String dosis;
    private String duracion;
    private String indicaciones;
}
