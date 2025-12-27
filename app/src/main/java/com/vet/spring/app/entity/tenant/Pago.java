package com.vet.spring.app.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Getter @Setter
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_suscripcion")
    private Suscripcion suscripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(name = "referencia_pago", length = 100)
    private String referenciaPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    @Column(name = "fecha_procesamiento")
    private LocalDateTime fechaProcesamiento;

    @Column(columnDefinition = "TEXT")
    private String notas;

    public enum EstadoPago {
        PENDIENTE, COMPLETADO, FALLIDO, REEMBOLSADO
    }
}
