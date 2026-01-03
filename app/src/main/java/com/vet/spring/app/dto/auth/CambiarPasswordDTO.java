package com.vet.spring.app.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambiarPasswordDTO {
    
    @NotBlank(message = "La contraseña actual es requerida")
    private String passwordActual;
    
    @NotBlank(message = "La nueva contraseña es requerida")
    private String passwordNueva;
    
    @NotBlank(message = "La confirmación de contraseña es requerida")
    private String passwordConfirmacion;
}
