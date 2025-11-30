package com.vet.spring.app.repository.inventarioRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.inventario.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

}
