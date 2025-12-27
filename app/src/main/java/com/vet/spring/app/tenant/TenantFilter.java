package com.vet.spring.app.tenant;

import com.vet.spring.app.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro que intercepta cada request y extrae el tenantId del JWT
 * para establecerlo en el TenantContext
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public TenantFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                try {
                    // Extraer el tipo de usuario del token
                    String userType = jwtUtil.extractUserType(token);
                    
                    // Si es un usuario normal (no super admin), extraer y establecer el tenantId
                    if ("USUARIO".equals(userType)) {
                        Integer tenantId = jwtUtil.extractTenantId(token);
                        if (tenantId != null) {
                            TenantContext.setTenantId(tenantId);
                        }
                    }
                    // Si es SUPER_ADMIN, no se establece tenant (tiene acceso a todos)
                    
                } catch (Exception e) {
                    // Token inválido o sin información de tenant
                    logger.warn("Error al extraer tenantId del token: " + e.getMessage());
                }
            }
            
            filterChain.doFilter(request, response);
            
        } finally {
            // IMPORTANTE: Limpiar el contexto después del request para evitar memory leaks
            TenantContext.clear();
        }
    }
}
