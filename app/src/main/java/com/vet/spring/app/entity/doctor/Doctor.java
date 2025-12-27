package com.vet.spring.app.entity.doctor;

import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.entity.tenant.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doctor")
@Getter @Setter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private Integer idDoctor;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombres;
    
    @Column(nullable = false, length = 100)
    private String apellidos;
    
    @Column(length = 50)
    private String colegiatura;
    
    @Column(length = 100)
    private String especialidad;
    
    @Column(length = 30)
    private String telefono;
    
    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDoctor estado = EstadoDoctor.ACTIVO;

    public enum EstadoDoctor {
        ACTIVO, INACTIVO
    }
}
