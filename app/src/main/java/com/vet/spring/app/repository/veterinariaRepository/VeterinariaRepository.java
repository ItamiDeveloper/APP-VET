package com.vet.spring.app.repository.veterinariaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.veterinaria.Veterinaria;

public interface VeterinariaRepository extends JpaRepository<Veterinaria, Integer> {

}
