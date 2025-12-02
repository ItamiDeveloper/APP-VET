package com.vet.spring.app.entity.cliente;

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
@Table(name = "CLIENTE")
@Getter @Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    @ManyToOne
    @JoinColumn(name = "id_veterinaria", nullable = false)
    private Veterinaria veterinaria;

    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String documento;
    private String telefono;
    private String email;
    private String direccion;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
