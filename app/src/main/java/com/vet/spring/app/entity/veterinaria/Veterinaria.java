package com.vet.spring.app.entity.veterinaria;

import com.vet.spring.app.entity.plan.Plan;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "VETERINARIA")
@Getter @Setter
public class Veterinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVeterinaria;

    @ManyToOne
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;

    private String nombre;
    private String ruc;
    private String telefono;
    private String direccion;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
