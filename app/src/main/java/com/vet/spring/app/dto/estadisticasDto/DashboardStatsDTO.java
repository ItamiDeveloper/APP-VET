package com.vet.spring.app.dto.estadisticasDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalClientes;
    private Long totalMascotas;
    private Long totalCitas;
    private Double totalIngresos;
    private Long totalVeterinarias;
    private Long totalPlanes;
}
