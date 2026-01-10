package com.vet.spring.app.dto.superadminDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminStatsDTO {
    private Long totalVeterinarias;
    private Long totalUsuarios;
    private Double ingresosmes;
    private Long suscripcionesActivas;
    private Double tendenciaMensual;
}
