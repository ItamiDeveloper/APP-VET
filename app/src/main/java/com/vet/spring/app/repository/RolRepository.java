package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.usuario.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

}
