package com.vet.spring.app.dto.usuarioDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioDTO {
    private Integer idUsuario;
    private Integer idTenant;
    private Integer idRol;
    private String rolNombre; // Nombre del rol
    private String veterinariaNombre; // Nombre de la veterinaria (tenant)
    private String username;
    private String email;
    private String password; // Solo para crear/actualizar
    private String nombres;
    private String apellidos;
    private String telefono;
    private String avatarUrl;
    private String estado;
}
