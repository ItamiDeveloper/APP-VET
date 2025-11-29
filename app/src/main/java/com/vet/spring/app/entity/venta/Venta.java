package com.vet.spring.app.entity.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.cliente.Cliente;
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
@Table(name = "VENTA")
@Getter @Setter
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;

    @ManyToOne
    @JoinColumn(name = "id_veterinaria", nullable = false)
    private Veterinaria veterinaria;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    private LocalDateTime fecha;
    private BigDecimal total;
    private String metodoPago;
    private String estado; // PENDIENTE,PAGADA,ANULADA
}
