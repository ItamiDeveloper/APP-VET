package com.vet.spring.app.entity.cita;

import java.time.LocalDateTime;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.doctor.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cita")
@Getter @Setter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;
    
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos = 30;
    
    @Column(length = 255)
    private String motivo;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CitaEstado estado = CitaEstado.PENDIENTE;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum CitaEstado {
        PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA, NO_ASISTIO
    }
}
