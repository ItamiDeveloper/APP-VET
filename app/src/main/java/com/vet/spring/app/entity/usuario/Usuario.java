package com.vet.spring.app.entity.usuario;

import com.vet.spring.app.entity.veterinaria.Veterinaria;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USUARIO")
@Getter @Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_veterinaria", nullable = false)
    private Veterinaria veterinaria;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    private String username;
    private String passwordHash;
    private String email;

    @Enumerated(EnumType.STRING)
    private com.vet.spring.app.entity.veterinaria.Estado estado;
}
