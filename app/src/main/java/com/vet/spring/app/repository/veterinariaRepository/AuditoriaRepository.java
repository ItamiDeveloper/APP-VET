package com.vet.spring.app.repository.veterinariaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.veterinaria.Auditoria;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

}
