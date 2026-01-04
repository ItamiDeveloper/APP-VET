package com.vet.spring.app.dto.compraDto;

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
    private String contacto;
    private String estado;
}
