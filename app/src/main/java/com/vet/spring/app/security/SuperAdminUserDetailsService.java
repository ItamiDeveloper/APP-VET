package com.vet.spring.app.security;

import com.vet.spring.app.entity.tenant.SuperAdmin;
import com.vet.spring.app.repository.tenantRepository.SuperAdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        return new SuperAdminUserDetails(superAdmin);
    }
}
