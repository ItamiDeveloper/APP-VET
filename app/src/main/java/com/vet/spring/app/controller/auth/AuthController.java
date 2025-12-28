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
     * DEBUG: Verificar hash de password
     */
    @PostMapping("/debug-password")
    public ResponseEntity<?> debugPassword(@RequestBody LoginRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getUsername());
            String hashFromDB = userDetails.getPassword();
            boolean matches = passwordEncoder.matches(request.getPassword(), hashFromDB);
            
            return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
                put("username", request.getUsername());
                put("passwordProvided", request.getPassword());
                put("hashFromDB", hashFromDB);
                put("hashLength", hashFromDB != null ? hashFromDB.length() : 0);
                put("matches", matches);
                put("tenantId", userDetails.getTenantId());
            }});
        } catch (Exception e) {
            return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
                put("error", e.getMessage());
                put("exception", e.getClass().getName());
            }});
        }
    }
    
    /**
     * DEBUG: Generar hash para una contraseña
     */
    @PostMapping("/generate-hash")
    public ResponseEntity<?> generateHash(@RequestBody LoginRequest request) {
        String newHash = passwordEncoder.encode(request.getPassword());
        boolean testMatch = passwordEncoder.matches(request.getPassword(), newHash);
        
        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("password", request.getPassword());
            put("newHash", newHash);
            put("hashLength", newHash.length());
            put("testMatches", testMatch);
            put("sqlUpdate", "UPDATE usuario SET password_hash = '" + newHash + "' WHERE username = '" + request.getUsername() + "';");
        }});
    }
    
    /**
     * Login universal - detecta automáticamente si es tenant o super admin
     */
    @PostMapping("/login")
    @Operation(
        summary = "Login universal",
        description = "Detecta automáticamente si el usuario es tenant o super admin y lo autentica."
    )
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        // Intentar primero como usuario tenant
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getUsername());
            
            if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                Integer tenantId = userDetails.getTenantId();
                String token = jwtUtil.generateTokenWithTenant(userDetails, tenantId);
                return ResponseEntity.ok(new JwtResponse(token, null, userDetails.getUsername(), tenantId != null ? tenantId.toString() : null));
            }
        } catch (Exception e) {
            // Si falla, intentar como super admin
            try {
                UserDetails superAdminDetails = superAdminUserDetailsService.loadUserByUsername(request.getUsername());
                
                if (passwordEncoder.matches(request.getPassword(), superAdminDetails.getPassword())) {
                    String token = jwtUtil.generateTokenForSuperAdmin(superAdminDetails);
                    return ResponseEntity.ok(new JwtResponse(token, null, superAdminDetails.getUsername(), null));
                }
            } catch (Exception ex) {
                // Ignora si no existe como super admin
            }
        }
        
        throw new BadCredentialsException("Credenciales inválidas");
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

        return ResponseEntity.ok(new JwtResponse(token, null, userDetails.getUsername(), tenantId != null ? tenantId.toString() : null));
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
        
        // Verificar password
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        
        String token = jwtUtil.generateTokenForSuperAdmin(userDetails);
        
        return ResponseEntity.ok(new JwtResponse(token, null, userDetails.getUsername(), null));
    }
}
