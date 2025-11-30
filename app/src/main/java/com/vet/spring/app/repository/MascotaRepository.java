package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.mascota.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Integer> {

}
