package com.vet.spring.app.entity.mascota;

import java.time.LocalDate;
import com.vet.spring.app.entity.cliente.Cliente;
import com.vet.spring.app.entity.veterinaria.Veterinaria;
import com.vet.spring.app.entity.veterinaria.Estado;
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
@Table(name = "MASCOTA")
@Getter @Setter
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMascota;

    @ManyToOne
    @JoinColumn(name = "id_veterinaria", nullable = false)
    private Veterinaria veterinaria;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_raza", nullable = false)
    private Raza raza;

    private String nombre;
    private String sexo;
    private LocalDate fechaNacimiento;
    private String color;

    private Estado estado;
}
