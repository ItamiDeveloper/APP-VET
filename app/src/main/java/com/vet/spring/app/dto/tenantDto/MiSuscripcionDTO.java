package com.vet.spring.app.dto.tenantDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiSuscripcionDTO {
    // Información del plan actual
    private Integer idPlan;
    private String nombrePlan;
    private String descripcionPlan;
    private BigDecimal precioMensual;
    private BigDecimal precioAnual;
    
    // Estado de la suscripción
    private String estadoSuscripcion; // TRIAL, ACTIVO, SUSPENDIDO, CANCELADO
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate proximoPago;
    
    // Límites del plan
    private Integer maxUsuarios;
    private Integer maxDoctores;
    private Integer maxMascotas;
    private Integer maxAlmacenamientoMb;
    
    // Uso actual
    private Integer usuariosActivos;
    private Integer doctoresActivos;
    private Integer mascotasRegistradas;
    private Integer almacenamientoUsadoMb;
    
    // Porcentajes de uso
    private Double porcentajeUsuarios;
    private Double porcentajeDoctores;
    private Double porcentajeMascotas;
    private Double porcentajeAlmacenamiento;
    
    // Features del plan
    private Boolean tieneReportesAvanzados;
    private Boolean tieneApiAcceso;
    private Boolean tieneSoportePrioritario;
    
    // Días restantes de trial
    private Integer diasRestantesTrial;
    private Boolean enPeriodoTrial;
}
