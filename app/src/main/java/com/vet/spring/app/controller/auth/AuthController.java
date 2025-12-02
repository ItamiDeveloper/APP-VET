package com.vet.spring.app.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.auth.JwtResponse;
import com.vet.spring.app.dto.auth.LoginRequest;
import com.vet.spring.app.dto.auth.RefreshTokenRequest;
import com.vet.spring.app.dto.usuarioDto.UsuarioDTO;
import com.vet.spring.app.entity.usuario.RefreshToken;
import com.vet.spring.app.entity.usuario.Usuario;
import com.vet.spring.app.entity.veterinaria.Estado;
import com.vet.spring.app.mapper.usuarioMapper.UsuarioMapper;
import com.vet.spring.app.repository.usuarioRepository.UsuarioRepository;
import com.vet.spring.app.security.JwtUtil;
import com.vet.spring.app.security.UserDetailsImpl;
import com.vet.spring.app.service.usuarioService.RefreshTokenService;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                         RefreshTokenService refreshTokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        
        return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), userDetails.getUsername()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUsuario)
            .map(usuario -> {
                String token = jwtUtil.generateToken(new UserDetailsImpl(usuario));
                return ResponseEntity.ok(new JwtResponse(token, requestRefreshToken, usuario.getUsername()));
            })
            .orElseThrow(() -> new RuntimeException("Refresh token no encontrado"));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioDTO dto) {
        Usuario u = UsuarioMapper.toEntity(dto);
        u.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        u.setEstado(Estado.ACTIVO); // Establecer estado por defecto
        Usuario saved = usuarioRepository.save(u);
        return ResponseEntity.ok(UsuarioMapper.toDTO(saved));
    }
}
