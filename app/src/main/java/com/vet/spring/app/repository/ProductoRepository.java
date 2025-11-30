package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.inventario.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
