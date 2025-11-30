package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.mascota.Raza;

public interface RazaRepository extends JpaRepository<Raza, Integer> {

}
