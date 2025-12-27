package com.vet.spring.app.entity.inventario;

import com.vet.spring.app.entity.tenant.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_tenant", "id_producto"}))
@Getter @Setter
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;
    
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 0;
    
    @Column(name = "stock_maximo", nullable = false)
    private Integer stockMaximo = 0;
    
    @Column(name = "fecha_ultimo_ingreso")
    private LocalDateTime fechaUltimoIngreso;
    
    @Column(name = "fecha_ultima_salida")
    private LocalDateTime fechaUltimaSalida;
}
