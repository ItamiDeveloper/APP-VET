package com.vet.spring.app.entity.compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vet.spring.app.entity.common.Proveedor;
import com.vet.spring.app.entity.tenant.Tenant;
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
@Table(name = "COMPRA")
@Getter @Setter
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompra;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado; // PENDIENTE,PAGADA,ANULADA
}
