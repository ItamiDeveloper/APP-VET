package com.vet.spring.app.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroVeterinariaDTO {
    
    @NotNull(message = "El plan es requerido")
    private Integer idPlan;
    
    // Datos de la veterinaria
    @NotBlank(message = "El nombre de la veterinaria es requerido")
    private String nombreVeterinaria;
    
    @NotBlank(message = "El RUC es requerido")
    private String ruc;
    
    @NotBlank(message = "El teléfono es requerido")
    private String telefono;
    
    @NotBlank(message = "La dirección es requerida")
    private String direccion;
    
    // Datos del doctor/administrador
    @NotBlank(message = "Los nombres son requeridos")
    private String nombresDoctor;
    
    @NotBlank(message = "Los apellidos son requeridos")
    private String apellidosDoctor;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "Email inválido")
    private String email;
    
    @NotBlank(message = "El username es requerido")
    private String username;
    
    @NotBlank(message = "El número de colegiatura es requerido")
    private String colegiatura;
    
    private String especialidad;
}
