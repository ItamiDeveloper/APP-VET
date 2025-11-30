package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.veterinaria.Suscripcion;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {

}
