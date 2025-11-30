package com.vet.spring.app.dto.inventarioDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProveedorDTO {
    private Integer idProveedor;
    private String nombre;
    private String ruc;
    private String telefono;
    private String email;
    private String direccion;
    private String estado;
}
