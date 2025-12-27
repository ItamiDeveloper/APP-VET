package com.vet.spring.app.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Getter @Setter
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Integer idPlan;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMensual;

    @Column(name = "precio_anual", precision = 10, scale = 2)
    private BigDecimal precioAnual;

    @Column(name = "max_usuarios", nullable = false)
    private Integer maxUsuarios = 5;

    @Column(name = "max_doctores", nullable = false)
    private Integer maxDoctores = 5;

    @Column(name = "max_mascotas", nullable = false)
    private Integer maxMascotas = 100;

    @Column(name = "max_almacenamiento_mb", nullable = false)
    private Integer maxAlmacenamientoMb = 1024;

    @Column(name = "tiene_reportes_avanzados", nullable = false)
    private Boolean tieneReportesAvanzados = false;

    @Column(name = "tiene_api_acceso", nullable = false)
    private Boolean tieneApiAcceso = false;

    @Column(name = "tiene_soporte_prioritario", nullable = false)
    private Boolean tieneSoportePrioritario = false;

    @Column(name = "orden_visualizacion", nullable = false)
    private Integer ordenVisualizacion = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPlan estado = EstadoPlan.ACTIVO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public enum EstadoPlan {
        ACTIVO, INACTIVO
    }
}
