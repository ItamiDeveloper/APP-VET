package com.vet.spring.app.repository.tenantRepository;

import com.vet.spring.app.entity.tenant.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Integer> {
    
    Optional<SuperAdmin> findByUsername(String username);
    
    Optional<SuperAdmin> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
