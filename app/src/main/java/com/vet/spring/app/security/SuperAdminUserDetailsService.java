package com.vet.spring.app.security;

import com.vet.spring.app.entity.tenant.SuperAdmin;
import com.vet.spring.app.repository.tenantRepository.SuperAdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Servicio para cargar detalles de Super Administradores
 */
@Service("superAdminUserDetailsService")
public class SuperAdminUserDetailsService implements UserDetailsService {

    private final SuperAdminRepository superAdminRepository;

    public SuperAdminUserDetailsService(SuperAdminRepository superAdminRepository) {
        this.superAdminRepository = superAdminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SuperAdmin superAdmin = superAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Super Admin no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                superAdmin.getUsername(),
                superAdmin.getPasswordHash(),
                superAdmin.getEstado() == SuperAdmin.EstadoSuperAdmin.ACTIVO,
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                getAuthorities()
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        // Super Admin tiene un rol especial con todos los permisos
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
    }
}
