package com.vet.spring.app.repository.compraRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vet.spring.app.entity.compra.Compra;

import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    
    @Query("SELECT c FROM Compra c WHERE c.tenant.idTenant = :tenantId")
    List<Compra> findByTenantId(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT c FROM Compra c WHERE c.idCompra = :id AND c.tenant.idTenant = :tenantId")
    Optional<Compra> findByIdAndTenantId(@Param("id") Integer id, @Param("tenantId") Integer tenantId);

}
