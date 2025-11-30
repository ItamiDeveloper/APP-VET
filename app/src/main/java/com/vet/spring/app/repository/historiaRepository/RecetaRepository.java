package com.vet.spring.app.repository.historiaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.historia.Receta;

public interface RecetaRepository extends JpaRepository<Receta, Integer> {

}
