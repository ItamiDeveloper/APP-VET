package com.vet.spring.app.dto.razaDto;

import lombok.Data;

@Data
public class RazaDTO {
    private Integer idRaza;
    private Integer idEspecie;
    private String nombre;
    private String descripcion;
    private String especieNombre; // Para mostrar en el frontend
}
