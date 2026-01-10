package com.vet.spring.app.repository.ventaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vet.spring.app.entity.venta.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    @Query("SELECT v FROM Venta v WHERE v.tenant.idTenant = :tenantId")
    List<Venta> findByTenantId(@Param("tenantId") Integer tenantId);
    
    @Query("SELECT v FROM Venta v WHERE v.idVenta = :id AND v.tenant.idTenant = :tenantId")
    Optional<Venta> findByIdAndTenantId(@Param("id") Integer id, @Param("tenantId") Integer tenantId);

}
