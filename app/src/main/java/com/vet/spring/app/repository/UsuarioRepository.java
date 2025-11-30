package com.vet.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vet.spring.app.entity.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}
