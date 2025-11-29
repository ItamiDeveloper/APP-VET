package com.vet.spring.app.entity.inventario;

import com.vet.spring.app.entity.veterinaria.Estado;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CATEGORIA_PRODUCTO")
@Getter @Setter
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;

    private String nombre;
    private String descripcion;
    private Estado estado;
}
