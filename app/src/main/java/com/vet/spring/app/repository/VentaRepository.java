package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.venta.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

}
