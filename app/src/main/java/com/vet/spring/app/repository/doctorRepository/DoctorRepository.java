package com.vet.spring.app.repository.doctorRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vet.spring.app.entity.doctor.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
	
	@Query("SELECT COUNT(d) FROM Doctor d WHERE d.tenant.idTenant = :tenantId")
	long countByIdTenant(Integer tenantId);
	
	@Query("SELECT d FROM Doctor d WHERE d.tenant.idTenant = :tenantId")
	List<Doctor> findByTenantId(@Param("tenantId") Integer tenantId);
	
	@Query("SELECT d FROM Doctor d WHERE d.idDoctor = :id AND d.tenant.idTenant = :tenantId")
	Optional<Doctor> findByIdAndTenantId(@Param("id") Integer id, @Param("tenantId") Integer tenantId);
}
