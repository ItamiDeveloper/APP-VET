package com.vet.spring.app.repository.ventaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.venta.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

}
