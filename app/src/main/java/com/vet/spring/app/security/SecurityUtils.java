package com.vet.spring.app.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    
    /**
     * Obtiene el ID de la veterinaria del usuario autenticado
     * @return idVeterinaria o null si no está autenticado
     */
    public static Integer getCurrentVeterinariaId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getTenantId();
        }
        return null;
    }
    
    /**
     * Obtiene el ID del usuario autenticado
     * @return idUsuario o null si no está autenticado
     */
    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        }
        return null;
    }
    
    /**
     * Verifica si el usuario tiene un rol específico
     * @param role nombre del rol (ej: "ROLE_ADMIN")
     * @return true si tiene el rol
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(role));
        }
        return false;
    }
}
