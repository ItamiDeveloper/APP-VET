package com.vet.spring.app.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vet.spring.app.entity.tenant.SuperAdmin;

/**
 * Implementación de UserDetails para SuperAdmin
 */
public class SuperAdminUserDetails implements UserDetails {

    private final SuperAdmin superAdmin;

    public SuperAdminUserDetails(SuperAdmin superAdmin) {
        this.superAdmin = superAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
    }

    @Override
    public String getPassword() {
        return superAdmin.getPasswordHash() != null ? superAdmin.getPasswordHash().trim() : null;
    }

    @Override
    public String getUsername() {
        return superAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return superAdmin.getEstado() == SuperAdmin.EstadoSuperAdmin.ACTIVO;
    }

    public Integer getId() {
        return superAdmin.getIdSuperAdmin();
    }

    public SuperAdmin getSuperAdmin() {
        return superAdmin;
    }
}
