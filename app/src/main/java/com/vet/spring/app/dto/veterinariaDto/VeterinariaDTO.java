package com.vet.spring.app.dto.veterinariaDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VeterinariaDTO {
    private Integer idVeterinaria;
    private Integer idPlan;
    private String nombre;
    private String ruc;
    private String telefono;
    private String direccion;
    private String estado;
}
