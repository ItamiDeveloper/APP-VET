package com.vet.spring.app.repository.historiaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.historia.HistoriaClinica;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {

}
