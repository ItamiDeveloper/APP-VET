package com.vet.spring.app.entity.usuario;

import com.vet.spring.app.entity.tenant.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_tenant", "username"}),
           @UniqueConstraint(columnNames = {"id_tenant", "email"})
       })
@Getter @Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(nullable = false, length = 50)
    private String username;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false, length = 100)
    private String nombres;
    
    @Column(nullable = false, length = 100)
    private String apellidos;
    
    @Column(length = 30)
    private String telefono;
    
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    public enum EstadoUsuario {
        ACTIVO, INACTIVO, SUSPENDIDO
    }
}
