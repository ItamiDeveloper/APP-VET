package com.vet.spring.app.entity.plan;

import java.math.BigDecimal;

import com.vet.spring.app.entity.veterinaria.Estado;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PLAN")
@Getter @Setter
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlan;

    private String nombre;
    private BigDecimal precioMensual;
    private Integer maxDoctores;
    private Integer maxMascotas;
    private Integer maxAlmacenamientoMb;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}