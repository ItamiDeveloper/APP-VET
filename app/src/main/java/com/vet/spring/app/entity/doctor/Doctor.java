package com.vet.spring.app.entity.doctor;

import com.vet.spring.app.entity.usuario.Usuario;
import jakarta.persistence.Entity;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.veterinaria.Estado;
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
@Table(name = "DOCTOR")
@Getter @Setter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDoctor;

    @ManyToOne
    @JoinColumn(name = "id_veterinaria", nullable = false)
    private Veterinaria veterinaria;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String nombres;
    private String apellidos;
    private String colegiatura;
    private String especialidad;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
