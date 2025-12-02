package com.vet.spring.app.dto.usuarioDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioDTO {
    private Integer idUsuario;
    private Integer idVeterinaria;
    private Integer idRol;
    private String username;
    private String email;
    private String password;
    private String estado;
}
