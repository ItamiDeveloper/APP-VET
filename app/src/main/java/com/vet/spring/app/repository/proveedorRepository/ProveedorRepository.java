package com.vet.spring.app.repository.proveedorRepository;

import com.vet.spring.app.entity.common.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    List<Proveedor> findByEstado(String estado);
}
