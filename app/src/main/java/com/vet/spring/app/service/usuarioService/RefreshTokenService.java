package com.vet.spring.app.service.usuarioService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.spring.app.entity.usuario.RefreshToken;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.repository.usuarioRepository.RefreshTokenRepository;
import com.vet.spring.app.repository.usuarioRepository.UsuarioRepository;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration:604800000}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, 
                               UsuarioRepository usuarioRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Integer userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Eliminar token anterior si existe
        refreshTokenRepository.findByUsuario(usuario)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Por favor inicie sesión nuevamente.");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Integer userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return refreshTokenRepository.deleteByUsuario(usuario);
    }
}
