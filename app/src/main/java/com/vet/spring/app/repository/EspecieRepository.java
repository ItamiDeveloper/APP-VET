package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.mascota.Especie;

public interface EspecieRepository extends JpaRepository<Especie, Integer> {

}
