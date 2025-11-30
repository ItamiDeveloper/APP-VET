package com.vet.spring.app.repository.inventarioRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.inventario.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
