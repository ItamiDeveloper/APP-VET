package com.vet.spring.app.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroVeterinariaResponseDTO {
    private Integer idVeterinaria;
    private String nombreVeterinaria;
    private String username;
    private String passwordTemporal;
    private String email;
    private String mensaje;
}
