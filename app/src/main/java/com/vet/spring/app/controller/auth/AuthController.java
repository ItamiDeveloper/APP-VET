package com.vet.spring.app.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.spring.app.dto.auth.JwtResponse;
import com.vet.spring.app.dto.auth.LoginRequest;
import com.vet.spring.app.security.CustomUserDetailsService;
import com.vet.spring.app.security.JwtUtil;
import com.vet.spring.app.security.SuperAdminUserDetailsService;
import com.vet.spring.app.security.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints de login para usuarios y super administradores")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final SuperAdminUserDetailsService superAdminUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(CustomUserDetailsService userDetailsService,
                         SuperAdminUserDetailsService superAdminUserDetailsService,
                         PasswordEncoder passwordEncoder,
                         JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.superAdminUserDetailsService = superAdminUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Login para usuarios de tenant (veterinarias)
     */
    @PostMapping("/tenant/login")
    @Operation(
        summary = "Login de usuario tenant",
        description = "Autenticación para usuarios de veterinarias. Retorna JWT con tenantId."
    )
    public ResponseEntity<JwtResponse> tenantLogin(@RequestBody LoginRequest request) {
        // Cargar usuario desde la tabla usuario
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getUsername());
        
        // Verificar password
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        
        Integer tenantId = userDetails.getTenantId();
        String token = jwtUtil.generateTokenWithTenant(userDetails, tenantId);
        
        return ResponseEntity.ok(new JwtResponse(token, "Bearer", userDetails.getUsername(), tenantId != null ? tenantId.toString() : null));
    }
    
    /**
     * Login para super administradores
     */
    @PostMapping("/super-admin/login")
    @Operation(
        summary = "Login de super administrador",
        description = "Autenticación para super administradores del sistema. Retorna JWT sin tenantId."
    )
    public ResponseEntity<JwtResponse> superAdminLogin(@RequestBody LoginRequest request) {
        // Cargar super admin desde la tabla super_admin
        UserDetails userDetails = superAdminUserDetailsService.loadUserByUsername(request.getUsername());
        
        // DEBUG: Log para verificar qué está pasando
        System.out.println("=== DEBUG SUPER ADMIN LOGIN ===");
        System.out.println("Username from request: " + request.getUsername());
        System.out.println("Password from request: " + request.getPassword());
        System.out.println("Hash from DB: " + userDetails.getPassword());
        System.out.println("Hash length: " + (userDetails.getPassword() != null ? userDetails.getPassword().length() : "null"));
        System.out.println("Password matches: " + passwordEncoder.matches(request.getPassword(), userDetails.getPassword()));
        System.out.println("===============================");
        
        // Verificar password
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        
        String token = jwtUtil.generateTokenForSuperAdmin(userDetails);
        
        return ResponseEntity.ok(new JwtResponse(token, "Bearer", userDetails.getUsername(), null));
    }
}
