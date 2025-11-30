package com.vet.spring.app.repository.citaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.cita.Cita;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

}
