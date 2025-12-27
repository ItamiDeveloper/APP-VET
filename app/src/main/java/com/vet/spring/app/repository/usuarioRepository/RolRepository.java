package com.vet.spring.app.repository.usuarioRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.usuario.Rol;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    Optional<Rol> findByNombre(String nombre);
}
