package com.vet.spring.app.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "super_admin")
@Getter @Setter
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_super_admin")
    private Integer idSuperAdmin;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 30)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSuperAdmin estado = EstadoSuperAdmin.ACTIVO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    public enum EstadoSuperAdmin {
        ACTIVO, INACTIVO
    }
}
