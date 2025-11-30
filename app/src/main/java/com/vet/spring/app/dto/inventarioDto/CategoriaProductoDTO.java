package com.vet.spring.app.dto.inventarioDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoriaProductoDTO {
    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    private String estado;
}
