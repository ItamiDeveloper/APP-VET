package com.vet.spring.app.entity.mascota;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RAZA")
@Getter @Setter
public class Raza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRaza;

    @ManyToOne
    @JoinColumn(name = "id_especie", nullable = false)
    private Especie especie;

    private String nombre;
    private String descripcion;
}
