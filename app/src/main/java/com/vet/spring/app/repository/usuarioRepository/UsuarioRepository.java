package com.vet.spring.app.repository.usuarioRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vet.spring.app.entity.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByUsername(String username);
	
	@Query("SELECT COUNT(u) FROM Usuario u WHERE u.tenant.idTenant = :tenantId")
	long countByIdTenant(Integer tenantId);
	
	@Query("SELECT u FROM Usuario u WHERE u.tenant.idTenant = :tenantId")
	List<Usuario> findByTenantId(@Param("tenantId") Integer tenantId);
	
	@Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id AND u.tenant.idTenant = :tenantId")
	Optional<Usuario> findByIdAndTenantId(@Param("id") Integer id, @Param("tenantId") Integer tenantId);
}
