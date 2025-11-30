package com.vet.spring.app.repository.inventarioRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.inventario.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

}
