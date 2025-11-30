package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.inventario.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

}
