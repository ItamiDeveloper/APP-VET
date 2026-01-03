package com.vet.spring.app.repository.mascotaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.vet.spring.app.entity.mascota.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
	
	@Query("SELECT COUNT(m) FROM Mascota m WHERE m.tenant.idTenant = :tenantId")
	long countByIdTenant(Integer tenantId);
}
