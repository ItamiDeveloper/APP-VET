package com.vet.spring.app.dto.historiaDto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HistoriaClinicaDTO {
    @NotNull(message = "El campo 'idHistoria' no puede ser nulo")
    private Integer idHistoria;

    @NotNull(message = "El campo 'idVeterinaria' no puede ser nulo")
    private Integer idVeterinaria;

    @NotNull(message = "El campo 'idMascota' no puede ser nulo")
    private Integer idMascota;

    @NotNull(message = "El campo 'idDoctor' no puede ser nulo")
    private Integer idDoctor;

    @NotNull(message = "El campo 'fechaAtencion' no puede ser nulo")
    private LocalDateTime fechaAtencion;

    @NotNull(message = "El campo 'diagnostico' no puede ser nulo")
    @Size(min = 1, max = 255, message = "El campo 'diagnostico' debe tener entre 1 y 255 caracteres")
    private String diagnostico;

    @NotNull(message = "El campo 'tratamiento' no puede ser nulo")
    @Size(min = 1, max = 255, message = "El campo 'tratamiento' debe tener entre 1 y 255 caracteres")
    private String tratamiento;

    private String observaciones;
}
