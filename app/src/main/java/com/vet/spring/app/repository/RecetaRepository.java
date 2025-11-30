package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.historia.Receta;

public interface RecetaRepository extends JpaRepository<Receta, Integer> {

}
