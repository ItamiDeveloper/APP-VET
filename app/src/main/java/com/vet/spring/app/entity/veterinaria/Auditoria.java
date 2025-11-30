package com.vet.spring.app.entity.veterinaria;

import java.time.LocalDateTime;
import com.vet.spring.app.entity.usuario.Usuario;
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
@Table(name = "AUDITORIA")
@Getter @Setter
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAuditoria;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private String tablaAfectada;
    private Integer idRegistro;
    private String accion;
    private LocalDateTime fechaHora;
    private String detalle;
}
