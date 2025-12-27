package com.vet.spring.app.entity.inventario;

import com.vet.spring.app.entity.common.Estado;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "PROVEEDOR")
@Getter @Setter
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProveedor;

    private String nombre;
    private String ruc;
    private String telefono;
    private String email;
    private String direccion;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
