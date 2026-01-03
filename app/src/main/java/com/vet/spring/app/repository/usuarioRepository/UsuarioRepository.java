package com.vet.spring.app.repository.usuarioRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.vet.spring.app.entity.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByUsername(String username);
	
	@Query("SELECT COUNT(u) FROM Usuario u WHERE u.tenant.idTenant = :tenantId")
	long countByIdTenant(Integer tenantId);
}
