package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.historia.DetalleReceta;

public interface DetalleRecetaRepository extends JpaRepository<DetalleReceta, Integer> {

}
