package com.vet.spring.app.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vet.spring.app.entity.usuario.Usuario;

public class UserDetailsImpl implements UserDetails {

    private final Usuario user;

    public UserDetailsImpl(Usuario user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRol() == null || user.getRol().getNombre() == null) return Collections.emptyList();
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRol().getNombre()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash() == null ? null : user.getPasswordHash().trim();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return user.getEstado() != null; }

    public Integer getId() { return user.getIdUsuario(); }
    
    public Integer getTenantId() { 
        return user.getTenant() != null ? user.getTenant().getIdTenant() : null; 
    }
    
    public Usuario getUsuario() { return user; }
}
