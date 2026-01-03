package com.vet.spring.app.repository.doctorRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.vet.spring.app.entity.doctor.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
	
	@Query("SELECT COUNT(d) FROM Doctor d WHERE d.tenant.idTenant = :tenantId")
	long countByIdTenant(Integer tenantId);
}
