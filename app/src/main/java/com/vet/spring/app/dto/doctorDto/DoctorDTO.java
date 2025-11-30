package com.vet.spring.app.dto.doctorDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DoctorDTO {
    private Integer idDoctor;
    private Integer idVeterinaria;
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String colegiatura;
    private String especialidad;
    private String estado;
}
