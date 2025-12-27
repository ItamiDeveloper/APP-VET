package com.vet.spring.app.entity.cliente;

import com.vet.spring.app.entity.tenant.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter @Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @Column(nullable = false, length = 100)
    private String nombres;
    
    @Column(nullable = false, length = 100)
    private String apellidos;
    
    @Column(name = "tipo_documento", length = 20)
    private String tipoDocumento = "DNI";
    
    @Column(name = "numero_documento", length = 20)
    private String numeroDocumento;
    
    @Column(length = 30)
    private String telefono;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 255)
    private String direccion;
    
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCliente estado = EstadoCliente.ACTIVO;

    public enum EstadoCliente {
        ACTIVO, INACTIVO
    }
}
