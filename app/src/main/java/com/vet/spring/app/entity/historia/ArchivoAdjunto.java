package com.vet.spring.app.entity.historia;

import java.time.LocalDateTime;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
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
@Table(name = "ARCHIVO_ADJUNTO")
@Getter @Setter
public class ArchivoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idArchivo;

    @ManyToOne
    @JoinColumn(name = "id_veterinaria", nullable = false)
    private Veterinaria veterinaria;

    @ManyToOne
    @JoinColumn(name = "id_historia", nullable = false)
    private HistoriaClinica historia;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    private String rutaArchivo;
    private String tipo;
    private String descripcion;
    private LocalDateTime fechaSubida;
}
