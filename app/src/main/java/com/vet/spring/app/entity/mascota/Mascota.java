package com.vet.spring.app.entity.mascota;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.tenant.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mascota")
@Getter @Setter
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Integer idMascota;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_raza", nullable = false)
    private Raza raza;

    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 20)
    private String sexo = "MACHO";
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(length = 50)
    private String color;
    
    @Column(name = "peso_kg", precision = 5, scale = 2)
    private BigDecimal pesoKg;
    
    @Column(length = 50)
    private String microchip;
    
    @Column(name = "foto_url")
    private String fotoUrl;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMascota estado = EstadoMascota.ACTIVO;
    
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public enum EstadoMascota {
        ACTIVO, FALLECIDO, ADOPTADO, PERDIDO
    }
}
