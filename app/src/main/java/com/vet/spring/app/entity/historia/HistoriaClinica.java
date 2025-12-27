package com.vet.spring.app.entity.historia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.doctor.Doctor;
import com.vet.spring.app.entity.cita.Cita;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "historia_clinica")
@Getter @Setter
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia")
    private Integer idHistoria;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;
    
    @ManyToOne
    @JoinColumn(name = "id_cita")
    private Cita cita;

    @Column(name = "fecha_atencion", nullable = false)
    private LocalDateTime fechaAtencion = LocalDateTime.now();
    
    @Column(name = "motivo_consulta", length = 255)
    private String motivoConsulta;
    
    @Column(columnDefinition = "TEXT")
    private String anamnesis;
    
    @Column(name = "examen_fisico", columnDefinition = "TEXT")
    private String examenFisico;
    
    @Column(columnDefinition = "TEXT")
    private String diagnostico;
    
    @Column(columnDefinition = "TEXT")
    private String tratamiento;
    
    @Column(name = "examenes_solicitados", columnDefinition = "TEXT")
    private String examenesSolicitados;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "proxima_cita")
    private LocalDate proximaCita;
}
