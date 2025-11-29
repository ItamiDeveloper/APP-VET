package com.vet.spring.app.entity.historia;

import java.time.LocalDateTime;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.mascota.Mascota;
import com.vet.spring.app.entity.doctor.Doctor;
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
@Table(name = "HISTORIA_CLINICA")
@Getter @Setter
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistoria;

    @ManyToOne
    @JoinColumn(name = "id_veterinaria", nullable = false)
    private Veterinaria veterinaria;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    private LocalDateTime fechaAtencion;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
}
