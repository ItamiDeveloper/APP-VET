package com.vet.spring.app.repository.tenantRepository;

import com.vet.spring.app.entity.tenant.Tenant;
import com.vet.spring.app.entity.tenant.Tenant.EstadoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {
    
    Optional<Tenant> findByCodigoTenant(String codigoTenant);
    
    boolean existsByCodigoTenant(String codigoTenant);
    
    List<Tenant> findByEstadoSuscripcion(EstadoSuscripcion estadoSuscripcion);
    
    @Query("SELECT t FROM Tenant t WHERE t.estadoSuscripcion = 'ACTIVO' AND t.estado = 'ACTIVO'")
    List<Tenant> findAllActiveTenants();
    
    @Query("SELECT COUNT(t) FROM Tenant t WHERE t.estadoSuscripcion = 'ACTIVO'")
    long countActiveTenants();
}
