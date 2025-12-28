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
    // totalVeterinarias y totalPlanes removidos - solo para Super Admin
}
