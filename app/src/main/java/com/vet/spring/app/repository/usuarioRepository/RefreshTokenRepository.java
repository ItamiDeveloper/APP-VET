package com.vet.spring.app.repository.usuarioRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.vet.spring.app.entity.usuario.RefreshToken;
import com.vet.spring.app.entity.usuario.Usuario;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUsuario(Usuario usuario);
    
    @Modifying
    int deleteByUsuario(Usuario usuario);
}
