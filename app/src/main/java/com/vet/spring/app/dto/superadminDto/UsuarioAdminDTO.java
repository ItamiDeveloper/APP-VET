package com.vet.spring.app.dto.superadminDto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioAdminDTO {
    private Integer idUsuario;
    private Integer idVeterinaria;
    private String veterinariaNombre;
    private String username;
    private String email;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String rol;
    private String estado;
    private LocalDateTime fechaCreacion;
}
