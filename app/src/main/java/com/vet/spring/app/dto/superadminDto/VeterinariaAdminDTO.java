package com.vet.spring.app.dto.superadminDto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class VeterinariaAdminDTO {
    private Integer idVeterinaria;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String plan;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaExpiracion;
    private LocalDateTime ultimoPago;
    private Double montoMensual;
}
