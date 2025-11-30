package com.vet.spring.app.repository.inventarioRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.inventario.CategoriaProducto;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {

}
