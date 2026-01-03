package com.vet.spring.app.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripcion")
@Getter @Setter
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_suscripcion")
    private Integer idSuscripcion;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "tipo_pago", length = 20)
    private String tipoPago;

    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSuscripcion estado = EstadoSuscripcion.ACTIVA;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum EstadoSuscripcion {
        ACTIVA, VENCIDA, CANCELADA
    }

    // Métodos helper (sin notas, no existe en BD)
    public boolean estaActiva() {
        LocalDate hoy = LocalDate.now();
        return estado == EstadoSuscripcion.ACTIVA && 
               !hoy.isBefore(fechaInicio) && 
               !hoy.isAfter(fechaFin);
    }

    public boolean estaPorVencer(int diasAntes) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaAlerta = fechaFin.minusDays(diasAntes);
        return hoy.isAfter(fechaAlerta) && hoy.isBefore(fechaFin);
    }
}
