package com.vet.spring.app.entity.compra;

import java.math.BigDecimal;

import com.vet.spring.app.entity.inventario.Producto;

import jakarta.persistence.Column;
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
@Table(name = "DETALLE_COMPRA")
@Getter @Setter
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_compra")
    private Integer idDetalleCompra;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad")
    private Integer cantidad;
    
    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;
    
    @Column(name = "subtotal")
    private BigDecimal subtotal;
}
