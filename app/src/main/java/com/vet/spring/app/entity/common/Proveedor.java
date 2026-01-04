package com.vet.spring.app.entity.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "proveedor")
@Getter @Setter
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "ruc", length = 20)
    private String ruc;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "contacto", length = 100)
    private String contacto;

    @Column(name = "estado", length = 20)
    private String estado = "ACTIVO"; // ACTIVO, INACTIVO

    public enum EstadoProveedor {
        ACTIVO, INACTIVO
    }
}
