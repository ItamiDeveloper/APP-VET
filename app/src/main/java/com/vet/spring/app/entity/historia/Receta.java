package com.vet.spring.app.entity.historia;

import java.time.LocalDateTime;
import com.vet.spring.app.entity.doctor.Doctor;
import com.vet.spring.app.entity.mascota.Mascota;
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
@Table(name = "RECETA")
@Getter @Setter
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReceta;

    @ManyToOne
    @JoinColumn(name = "id_historia", nullable = false)
    private HistoriaClinica historia;

    @ManyToOne
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    private LocalDateTime fechaEmision;
    private String observaciones;
    private RecetaEstado estado;
}
