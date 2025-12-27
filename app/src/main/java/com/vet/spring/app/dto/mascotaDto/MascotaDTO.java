package com.vet.spring.app.dto.mascotaDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MascotaDTO {
    private Integer idMascota;
    private Integer idTenant;
    private Integer idCliente;
    private Integer idRaza;
    private String nombre;
    private String sexo;
    private LocalDate fechaNacimiento;
    private String color;
    private BigDecimal pesoKg;
    private String microchip;
    private String observaciones;
    private String estado;
}
