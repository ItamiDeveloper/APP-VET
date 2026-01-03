package com.vet.spring.app.entity.inventario;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "PRODUCTO")
@Getter @Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaProducto categoria;

    private String nombre;
    private String descripcion;
    private Boolean esMedicamento;
    private BigDecimal precioUnitario;
    @Enumerated(EnumType.STRING)
    private EstadoProducto estado;
    
    public enum EstadoProducto {
        ACTIVO, INACTIVO
    }
}
