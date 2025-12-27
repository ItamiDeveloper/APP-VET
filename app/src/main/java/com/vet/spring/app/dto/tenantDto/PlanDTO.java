package com.vet.spring.app.dto.tenantDto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class PlanDTO {
    private Integer idPlan;
    private String nombre;
    private String descripcion;
    private BigDecimal precioMensual;
    private BigDecimal precioAnual;
    private Integer maxUsuarios;
    private Integer maxDoctores;
    private Integer maxMascotas;
    private Integer maxAlmacenamientoMb;
    private Boolean tieneReportesAvanzados;
    private Boolean tieneApiAcceso;
    private Boolean tieneSoportePrioritario;
    private Integer ordenVisualizacion;
    private String estado;
}
